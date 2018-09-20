package com.yz.dao;

import com.yz.model.BdConfirmReceipt;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:
 * @Author: luxing
 * @Date 2018\8\17 0017 16:16
 **/
public interface BdConfirmReceiptMapper {

    /**
     * 查询回执信息
     * @param learnId
     * @return
     */
    public BdConfirmReceipt getReceiptInfo(String learnId);

    /**
     * 添加回执附件信息
     * @param bdConfirmReceipt
     */
    void insertReceiptFile(BdConfirmReceipt bdConfirmReceipt);

    /**
     * 更新上传回执
     * @param bdConfirmReceipt
     */
    void updateReceiptFile(BdConfirmReceipt bdConfirmReceipt);

    /**
     * 插入考生号数据
     * @param learnId
     * @param stdId
     * @param examNo
     * @return
     */
    int insertExamNo(@Param("learnId") String learnId, @Param("stdId") String stdId, @Param("examNo") String examNo,@Param("operateNum") String operateNum);

    /**
     * 更新考生号数据
     * @param learnId
     * @param examNo
     * @return
     */
    int updateExamNo(@Param("learnId") String learnId, @Param("examNo") String examNo,@Param("operateNum") String operateNum);

    int updateNum(@Param("learnId") String learnId,@Param("operateNum") String operateNum);
}
