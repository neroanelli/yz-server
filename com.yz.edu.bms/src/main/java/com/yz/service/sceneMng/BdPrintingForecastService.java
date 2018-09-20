
package com.yz.service.sceneMng;


import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.sceneMng.BdPrintingForecastMapper;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.sceneMng.BdPrintingForecast;
import com.yz.model.sceneMng.BdPrintingForecastQuery;


@Service
@Transactional
public class BdPrintingForecastService {
    @Autowired
    BdPrintingForecastMapper bdPrintingForecastMapper;


    /**
     * 分页网报成功学员信息
     * @param confirmQuery
     * @return
     */
	public List<BdPrintingForecast> findPrintingForecastStdPage(Integer start,Integer length,BdPrintingForecastQuery query) {
    	List<String> array = bdPrintingForecastMapper.findConfirmDictValue("现场确认");
    	BaseUser user = SessionUtil.getUser();
        if (user.getJtList().contains("XCQR_ALL")||"1".equals(user.getUserLevel())) {
            user.setUserLevel("1");
        }else {
        	List<String> array1 = new ArrayList<String>();
        	if(user.getJtList().size()>0){
        		for(String jtCode : user.getJtList()){
            		
            		if(array.contains(jtCode)){
            			array1.add(jtCode);
            		}
            	}
        	}else{
        		user.setJtList(null);
        	}
        	
        	if(array1.size()>0){
        		user.setJtList(array1);
        	}else{
        		 user.setUserLevel("10");
        	}
        }
        //PageHelper.offsetPage(start, length).setRmGroup(false);
        PageHelper.offsetPage(start, length).setCountMapper("com.yz.dao.sceneMng.BdPrintingForecastMapper.getAllPrintingForecastStdCount"); 
        List<BdPrintingForecast> result = bdPrintingForecastMapper.findPrintingForecastStdPage(query,user);
        return result;
    }

    
    public List<String>  findPdfHtmlByArrayId( String[] registerIds){
    	return bdPrintingForecastMapper.findPdfHtmlByArrayId(registerIds);
    }
    
    public List<String>  findPdfHtml( String registerId){
    	return bdPrintingForecastMapper.findPdfHtml(registerId);
    }
    
    public List<String>  findPrintingForecastStd(BdPrintingForecastQuery query){
    	List<String> array = bdPrintingForecastMapper.findConfirmDictValue("现场确认");
    	BaseUser user = SessionUtil.getUser();
        if (user.getJtList().contains("XCQR_ALL")||"1".equals(user.getUserLevel())) {
            user.setUserLevel("1");
        }else {
        	List<String> array1 = new ArrayList<String>();
        	if(user.getJtList().size()>0){
        		for(String jtCode : user.getJtList()){
            		
            		if(array.contains(jtCode)){
            			array1.add(jtCode);
            		}
            	}
        	}else{
        		user.setJtList(null);
        	}
        	
        	if(array1.size()>0){
        		user.setJtList(array1);
        	}else{
        		 user.setUserLevel("10");
        	}
        }
    	return bdPrintingForecastMapper.findPrintingForecastStd(query,user);
    }
}



    
    
    



