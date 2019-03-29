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
@Table(name = "dsp_report_manage")
public class DspReportManage extends BaseEntity {

    /** DSPレポート管理ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dsp_report_manage_id")
    Long dspReportManageId;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    Integer campaignId;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 広告グループID */
    @Column(name = "ad_group_id")
    Integer adGroupId;

    /** 広告ID */
    @Column(name = "ad_id")
    Integer adId;

    /** クリエイティブID */
    @Column(name = "creative_id")
    Integer creativeId;

    /** クリエイティブ名 */
    @Column(name = "creative_name")
    String creativeName;

    /** デバイスタイプ */
    @Column(name = "device_type")
    Integer deviceType;

    /** 対象日 */
    @Column(name = "date")
    LocalDate date;

    /** 表示回数 */
    @Column(name = "impression_count")
    Integer impressionCount;

    /** クリック回数 */
    @Column(name = "click_count")
    Integer clickCount;

    /** 広告費用 */
    @Column(name = "price")
    Double price;

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
     * Returns the dspReportManageId.
     * 
     * @return the dspReportManageId
     */
    public Long getDspReportManageId() {
        return dspReportManageId;
    }

    /** 
     * Sets the dspReportManageId.
     * 
     * @param dspReportManageId the dspReportManageId
     */
    public void setDspReportManageId(Long dspReportManageId) {
        this.dspReportManageId = dspReportManageId;
    }

    /** 
     * Returns the campaignId.
     * 
     * @return the campaignId
     */
    public Integer getCampaignId() {
        return campaignId;
    }

    /** 
     * Sets the campaignId.
     * 
     * @param campaignId the campaignId
     */
    public void setCampaignId(Integer campaignId) {
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
     * Returns the adGroupId.
     * 
     * @return the adGroupId
     */
    public Integer getAdGroupId() {
        return adGroupId;
    }

    /** 
     * Sets the adGroupId.
     * 
     * @param adGroupId the adGroupId
     */
    public void setAdGroupId(Integer adGroupId) {
        this.adGroupId = adGroupId;
    }

    /** 
     * Returns the adId.
     * 
     * @return the adId
     */
    public Integer getAdId() {
        return adId;
    }

    /** 
     * Sets the adId.
     * 
     * @param adId the adId
     */
    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    /** 
     * Returns the creativeId.
     * 
     * @return the creativeId
     */
    public Integer getCreativeId() {
        return creativeId;
    }

    /** 
     * Sets the creativeId.
     * 
     * @param creativeId the creativeId
     */
    public void setCreativeId(Integer creativeId) {
        this.creativeId = creativeId;
    }

    /** 
     * Returns the creativeName.
     * 
     * @return the creativeName
     */
    public String getCreativeName() {
        return creativeName;
    }

    /** 
     * Sets the creativeName.
     * 
     * @param creativeName the creativeName
     */
    public void setCreativeName(String creativeName) {
        this.creativeName = creativeName;
    }

    /** 
     * Returns the deviceType.
     * 
     * @return the deviceType
     */
    public Integer getDeviceType() {
        return deviceType;
    }

    /** 
     * Sets the deviceType.
     * 
     * @param deviceType the deviceType
     */
    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
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
     * Returns the impressionCount.
     * 
     * @return the impressionCount
     */
    public Integer getImpressionCount() {
        return impressionCount;
    }

    /** 
     * Sets the impressionCount.
     * 
     * @param impressionCount the impressionCount
     */
    public void setImpressionCount(Integer impressionCount) {
        this.impressionCount = impressionCount;
    }

    /** 
     * Returns the clickCount.
     * 
     * @return the clickCount
     */
    public Integer getClickCount() {
        return clickCount;
    }

    /** 
     * Sets the clickCount.
     * 
     * @param clickCount the clickCount
     */
    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    /** 
     * Returns the price.
     * 
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /** 
     * Sets the price.
     * 
     * @param price the price
     */
    public void setPrice(Double price) {
        this.price = price;
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