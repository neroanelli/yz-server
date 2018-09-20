package com.yz.service.pubquery;

import com.yz.constants.GlobalConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.pubquery.TutorshipBookSendMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.BdStudentSendMap;
import com.yz.model.pubquery.TutorshipBookSendQuery;
import com.yz.model.recruit.StudentAllListInfo;
import com.yz.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@Service
public class TutorshipBookSendService {

    private static Logger log = LoggerFactory.getLogger(TutorshipBookSendService.class);

    @Autowired
    TutorshipBookSendMapper tutorshipBookSendMapper;

    /**
     * 辅导书发放查询
     * @param query
     * @return
     */
    public List<Map<Object, String>> findAllTutorshipBookSend(TutorshipBookSendQuery query) {
        BaseUser user = SessionUtil.getUser();

        //400专员也能查看所有记录
        List<String> jtList = user.getJtList();
        if(jtList != null) {
            if(jtList.contains("400")) {
                user.setUserLevel(GlobalConstants.USER_LEVEL_SUPER);
            }
        }

        List<Map<Object, String>> result = tutorshipBookSendMapper.findAllTutorshipBookSend(query,user);
        return result;
    }
    
    /**
     * 学科教材查询
     * @param query
     * @return
     */
    public IPageInfo<Map<Object,String>> findAllTextbook(BdStudentSendMap query,int start,int length) {
    	List<Map<Object,String>> result = null;
    	if(StringUtil.hasValue(query.getStdName()) || StringUtil.hasValue(query.getMobile()) ||
    			StringUtil.hasValue(query.getIdCard()) || StringUtil.hasValue(query.getLogisticsNo())){
    		PageHelper.offsetPage(start, length);
    		result = tutorshipBookSendMapper.findAllTextbook(query);
            return new IPageInfo<Map<Object,String>>((Page) result);
    	}else{
    		return new IPageInfo<Map<Object,String>>();
    	}
    }
}
