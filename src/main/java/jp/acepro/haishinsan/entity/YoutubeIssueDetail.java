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
public class YoutubeIssueDetail extends BaseEntity {

    /** 案件ID */
    @Column(name = "issue_id")
    Long issueId;

    /** 予算 */
    @Column(name = "budget")
    Long budget;

    /** 地域 */
    @Column(name = "area")
    String area;

    /** 配信開始日 */
    @Column(name = "start_date")
    String startDate;

    /** 配信終了日 */
    @Column(name = "end_date")
    String endDate;
    
    
    /** キャンペインID */
    @Column(name = "campaign_id")
    Long campaignId;
    
    /** キャンペイン名 */
    @Column(name = "campaign_name")
    String campaignName;
    
    /** 広告タイプ */
    @Column(name = "ad_type")
    String adType;

    /** LP */
    @Column(name = "lp")
    String lp;
    
    /** 動画URL */
    @Column(name = "video_url")
    String videoUrl;
    
}