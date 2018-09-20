package com.yz.controller;


import com.yz.constants.GlobalConstants;
import com.yz.core.annotation.Rule;

import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileUploadUtil;
import com.yz.core.util.WebUploaderUtil;
import com.yz.exception.UploadException;
import com.yz.model.paper.StudentAttachment;
import com.yz.service.StudentGraduatePaperService;
import com.yz.service.SysParameterService;
import com.yz.util.FileUtil;
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
import java.io.IOException;
import java.io.PrintWriter;


@Controller
@RequestMapping("/paper")
public class PaperController {

    private static final Logger log = LoggerFactory.getLogger(PaperController.class);

    @Autowired
    StudentGraduatePaperService graduatePaperService;

    @Autowired
    SysParameterService parameterService;

    @RequestMapping("/index")
    @Rule
    public String paper() {
        return "paper";
    }

    @RequestMapping("/upload")
    @Rule
    public String upload() {
        return "uploadfile";
    }

    @RequestMapping("/getAttachments")
    @Rule
    @ResponseBody
    public Object getAttachments(String learnId) {
        return graduatePaperService.selectStudentAttachment(learnId);
    }

    @RequestMapping("/getLearnInfo")
    @Rule
    @ResponseBody
    public Object getLearnInfo(String learnId) {
        return graduatePaperService.getLearnInfo(learnId);
    }

    @RequestMapping("/webuploader")
    @Rule
    @ResponseBody
    public void fileUploadForWebuploader(MultipartHttpServletRequest mRequest, HttpServletResponse response) {

        String bucket = parameterService.getString(GlobalConstants.FILE_BUCKET);
        try {
            WebUploaderUtil.fileUploadForWebuploader(mRequest,response,bucket, FileSrcUtil.Type.PAPER,null);

        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new UploadException("文件上传失败");
        }
    }

    @RequestMapping("/submitUploader")
    @Rule
    @ResponseBody
    public Object submitUploader(StudentAttachment attachment) {
        graduatePaperService.insertAttachment(attachment);
        return "SUCCESS";
    }

    /*
    获取论文信息展示前端
     */
    @RequestMapping("/getPaperInfo")
    @Rule
    @ResponseBody
    public Object getPaperContent(String learnId) {
        return graduatePaperService.getPaperInfoByLearnId(learnId);
    }
}
