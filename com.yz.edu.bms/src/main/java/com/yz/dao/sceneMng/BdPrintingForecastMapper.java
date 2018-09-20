
package com.yz.dao.sceneMng;


import com.yz.model.admin.BaseUser;
import com.yz.model.sceneMng.BdPrintingForecast;
import com.yz.model.sceneMng.BdPrintingForecastQuery;

import org.apache.ibatis.annotations.Param;

import java.util.List;



public interface BdPrintingForecastMapper {

	List<BdPrintingForecast> findPrintingForecastStdPage(@Param("query") BdPrintingForecastQuery query,@Param("user") BaseUser user);
	
	int getAllPrintingForecastStdCount(@Param("query") BdPrintingForecastQuery query,@Param("user") BaseUser user);

	List<String> findConfirmDictValue(@Param("confirmName") String confirmName);
	
	
	List<String> findPdfHtmlByArrayId(@Param("registerIds") String[] registerIds);
	
	List<String> findPdfHtml(@Param("registerId") String registerId);
	
	List<String> findPrintingForecastStd(@Param("query") BdPrintingForecastQuery query,@Param("user") BaseUser user);
}
