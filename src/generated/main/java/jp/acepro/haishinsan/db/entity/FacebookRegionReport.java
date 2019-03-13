/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.db.entity;

import java.time.LocalDate;
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
@Table(name = "facebook_region_report")
public class FacebookRegionReport extends BaseEntity {

    /** Facebook地域別レポートID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facebook_region_report_id")
    Long facebookRegionReportId;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    String campaignId;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 日付 */
    @Column(name = "date")
    LocalDate date;

    /** 地域 */
    @Column(name = "region")
    String region;

    /** 表示回数 */
    @Column(name = "impressions")
    Long impressions;

    /** クリック数 */
    @Column(name = "clicks")
    Long clicks;

    /** 利用金額 */
    @Column(name = "spend")
    Long spend;

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
     * Returns the facebookRegionReportId.
     * 
     * @return the facebookRegionReportId
     */
    public Long getFacebookRegionReportId() {
        return facebookRegionReportId;
    }

    /** 
     * Sets the facebookRegionReportId.
     * 
     * @param facebookRegionReportId the facebookRegionReportId
     */
    public void setFacebookRegionReportId(Long facebookRegionReportId) {
        this.facebookRegionReportId = facebookRegionReportId;
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
     * Returns the date.
     * 
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /** 
     * Sets the date.
     * 
     * @param date the date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /** 
     * Returns the region.
     * 
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /** 
     * Sets the region.
     * 
     * @param region the region
     */
    public void setRegion(String region) {
        this.region = region;
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
     * Returns the spend.
     * 
     * @return the spend
     */
    public Long getSpend() {
        return spend;
    }

    /** 
     * Sets the spend.
     * 
     * @param spend the spend
     */
    public void setSpend(Long spend) {
        this.spend = spend;
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