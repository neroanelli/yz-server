package com.yz.controller.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.FileUploadUtil;
import com.yz.model.finance.EditPayableQuery;
import com.yz.service.finance.EditPayableService;
import com.yz.util.StringUtil;

@Controller
@RequestMapping("/editpayable")
public class EditPayableController {

	@Autowired
	private EditPayableService editService;
	@Autowired
	private YzSysConfig yzSysConfig;

	@RequestMapping("/toList")
	@Rule("editpayable:query")
	public String toList() {
		return "finance/editpayable/payable-list";
	}

	@RequestMapping("/toEdit")
	@Rule("editpayable:edit")
	public String toEdit(Model model, @RequestParam(name = "learnId", required = true) String learnId) {
		Object o = editService.selectStudent(learnId);
		model.addAttribute("student", o);
		return "finance/editpayable/payable-edit";
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("editpayable:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, EditPayableQuery query) {
		return editService.selectStudentByPage(start, length, query);
	}

	@RequestMapping("/edit")
	@ResponseBody
	@Rule("editpayable:edit")
	public Object edit(@RequestParam(name = "learnId", required = true) String learnId,
			@RequestParam(name = "stdId", required = true) String stdId,
			@RequestParam(name = "remark", required = true) String remark,
			@RequestParam(name = "fileUrl", required = false) MultipartFile fileUrl,
			@RequestParam(name = "itemCodes", required = true) String[] itemCodes,
			@RequestParam(name = "subOrderNos", required = true) String[] subOrderNos,
			@RequestParam(name = "amounts", required = true) String[] amounts) {
		// 附件上传到OSS上
		String fileurl = "";
		if (fileUrl != null) {
			try {
				String bucket = yzSysConfig.getBucket();
				fileurl = "modify/file/" + com.yz.util.StringUtil.UUID() + "."
						+ StringUtil.substringAfter(fileUrl.getOriginalFilename(), ".");
				boolean b = FileUploadUtil.upload(bucket, fileurl, fileUrl.getBytes());
				if (!b) {
					return "附件上传失败!";
				}
			} catch (Exception e) {
				return "附件上传失败!";
			}
		}
		editService.edit(learnId, stdId, remark, fileurl, itemCodes, subOrderNos, amounts);
		return "SUCCESS";

	}
}
