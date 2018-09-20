package com.yz.controller.operate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.model.operate.BdEnrollLotteryInfo;
import com.yz.service.operate.BdEnrollLotteryService;
import com.yz.util.Assert;

/**
 *  报读抽奖,运营活动
 * @author lx
 * @date 2018年7月11日 下午3:44:26
 */
@Controller
@RequestMapping("/enrollLottery")
public class BdEnrollLotteryController {
	
	@Autowired
	private BdEnrollLotteryService enrollLotteryService;

	/**
	 * 跳转到抽奖列表
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("enrollLottery:query")
	public String toList() {
		return "operate/enrollLottery/enroll_lottery_list";
	}
	
	/**
	 * 加载抽奖活动列表
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	@Rule("enrollLottery:query")
	public Object getEnrollLotteryList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,BdEnrollLotteryInfo query){
		return enrollLotteryService.getEnrollLotteryList(start,length,query);
	}
	
	/**
	 * 跳转到编辑页面
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toLotteryEdit")
	@Rule("enrollLottery:insert")
	public String toLotteryEdit(@RequestParam(name = "exType", required = true) String exType,
			HttpServletRequest request,Model model) {
		BdEnrollLotteryInfo lotteryInfo = new BdEnrollLotteryInfo();
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String lotteryId = RequestUtil.getString("lotteryId");
			Assert.hasText(lotteryId, "参数名称不能为空");
			//获取信息
		    lotteryInfo =enrollLotteryService.getEnrollLotteryInfo(lotteryId);
		}else{
			lotteryInfo.setAttrList(enrollLotteryService.getScholarshipList());
		}
		model.addAttribute("lotteryInfo", lotteryInfo);
		model.addAttribute("exType", exType);
		return "operate/enrollLottery/enroll_lottery_edit";
	}
	
	/**
	 * 新增抽奖
	 * @param lotteryInfo
	 * @return
	 */
	@RequestMapping("/add")
	@Rule("enrollLottery:insert")
	@ResponseBody
	public Object add(BdEnrollLotteryInfo lotteryInfo) {
		enrollLotteryService.add(lotteryInfo);
		return null;
	}
	
	/**
	 * 修改抽奖
	 * @param lotteryInfo
	 * @return
	 */
	@RequestMapping("/update")
	@Rule("enrollLottery:insert")
	@ResponseBody
	public Object update(BdEnrollLotteryInfo lotteryInfo) {
		enrollLotteryService.update(lotteryInfo);
		return null;
	}
	
	/**
	 * 获取抽奖活动
	 * @param status
	 * @return
	 */
	@RequestMapping("/getLotteryInfoByStatus")
	@ResponseBody
	public Object getLotteryInfoByStatus(@RequestParam(name = "status") String status) {
		return enrollLotteryService.getLotteryInfoByStatus(status);
	}
	
	/**
	 * 获取特定的优惠券信息
	 * @return
	 */
	@RequestMapping("/getCouponInfoByCond")
	@ResponseBody
	public Object getCouponInfoByCond() {
		return enrollLotteryService.getCouponInfoByCond();
	}
}
