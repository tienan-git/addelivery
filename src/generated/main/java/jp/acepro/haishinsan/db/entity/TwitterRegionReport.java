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
@Table(name = "twitter_region_report")
public class TwitterRegionReport extends BaseEntity {

    /** Twitter地域別レポートID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "twitter_region_report")
    Long twitterRegionReport;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    String campaignId;

    /** 地域 */
    @Column(name = "region")
    String region;

    /** 日付 */
    @Column(name = "day")
    LocalDate day;

    /** 表示回数 */
    @Column(name = "impressions")
    String impressions;

    /** フォロワー */
    @Column(name = "follows")
    String follows;

    /** URLクリック */
    @Column(name = "url_clicks")
    String urlClicks;

    /** 費用 */
    @Column(name = "billed_charge_loacl_micro")
    String billedChargeLoaclMicro;

    /** エンゲージメント費用 */
    @Column(name = "billed_engagements")
    String billedEngagements;

    /** エンゲージメント */
    @Column(name = "engagements")
    String engagements;

    /** リトウット */
    @Column(name = "retweets")
    String retweets;

    /** いいね */
    @Column(name = "likes")
    String likes;

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
     * Returns the twitterRegionReport.
     * 
     * @return the twitterRegionReport
     */
    public Long getTwitterRegionReport() {
        return twitterRegionReport;
    }

    /** 
     * Sets the twitterRegionReport.
     * 
     * @param twitterRegionReport the twitterRegionReport
     */
    public void setTwitterRegionReport(Long twitterRegionReport) {
        this.twitterRegionReport = twitterRegionReport;
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
     * Returns the region.
     * 
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /** 
     * Sets the region.
     * 
     * @param region the region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /** 
     * Returns the day.
     * 
     * @return the day
     */
    public LocalDate getDay() {
        return day;
    }

    /** 
     * Sets the day.
     * 
     * @param day the day
     */
    public void setDay(LocalDate day) {
        this.day = day;
    }

    /** 
     * Returns the impressions.
     * 
     * @return the impressions
     */
    public String getImpressions() {
        return impressions;
    }

    /** 
     * Sets the impressions.
     * 
     * @param impressions the impressions
     */
    public void setImpressions(String impressions) {
        this.impressions = impressions;
    }

    /** 
     * Returns the follows.
     * 
     * @return the follows
     */
    public String getFollows() {
        return follows;
    }

    /** 
     * Sets the follows.
     * 
     * @param follows the follows
     */
    public void setFollows(String follows) {
        this.follows = follows;
    }

    /** 
     * Returns the urlClicks.
     * 
     * @return the urlClicks
     */
    public String getUrlClicks() {
        return urlClicks;
    }

    /** 
     * Sets the urlClicks.
     * 
     * @param urlClicks the urlClicks
     */
    public void setUrlClicks(String urlClicks) {
        this.urlClicks = urlClicks;
    }

    /** 
     * Returns the billedChargeLoaclMicro.
     * 
     * @return the billedChargeLoaclMicro
     */
    public String getBilledChargeLoaclMicro() {
        return billedChargeLoaclMicro;
    }

    /** 
     * Sets the billedChargeLoaclMicro.
     * 
     * @param billedChargeLoaclMicro the billedChargeLoaclMicro
     */
    public void setBilledChargeLoaclMicro(String billedChargeLoaclMicro) {
        this.billedChargeLoaclMicro = billedChargeLoaclMicro;
    }

    /** 
     * Returns the billedEngagements.
     * 
     * @return the billedEngagements
     */
    public String getBilledEngagements() {
        return billedEngagements;
    }

    /** 
     * Sets the billedEngagements.
     * 
     * @param billedEngagements the billedEngagements
     */
    public void setBilledEngagements(String billedEngagements) {
        this.billedEngagements = billedEngagements;
    }

    /** 
     * Returns the engagements.
     * 
     * @return the engagements
     */
    public String getEngagements() {
        return engagements;
    }

    /** 
     * Sets the engagements.
     * 
     * @param engagements the engagements
     */
    public void setEngagements(String engagements) {
        this.engagements = engagements;
    }

    /** 
     * Returns the retweets.
     * 
     * @return the retweets
     */
    public String getRetweets() {
        return retweets;
    }

    /** 
     * Sets the retweets.
     * 
     * @param retweets the retweets
     */
    public void setRetweets(String retweets) {
        this.retweets = retweets;
    }

    /** 
     * Returns the likes.
     * 
     * @return the likes
     */
    public String getLikes() {
        return likes;
    }

    /** 
     * Sets the likes.
     * 
     * @param likes the likes
     */
    public void setLikes(String likes) {
        this.likes = likes;
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