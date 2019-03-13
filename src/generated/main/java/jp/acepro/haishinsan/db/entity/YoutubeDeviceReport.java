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
@Table(name = "youtube_device_report")
public class YoutubeDeviceReport extends BaseEntity {

    /** Youtubeデバイス別レポートID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Youtube_device_report_id")
    Long youtubeDeviceReportId;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    Long campaignId;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 日付 */
    @Column(name = "date")
    String date;

    /** デバイスタイプ */
    @Column(name = "device_type")
    String deviceType;

    /** 表示回数 */
    @Column(name = "impressions")
    Long impressions;

    /** クリック数 */
    @Column(name = "clicks")
    Long clicks;

    /** 費用 */
    @Column(name = "costs")
    Double costs;

    /** 視聴回数 */
    @Column(name = "video_views")
    Long videoViews;

    /** 視聴率 */
    @Column(name = "video_view_rate")
    Double videoViewRate;

    /** 視聴単価 */
    @Column(name = "avg_cpv")
    Double avgCpv;

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
     * Returns the youtubeDeviceReportId.
     * 
     * @return the youtubeDeviceReportId
     */
    public Long getYoutubeDeviceReportId() {
        return youtubeDeviceReportId;
    }

    /** 
     * Sets the youtubeDeviceReportId.
     * 
     * @param youtubeDeviceReportId the youtubeDeviceReportId
     */
    public void setYoutubeDeviceReportId(Long youtubeDeviceReportId) {
        this.youtubeDeviceReportId = youtubeDeviceReportId;
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
     * Returns the date.
     * 
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /** 
     * Sets the date.
     * 
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /** 
     * Returns the deviceType.
     * 
     * @return the deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /** 
     * Sets the deviceType.
     * 
     * @param deviceType the deviceType
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /** 
     * Returns the impressions.
     * 
     * @return the impressions
     */
    public Long getImpressions() {
        return impressions;
    }

    /** 
     * Sets the impressions.
     * 
     * @param impressions the impressions
     */
    public void setImpressions(Long impressions) {
        this.impressions = impressions;
    }

    /** 
     * Returns the clicks.
     * 
     * @return the clicks
     */
    public Long getClicks() {
        return clicks;
    }

    /** 
     * Sets the clicks.
     * 
     * @param clicks the clicks
     */
    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }

    /** 
     * Returns the costs.
     * 
     * @return the costs
     */
    public Double getCosts() {
        return costs;
    }

    /** 
     * Sets the costs.
     * 
     * @param costs the costs
     */
    public void setCosts(Double costs) {
        this.costs = costs;
    }

    /** 
     * Returns the videoViews.
     * 
     * @return the videoViews
     */
    public Long getVideoViews() {
        return videoViews;
    }

    /** 
     * Sets the videoViews.
     * 
     * @param videoViews the videoViews
     */
    public void setVideoViews(Long videoViews) {
        this.videoViews = videoViews;
    }

    /** 
     * Returns the videoViewRate.
     * 
     * @return the videoViewRate
     */
    public Double getVideoViewRate() {
        return videoViewRate;
    }

    /** 
     * Sets the videoViewRate.
     * 
     * @param videoViewRate the videoViewRate
     */
    public void setVideoViewRate(Double videoViewRate) {
        this.videoViewRate = videoViewRate;
    }

    /** 
     * Returns the avgCpv.
     * 
     * @return the avgCpv
     */
    public Double getAvgCpv() {
        return avgCpv;
    }

    /** 
     * Sets the avgCpv.
     * 
     * @param avgCpv the avgCpv
     */
    public void setAvgCpv(Double avgCpv) {
        this.avgCpv = avgCpv;
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