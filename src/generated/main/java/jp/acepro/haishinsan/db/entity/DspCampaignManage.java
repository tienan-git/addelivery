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
@Table(name = "dsp_campaign_manage")
public class DspCampaignManage extends BaseEntity {

    /** DSPキャンペーン管理ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dsp_campaign_manage_id")
    Long dspCampaignManageId;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    Integer campaignId;

    /** クリエイティブID */
    @Column(name = "creative_id")
    String creativeId;

    /** セグメントID */
    @Column(name = "segment_id")
    Integer segmentId;

    /** 予算 */
    @Column(name = "budget")
    Integer budget;

    /** 審査フラグ */
    @Column(name = "approval_flag")
    String approvalFlag;

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
     * Returns the dspCampaignManageId.
     * 
     * @return the dspCampaignManageId
     */
    public Long getDspCampaignManageId() {
        return dspCampaignManageId;
    }

    /** 
     * Sets the dspCampaignManageId.
     * 
     * @param dspCampaignManageId the dspCampaignManageId
     */
    public void setDspCampaignManageId(Long dspCampaignManageId) {
        this.dspCampaignManageId = dspCampaignManageId;
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
     * Returns the creativeId.
     * 
     * @return the creativeId
     */
    public String getCreativeId() {
        return creativeId;
    }

    /** 
     * Sets the creativeId.
     * 
     * @param creativeId the creativeId
     */
    public void setCreativeId(String creativeId) {
        this.creativeId = creativeId;
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
     * Returns the budget.
     * 
     * @return the budget
     */
    public Integer getBudget() {
        return budget;
    }

    /** 
     * Sets the budget.
     * 
     * @param budget the budget
     */
    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    /** 
     * Returns the approvalFlag.
     * 
     * @return the approvalFlag
     */
    public String getApprovalFlag() {
        return approvalFlag;
    }

    /** 
     * Sets the approvalFlag.
     * 
     * @param approvalFlag the approvalFlag
     */
    public void setApprovalFlag(String approvalFlag) {
        this.approvalFlag = approvalFlag;
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