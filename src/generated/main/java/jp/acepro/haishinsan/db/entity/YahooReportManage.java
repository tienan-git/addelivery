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
@Table(name = "yahoo_report_manage")
public class YahooReportManage extends BaseEntity {

    /** Yahooレポート管理ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yahoo_report_manage_id")
    Long yahooReportManageId;

    /** 日 */
    @Column(name = "create_date")
    LocalDate createDate;

    /** デバイス */
    @Column(name = "device_name")
    String deviceName;

    /** 都道府県 */
    @Column(name = "region_name")
    String regionName;

    /** 市区郡 */
    @Column(name = "city_name")
    String cityName;

    /** 行政区 */
    @Column(name = "district_name")
    String districtName;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** キャンペーンタイプ */
    @Column(name = "campaign_type")
    String campaignType;

    /** 広告掲載方式 */
    @Column(name = "advertising_method")
    String advertisingMethod;

    /** インプレッション数 */
    @Column(name = "impression_count")
    Long impressionCount;

    /** クリック数 */
    @Column(name = "click_count")
    Long clickCount;

    /** コスト */
    @Column(name = "cost")
    Double cost;

    /** 平均掲載順位 */
    @Column(name = "ave_publish_rank")
    Double avePublishRank;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    Long campaignId;

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
     * Returns the yahooReportManageId.
     * 
     * @return the yahooReportManageId
     */
    public Long getYahooReportManageId() {
        return yahooReportManageId;
    }

    /** 
     * Sets the yahooReportManageId.
     * 
     * @param yahooReportManageId the yahooReportManageId
     */
    public void setYahooReportManageId(Long yahooReportManageId) {
        this.yahooReportManageId = yahooReportManageId;
    }

    /** 
     * Returns the createDate.
     * 
     * @return the createDate
     */
    public LocalDate getCreateDate() {
        return createDate;
    }

    /** 
     * Sets the createDate.
     * 
     * @param createDate the createDate
     */
    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    /** 
     * Returns the deviceName.
     * 
     * @return the deviceName
     */
    public String getDeviceName() {
        return deviceName;
    }

    /** 
     * Sets the deviceName.
     * 
     * @param deviceName the deviceName
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /** 
     * Returns the regionName.
     * 
     * @return the regionName
     */
    public String getRegionName() {
        return regionName;
    }

    /** 
     * Sets the regionName.
     * 
     * @param regionName the regionName
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /** 
     * Returns the cityName.
     * 
     * @return the cityName
     */
    public String getCityName() {
        return cityName;
    }

    /** 
     * Sets the cityName.
     * 
     * @param cityName the cityName
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /** 
     * Returns the districtName.
     * 
     * @return the districtName
     */
    public String getDistrictName() {
        return districtName;
    }

    /** 
     * Sets the districtName.
     * 
     * @param districtName the districtName
     */
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
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
     * Returns the campaignType.
     * 
     * @return the campaignType
     */
    public String getCampaignType() {
        return campaignType;
    }

    /** 
     * Sets the campaignType.
     * 
     * @param campaignType the campaignType
     */
    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    /** 
     * Returns the advertisingMethod.
     * 
     * @return the advertisingMethod
     */
    public String getAdvertisingMethod() {
        return advertisingMethod;
    }

    /** 
     * Sets the advertisingMethod.
     * 
     * @param advertisingMethod the advertisingMethod
     */
    public void setAdvertisingMethod(String advertisingMethod) {
        this.advertisingMethod = advertisingMethod;
    }

    /** 
     * Returns the impressionCount.
     * 
     * @return the impressionCount
     */
    public Long getImpressionCount() {
        return impressionCount;
    }

    /** 
     * Sets the impressionCount.
     * 
     * @param impressionCount the impressionCount
     */
    public void setImpressionCount(Long impressionCount) {
        this.impressionCount = impressionCount;
    }

    /** 
     * Returns the clickCount.
     * 
     * @return the clickCount
     */
    public Long getClickCount() {
        return clickCount;
    }

    /** 
     * Sets the clickCount.
     * 
     * @param clickCount the clickCount
     */
    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    /** 
     * Returns the cost.
     * 
     * @return the cost
     */
    public Double getCost() {
        return cost;
    }

    /** 
     * Sets the cost.
     * 
     * @param cost the cost
     */
    public void setCost(Double cost) {
        this.cost = cost;
    }

    /** 
     * Returns the avePublishRank.
     * 
     * @return the avePublishRank
     */
    public Double getAvePublishRank() {
        return avePublishRank;
    }

    /** 
     * Sets the avePublishRank.
     * 
     * @param avePublishRank the avePublishRank
     */
    public void setAvePublishRank(Double avePublishRank) {
        this.avePublishRank = avePublishRank;
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