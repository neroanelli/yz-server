package com.yz.controller.stdService;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.educational.BdExamPlace;
import com.yz.model.stdService.StuDiplomaP;
import com.yz.model.stdService.StuDiplomaPInfo;
import com.yz.model.stdService.StuDiplomaPQuery;
import com.yz.model.stdService.StuDiplomaTCQuery;
import com.yz.service.stdService.StuDiplomaPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 毕业证发放地址配置
 */
@Controller
@RequestMapping("/diplomaP")
public class StuDiplomaPController {

    @Autowired
    private StuDiplomaPService stuDiplomaPService;

    @RequestMapping("/getPlaceName")
    @ResponseBody
    public Object getPlaceName(){
        return stuDiplomaPService.getPlaceName();
    }

    @RequestMapping("/getAddress")
    @ResponseBody
    public Object getAddress(String placeId){
        return stuDiplomaPService.getAddress(placeId);
    }

    @RequestMapping("/toList")
    @Rule("diplomaP:query")
    public String toList(){
        return "/stdservice/diplomaP/student_diplomaP_list";
    }

    @RequestMapping("/findDiplomaPList")
    @Rule("diplomaP:query")
    @ResponseBody
    public Object findDiplomaPList(@RequestParam(name = "start", defaultValue = "0") int start,
                                    @RequestParam(name = "length", defaultValue = "10") int length,StuDiplomaPQuery stuDiplomaPQuery) {
        PageHelper.offsetPage(start, length);
        return stuDiplomaPService.findDiplomaPList(stuDiplomaPQuery);
    }

    @RequestMapping("/updateStatus")
    @ResponseBody
    @Rule("diplomaP:updateStatus")
    public Object updateStatus(String placeId,String status){
        stuDiplomaPService.updateStatus(placeId,status);
        return "SUCCESS";
    }

    @RequestMapping("/toAdd")
    @Rule("diplomaP:insert")
    public String toAdd(Model model) {
        StuDiplomaPInfo diplomaP = new StuDiplomaPInfo();
        model.addAttribute("exType", "ADD");
        model.addAttribute("diplomaP", diplomaP);
        return "/stdservice/diplomaP/student_diplomaP_edit";
    }

    @RequestMapping("/add")
    @ResponseBody
    @Rule("diplomaP:insert")
    public Object add(StuDiplomaPInfo stuDiplomaPInfo) {
        stuDiplomaPService.insert(stuDiplomaPInfo);
        return "SUCCESS";
    }

    @RequestMapping("/validatePlaceName")
    @ResponseBody
    public Object validateItemCode(@RequestParam(name = "placeName", required = true) String placeName,
                                   @RequestParam(name = "placeId", required = true) String placeId,
                                   @RequestParam(name = "exType", required = true) String exType) {
        StuDiplomaP s = stuDiplomaPService.getExamRoomByPlaceName(placeName);
        if ("ADD".equals(exType)) {
            if (null != s) {
                return false;
            }
        } else if ("UPDATE".equals(exType)) {
            if (null == s) {
                return true;
            }
            return placeId.equals(s.getPlaceId());
        } else {
            return false;
        }
        return true;
    }

    @RequestMapping("/toUpdate")
    @Rule("diplomaP:update")
    public String toUpdate(Model model, @RequestParam(name = "placeId") String placeId) {
        StuDiplomaP diplomaP = stuDiplomaPService.getExamRoomByPlaceId(placeId);
        model.addAttribute("diplomaP", diplomaP);
        model.addAttribute("exType", "UPDATE");
        return "/stdservice/diplomaP/student_diplomaP_edit";
    }

    @RequestMapping("/update")
    @ResponseBody
    @Rule("diplomaP:update")
    public Object update(StuDiplomaPInfo stuDiplomaPInfo) {
        stuDiplomaPService.update(stuDiplomaPInfo);
        return "SUCCESS";
    }

}
