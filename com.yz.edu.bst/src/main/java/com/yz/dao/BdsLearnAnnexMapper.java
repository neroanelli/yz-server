package com.yz.dao;

import com.yz.model.annex.BdsLearnAnnexInfo;
import com.yz.model.annex.BdsLearnAnnexTypeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdsLearnAnnexMapper {


    List<BdsLearnAnnexTypeInfo> findAnnexByLearnId(@Param("learnId") String learnId);

    void insert(BdsLearnAnnexInfo bdsLearnAnnexInfo);

    void deleteAnnexByLearnIdAndAnnexType(@Param("learnId") String learnId, @Param("annexType") String annexType);
}
