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
@Table(name = "segment_report_manage")
public class SegmentReportManage extends BaseEntity {

    /** セグメントレポート管理ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "segment_report_manage_id")
    Long segmentReportManageId;

    /** セグメントID */
    @Column(name = "segment_id")
    Integer segmentId;

    /** セグメント名 */
    @Column(name = "segment_name")
    String segmentName;

    /** 対象日 */
    @Column(name = "date")
    LocalDate date;

    /** ユニークユーザ数 */
    @Column(name = "uunum")
    Integer uunum;

    /** ユニークユーザ数PC */
    @Column(name = "uunum_pc")
    Integer uunumPc;

    /** ユニークユーザ数SP */
    @Column(name = "uunum_sp")
    Integer uunumSp;

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
     * Returns the segmentReportManageId.
     * 
     * @return the segmentReportManageId
     */
    public Long getSegmentReportManageId() {
        return segmentReportManageId;
    }

    /** 
     * Sets the segmentReportManageId.
     * 
     * @param segmentReportManageId the segmentReportManageId
     */
    public void setSegmentReportManageId(Long segmentReportManageId) {
        this.segmentReportManageId = segmentReportManageId;
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
     * Returns the segmentName.
     * 
     * @return the segmentName
     */
    public String getSegmentName() {
        return segmentName;
    }

    /** 
     * Sets the segmentName.
     * 
     * @param segmentName the segmentName
     */
    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    /** 
     * Returns the date.
     * 
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /** 
     * Sets the date.
     * 
     * @param date the date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /** 
     * Returns the uunum.
     * 
     * @return the uunum
     */
    public Integer getUunum() {
        return uunum;
    }

    /** 
     * Sets the uunum.
     * 
     * @param uunum the uunum
     */
    public void setUunum(Integer uunum) {
        this.uunum = uunum;
    }

    /** 
     * Returns the uunumPc.
     * 
     * @return the uunumPc
     */
    public Integer getUunumPc() {
        return uunumPc;
    }

    /** 
     * Sets the uunumPc.
     * 
     * @param uunumPc the uunumPc
     */
    public void setUunumPc(Integer uunumPc) {
        this.uunumPc = uunumPc;
    }

    /** 
     * Returns the uunumSp.
     * 
     * @return the uunumSp
     */
    public Integer getUunumSp() {
        return uunumSp;
    }

    /** 
     * Sets the uunumSp.
     * 
     * @param uunumSp the uunumSp
     */
    public void setUunumSp(Integer uunumSp) {
        this.uunumSp = uunumSp;
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