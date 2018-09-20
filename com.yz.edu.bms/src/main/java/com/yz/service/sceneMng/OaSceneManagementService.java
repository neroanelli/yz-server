package com.yz.service.sceneMng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.sceneMng.OaSceneManagementMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.sceneMng.OaSceneManagement;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;
import com.yz.util.ExcelUtil.IExcelConfig;

@Service
public class OaSceneManagementService{
	
	@Autowired
	private OaSceneManagementMapper sceneManagementMapper;
	
	
	/**
	 * 查询现场确认信息
	 * @param page
	 * @param pageSize
	 * @param sceneManagement
	 * @return
	 */
	public IPageInfo<OaSceneManagement> getScholarshipStory(int page,int pageSize,OaSceneManagement sceneManagement) {
		PageHelper.offsetPage(page, pageSize);
		if(StringUtil.hasValue(sceneManagement.getStartTime())){
			sceneManagement.setStartTime(sceneManagement.getStartTime()+" 00:00:00");
		}
		if(StringUtil.hasValue(sceneManagement.getEndTime())){
			sceneManagement.setEndTime(sceneManagement.getEndTime()+" 23:59:59");
		}
		
		List<OaSceneManagement> sceneManagementList = sceneManagementMapper.getSceneManagement(sceneManagement);
		return new IPageInfo<>((Page<OaSceneManagement>)sceneManagementList);
	} 
	
	public Map<String, Object> getSceneManagementById(String id) {
		return sceneManagementMapper.getSceneManagementById(id);
	} 
	
	@Transactional
	public void insert(OaSceneManagement sceneManagement){
		 if(sceneManagement.getConfirmAddressLevels().length>1){
			 sceneManagement.setConfirmAddressLevel("3");
		 }else{
			 sceneManagement.setConfirmAddressLevel(sceneManagement.getConfirmAddressLevels()[0]);
		 }
		 List<String> dateList = new ArrayList<String>();
		 for (OaSceneManagement s : sceneManagement.getConfigs()) {
			 if(dateList.contains(s.getDate()+s.getStartTime()+s.getEndTime())){
				 throw new IllegalArgumentException("同一确认日期不能存在相同的时间段");
			 }
			 sceneManagement.setAvailableNumbers(s.getNumber()); 
			 sceneManagement.setNumber(s.getNumber());
			 sceneManagement.setStartTime(s.getDate() + " " + s.getStartTime());
			 sceneManagement.setEndTime(s.getDate() + " " + s.getEndTime());
			 dateList.add(s.getDate()+s.getStartTime()+s.getEndTime());
	         sceneManagementMapper.insert(sceneManagement);
	     }
	}
	
	public List<Map<String, String>> getExamDicName() {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = sceneManagementMapper.getExamDicName();
		Map<String, String> map = null;
		if (null != list && !list.isEmpty()) {
			for (Map<String, String> resultMap : list) {
				map = new HashMap<String, String>();
				map.put("dictValue", resultMap.get("ta_id"));
				map.put("dictName", resultMap.get("ta_name"));
				resultList.add(map);
			}

		}
		return resultList;
	}
	
	@Transactional
	public void delete(String confirmationId) {
		sceneManagementMapper.delete(confirmationId);
	}


	@Transactional
	public void deleteByIdArr(String[] confirmationIds){
		sceneManagementMapper.deleteByIdArr(confirmationIds);
	}


	@Transactional
	public void updateStatus(String[] confirmationIds, String status) {
		for (int i = 0; i < confirmationIds.length; i++) {
			if(sceneManagementMapper.findStuConfirmById(confirmationIds[i])>0){
				confirmationIds[i] = null;
			}
		}
		sceneManagementMapper.updateStatus(confirmationIds, status);
	}

	public int findStuConfirmById(String confirmationId) {
		return sceneManagementMapper.findStuConfirmById(confirmationId);
	}
	
	@Transactional
	public void updateSceneManagement(OaSceneManagement sceneManagement){
		if(sceneManagement.getConfirmAddressLevels()!=null){
			if(sceneManagement.getConfirmAddressLevels().length>1){
				 sceneManagement.setConfirmAddressLevel("3");
			 }else{
				 sceneManagement.setConfirmAddressLevel(sceneManagement.getConfirmAddressLevels()[0]);
			 }
		}
		 
		 sceneManagement.setStartTime(sceneManagement.getConfigs().get(0).getDate() + " " + sceneManagement.getConfigs().get(0).getStartTime());
		 sceneManagement.setEndTime(sceneManagement.getConfigs().get(0).getDate() + " " + sceneManagement.getConfigs().get(0).getEndTime());
		 sceneManagement.setNumber(sceneManagement.getConfigs().get(0).getNumber());
		 sceneManagementMapper.updateSceneManagement(sceneManagement);
	}
	
	@Transactional
	public void updateIsAllow(String confirmationId,String status){
		sceneManagementMapper.updateIsAllow(confirmationId, status);
	}

