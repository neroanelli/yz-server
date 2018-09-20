package com.yz.dao.stdService;

import com.yz.model.stdService.StuDiplomaP;
import com.yz.model.stdService.StuDiplomaPInfo;
import com.yz.model.stdService.StuDiplomaPQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StuDiplomaPMapper {

    List<Map<String,String>> getPlaceName();

    Object getAddress(@Param("placeId") String placeId);

    List<StuDiplomaP> findDiplomaPList(StuDiplomaPQuery stuDiplomaPQuery);

    void updateStatus(@Param("placeId") String placeId, @Param("status") String status);

    void insert(StuDiplomaPInfo stuDiplomaPInfo);

    StuDiplomaP getExamRoomByPlaceName(@Param("placeName") String placeName);

    StuDiplomaP getExamRoomByPlaceId(@Param("placeId") String placeId);

    void update(StuDiplomaPInfo stuDiplomaPInfo);
}
