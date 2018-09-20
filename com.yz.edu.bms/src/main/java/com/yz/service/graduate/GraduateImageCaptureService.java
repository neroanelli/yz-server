package com.yz.service.graduate;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.graduate.GraduateImageCaptureMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
import com.yz.model.graduate.ImageCaptureMap;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;
import com.yz.util.ExcelUtil.IExcelConfig;

@Service
@Transactional
public class GraduateImageCaptureService {
	private static final Logger log = LoggerFactory.getLogger(GraduateImageCaptureService.class);
	
	@Autowired
	private GraduateImageCaptureMapper graduateImageCaptureMapper;
	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo findAllListByPage(int start, int length,ImageCaptureMap query) {
		PageHelper.offsetPage(start, length);
		List<ImageCaptureMap> imageCapture = graduateImageCaptureMapper.findAllListByPage(query);
		return new IPageInfo((Page) imageCapture);
	}
	
	public Object reviewCheck(String orderNo) {
		BaseUser user = SessionUtil.getUser();
		ImageCaptureMap imageCapture = new ImageCaptureMap();
		imageCapture.setOrderNo(orderNo);
		imageCapture.setCheckUser(user.getRealName());
		imageCapture.setCheckUserId(user.getUserId());
		imageCapture.setCheckTime(DateUtil.getNowDateAndTime());
		imageCapture.setCheckStatus("1");
		return graduateImageCaptureMapper.reviewCheck(imageCapture);
	}
	
	@SuppressWarnings("unchecked")
	public void exportImageCapture(ImageCaptureMap imageCapture, HttpServletResponse response) {
      // 对导出工具进行字段填充
      IExcelConfig<ImageCaptureMap> testExcelCofing = new IExcelConfig<ImageCaptureMap>();
          testExcelCofing.setSheetName("index").setType(ImageCaptureMap.class)
                  .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                  .addTitle(new ExcelUtil.IExcelTitle("身份证号码", "idCard"))
                  .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                  .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                  .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                  .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                  .addTitle(new ExcelUtil.IExcelTitle("学员阶段", "stdStage"))
		          .addTitle(new ExcelUtil.IExcelTitle("缴费项目", "payItem"))
		          .addTitle(new ExcelUtil.IExcelTitle("缴费金额", "amount"))
		          .addTitle(new ExcelUtil.IExcelTitle("缴费日期", "payTime"))
		          .addTitle(new ExcelUtil.IExcelTitle("收款方式", "paymentType"))
		          .addTitle(new ExcelUtil.IExcelTitle("单据号", "orderNo"))
		          .addTitle(new ExcelUtil.IExcelTitle("第三方订单号", "outSerialNo"))
		          .addTitle(new ExcelUtil.IExcelTitle("是否审核", "checkStatus"))
		          .addTitle(new ExcelUtil.IExcelTitle("审核人", "checkUser"));

      List<ImageCaptureMap> list = graduateImageCaptureMapper.findAllListByPage(imageCapture);

      for (ImageCaptureMap imageCaptureMap : list) {
    	  //转换层次
    	  if (StringUtil.hasValue(imageCaptureMap.getPfsnLevel()) && imageCaptureMap.getPfsnLevel().equals("1")) {
    		  imageCaptureMap.setPfsnLevel("1>专科升本科类");
          } else if (StringUtil.hasValue(imageCaptureMap.getPfsnLevel()) && imageCaptureMap.getPfsnLevel().equals("5")) {
        	  imageCaptureMap.setPfsnLevel("5>高中起点高职高专");
          } else {
        	  imageCaptureMap.setPfsnLevel("");
          }
    	  if(("1").equals(imageCaptureMap.getCheckStatus())){
    		  imageCaptureMap.setCheckStatus("是");
    	  }else if(("0").equals(imageCaptureMap.getCheckStatus())){
    		  imageCaptureMap.setCheckStatus("否");
    	  }
    	  if(StringUtil.hasValue(imageCaptureMap.getPaymentType())){
    		  String paymentType = dictExchangeUtil.getParamKey("paymentType", imageCaptureMap.getPaymentType());
        	  imageCaptureMap.setPaymentType(paymentType);
    	  }
    	  if(StringUtil.hasValue(imageCaptureMap.getPayItem())){
    		  String payItem = dictExchangeUtil.getParamKey("payItem", imageCaptureMap.getPayItem());
        	  imageCaptureMap.setPayItem(payItem);
    	  }
    	  if(StringUtil.hasValue(imageCaptureMap.getStdStage())){
    		  String stdStage = dictExchangeUtil.getParamKey("stdStage", imageCaptureMap.getStdStage());
        	  imageCaptureMap.setStdStage(stdStage);
    	  }
      }

      SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

      ServletOutputStream out = null;
      try {
          response.setContentType("application/vnd.ms-excel");
          response.setHeader("Content-disposition", "attachment;filename=imageCapture.xls");
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
	
	public Object reviewSerial(String[] orderNos) {
		BaseUser user = SessionUtil.getUser();
		return graduateImageCaptureMapper.reviewSerial(orderNos, user.getUserId(), user.getRealName());
	}
}
