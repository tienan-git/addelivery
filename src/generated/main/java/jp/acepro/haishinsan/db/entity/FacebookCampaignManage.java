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
@Table(name = "facebook_campaign_manage")
public class FacebookCampaignManage extends BaseEntity {

    /** Facebookキャンペーン管理ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Facebook_campaign_manage_id")
    Long facebookCampaignManageId;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    String campaignId;

    /** 店舗ID */
    @Column(name = "shop_id")
    Long shopId;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 予算 */
    @Column(name = "budget")
    Long budget;

    /** セグメントID */
    @Column(name = "segment_id")
    Integer segmentId;

    /** 地域コード */
    @Column(name = "regions")
    String regions;

    /** イメージURL */
    @Column(name = "image_url")
    String imageUrl;

    /** リンク先URL */
    @Column(name = "link_url")
    String linkUrl;

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
     * Returns the facebookCampaignManageId.
     * 
     * @return the facebookCampaignManageId
     */
    public Long getFacebookCampaignManageId() {
        return facebookCampaignManageId;
    }

    /** 
     * Sets the facebookCampaignManageId.
     * 
     * @param facebookCampaignManageId the facebookCampaignManageId
     */
    public void setFacebookCampaignManageId(Long facebookCampaignManageId) {
        this.facebookCampaignManageId = facebookCampaignManageId;
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
     * Returns the segmentId.
     * 
     * @return the segmentId
     */
    public Integer getSegmentId() {
        return segmentId;
    }

    /** 
     * Sets the segmentId.
     * 
     * @param segmentId the segmentId
     */
    public void setSegmentId(Integer segmentId) {
        this.segmentId = segmentId;
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
     * Returns the imageUrl.
     * 
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /** 
     * Sets the imageUrl.
     * 
     * @param imageUrl the imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /** 
     * Returns the linkUrl.
     * 
     * @return the linkUrl
     */
    public String getLinkUrl() {
        return linkUrl;
    }

    /** 
     * Sets the linkUrl.
     * 
     * @param linkUrl the linkUrl
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
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