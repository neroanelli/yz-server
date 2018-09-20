package com.yz.dao;

import com.yz.model.imaq.ImaqInfo;
import org.apache.ibatis.annotations.Param;

public interface ImaqMapper {

    ImaqInfo getSPCInfoByLearnId(@Param("learnId") String learnId);

	int getPayOrderByOrderNo(String orderNo);

    /**
     * 根据图像采集跟进id获取图片编号
     * @param picCollectId
     * @return
     */
    String getSPCPictureNoByPicCollectId(@Param("picCollectId") String picCollectId);

    /**
     * 更新图片地址和文件名
     * @param picCollectId
     * @param pictureFilename
     * @param newPictureUrl
     */
    void updatePictureUrl(@Param("picCollectId") String picCollectId, @Param("pictureFilename") String pictureFilename, @Param("newPictureUrl") String newPictureUrl);

    /**
     *提交有误信息
     * @param picCollectId
     * @param errorMessage
     */
    void infoError(@Param("picCollectId") String picCollectId, @Param("errorMessage") String errorMessage);

    /**
     * 根据跟进ID获取订单号
     * @param picCollectId
     * @return
     */
    String getorderNoByPicCollectId(@Param("picCollectId") String picCollectId);

    void updatePictureStauts(@Param("picCollectId") String picCollectId);
}
