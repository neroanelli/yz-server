package com.yz.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.yz.api.AtsAccountApi;
import com.yz.conf.YzSysConfig;
import com.yz.constants.FinanceConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.constants.AppConstants;
import com.yz.dao.BsOrderMapper;
import com.yz.dao.GsGoodsMapper;
import com.yz.dao.ZmcGoodsSalesMapper;
import com.yz.edu.cmd.BaseCommand;
import com.yz.edu.cmd.JdExchangeCmd;
import com.yz.edu.cmd.JdExchangeCollectCmd;
import com.yz.edu.cmd.UserExchangeCmd;
import com.yz.edu.constants.YzDomainConstants;
import com.yz.edu.context.DomainContext;
import com.yz.edu.domain.JdGoodsSalesDomain;
import com.yz.edu.domain.callback.DomainCallBack;
import com.yz.edu.domain.engine.DomainExecEngine;
import com.yz.edu.domain.executor.JdExchangeExecutor;
import com.yz.edu.paging.bean.PageInfo;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.BsOrderParamInfo;
import com.yz.model.GsAuctionPart;
import com.yz.model.GsAuctionPartInsertInfo;
import com.yz.model.GsCourseGoods;
import com.yz.model.GsExchangePartInsertInfo;
import com.yz.model.GsGoodsCommentInsertInfo;
import com.yz.model.GsGoodsOrderInfo;
import com.yz.model.GsLotteryPart;
import com.yz.model.GsSalesAuction;
import com.yz.model.GsSalesLottery;
import com.yz.model.GsSalesNotify;
import com.yz.model.GsSalesPlan;
import com.yz.model.SessionInfo;
import com.yz.model.WechatMsgVo;
import com.yz.model.ZmcGoodsAuctionInfo;
import com.yz.model.ZmcGoodsExChangeInfo;
import com.yz.model.ZmcGoodsLotteryInfo;
import com.yz.model.ZmcGoodsSalesInfo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.redis.RedisService;
import com.yz.redis.hook.RedisOpHookHolder;
import com.yz.session.AppSessionHolder;
import com.yz.task.YzTaskConstants;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.CodeUtil;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 活动商品servic
 * 
 * @author lx
 * @date 2017年7月24日 下午12:11:22
 */
@Service
@Transactional
public class ZmcGoodsSalesService {

	private static final Logger log = LoggerFactory.getLogger(ZmcGoodsSalesService.class);

	private ExecutorService yzJdCompensateThreadPool = Executors.newFixedThreadPool(16,
			new NamedThreadFactory("yzJdCompensateThread"));

	@Autowired
	private DomainExecEngine domainExecEngine;

	@Autowired
	private ZmcGoodsSalesMapper zmcGoodsSalesMapper;

	@Autowired
	private GsGoodsMapper gsGoodsMapper;

	@Autowired
	private BsOrderService bsOrderService;

	@Autowired
	private BsOrderMapper bsOrderMapper;

	@Reference(version = "1.0")
	private AtsAccountApi atsAccountApi;

	@Autowired
	private YzSysConfig yzSysConfig;

	public Object queryGoodsSalesByPage(int pageNum, int pageSize, Body body) {

		String salesType = body.getString("salesType");
		String goodsType = body.getString("goodsType");
		PageHelper.startPage(pageNum, pageSize, false);
		List<ZmcGoodsSalesInfo> list = zmcGoodsSalesMapper.getZmcGoodsSalesInfo(salesType, goodsType);
		if (StringUtil.equalsIgnoreCase(salesType, "2")) {
			if (null != list && list.size() > 0) {
				for (ZmcGoodsSalesInfo salesInfo : list) {
					String salesStatus = "2";
					Date startDate = DateUtil.convertDateStrToDate(salesInfo.getStartTime(),
							DateUtil.YYYYMMDDHHMMSS_SPLIT);
					Date currentDate = new Date();
					if (StringUtil.hasValue(salesInfo.getEndTime())) {
						salesStatus = "1"; // 已经结束
					} else {
						if (currentDate.getTime() < startDate.getTime()) {
							salesStatus = "2"; // 即将开始
						} else if (currentDate.getTime() > startDate.getTime()) {
							salesStatus = "3"; // 进行中
						}
					}
					salesInfo.setSalesStatus(salesStatus);
				}
			}
		}
		PageInfo<ZmcGoodsSalesInfo> pageInfo = new PageInfo<>(list);
		return JSONObject.fromObject(pageInfo);
	}

