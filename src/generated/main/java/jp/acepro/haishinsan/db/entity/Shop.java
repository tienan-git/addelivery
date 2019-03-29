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
@Table(name = "shop")
public class Shop extends BaseEntity {

    /** 店舗ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id")
    Long shopId;

    /** 店舗名 */
    @Column(name = "shop_name")
    String shopName;

    /** 法人ID */
    @Column(name = "corporation_id")
    Long corporationId;

    /** DSPユーザーID */
    @Column(name = "dsp_user_id")
    Integer dspUserId;

    /** GoogleアカウントID */
    @Column(name = "google_account_id")
    String googleAccountId;

    /** FacebookページID */
    @Column(name = "facebook_page_id")
    String facebookPageId;

    /** TwitterアカウントID */
    @Column(name = "twitter_account_id")
    String twitterAccountId;

    /** DSP分配率 */
    @Column(name = "dsp_distribution_ratio")
    Integer dspDistributionRatio;

    /** Google分配率 */
    @Column(name = "google_distribution_ratio")
    Integer googleDistributionRatio;

    /** Facebook分配率 */
    @Column(name = "facebook_distribution_ratio")
    Integer facebookDistributionRatio;

    /** Twitter分配率 */
    @Column(name = "twitter_distribution_ratio")
    Integer twitterDistributionRatio;

    /** 営業審査フラグ */
    @Column(name = "sales_check_flag")
    String salesCheckFlag;

    /** マージン率 */
    @Column(name = "margin_ratio")
    Integer marginRatio;

    /** 店舗通知メール先 */
    @Column(name = "shop_mail_list")
    String shopMailList;

    /** 営業通知メール先 */
    @Column(name = "sales_mail_list")
    String salesMailList;

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
     * Returns the shopName.
     * 
     * @return the shopName
     */
    public String getShopName() {
        return shopName;
    }

    /** 
     * Sets the shopName.
     * 
     * @param shopName the shopName
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /** 
     * Returns the corporationId.
     * 
     * @return the corporationId
     */
    public Long getCorporationId() {
        return corporationId;
    }

    /** 
     * Sets the corporationId.
     * 
     * @param corporationId the corporationId
     */
    public void setCorporationId(Long corporationId) {
        this.corporationId = corporationId;
    }

    /** 
     * Returns the dspUserId.
     * 
     * @return the dspUserId
     */
    public Integer getDspUserId() {
        return dspUserId;
    }

    /** 
     * Sets the dspUserId.
     * 
     * @param dspUserId the dspUserId
     */
    public void setDspUserId(Integer dspUserId) {
        this.dspUserId = dspUserId;
    }

    /** 
     * Returns the googleAccountId.
     * 
     * @return the googleAccountId
     */
    public String getGoogleAccountId() {
        return googleAccountId;
    }

    /** 
     * Sets the googleAccountId.
     * 
     * @param googleAccountId the googleAccountId
     */
    public void setGoogleAccountId(String googleAccountId) {
        this.googleAccountId = googleAccountId;
    }

    /** 
     * Returns the facebookPageId.
     * 
     * @return the facebookPageId
     */
    public String getFacebookPageId() {
        return facebookPageId;
    }

    /** 
     * Sets the facebookPageId.
     * 
     * @param facebookPageId the facebookPageId
     */
    public void setFacebookPageId(String facebookPageId) {
        this.facebookPageId = facebookPageId;
    }

    /** 
     * Returns the twitterAccountId.
     * 
     * @return the twitterAccountId
     */
    public String getTwitterAccountId() {
        return twitterAccountId;
    }

    /** 
     * Sets the twitterAccountId.
     * 
     * @param twitterAccountId the twitterAccountId
     */
    public void setTwitterAccountId(String twitterAccountId) {
        this.twitterAccountId = twitterAccountId;
    }

    /** 
     * Returns the dspDistributionRatio.
     * 
     * @return the dspDistributionRatio
     */
    public Integer getDspDistributionRatio() {
        return dspDistributionRatio;
    }

    /** 
     * Sets the dspDistributionRatio.
     * 
     * @param dspDistributionRatio the dspDistributionRatio
     */
    public void setDspDistributionRatio(Integer dspDistributionRatio) {
        this.dspDistributionRatio = dspDistributionRatio;
    }

    /** 
     * Returns the googleDistributionRatio.
     * 
     * @return the googleDistributionRatio
     */
    public Integer getGoogleDistributionRatio() {
        return googleDistributionRatio;
    }

    /** 
     * Sets the googleDistributionRatio.
     * 
     * @param googleDistributionRatio the googleDistributionRatio
     */
    public void setGoogleDistributionRatio(Integer googleDistributionRatio) {
        this.googleDistributionRatio = googleDistributionRatio;
    }

    /** 
     * Returns the facebookDistributionRatio.
     * 
     * @return the facebookDistributionRatio
     */
    public Integer getFacebookDistributionRatio() {
        return facebookDistributionRatio;
    }

    /** 
     * Sets the facebookDistributionRatio.
     * 
     * @param facebookDistributionRatio the facebookDistributionRatio
     */
    public void setFacebookDistributionRatio(Integer facebookDistributionRatio) {
        this.facebookDistributionRatio = facebookDistributionRatio;
    }

    /** 
     * Returns the twitterDistributionRatio.
     * 
     * @return the twitterDistributionRatio
     */
    public Integer getTwitterDistributionRatio() {
        return twitterDistributionRatio;
    }

    /** 
     * Sets the twitterDistributionRatio.
     * 
     * @param twitterDistributionRatio the twitterDistributionRatio
     */
    public void setTwitterDistributionRatio(Integer twitterDistributionRatio) {
        this.twitterDistributionRatio = twitterDistributionRatio;
    }

    /** 
     * Returns the salesCheckFlag.
     * 
     * @return the salesCheckFlag
     */
    public String getSalesCheckFlag() {
        return salesCheckFlag;
    }

    /** 
     * Sets the salesCheckFlag.
     * 
     * @param salesCheckFlag the salesCheckFlag
     */
    public void setSalesCheckFlag(String salesCheckFlag) {
        this.salesCheckFlag = salesCheckFlag;
    }

    /** 
     * Returns the marginRatio.
     * 
     * @return the marginRatio
     */
    public Integer getMarginRatio() {
        return marginRatio;
    }

    /** 
     * Sets the marginRatio.
     * 
     * @param marginRatio the marginRatio
     */
    public void setMarginRatio(Integer marginRatio) {
        this.marginRatio = marginRatio;
    }

    /** 
     * Returns the shopMailList.
     * 
     * @return the shopMailList
     */
    public String getShopMailList() {
        return shopMailList;
    }

    /** 
     * Sets the shopMailList.
     * 
     * @param shopMailList the shopMailList
     */
    public void setShopMailList(String shopMailList) {
        this.shopMailList = shopMailList;
    }

    /** 
     * Returns the salesMailList.
     * 
     * @return the salesMailList
     */
    public String getSalesMailList() {
        return salesMailList;
    }

    /** 
     * Sets the salesMailList.
     * 
     * @param salesMailList the salesMailList
     */
    public void setSalesMailList(String salesMailList) {
        this.salesMailList = salesMailList;
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