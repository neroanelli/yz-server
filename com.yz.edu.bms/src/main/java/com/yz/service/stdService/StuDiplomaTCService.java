package com.yz.service.stdService;


import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.stdService.StuDiplomaTCMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.exception.BusinessException;
import com.yz.exception.CustomException;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.stdService.StuDiplomaConfig;
import com.yz.model.stdService.StuDiplomaTC;
import com.yz.model.stdService.StuDiplomaTCInfo;
import com.yz.model.stdService.StuDiplomaTCQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StuDiplomaTCService {

    @Autowired
    private StuDiplomaTCMapper stuDiplomaTCMapper;

    public Object findDiplomaTCList(StuDiplomaTCQuery stuDiplomaTCQuery) {
        List<StuDiplomaTC> list = stuDiplomaTCMapper.findDiplomaTCList(stuDiplomaTCQuery);
        return new IPageInfo<>((Page) list);
    }

    public void updateStatus(String[] configIds, String status) {
        stuDiplomaTCMapper.updateStatus(configIds,status);
    }

    public void deleteByConfigId(String configId) {
        //删除前查看是否有学生选择了该配置
        StuDiplomaTC stuDiplomaTC = stuDiplomaTCMapper.getDiplomaTCByConfigId(configId);
        if(null != stuDiplomaTC){
            //容量与剩余不等，有选择该配置的学生，无法删除
            if(!stuDiplomaTC.getNumber().equals(stuDiplomaTC.getAvailableNumbers())){
                throw new CustomException("已经有学员选择了该时间段配置，无法删除！");
            }
        }
        stuDiplomaTCMapper.deleteByConfigId(configId);
        //删除关联信息单独拆开
        stuDiplomaTCMapper.deleteUnvisByConfigId(configId);
    }

    public void insert(StuDiplomaTCInfo stuDiplomaTCInfo) {
        for (StuDiplomaConfig s : stuDiplomaTCInfo.getStuDiplomaConfigs()) {
            s.setDiplomaId(stuDiplomaTCInfo.getDiplomaId());
            s.setPlaceId(stuDiplomaTCInfo.getPlaceId());
            s.setAvailableNumbers(s.getNumber());
            s.setStartTime(s.getDate() + " " + s.getStartTime());
            s.setEndTime(s.getDate() + " " + s.getEndTime());
            stuDiplomaTCMapper.insert(s);
            System.out.println(s);
            stuDiplomaTCMapper.insertUnvis(stuDiplomaTCInfo.getStuDiplomaConfigUnvis(),s.getConfigId());
        }
    }

    public StuDiplomaTC getDiplomaTCByConfigId(String configId) {
        return stuDiplomaTCMapper.getDiplomaTCByConfigId(configId);
    }

    public void update(StuDiplomaTCInfo stuDiplomaTCInfo) {
        StuDiplomaConfig s = stuDiplomaTCInfo.getStuDiplomaConfigs().get(0);
        s.setStartTime(s.getDate() + " " + s.getStartTime());
        s.setEndTime(s.getDate() + " " + s.getEndTime());

        BaseUser user = SessionUtil.getUser();
        if(null != user){
            s.setUpdateUser(user.getUserName());
            s.setUpdateUserId(user.getUserId());
        }else {
            throw new BusinessException("E000034");// 登录超时或尚未登录
        }

        //先删除关联
        if (null != stuDiplomaTCInfo.getDelIds() && stuDiplomaTCInfo.getDelIds().length > 0) {
            stuDiplomaTCMapper.deleteUnvisByIds(stuDiplomaTCInfo.getDelIds());
        }
        //更新数据
        stuDiplomaTCMapper.update(s);
        //重新添加关联
        if (null != stuDiplomaTCInfo.getStuDiplomaConfigUnvis() && stuDiplomaTCInfo.getStuDiplomaConfigUnvis().size() > 0) {
            stuDiplomaTCMapper.insertUnvis(stuDiplomaTCInfo.getStuDiplomaConfigUnvis(),s.getConfigId());
        }

    }

    public void isSelect(String configId) {
        StuDiplomaTC stuDiplomaTC = stuDiplomaTCMapper.getDiplomaTCByConfigId(configId);
        if(null != stuDiplomaTC){
            if(!stuDiplomaTC.getNumber().equals(stuDiplomaTC.getAvailableNumbers())){
                throw new CustomException("已经有学员选择了该时间段配置，无法修改！");
            }
        }
    }

    public void updateNumber(String configId, String number) {
        StuDiplomaTC stuDiplomaTC = stuDiplomaTCMapper.getDiplomaTCByConfigId(configId);
        int isSelect = Integer.parseInt(stuDiplomaTC.getNumber()) - Integer.parseInt(stuDiplomaTC.getAvailableNumbers());
        if(Integer.parseInt(number) < isSelect){
            throw new CustomException("修改的容量不得小于已选的学员人数！");
        }
        stuDiplomaTCMapper.updateNumber(configId,number,Integer.parseInt(number) - isSelect);
    }
}
