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
@Table(name = "dsp_ad_manage")
public class DspAdManage extends BaseEntity {

    /** DSP広告管理ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dsp_ad_manage_id")
    Long dspAdManageId;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    Integer campaignId;

    /** 広告グループID */
    @Column(name = "ad_group_id")
    Integer adGroupId;

    /** 広告ID */
    @Column(name = "ad_id")
    Integer adId;

    /** クリエイティブID */
    @Column(name = "creative_id")
    Integer creativeId;

    /** デバイスタイプ */
    @Column(name = "device_type")
    Integer deviceType;

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
     * Returns the dspAdManageId.
     * 
     * @return the dspAdManageId
     */
    public Long getDspAdManageId() {
        return dspAdManageId;
    }

    /** 
     * Sets the dspAdManageId.
     * 
     * @param dspAdManageId the dspAdManageId
     */
    public void setDspAdManageId(Long dspAdManageId) {
        this.dspAdManageId = dspAdManageId;
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