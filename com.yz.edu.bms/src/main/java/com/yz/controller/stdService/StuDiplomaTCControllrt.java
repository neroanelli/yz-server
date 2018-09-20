package com.yz.controller.stdService;

import com.yz.core.security.annotation.Rule;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.stdService.StuDiplomaTC;
import com.yz.model.stdService.StuDiplomaTCInfo;
import com.yz.model.stdService.StuDiplomaTCQuery;
import com.yz.service.stdService.StuDiplomaTCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 毕业证发放任务配置
 */
@Controller
@RequestMapping("/diplomaTC")
public class StuDiplomaTCControllrt {

    @Autowired
    private StuDiplomaTCService stuDiplomaTCService;

    @RequestMapping("/toList")
    @Rule("diplomaTC:query")
    public String toList(){
        return "/stdservice/diplomaTC/student_diplomaTC_list";
    }

    @RequestMapping("/findDiplomaTCList")
    @ResponseBody
    @Rule("diplomaTC:query")
    public Object findDiplomaTCList(@RequestParam(name = "start", defaultValue = "0") int start,
                                    @RequestParam(name = "length", defaultValue = "10") int length,StuDiplomaTCQuery stuDiplomaTCQuery) {
        PageHelper.offsetPage(start, length);
        return stuDiplomaTCService.findDiplomaTCList(stuDiplomaTCQuery);
    }

    @RequestMapping("/updateStatus")
    @ResponseBody
    @Rule("diplomaTC:updateStatus")
    public Object updateStatus(@RequestParam(name = "configIds[]") String[] configIds, @RequestParam(name = "status") String status) {
        stuDiplomaTCService.updateStatus(configIds, status);
        return "SUCCESS";
    }

    @RequestMapping("/deleteByConfigId")
    @ResponseBody
    @Rule("diplomaTC:delete")
    public Object deleteByConfigId(String configId) {
        stuDiplomaTCService.deleteByConfigId(configId);
        return "SUCCESS";
    }

    @RequestMapping("/toAdd")
    @Rule("diplomaTC:insert")
    public Object toAdd(){
        return "/stdservice/diplomaTC/student_diplomaTC_add";
    }

    @RequestMapping("/add")
    @ResponseBody
    @Rule("diplomaTC:insert")
    public Object add(StuDiplomaTCInfo stuDiplomaTCInfo){
        stuDiplomaTCService.insert(stuDiplomaTCInfo);
        return "SUCCESS";
    }

    @RequestMapping("/toEdit")
    @Rule("diplomaTC:update")
    public Object toEdit(String configId, Model model){
        StuDiplomaTC diplomaTC = stuDiplomaTCService.getDiplomaTCByConfigId(configId);
        model.addAttribute("diplomaTC",diplomaTC);
        return "/stdservice/diplomaTC/student_diplomaTC_edit";
    }

    @RequestMapping("/update")
    @Rule("diplomaTC:update")
    @ResponseBody
    public Object update(StuDiplomaTCInfo stuDiplomaTCInfo){
        stuDiplomaTCService.update(stuDiplomaTCInfo);
        return "SUCCESS";
    }

    @RequestMapping("/isSelect")
    @Rule("diplomaTC:query")
    @ResponseBody
    public Object isSelect(String configId){
        stuDiplomaTCService.isSelect(configId);
        return "SUCCESS";
    }

    @RequestMapping("/updateNumber")
    @Rule("diplomaTC:updateNumber")
    @ResponseBody
    public Object updateNumber(String configId,String number){
        stuDiplomaTCService.updateNumber(configId,number);
        return "SUCCESS";
    }


}
