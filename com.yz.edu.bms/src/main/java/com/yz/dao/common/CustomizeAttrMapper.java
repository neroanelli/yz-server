package com.yz.dao.common;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.common.CustomizeAttrInfo;

/**
 * 自定义属性
 * @author lx
 * @date 2018年7月30日 下午4:01:30
 */
public interface CustomizeAttrMapper {
	
	/**
	 * 通过业务id和所属目录获取属性
	 * @param refHandler
	 * @param defCatalog
	 * @return
	 */
	List<CustomizeAttrInfo> getCustomizeAttrList(@Param("refHandler") String refHandler,@Param("defCatalog") String defCatalog);
}
