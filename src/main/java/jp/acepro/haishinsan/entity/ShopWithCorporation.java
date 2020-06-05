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
public class ShopWithCorporation extends BaseEntity {

	/** 代理店ID */
	@Column(name = "agency_id")
	Long agencyId;

	/** 代理店名 */
	@Column(name = "agency_name")
	String agencyName;

	/** 法人ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "corporation_id")
	Long corporationId;

	/** 法人名 */
	@Column(name = "corporation_name")
	String corporationName;

	/** 店舗ID */
	@Column(name = "shop_id")
	Long shopId;

	/** 店舗名 */
	@Column(name = "shop_name")
	String shopName;

	/** DSPアカウントID */
	@Column(name = "dsp_account_id")
	String dspAccountId;

	/** DSPパスワード */
	@Column(name = "dsp_password")
	String dspPassword;

	/** DSPユーザーID */
	@Column(name = "dsp_user_id")
	Integer dspUserId;

	/** GoogleアカウントID */
	@Column(name = "google_account_id")
	String googleAccountId;

	/** FacebookページID */
	@Column(name = "facebook_page_id")
	String facebookPageId;

	/** TwitterアカウントID */
	@Column(name = "twitter_account_id")
	String twitterAccountId;

	/** TwitterAPIキー */
	@Column(name = "twitter_api_key")
	String twitterApiKey;

	/** TwitterAPIシークレットキー */
	@Column(name = "twitter_api_secret_key")
	String twitterApiSecretKey;

	/** Twitterアクセストークン */
	@Column(name = "twitter_access_token")
	String twitterAccessToken;

	/** Twitterアクセストークンシークレット */
	@Column(name = "twitter_access_token_secret")
	String twitterAccessTokenSecret;

	/** DSP分配率 */
	@Column(name = "dsp_distribution_ratio")
	Integer dspDistributionRatio;

	/** Google分配率 */
	@Column(name = "google_distribution_ratio")
	Integer googleDistributionRatio;

	/** Facebook分配率 */
	@Column(name = "facebook_distribution_ratio")
	Integer facebookDistributionRatio;

	/** Twitter分配率 */
	@Column(name = "twitter_distribution_ratio")
	Integer twitterDistributionRatio;

	/** 営業審査フラグ */
	@Column(name = "sales_check_flag")
	String salesCheckFlag;

	/** マージン率 */
	@Column(name = "margin_ratio")
	Integer marginRatio;

	/** 店舗通知メール先 */
	@Column(name = "shop_mail_list")
	String shopMailList;

	/** 営業通知メール先 */
	@Column(name = "sales_mail_list")
	String salesMailList;

}