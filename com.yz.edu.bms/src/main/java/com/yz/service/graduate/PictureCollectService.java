package com.yz.service.graduate;

import com.yz.constants.GlobalConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.enroll.BdStdEnrollMapper;
import com.yz.dao.graduate.PictureCollectMapper;
import com.yz.dao.refund.UsInfoMapper;
import com.yz.dao.stdService.StudentXuexinMapper;
import com.yz.dao.system.SysParameterMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.educational.OaTaskTargetStudentQuery;
import com.yz.model.educational.OaTaskStudentInfo;
import com.yz.model.graduate.PictureCollectInfo;
import com.yz.model.graduate.PictureCollectQuery;
import com.yz.model.graduate.PictureCollectWhiteInfo;
import com.yz.model.graduate.PictureCollectWhiteQuery;
import com.yz.model.message.GwReceiver;
import com.yz.model.stdService.StudentXuexinInfo;
import com.yz.model.stdService.StudentXuexinQuery;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.ExcelUtil.IExcelConfig;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import scala.collection.parallel.ParIterableLike;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 毕业服务---毕业证图像采集
 *
 * @author jyt
 */
@Service
public class PictureCollectService {
    private static Logger log = LoggerFactory.getLogger(PictureCollectService.class);

    @Autowired
    private PictureCollectMapper pictureCollectMapper;

    @Autowired
    private SysParameterMapper parameterMapper;

    @Autowired
    private BdStdEnrollMapper stdMapper;

    @Autowired
    private UsInfoMapper usInfoMapper;

    public Object findPictureCollectList(PictureCollectQuery query) {
        BaseUser user =  getUser();
        if (user.getJtList().contains("GKXJ")) {
            query.setRecruitType("2");
            user.setUserLevel("1");
        }
        if (user.getJtList().contains("CJXJ")) {
            query.setRecruitType("1");
            user.setUserLevel("1");
        }
        if (user.getJtList().contains("GKXJ") && user.getJtList().contains("CJXJ")) {
            query.setRecruitType("");
            user.setUserLevel("1");
        }
        List<PictureCollectInfo> list = pictureCollectMapper.findPictureCollectList(query, user);
        return new IPageInfo<>((Page) list);
    }

