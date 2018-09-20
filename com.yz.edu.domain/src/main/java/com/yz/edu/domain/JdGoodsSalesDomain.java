package com.yz.edu.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import com.yz.constants.JDConstants;
import com.yz.edu.cmd.GsExchangeGoodsSalesCmd;
import com.yz.edu.cmd.JdExchangeCmd;
import com.yz.edu.cmd.JdExchangeCollectCmd;
import com.yz.edu.context.DomainContext;
import com.yz.edu.domain.executor.JdExchangeExecutor;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/***
 * 
 * @desc 京东兑换域
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class JdGoodsSalesDomain extends YzBaseDomain {

	private String salesId; // '活动ID',

	private String goodsId; // '商品ID',

	private String goodsName; // 商品名称

	private String costPrice; // '成本价',

	private String originalPrice; // '原价';

	private int goodsType; // 商品类型

	private String skuId; // '京东商品Id',

	private String salesName; // '活动名称',

	private String salesType; // '活动类型 --- 对应字典：salesType 1-兑换活动 2-抽奖活动 3-竞价活动',

	private BigDecimal salesPrice;// DEFAULT '0.00' COMMENT '销售价',

	private int salesCount;// '活动商品数量',

	private Date startTime; // '活动起始时间',

	private Date endTime;// '活动结束时间',

	private int dailyCount = 0;// '每日可兑换数',

	private int sumCount = 0;// '可兑换总数',

	private int onceCount = 0;// '每次可兑换',

	private int jdGoodsType; // 京东类型 0-实物，1-实体卡

	private String unit; // 商品单位

	private String goodsImg;

	private JdExchangeRecords records; // 该兑换记录的所有的兑换记录
	

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

	public int getGoodsType() {
		return goodsType;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setRecords(JdExchangeRecords records) {
		this.records = records;
	}

	public JdExchangeRecords getRecords() {
		return records;
	}

	public String getSalesId() {
		return salesId;
	}

	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getSalesName() {
		return salesName;
	}

	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}

	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	public int getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(int salesCount) {
		this.salesCount = salesCount;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public void setId(Object key) {
		this.salesId = String.valueOf(key);
	}

	@Override
	public Object getId() {
		return this.salesId;
	}

	public int getDailyCount() {
		return dailyCount;
	}

	public void setDailyCount(int dailyCount) {
		this.dailyCount = dailyCount;
	}

	public int getSumCount() {
		return sumCount;
	}

	public void setSumCount(int sumCount) {
		this.sumCount = sumCount;
	}

	public int getOnceCount() {
		return onceCount;
	}

	public void setOnceCount(int onceCount) {
		this.onceCount = onceCount;
	}

	public void setJdGoodsType(int jdGoodsType) {
		this.jdGoodsType = jdGoodsType;
	}

	public int getJdGoodsType() {
		return jdGoodsType;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	/**
	 * @desc 根据salesId 获取
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
	public JdGoodsSalesDomain info(JdExchangeCollectCmd cmd) {
		return this;
	}

	/**
	 * 
	 * @param cmd
	 * @step 1 根据收货地址查询该商品的库存信息 2 调用京东下单地址下单?
	 * @return
	 * @throws Exception
	 */
	public JdGoodsSalesDomain exchange(JdExchangeCmd cmd) {
		if (salesCount < cmd.getExchangeCount() || salesCount <= 0 || cmd.getExchangeCount() < 0) {
			cmd.setErrorCode("E200012");
			return this;
		}
		if (cmd.getExchangeCount() > onceCount) {
			cmd.setErrorCode("E200025");
			return this;
		}
		long alreadyExchangeCount = records.countRecord(cmd.getUserId());
		if (alreadyExchangeCount >= sumCount && sumCount > 0) {
			cmd.setErrorCode("E200025");
			return this;
		}
		Date now = new Date();
		if (now.before(startTime) || now.after(endTime)) // 校验兑换时间是否
		{
			cmd.setErrorCode("E200025");
			return this;
		}
		Map<String, Object> result = JdExchangeExecutor.submitJdOrder(this,cmd);
		if (!StringUtil.equalsIgnoreCase("0001", StringUtil.obj2String(result.get("resultCode")))) {
			logger.error("京东下单失败.error:{}", result.get("resultMessage"));
			cmd.setErrorCode("E200031");
			return this;
		}
		this.salesCount -= cmd.getExchangeCount(); // 扣减兑换数量
		logger.info("salesCount=>{}",salesCount );
		this.records.addJdExchangeRecord(cmd.getThirdOrder(), cmd.getUserId(), cmd.getExchangeCount()); // 添加兑换记录
		cmd.addExtendAttrs(result);
		cmd.setUserName(String.valueOf(DomainContext.getInstance().getContextAttr("userName")));
		cmd.setUserMobile(String.valueOf(DomainContext.getInstance().getContextAttr("userMobile")));
		cmd.setHeadImgUrl(String.valueOf(DomainContext.getInstance().getContextAttr("userHeadImg")));
		return this;
	}


	
	/**
	 * 
	 * @param cmd
	 * @step 修改兑换商品的之后，同步域数据
	 * @return
	 * @throws Exception
	 */
	public JdGoodsSalesDomain updateExchangeGoodsSales(GsExchangeGoodsSalesCmd cmd) {
		this.salesCount = cmd.getGoodsCount()==null?0:Integer.parseInt(cmd.getGoodsCount());
		this.salesName = cmd.getSalesName();
		this.salesPrice = cmd.getSalesPrice()==null?BigDecimal.ZERO:BigDecimal.valueOf(Double.valueOf(cmd.getSalesPrice()));
		this.startTime = DateUtil.convertDateStrToDate(cmd.getStartTime(), "yyyy-MM-dd HH:mm:ss");
		this.endTime = DateUtil.convertDateStrToDate(cmd.getEndTime(), "yyyy-MM-dd HH:mm:ss");
		this.onceCount = cmd.getOnceCount()==null?0:Integer.parseInt(cmd.getOnceCount());
		this.goodsName = cmd.getGoodsName();
		this.costPrice = cmd.getCostPrice();
		this.originalPrice = cmd.getOriginalPrice();
		this.goodsType = cmd.getGoodsType()==null?1:Integer.parseInt(cmd.getGoodsType());
		this.goodsImg = cmd.getGoodsImg();
		return this;
	}
}
