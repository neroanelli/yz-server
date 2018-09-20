package com.yz.controller.statistics;

import com.yz.core.security.annotation.Rule;
import com.yz.model.statistics.SceneConfirmStatQuery;
import com.yz.model.statistics.SendBookStatQuery;
import com.yz.service.statistics.SceneConfirmStatService;
import com.yz.service.statistics.SendBookStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RequestMapping("/sceneConfirmStat")
@Controller
public class SceneConfirmStatController
{
	@Autowired
	private SceneConfirmStatService sceneConfirmStatService;
	
	@RequestMapping("/toSceneSonfirmStat")
	@Rule("sceneConfirmStat:query")
	public String toListPage() {
		return "statistics/scene_confirm_stat";
	}

	@RequestMapping("/sceneConfirmStatList")
	@ResponseBody
	@Rule("sceneConfirmStat:query")
	public Object sceneConfirmStatList(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam(name = "start", defaultValue = "0") int start,
            @RequestParam(name = "length", defaultValue = "10") int length,
            SceneConfirmStatQuery statQuery) {
		String[] stdStageArray = statQuery.getStdStage().split(",");
		statQuery.setStdStageArray(stdStageArray);
		return sceneConfirmStatService.sceneConfirmStatList(start, length, statQuery);
	}

	@RequestMapping("/countSceneConfirmStat")
	@ResponseBody
	@Rule("sceneConfirmStat:query")
	public Object countSceneConfirmStat(HttpServletRequest req, HttpServletResponse resp,
									   @RequestParam(name = "start", defaultValue = "0") int start,
									   @RequestParam(name = "length", defaultValue = "10") int length,
									   SceneConfirmStatQuery statQuery) {
		String[] stdStageArray = statQuery.getStdStage().split(",");
		statQuery.setStdStageArray(stdStageArray);
		return sceneConfirmStatService.countSceneConfirmStat(start, length, statQuery);
	}
}