	public Object getGsGoodsSalesDetailInfo(Header header, Body body) {
		String salesId = body.getString("salesId");
		String salesType = body.getString("salesType");
		if (null == salesId || null == salesType) {
			throw new BusinessException("E200018");
		}
		String userId = header.getUserId();
		if (salesType.equals(AppConstants.SALES_TYPE_EXCHNAGE)) {// 兑换
			ZmcGoodsExChangeInfo info = zmcGoodsSalesMapper.getZmcGoodsExChangeInfo(salesId);
			if (null != info) {
				info.setSalesStatus(getSalesStatus(info.getStartTime(), info.getEndTime()));
				int exchangeCount = zmcGoodsSalesMapper.selectExchangeCount(salesId, userId);
				if (exchangeCount >= Integer.parseInt(info.getOnceCount())) {
					info.setIfExchange("Y");
				} else {
					info.setIfExchange("N");
				}
				GsSalesNotify notify = zmcGoodsSalesMapper.getSalesNotifyLog(salesId, userId);
				if (null != notify) {
					info.setIfAddNotify("Y");
				}
				if (StringUtil.isEmpty(info.getGoodsId())) {
					throw new BusinessException("E200019");
				}
				if (info.getGoodsType().equals("2")) { // 课程
					GsCourseGoods courseGoods = gsGoodsMapper.getGsCourseGoods(info.getGoodsId());
					info.setAddress(courseGoods.getAddress());
					info.setActivityStartTime(courseGoods.getStartTime());
					info.setActivityEndTime(courseGoods.getEndTime());
					info.setLocation(courseGoods.getLocation());
					info.setCourseType(courseGoods.getCourseType());

				} else if (info.getGoodsType().equals("3")) { // 活动
					GsCourseGoods activityGoods = gsGoodsMapper.getGsActivitiesGoods(info.getGoodsId());
					if (null != activityGoods) {
						info.setAddress(activityGoods.getAddress());
						info.setLocation(activityGoods.getLocation());
						info.setTakein(activityGoods.getTakein());
						info.setActivityEndTime(activityGoods.getEndTime());
						info.setActivityStartTime(activityGoods.getStartTime());
					}
				}
			}

			return JSONObject.fromObject(info);
		} else if (salesType.equals(AppConstants.SALES_TYPE_LOTTERY)) {// 抽奖
			ZmcGoodsLotteryInfo info = zmcGoodsSalesMapper.getZmcGoodsLotteryInfo(salesId);
			if (null != info) {

				String salesStatus = "2";
				Date startDate = DateUtil.convertDateStrToDate(info.getStartTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT);
				Date currentDate = new Date();
				if (StringUtil.hasValue(info.getEndTime())) {
					salesStatus = "1"; // 已经结束
				} else {
					if (currentDate.getTime() < startDate.getTime()) {
						salesStatus = "2"; // 即将开始
					} else if (currentDate.getTime() > startDate.getTime()) {
						salesStatus = "3"; // 进行中
					}
				}
				info.setSalesStatus(salesStatus);
				GsSalesNotify notify = zmcGoodsSalesMapper.getSalesNotifyLog(salesId, userId);
				if (null != notify) {
					info.setIfAddNotify("Y");
				}
				GsLotteryPart part = zmcGoodsSalesMapper.getGsLotteryPart(salesId, userId, info.getPlanCount());
				if (null != part) {
					info.setIfJoin("Y");
				}
			}
			return JSONObject.fromObject(info);
		} else if (salesType.equals(AppConstants.SALES_TYPE_AUCTION)) { // 竞拍
			ZmcGoodsAuctionInfo info = zmcGoodsSalesMapper.getZmcGoodsAuctionInfo(salesId);
			if (null != info) {
				// info.setSalesStatus(getSalesStatus(info.getStartTime(),info.getEndTime()));
				GsSalesNotify notify = zmcGoodsSalesMapper.getSalesNotifyLog(salesId, userId);
				if (null != notify) {
					info.setIfAddNotify("Y");
				}
			}
			return JSONObject.fromObject(info);
		}
		return null;
	}

	public void insertGoodsComment(Header header, Body body) {
		GsGoodsCommentInsertInfo insertInfo = new GsGoodsCommentInsertInfo();
		insertInfo.setCommentContent(body.getString("commentContent"));
		insertInfo.setSalesId(body.getString("salesId"));
		// SessionInfo session =
		// RedisService.getRedisService().getByte(header.getUserId(),SessionInfo.class);
		SessionInfo session = AppSessionHolder.getSessionInfo(header.getUserId(),
				AppSessionHolder.RPC_SESSION_OPERATOR);
		if (null == session) {
			throw new BusinessException("E200013");
		}
		insertInfo.setHeadImgUrl(session.getHeadImg());
		insertInfo.setMobile(session.getMobile());
		insertInfo.setUserId(session.getUserId());
		insertInfo.setUserName(CodeUtil.base64Encode2String(session.getNickName()));
		insertInfo.setCommentId(IDGenerator.generatorId());
		zmcGoodsSalesMapper.insertGoodsComment(insertInfo);
	}

