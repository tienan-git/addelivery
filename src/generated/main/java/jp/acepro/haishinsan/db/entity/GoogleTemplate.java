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
@Table(name = "google_template")
public class GoogleTemplate extends BaseEntity {

    /** テンプレートID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    Long templateId;

    /** テンプレート所有店舗ID:テンプレート所有店舗ID */
    @Column(name = "shop_id")
    Long shopId;

    /** テンプレート名 */
    @Column(name = "template_name")
    String templateName;

    /** テンプレート優先順:数値が小さいほど、優先順が高い */
    @Column(name = "template_priority")
    Integer templatePriority;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 予算 */
    @Column(name = "budget")
    Long budget;

    /** デバイスタイプ:01:パソコン 02:モバイル 03:全て */
    @Column(name = "device_type")
    String deviceType;

    /** 地域 */
    @Column(name = "location_list")
    String locationList;

    /** 単価タイプ:01:クリック重視 02:表示重視 */
    @Column(name = "unit_price_type")
    String unitPriceType;

    /** 広告タイプ:01:レスポンシブ広告 02:イメージ広告 03:拡張テキスト広告 */
    @Column(name = "ad_type")
    String adType;

    /** レスポンシブ広告短い見出し */
    @Column(name = "res_ad_short_title")
    String resAdShortTitle;

    /** レスポンシブ広告説明文 */
    @Column(name = "res_ad_description")
    String resAdDescription;

    /** レスポンシブ広告最終ページURL */
    @Column(name = "res_ad_final_page_url")
    String resAdFinalPageUrl;

    /** イメージ広告最終ページURL */
    @Column(name = "image_ad_final_page_url")
    String imageAdFinalPageUrl;

    /** テキスト広告最終ページURL */
    @Column(name = "text_ad_final_page_url")
    String textAdFinalPageUrl;

    /** テキスト広告見出し１ */
    @Column(name = "text_ad_title_1")
    String textAdTitle1;

    /** テキスト広告見出し２ */
    @Column(name = "text_ad_title_2")
    String textAdTitle2;

    /** テキスト広告説明文 */
    @Column(name = "text_ad_description")
    String textAdDescription;

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
     * Returns the templateId.
     * 
     * @return the templateId
     */
    public Long getTemplateId() {
        return templateId;
    }

    /** 
     * Sets the templateId.
     * 
     * @param templateId the templateId
     */
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
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
     * Returns the templateName.
     * 
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /** 
     * Sets the templateName.
     * 
     * @param templateName the templateName
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /** 
     * Returns the templatePriority.
     * 
     * @return the templatePriority
     */
    public Integer getTemplatePriority() {
        return templatePriority;
    }

    /** 
     * Sets the templatePriority.
     * 
     * @param templatePriority the templatePriority
     */
    public void setTemplatePriority(Integer templatePriority) {
        this.templatePriority = templatePriority;
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
     * Returns the locationList.
     * 
     * @return the locationList
     */
    public String getLocationList() {
        return locationList;
    }

    /** 
     * Sets the locationList.
     * 
     * @param locationList the locationList
     */
    public void setLocationList(String locationList) {
        this.locationList = locationList;
    }

    /** 
     * Returns the unitPriceType.
     * 
     * @return the unitPriceType
     */
    public String getUnitPriceType() {
        return unitPriceType;
    }

    /** 
     * Sets the unitPriceType.
     * 
     * @param unitPriceType the unitPriceType
     */
    public void setUnitPriceType(String unitPriceType) {
        this.unitPriceType = unitPriceType;
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
     * Returns the resAdShortTitle.
     * 
     * @return the resAdShortTitle
     */
    public String getResAdShortTitle() {
        return resAdShortTitle;
    }

    /** 
     * Sets the resAdShortTitle.
     * 
     * @param resAdShortTitle the resAdShortTitle
     */
    public void setResAdShortTitle(String resAdShortTitle) {
        this.resAdShortTitle = resAdShortTitle;
    }

    /** 
     * Returns the resAdDescription.
     * 
     * @return the resAdDescription
     */
    public String getResAdDescription() {
        return resAdDescription;
    }

    /** 
     * Sets the resAdDescription.
     * 
     * @param resAdDescription the resAdDescription
     */
    public void setResAdDescription(String resAdDescription) {
        this.resAdDescription = resAdDescription;
    }

    /** 
     * Returns the resAdFinalPageUrl.
     * 
     * @return the resAdFinalPageUrl
     */
    public String getResAdFinalPageUrl() {
        return resAdFinalPageUrl;
    }

    /** 
     * Sets the resAdFinalPageUrl.
     * 
     * @param resAdFinalPageUrl the resAdFinalPageUrl
     */
    public void setResAdFinalPageUrl(String resAdFinalPageUrl) {
        this.resAdFinalPageUrl = resAdFinalPageUrl;
    }

    /** 
     * Returns the imageAdFinalPageUrl.
     * 
     * @return the imageAdFinalPageUrl
     */
    public String getImageAdFinalPageUrl() {
        return imageAdFinalPageUrl;
    }

    /** 
     * Sets the imageAdFinalPageUrl.
     * 
     * @param imageAdFinalPageUrl the imageAdFinalPageUrl
     */
    public void setImageAdFinalPageUrl(String imageAdFinalPageUrl) {
        this.imageAdFinalPageUrl = imageAdFinalPageUrl;
    }

    /** 
     * Returns the textAdFinalPageUrl.
     * 
     * @return the textAdFinalPageUrl
     */
    public String getTextAdFinalPageUrl() {
        return textAdFinalPageUrl;
    }

    /** 
     * Sets the textAdFinalPageUrl.
     * 
     * @param textAdFinalPageUrl the textAdFinalPageUrl
     */
    public void setTextAdFinalPageUrl(String textAdFinalPageUrl) {
        this.textAdFinalPageUrl = textAdFinalPageUrl;
    }

    /** 
     * Returns the textAdTitle1.
     * 
     * @return the textAdTitle1
     */
    public String getTextAdTitle1() {
        return textAdTitle1;
    }

    /** 
     * Sets the textAdTitle1.
     * 
     * @param textAdTitle1 the textAdTitle1
     */
    public void setTextAdTitle1(String textAdTitle1) {
        this.textAdTitle1 = textAdTitle1;
    }

    /** 
     * Returns the textAdTitle2.
     * 
     * @return the textAdTitle2
     */
    public String getTextAdTitle2() {
        return textAdTitle2;
    }

    /** 
     * Sets the textAdTitle2.
     * 
     * @param textAdTitle2 the textAdTitle2
     */
    public void setTextAdTitle2(String textAdTitle2) {
        this.textAdTitle2 = textAdTitle2;
    }

    /** 
     * Returns the textAdDescription.
     * 
     * @return the textAdDescription
     */
    public String getTextAdDescription() {
        return textAdDescription;
    }

    /** 
     * Sets the textAdDescription.
     * 
     * @param textAdDescription the textAdDescription
     */
    public void setTextAdDescription(String textAdDescription) {
        this.textAdDescription = textAdDescription;
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