	@SuppressWarnings("unchecked")
    public void uploadSceneManagementData(MultipartFile sceneManagementImport) {
        //对导入工具进行字段填充
        IExcelConfig<OaSceneManagement> testExcelCofing = new IExcelConfig<OaSceneManagement>();
        testExcelCofing.setSheetName("index").setType(OaSceneManagement.class)
                .addTitle(new ExcelUtil.IExcelTitle("考试县区", "taName"))
                .addTitle(new ExcelUtil.IExcelTitle("确认城市", "confirmCity"))
                .addTitle(new ExcelUtil.IExcelTitle("确认点名称", "confirmName"))
                .addTitle(new ExcelUtil.IExcelTitle("详细地址", "address"))
                .addTitle(new ExcelUtil.IExcelTitle("确认学员层次", "confirmAddressLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("所需材料", "requiredMaterials"))
                .addTitle(new ExcelUtil.IExcelTitle("负责人", "chargePerson"))
                .addTitle(new ExcelUtil.IExcelTitle("负责人电话", "chargePersonTel"))
                .addTitle(new ExcelUtil.IExcelTitle("确认日期", "date"))
                .addTitle(new ExcelUtil.IExcelTitle("时间段", "startTime"))
                .addTitle(new ExcelUtil.IExcelTitle("人数容量", "number"));

        // 行数记录
        int index = 2;
        try {
            // 对文件进行分析转对象
        	List<OaSceneManagement> list = new ArrayList<OaSceneManagement>();
			try {
				list = ExcelUtil.importWorkbook(sceneManagementImport.getInputStream(), testExcelCofing,
							sceneManagementImport.getOriginalFilename());
			} catch (IndexOutOfBoundsException e1) {
				
				throw new IllegalArgumentException("模板格式有误,请严格按照下载模板和导入规则导入");
			}
			
            // 遍历插入
            for (OaSceneManagement info : list) {
                if (!StringUtil.hasValue(info.getTaName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！考试县区不能为空");
                }
                if (!StringUtil.hasValue(info.getConfirmCity())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！确认城市不能为空");
                }else{
                	if(info.getConfirmCity().length()>20){
                		throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！确认城市不应超过20个字");
                	}
                }
                if (!StringUtil.hasValue(info.getConfirmName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！确认点名称不能为空");
                }else{
                	if(info.getConfirmName().length()>50){
                		throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！确认城市不应超过50个字");
                	}
                }
                
                if (!StringUtil.hasValue(info.getRequiredMaterials())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！所需材料不能为空");
                }else{
                	if(info.getRequiredMaterials().length()>800){
                		throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！所需材料不应超过800个字");
                	}else{
                		if(info.getRequiredMaterials().indexOf("\n")!=-1){
                			String[] cl = info.getRequiredMaterials().split("\n");
                			StringBuilder str = new StringBuilder();
                			for (int i = 0; i < cl.length; i++) {
                					str.append("<p>" + cl[i] + "</p>");
							}
                			info.setRequiredMaterials(str.toString());
                    	}
                	}
                }
                
                if (!StringUtil.hasValue(info.getAddress())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！详细地址不能为空");
                }else{
                	if(info.getAddress().length()>100){
                		throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！详细地址不能超过100个字");
                	}
                }
                if (StringUtil.hasValue(info.getDate())) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        format.setLenient(false);
                        format.parse(info.getDate());
                    } catch (Exception e) {
                        throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！确认日期格式错误，正确格式如:2018-07-26");
                    }
                } else {
                	throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！确认日期不能为空,且正确格式为：2018-07-26");
                }
                if (StringUtil.hasValue(info.getStartTime())) {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    try {
                    	String[] time = info.getStartTime().split("至");
                        format.setLenient(false);
                        format.parse(time[0].trim());
                        format.parse(time[1].trim());
                        info.setStartTime(info.getDate() + " " + time[0].trim());
                        info.setEndTime(info.getDate() + " " + time[1].trim());
                    } catch (Exception e) {
                        throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！时间段格式错误，正确格式如:12:00 至 12:30");
                    }
                } else {
                	throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！时间段不能为空,且正确格式为：12:00 至 12:30");
                }
                
                if (!StringUtil.hasValue(info.getChargePerson())) {
                	info.setChargePerson(null);
                }
                if (!StringUtil.hasValue(info.getChargePersonTel())) {
                	info.setChargePersonTel(null);
                }
                if (StringUtil.hasValue(info.getConfirmAddressLevel())) {
                    if (info.getConfirmAddressLevel().equals("高起专/专升本")) {
                    	info.setConfirmAddressLevel("3");
                    } else if (info.getConfirmAddressLevel().equals("高起专")) {
                    	info.setConfirmAddressLevel("5");
                    }else if(info.getConfirmAddressLevel().equals("专升本")){
                    	info.setConfirmAddressLevel("1");
                    }
                } else {
                	throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！确认学员层次不能为空");
                }
                
                if (StringUtil.hasValue(info.getNumber())) {
                	int inVal = (int) Math.round(Double.valueOf(info.getNumber()));
                    if (Double.valueOf(info.getNumber()) - inVal == 0) {
                    	info.setNumber(String.valueOf(inVal));
                    }
                	Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
                    if(pattern.matcher(info.getNumber()).matches()){
                    	int num = Integer.valueOf(info.getNumber());
                    	if(num<=0||num>=9999){
                    		throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！人数容量必须是少于4位的正整数且大于0");
                    	}else{
                    		info.setAvailableNumbers(String.valueOf(num));
                    	}
                    }else{
                    	throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！人数容量必须是少于4位的正整数且大于0");
                    }
                } else {
                	throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！人数容量不能为空");
                }
                index++;

            }
            List<Map<String, Object>> resultList = sceneManagementMapper.getNonExistsExamAddress(list);
            if (resultList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("以下考试县区不存在：<br/>");
                for (Map<String, Object> map : resultList) {
                    sb.append(map.get("ta_name") + "<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            BaseUser user = SessionUtil.getUser();
            sceneManagementMapper.insertConfirma(list,user);
        } catch (IOException e) {
        	throw new IllegalArgumentException("模板格式有误,请严格按照下载模板和导入规则导入");
        }
    }
}
