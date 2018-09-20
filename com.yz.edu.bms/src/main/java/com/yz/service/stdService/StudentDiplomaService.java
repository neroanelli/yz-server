package com.yz.service.stdService;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.enroll.BdStdEnrollMapper;
import com.yz.dao.refund.UsInfoMapper;
import com.yz.dao.stdService.StuDiplomaTCMapper;
import com.yz.dao.stdService.StudentDiplomaMapper;
import com.yz.dao.system.SysCityMapper;
import com.yz.dao.system.SysDistrictMapper;
import com.yz.dao.system.SysProvinceMapper;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.message.GwReceiver;
import com.yz.model.stdService.StuDiplomaTC;
import com.yz.model.stdService.StudentDiplomaInfo;
import com.yz.model.stdService.StudentDiplomaQuery;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysDistrict;
import com.yz.model.system.SysProvince;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.JsonUtil;
import com.yz.util.ExcelUtil.IExcelConfig;
import com.yz.util.StringUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 学员服务---毕业证发放任务跟进
 * @author zlp
 */
@Service
public class StudentDiplomaService {
    private static Logger log = LoggerFactory.getLogger(StudentDiplomaService.class);
    
    @Autowired
    private BdStdEnrollMapper stdMapper;
    
    @Autowired
    private UsInfoMapper usInfoMapper;

    @Autowired
    private StudentDiplomaMapper studentDiplomaMapper;
    
	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	@Autowired
	private SysProvinceMapper provinceMapper;

	@Autowired
	private SysCityMapper cityMapper;

	@Autowired
	private SysDistrictMapper districtMapper;
	@Autowired
    private StuDiplomaTCMapper stuDiplomaTCMapper;
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Object findAllDiplomaList(StudentDiplomaQuery query) {
        List<StudentDiplomaInfo> list = studentDiplomaMapper.findAllDiplomaList(query, getUser());
        return new IPageInfo<>((Page) list);
    }

    public StudentDiplomaInfo getDiplomaInfoById(String followId) {
    	StudentDiplomaQuery query = new StudentDiplomaQuery();
        query.setFollowId(followId);
        List<StudentDiplomaInfo> list = studentDiplomaMapper.findAllDiplomaList(query, getUser());
        if (list.size() > 0) {
        	StudentDiplomaInfo info=list.get(0);
        	SysProvince province = provinceMapper.selectByPrimaryKey(info.getReceiveProvince());
			SysCity city = cityMapper.selectByPrimaryKey(info.getReceiveCity());
			SysDistrict district = districtMapper.selectByPrimaryKey(info.getReceiveDistrict());
			if (null != province) {
				info.setReceiveProvinceName(province.getProvinceName());
			}
			if (null != city) {
				info.setReceiveCityName(city.getCityName());
			}
			if (null != district) {
				info.setReceiveDistrictName(district.getDistrictName());
			}
			
			SysProvince mailprovince = provinceMapper.selectByPrimaryKey(info.getProvince());
			SysCity mailcity = cityMapper.selectByPrimaryKey(info.getCity());
			SysDistrict maildistrict = districtMapper.selectByPrimaryKey(info.getDistrict());
			StringBuffer tempMailAddr=new StringBuffer();
			if (null != mailprovince) {
				tempMailAddr.append(mailprovince.getProvinceName());
			}
			if (null != mailcity) {
				tempMailAddr.append(mailcity.getCityName());
			}
			if (null != maildistrict) {
				tempMailAddr.append(maildistrict.getDistrictName());
			}
			if(tempMailAddr.toString().length()>0) {
				info.setProvinceName(tempMailAddr.toString());
			}
			
            return info;
        }
			
        return null;
    }
    
    /**
     * 根据任务Id得到确认时间段列表
     * @param taskId
     * @return
     */
    public List<Map<String, String>> findAffirmTimeList(String taskId) {
    	List<Map<String, String>> result=studentDiplomaMapper.findAffirmTimeList(taskId);
    	result.stream().forEach(v ->{
    		String time=v.get("start_time") + "-" + v.get("end_time");
    		v.put("time", time);
    	} );
        return result;
    }
    
