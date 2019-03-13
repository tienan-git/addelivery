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
@Table(name = "youtube_campaign_manage")
public class YoutubeCampaignManage extends BaseEntity {

    /** Youtubeキャンペーン管理ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "youtube_campaign_manage_id")
    Long youtubeCampaignManageId;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    Long campaignId;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 広告タイプ:04:インストリーム広告 05:バンパー広告 */
    @Column(name = "ad_type")
    String adType;

    /** 予算 */
    @Column(name = "budget")
    Long budget;

    /** 配信開始日 */
    @Column(name = "start_date")
    String startDate;

    /** 配信終了日 */
    @Column(name = "end_date")
    String endDate;

    /** 配信地域 */
    @Column(name = "area")
    String area;

    /** LP */
    @Column(name = "lp")
    String lp;

    /** 動画URL */
    @Column(name = "video_url")
    String videoUrl;

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
     * Returns the campaignId.
     * 
     * @return the campaignId
     */
    public Long getCampaignId() {
        return campaignId;
    }

    /** 
     * Sets the campaignId.
     * 
     * @param campaignId the campaignId
     */
    public void setCampaignId(Long campaignId) {
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
     * Returns the adType.
     * 
     * @return the adType
     */
    public String getAdType() {
        return adType;
    }

    /** 
     * Sets the adType.
     * 
     * @param adType the adType
     */
    public void setAdType(String adType) {
        this.adType = adType;
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
     * Returns the area.
     * 
     * @return the area
     */
    public String getArea() {
        return area;
    }

    /** 
     * Sets the area.
     * 
     * @param area the area
     */
    public void setArea(String area) {
        this.area = area;
    }

    /** 
     * Returns the lp.
     * 
     * @return the lp
     */
    public String getLp() {
        return lp;
    }

    /** 
     * Sets the lp.
     * 
     * @param lp the lp
     */
    public void setLp(String lp) {
        this.lp = lp;
    }

    /** 
     * Returns the videoUrl.
     * 
     * @return the videoUrl
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /** 
     * Sets the videoUrl.
     * 
     * @param videoUrl the videoUrl
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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