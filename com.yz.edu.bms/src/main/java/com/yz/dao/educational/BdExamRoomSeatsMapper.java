package com.yz.dao.educational;

import com.yz.model.educational.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
public interface BdExamRoomSeatsMapper {
    List<Map<String, Object>> findAllExamRoomSeats(ExamRoomSeatsQuery query);

    List<Map<String, Object>> getExamSeatsInfo(@Param("pyId") String pyId,@Param("placeId") String placeId);

    List<Map<String, Object>> getPlaceMaxCount(String pyId);

    int insertExamRoomSeatList(@Param("examSeatsList") List<BdExamSeats> examSeatsList);

    int deleteExamRoomSeats(@Param("eyId") String eyId,@Param("epId") String epId,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<Map<String, Object>> findPlaceInfo(@Param("eyId") String eyId,@Param("epId") String epId,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<ExamRoomSeatsExport> findExcelExport(ExamRoomSeatsQuery query);
}
