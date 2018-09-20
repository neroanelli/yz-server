package com.yz.service.recruit;

import com.yz.dao.recruit.BdLearnAnnexTypeMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.common.IPageInfo;
import com.yz.model.recruit.BdLearnAnnexType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @描述: 附件管理
 * @作者: DuKai
 * @创建时间: 2018/6/19 18:08
 * @版本号: V1.0
 */

@Service
public class LearnAnnexTypeService {
    private static final Logger log = LoggerFactory.getLogger(LearnAnnexTypeService.class);

    @Autowired
    private BdLearnAnnexTypeMapper bdLearnAnnexTypeMapper;

    public IPageInfo<BdLearnAnnexType> getAllAnnexTypeList(BdLearnAnnexType queryInfo) {
        PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
        List<BdLearnAnnexType> list = bdLearnAnnexTypeMapper.selectAnnexTypeList(queryInfo);
        return new IPageInfo<BdLearnAnnexType>((Page<BdLearnAnnexType>) list);
    }

    public BdLearnAnnexType getAnnexType(String id) {
        return bdLearnAnnexTypeMapper.selectAnnexType(id);
    }

    public boolean delAnnexType(String id) {
        BdLearnAnnexType learnAnnexType = bdLearnAnnexTypeMapper.selectAnnexType(id);
        int count = bdLearnAnnexTypeMapper.existsAnnexType(learnAnnexType.getAnnexTypeValue(),learnAnnexType.getRecruitType());
        if(count>0){
            return false;
        }
        bdLearnAnnexTypeMapper.deleteAnnexTypeById(id);
        return true;
    }

    public void updateAnnexType(BdLearnAnnexType bdLearnAnnexType) {
        bdLearnAnnexTypeMapper.updateAnnexType(bdLearnAnnexType);
    }

    public void addAnnexType(BdLearnAnnexType bdLearnAnnexType) {
        bdLearnAnnexTypeMapper.insertAnnexType(bdLearnAnnexType);
    }

    public int getAnnexTypeCount(BdLearnAnnexType bdLearnAnnexType) {
        return bdLearnAnnexTypeMapper.selectAnnexTypeCount(bdLearnAnnexType);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    public void deleteAnnexTypeBatch(String[] ids) {
        bdLearnAnnexTypeMapper.deleteAnnexTypeByIds(ids);
    }

    public boolean existsName(String recruitType, String annexTypeName) {
        int count = bdLearnAnnexTypeMapper.existsName(recruitType, annexTypeName);
        return count > 0 ? false : true;
    }
}
