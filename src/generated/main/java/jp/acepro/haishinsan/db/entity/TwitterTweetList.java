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
@Table(name = "twitter_tweet_list")
public class TwitterTweetList extends BaseEntity {

    /** ツイートリストID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "twitter_tweet_list_id")
    Long twitterTweetListId;

    /** アカウントID */
    @Column(name = "account_id")
    String accountId;

    /** キャンペーンID */
    @Column(name = "campaign_id")
    String campaignId;

    /** ツイートID */
    @Column(name = "tweet_id")
    String tweetId;

    /** ツイートタイトル */
    @Column(name = "tweet_title")
    String tweetTitle;

    /**  */
    @Column(name = "tweet_body")
    String tweetBody;

    /** プレビューURL */
    @Column(name = "preview_url")
    String previewUrl;

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
     * Returns the twitterTweetListId.
     * 
     * @return the twitterTweetListId
     */
    public Long getTwitterTweetListId() {
        return twitterTweetListId;
    }

    /** 
     * Sets the twitterTweetListId.
     * 
     * @param twitterTweetListId the twitterTweetListId
     */
    public void setTwitterTweetListId(Long twitterTweetListId) {
        this.twitterTweetListId = twitterTweetListId;
    }

    /** 
     * Returns the accountId.
     * 
     * @return the accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /** 
     * Sets the accountId.
     * 
     * @param accountId the accountId
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
     * Returns the tweetId.
     * 
     * @return the tweetId
     */
    public String getTweetId() {
        return tweetId;
    }

    /** 
     * Sets the tweetId.
     * 
     * @param tweetId the tweetId
     */
    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    /** 
     * Returns the tweetTitle.
     * 
     * @return the tweetTitle
     */
    public String getTweetTitle() {
        return tweetTitle;
    }

    /** 
     * Sets the tweetTitle.
     * 
     * @param tweetTitle the tweetTitle
     */
    public void setTweetTitle(String tweetTitle) {
        this.tweetTitle = tweetTitle;
    }

    /** 
     * Returns the tweetBody.
     * 
     * @return the tweetBody
     */
    public String getTweetBody() {
        return tweetBody;
    }

    /** 
     * Sets the tweetBody.
     * 
     * @param tweetBody the tweetBody
     */
    public void setTweetBody(String tweetBody) {
        this.tweetBody = tweetBody;
    }

    /** 
     * Returns the previewUrl.
     * 
     * @return the previewUrl
     */
    public String getPreviewUrl() {
        return previewUrl;
    }

    /** 
     * Sets the previewUrl.
     * 
     * @param previewUrl the previewUrl
     */
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
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