package com.yz.controller.graduate;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.model.graduate.ImageCaptureMap;
import com.yz.service.graduate.GraduateImageCaptureService;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 毕业图像采集缴费对账
 * @Description: 
 * @author luxing
 * @date 2018年7月10日 下午7:41:43
 */
@Controller
@RequestMapping("/imageCapture")
public class GraduateImageCaptureController {
	@Autowired
	private GraduateImageCaptureService graduateImageCaptureService;
	
	/**
	 * 跳转到 毕业图像采集缴费对账页面
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("imageCapture:query")
	public String toList() {
		return "graduate/imageCapture/imageCapture_list";
	}
	
	/**
	 * 分页获取图像采集缴费记录数据
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("imageCapture:query")
	@ResponseBody
	public Object findAllList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,ImageCaptureMap query) {
		return graduateImageCaptureService.findAllListByPage(start, length, query);
	}
	
	/**
	 * 导出导出毕业图像采集数据
	 * @param query
	 * @param response
	 */
	@RequestMapping("/exportImageCapture")
    @Rule("imageCapture:export")
	public void exportImageCapture(ImageCaptureMap query,HttpServletResponse response){
		graduateImageCaptureService.exportImageCapture(query,response);
	}
	
	/**
	 * 毕业图像采集对账审核
	 * @param orderNo
	 * @return
	 */
	@RequestMapping("/reviewCheck")
	@ResponseBody
	@Rule("imageCapture:update")
	@Log
	public Object reviewCheck(@RequestParam(name = "orderNo", required = true) String orderNo) {
		return graduateImageCaptureService.reviewCheck(orderNo);
	}
	
	/**
	 * 批量审核
	 * @param orderNos
	 * @return
	 */
	@RequestMapping("/reviewFees")
	@ResponseBody
	@Rule("imageCapture:update")
	@Log
	public Object reviewFees(@RequestParam(name = "orderNos[]", required = true) String[] orderNos) {
		return graduateImageCaptureService.reviewSerial(orderNos);
	}
}
