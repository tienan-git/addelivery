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
@Table(name = "google_area_price")
public class GoogleAreaPrice extends BaseEntity {

    /** Google地域別単価ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "google_area_price_id")
    Long googleAreaPriceId;

    /** 地域ID */
    @Column(name = "area_id")
    Long areaId;

    /** 地域名 */
    @Column(name = "area_name")
    String areaName;

    /** クリック単価 */
    @Column(name = "unit_price_click")
    Integer unitPriceClick;

    /** 表示単価 */
    @Column(name = "unit_price_display")
    Integer unitPriceDisplay;

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
     * Returns the googleAreaPriceId.
     * 
     * @return the googleAreaPriceId
     */
    public Long getGoogleAreaPriceId() {
        return googleAreaPriceId;
    }

    /** 
     * Sets the googleAreaPriceId.
     * 
     * @param googleAreaPriceId the googleAreaPriceId
     */
    public void setGoogleAreaPriceId(Long googleAreaPriceId) {
        this.googleAreaPriceId = googleAreaPriceId;
    }

    /** 
     * Returns the areaId.
     * 
     * @return the areaId
     */
    public Long getAreaId() {
        return areaId;
    }

    /** 
     * Sets the areaId.
     * 
     * @param areaId the areaId
     */
    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    /** 
     * Returns the areaName.
     * 
     * @return the areaName
     */
    public String getAreaName() {
        return areaName;
    }

    /** 
     * Sets the areaName.
     * 
     * @param areaName the areaName
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    /** 
     * Returns the unitPriceClick.
     * 
     * @return the unitPriceClick
     */
    public Integer getUnitPriceClick() {
        return unitPriceClick;
    }

    /** 
     * Sets the unitPriceClick.
     * 
     * @param unitPriceClick the unitPriceClick
     */
    public void setUnitPriceClick(Integer unitPriceClick) {
        this.unitPriceClick = unitPriceClick;
    }

    /** 
     * Returns the unitPriceDisplay.
     * 
     * @return the unitPriceDisplay
     */
    public Integer getUnitPriceDisplay() {
        return unitPriceDisplay;
    }

    /** 
     * Sets the unitPriceDisplay.
     * 
     * @param unitPriceDisplay the unitPriceDisplay
     */
    public void setUnitPriceDisplay(Integer unitPriceDisplay) {
        this.unitPriceDisplay = unitPriceDisplay;
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