	/**
	 * 添加活动提醒
	 * 
	 * @param header
	 * @param body
	 */
	public void addNewSalesNotify(Header header, Body body) {
		// SessionInfo session =
		// RedisService.getRedisService().getByte(header.getUserId(),SessionInfo.class);
		SessionInfo session = AppSessionHolder.getSessionInfo(header.getUserId(),
				AppSessionHolder.RPC_SESSION_OPERATOR);
		if (null == session) {
			throw new BusinessException("E200020");
		}

		GsSalesNotify notify = new GsSalesNotify();
		String salesType = body.getString("salesType");
		if (salesType.equals("2") || salesType.equals("3")) {
			notify.setNotifyContent(body.getString("salesName") + "活动第" + body.getString("planCount") + "即将开始,请做好准备");
			notify.setPlanCount(body.getString("planCount"));
		} else {
			notify.setNotifyContent(body.getString("salesName") + "活动即将开始,请做好准备");
		}
		notify.setSalesType(salesType);
		notify.setNotifyType("2");
		notify.setSalesId(body.getString("salesId"));
		notify.setUserId(header.getUserId());
		notify.setOpenId(session.getOpenId());
		notify.setNickName(CodeUtil.base64Encode2String(session.getNickName()));
		notify.setMobile(session.getMobile());

		// 需要验证是否已经存在提醒,防止出现多条记录
		GsSalesNotify alreadyNotify = zmcGoodsSalesMapper.getSalesNotifyLog(body.getString("salesId"),
				session.getUserId());
		if (alreadyNotify == null) {
			notify.setNotifyId(IDGenerator.generatorId());
			zmcGoodsSalesMapper.addNewSalesNotify(notify);
		}

	}

	@SuppressWarnings("unchecked")
	public void addGsAuctionPart(Header header, Body body) {
		// SessionInfo session =
		// RedisService.getRedisService().getByte(header.getUserId(),SessionInfo.class);
		SessionInfo session = AppSessionHolder.getSessionInfo(header.getUserId(),
				AppSessionHolder.RPC_SESSION_OPERATOR);
		if (null == session) {
			throw new BusinessException("E200014");
		}
		String salesId = body.getString("salesId");
		String auctionPrice = body.getString("auctionPrice");
		String addAuctionPrice = "0";
		// 查询商品信息
		GsGoodsOrderInfo goodsInfo = zmcGoodsSalesMapper.getGsGoodsOrderInfoById(salesId);
		if (goodsInfo == null) {
			throw new BusinessException("E200011");
		}

		// 查找最后出价纪录
		GsAuctionPart lastAuctionPart = zmcGoodsSalesMapper.getLastAuctionLog(salesId, goodsInfo.getPlanCount());
		if (null != lastAuctionPart) {
			// 判断出价是否高于最后一条
			BigDecimal lastAuctionPrice = AmountUtil.str2Amount(lastAuctionPart.getAuctionPrice());
			BigDecimal thisAuctionPrice = AmountUtil.str2Amount(auctionPrice);
			if (lastAuctionPrice.compareTo(thisAuctionPrice) > 0) {
				throw new BusinessException("E200023");
			}
			BigDecimal diffPrice = BigDecimalUtil.substract(thisAuctionPrice, lastAuctionPrice);
			// 判断当前出价人是否和最后一条 相符
			if (lastAuctionPart.getUserId().equals(header.getUserId())) {
				// 继续扣除差价
				addAuctionPrice = String.valueOf(diffPrice);
				body.put("amount", diffPrice.intValue());
				body.put("excDesc", "参与<" + goodsInfo.getSalesName() + ">竞拍活动追加价,扣除");
			} else {
				// 把最后一个出价的人 智米退还
				Map<String, String> otherAccountMap = atsAccountApi.getAccount(lastAuctionPart.getUserId(), null, null,
						FinanceConstants.ACC_TYPE_ZHIMI);
				body.put("accId", otherAccountMap.get("accId"));
				body.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
				body.put("stdId", null);
				body.put("userId", lastAuctionPart.getUserId());
				body.put("empId", null);
				body.put("amount", lastAuctionPart.getAuctionPrice());
				body.put("action", FinanceConstants.ACC_ACTION_IN);
				body.put("excDesc", "参与<" + goodsInfo.getSalesName() + ">竞拍活动失败,返还");
				body.put("mappingId", goodsInfo.getSalesId());
				atsAccountApi.trans(body);
			}
		} else {
			// 直接完成出价操作
			body.put("amount", auctionPrice);
			body.put("excDesc", "参与<" + goodsInfo.getSalesName() + ">竞拍活动出价,扣除");
		}
		// 判断当前账户智米是否够抵扣
		Map<String, String> accountMap = atsAccountApi.getAccount(header.getUserId(), header.getStdId(),
				header.getEmpId(), FinanceConstants.ACC_TYPE_ZHIMI);
		if (null != accountMap && accountMap.size() > 0) {
			String accStatus = accountMap.get("accStatus"); // 账户状态
			BigDecimal accAmount = AmountUtil.str2Amount(accountMap.get("accAmount")); // 账户余额
			if (null != accStatus && !accStatus.equals(FinanceConstants.ACC_STATUS_NORMAL)) {
				throw new BusinessException("E200021");
			}
			BigDecimal thisAuctionPrice = AmountUtil.str2Amount(addAuctionPrice);
			if (null != accAmount && thisAuctionPrice.compareTo(accAmount) > 0) {
				throw new BusinessException("E200022");
			}
		}
		body.put("accId", accountMap.get("accId"));
		body.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
		body.put("stdId", header.getStdId());
		body.put("userId", header.getUserId());
		body.put("empId", header.getEmpId());
		body.put("action", FinanceConstants.ACC_ACTION_OUT);
		body.put("mappingId", goodsInfo.getSalesId());
		atsAccountApi.trans(body);

		// 修改最新的竞拍价
		GsSalesAuction auction = new GsSalesAuction();
		auction.setCurPrice(auctionPrice);
		auction.setUserId(session.getUserId());
		auction.setSalesId(salesId);

		zmcGoodsSalesMapper.updateGsSalesAuction(auction);

		GsAuctionPartInsertInfo partInfo = new GsAuctionPartInsertInfo();
		partInfo.setAuctionPrice(auctionPrice);
		partInfo.setSalesId(salesId);
		partInfo.setPlanCount(goodsInfo.getPlanCount());
		partInfo.setHeadImgUrl(session.getHeadImg());
		partInfo.setMobile(session.getMobile());
		partInfo.setUserId(session.getUserId());
		partInfo.setUserName(CodeUtil.base64Encode2String(session.getNickName()));
		partInfo.setOpenId(session.getOpenId());
		partInfo.setAuctionId(IDGenerator.generatorId());
		zmcGoodsSalesMapper.addGsAuctionPart(partInfo);
	}

