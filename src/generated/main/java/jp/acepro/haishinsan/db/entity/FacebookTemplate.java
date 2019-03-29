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
@Table(name = "facebook_template")
public class FacebookTemplate extends BaseEntity {

    /** テンプレートID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    Long templateId;

    /** テンプレート名 */
    @Column(name = "template_name")
    String templateName;

    /** テンプレート所有店舗ID */
    @Column(name = "shop_id")
    Long shopId;

    /** テンプレート優先順 */
    @Column(name = "template_priority")
    Integer templatePriority;

    /** 単価タイプ:01:クリック重視 02:表示重視 */
    @Column(name = "unit_price_type")
    String unitPriceType;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** キャンペーン目的 */
    @Column(name = "campaign_purpose")
    String campaignPurpose;

    /** 一日の予算 */
    @Column(name = "daily_budget")
    Long dailyBudget;

    /** 入札金額 */
    @Column(name = "bid_amount")
    Long bidAmount;

    /** 地域 */
    @Column(name = "geolocation")
    String geolocation;

    /** 配信媒体 */
    @Column(name = "media")
    String media;

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
     * Returns the campaignPurpose.
     * 
     * @return the campaignPurpose
     */
    public String getCampaignPurpose() {
        return campaignPurpose;
    }

    /** 
     * Sets the campaignPurpose.
     * 
     * @param campaignPurpose the campaignPurpose
     */
    public void setCampaignPurpose(String campaignPurpose) {
        this.campaignPurpose = campaignPurpose;
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
     * Returns the bidAmount.
     * 
     * @return the bidAmount
     */
    public Long getBidAmount() {
        return bidAmount;
    }

    /** 
     * Sets the bidAmount.
     * 
     * @param bidAmount the bidAmount
     */
    public void setBidAmount(Long bidAmount) {
        this.bidAmount = bidAmount;
    }

    /** 
     * Returns the geolocation.
     * 
     * @return the geolocation
     */
    public String getGeolocation() {
        return geolocation;
    }

    /** 
     * Sets the geolocation.
     * 
     * @param geolocation the geolocation
     */
    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    /** 
     * Returns the media.
     * 
     * @return the media
     */
    public String getMedia() {
        return media;
    }

    /** 
     * Sets the media.
     * 
     * @param media the media
     */
    public void setMedia(String media) {
        this.media = media;
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