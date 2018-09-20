package com.yz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yz.dao.BdStudentSendMapper;

@Service
@Transactional
public class BdsStudentSendService {

	@Autowired
	private BdStudentSendMapper studentSendMapper;
	/**
	 * 更改收教材地址
	 * @param learnId
	 * @param saName
	 * @param mobile
	 * @param address
	 * @param provinceCode
	 * @param cityCode
	 * @param districtCode
	 * @param semester
	 */
	public void updateStdBookReceive(String learnId, String saName, String mobile, String address, String provinceCode,
			String cityCode, String districtCode,String streetCode,String provinceName,String cityName,
			String districtName,String streetName,String[] semester) {
		studentSendMapper.updateBookReceiveAddressAndStatus(learnId, saName, mobile, address, provinceCode, cityCode,
				districtCode,streetCode,provinceName,cityName,districtName,streetName,semester);
	}

}
