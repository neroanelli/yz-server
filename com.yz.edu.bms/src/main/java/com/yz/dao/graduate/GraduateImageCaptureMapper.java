package com.yz.dao.graduate;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.graduate.ImageCaptureMap;

public interface GraduateImageCaptureMapper {

	/**
	 * 查询图像采集缴费对账查询
	 * @param query
	 * @return
	 */
	public List<ImageCaptureMap> findAllListByPage(ImageCaptureMap query);
	
	/**
	 * 撤销申请
	 * @param imageCaptureMap
	 * @return
	 */
	public Integer reviewCheck(ImageCaptureMap imageCaptureMap);
	
	/**
	 * 批量审核
	 * @param orderNos
	 * @param userId
	 * @param realName
	 * @return
	 */
	int reviewSerial(@Param("orderNos") String[] orderNos, @Param("userId") String userId,
			@Param("realName") String realName);
}
