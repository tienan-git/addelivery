/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.db.entity;

import java.time.LocalDateTime;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

/**
 * 
 */
@Entity
@Table(name = "twitter_campaign_manage")
public class TwitterCampaignManage extends BaseEntity {

    /** Twitterキャンペーン管理ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "twitter_campaign_manage_id")
    Long twitterCampaignManageId;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    String campaignId;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 日別予算 */
    @Column(name = "daily_budget")
    Long dailyBudget;

    /** 総予算 */
    @Column(name = "total_budget")
    Long totalBudget;

    /** グループID */
    @Column(name = "group_id")
    String groupId;

    /** ツイートIDS */
    @Column(name = "tweet_ids")
    String tweetIds;

    /** 審査フラグ */
    @Column(name = "approval_flag")
    String approvalFlag;

    /** バージョン番号 */
    @Version
    @Column(name = "version_no")
    Long versionNo;

    /** 登録日時 */
    @Column(name = "created_at")
    LocalDateTime createdAt;

    /** 登録者 */
    @Column(name = "created_by")
    String createdBy;

    /** 更新日時 */
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    /** 更新者 */
    @Column(name = "updated_by")
    String updatedBy;

    /** アクティブフラグ:0：無効、1：有効 */
    @Column(name = "is_actived")
    Integer isActived;

    /** 
     * Returns the twitterCampaignManageId.
     * 
     * @return the twitterCampaignManageId
     */
    public Long getTwitterCampaignManageId() {
        return twitterCampaignManageId;
    }

    /** 
     * Sets the twitterCampaignManageId.
     * 
     * @param twitterCampaignManageId the twitterCampaignManageId
     */
    public void setTwitterCampaignManageId(Long twitterCampaignManageId) {
        this.twitterCampaignManageId = twitterCampaignManageId;
    }

    /** 
     * Returns the campaignId.
     * 
     * @return the campaignId
     */
    public String getCampaignId() {
        return campaignId;
    }

    /** 
     * Sets the campaignId.
     * 
     * @param campaignId the campaignId
     */
    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    /** 
     * Returns the campaignName.
     * 
     * @return the campaignName
     */
    public String getCampaignName() {
        return campaignName;
    }

    /** 
     * Sets the campaignName.
     * 
     * @param campaignName the campaignName
     */
    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    /** 
     * Returns the dailyBudget.
     * 
     * @return the dailyBudget
     */
    public Long getDailyBudget() {
        return dailyBudget;
    }

    /** 
     * Sets the dailyBudget.
     * 
     * @param dailyBudget the dailyBudget
     */
    public void setDailyBudget(Long dailyBudget) {
        this.dailyBudget = dailyBudget;
    }

    /** 
     * Returns the totalBudget.
     * 
     * @return the totalBudget
     */
    public Long getTotalBudget() {
        return totalBudget;
    }

    /** 
     * Sets the totalBudget.
     * 
     * @param totalBudget the totalBudget
     */
    public void setTotalBudget(Long totalBudget) {
        this.totalBudget = totalBudget;
    }

    /** 
     * Returns the groupId.
     * 
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /** 
     * Sets the groupId.
     * 
     * @param groupId the groupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /** 
     * Returns the tweetIds.
     * 
     * @return the tweetIds
     */
    public String getTweetIds() {
        return tweetIds;
    }

    /** 
     * Sets the tweetIds.
     * 
     * @param tweetIds the tweetIds
     */
    public void setTweetIds(String tweetIds) {
        this.tweetIds = tweetIds;
    }

    /** 
     * Returns the approvalFlag.
     * 
     * @return the approvalFlag
     */
    public String getApprovalFlag() {
        return approvalFlag;
    }

    /** 
     * Sets the approvalFlag.
     * 
     * @param approvalFlag the approvalFlag
     */
    public void setApprovalFlag(String approvalFlag) {
        this.approvalFlag = approvalFlag;
    }

    /** 
     * Returns the versionNo.
     * 
     * @return the versionNo
     */
    public Long getVersionNo() {
        return versionNo;
    }

    /** 
     * Sets the versionNo.
     * 
     * @param versionNo the versionNo
     */
    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    /** 
     * Returns the createdAt.
     * 
     * @return the createdAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** 
     * Sets the createdAt.
     * 
     * @param createdAt the createdAt
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** 
     * Returns the createdBy.
     * 
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /** 
     * Sets the createdBy.
     * 
     * @param createdBy the createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** 
     * Returns the updatedAt.
     * 
     * @return the updatedAt
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /** 
     * Sets the updatedAt.
     * 
     * @param updatedAt the updatedAt
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /** 
     * Returns the updatedBy.
     * 
     * @return the updatedBy
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /** 
     * Sets the updatedBy.
     * 
     * @param updatedBy the updatedBy
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /** 
     * Returns the isActived.
     * 
     * @return the isActived
     */
    public Integer getIsActived() {
        return isActived;
    }

    /** 
     * Sets the isActived.
     * 
     * @param isActived the isActived
     */
    public void setIsActived(Integer isActived) {
        this.isActived = isActived;
    }
}