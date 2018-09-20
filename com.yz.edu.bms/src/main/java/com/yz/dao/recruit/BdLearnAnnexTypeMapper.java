package com.yz.dao.recruit;


import com.yz.model.recruit.BdLearnAnnexType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdLearnAnnexTypeMapper {
    BdLearnAnnexType selectAnnexType(String id);
    int selectAnnexTypeCount(BdLearnAnnexType bdLearnAnnexType);
    List<BdLearnAnnexType> selectAnnexTypeList(BdLearnAnnexType bdLearnAnnexType);
    int deleteAnnexTypeById(String id);
    int deleteAnnexTypeByIds(@Param(value = "ids") String[] ids);
    int insertAnnexType(BdLearnAnnexType bdLearnAnnexType);
    int updateAnnexType(BdLearnAnnexType bdLearnAnnexType);
    int existsName(@Param("recruitType") String recruitType, @Param("annexTypeName") String annexTypeName);
    int existsAnnexType(@Param("annexType") String annexType, @Param("recruitType") String recruitType);
}