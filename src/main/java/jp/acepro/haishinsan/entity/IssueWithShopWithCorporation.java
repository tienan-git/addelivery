/*
 * Copyright 2018 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

import jp.acepro.haishinsan.db.entity.BaseEntity;
import lombok.Data;

/**
 * 
 */
@Entity
@Data
public class IssueWithShopWithCorporation extends BaseEntity {

    /** 店舗ID */
    @Column(name = "shop_id")
    Long shopId;

    /** 店舗名 */
    @Column(name = "shop_name")
    String shopName;

    /** 法人ID */
    @Column(name = "corporation_id")
    Long corporationId;

    /** 法人名 */
    @Column(name = "corporation_name")
    String corporationName;

    /** 案件ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    Long issueId;

    /** DSPキャンペーン管理ID */
    @Column(name = "dsp_campaign_manage_id")
    Long dspCampaignManageId;

    /** Googleキャンペーン管理ID */
    @Column(name = "google_campaign_manage_id")
    Long googleCampaignManageId;

    /** Facebookキャンペーン管理ID */
    @Column(name = "facebook_campaign_manage_id")
    Long facebookCampaignManageId;

    /** Twitterキャンペーン管理ID */
    @Column(name = "twitter_campaign_manage_id")
    Long twitterCampaignManageId;

    /** Yahooキャンペーン管理ID */
    @Column(name = "yahoo_campaign_manage_id")
    Long yahooCampaignManageId;

    /** Youtubeキャンペーン管理ID */
    @Column(name = "youtube_campaign_manage_id")
    Long youtubeCampaignManageId;

    /** キャンペーン名 */
    @Column(name = "campaign_name")
    String campaignName;

    /** 全体予算 */
    @Column(name = "budget")
    Long budget;

    /** 配信開始日 */
    @Column(name = "start_date")
    String startDate;

    /** 配信終了日 */
    @Column(name = "end_date")
    String endDate;

    /** アクティブフラグ:0：無効、1：有効 */
    @Column(name = "is_actived")
    Integer isActived;

}