	/**
	 * 参与抽奖
	 * 
	 * @param header
	 * @param body
	 */
	@SuppressWarnings("unchecked")
	public void addGsLotteryPart(Header header, Body body) {
		// SessionInfo session =
		// RedisService.getRedisService().getByte(header.getUserId(),SessionInfo.class);
		SessionInfo session = AppSessionHolder.getSessionInfo(header.getUserId(),
				AppSessionHolder.RPC_SESSION_OPERATOR);
		if (null == session) {
			new BusinessException("E200015");
		}

		// 查询商品信息
		String salesId = body.getString("salesId");
		GsGoodsOrderInfo goodsInfo = zmcGoodsSalesMapper.getGsGoodsOrderInfoById(salesId);
		if (goodsInfo == null) {
			throw new BusinessException("E200011");
		}
		// 判断当前账户智米
		Map<String, String> accountMap = atsAccountApi.getAccount(header.getUserId(), header.getStdId(),
				header.getEmpId(), FinanceConstants.ACC_TYPE_ZHIMI);

		// 账户余额变动
		body.put("accId", accountMap.get("accId"));
		body.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
		body.put("stdId", header.getStdId());
		body.put("userId", header.getUserId());
		body.put("empId", header.getEmpId());
		body.put("amount", goodsInfo.getSalesPrice());
		body.put("action", FinanceConstants.ACC_ACTION_OUT);
		body.put("excDesc", "参与<" + goodsInfo.getSalesName() + ">活动的抽奖扣除");
		body.put("mappingId", goodsInfo.getSalesId());
		atsAccountApi.trans(body);

		GsLotteryPart partInfo = new GsLotteryPart();
		partInfo.setSalesId(salesId);
		partInfo.setPlanCount(goodsInfo.getPlanCount());
		partInfo.setHeadImgUrl(session.getHeadImg());
		partInfo.setMobile(session.getMobile());
		partInfo.setUserId(session.getUserId());
		partInfo.setUserName(CodeUtil.base64Encode2String(session.getNickName()));
		partInfo.setOpenId(session.getOpenId());
		partInfo.setPartId(IDGenerator.generatorId());
		zmcGoodsSalesMapper.addGsLotteryPart(partInfo);

		// TODO 如果此次抽奖刚好达到人数则开奖进入下一期
		GsSalesLottery lottery = zmcGoodsSalesMapper.getGsSalesLotteryById(salesId);
		if (null != lottery) {

			if (zmcGoodsSalesMapper.getGsLotteryPartCount(salesId, goodsInfo.getPlanCount()) >= Integer
					.parseInt(lottery.getRunCount())) {
				// 开奖 随机取出 开奖人员
				List<GsLotteryPart> partList = zmcGoodsSalesMapper.getLuckyUserInfo(salesId,
						Integer.parseInt(lottery.getWinnerCount()), goodsInfo.getPlanCount());
				if (null != partList && partList.size() > 0) {
					for (GsLotteryPart part : partList) {
						// TODO 修改中奖状态同时给当前用户推送微信消息
						zmcGoodsSalesMapper.updateUserWinStatus(salesId, part.getUserId(), goodsInfo.getPlanCount());

						// TODO 下订单
						String orderNo = addNewBsOrder(salesId, 1, null, session, false).toString();
						// 推送微信公众号信息
						sendWinningMsg(true, goodsInfo.getSalesName(), orderNo, part.getOpenId());
					}
				}

				updateSalesLotteryPlan(goodsInfo);
			}
		}
	}

