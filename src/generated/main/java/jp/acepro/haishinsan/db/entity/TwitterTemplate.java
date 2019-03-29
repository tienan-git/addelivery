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
@Table(name = "twitter_template")
public class TwitterTemplate extends BaseEntity {

    /** テンプレートID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    Long templateId;

    /** テンプレート所有店舗ID */
    @Column(name = "shop_id")
    Long shopId;

    /** テンプレート名 */
    @Column(name = "template_name")
    String templateName;

    /** テンプレート優先順 */
    @Column(name = "template_priority")
    Integer templatePriority;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 開始日 */
    @Column(name = "start_time")
    String startTime;

    /** 終了日 */
    @Column(name = "end_time")
    String endTime;

    /** 日予算 */
    @Column(name = "daily_budget")
    Integer dailyBudget;

    /** 都道府県 */
    @Column(name = "regions")
    String regions;

    /** 地域 */
    @Column(name = "location")
    String location;

    /** 総予算 */
    @Column(name = "total_budget")
    Integer totalBudget;

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
     * Returns the startTime.
     * 
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /** 
     * Sets the startTime.
     * 
     * @param startTime the startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /** 
     * Returns the endTime.
     * 
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /** 
     * Sets the endTime.
     * 
     * @param endTime the endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /** 
     * Returns the dailyBudget.
     * 
     * @return the dailyBudget
     */
    public Integer getDailyBudget() {
        return dailyBudget;
    }

    /** 
     * Sets the dailyBudget.
     * 
     * @param dailyBudget the dailyBudget
     */
    public void setDailyBudget(Integer dailyBudget) {
        this.dailyBudget = dailyBudget;
    }

    /** 
     * Returns the regions.
     * 
     * @return the regions
     */
    public String getRegions() {
        return regions;
    }

    /** 
     * Sets the regions.
     * 
     * @param regions the regions
     */
    public void setRegions(String regions) {
        this.regions = regions;
    }

    /** 
     * Returns the location.
     * 
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /** 
     * Sets the location.
     * 
     * @param location the location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /** 
     * Returns the totalBudget.
     * 
     * @return the totalBudget
     */
    public Integer getTotalBudget() {
        return totalBudget;
    }

    /** 
     * Sets the totalBudget.
     * 
     * @param totalBudget the totalBudget
     */
    public void setTotalBudget(Integer totalBudget) {
        this.totalBudget = totalBudget;
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