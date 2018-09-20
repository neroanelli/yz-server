package com.yz.controller;

import com.yz.constants.GlobalConstants;
import com.yz.core.annotation.Rule;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileUploadUtil;
import com.yz.core.util.WebUploaderUtil;
import com.yz.exception.UploadException;
import com.yz.model.annex.BdsLearnAnnexInfo;
import com.yz.service.BdsLearnAnnexService;
import com.yz.service.SysParameterService;
import com.yz.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Controller
@RequestMapping("/annex")
public class AnnexController {

    private static final Logger log = LoggerFactory.getLogger(AnnexController.class);

    @Autowired
    private BdsLearnAnnexService bdsLearnAnnexService;

    @Autowired
    SysParameterService parameterService;

    @RequestMapping("/index")
    @Rule
    public String annex(){
        return "annex";
    }

    /**
     * 获取学员可见的附件信息
     * @param learnId
     * @return
     */
    @RequestMapping("/find")
    @ResponseBody
    @Rule
    public Object find(String learnId){
        return bdsLearnAnnexService.findAnnexByLearnId(learnId);
    }

    /**
     * 上传成功后回调保存附件信息
     * @param bdsLearnAnnexInfo
     * @return
     */
    @RequestMapping("/submitUploader")
    @ResponseBody
    @Rule
    public Object submitUploader(BdsLearnAnnexInfo bdsLearnAnnexInfo){
        bdsLearnAnnexService.insert(bdsLearnAnnexInfo);
        return "SUCCESS";
    }

    /**
     * 删除附件信息
     * @param learnId
     * @param annexType
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @Rule
    public Object delete(String learnId, String annexType, String annexUrl){
        bdsLearnAnnexService.deleteAnnexByLearnIdAndAnnexType(learnId,annexType);
        //删除文件
        String bucket = parameterService.getString(GlobalConstants.FILE_BUCKET);
        FileUploadUtil.delete(bucket,annexUrl);
        return "SUCCESS";
    }

    /**
     * 附件文件上传
     * @param mRequest
     * @param response
     */
    @RequestMapping("/webuploader")
    @Rule
    @ResponseBody
    public void fileUploadForWebuploader(MultipartHttpServletRequest mRequest, HttpServletResponse response) {
        String learnId = mRequest.getParameter("learnId");
        String bucket = parameterService.getString(GlobalConstants.FILE_BUCKET);
        try {
            WebUploaderUtil.fileUploadForWebuploader(mRequest,response,bucket,FileSrcUtil.Type.STUDENT,learnId);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new UploadException("文件上传失败");
        }
    }

}