    /**
     * 根据任务Id得到确认时间段列表
     * @param taskId
     * @return
     */
    public List<Map<String, String>> findAffirmDateListByLearnId(StudentDiplomaInfo info) {
    	List<Map<String, String>> result=studentDiplomaMapper.findAffirmDateListByLearnId(info);
    	result.stream().forEach(v ->{
    		String weekStr=DateUtil.getDayCN(DateUtil.convertDateStrToDate(v.get("start_time"), "yyyy-MM-dd"));
    		String date=v.get("start_time") + " " + weekStr;
    		v.put("date", date);
    	} );
        return result;
    }
    
    /**
     * 根据任务Id得到确认时间段列表
     * @param taskId
     * @return
     */
    public List<Map<String, String>> findAffirmTimeListByLearnId(StudentDiplomaInfo info) {
    	List<Map<String, String>> result=studentDiplomaMapper.findAffirmTimeListByLearnId(info);
    	result.stream().forEach(v ->{
    		int yixuan=Integer.parseInt(v.get("number")==null?"0":v.get("number"))-Integer.parseInt(v.get("available_numbers")==null?"0":v.get("available_numbers"));
    		String time=v.get("start_time").replace("AM", "上午").replace("PM", "下午") + "-" + v.get("end_time")+"  上限: "+yixuan+"/"+v.get("number");
    		v.put("time", time);
    	} );
        return result;
    }
    
    @Transactional
    public int updateRemark(String followId, String remark) {
        return studentDiplomaMapper.updateRemark(followId, remark);
    }
    
    @Transactional
    public int resetUnconfirmReason(String followId, String taskId, String learnId) {
        return studentDiplomaMapper.resetTask(followId, taskId, learnId);
    }
    
    @Transactional
    public int resetTask(String followId, String taskId, String learnId) {
        return studentDiplomaMapper.resetTask(followId, taskId, learnId);
    }
    

    @Transactional
    public int editDiplomaInfo(StudentDiplomaInfo info) {
    	return studentDiplomaMapper.editDiplomaInfo(info);
    }
    @Transactional
    public int receiveInfoSet(StudentDiplomaInfo info) {
    	String configIdOld=info.getConfigId();
    	String configId=info.getAffirmEndTime();
    	StuDiplomaTC tcinfo=stuDiplomaTCMapper.getDiplomaTCByConfigId(configId);
    	//info.setAffirmStartTime(tcinfo.getStartTime());
    	//info.setAffirmEndTime(tcinfo.getEndTime());
    	info.setReceiveAddress(tcinfo.getAddress());
    	info.setConfigId(configId);
    	info.setIsAffirm("1");
    	if(!configIdOld.equals("configId")) {
    		 studentDiplomaMapper.receiveInfoSet(configId,configIdOld,info.getLearnId(),info.getTaskId());
    	}
    	return studentDiplomaMapper.editDiplomaInfo(info);
    }
   
    @Transactional
    public int resetUnconfirmReason(String followId,String learnId) {
    	int row=studentDiplomaMapper.resetUnconfirmReason(followId);
    	//推送微信提醒
    	String title = "未确认提醒";
    	String msgName="毕业证发放未确认提醒";
    	String messageTemp="请您收到信息后按操作重新选择毕业证领取时间    审核时间："+ DateUtil.getNow("yyyy-MM-dd HH:mm:ss");
        SendWeChat(learnId, title, messageTemp, msgName);
    	return row;
    }

    /**
     * 得到毕业证发放任务统计信息
     * @return
     */
    public Object getDiplomaStatisticsInfo(String taskId,String tutor) {
    	List<Map<String, Object>> resultList = studentDiplomaMapper.selectDiplomaStatisticsInfo(taskId,tutor);
    	return new IPageInfo<>((Page) resultList);
    }
    
    public Object getReceivePlaceList(StudentDiplomaInfo info) {
    	List<Map<String, Object>> resultList = studentDiplomaMapper.getReceivePlaceList(info);
    	return resultList;
    	
    }
    
    public Object getReceiveAddress(String placeId) {
    	Map<String, String> result = studentDiplomaMapper.getReceiveAddress(placeId);
    	if(result!=null) {
    		SysProvince province = provinceMapper.selectByPrimaryKey(result.get("provinceCode"));
			SysCity city = cityMapper.selectByPrimaryKey(result.get("cityCode"));
			SysDistrict district = districtMapper.selectByPrimaryKey(result.get("districtCode"));
			StringBuffer tempAddress=new StringBuffer();
			if (null != province) {
				tempAddress.append(province.getProvinceName());
			}
			if (null != city) {
				tempAddress.append(city.getCityName());
			}
			if (null != district) {
				tempAddress.append(district.getDistrictName());
			}
			tempAddress.append(result.get("address"));
			result.put("address", tempAddress.toString());
    	}
    	return result;
    	
    }
    
