package jp.acepro.haishinsan.constant;

/**
 * 権限定数を定義する。
 *
 *
 */
public class AuthConstant {

	/*---------------------
	 * アカウント体制管理
	 ---------------------*/
	/** 法人 法人管理 */
	public static final String CORPORATION_MANAGE = "CORPORATION_MANAGE";
	/** 法人 法人照会 */
	public static final String CORPORATION_VIEW = "CORPORATION_VIEW";
	/** 店舗 店舗管理 */
	public static final String SHOP_MANAGE = "SHOP_MANAGE";
	/** 店舗 店舗照会 */
	public static final String SHOP_VIEW = "SHOP_VIEW";
	/** ユーザー ユーザー管理 */
	public static final String USER_MANAGE = "USER_MANAGE";
	/** ユーザー ユーザー照会 */
	public static final String USER_VIEW = "USER_VIEW";
	/** 代理店 代理店管理 */
	public static final String AGENCY_MANAGE = "AGENCY_MANAGE";
	/** 代理店 代理店照会 */
	public static final String AGENCY_VIEW = "AGENCY_VIEW";
	/** キャンペーン承認 キャンペーン承認 */
	public static final String CAMPAIGN_APPROVAL = "CAMPAIGN_APPROVAL";
	/** 店舗管理範囲 ショップリスト_管理 */
	public static final String SHOPLIST_MANAGE = "SHOPLIST_MANAGE";
	/** 店舗管理範囲 ショップリスト_代理店 */
	public static final String SHOPLIST_AGENCY = "SHOPLIST_AGENCY";
	/** 店舗管理範囲 ショップリスト_法人 */
	public static final String SHOPLIST_CORPORATION = "SHOPLIST_CORPORATION";
	/** 店舗管理範囲 ショップリスト_店舗 */
	public static final String SHOPLIST_SHOP = "SHOPLIST_SHOP";

	/*---------------------
	 * 広告作成
	 ---------------------*/
	/** 広告作成 新規作成 */
	public static final String CAMPAIGN_MANAGE = "CAMPAIGN_MANAGE";

	/*---------------------
	 * 広告物アップロード
	 ---------------------*/
	/** 広告物アップロード 作成 */
	public static final String CAMPAIGN_UPLOAD = "CAMPAIGN_UPLOAD";

	/*---------------------
	 * 案件一覧
	 ---------------------*/
	/** 案件一覧 登録更新 */
	public static final String ISSUE_MANAGE = "ISSUE_MANAGE";
	/** 案件一覧 閲覧 */
	public static final String ISSUE_VIEW = "ISSUE_VIEW";
	/** レポート 分析 */
	public static final String CAMPAIGN_REPORT_VIEW = "CAMPAIGN_REPORT_VIEW";

	/*---------------------
	 * 分析レポート
	 ---------------------*/
	/** 分析レポート 分析 */
	public static final String SEGMENT_REPORT_VIEW = "SEGMENT_REPORT_VIEW";

	/*---------------------
	 * 設定
	 ---------------------*/
	/** マニュアル 閲覧 */
	public static final String SETTING_MANUAL = "SETTING_MANUAL";
	/** 入稿規定 閲覧 */
	public static final String SETTING_PROVISION = "SETTING_PROVISION";
	/** リンク先管理 管理 */
	public static final String SEGMENT_MANAGE = "SEGMENT_MANAGE";
	/** リンク先管理 閲覧 */
	public static final String SEGMENT_VIEW = "SEGMENT_VIEW";

	/*---------------------
	 * テンプレート設定
	 ---------------------*/
	/** テンプレート管理 一覧 */
	public static final String TEMPLATE_VIEW = "TEMPLATE_VIEW";
	/** テンプレート管理 新規作成 */
	public static final String TEMPLATE_MANAGE = "TEMPLATE_MANAGE";

