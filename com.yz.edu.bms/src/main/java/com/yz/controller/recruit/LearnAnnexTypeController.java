package com.yz.controller.recruit;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.recruit.BdLearnAnnexType;
import com.yz.service.recruit.LearnAnnexTypeService;
import com.yz.util.Assert;
import com.yz.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @描述: 附件类型管理
 * @作者: DuKai
 * @创建时间: 2018/6/19 18:04
 * @版本号: V1.0
 */


@RequestMapping("/learnAnnexType")
@Controller
public class LearnAnnexTypeController {

    @Autowired
    private LearnAnnexTypeService learnAnnexTypeService;

    @RequestMapping("/toList")
    @Rule(value = {"learnAnnexType:query","learnAnnexTypeCJ:query"})
    public String toList(@RequestParam(name = "recruitType") String recruitType, Model model) {
        model.addAttribute("recruitType", recruitType);
        return "/recruit/annexType/learn_annex_type_list";
    }

    @RequestMapping("/getAnnexTypeList")
    @Rule(value = {"learnAnnexType:query","learnAnnexTypeCJ:query"})
    @ResponseBody
    public Object getAnnexTypeList(BdLearnAnnexType queryInfo) {
        return learnAnnexTypeService.getAllAnnexTypeList(queryInfo);
    }

    @RequestMapping("/toEditAnnexType")
    @Token(action = Token.Flag.Save, groupId = "annexTypeEdit")
    @Rule(value={"learnAnnexType:insert","learnAnnexTypeCJ:insert"})
    public Object toEditAnnexType(@RequestParam(name = "exType") String exType, @RequestParam(name = "recruitType") String recruitType, Model model, HttpServletRequest request){
        BdLearnAnnexType bdLearnAnnexType = new BdLearnAnnexType();
        if ("UPDATE".equals(exType.trim().toUpperCase())) {
            String id = RequestUtil.getString("id");
            Assert.hasText(id, "参数名称不能为空");
            bdLearnAnnexType = learnAnnexTypeService.getAnnexType(id);
        }
        bdLearnAnnexType.setRecruitType(recruitType);
        model.addAttribute("exType", exType);
        model.addAttribute("bdLearnAnnexType", bdLearnAnnexType);
        return "recruit/annexType/learn_annex_type_edit";
    }

    @RequestMapping("/editAnnexType")
    @ResponseBody
    @Token(action = Token.Flag.Remove, groupId = "annexTypeEdit")
    @Rule(value={"learnAnnexType:insert","learnAnnexTypeCJ:insert"})
    public Object editAnnexType(@RequestParam(name = "exType") String exType, BdLearnAnnexType bdLearnAnnexType){
        if(bdLearnAnnexType.getIsRequire()==null){
            bdLearnAnnexType.setIsRequire("0");
        }
        if(bdLearnAnnexType.getIsUpload()==null){
            bdLearnAnnexType.setIsUpload("0");
        }
        String deal = exType.trim();
        BaseUser user = SessionUtil.getUser();
        if ("UPDATE".equalsIgnoreCase(deal)) {
            bdLearnAnnexType.setUpdateUserId(user.getUserId());
            bdLearnAnnexType.setUpdateUser(user.getRealName());
            learnAnnexTypeService.updateAnnexType(bdLearnAnnexType);
            //清除缓存
        } else if ("ADD".equalsIgnoreCase(exType)) {
            int count = learnAnnexTypeService.getAnnexTypeCount(bdLearnAnnexType);
            if(count <= 0){
                bdLearnAnnexType.setCreateUserId(user.getUserId());
                bdLearnAnnexType.setCreateUser(user.getRealName());
                learnAnnexTypeService.addAnnexType(bdLearnAnnexType);
            }else{
                return "fail";
            }

        }
        return "success";
    }

    @RequestMapping("/deleteAnnexType")
    @ResponseBody
    @Rule(value={"learnAnnexType:delete","learnAnnexTypeCJ:delete"})
    public Object deleteAnnexType(String id){
        boolean result = learnAnnexTypeService.delAnnexType(id);
        if(result==false){
            return "fail";
        }
        return "success";
    }

    @RequestMapping("/deleteAnnexTypeBatch")
    @ResponseBody
    @Rule(value={"learnAnnexType:delete","learnAnnexTypeCJ:delete"})
    public Object deleteAnnexTypeBatch(@RequestParam(name = "ids[]") String[] ids){
        learnAnnexTypeService.deleteAnnexTypeBatch(ids);
        return "success";
    }

    @RequestMapping("/existsName")
    @ResponseBody
    public Object existsName(@RequestParam(name = "recruitType", required = true) String recruitType, String annexTypeName) throws IOException {
        if (StringUtil.hasValue(annexTypeName)) {
            return learnAnnexTypeService.existsName(recruitType, annexTypeName);
        }
        return false;
    }
}
