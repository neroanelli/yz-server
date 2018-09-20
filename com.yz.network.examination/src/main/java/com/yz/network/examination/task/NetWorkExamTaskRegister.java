package com.yz.network.examination.task;

 
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.util.StringUtil;
 
/**
 * @author Administrator
 */
@Component(value = "netWorkExamTaskRegister")
public class NetWorkExamTaskRegister implements ApplicationListener<ContextRefreshedEvent> {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Value("${task.switch:N}")
    private String yzTaskSwitch;
    
    @Autowired
    private ZookeeperRegistryCenter jobRegistryCenter;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext parent = contextRefreshedEvent.getApplicationContext().getParent();
        if (parent == null) {
            ApplicationContext ac = contextRefreshedEvent.getApplicationContext();
            Map<String, AbstractNetworkExamJob>tasks = ac.getBeansOfType(AbstractNetworkExamJob.class, true, true);
            register(tasks);
        }
    }

    /**
     *
     * @param tasks
     */
    private void register(Map<String, AbstractNetworkExamJob> tasks) {
    	if(tasks!=null&&!tasks.isEmpty())
    	{ 
    		if(StringUtil.equalsIgnoreCase(yzTaskSwitch, "Y"))
    		{
    			tasks.entrySet().parallelStream().forEach(v->{
        			AbstractNetworkExamJob task = v.getValue();
        			JobCoreConfiguration conf = createJobCoreConfiguration(task.getClass().getName(), task, task.getJobParamStr());
        			JobTypeConfiguration jobConf = StringUtil.equalsIgnoreCase(task.getJobType(), NetWorkExamConstants.JOB_TYPE_DATAFLOW)?
        					new DataflowJobConfiguration(conf, task.getClass().getCanonicalName(),true):
        					new SimpleJobConfiguration(conf, task.getClass().getCanonicalName());
        			SpringJobScheduler scheduler = new SpringJobScheduler(task, jobRegistryCenter,
        					LiteJobConfiguration.newBuilder(jobConf).reconcileIntervalMinutes(1).overwrite(true).build());
        			logger.info("{}:startTask", task.getDesc());
        			scheduler.init();
        		});
    		} 
    	}
    }
    
    
	/**
	 * 
	 * 
	 * @param name
	 * @param mhsJob
	 * @return
	 */
	private JobCoreConfiguration createJobCoreConfiguration(String name,  AbstractNetworkExamJob yzJob, String jobParam) {
		JobCoreConfiguration conf = JobCoreConfiguration.newBuilder(name, yzJob.getCorn(), yzJob.getShardingTotalCount())
				.failover(true).misfire(true).jobParameter(jobParam)
				.description(yzJob.getDesc()).build();
		return conf;
	}

}
