package com.yz.star.observatory;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.google.common.collect.Lists;
import com.yz.constants.FinanceConstants;
import com.yz.edu.cmd.UserAccountCmd;
import com.yz.edu.consumer.DomainConsumeVo;
import com.yz.edu.domain.YzUserDomain;
import com.yz.edu.model.BaseEsObject;
import com.yz.edu.model.EsTraceInfo;
import com.yz.edu.model.EsTraceSpan;
import com.yz.edu.model.EventSourceObject;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 
 * { "addr":"172.18.253.207", "appName":"bcc", "date":1529735938149,
 * "destination":73, "errorCode":"", "isError":0, "remark":"学员基础信息",
 * "serviceName":"stdLearnInfo", "traceId":"15EDDC660636441EB8ED089629841C6A" },
 * { "appName":"proxy", "async":0, "date":1529735938149, "destination":73,
 * "errorCode":"", "host":"172.18.253.207", "isError":0,
 * "methodName":"stdLearnInfo", "pid":"", "remark":"", "seq":1,
 * "tid":"1FC2B698981F4D219528C8D000FD28C2",
 * "traceId":"15EDDC660636441EB8ED089629841C6A" }, { "date":1529735938222,
 * "destination":73, "operation":"consumer:com.yz.api.BdsLearnInfoApi.$invoke;",
 * "resouceType":3, "resouceURI":"172.18.253.207:28890", "sort":1,
 * "spanId":"1FC2B698981F4D219528C8D000FD28C2",
 * "traceId":"15EDDC660636441EB8ED089629841C6A" }
 * 
 * @author Administrator
 *
 */
public class EsSaveServiceTest {

	public static void main(String[] args) throws Exception {
		Settings setting = Settings.builder().put("client.transport.sniff", true)
				.put("client.transport.ignore_cluster_name", false).put("client.transport.ping_timeout", "300s")
				.put("client.transport.nodes_sampler_interval", "300s").put("cluster.name", "yz-trace-es").build();
		TransportClient client = new PreBuiltTransportClient(setting);
		client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("120.24.167.89"), 9300));

		
		UserAccountCmd accountCmd  = new UserAccountCmd();
		accountCmd.setUserId("100086");
		accountCmd.setAmount(new BigDecimal(100));
		accountCmd.setAccountType(FinanceConstants.ACC_ACTION_IN);
		
		YzUserDomain domain =  new YzUserDomain();
		domain.setCash(new BigDecimal(100));
		domain.setId("100086");
		
		DomainConsumeVo vo = new DomainConsumeVo();
		vo.setCmd(accountCmd);
		vo.setDomain(domain);
		vo.setTraceId("10086");
		vo.setAppName("star");
		vo.setCreateDate(new Date());
		
		
		EventSourceObject obj = EventSourceObject.wrapEventSourceObject(vo);
		try {
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			for (BaseEsObject trace : Lists.newArrayList(obj)) {
				IndexRequestBuilder bu = client.prepareIndex(trace.indexName(), trace.indexName());
				bu.setSource(trace.toXContentBuilder());
				bu.setId(trace.getId());
				bu.setTimeout("5m");
				if (bu != null)
					bulkRequest.add(bu); 
			}

			if (bulkRequest.numberOfActions() > 0) {
				BulkResponse bulkResponse = bulkRequest.execute().actionGet();
				if (bulkResponse.hasFailures()) {
				 
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		System.in.read();
		
	}
	
	
	
	/**
	 * 
	 * @throws Exception
	 */
	private static void saveTraceData()throws Exception
	{
		Settings setting = Settings.builder().put("client.transport.sniff", true)
				.put("client.transport.ignore_cluster_name", false).put("client.transport.ping_timeout", "300s")
				.put("client.transport.nodes_sampler_interval", "300s").put("cluster.name", "yz-trace-es").build();
		TransportClient client = new PreBuiltTransportClient(setting);
		client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("120.24.167.89"), 9300));

		List<BaseEsObject> datas = Lists.newArrayList();
		EsTraceInfo info = new EsTraceInfo();
		info.setAddr("172.18.253.207");
		info.setAppName("bcc");
		info.setDate(new Date(1529735938149l));
		info.setDestination(73);
		info.setErrorCode("");
		info.setIsError(1);
		info.setRemark("学员基础信息");
		info.setServiceName("stdLearnInfo");
		info.setTraceId("15EDDC660636441EB8ED089629841C6A");
		datas.add(info);

		EsTraceSpan span = new EsTraceSpan();
		span.setAppName("proxy");
		span.setAsync(1);
		span.setDate(new Date(1529735938149l));
		span.setTid("1FC2B698981F4D219528C8D000FD28C2");
		span.setTraceId("15EDDC660636441EB8ED089629841C6A");
		datas.add(span);
		for (int i = 0; i < 10; i++) {
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			for(BaseEsObject data:datas)
			{ 
				IndexRequestBuilder bu = client.prepareIndex(data.indexName(), data.indexName())
						.setSource(data.toXContentBuilder())
						.setId(data.getId())
						.setTimeout("10m")
						.setRefreshPolicy(RefreshPolicy.NONE);
				if (bu != null) {
					bulkRequest.add(bu);
					System.out.println("addTobulkRequest-1:{},bu:{}" + JsonUtil.object2String(data) + JsonUtil.object2String(bu));
				} else {
					System.out.println("11111111");
				} 
			} 
			bulkRequest.request().payloads();
			System.out.println(bulkRequest.numberOfActions() );
			if (bulkRequest.numberOfActions() > 0) {
				BulkResponse bulkResponse = bulkRequest.execute().actionGet();
				if (bulkResponse.hasFailures()) {
					System.out.println("saveTrace.error:{}" + bulkResponse.buildFailureMessage());
				}
			}
		}

	
	}
}