	/*---------------------
	 * 以下は不要になる予定
	 ---------------------*/
	public static final String SIMPLE_CAMPAIGN_MANAGE = "SIMPLE_CAMPAIGN_MANAGE";
	public static final String SIMPLE_CAMPAIGN_VIEW = "SIMPLE_CAMPAIGN_VIEW";

	public static final String DSP_SEGMENT_MANAGE = "DSP_SEGMENT_MANAGE";
	public static final String DSP_SEGMENT_VIEW = "DSP_SEGMENT_VIEW";
	public static final String DSP_CREATIVE_MANAGE = "DSP_CREATIVE_MANAGE";
	public static final String DSP_CREATIVE_VIEW = "DSP_CREATIVE_VIEW";
	public static final String DSP_CAMPAIGN_MANAGE = "DSP_CAMPAIGN_MANAGE";
	public static final String DSP_CAMPAIGN_VIEW = "DSP_CAMPAIGN_VIEW";
	public static final String DSP_REPORT_VIEW = "DSP_REPORT_VIEW";
	public static final String DSP_TEMPLATE_VIEW = "DSP_TEMPLATE_VIEW";
	public static final String DSP_TEMPLATE_MANAGE = "DSP_TEMPLATE_MANAGE";

	public static final String GOOGLE_CAMPAIGN_MANAGE = "GOOGLE_CAMPAIGN_MANAGE";
	public static final String GOOGLE_CAMPAIGN_VIEW = "GOOGLE_CAMPAIGN_VIEW";
	public static final String GOOGLE_REPORT_VIEW = "GOOGLE_REPORT_VIEW";
	public static final String GOOGLE_TEMPLATE_VIEW = "GOOGLE_TEMPLATE_VIEW";
	public static final String GOOGLE_TEMPLATE_MANAGE = "GOOGLE_TEMPLATE_MANAGE";

	public static final String FACEBOOK_CAMPAIGN_MANAGE = "FACEBOOK_CAMPAIGN_MANAGE";
	public static final String FACEBOOK_CAMPAIGN_VIEW = "FACEBOOK_CAMPAIGN_VIEW";
	public static final String FACEBOOK_REPORT_VIEW = "FACEBOOK_REPORT_VIEW";
	public static final String FACEBOOK_TEMPLATE_VIEW = "FACEBOOK_TEMPLATE_VIEW";
	public static final String FACEBOOK_TEMPLATE_MANAGE = "FACEBOOK_TEMPLATE_MANAGE";

	public static final String TWITTER_CAMPAIGN_MANAGE = "TWITTER_CAMPAIGN_MANAGE";
	public static final String TWITTER_CAMPAIGN_VIEW = "TWITTER_CAMPAIGN_VIEW";
	public static final String TWITTER_REPORT_VIEW = "TWITTER_REPORT_VIEW";
	public static final String TWITTER_TEMPLATE_VIEW = "TWITTER_TEMPLATE_VIEW";
	public static final String TWITTER_TEMPLATE_MANAGE = "TWITTER_TEMPLATE_MANAGE";

	public static final String YOUTUBE_CAMPAIGN_REQUEST = "YOUTUBE_CAMPAIGN_REQUEST";
	public static final String YOUTUBE_CAMPAIGN_VIEW = "YOUTUBE_CAMPAIGN_VIEW";
	public static final String YOUTUBE_CAMPAIGN_MANAGE = "YOUTUBE_CAMPAIGN_MANAGE";
	public static final String YOUTUBE_REPORT_VIEW = "YOUTUBE_REPORT_VIEW";

	public static final String YAHOO_CAMPAIGN_REQUEST = "YAHOO_CAMPAIGN_REQUEST";
	public static final String YAHOO_CAMPAIGN_VIEW = "YAHOO_CAMPAIGN_VIEW";
	public static final String YAHOO_CAMPAIGN_MANAGE = "YAHOO_CAMPAIGN_MANAGE";
	public static final String YAHOO_REPORT_VIEW = "YAHOO_REPORT_VIEW";
	public static final String YAHOO_CSV_UPLOAD = "YAHOO_CSV_UPLOAD";

}
