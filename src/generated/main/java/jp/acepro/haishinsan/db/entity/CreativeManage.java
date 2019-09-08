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
@Table(name = "creative_manage")
public class CreativeManage extends BaseEntity {

    /** クリエイティブ管理ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creative_manage_id")
    Long creativeManageId;

    /** クリエイティブID */
    @Column(name = "creative_id")
    Integer creativeId;

    /** クリエイティブURL */
    @Column(name = "url")
    String url;

    /** クリエイティブ名 */
    @Column(name = "creative_name")
    String creativeName;

    /** 店舗ID */
    @Column(name = "shop_id")
    Long shopId;

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

    /**  */
    @Column(name = "screening")
    Integer screening;

    /** 
     * Returns the creativeManageId.
     * 
     * @return the creativeManageId
     */
    public Long getCreativeManageId() {
        return creativeManageId;
    }

    /** 
     * Sets the creativeManageId.
     * 
     * @param creativeManageId the creativeManageId
     */
    public void setCreativeManageId(Long creativeManageId) {
        this.creativeManageId = creativeManageId;
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
     * Returns the url.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /** 
     * Sets the url.
     * 
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /** 
     * Returns the creativeName.
     * 
     * @return the creativeName
     */
    public String getCreativeName() {
        return creativeName;
    }

    /** 
     * Sets the creativeName.
     * 
     * @param creativeName the creativeName
     */
    public void setCreativeName(String creativeName) {
        this.creativeName = creativeName;
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

    /** 
     * Returns the screening.
     * 
     * @return the screening
     */
    public Integer getScreening() {
        return screening;
    }

    /** 
     * Sets the screening.
     * 
     * @param screening the screening
     */
    public void setScreening(Integer screening) {
        this.screening = screening;
    }
}