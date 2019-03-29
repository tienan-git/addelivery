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
@Table(name = "operation_log")
public class OperationLog extends BaseEntity {

    /** オペレーションログID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_log_id")
    Long operationLogId;

    /** ユーザーID */
    @Column(name = "user_id")
    Long userId;

    /** オペレーション名 */
    @Column(name = "name")
    String name;

    /** オペレーション詳細 */
    @Column(name = "detail")
    String detail;

    /** ユーザーエージェント */
    @Column(name = "user_agent")
    String userAgent;

    /** IPアドレス */
    @Column(name = "ip_address")
    String ipAddress;

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
     * Returns the operationLogId.
     * 
     * @return the operationLogId
     */
    public Long getOperationLogId() {
        return operationLogId;
    }

    /** 
     * Sets the operationLogId.
     * 
     * @param operationLogId the operationLogId
     */
    public void setOperationLogId(Long operationLogId) {
        this.operationLogId = operationLogId;
    }

    /** 
     * Returns the userId.
     * 
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /** 
     * Sets the userId.
     * 
     * @param userId the userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /** 
     * Returns the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /** 
     * Sets the name.
     * 
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /** 
     * Returns the detail.
     * 
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /** 
     * Sets the detail.
     * 
     * @param detail the detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /** 
     * Returns the userAgent.
     * 
     * @return the userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /** 
     * Sets the userAgent.
     * 
     * @param userAgent the userAgent
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /** 
     * Returns the ipAddress.
     * 
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /** 
     * Sets the ipAddress.
     * 
     * @param ipAddress the ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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