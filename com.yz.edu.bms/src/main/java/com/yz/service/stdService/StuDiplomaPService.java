package com.yz.service.stdService;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.stdService.StuDiplomaPMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.exception.BusinessException;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.stdService.StuDiplomaP;
import com.yz.model.stdService.StuDiplomaPInfo;
import com.yz.model.stdService.StuDiplomaPQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StuDiplomaPService {

    @Autowired
    private StuDiplomaPMapper stuDiplomaPMapper;

    public Object getPlaceName() {
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> list = stuDiplomaPMapper.getPlaceName();
        Map<String, String> map = null;
        if (null != list && !list.isEmpty()) {
            for (Map<String, String> resultMap : list) {
                map = new HashMap<String, String>();
                map.put("dictValue", resultMap.get("place_id"));
                map.put("dictName", resultMap.get("place_name"));
                resultList.add(map);
            }
        }
        return resultList;
    }

    public Object getAddress(String placeId) {
        return stuDiplomaPMapper.getAddress(placeId);
    }

    public Object findDiplomaPList(StuDiplomaPQuery stuDiplomaPQuery) {
        List<StuDiplomaP> list = stuDiplomaPMapper.findDiplomaPList(stuDiplomaPQuery);
        return new IPageInfo<>((Page) list);
    }

    public void updateStatus(String placeId, String status) {
        stuDiplomaPMapper.updateStatus(placeId,status);
    }

    public void insert(StuDiplomaPInfo stuDiplomaPInfo) {
        BaseUser user = SessionUtil.getUser();
        if (null != user){
            stuDiplomaPInfo.setCreateUser(user.getUserName());
            stuDiplomaPInfo.setCreateUserId(user.getUserId());
        }else {
            throw new BusinessException("E000034");// 登录超时或尚未登录
        }
        stuDiplomaPMapper.insert(stuDiplomaPInfo);
    }

    public StuDiplomaP getExamRoomByPlaceName(String placeName) {
        return stuDiplomaPMapper.getExamRoomByPlaceName(placeName);
    }

    public StuDiplomaP getExamRoomByPlaceId(String placeId) {
        return stuDiplomaPMapper.getExamRoomByPlaceId(placeId);
    }

    public void update(StuDiplomaPInfo stuDiplomaPInfo) {
        BaseUser user = SessionUtil.getUser();
        if (null != user){
            stuDiplomaPInfo.setUpdateUser(user.getUserName());
            stuDiplomaPInfo.setUpdateUserId(user.getUserId());
        }else {
            throw new BusinessException("E000034");// 登录超时或尚未登录
        }
        stuDiplomaPMapper.update(stuDiplomaPInfo);
    }
}
