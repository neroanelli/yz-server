package com.yz.dao;

import com.yz.model.system.SysCity;
import com.yz.model.system.SysDistrict;
import com.yz.model.system.SysProvince;
import org.apache.ibatis.annotations.Param;

/**
 * 省市区
 * @author lx
 * @date 2017年12月8日 下午5:49:58
 */
public interface SysProvinceCityDistrictMapper
{
	public SysProvince selectProvinceByPrimaryKey(String provinceCode);
	
	public SysCity selectCityByPrimaryKey(String cityCode);
	
	public SysDistrict selectDistrictByPrimaryKey(String districtCode);

	/**
	 * 更具省市区编码获取具体名称
	 * @param provinceCode
	 * @param cityCode
	 * @param districtCode
	 * @return
	 */
    String selectPCDByPCDCode(@Param("provinceCode") String provinceCode, @Param("cityCode") String cityCode, @Param("districtCode") String districtCode);
}
