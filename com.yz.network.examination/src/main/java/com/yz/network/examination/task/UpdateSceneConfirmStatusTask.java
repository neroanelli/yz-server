//package com.yz.network.examination.task;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//import com.yz.network.examination.constants.NetWorkExamConstants;
//import com.yz.network.examination.dao.RegNetWorkExamFrmMapper;
//import com.yz.network.examination.form.GetBaseInfoNetWorkExamForm;
//import com.yz.network.examination.form.LoginNetWorkExamForm;
//import com.yz.network.examination.starter.NetWorkExamStarter;
//
///**
// *
// * @desc 网报采集学员已确认基本信息任务
// * @author zhuliping
// *
// **/
////@Component(value = "updateSceneConfirmStatusTask")
//public class UpdateSceneConfirmStatusTask extends AbstractNetworkExamJob implements SimpleJob, NetWorkExamConstants {
//
//	@Autowired
//	private NetWorkExamStarter netWorkExamStarter;
//
//	@Autowired
//	RegNetWorkExamFrmMapper netWorkExamFrmMapper;
//
//
//	public UpdateSceneConfirmStatusTask() {
//		setCorn("0/30 * * * * ?");
//		setDesc(NETWORKEXAMINATION_BASEINFO_DESC);
//		setJobType(JOB_TYPE_SIMPLE);
//		setShardingTotalCount(10);
//	}
//
//	@Override
//	public void execute(ShardingContext shardingContext) {
//	    int shard = shardingContext.getShardingItem();
//	    List<Map<String, String>> list = netWorkExamFrmMapper.getNeedUpdateSceneConfirmStatus(shard);
//	    if(list!=null&&list.size()>0) {
//	    	list.forEach(p->{
////				LoginNetWorkExamForm loginExamForm = new LoginNetWorkExamForm(p.get("learn_id"));
////				loginExamForm.addParam("dlfs", "1");
////				loginExamForm.addParam("id", p.get("username"));
////				loginExamForm.addParam("pwd",p.get("password"));  
////				loginExamForm.addValidCode();
////				netWorkExamStarter.start(loginExamForm);
//	    		
//	    		String learnId = p.get("learn_id");
//				GetBaseInfoNetWorkExamForm baseInfoNetWorkExamForm = new GetBaseInfoNetWorkExamForm(learnId);
//				baseInfoNetWorkExamForm.setNeedLogin(true);
//				baseInfoNetWorkExamForm.setLogFrm(false);
//				netWorkExamStarter.start(baseInfoNetWorkExamForm);
//				
//			});
//	    }
//		
//	}
//}
