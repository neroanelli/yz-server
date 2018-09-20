package com.yz.controller.transfer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yz.constants.GlobalConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.educational.BdStudentSendMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.transfer.BdStudentOut;
import com.yz.model.transfer.BdStudentOutMap;
import com.yz.service.transfer.BdCheckRecordService;
import com.yz.service.transfer.BdStudentOutService;
import com.yz.util.StringUtil;

@Controller
@RequestMapping("/studentOut")
public class BdStudentOutController {
	private static final Logger log = LoggerFactory.getLogger(BdStudentOutController.class);
	@Autowired
	private BdStudentOutService studentOutService;
	@Autowired
	private BdCheckRecordService checkRecordService;
	@Autowired
	private BdStudentSendMapper bdStudentSendMapper;

	@RequestMapping("/list")
	@Rule("studentOut:find")
	public String showList(HttpServletRequest request) {
		return "transfer/out/student-out-list";
	}

	@RequestMapping("/edit")
	@Rule("studentOut:insert")
	@Token(groupId = "studentOut:insert", action = Flag.Save)
	public String edit(HttpServletRequest request) {
		return "transfer/out/student-out-edit";
	}

	@RequestMapping("/addStudentOut")
	@ResponseBody
	@Rule("studentOut:insert")
	@Token(groupId = "studentOut:insert", action = Flag.Remove)
	public Object addStudentOut(HttpServletRequest request, BdStudentOut studentOut,
			@RequestParam(name = "attachment", required = false) MultipartFile attachment) {

		studentOutService.addStudentOut(studentOut, attachment);

		return "SUCCESS";
	}

	/**
	 * Description: 根据条件查询所有符合数据
	 * 
	 * @param studentOutMap
	 *            BdStudentOutMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.transfer.BdStudentOutController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findAllBdStudentOut")
	@ResponseBody
	@Rule("studentOut:find")
	public Object findAllBdStudentOut(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdStudentOutMap studentOutMap) {

		IPageInfo<BdStudentOutMap> resultList = studentOutService.findAllBdStudentOut(start, length, studentOutMap);

		return resultList;
	}

	/**
	 * Description: 根据名字,身份证,电话查询符合条件的学生信息
	 * 
	 * @param sName
	 *            名字,身份证,电话
	 * @return 返回IPageInfo
	 * @see com.yz.controller.transfer.BdStudentOutController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findStudentInfo")
	@ResponseBody
	@Rule("studentOut:find")
	public Object findStudentInfo(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		return studentOutService.findStudentInfo(page, rows, sName);
	}

	/**
	 * Description: 学业编号查询学生信息
	 * 
	 * @param stdId
	 *            学员id,learnId 学业编号
	 * @return 返回json
	 * @see com.yz.controller.transfer.BdStudentOutController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findStudentInfoById")
	@ResponseBody
	@Rule("studentOut:find")
	public Object findStudentInfoById(String learnId) {
		return studentOutService.findStudentInfoById(learnId);
	}

	/**
	 * Description: 学员ID和年级查询学生信息
	 * 
	 * @param stdId
	 *            学员id,learnId 学业编号
	 * @return 返回json
	 * @see com.yz.controller.transfer.BdStudentOutController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findStudentInfoByGraStdId")
	@ResponseBody
	@Rule("studentOut:find")
	public Object findStudentInfoByGraStdId(@RequestParam(name = "learnId", required = true) String learnId) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.hasValue(learnId)) {
			Map<String, String> user = studentOutService.findStudentInfoByGraStdId(learnId);
			map.put("user", user);
			List<Map<String, Object>> fashu = bdStudentSendMapper.findStudentOrderBookForLearnId(learnId);
			map.put("fashu", fashu);
		}
		return map;
	}

	@RequestMapping("/cancelChecks")
	@ResponseBody
	@Rule("studentOut:delete")
	public Object cancelChecks(@RequestParam(name = "outIds[]", required = true) String[] outIds) {
		String str=studentOutService.cancelCheck(outIds);
		return str;
	}
	
	@RequestMapping("/exportStudentOut")
    @Rule("studentOut:export")
    public void exportStudentOut(BdStudentOutMap studentOutMap, HttpServletResponse response) {
		studentOutService.exportStudentOut(studentOutMap,response);
    }

}
