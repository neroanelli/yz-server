package com.yz.dao.graduate;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.graduate.OaDiplomaTask;
import com.yz.model.stdService.StuDiplomaTCQuery;

/**
 * 毕业证发放任务
 * @author Dell
 *
 */
public interface OaDiplomaTaskMapper {
		/**
		 * 获取毕业任务发放任务
		 * @param oaDiplomaTask
		 * @return
		 */
		List<OaDiplomaTask> getOaDiplomaTask(OaDiplomaTask oaDiplomaTask);
		
		/**
		 * 删除
		 * @param diplomaId
		 */
		void deleteOaDiplomaTask(@Param("diplomaId") String diplomaId);
		
		/**
		 * 修改
		 * @param diplomaId
		 */
		void updateOaDiplomaTask(OaDiplomaTask oaDiplomaTask);
		
		/**
		 * 得到一个毕业任务发放对象
		 * @param diplomaId
		 * @return
		 */
		Map<String,Object> getOneOaDiplomaTask(@Param("diplomaId") String diplomaId);
		
		/**
		 * 获取任务名称下拉对象
		 * @return
		 */
		List<Map<String, String>> getTaskName();
		
		void insert(OaDiplomaTask oaDiplomaTask);
		
		
		/**
		 * 删除时得到当前发布任务是否有配置参数
		 * @param diplomaId
		 */
		int getOaDiplomaTaskConfigure(@Param("diplomaId") String diplomaId);
}
