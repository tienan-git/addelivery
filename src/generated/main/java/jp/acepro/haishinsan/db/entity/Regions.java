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
@Table(name = "regions")
public class Regions extends BaseEntity {

    /** 都道府県ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regions_id")
    Long regionsId;

    /** 都道府県名 */
    @Column(name = "regions_name")
    String regionsName;

    /** 都道府県コード */
    @Column(name = "target_value")
    String targetValue;

    /** リンククリック課金 */
    @Column(name = "link_click_price")
    Integer linkClickPrice;

    /** フォロー課金 */
    @Column(name = "follower_price")
    Integer followerPrice;

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
     * Returns the regionsId.
     * 
     * @return the regionsId
     */
    public Long getRegionsId() {
        return regionsId;
    }

    /** 
     * Sets the regionsId.
     * 
     * @param regionsId the regionsId
     */
    public void setRegionsId(Long regionsId) {
        this.regionsId = regionsId;
    }

    /** 
     * Returns the regionsName.
     * 
     * @return the regionsName
     */
    public String getRegionsName() {
        return regionsName;
    }

    /** 
     * Sets the regionsName.
     * 
     * @param regionsName the regionsName
     */
    public void setRegionsName(String regionsName) {
        this.regionsName = regionsName;
    }

    /** 
     * Returns the targetValue.
     * 
     * @return the targetValue
     */
    public String getTargetValue() {
        return targetValue;
    }

    /** 
     * Sets the targetValue.
     * 
     * @param targetValue the targetValue
     */
    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    /** 
     * Returns the linkClickPrice.
     * 
     * @return the linkClickPrice
     */
    public Integer getLinkClickPrice() {
        return linkClickPrice;
    }

    /** 
     * Sets the linkClickPrice.
     * 
     * @param linkClickPrice the linkClickPrice
     */
    public void setLinkClickPrice(Integer linkClickPrice) {
        this.linkClickPrice = linkClickPrice;
    }

    /** 
     * Returns the followerPrice.
     * 
     * @return the followerPrice
     */
    public Integer getFollowerPrice() {
        return followerPrice;
    }

    /** 
     * Sets the followerPrice.
     * 
     * @param followerPrice the followerPrice
     */
    public void setFollowerPrice(Integer followerPrice) {
        this.followerPrice = followerPrice;
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