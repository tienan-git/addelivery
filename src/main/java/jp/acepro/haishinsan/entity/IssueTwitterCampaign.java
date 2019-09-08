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
public class IssueTwitterCampaign extends BaseEntity {

    /** TwitterキャンペーンID */
    @Column(name = "twitter_campaign_id")
    String twitterCampaignId;

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

}