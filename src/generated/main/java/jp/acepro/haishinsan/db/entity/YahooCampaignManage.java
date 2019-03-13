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
@Table(name = "yahoo_campaign_manage")
public class YahooCampaignManage extends BaseEntity {

    /** Yahooキャンペーン管理ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yahoo_campaign_manage_id")
    Long yahooCampaignManageId;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    String campaignId;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 出稿先 */
    @Column(name = "adv_destination")
    String advDestination;

    /** デバイスタイプ */
    @Column(name = "device_type")
    String deviceType;

    /** 配信地域 */
    @Column(name = "area")
    String area;

    /** リンク先 */
    @Column(name = "url")
    String url;

    /** 短い広告見出し */
    @Column(name = "ad_short_title")
    String adShortTitle;

    /** 広告見出し１ */
    @Column(name = "ad_title1")
    String adTitle1;

    /** 広告見出し２ */
    @Column(name = "ad_title2")
    String adTitle2;

    /** 説明文 */
    @Column(name = "ad_description")
    String adDescription;

    /** 画像名 */
    @Column(name = "image_name")
    String imageName;

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
     * Returns the advDestination.
     * 
     * @return the advDestination
     */
    public String getAdvDestination() {
        return advDestination;
    }

    /** 
     * Sets the advDestination.
     * 
     * @param advDestination the advDestination
     */
    public void setAdvDestination(String advDestination) {
        this.advDestination = advDestination;
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
     * Returns the url.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /** 
     * Sets the url.
     * 
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /** 
     * Returns the adShortTitle.
     * 
     * @return the adShortTitle
     */
    public String getAdShortTitle() {
        return adShortTitle;
    }

    /** 
     * Sets the adShortTitle.
     * 
     * @param adShortTitle the adShortTitle
     */
    public void setAdShortTitle(String adShortTitle) {
        this.adShortTitle = adShortTitle;
    }

    /** 
     * Returns the adTitle1.
     * 
     * @return the adTitle1
     */
    public String getAdTitle1() {
        return adTitle1;
    }

    /** 
     * Sets the adTitle1.
     * 
     * @param adTitle1 the adTitle1
     */
    public void setAdTitle1(String adTitle1) {
        this.adTitle1 = adTitle1;
    }

    /** 
     * Returns the adTitle2.
     * 
     * @return the adTitle2
     */
    public String getAdTitle2() {
        return adTitle2;
    }

    /** 
     * Sets the adTitle2.
     * 
     * @param adTitle2 the adTitle2
     */
    public void setAdTitle2(String adTitle2) {
        this.adTitle2 = adTitle2;
    }

    /** 
     * Returns the adDescription.
     * 
     * @return the adDescription
     */
    public String getAdDescription() {
        return adDescription;
    }

    /** 
     * Sets the adDescription.
     * 
     * @param adDescription the adDescription
     */
    public void setAdDescription(String adDescription) {
        this.adDescription = adDescription;
    }

    /** 
     * Returns the imageName.
     * 
     * @return the imageName
     */
    public String getImageName() {
        return imageName;
    }

    /** 
     * Sets the imageName.
     * 
     * @param imageName the imageName
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
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