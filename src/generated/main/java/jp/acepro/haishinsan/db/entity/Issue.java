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
@Table(name = "issue")
public class Issue extends BaseEntity {

    /** 案件ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    Long issueId;

    /** 店舗ID */
    @Column(name = "shop_id")
    Long shopId;

    /** DSPキャンペーン管理ID */
    @Column(name = "dsp_campaign_manage_id")
    Long dspCampaignManageId;

    /** Twitterキャンペーン管理ID */
    @Column(name = "twitter_campaign_manage_id")
    Long twitterCampaignManageId;

    /** Yahooキャンペーン管理ID */
    @Column(name = "yahoo_campaign_manage_id")
    Long yahooCampaignManageId;

    /** Youtubeキャンペーン管理ID */
    @Column(name = "youtube_campaign_manage_id")
    Long youtubeCampaignManageId;

    /** GoogleキャンペーンID */
    @Column(name = "google_campaign_id")
    Long googleCampaignId;

    /** FacebookキャンペーンID */
    @Column(name = "facebook_campaign_id")
    String facebookCampaignId;

    /** InstagramキャンペーンID */
    @Column(name = "instagram_campaign_id")
    String instagramCampaignId;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 全体予算 */
    @Column(name = "budget")
    Long budget;

    /** 配信開始日 */
    @Column(name = "start_date")
    String startDate;

    /** 配信終了日 */
    @Column(name = "end_date")
    String endDate;

    /** Google日別予算 */
    @Column(name = "google_oneday_budget")
    Long googleOnedayBudget;

    /** Google地域コード */
    @Column(name = "google_regions")
    String googleRegions;

    /** Facebook一日予算 */
    @Column(name = "facebook_oneday_budget")
    Long facebookOnedayBudget;

    /** Facebook地域コード */
    @Column(name = "facebook_regions")
    String facebookRegions;

    /** 配信開始タイムスタンプ */
    @Column(name = "start_timestamp")
    LocalDateTime startTimestamp;

    /** 配信終了タイムスタンプ */
    @Column(name = "end_timestamp")
    LocalDateTime endTimestamp;

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
     * Returns the issueId.
     * 
     * @return the issueId
     */
    public Long getIssueId() {
        return issueId;
    }

    /** 
     * Sets the issueId.
     * 
     * @param issueId the issueId
     */
    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    /** 
     * Returns the shopId.
     * 
     * @return the shopId
     */
    public Long getShopId() {
        return shopId;
    }

    /** 
     * Sets the shopId.
     * 
     * @param shopId the shopId
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    /** 
     * Returns the dspCampaignManageId.
     * 
     * @return the dspCampaignManageId
     */
    public Long getDspCampaignManageId() {
        return dspCampaignManageId;
    }

    /** 
     * Sets the dspCampaignManageId.
     * 
     * @param dspCampaignManageId the dspCampaignManageId
     */
    public void setDspCampaignManageId(Long dspCampaignManageId) {
        this.dspCampaignManageId = dspCampaignManageId;
    }

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
     * Returns the yahooCampaignManageId.
     * 
     * @return the yahooCampaignManageId
     */
    public Long getYahooCampaignManageId() {
        return yahooCampaignManageId;
    }

    /** 
     * Sets the yahooCampaignManageId.
     * 
     * @param yahooCampaignManageId the yahooCampaignManageId
     */
    public void setYahooCampaignManageId(Long yahooCampaignManageId) {
        this.yahooCampaignManageId = yahooCampaignManageId;
    }

    /** 
     * Returns the youtubeCampaignManageId.
     * 
     * @return the youtubeCampaignManageId
     */
    public Long getYoutubeCampaignManageId() {
        return youtubeCampaignManageId;
    }

    /** 
     * Sets the youtubeCampaignManageId.
     * 
     * @param youtubeCampaignManageId the youtubeCampaignManageId
     */
    public void setYoutubeCampaignManageId(Long youtubeCampaignManageId) {
        this.youtubeCampaignManageId = youtubeCampaignManageId;
    }