	// 处理抽奖活动排期
	public void updateSalesLotteryPlan(GsGoodsOrderInfo goodsInfo) {
		// 结束本期，开启下一期
		// 修改抽奖活动的期数以及期数信息
		GsSalesPlan salesPlan = zmcGoodsSalesMapper.getGsSalesPalnById(goodsInfo.getPlanId());
		if (null != salesPlan) {
			// TODO 修改排期数,以及当前排期的结束时间 增加定时任务
			GsGoodsOrderInfo salesInfo = new GsGoodsOrderInfo();
			GsSalesPlan updatePlan = new GsSalesPlan();
			updatePlan.setPlanId(salesPlan.getPlanId());

			if (Integer.parseInt(goodsInfo.getPlanCount()) < Integer.parseInt(salesPlan.getTotalCount())) {
				// 期数没有结束,正常接期
				salesInfo.setSalesId(goodsInfo.getSalesId());
				salesInfo.setPlanCount(String.valueOf(Integer.parseInt(goodsInfo.getPlanCount()) + 1));
				// salesInfo.setStartTime(DateUtil.stampToDate(new Date()));
				// 180308 版本规划 修改
				// salesInfo.setEndTime(DateUtil.dateTimeAddOrReduceDays(DateUtil.stampToDate(new
				// Date()), Integer.parseInt(goodsInfo.getInterval())));
				zmcGoodsSalesMapper.updateGsSalesPlanCount(salesInfo);
				// 定时任务
				// 把已经加入任务的删除
				// 删除定时任务
				// 180308 版本规划 修改
				// BmsTimer timer =
				// scheduleApi.selectByJobName(salesInfo.getSalesId() +
				// "continuePlan");
				// if(null != timer){
				// scheduleApi.removeTimer(timer.getId());
				// scheduleApi.deleteTimer(timer.getId());
				// }
				// goodsInfo.setEndTime(salesInfo.getEndTime());
				// salesContinue(goodsInfo,"定时延期");

				updatePlan.setCurCount(String.valueOf(Integer.parseInt(goodsInfo.getPlanCount()) + 1));
				updatePlan.setReason("上期结束,正常延期");
				updatePlan.setPlanStatus("1");
			} else {
				// 本期结束
				updatePlan.setCurCount(goodsInfo.getPlanCount());
				updatePlan.setReason("本期结束,延期终止");
				updatePlan.setPlanStatus("3");
				updatePlan.setEndTime(DateUtil.stampToDate(new Date()));
				// TODO 删除定时任务
				// 180308 版本规划 修改
				// BmsTimer timer =
				// scheduleApi.selectByJobName(salesInfo.getSalesId() +
				// "continuePlan");
				// if(null != timer){
				// scheduleApi.removeTimer(timer.getId());
				// scheduleApi.deleteTimer(timer.getId());
				// }
				// 180308 版本规划 修改
				salesInfo.setSalesId(goodsInfo.getSalesId());
				salesInfo.setEndTime(DateUtil.stampToDate(new Date()));
				zmcGoodsSalesMapper.updateGsSalesPlanCount(salesInfo);
			}
			updatePlan.setLessCount(String
					.valueOf(Integer.parseInt(salesPlan.getTotalCount()) - Integer.parseInt(updatePlan.getCurCount())));
			zmcGoodsSalesMapper.updateGsSalesPlan(updatePlan);
		}
	}

