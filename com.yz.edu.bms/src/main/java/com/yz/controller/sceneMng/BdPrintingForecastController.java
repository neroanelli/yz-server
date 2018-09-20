
package com.yz.controller.sceneMng;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.edu.paging.bean.Page;
import com.yz.model.common.IPageInfo;
import com.yz.model.sceneMng.BdPrintingForecast;
import com.yz.model.sceneMng.BdPrintingForecastQuery;
import com.yz.service.sceneMng.BdPrintingForecastService;


@Controller
@RequestMapping("/printingForecast")
public class BdPrintingForecastController {
    @Autowired
    BdPrintingForecastService bdPrintingForecastService;

    @RequestMapping("/toPrintingForecastList")
    @Rule("printingForecast")
    public String toListPage() {
        return "sceneMng/printing_forecast_list";
    }
    
    @RequestMapping("/findPrintingForecast")
    @Rule("printingForecast:query")
    @ResponseBody
    public Object findAllSceneConfirm(@RequestParam(value = "start", defaultValue = "1") int start,
                                      @RequestParam(value = "length", defaultValue = "10") int length, BdPrintingForecastQuery query) {
        List<BdPrintingForecast> resultList = bdPrintingForecastService.findPrintingForecastStdPage(start,length,query);
        return new IPageInfo((Page) resultList);
    }

    
    @RequestMapping("/forecastPrintsArray")
	//@Rule("printingForecast:print")
	public Object reptPrintsArray(@RequestParam(name = "registerIds[]", required = true) String[] registerIds,Model model) {
    	List<String> list = bdPrintingForecastService.findPdfHtmlByArrayId(registerIds);
    	model.addAttribute("lists", list);
		return "sceneMng/printing_forecast_print";
	}
    
    
    @RequestMapping("/forecastPrintsByQuery")
   	//@Rule("printingForecast:print")
   	public Object reptPrintsByQuery(@RequestParam(name = "campusId", required = false) String campusId,
						   			@RequestParam(name = "content", required = false) String content,
						   			@RequestParam(name = "dpId", required = false) String dpId,
						   			@RequestParam(name = "educationAppraisal", required = false) String educationAppraisal,
						   			@RequestParam(name = "endTime", required = false) String endTime,
						   			@RequestParam(name = "examPayStatus", required = false) String examPayStatus,
						   			@RequestParam(name = "grade", required = false) String grade,
						   			@RequestParam(name = "hasRegisterNo", required = false) String hasRegisterNo,
						   			@RequestParam(name = "hasUsername", required = false) String hasUsername,
						   			@RequestParam(name = "idCard", required = false) String idCard,
						   			@RequestParam(name = "mobile", required = false) String mobile,
						   			@RequestParam(name = "pfsnLevel", required = false) String pfsnLevel,
						   			@RequestParam(name = "placeConfirmStatus", required = false) String placeConfirmStatus,
						   			@RequestParam(name = "recruit", required = false) String recruit,
						   			@RequestParam(name = "scholarship", required = false) String scholarship,
						   			@RequestParam(name = "startTime", required = false) String startTime,
						   			@RequestParam(name = "stdName", required = false) String stdName,
						   			@RequestParam(name = "stdStage", required = false) String stdStage,
						   			@RequestParam(name = "taId", required = false) String taId,
						   			@RequestParam(name = "unvsId", required = false) String unvsId,
						   			@RequestParam(name = "webRegisterEndTime", required = false) String webRegisterEndTime,
						   			@RequestParam(name = "webRegisterStartTime", required = false) String webRegisterStartTime,
						   			@RequestParam(name = "pfsnId", required = false) String pfsnId,
   									Model model) {
							    	BdPrintingForecastQuery query = new BdPrintingForecastQuery();
							    	query.setCampusId(campusId);
							    	query.setContent(content);
							    	query.setDpId( dpId);
							    	query.setEducationAppraisal(educationAppraisal);
							    	query.setEndTime(endTime);
							    	query.setExamPayStatus(examPayStatus);
							    	query.setGrade(grade);
							    	query.setHasRegisterNo(hasRegisterNo);
							    	query.setHasUsername(hasUsername);
							    	query.setIdCard(idCard);
							    	query.setMobile(mobile);
							    	query.setPfsnLevel(pfsnLevel);
							    	query.setPfsnId(pfsnId);
							    	query.setPlaceConfirmStatus(placeConfirmStatus);
							    	query.setRecruit(recruit);
							    	query.setScholarship(scholarship);
							    	query.setStartTime(startTime);
							    	query.setStdName(stdName);
							    	query.setStdStage(stdStage);
							    	query.setTaId(taId);
							    	query.setUnvsId(unvsId);
							    	query.setWebRegisterEndTime(webRegisterEndTime);
							    	query.setWebRegisterStartTime(webRegisterStartTime);
							    	 List<String> resultList = bdPrintingForecastService.findPrintingForecastStd(query);
							       	model.addAttribute("lists", resultList);
							   		return "sceneMng/printing_forecast_print";
   	}
    
    
    @RequestMapping("/forecastPrints")
   	//@Rule("printingForecast:print")
   	public Object reptPrints(@RequestParam(name = "registerId", required = true) String registerId,Model model) {
       	List<String> list = bdPrintingForecastService.findPdfHtml(registerId);
       	model.addAttribute("lists", list);
   		return "sceneMng/printing_forecast_print";
   	}

}
