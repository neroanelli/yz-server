package com.yz.service.graduate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.dao.graduate.OaDiplomaTaskMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.graduate.OaDiplomaTask;
import com.yz.model.stdService.StuDiplomaTCQuery;
/**
 * 毕业任务发放
 * @author Dell
 *
 */

@Service
@Transactional
public class OaDiplomaTaskService{
	
	@Autowired
	private OaDiplomaTaskMapper oaDiplomaTaskMapper;

	
	public IPageInfo<OaDiplomaTask> getOaDiplomaTask(int page,int pageSize,OaDiplomaTask oaDiplomaTask) {
		PageHelper.offsetPage(page, pageSize);
		List<OaDiplomaTask> diplomaTaskList = oaDiplomaTaskMapper.getOaDiplomaTask(oaDiplomaTask);
		return new IPageInfo<>((Page<OaDiplomaTask>)diplomaTaskList);
	}

	public void deleteOaDiplomaTask(String diplomaId) {
		oaDiplomaTaskMapper.deleteOaDiplomaTask(diplomaId);
		
	}

	public void updateOaDiplomaTask(OaDiplomaTask oaDiplomaTask) {
		// TODO Auto-generated method stub
		oaDiplomaTaskMapper.updateOaDiplomaTask(oaDiplomaTask);
	}

	public Map<String, Object> getOneOaDiplomaTask(String diplomaId) {
		// TODO Auto-generated method stub
		return oaDiplomaTaskMapper.getOneOaDiplomaTask(diplomaId);
	}
	
	public int getOaDiplomaTaskConfigure(@Param("diplomaId") String diplomaId){
		return oaDiplomaTaskMapper.getOaDiplomaTaskConfigure(diplomaId);
	}
	
	/**
	 * 任务对象下拉对象获取
	 * @return
	 */
	public List<Map<String, String>> getTaskName() {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = oaDiplomaTaskMapper.getTaskName();
		Map<String, String> map = null;
		if (null != list && !list.isEmpty()) {
			for (Map<String, String> resultMap : list) {
				map = new HashMap<String, String>();
				map.put("dictValue", resultMap.get("diploma_id"));
				map.put("dictName", resultMap.get("task_name"));
				resultList.add(map);
			}

		}
		return resultList;
	}
	
	public void insert(OaDiplomaTask oaDiplomaTask){
		 oaDiplomaTask.setDiplomaId(IDGenerator.generatorId());
		 oaDiplomaTaskMapper.insert(oaDiplomaTask);
	}

}