	@SuppressWarnings("unchecked")
	public Object addNewBsOrder(String salesId, int count, String saId, SessionInfo session, boolean ifNeedZM) {
		if (null == session) {
			throw new BusinessException("E200016");
		}
		log.debug("学员:" + session.getStdId() + "对应的用户:" + session.getUserId() + "正在进行下单操作....." + salesId);
		// 查询商品信息
		GsGoodsOrderInfo goodsInfo = zmcGoodsSalesMapper.getGsGoodsOrderInfoById(salesId);
		if (goodsInfo == null) {
			throw new BusinessException("E200011");
		}
		// TODO 针对兑换商品活动做特殊处理,其余两个等下次优化处理 2018-01-30
		if (goodsInfo.getSalesType().equals("1") || goodsInfo.getSalesType().equals("2")) {
			// 查询兑换活动的可兑换数量
			int salesCount = zmcGoodsSalesMapper.getSalesCountById(salesId);
			if (salesCount < count) {
				throw new BusinessException("E200012");
			}
			// 扣除库存
			zmcGoodsSalesMapper.updateSalesCount(count, salesId);
			if (salesCount == count) { // 刚好扣完库存,此次兑换结束,如果是结束后不可见状态改为下架
										// 2018-01-30
				if (StringUtil.hasValue(goodsInfo.getShowAfterOver()) && goodsInfo.getShowAfterOver().equals("0")) {
					zmcGoodsSalesMapper.updateGoodsSalesStatus(salesId);
				}
			}
		} else {
			int goodsCount = zmcGoodsSalesMapper.getGoodsCountById(goodsInfo.getGoodsId());
			if (goodsCount < count) {
				throw new BusinessException("E200012");
			}
			// 扣除库存
			zmcGoodsSalesMapper.updateGoodsCount(goodsCount, goodsInfo.getGoodsId());
			if (goodsCount == count) { // 刚好扣完库存,此次兑换结束,如果是结束后不可见状态改为下架
										// 2018-01-30
				if (StringUtil.hasValue(goodsInfo.getShowAfterOver()) && goodsInfo.getShowAfterOver().equals("0")) {
					zmcGoodsSalesMapper.updateGoodsSalesStatus(salesId);
				}
			}
		}

		if (ifNeedZM) { // 如果是抽奖下单则不需要扣除
			// 扣积分
			Map<String, String> accountMap = atsAccountApi.getAccount(session.getUserId(), session.getStdId(),
					session.getEmpId(), FinanceConstants.ACC_TYPE_ZHIMI);
			// 账户余额变动
			Body body = new Body();
			body.put("accId", accountMap.get("accId"));
			body.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
			body.put("stdId", session.getStdId());
			body.put("userId", session.getUserId());
			body.put("empId", session.getEmpId());
			body.put("amount", BigDecimalUtil.multiply(AmountUtil.str2Amount(goodsInfo.getSalesPrice()),
					AmountUtil.str2Amount(count + "")));
			body.put("action", FinanceConstants.ACC_ACTION_OUT);
			body.put("excDesc", "参与<" + goodsInfo.getSalesName() + ">活动的兑换扣除");
			body.put("mappingId", goodsInfo.getSalesId());
			atsAccountApi.trans(body);
		}
		// 进行下单
		BsOrderParamInfo paramInfo = new BsOrderParamInfo();
		paramInfo.setCostPrice(goodsInfo.getCostPrice());
		paramInfo.setGoodsId(goodsInfo.getGoodsId());
		paramInfo.setGoodsName(goodsInfo.getGoodsName());
		paramInfo.setOriginalPrice(goodsInfo.getOriginalPrice());
		paramInfo.setSalesId(goodsInfo.getSalesId());
		paramInfo.setSalesName(goodsInfo.getSalesName());
		paramInfo.setSalesPrice(goodsInfo.getSalesPrice());
		paramInfo.setSalesType(goodsInfo.getSalesType());
		paramInfo.setCount(count + "");
		paramInfo.setGoodsType(goodsInfo.getGoodsType());
		paramInfo.setUnit(goodsInfo.getUnit());
		paramInfo.setUserId(session.getUserId());
		paramInfo.setGoodsImg(goodsInfo.getGoodsImg());
		paramInfo.setMobile(session.getMobile());
		paramInfo.setUserName(CodeUtil.base64Encode2String(session.getNickName()));
		paramInfo.setSaId(saId);
		return bsOrderService.addNewBsOrderForActivity(paramInfo);
	}

	public Object addGsExchangePart(Header header, Body body) {
		GsExchangePartInsertInfo partInfo = new GsExchangePartInsertInfo();
		partInfo.setExchangeCount(body.getString("exchangeCount"));
		partInfo.setSalesId(body.getString("salesId"));
		// SessionInfo session =
		// RedisService.getRedisService().getByte(header.getUserId(),SessionInfo.class);
		SessionInfo session = AppSessionHolder.getSessionInfo(header.getUserId(),
				AppSessionHolder.RPC_SESSION_OPERATOR);
		if (null == session) {
			throw new BusinessException("E200017");
		}
		ZmcGoodsExChangeInfo info = zmcGoodsSalesMapper.getZmcGoodsExChangeInfo(body.getString("salesId"));
		if (null != info) {
			if (body.getInt("exchangeCount") > Integer.parseInt(info.getOnceCount())) {
				throw new BusinessException("E200025");
			}
			int exchangeCount = zmcGoodsSalesMapper.selectExchangeCount(body.getString("salesId"), session.getUserId());
			log.debug("用户=" + session.getUserId() + "已经兑换=" + exchangeCount);
			if (exchangeCount >= Integer.parseInt(info.getOnceCount())) {
				throw new BusinessException("E200025");
			}
		} else {
			throw new BusinessException("E200025");
		}

		partInfo.setHeadImgUrl(session.getHeadImg());
		partInfo.setMobile(session.getMobile());
		partInfo.setUserId(session.getUserId());
		partInfo.setUserName(CodeUtil.base64Encode2String(session.getNickName()));

		// 收货地址信息
		String saId = body.getString("saId");
		if (StringUtil.isEmpty(saId)) {
			throw new BusinessException("E200024");
		}
		partInfo.setExchangeId(IDGenerator.generatorId());
		zmcGoodsSalesMapper.addGsExchangePart(partInfo);

		return addNewBsOrder(partInfo.getSalesId(), Integer.parseInt(partInfo.getExchangeCount()), saId, session, true);
	}

