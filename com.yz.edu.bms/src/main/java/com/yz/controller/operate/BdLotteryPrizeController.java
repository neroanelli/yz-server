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
import com.yz.model.operate.BdLotteryPrizeInfo;
import com.yz.service.operate.BdLotteryPrizeService;
import com.yz.util.Assert;

/**
 * 抽奖奖品设置
 * @author lx
 * @date 2018年7月12日 上午11:36:41
 */
@Controller
@RequestMapping("/lotteryPrize")
public class BdLotteryPrizeController {
	
	@Autowired
	private BdLotteryPrizeService lotteryPrizeService;
	
	/**
	 * 跳转到抽奖奖品列表
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("lotteryPrize:query")
	public String toList() {
		return "operate/lotteryPrize/lottery_prize_list";
	}
	
	/**
	 * 加载抽奖奖品列表
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	@Rule("lotteryPrize:query")
	public Object getEnrollLotteryList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,BdLotteryPrizeInfo query){
		return lotteryPrizeService.getLotteryPrizeList(start,length,query);
	}
	
	
	/**
	 * 跳转到编辑页面
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toPrizeEdit")
	@Rule("lotteryPrize:insert")
	public String toLotteryEdit(@RequestParam(name = "exType", required = true) String exType,
			HttpServletRequest request,Model model) {
		BdLotteryPrizeInfo prizeInfo = new BdLotteryPrizeInfo();
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String prizeId = RequestUtil.getString("prizeId");
			Assert.hasText(prizeId, "参数名称不能为空");
			//获取信息
			prizeInfo =lotteryPrizeService.getLotteryPrizeInfo(prizeId);
		}
		model.addAttribute("prizeInfo", prizeInfo);
		model.addAttribute("exType", exType);
		return "operate/lotteryPrize/lottery_prize_edit";
	}
	
	/**
	 * 新增抽奖奖品
	 * @param lotteryInfo
	 * @return
	 */
	@RequestMapping("/add")
	@Rule("lotteryPrize:insert")
	@ResponseBody
	public Object add(BdLotteryPrizeInfo prizeInfo) {
		lotteryPrizeService.add(prizeInfo);
		return null;
	}
	
	/**
	 * 修改抽奖奖品
	 * @param lotteryInfo
	 * @return
	 */
	@RequestMapping("/update")
	@Rule("lotteryPrize:insert")
	@ResponseBody
	public Object update(BdLotteryPrizeInfo prizeInfo) {
		lotteryPrizeService.update(prizeInfo);
		return null;
	}
	
	
}