    @SuppressWarnings("unchecked")
    public void uploadictureCollectWhite(MultipartFile importFile) {
        //对导入工具进行字段填充
        IExcelConfig<PictureCollectInfo> testExcelCofing = new IExcelConfig<PictureCollectInfo>();
        testExcelCofing.setSheetName("index").setType(PictureCollectInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"));

        // 行数记录
        int index = 2;
        try {
            // 对文件进行分析转对象
            List<PictureCollectInfo> list = ExcelUtil.importWorkbook(importFile.getInputStream(), testExcelCofing,
                    importFile.getOriginalFilename());
            // 遍历插入
            for (PictureCollectInfo pictureCollectInfo : list) {
                if (!StringUtil.hasValue(pictureCollectInfo.getStdName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！姓名不能为空");
                }
                if (!StringUtil.hasValue(pictureCollectInfo.getGrade())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级不能为空");
                }
                if (!StringUtil.hasValue(pictureCollectInfo.getIdCard())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！身份证号不能为空");
                }
                index++;

            }
            List<Map<String, Object>> resultList = pictureCollectMapper.getNonExistsWhiteStudent(list);
            if (resultList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("以下学员不存在：<br/>");
                for (Map<String, Object> map : resultList) {
                    sb.append(map.get("id_card") + "-" + map.get("std_name") + "<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            BaseUser user = SessionUtil.getUser();
            pictureCollectMapper.insertWhite(list, user);
        } catch (IOException e) {
            throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
        }
    }

    @SuppressWarnings("unchecked")
    public void uploadPictureCollect(MultipartFile importFile) {
        //对导入工具进行字段填充
        IExcelConfig<PictureCollectInfo> testExcelCofing = new IExcelConfig<PictureCollectInfo>();
        testExcelCofing.setSheetName("index").setType(PictureCollectInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("照片编号", "pictureNo"));

        // 行数记录
        int index = 2;
        try {
            // 对文件进行分析转对象
            List<PictureCollectInfo> list = ExcelUtil.importWorkbook(importFile.getInputStream(), testExcelCofing,
                    importFile.getOriginalFilename());
            // 遍历插入
            for (PictureCollectInfo pictureCollectInfo : list) {
                if (!StringUtil.hasValue(pictureCollectInfo.getStdName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！姓名不能为空");
                }
                if (!StringUtil.hasValue(pictureCollectInfo.getGrade())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级不能为空");
                }
                if (!StringUtil.hasValue(pictureCollectInfo.getIdCard())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！身份证号不能为空");
                }
                if (!StringUtil.hasValue(pictureCollectInfo.getPictureNo())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！照片编号不能为空");
                }
                index++;

            }
            List<Map<String, Object>> resultList = pictureCollectMapper.getNonExistsStudent(list);
            if (resultList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("毕业证图像采集，以下学员不存在：<br/>");
                for (Map<String, Object> map : resultList) {
                    sb.append(map.get("id_card") + "-" + map.get("std_name") + "<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            BaseUser user = SessionUtil.getUser();
            pictureCollectMapper.insert(list, user);
        } catch (IOException e) {
            throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
        }
    }

    public int updateRemark(String picCollectId, String remark) {
        return pictureCollectMapper.updateRemark(picCollectId, remark);
    }

    public int check(List<String> picCollectIds,String checkStatus, String reason) {
        BaseUser user = SessionUtil.getUser();
        List<String> list = pictureCollectMapper.getLearnIds(picCollectIds);
        //检查是否存在有未缴费的学员
        List<String> noPaylist = pictureCollectMapper.getNotPayStudentList(picCollectIds);
        if(noPaylist!=null&&noPaylist.size()>0) {
        	throw new IllegalArgumentException("学员："+noPaylist.toString()+"未缴纳图像采集费用，无法审核！");
        }
        String title = "毕业证图像采集审核";
        for (String learnId : list) {
            SendWeChat(learnId, title, reason + "\n审核时间：" + DateUtil.getNow("yyyy-MM-dd HH:mm:ss"), "毕业证图像采集");
        }
        return pictureCollectMapper.check(picCollectIds, checkStatus,reason,user.getEmpId());
    }
    
    public int revoke(List<String> picCollectIds,String checkStatus) {
        BaseUser user = SessionUtil.getUser();
        return pictureCollectMapper.revoke(picCollectIds, checkStatus,user.getEmpId());
    }

    @SuppressWarnings("unchecked")
    public void exportPictureCollect(PictureCollectQuery query, HttpServletResponse response) {
        // 对导出工具进行字段填充
        IExcelConfig<PictureCollectInfo> testExcelCofing = new IExcelConfig<PictureCollectInfo>();
        testExcelCofing.setSheetName("index").setType(PictureCollectInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("照片编号", "pictureNo"))
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("学历层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("性别", "sex"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号码", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("所在校别", "unvsType"))
                .addTitle(new ExcelUtil.IExcelTitle("院校代码", "unvsCode"))
                .addTitle(new ExcelUtil.IExcelTitle("院校名称", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("班主任", "tutor"))
                .addTitle(new ExcelUtil.IExcelTitle("确认信息是否有误", "infoStatus"))
                .addTitle(new ExcelUtil.IExcelTitle("是否上传相片", "isUploadPicture"))
                .addTitle(new ExcelUtil.IExcelTitle("是否缴费", "isPay"))
                .addTitle(new ExcelUtil.IExcelTitle("审核状态", "checkStatus"))
                .addTitle(new ExcelUtil.IExcelTitle("备注", "remark"));

        BaseUser user =  getUser();
        if (user.getJtList().contains("GKXJ")) {
            query.setRecruitType("2");
            user.setUserLevel("1");
        }
        if (user.getJtList().contains("CJXJ")) {
            query.setRecruitType("1");
            user.setUserLevel("1");
        }
        if (user.getJtList().contains("GKXJ") && user.getJtList().contains("CJXJ")) {
            query.setRecruitType("");
            user.setUserLevel("1");
        }
        List<PictureCollectInfo> list = pictureCollectMapper.findPictureCollectList(query, user);

        for (PictureCollectInfo pictureCollectInfo : list) {

            if (StringUtil.hasValue(pictureCollectInfo.getPfsnLevel()) && pictureCollectInfo.getPfsnLevel().equals("1")) {
                pictureCollectInfo.setPfsnLevel("成人本科");
            } else if (StringUtil.hasValue(pictureCollectInfo.getPfsnLevel()) && pictureCollectInfo.getPfsnLevel().equals("5")) {
                pictureCollectInfo.setPfsnLevel("成人专科");
            } else {
                pictureCollectInfo.setPfsnLevel("");
            }

            if (StringUtil.hasValue(pictureCollectInfo.getSex()) && pictureCollectInfo.getSex().equals("1")) {
                pictureCollectInfo.setSex("男");
            } else if (StringUtil.hasValue(pictureCollectInfo.getSex()) && pictureCollectInfo.getSex().equals("2")) {
                pictureCollectInfo.setSex("女");
            } else {
                pictureCollectInfo.setSex("");
            }

            if (pictureCollectInfo.getInfoStatus().equals("0")) {
                pictureCollectInfo.setInfoStatus("无误");
            } else {
                pictureCollectInfo.setInfoStatus("有误");
            }

            if (pictureCollectInfo.getIsPay().equals("0")) {
                pictureCollectInfo.setIsPay("否");
            } else {
                pictureCollectInfo.setIsPay("是");
            }

            if (pictureCollectInfo.getCheckStatus().equals("0")) {
                pictureCollectInfo.setCheckStatus("待审核");
            } else if(pictureCollectInfo.getCheckStatus().equals("1")) {
                pictureCollectInfo.setCheckStatus("审核通过");
            }else {
                pictureCollectInfo.setCheckStatus("审核不通过");
            }
        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=picCollect.xls");
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

    private BaseUser getUser(){
        BaseUser user = SessionUtil.getUser();
        // 部门主任，学籍组长
        if (user.getJtList().contains("BMZR")) {
            user.setUserLevel("1");
        }
        if(user.getJtList().size()==0){
            user.setUserLevel("1");
        }
        return user;
    }

    public IPageInfo queryWhiteList(int start, int length,PictureCollectWhiteQuery whiteQuery) {
        PageHelper.offsetPage(start, length);
        String[] stdStageArray = whiteQuery.getStdStage().split(",");
        whiteQuery.setStdStageArray(stdStageArray);
        List<PictureCollectWhiteInfo> recruiterInfos = pictureCollectMapper.queryWhiteList(whiteQuery);
        return new IPageInfo((Page) recruiterInfos);
    }

    @Transactional
    public void addWhiteStu(String[] idArray){
        List<PictureCollectInfo> list = new ArrayList<PictureCollectInfo>();
        BaseUser user = SessionUtil.getUser();
        for (String id:idArray){
            PictureCollectInfo pictureCollectInfo = new PictureCollectInfo();
            pictureCollectInfo.setPicCollectId(IDGenerator.generatorId());
            pictureCollectInfo.setLearnId(id.split(";")[0]);
            pictureCollectInfo.setStdId(id.split(";")[1]);
            pictureCollectInfo.setCreateUser(user.getRealName());
            pictureCollectInfo.setCreateUserId(user.getEmpId());
            list.add(pictureCollectInfo);
        }
        pictureCollectMapper.addWhiteStu(list);
    }

    @Transactional
    public void delWhiteStu(String[] idArray){
        List<PictureCollectInfo> list = new ArrayList<PictureCollectInfo>();
        for (String id:idArray){
            PictureCollectInfo pictureCollectInfo = new PictureCollectInfo();
            pictureCollectInfo.setLearnId(id.split(";")[0]);
            list.add(pictureCollectInfo);
        }
        pictureCollectMapper.delWhiteStu(list);
    }

    @Transactional
    public void addAllWhiteStu(PictureCollectWhiteQuery whiteQuery){
        whiteQuery.setStdStageArray(whiteQuery.getStdStage().split(","));
        List<PictureCollectWhiteInfo> whiteList = pictureCollectMapper.queryWhiteList(whiteQuery);
        BaseUser user = SessionUtil.getUser();
        List<PictureCollectInfo> list = new ArrayList<PictureCollectInfo>();
        for(PictureCollectWhiteInfo whiteInfo:whiteList){
            PictureCollectInfo pictureCollectInfo = new PictureCollectInfo();
            pictureCollectInfo.setPicCollectId(UUID.randomUUID().toString().replace("-",""));
            pictureCollectInfo.setLearnId(whiteInfo.getLearnId());
            pictureCollectInfo.setStdId(whiteInfo.getStdId());
            pictureCollectInfo.setCreateUser(user.getRealName());
            pictureCollectInfo.setCreateUserId(user.getEmpId());
            list.add(pictureCollectInfo);
        }
        pictureCollectMapper.addWhiteStu(list);
    }

    @Transactional
    public void delAllWhiteStu(PictureCollectWhiteQuery whiteQuery){
        whiteQuery.setStdStageArray(whiteQuery.getStdStage().split(","));
        List<PictureCollectWhiteInfo> whiteList = pictureCollectMapper.queryWhiteList(whiteQuery);
        List<PictureCollectInfo> list = new ArrayList<PictureCollectInfo>();
        for(PictureCollectWhiteInfo whiteInfo:whiteList){
            PictureCollectInfo pictureCollectInfo = new PictureCollectInfo();
            pictureCollectInfo.setLearnId(whiteInfo.getLearnId());
            list.add(pictureCollectInfo);
        }
        pictureCollectMapper.delWhiteStu(list);
    }

    public void exportRar(PictureCollectQuery query, HttpServletResponse response) {
        try {
            String downloadFilename = "picCollect.zip";//文件的名称
            downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");//转换中文否则可能会产生乱码
            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);// 设置在下载框默认显示的文件名
            String filePath = parameterMapper.selectByPrimaryKey(GlobalConstants.FILE_BROWSER_URL).getParamValue();
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            BaseUser user =  getUser();
            if (user.getJtList().contains("GKXJ")) {
                query.setRecruitType("2");
                user.setUserLevel("1");
            }
            if (user.getJtList().contains("CJXJ")) {
                query.setRecruitType("1");
                user.setUserLevel("1");
            }
            if (user.getJtList().contains("GKXJ") && user.getJtList().contains("CJXJ")) {
                query.setRecruitType("");
                user.setUserLevel("1");
            }
            List<PictureCollectInfo> list = pictureCollectMapper.findPictureCollectList(query, user);
            for (PictureCollectInfo pictureCollectInfo:list) {
                if(!StringUtil.hasValue(pictureCollectInfo.getPictureFileName())){
                    continue;
                }
                URL url = new URL(filePath+pictureCollectInfo.getPictureUrl());
                zos.putNextEntry(new ZipEntry(pictureCollectInfo.getPictureFileName()));
                InputStream fis = url.openConnection().getInputStream();
                byte[] buffer = new byte[1024];
                int r = 0;
                while ((r = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, r);
                }
                fis.close();
            }
            zos.flush();
            zos.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信推送消息
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
