package com.yz.core.util;

import com.yz.constants.GlobalConstants;
import com.yz.exception.UploadException;
import com.yz.util.StringUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class WebUploaderUtil {

    /**
     * 抽取公共文件上传代码
     * @param mRequest
     * @param response
     * @param bucket    上传地址
     * @param type      上传类型（文件夹）
     * @param id        是否根据ID继续创建文件夹
     * @throws Exception
     */
    public static void fileUploadForWebuploader(MultipartHttpServletRequest mRequest, HttpServletResponse response, String bucket, FileSrcUtil.Type type, String id) throws Exception {
        String resourceType = mRequest.getParameter("resourceType");
        if (StringUtil.isEmpty(resourceType)) {
            resourceType = GlobalConstants.UPLOAD_RESOURCE_TYPE_SINGLE;
        }

        if (GlobalConstants.UPLOAD_RESOURCE_TYPE_SINGLE.equals(resourceType)) {
                MultipartFile file = mRequest.getFile("file");
                String fileSrc =  FileSrcUtil.createFileSrc(type, id, file.getOriginalFilename());
                FileUploadUtil.upload(bucket, fileSrc, file.getBytes());

                response.setContentType("text/html;charset=UTF-8");
                response.setHeader("Connection", "close");
                PrintWriter writer = response.getWriter();
                writer.write(fileSrc);
                writer.flush();
                writer.close();
            } else {
                throw new UploadException("文件上传失败");
            }

    }

}
