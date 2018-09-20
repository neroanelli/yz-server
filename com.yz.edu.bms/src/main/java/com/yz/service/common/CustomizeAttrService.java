package com.yz.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yz.dao.common.CustomizeAttrMapper;
import com.yz.model.common.CustomizeAttrInfo;
/**
 * 自定义属性
 * @author lx
 * @date 2018年7月30日 下午4:32:22
 */
@Service
@Transactional
public class CustomizeAttrService {

	@Autowired
	private CustomizeAttrMapper customizeAttrMapper;
	
	/**
	 * 自定义属性
	 * @param refHandler
	 * @param defCatalog
	 * @return
	 */
	public List<CustomizeAttrInfo> getCustomizeAttrList(String refHandler,String defCatalog){
		return customizeAttrMapper.getCustomizeAttrList(refHandler, defCatalog);
	}
}