    /** @param query
     * @param response
     */
    @SuppressWarnings("unchecked")
    public void exportDiplomaInfo(StudentDiplomaQuery query, HttpServletResponse response) {
        // 对导出工具进行字段填充
        IExcelConfig<StudentDiplomaInfo> testExcelCofing = new IExcelConfig<StudentDiplomaInfo>();
        testExcelCofing.setSheetName("index").setType(StudentDiplomaInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("学服任务", "taskTitle"))
                .addTitle(new ExcelUtil.IExcelTitle("毕业证编号", "diplomaCode"))
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("是否确认", "isAffirm"))
                .addTitle(new ExcelUtil.IExcelTitle("确认时间", "affirmStartTime")) 
                .addTitle(new ExcelUtil.IExcelTitle("确认地点", "receiveAddress"))
                .addTitle(new ExcelUtil.IExcelTitle("未确认原因", "unconfirmReason"))
                .addTitle(new ExcelUtil.IExcelTitle("是否领取", "receiveStatus"))
                .addTitle(new ExcelUtil.IExcelTitle("领取时间", "receiveTime"))
                .addTitle(new ExcelUtil.IExcelTitle("是否邮寄", "isMail"))
                .addTitle(new ExcelUtil.IExcelTitle("邮寄信息", "addressee"))
                .addTitle(new ExcelUtil.IExcelTitle("快递单号", "logisticsNo"))
                .addTitle(new ExcelUtil.IExcelTitle("发票编号", "invoiceNo"))
                .addTitle(new ExcelUtil.IExcelTitle("班主任", "tutor"))
                .addTitle(new ExcelUtil.IExcelTitle("备注", "remark"))
                .addTitle(new ExcelUtil.IExcelTitle("重置状态", "isReset"));
        
        query.setIsMail("1");
        List<StudentDiplomaInfo> list = studentDiplomaMapper.findAllDiplomaList(query, getUser());