	// 获取活动商品状态
	public static String getSalesStatus(String startTime, String endTime) {
		String salesStatus = "2";
		Date startDate = null;
		Date endDate = null;
		startDate = DateUtil.convertDateStrToDate(startTime, DateUtil.YYYYMMDDHHMMSS_SPLIT);
		endDate = DateUtil.convertDateStrToDate(endTime, DateUtil.YYYYMMDDHHMMSS_SPLIT);
		Date currentDate = new Date();
		if (currentDate.getTime() < startDate.getTime()) {
			salesStatus = "2"; // 即将开始
		} else if (currentDate.getTime() > startDate.getTime() && currentDate.getTime() < endDate.getTime()) {
			salesStatus = "3"; // 进行中
		} else if (currentDate.getTime() > endDate.getTime()) {
			salesStatus = "1"; // 已经结束
		}
		return salesStatus;
	}

	public void updateSalesAuctionPlan(String salesId) {
		GsGoodsOrderInfo goodsInfo = zmcGoodsSalesMapper.getGsGoodsOrderInfoById(salesId);
		if (goodsInfo == null) {
			throw new BusinessException("E200011");
		}
		GsAuctionPart lastAuctionPart = zmcGoodsSalesMapper.getLastAuctionLog(salesId, goodsInfo.getPlanCount());
		// TODO 修改竞拍状态同时给当前用户推送微信消息
		zmcGoodsSalesMapper.updateUserMineStatus(salesId, lastAuctionPart.getUserId(), goodsInfo.getPlanCount());
		// TODO 下订单
		// addNewBsOrder(salesId,"1",null,session);
		// 结束本期，开启下一期
		// 修改抽奖活动的期数以及期数信息
		GsSalesPlan salesPlan = zmcGoodsSalesMapper.getGsSalesPalnById(goodsInfo.getPlanId());
		if (null != salesPlan) {
			// 修改排期数,以及当前排期的结束时间 增加定时任务
			GsGoodsOrderInfo salesInfo = new GsGoodsOrderInfo();
			GsSalesPlan updatePlan = new GsSalesPlan();
			updatePlan.setPlanId(salesPlan.getPlanId());

			if (Integer.parseInt(goodsInfo.getPlanCount()) < Integer.parseInt(salesPlan.getTotalCount())) {
				// 期数没有结束,正常接期
				salesInfo.setSalesId(goodsInfo.getSalesId());
				salesInfo.setPlanCount(String.valueOf(Integer.parseInt(goodsInfo.getPlanCount()) + 1));
				salesInfo.setStartTime(DateUtil.stampToDate(new Date()));
				salesInfo.setEndTime(DateUtil.dateTimeAddOrReduceDays(DateUtil.stampToDate(new Date()),
						Integer.parseInt(goodsInfo.getInterval())));
				zmcGoodsSalesMapper.updateGsSalesPlanCount(salesInfo);
				// 定时任务
				GsGoodsOrderInfo salesContinue = new GsGoodsOrderInfo();
				salesContinue.setSalesId(salesInfo.getSalesId());
				salesContinue.setEndTime(salesInfo.getEndTime());
				salesContinue.setSalesName(salesInfo.getSalesName());
				salesContinue.setPlanCount(salesInfo.getPlanCount());
				salesContinue.setSalesType(salesInfo.getSalesType());
				// TODO 待处理
				// salesContinue(salesContinue, "定时延期任务");

				updatePlan.setCurCount(String.valueOf(Integer.parseInt(goodsInfo.getPlanCount()) + 1));
				updatePlan.setReason("上期结束,正常延期");
				updatePlan.setPlanStatus("1");
			} else {
				// 本期结束
				updatePlan.setCurCount(goodsInfo.getPlanCount());
				updatePlan.setReason("本期结束,延期终止");
				updatePlan.setPlanStatus("3");
				updatePlan.setEndTime(DateUtil.stampToDate(new Date()));
			}
			updatePlan.setLessCount(String
					.valueOf(Integer.parseInt(salesPlan.getTotalCount()) - Integer.parseInt(updatePlan.getCurCount())));
			zmcGoodsSalesMapper.updateGsSalesPlan(updatePlan);
		}
	}

