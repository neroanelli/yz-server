package com.yz.controller.transfer;

import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.IdCardVerifyUtil;
import com.yz.core.util.RequestUtil;
import com.yz.edu.paging.bean.Page;
import com.yz.exception.BusinessException;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsFunc;
import com.yz.model.common.IPageInfo;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.transfer.StudentModifyMap;
import com.yz.service.transfer.BdCheckRecordService;
import com.yz.service.transfer.BdStudentConfirmCheckService;
import com.yz.service.transfer.BdStudentConfirmModifyService;
import com.yz.service.transfer.BdStudentModifyService;
import com.yz.util.Assert;
import com.yz.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 现场确认异动
 */
@Controller
@RequestMapping("/confirmModify")
public class BdStudentConfirmModifyController {

	private static final Logger log = LoggerFactory.getLogger(BdStudentConfirmModifyController.class);

	@Autowired
	private BdStudentConfirmModifyService sms;
	@Autowired
	private BdStudentModifyService studentModifyService;
	@Autowired
    private BdStudentConfirmCheckService bdStudentConfirmCheckService;

	@RequestMapping("/list")
	@Rule("confirmModify:find")
	public String showList(HttpServletRequest request) {
		return "transfer/confirmModify/student-confirm-modify-list";
	}

    @RequestMapping("/add")
    @Rule("confirmModify:insert")
    @Token(action = Flag.Save, groupId = "confirmModify:insert")
    public String addList(HttpServletRequest request,Model model) {

        //判断当前登录人是否拥有 ：优惠类型修改 权限节点
        BaseUser user = SessionUtil.getUser();
        boolean ifCanUpdate = false;
        if(null != user.getFuncs() && user.getFuncs().size()>0){
            for(BmsFunc func:user.getFuncs()){
                if(func.getFuncCode().equals("confirmModify:typeUpdate")){
                    ifCanUpdate = true;
                    break;
                }
            }
        }
        model.addAttribute("ifCanUpdate",ifCanUpdate);

        return "transfer/confirmModify/student-confirm-modify-add";
    }
    
    /*
	 * @Description:根据修改记录id查询修改信息
	 */
	@RequestMapping("/view")
	@Rule("confirmModify:view")
	public String view(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		String modifyId = RequestUtil.getString("modifyId");
		Assert.hasText(modifyId, "参数名称不能为空");
		BdStudentModify studentModify = sms.findStudentModifyById(modifyId);
		//查询审核记录
        List<Map<String, String>> records = bdStudentConfirmCheckService.findStudentModifyByModifyId(modifyId);
        model.addAttribute("records",records);
		model.addAttribute("exType", exType);
		model.addAttribute("studentModifyInfo", studentModify);
		return "transfer/confirmModify/student-confirm-modify-view";
	}


    @RequestMapping("/getNewCityCode")
    @ResponseBody
    public Object getNewCityCode(@RequestParam(name = "taId", required = true) String taId) {
        String newCityCode = sms.getCityCodeByTaId(taId);
        Map map = new HashMap();
        map.put("newCityCode",newCityCode);
        return map;
    }

	/**
	 * Description: 根据条件查询所有信息修改数据
	 *
	 * @param studentModifyMap
	 *            StudentModifyMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see BdStudentConfirmModifyController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findConfirmModify")
	@ResponseBody
	@Rule("confirmModify:find")
	public Object findConfirmModify(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, StudentModifyMap studentModifyMap) {
		List<Map<String, String>> resultList = sms.findConfirmModify(studentModifyMap, start, length);
		return new IPageInfo((Page) resultList);
	}
	
	/*
	 * @Description:查询学员（针对网报成功的学员）
	 */
	@RequestMapping("/findStudentInfo")
	@ResponseBody
	@Rule("confirmModify:find")
	public Object findStudentInfo(String sName, @RequestParam(value = "rows", defaultValue = "5") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		List<Map<String, String>> studentInfoMap = sms.findStudentInfo(sName, page, rows);
		if (null == studentInfoMap) {
			return "不存在!";
		}
		return new IPageInfo((Page) studentInfoMap);
	}
	
	/*
	 * @Description:根据学员ID和年级查询 学员报读信息
	 */
	@RequestMapping("/findStudentEnrollInfo")
	@ResponseBody
	@Rule("confirmModify:find")
	public Object findStudentEnrollInfo(String learnId) {
		Map<String, String> studentEnrollInfoMap = sms.findStudentEnrollInfo(learnId);
		return studentEnrollInfoMap;
	}
	
	/*
	 * @Description:新增修改申请
	 */
	@RequestMapping("/insertConfirmModify")
	@ResponseBody
	@Token(action = Flag.Remove, groupId = "confirmModify:insert")
	@Rule("confirmModify:insert")
	public Object insertConfirmModify(BdStudentModify studentModify) {
        Integer count = sms.ifModifyingByLearnId(studentModify.getLearnId());
        if(count!=null && count>0){
            throw new IllegalArgumentException("已提交异动申请，结束后才能再次申请！");
        }
		 BaseUser user = SessionUtil.getUser();
		// 审核类型为新生信息修改
		studentModify.setCheckType(TransferConstants.CHECK_TYPE_CONFIRM);
		// 变更类型为新生信息修改
		studentModify.setModifyType(TransferConstants.MODIFY_TYPE_MODIFY);
		studentModify.setCheckOrder("1");
		studentModify.setCreateUser(user.getRealName());
		studentModify.setCreateUserId(user.getUserId());
		// 插入
		sms.insertConfirmModify(studentModify);
		return "SUCCESS";
	}
	
	/*
	 * @Description:批量取消删除
	 */
	@RequestMapping("/deleteStudentModify")
	@ResponseBody
	@Rule("confirmModify:delete")
	public Object deleteStudentModify(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		sms.deleteStudentModify(idArray);
		return "SUCCESS";
	}

	/**
	 * 校验身份证规则
	 * @return
	 */
	@RequestMapping("/ifExistInfo")
	@ResponseBody
	public Object ifExistInfo(String idCard) {
		if(!IdCardVerifyUtil.strongVerifyIdNumber(idCard)){
			return false;
		}
		return true;
	}

}
