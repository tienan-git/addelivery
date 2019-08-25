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
@Table(name = "google_campaign_manage")
public class GoogleCampaignManage extends BaseEntity {

    /** Googleキャンペーン管理ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "google_campaign_manage_id")
    Long googleCampaignManageId;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    Long campaignId;

    /** 店舗ID */
    @Column(name = "shop_id")
    Long shopId;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 予算 */
    @Column(name = "budget")
    Long budget;

    /** 地域コード */
    @Column(name = "regions")
    String regions;

    /** 広告タイプ:01:レスポンシブ広告 02:イメージ広告 03:拡張テキスト広告 */
    @Column(name = "ad_type")
    String adType;

    /** イメージ１URL */
    @Column(name = "image1_url")
    String image1Url;

    /** イメージ２URL */
    @Column(name = "image2_url")
    String image2Url;

    /** イメージ３URL */
    @Column(name = "image3_url")
    String image3Url;

    /** イメージ４URL */
    @Column(name = "image4_url")
    String image4Url;

    /** タイトル１ */
    @Column(name = "title1")
    String title1;

    /** タイトル２ */
    @Column(name = "title2")
    String title2;

    /** 説明文 */
    @Column(name = "description")
    String description;

    /** リンク先 */
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
     * Returns the googleCampaignManageId.
     * 
     * @return the googleCampaignManageId
     */
    public Long getGoogleCampaignManageId() {
        return googleCampaignManageId;
    }

    /** 
     * Sets the googleCampaignManageId.
     * 
     * @param googleCampaignManageId the googleCampaignManageId
     */
    public void setGoogleCampaignManageId(Long googleCampaignManageId) {
        this.googleCampaignManageId = googleCampaignManageId;
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
     * Returns the image1Url.
     * 
     * @return the image1Url
     */
    public String getImage1Url() {
        return image1Url;
    }

    /** 
     * Sets the image1Url.
     * 
     * @param image1Url the image1Url
     */
    public void setImage1Url(String image1Url) {
        this.image1Url = image1Url;
    }

    /** 
     * Returns the image2Url.
     * 
     * @return the image2Url
     */
    public String getImage2Url() {
        return image2Url;
    }

    /** 
     * Sets the image2Url.
     * 
     * @param image2Url the image2Url
     */
    public void setImage2Url(String image2Url) {
        this.image2Url = image2Url;
    }

    /** 
     * Returns the image3Url.
     * 
     * @return the image3Url
     */
    public String getImage3Url() {
        return image3Url;
    }

    /** 
     * Sets the image3Url.
     * 
     * @param image3Url the image3Url
     */
    public void setImage3Url(String image3Url) {
        this.image3Url = image3Url;
    }

    /** 
     * Returns the image4Url.
     * 
     * @return the image4Url
     */
    public String getImage4Url() {
        return image4Url;
    }

    /** 
     * Sets the image4Url.
     * 
     * @param image4Url the image4Url
     */
    public void setImage4Url(String image4Url) {
        this.image4Url = image4Url;
    }

    /** 
     * Returns the title1.
     * 
     * @return the title1
     */
    public String getTitle1() {
        return title1;
    }

    /** 
     * Sets the title1.
     * 
     * @param title1 the title1
     */
    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    /** 
     * Returns the title2.
     * 
     * @return the title2
     */
    public String getTitle2() {
        return title2;
    }

    /** 
     * Sets the title2.
     * 
     * @param title2 the title2
     */
    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    /** 
     * Returns the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /** 
     * Sets the description.
     * 
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
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