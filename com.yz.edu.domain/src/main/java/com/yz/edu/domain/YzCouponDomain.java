package com.yz.edu.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yz.edu.cmd.UseCouponCmd;
import com.yz.edu.cmd.UseCouponPaySuccCmd; 
import com.yz.edu.context.DomainContext;
import com.yz.model.NameValuePari;

/**
 * 
 * @desc 优惠券域
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class YzCouponDomain extends YzBaseDomain {

	private String id; // 优惠券id

	private String userId;// 用户id

	private String stdId; // 学员id

	private String name; // 优惠券名称

	private BigDecimal amount; // 优惠券金额

	private int type; // 优惠券类型

	private Date availableStartTime; // 可用时间

	private Date availableExpireTime; // 到期时间

	private int status; // 状态 是否使用 0 是 1 否

	private List<String> itemCodeList; // 优惠券使用的收费代码

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getAvailableStartTime() {
		return availableStartTime;
	}

	public void setAvailableStartTime(Date availableStartTime) {
		this.availableStartTime = availableStartTime;
	}

	public Date getAvailableExpireTime() {
		return availableExpireTime;
	}

	public void setAvailableExpireTime(Date availableExpireTime) {
		this.availableExpireTime = availableExpireTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<String> getItemCodeList() {
		return itemCodeList;
	}

	public void setItemCodeList(List<String> itemCodeList) {
		this.itemCodeList = itemCodeList;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setId(Object key) {
		this.id = String.valueOf(key);
	}

	@Override
	public Object getId() {
		return this.id;
	}

	/**
	 * 
	 * @desc 去支付
	 * @param cmd
	 * @return
	 */
	public YzCouponDomain toPay(UseCouponCmd cmd) {
		Date now = new Date();
		if (status == 1 || (now.after(availableExpireTime) || now.before(availableStartTime))) {
			cmd.setErrorCode("E60021");
			return this;
		}
		BigDecimal amount = checkItemCode(cmd); // 当前指令所用到的优惠券
		if (cmd.isSuccess()) {
			if (amount.compareTo(this.amount) > 0) // 抵扣金额大于优惠券金额
			{
				cmd.setErrorCode("E60043"); // 优惠券金额抵扣错误
				return this;
			}
		}
		boolean bol = (Boolean) DomainContext.getInstance().getContextAttr("PAY_STATUS", false);
		if (bol) {
			this.status = 1; // 标志已经使用
		}
		return this;
	}

	/**
	 * 
	 * @desc 使用优惠券
	 * @param cmd
	 * @return
	 */
	public YzCouponDomain paySuccess(UseCouponPaySuccCmd cmd) {
		Date now = new Date();
		if (status == 1 || (now.after(availableExpireTime) || now.before(availableStartTime))) {
			cmd.setErrorCode("E60021");
			return this;
		}
		BigDecimal amount = checkItemCode(cmd); // 当前指令所用到的优惠券
		if (cmd.isSuccess()) {
			if (amount.compareTo(this.amount) > 0) // 抵扣金额大于优惠券金额
			{
				cmd.setErrorCode("E60043"); // 优惠券金额抵扣错误
				return this;
			}
		}
		this.status = 1; // 标志已经使用
		return this;
	}

	/**
	 * @desc 校验收费代码
	 * @param cmd
	 */
	private BigDecimal checkItemCode(UseCouponCmd cmd) {
		BigDecimal amount = BigDecimal.ZERO;
		List<NameValuePari> item = cmd.getItemCodeList();
		if (item != null && !item.isEmpty()) {
			for (NameValuePari coupon : item) {
				if (this.itemCodeList.contains(coupon.getKey())) // 存在
				{
					amount = amount.add(new BigDecimal(coupon.getValue())); // 合计优惠券金额
				} else {
					cmd.setErrorCode("E60021"); // 无效优惠券
					break;
				}
			}
		}
		return amount;
	}

}
