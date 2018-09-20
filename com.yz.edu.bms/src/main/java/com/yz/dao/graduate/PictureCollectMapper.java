package com.yz.dao.graduate;

import com.yz.model.admin.BaseUser;
import com.yz.model.graduate.PictureCollectInfo;
import com.yz.model.graduate.PictureCollectQuery;
import com.yz.model.graduate.PictureCollectWhiteInfo;
import com.yz.model.graduate.PictureCollectWhiteQuery;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface PictureCollectMapper {

    List<PictureCollectInfo> findPictureCollectList(@Param("queryInfo") PictureCollectQuery query, @Param("user") BaseUser user);

    int updateRemark(@Param("picCollectId") String picCollectId, @Param("remark") String remark);

    int check(@Param("picCollectIds") List<String> picCollectIds, @Param("checkStatus") String checkStatus, @Param("checkRemark") String checkRemark,@Param("userId") String userId);
    
    int revoke(@Param("picCollectIds") List<String> picCollectIds, @Param("checkStatus") String checkStatus,@Param("userId") String userId);

    List<PictureCollectWhiteInfo> queryWhiteList(PictureCollectWhiteQuery studentQuery);

    int addWhiteStu(@Param("pictureCollectInfos") List<PictureCollectInfo> pictureCollectInfos);

    int delWhiteStu(@Param("pictureCollectInfos") List<PictureCollectInfo> pictureCollectInfos);

    List<Map<String, Object>> getNonExistsWhiteStudent(@Param("pictureCollectInfos")List<PictureCollectInfo> pictureCollectInfos);

    void insertWhite(@Param("pictureCollectInfos")List<PictureCollectInfo> pictureCollectInfos,@Param("user") BaseUser user);

    List<Map<String, Object>> getNonExistsStudent(@Param("pictureCollectInfos")List<PictureCollectInfo> pictureCollectInfos);

    void insert(@Param("pictureCollectInfos")List<PictureCollectInfo> pictureCollectInfos,@Param("user") BaseUser user);

    List<String> getLearnIds(@Param("picCollectIds") List<String> picCollectIds);
    
    List<String>getNotPayStudentList(@Param("picCollectIds") List<String> picCollectIds);
}