    /** 
     * Returns the googleCampaignId.
     * 
     * @return the googleCampaignId
     */
    public Long getGoogleCampaignId() {
        return googleCampaignId;
    }

    /** 
     * Sets the googleCampaignId.
     * 
     * @param googleCampaignId the googleCampaignId
     */
    public void setGoogleCampaignId(Long googleCampaignId) {
        this.googleCampaignId = googleCampaignId;
    }

    /** 
     * Returns the facebookCampaignId.
     * 
     * @return the facebookCampaignId
     */
    public String getFacebookCampaignId() {
        return facebookCampaignId;
    }

    /** 
     * Sets the facebookCampaignId.
     * 
     * @param facebookCampaignId the facebookCampaignId
     */
    public void setFacebookCampaignId(String facebookCampaignId) {
        this.facebookCampaignId = facebookCampaignId;
    }

    /** 
     * Returns the instagramCampaignId.
     * 
     * @return the instagramCampaignId
     */
    public String getInstagramCampaignId() {
        return instagramCampaignId;
    }

    /** 
     * Sets the instagramCampaignId.
     * 
     * @param instagramCampaignId the instagramCampaignId
     */
    public void setInstagramCampaignId(String instagramCampaignId) {
        this.instagramCampaignId = instagramCampaignId;
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
     * Returns the budget.
     * 
     * @return the budget
     */
    public Long getBudget() {
        return budget;
    }

    /** 
     * Sets the budget.
     * 
     * @param budget the budget
     */
    public void setBudget(Long budget) {
        this.budget = budget;
    }

    /** 
     * Returns the startDate.
     * 
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /** 
     * Sets the startDate.
     * 
     * @param startDate the startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /** 
     * Returns the endDate.
     * 
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /** 
     * Sets the endDate.
     * 
     * @param endDate the endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /** 
     * Returns the googleOnedayBudget.
     * 
     * @return the googleOnedayBudget
     */
    public Long getGoogleOnedayBudget() {
        return googleOnedayBudget;
    }

    /** 
     * Sets the googleOnedayBudget.
     * 
     * @param googleOnedayBudget the googleOnedayBudget
     */
    public void setGoogleOnedayBudget(Long googleOnedayBudget) {
        this.googleOnedayBudget = googleOnedayBudget;
    }

    /** 
     * Returns the googleRegions.
     * 
     * @return the googleRegions
     */
    public String getGoogleRegions() {
        return googleRegions;
    }

    /** 
     * Sets the googleRegions.
     * 
     * @param googleRegions the googleRegions
     */
    public void setGoogleRegions(String googleRegions) {
        this.googleRegions = googleRegions;
    }

    /** 
     * Returns the facebookOnedayBudget.
     * 
     * @return the facebookOnedayBudget
     */
    public Long getFacebookOnedayBudget() {
        return facebookOnedayBudget;
    }

    /** 
     * Sets the facebookOnedayBudget.
     * 
     * @param facebookOnedayBudget the facebookOnedayBudget
     */
    public void setFacebookOnedayBudget(Long facebookOnedayBudget) {
        this.facebookOnedayBudget = facebookOnedayBudget;
    }

    /** 
     * Returns the facebookRegions.
     * 
     * @return the facebookRegions
     */
    public String getFacebookRegions() {
        return facebookRegions;
    }

    /** 
     * Sets the facebookRegions.
     * 
     * @param facebookRegions the facebookRegions
     */
    public void setFacebookRegions(String facebookRegions) {
        this.facebookRegions = facebookRegions;
    }

    /** 
     * Returns the startTimestamp.
     * 
     * @return the startTimestamp
     */
    public LocalDateTime getStartTimestamp() {
        return startTimestamp;
    }

    /** 
     * Sets the startTimestamp.
     * 
     * @param startTimestamp the startTimestamp
     */
    public void setStartTimestamp(LocalDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    /** 
     * Returns the endTimestamp.
     * 
     * @return the endTimestamp
     */
    public LocalDateTime getEndTimestamp() {
        return endTimestamp;
    }

    /** 
     * Sets the endTimestamp.
     * 
     * @param endTimestamp the endTimestamp
     */
    public void setEndTimestamp(LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
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