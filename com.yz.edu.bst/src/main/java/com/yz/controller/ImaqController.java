package com.yz.controller;

import com.yz.constants.GlobalConstants;
import com.yz.core.annotation.Rule;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileUploadUtil;
import com.yz.core.util.WebUploaderUtil;
import com.yz.exception.UploadException;
import com.yz.service.ImaqService;
import com.yz.service.SysParameterService;
import com.yz.util.FileUtil;
import com.yz.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

@RequestMapping("/imaq")
@Controller
public class ImaqController {

    private static final Logger log = LoggerFactory.getLogger(ImaqController.class);

    @Autowired
    private ImaqService imaqService;

    @Autowired
    SysParameterService parameterService;

    @RequestMapping("/index")
    @Rule
    public String index(){
        return "imaq";
    }

    //获取支付链接
    @RequestMapping("/getQRCode")
    @Rule
    @ResponseBody
    public Object getQRCode(String picCollectId, String learnId){
        return imaqService.getQRCode(picCollectId, learnId);
    }

    //请求是否支付成功
    @RequestMapping("/isFee")
    @Rule
    @ResponseBody
    public Object isFee(String orderNo){
        return imaqService.isFee(orderNo);
    }

    //获取白名单信息
    @RequestMapping("/getSPCInfo")
    @Rule
    @ResponseBody
    public Object getSPCInfo(String learnId){
        return imaqService.getSPCInfoByLearnId(learnId);
    }

    /**
     * 上传图片
     * @param mRequest
     * @param response
     */
    @RequestMapping("/webuploader")
    @Rule
    @ResponseBody
    public void fileUploadForWebuploader(MultipartHttpServletRequest mRequest, HttpServletResponse response) {
        //后台检测图片
        try {
            BufferedImage image = ImageIO.read(mRequest.getFile("file").getInputStream());
            if (image != null) {//如果image=null 表示上传的不是图片格式
                BigDecimal w = new BigDecimal(image.getWidth());
                BigDecimal h = new BigDecimal(image.getHeight());
                BigDecimal divide = w.divide(h, 2, RoundingMode.HALF_UP);
                if(!"0.75".equals(divide.toString())){
                    PrintWriter writer = response.getWriter();
                    writer.write("false");
                    return;
                }
            }
        } catch (IOException e) {
            throw new UploadException("文件上传失败");
        }
        String bucket = parameterService.getString(GlobalConstants.FILE_BUCKET);
        try {
            WebUploaderUtil.fileUploadForWebuploader(mRequest,response,bucket, FileSrcUtil.Type.IMAQ,null);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new UploadException("文件上传失败");
        }
    }

    /**
     * 上传成功后回调保存附件信息
     * @param picCollectId
     * @param newPictureUrl
     * @param pictureUrl
     * @return
     */
    @RequestMapping("/submitUploader")
    @Rule
    @ResponseBody
    public Object submitUploader(String picCollectId, String newPictureUrl, String pictureUrl){
        //更新图像采集信息
        String imaqId = imaqService.getSPCPictureNoByPicCollectId(picCollectId);
        if(StringUtil.isEmpty(imaqId)){
            imaqId = newPictureUrl.substring(newPictureUrl.lastIndexOf("/") + 1,newPictureUrl.lastIndexOf("."));
        }
        String pictureFilename = imaqId + '.' + FileUtil.getSuffix(newPictureUrl);
        imaqService.updatePictureUrl(picCollectId,pictureFilename,newPictureUrl);

        //重新上传图片后更改白名单审核状态
        imaqService.updatePictureStauts(picCollectId);

        //删除旧文件
        FileUploadUtil.delete(parameterService.getString(GlobalConstants.FILE_BUCKET),pictureUrl);
        return "SUCCESS";
    }

    /**
     * 信息有误
     * @param picCollectId
     * @param errorMessage
     * @return
     */
    @RequestMapping("/infoError")
    @Rule
    @ResponseBody
    public Object infoError(String picCollectId,String errorMessage){
        imaqService.infoError(picCollectId,errorMessage);
        return "SUCCESS";
    }


}