	public static String getCronByTimeStr(String timeStr) {
		if (StringUtil.isEmpty(timeStr)) {
			return null;
		}
		// cron
		// 秒 分 时 日 月 周 年
		String[] timeStrArray = timeStr.split(" ");
		String[] ymdArray = timeStrArray[0].split("-");
		String[] hsmArray = timeStrArray[1].split(":");
		String d = ymdArray[2];
		String m = ymdArray[1];
		if (Integer.parseInt(d) < 10) {
			d = Integer.parseInt(d) + "";
		}
		if (Integer.parseInt(m) < 10) {
			m = Integer.parseInt(m) + "";
		}
		return hsmArray[2] + " " + hsmArray[1] + " " + hsmArray[0] + " " + d + " " + m + " " + "? " + ymdArray[0];
	}

	/**
	 * 中奖通知 推送微信公众号信息
	 * 
	 * @param isWinning
	 * @param goodName
	 * @param orderNo
	 * @param openId
	 */
	public void sendWinningMsg(boolean isWinning, String goodName, String orderNo, String openId) {

		WechatMsgVo msgVo = new WechatMsgVo();
		msgVo.setTouser(openId);
		msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_WINNING);
		msgVo.addData("goodName", goodName);
		msgVo.addData("now", DateUtil.getNowDateAndTime());
		if (isWinning) {
			msgVo.setExt1(orderNo);
			msgVo.addData("firstWord", "恭喜您，中奖啦！！！\n");
			msgVo.addData("remark", "\n请点击查看详情，完善收货地址");
		} else {
			msgVo.addData("firstWord", "很遗憾，此次活动未中奖\n");
			msgVo.addData("remark", "");
		}
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
	}

	/**
	 * 确定兑换商品(新增的接口针对兑换京东对接的商品)
	 * 
	 * @param header
	 * @param body
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object confirmExchangeGoods(Header header, Body body) {
		int exchangeCount = body.getInt("exchangeCount");
		String salesId = body.getString("salesId");
		// 收货地址信息
		String saId = body.getString("saId");
		String userId = header.getUserId();
		if (StringUtil.isEmpty(saId)) {
			throw new BusinessException("E200024");
		}
		Map<String, String> addressMap = bsOrderMapper.getAddress(saId);
		List<BaseCommand> cmds = Lists.newArrayList(new JdExchangeCollectCmd(salesId));

		JdExchangeCmd cmd = new JdExchangeCmd();
		cmd.setStreetCode((addressMap.get("streetCode") == null ? "0" : addressMap.get("streetCode")));
		cmd.setProvinceCode(addressMap.get("provinceCode"));
		cmd.setCityCode(addressMap.get("cityCode"));
		cmd.setDistrictCode(addressMap.get("districtCode"));
		cmd.setProvinceName(addressMap.get("provinceName"));
		cmd.setCityName(addressMap.get("cityName"));
		cmd.setDistrictName(addressMap.get("districtName"));
		cmd.setStreetName(addressMap.get("streetName"));
		cmd.setSalesId(salesId);
		cmd.setUserId(userId);
		cmd.setAddress(addressMap.get("address"));
		cmd.setEmail(addressMap.get("email"));
		cmd.setMobile(addressMap.get("mobile"));
		cmd.setName(addressMap.get("saName"));
		cmd.setExchangeCount(exchangeCount);
		cmd.setSubmitState(yzSysConfig.getSubmitState());
		cmd.setRegCode(yzSysConfig.getRegCode());
		cmd.setInvoicePhone(yzSysConfig.getInvoicePhone());
		cmd.setInvoiceType(yzSysConfig.getInvoiceType());
		cmd.setThirdOrder(IDGenerator.generatorId());// 生成内部兑换订单号
		String jdAccessToken = RedisService.getRedisService().get("jdAccessToken", RedisOpHookHolder.GET_JD_TOKEN_HOOK);
		String jdEntityCardToken = RedisService.getRedisService().get("jdEntityCardToken",
				RedisOpHookHolder.GET_ENTITY_JD_TOKEN_HOOK);
		cmd.setJdAccessToken(jdAccessToken);
		cmd.setJdEntityCardToken(jdEntityCardToken);
		UserExchangeCmd userExchangeCmd = new UserExchangeCmd();
		userExchangeCmd.setUserId(userId);
		userExchangeCmd.setExchangeCount(exchangeCount);
		userExchangeCmd.setMappingId(cmd.getThirdOrder());
		cmds.add(userExchangeCmd);
		cmds.add(cmd);
		return domainExecEngine.executeCmds(cmds, new DomainCallBack() {
			@Override
			public Object callSuccess() {
				return null;
			}

			@Override
			public void callFailed() {
				List<JdGoodsSalesDomain> domains = DomainContext.getInstance().getList(JdGoodsSalesDomain.class);
				if (domains != null && !domains.isEmpty()) {
					JdGoodsSalesDomain domain = domains.parallelStream().findFirst().get();
					yzJdCompensateThreadPool.execute(() -> {
						JdExchangeExecutor.cancel(domain, cmd);
					});
				}

			}
		});
	}

}