        for (StudentDiplomaInfo diploma : list) {
        	//现场确认结果
            if (diploma.getIsAffirm().equals("0")) {
            	diploma.setIsAffirm("否");
            } else {
            	diploma.setIsAffirm("是");
            } 
            //重置状态结果
            if (diploma.getIsReset().equals("0")) {
            	diploma.setIsReset("否");
            } else {
            	diploma.setIsReset("是");
            } 
            //是否邮寄
            if (diploma.getIsMail().equals("0")) {
            	diploma.setIsMail("否");
            } else {
            	diploma.setIsMail("是");
            } 
            //确认时间
            if(StringUtil.hasValue(diploma.getAffirmStartTime())) {
            	String dateTime=diploma.getAffirmStartTime().replace("AM", "上午").replace("PM", "下午") + "-" + diploma.getAffirmEndTime();
            	diploma.setAffirmStartTime(dateTime);
            }
            
            // 未确认原因转换
			String valueTemple = dictExchangeUtil.getParamKey("unconfirmReason", diploma.getUnconfirmReason());
			diploma.setUnconfirmReason(valueTemple);
			// 领取状态转换
			if(StringUtil.hasValue(diploma.getReceiveStatus())) {
				valueTemple = dictExchangeUtil.getParamKey("diplomaReceiveStatus", diploma.getReceiveStatus());
				diploma.setReceiveStatus(valueTemple);
			}
		
            if (StringUtil.hasValue(diploma.getPfsnLevel()) && diploma.getPfsnLevel().equals("1")) {
            	diploma.setPfsnLevel("1>专科升本科类");
            } else if (StringUtil.hasValue(diploma.getPfsnLevel()) && diploma.getPfsnLevel().equals("5")) {
            	diploma.setPfsnLevel("5>高中起点高职高专");
            } else {
            	diploma.setPfsnLevel("");
            }
            StringBuffer tempAddress=new StringBuffer();
            if(StringUtil.hasValue(diploma.getAddressee())) {
            	tempAddress.append(diploma.getAddressee()+diploma.getMobile()+"\n");
            }
            if(StringUtil.hasValue(diploma.getAddress())) {
            	SysProvince province = provinceMapper.selectByPrimaryKey(diploma.getProvince());
     			SysCity city = cityMapper.selectByPrimaryKey(diploma.getCity());
     			SysDistrict district = districtMapper.selectByPrimaryKey(diploma.getDistrict());
     			if (null != province) {
     				tempAddress.append(province.getProvinceName());
     			}
     			if (null != city) {
     				tempAddress.append(city.getCityName());
     			}
     			if (null != district) {
     				tempAddress.append(district.getDistrictName());
     			}
     			tempAddress.append(diploma.getAddress());
                
            }
            diploma.setAddressee(tempAddress.toString());

        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=stuDiplomaInfo.xls");
            out = response.getOutputStream();
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public void importStudiplomaDataInfo(MultipartFile stuDiplomaImport) {
    	//对导入工具进行字段填充
        IExcelConfig<StudentDiplomaInfo> testExcelCofing = new IExcelConfig<StudentDiplomaInfo>();
        testExcelCofing.setSheetName("index").setType(StudentDiplomaInfo.class)           
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll")) 
                .addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("毕业证编号", "diplomaCode"))
                .addTitle(new ExcelUtil.IExcelTitle("发票编号", "invoiceNo"));

        // 行数记录
        int index = 2;
        try {
            // 对文件进行分析转对象
            List<StudentDiplomaInfo> list = ExcelUtil.importWorkbook(stuDiplomaImport.getInputStream(), testExcelCofing,
            		stuDiplomaImport.getOriginalFilename());
            // 遍历插入
            for (StudentDiplomaInfo diploma : list) {
                
                if (!StringUtil.hasValue(diploma.getStdName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！姓名不能为空");
                }
                if (!StringUtil.hasValue(diploma.getIdCard())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！身份证号不能为空");
                }
                if (!StringUtil.hasValue(diploma.getGrade())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级不能为空");
                }
                index++;

            }
            List<Map<String, Object>> resultList = studentDiplomaMapper.getNonExistsStudent(list);
            if (resultList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("核对以下学员不存在：<br/>");
                for (Map<String, Object> map : resultList) {
                    sb.append("身份证："+map.get("id_card") + "-姓名：" + map.get("std_name") +"-年级：" + map.get("grade")+"<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            BaseUser user = SessionUtil.getUser();
            studentDiplomaMapper.insertByExcel(list, user);
        } catch (IOException e) {
            throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
        }
    	
    	
    }
    
    private BaseUser getUser(){
        BaseUser user = SessionUtil.getUser();
        //没有职称看所有数据
        if(user.getJtList()==null||user.getJtList().size()==0) {
        	user.setUserLevel("1");
        }
        // 部门主任，学籍组长
        if (user.getJtList().contains("BMZR")  || user.getJtList().contains("XJZZ")) {
            user.setUserLevel("1");
        }else if(user.getJtList().contains("CJXJ")&& user.getJtList().contains("GKXJ")) {
        	user.setUserLevel("1");
        }else if(user.getJtList().contains("GKXJ")) {//国开学籍组长看国开的学员数据
        	user.setUserLevel("3");
        }else if(user.getJtList().contains("CJXJ") ) {//成教的学籍组长看成教的学员数据
        	user.setUserLevel("2");
        }else if(user.getJtList().contains("GKXJ")) {//国开学籍组长看国开的学员数据
        	user.setUserLevel("3");
        }else if(user.getJtList().contains("CJZFDY") || user.getJtList().contains("GKZFDY")) {
        	user.setUserLevel("6");
        }
        return user;
    }
    
    /**
     * 推送消息
     *
     * @param learnId
     * @param msg
     */
    private void SendWeChat(String learnId, String title, String msg, String msgName) {
        GwReceiver std = stdMapper.selectStdInfoByLearnId(learnId);
        String openId = usInfoMapper.selectUserOpenId(std.getUserId());
        // 微信推送消息
        WechatMsgVo wechatVo = new WechatMsgVo();
        wechatVo.setTouser(openId);
        wechatVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_STUDENT_INFORM);
        wechatVo.addData("title", title);
        wechatVo.addData("msgName", msgName);
        wechatVo.addData("code", "YZ");
        wechatVo.addData("content", msg);
        wechatVo.setExt1("");
        wechatVo.setIfUseTemplateUlr(false);

        RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(wechatVo));
    }
}
