package com.yz.dao;

import org.apache.ibatis.annotations.Param;

import com.yz.model.BdStudentBaseInfo;
import com.yz.model.educational.BdStudentSend;
public interface BdStudentSendMapper {

	int insertSelective(BdStudentSend record);

	void insertBdTextBookSend(@Param("sendId") String sendId, @Param("learnId") String learnId);

	String[] selectTestSubByLearnId(String learnId);

	void insertBdTextBookSendFD(@Param("sendId") String sendId, @Param("testSubject") String[] testSubject);

	void updateBookReceiveAddressAndStatus(@Param("learnId") String learnId, @Param("saName") String saName,
			@Param("mobile") String mobile, @Param("address") String address,
			@Param("provinceCode") String provinceCode, @Param("cityCode") String cityCode,
			@Param("districtCode") String districtCode,@Param("streetCode") String streetCode,
			@Param("provinceName") String provinceName, @Param("cityName") String cityName,
			@Param("districtName") String districtName,@Param("streetName") String streetName,
			@Param("semester") String[] semester);

	int selectTestBookCount(@Param("testSubject") String[] testSubject);
	
	void updateSendAddressStatusByFD(BdStudentBaseInfo baseInfo);

}