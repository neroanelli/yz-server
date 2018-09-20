package com.yz.dao.statistics;

import com.yz.model.admin.BaseUser;
import com.yz.model.statistics.SceneConfirmStatInfo;
import com.yz.model.statistics.SceneConfirmStatQuery;
import com.yz.model.statistics.SendBookStatInfo;
import com.yz.model.statistics.SendBookStatQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SceneConfirmStatMapper
{
	List<SceneConfirmStatInfo> sceneConfirmStatList(@Param("query") SceneConfirmStatQuery statQuery, @Param("user") BaseUser user);

	SceneConfirmStatInfo countSceneConfirmStat(@Param("query") SceneConfirmStatQuery statQuery, @Param("user") BaseUser user);
}
