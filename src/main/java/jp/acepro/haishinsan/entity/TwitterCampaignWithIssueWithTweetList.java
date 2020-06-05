/*
 * Copyright 2018 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;

import jp.acepro.haishinsan.db.entity.BaseEntity;
import lombok.Data;

/**
 * 
 */
@Entity
@Data
public class TwitterCampaignWithIssueWithTweetList extends BaseEntity {

    /** キャンペーンID */
    @Column(name = "twitter_campaign_id")
    String campaignId;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 日別予算 */
    @Column(name = "daily_budget")
    Long dailyBudget;

    /** 総予算 */
    @Column(name = "total_budget")
    Long totalBudget;

    /** 配信開始日 */
    @Column(name = "start_date")
    String startDate;

    /** 配信終了日 */
    @Column(name = "end_date")
    String endDate;

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

}