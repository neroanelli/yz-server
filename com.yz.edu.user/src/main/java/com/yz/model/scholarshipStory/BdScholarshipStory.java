package com.yz.model.scholarshipStory;

import java.io.Serializable;

public class BdScholarshipStory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String scholarshipId;  //奖学金故事ID
	private String articleTitle;	//文章标题
	private String textPre;          //正文前半部分
	private String entranceText;	//报读入口文案
	private String entranceLink;	//报读入口链接
	private String textLast;		//正文后半部分
	private String articleLinkTitle; //文章标题链接
	private String articleLinkDes;	//文章标题描述
	private String articlePicUrl;	//文章标题路劲
	private String isAllow;			//是否禁用
	private String updateTime;		//最后更新时间
	private String updateUser;		//最后更新人
	private String updateUserId;    //最后更新人ID
	private String createUserId;	//创建人ID
	private String createTime;		//创建时间
	private String createUser;		//创建人
	private String startTime;       // 创建开始时间
	private String endTime;         //创建截止时间
	
	public String getScholarshipId() {
		return scholarshipId;
	}
	public void setScholarshipId(String scholarshipId) {
		this.scholarshipId = scholarshipId;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getTextPre() {
		return textPre;
	}
	public void setTextPre(String textPre) {
		this.textPre = textPre;
	}
	public String getEntranceText() {
		return entranceText;
	}
	public void setEntranceText(String entranceText) {
		this.entranceText = entranceText;
	}
	public String getEntranceLink() {
		return entranceLink;
	}
	public void setEntranceLink(String entranceLink) {
		this.entranceLink = entranceLink;
	}
	public String getTextLast() {
		return textLast;
	}
	public void setTextLast(String textLast) {
		this.textLast = textLast;
	}
	public String getArticleLinkTitle() {
		return articleLinkTitle;
	}
	public void setArticleLinkTitle(String articleLinkTitle) {
		this.articleLinkTitle = articleLinkTitle;
	}
	public String getArticleLinkDes() {
		return articleLinkDes;
	}
	public void setArticleLinkDes(String articleLinkDes) {
		this.articleLinkDes = articleLinkDes;
	}
	public String getArticlePicUrl() {
		return articlePicUrl;
	}
	public void setArticlePicUrl(String articlePicUrl) {
		this.articlePicUrl = articlePicUrl;
	}
	public String getIsAllow() {
		return isAllow;
	}
	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
