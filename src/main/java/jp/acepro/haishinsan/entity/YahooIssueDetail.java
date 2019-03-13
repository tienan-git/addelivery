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
public class YahooIssueDetail extends BaseEntity {

    /** 案件ID */
    @Column(name = "issue_id")
    Long issueId;
    
    /** キャンペインID */
    @Column(name = "campaign_id")
    String campaignId;
    
    /** キャンペイン名 */
    @Column(name = "campaign_name")
    String campaignName;
    
    /** 出稿先 */
    @Column(name = "adv_destination")
    String advDestination;
    
    /** デバイスタイプ */
    @Column(name = "device_type")
    String deviceType;
    
    /** url */
    @Column(name = "url")
    String url;
    
    /** 配信地域 */
    @Column(name = "area")
    String area;
    
    /** 予算 */
    @Column(name = "budget")
    Long budget;
    
    /** 配信開始日 */
    @Column(name = "start_date")
    String startDate;

    /** 配信終了日 */
    @Column(name = "end_date")
    String endDate;
    
    /** イメージ名 */
    @Column(name = "image_name")
    String imageName;
    
    /** yahooキャンペインmanage ID */
    @Column(name = "yahoo_campaign_manage_id")
    Long yahooCampaignManageId;
    
    /** 短い広告見出し */
    @Column(name = "ad_short_title")
    String adShortTitle;
    
    /** 広告見出し１ */
    @Column(name = "ad_title1")
    String adTitle1;
    
    /** 広告見出し２ */
    @Column(name = "ad_title2")
    String adTitle2;
    
    /** 説明文 */
    @Column(name = "ad_description")
    String adDescription;
}