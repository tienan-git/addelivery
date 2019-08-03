-- Project Name : reporting
-- Date/Time    : 2019/08/03 16:04:21
-- Author       : luoq1
-- RDBMS Type   : MySQL
-- Application  : A5:SQL Mk-2

/*
  BackupToTempTable, RestoreFromTempTable疑似命令が付加されています。
  これにより、drop table, create table 後もデータが残ります。
  この機能は一時的に $$TableName のような一時テーブルを作成します。
*/

-- キャンペーンキーワード
--* RestoreFromTempTable
create table campaign_keyword (
  keyword_id bigint auto_increment not null comment 'キーワードID'
  , keyword_name VARCHAR(120) not null comment 'キーワード名'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint campaign_keyword_PKC primary key (keyword_id)
) comment 'キャンペーンキーワード' ;

-- Yahoo地域
--* RestoreFromTempTable
create table yahoo_area (
  yahoo_area_id bigint auto_increment not null comment 'Yahoo地域ID'
  , area_id bigint not null comment '地域ID'
  , area_name VARCHAR(120) not null comment '地域名'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint yahoo_area_PKC primary key (yahoo_area_id)
) comment 'Yahoo地域' ;

-- セグメントレポート管理
--* RestoreFromTempTable
create table segment_report_manage (
  segment_report_manage_id bigint auto_increment not null comment 'セグメントレポート管理ID'
  , segment_id INT not null comment 'セグメントID'
  , segment_name VARCHAR(120) comment 'セグメント名'
  , date DATE not null comment '対象日'
  , uunum INT not null comment 'ユニークユーザ数'
  , uunum_pc INT not null comment 'ユニークユーザ数PC'
  , uunum_sp INT not null comment 'ユニークユーザ数SP'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint segment_report_manage_PKC primary key (segment_report_manage_id)
) comment 'セグメントレポート管理' ;

-- Youtubeキャンペーン管理
--* RestoreFromTempTable
create table youtube_campaign_manage (
  youtube_campaign_manage_id bigint auto_increment not null comment 'Youtubeキャンペーン管理ID'
  , campaign_id bigint comment 'キャンペーンID'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , ad_type CHAR(2) comment '広告タイプ:04:インストリーム広告 05:バンパー広告'
  , budget bigint comment '予算'
  , start_date VARCHAR(10) comment '配信開始日'
  , end_date VARCHAR(10) comment '配信終了日'
  , area text comment '配信地域'
  , lp VARCHAR(120) comment 'LP'
  , video_url VARCHAR(2083) comment '動画URL'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint youtube_campaign_manage_PKC primary key (youtube_campaign_manage_id)
) comment 'Youtubeキャンペーン管理' ;

-- Youtube地域別レポート
--* RestoreFromTempTable
create table youtube_location_report (
  youtube_region_report_id bigint auto_increment not null comment 'Youtube地域別レポートID'
  , campaign_id bigint not null comment 'キャンペーンID'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , date CHAR(8) comment '日付'
  , location_id bigint comment '地域ID'
  , impressions bigint comment '表示回数'
  , clicks bigint comment 'クリック数'
  , costs DOUBLE comment '費用'
  , video_views bigint comment '視聴回数'
  , video_view_rate DOUBLE comment '視聴率'
  , avg_cpv DOUBLE comment '視聴単価'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint youtube_location_report_PKC primary key (youtube_region_report_id)
) comment 'Youtube地域別レポート' ;

-- Youtubeデバイス別レポート
--* RestoreFromTempTable
create table youtube_device_report (
  Youtube_device_report_id bigint auto_increment not null comment 'Youtubeデバイス別レポートID'
  , campaign_id bigint not null comment 'キャンペーンID'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , date CHAR(8) comment '日付'
  , device_type CHAR(2) comment 'デバイスタイプ'
  , impressions bigint comment '表示回数'
  , clicks bigint comment 'クリック数'
  , costs DOUBLE comment '費用'
  , video_views bigint comment '視聴回数'
  , video_view_rate DOUBLE comment '視聴率'
  , avg_cpv DOUBLE comment '視聴単価'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint youtube_device_report_PKC primary key (Youtube_device_report_id)
) comment 'Youtubeデバイス別レポート' ;

-- Yahooキャンペーン管理
--* RestoreFromTempTable
create table yahoo_campaign_manage (
  yahoo_campaign_manage_id bigint auto_increment not null comment 'Yahooキャンペーン管理ID'
  , campaign_id TEXT comment 'キャンペーンID'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , adv_destination VARCHAR(2) comment '出稿先'
  , device_type CHAR(2) comment 'デバイスタイプ'
  , area text comment '配信地域'
  , url VARCHAR(2083) comment 'リンク先'
  , ad_short_title VARCHAR(25) comment '短い広告見出し'
  , ad_title1 VARCHAR(30) comment '広告見出し１'
  , ad_title2 VARCHAR(30) comment '広告見出し２'
  , ad_description VARCHAR(90) comment '説明文'
  , image_name TEXT comment '画像名'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint yahoo_campaign_manage_PKC primary key (yahoo_campaign_manage_id)
) comment 'Yahooキャンペーン管理' ;

-- Yahooレポート管理
--* RestoreFromTempTable
create table yahoo_report_manage (
  yahoo_report_manage_id bigint auto_increment not null comment 'Yahooレポート管理ID'
  , create_date DATE comment '日'
  , device_name VARCHAR(120) comment 'デバイス'
  , region_name VARCHAR(120) comment '都道府県'
  , city_name VARCHAR(120) comment '市区郡'
  , district_name VARCHAR(120) comment '行政区'
  , campaign_name VARCHAR(120) comment 'キャンペーン名'
  , campaign_type VARCHAR(120) comment 'キャンペーンタイプ'
  , advertising_method VARCHAR(120) comment '広告掲載方式'
  , impression_count bigint comment 'インプレッション数'
  , click_count bigint comment 'クリック数'
  , cost DOUBLE comment 'コスト'
  , ave_publish_rank DOUBLE comment '平均掲載順位'
  , campaign_id bigint comment 'キャンペーンID'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint yahoo_report_manage_PKC primary key (yahoo_report_manage_id)
) comment 'Yahooレポート管理' ;

-- Google地域別レポート
--* RestoreFromTempTable
create table google_location_report (
  google_region_report_id bigint auto_increment not null comment 'Google地域別レポートID'
  , campaign_id bigint not null comment 'キャンペーンID'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , date CHAR(8) comment '日付'
  , location_id bigint comment '地域ID'
  , impressions bigint comment '表示回数'
  , clicks bigint comment 'クリック数'
  , costs DOUBLE comment '費用'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint google_location_report_PKC primary key (google_region_report_id)
) comment 'Google地域別レポート' ;

-- Googleデバイス別レポート
--* RestoreFromTempTable
create table google_device_report (
  Google_device_report_id bigint auto_increment not null comment 'Googleデバイス別レポートID'
  , campaign_id bigint not null comment 'キャンペーンID'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , date CHAR(8) comment '日付'
  , device_type CHAR(2) comment 'デバイスタイプ'
  , impressions bigint comment '表示回数'
  , clicks bigint comment 'クリック数'
  , costs DOUBLE comment '費用'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint google_device_report_PKC primary key (Google_device_report_id)
) comment 'Googleデバイス別レポート' ;

-- Twitterデバイス別レポート
--* RestoreFromTempTable
create table twitter_device_report (
  twitter_device_report bigint auto_increment not null comment 'Twitterデバイス別レポートID'
  , campaign_id VARCHAR(80) not null comment 'キャンペーンID'
  , device VARCHAR(30) comment 'デバイス'
  , day DATE comment '日付'
  , impressions VARCHAR(30) comment '表示回数'
  , follows VARCHAR(30) comment 'フォロワー'
  , url_clicks VARCHAR(2083) comment 'URLクリック'
  , billed_charge_loacl_micro VARCHAR(30) comment '費用'
  , billed_engagements VARCHAR(30) comment 'エンゲージメント費用'
  , engagements VARCHAR(30) comment 'エンゲージメント'
  , retweets VARCHAR(30) comment 'リトウット'
  , likes VARCHAR(30) comment 'いいね'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint twitter_device_report_PKC primary key (twitter_device_report)
) comment 'Twitterデバイス別レポート' ;

-- Twitter地域別レポート
--* RestoreFromTempTable
create table twitter_region_report (
  twitter_region_report bigint auto_increment not null comment 'Twitter地域別レポートID'
  , campaign_id VARCHAR(80) not null comment 'キャンペーンID'
  , region VARCHAR(30) comment '地域'
  , day DATE comment '日付'
  , impressions VARCHAR(30) comment '表示回数'
  , follows VARCHAR(30) comment 'フォロワー'
  , url_clicks VARCHAR(2083) comment 'URLクリック'
  , billed_charge_loacl_micro VARCHAR(30) comment '費用'
  , billed_engagements VARCHAR(30) comment 'エンゲージメント費用'
  , engagements VARCHAR(30) comment 'エンゲージメント'
  , retweets VARCHAR(30) comment 'リトウット'
  , likes VARCHAR(30) comment 'いいね'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint twitter_region_report_PKC primary key (twitter_region_report)
) comment 'Twitter地域別レポート' ;

-- DSP広告管理
--* RestoreFromTempTable
create table dsp_ad_manage (
  dsp_ad_manage_id bigint auto_increment not null comment 'DSP広告管理ID'
  , campaign_id INT not null comment 'キャンペーンID'
  , ad_group_id INT not null comment '広告グループID'
  , ad_id INT not null comment '広告ID'
  , creative_id INT not null comment 'クリエイティブID'
  , device_type INT not null comment 'デバイスタイプ'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint dsp_ad_manage_PKC primary key (dsp_ad_manage_id)
) comment 'DSP広告管理' ;

-- DSPレポート管理
--* RestoreFromTempTable
create table dsp_report_manage (
  dsp_report_manage_id bigint auto_increment not null comment 'DSPレポート管理ID'
  , campaign_id INT not null comment 'キャンペーンID'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , ad_group_id INT not null comment '広告グループID'
  , ad_id INT not null comment '広告ID'
  , creative_id INT not null comment 'クリエイティブID'
  , creative_name VARCHAR(120) comment 'クリエイティブ名'
  , device_type INT not null comment 'デバイスタイプ'
  , date DATE not null comment '対象日'
  , impression_count INT comment '表示回数'
  , click_count INT comment 'クリック回数'
  , price DOUBLE comment '広告費用'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint dsp_report_manage_PKC primary key (dsp_report_manage_id)
) comment 'DSPレポート管理' ;

-- DSP広告グループ管理
--* RestoreFromTempTable
create table dsp_ad_group_manage (
  dsp_ad_group_manage_id bigint auto_increment not null comment 'DSP広告グループ管理ID'
  , campaign_id INT not null comment 'キャンペーンID'
  , ad_group_id INT not null comment '広告グループID'
  , device_type INT not null comment 'デバイスタイプ'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint dsp_ad_group_manage_PKC primary key (dsp_ad_group_manage_id)
) comment 'DSP広告グループ管理' ;

-- Facebookデバイス別レポート
--* RestoreFromTempTable
create table facebook_device_report (
  facebook_device_report_id bigint auto_increment not null comment 'Facebookデバイス別レポートID'
  , campaign_id VARCHAR(80) comment 'キャンペーンID'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , date DATE comment '日付'
  , device VARCHAR(120) comment 'デバイス名'
  , impressions bigint comment '表示回数'
  , clicks bigint comment 'クリック数'
  , spend bigint comment '利用金額'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint facebook_device_report_PKC primary key (facebook_device_report_id)
) comment 'Facebookデバイス別レポート' ;

-- Facebook地域別レポート
--* RestoreFromTempTable
create table facebook_region_report (
  facebook_region_report_id bigint auto_increment not null comment 'Facebook地域別レポートID'
  , campaign_id VARCHAR(80) comment 'キャンペーンID'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , date DATE comment '日付'
  , region VARCHAR(120) comment '地域'
  , impressions bigint comment '表示回数'
  , clicks bigint comment 'クリック数'
  , spend bigint comment '利用金額'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint facebook_region_report_PKC primary key (facebook_region_report_id)
) comment 'Facebook地域別レポート' ;

-- Twitterツイートリスト
--* RestoreFromTempTable
create table twitter_tweet_list (
  twitter_tweet_list_id bigint auto_increment not null comment 'ツイートリストID'
  , account_id VARCHAR(80) not null comment 'アカウントID'
  , tweet_id VARCHAR(80) not null comment 'ツイートID'
  , tweet_title VARCHAR(100) not null comment 'ツイートタイトル'
  , tweet_body VARCHAR(1024) not null comment 'ツイートボディー'
  , preview_url VARCHAR(1024) not null comment 'プレビューURL'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint twitter_tweet_list_PKC primary key (twitter_tweet_list_id)
) comment 'Twitterツイートリスト' ;

-- Facebook地域別単価
--* RestoreFromTempTable
create table facebook_area_price (
  facebook_area_price_id bigint auto_increment not null comment 'Facebook地域別単価ID'
  , area_id bigint not null comment '地域ID'
  , area_name VARCHAR(120) not null comment '地域名'
  , english_name VARCHAR(120) comment '英語名'
  , unit_price_click INT not null comment 'クリック単価'
  , unit_price_display INT not null comment '表示単価'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint facebook_area_price_PKC primary key (facebook_area_price_id)
) comment 'Facebook地域別単価' ;

-- 都道府県
--* RestoreFromTempTable
create table regions (
  regions_id bigint auto_increment not null comment '都道府県ID'
  , regions_name VARCHAR(120) comment '都道府県名'
  , target_value VARCHAR(120) comment '都道府県コード'
  , link_click_price INT comment 'リンククリック課金'
  , follower_price INT comment 'フォロー課金'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint regions_PKC primary key (regions_id)
) comment '都道府県' ;

-- DSPトークン
--* RestoreFromTempTable
create table dsp_token (
  dsp_token_id bigint auto_increment not null comment 'DSPトークンID'
  , token VARCHAR(50) not null comment 'トークン'
  , refresh_token VARCHAR(50) not null comment 'リフレッシュトークン'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint dsp_token_PKC primary key (dsp_token_id)
) comment 'DSPトークン' ;

-- 案件
--* RestoreFromTempTable
create table issue (
  issue_id bigint auto_increment not null comment '案件ID'
  , shop_id bigint not null comment '店舗ID'
  , dsp_campaign_manage_id bigint comment 'DSPキャンペーン管理ID'
  , google_campaign_manage_id bigint comment 'Googleキャンペーン管理ID'
  , facebook_campaign_manage_id bigint comment 'Facebookキャンペーン管理ID'
  , twitter_campaign_manage_id bigint comment 'Twitterキャンペーン管理ID'
  , yahoo_campaign_manage_id bigint comment 'Yahooキャンペーン管理ID'
  , youtube_campaign_manage_id bigint comment 'Youtubeキャンペーン管理ID'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , budget bigint comment '全体予算'
  , start_date VARCHAR(10) comment '配信開始日'
  , end_date VARCHAR(10) comment '配信終了日'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint issue_PKC primary key (issue_id)
) comment '案件' ;

-- 代理店
--* RestoreFromTempTable
create table agency (
  agency_id bigint auto_increment not null comment '代理店ID'
  , agency_name VARCHAR(120) comment '代理店名'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint agency_PKC primary key (agency_id)
) comment '代理店' ;

-- Facebookテンプレート
--* RestoreFromTempTable
create table facebook_template (
  template_id bigint auto_increment not null comment 'テンプレートID'
  , template_name VARCHAR(120) not null comment 'テンプレート名'
  , shop_id bigint not null comment 'テンプレート所有店舗ID'
  , template_priority INT not null comment 'テンプレート優先順'
  , unit_price_type CHAR(2) not null comment '単価タイプ:01:クリック重視 02:表示重視'
  , campaign_name VARCHAR(120) comment 'キャンペーン名'
  , campaign_purpose VARCHAR(2) comment 'キャンペーン目的'
  , daily_budget bigint comment '一日の予算'
  , bid_amount bigint comment '入札金額'
  , geolocation text comment '地域'
  , media VARCHAR(100) comment '配信媒体'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint facebook_template_PKC primary key (template_id)
) comment 'Facebookテンプレート' ;

-- Facebookキャンペーン管理
--* RestoreFromTempTable
create table facebook_campaign_manage (
  Facebook_campaign_manage_id bigint auto_increment not null comment 'Facebookキャンペーン管理ID'
  , campaign_id VARCHAR(80) not null comment 'キャンペーンID'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , budget bigint comment '予算'
  , segment_id INT comment 'セグメントID'
  , regions text comment '地域コード'
  , approval_flag CHAR(1) not null comment '審査フラグ'
  , image_url VARCHAR(2083) comment 'イメージURL'
  , link_url VARCHAR(2083) comment 'リンク先URL'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint facebook_campaign_manage_PKC primary key (Facebook_campaign_manage_id)
) comment 'Facebookキャンペーン管理' ;

-- Twitterテンプレート
--* RestoreFromTempTable
create table twitter_template (
  template_id bigint auto_increment not null comment 'テンプレートID'
  , shop_id bigint not null comment 'テンプレート所有店舗ID'
  , template_name VARCHAR(80) not null comment 'テンプレート名'
  , template_priority INT not null comment 'テンプレート優先順'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , start_time CHAR(8) comment '開始日'
  , end_time CHAR(8) comment '終了日'
  , daily_budget INT comment '日予算'
  , regions VARCHAR(1024) comment '都道府県'
  , location CHAR(1) comment '地域'
  , total_budget INT comment '総予算'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint twitter_template_PKC primary key (template_id)
) comment 'Twitterテンプレート' ;

-- Twitterキャンペーン管理
--* RestoreFromTempTable
create table twitter_campaign_manage (
  twitter_campaign_manage_id bigint auto_increment not null comment 'Twitterキャンペーン管理ID'
  , campaign_id VARCHAR(80) not null comment 'キャンペーンID'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , daily_budget bigint not null comment '日別予算'
  , total_budget bigint comment '総予算'
  , group_id VARCHAR(80) not null comment 'グループID'
  , tweet_ids VARCHAR(500) not null comment 'ツイートIDS'
  , approval_flag CHAR(1) not null comment '審査フラグ'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint twitter_campaign_manage_PKC primary key (twitter_campaign_manage_id)
) comment 'Twitterキャンペーン管理' ;

-- セグメント管理
--* RestoreFromTempTable
create table segment_manage (
  segment_manage_id bigint auto_increment not null comment 'セグメント管理ID'
  , segment_id INT not null comment 'セグメントID'
  , shop_id bigint not null comment '店舗ID'
  , segment_name VARCHAR(120) comment 'セグメント名'
  , url VARCHAR(2083) comment 'リンク先'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint segment_manage_PKC primary key (segment_manage_id)
) comment 'セグメント管理' ;

-- DSPテンプレート
--* RestoreFromTempTable
create table dsp_template (
  template_id bigint auto_increment not null comment 'テンプレートID'
  , shop_id bigint not null comment 'テンプレート所有店舗ID:テンプレート所有店舗ID'
  , template_name VARCHAR(80) not null comment 'テンプレート名'
  , template_priority INT not null comment 'テンプレート優先順:数値が小さいほど、優先順が高い'
  , billing_type INT not null comment '課金方式'
  , bid_cpc_price INT not null comment '入札CPC単価'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint dsp_template_PKC primary key (template_id)
) comment 'DSPテンプレート' ;

-- DSPキャンペーン管理
--* RestoreFromTempTable
create table dsp_campaign_manage (
  dsp_campaign_manage_id bigint auto_increment not null comment 'DSPキャンペーン管理ID'
  , campaign_id INT not null comment 'キャンペーンID'
  , creative_id VARCHAR(200) not null comment 'クリエイティブID'
  , segment_id INT not null comment 'セグメントID'
  , budget INT not null comment '予算'
  , approval_flag CHAR(1) not null comment '審査フラグ'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint dsp_campaign_manage_PKC primary key (dsp_campaign_manage_id)
) comment 'DSPキャンペーン管理' ;

-- クリエイティブ管理
--* RestoreFromTempTable
create table creative_manage (
  creative_manage_id bigint auto_increment not null comment 'クリエイティブ管理ID'
  , creative_id INT not null comment 'クリエイティブID'
  , url VARCHAR(1000) not null comment 'クリエイティブURL'
  , creative_name VARCHAR(120) not null comment 'クリエイティブ名'
  , shop_id bigint not null comment '店舗ID'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint creative_manage_PKC primary key (creative_manage_id)
) comment 'クリエイティブ管理' ;

-- オペレーションログ
--* RestoreFromTempTable
create table operation_log (
  operation_log_id bigint auto_increment not null comment 'オペレーションログID'
  , user_id bigint comment 'ユーザーID'
  , name VARCHAR(80) not null comment 'オペレーション名'
  , detail text comment 'オペレーション詳細'
  , user_agent text comment 'ユーザーエージェント'
  , ip_address VARCHAR(24) comment 'IPアドレス'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint operation_log_PKC primary key (operation_log_id)
) comment 'オペレーションログ' ;

-- Google地域別単価
--* RestoreFromTempTable
create table google_area_price (
  google_area_price_id bigint auto_increment not null comment 'Google地域別単価ID'
  , area_id bigint not null comment '地域ID'
  , area_name VARCHAR(120) not null comment '地域名'
  , unit_price_click INT not null comment 'クリック単価'
  , unit_price_display INT not null comment '表示単価'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint google_area_price_PKC primary key (google_area_price_id)
) comment 'Google地域別単価' ;

-- Googleテンプレート
--* RestoreFromTempTable
create table google_template (
  template_id bigint auto_increment not null comment 'テンプレートID'
  , shop_id bigint not null comment 'テンプレート所有店舗ID:テンプレート所有店舗ID'
  , template_name VARCHAR(80) not null comment 'テンプレート名'
  , template_priority INT not null comment 'テンプレート優先順:数値が小さいほど、優先順が高い'
  , campaign_name VARCHAR(240) comment 'キャンペーン名'
  , budget bigint comment '予算'
  , device_type CHAR(2) comment 'デバイスタイプ:01:パソコン 02:モバイル 03:全て'
  , location_list text comment '地域'
  , unit_price_type CHAR(2) not null comment '単価タイプ:01:クリック重視 02:表示重視'
  , ad_type CHAR(2) comment '広告タイプ:01:レスポンシブ広告 02:イメージ広告 03:拡張テキスト広告'
  , res_ad_short_title VARCHAR(25) comment 'レスポンシブ広告短い見出し'
  , res_ad_description VARCHAR(90) comment 'レスポンシブ広告説明文'
  , res_ad_final_page_url VARCHAR(2083) comment 'レスポンシブ広告最終ページURL'
  , image_ad_final_page_url VARCHAR(2083) comment 'イメージ広告最終ページURL'
  , text_ad_final_page_url VARCHAR(2083) comment 'テキスト広告最終ページURL'
  , text_ad_title_1 VARCHAR(30) comment 'テキスト広告見出し１'
  , text_ad_title_2 VARCHAR(30) comment 'テキスト広告見出し２'
  , text_ad_description VARCHAR(90) comment 'テキスト広告説明文'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint google_template_PKC primary key (template_id)
) comment 'Googleテンプレート' ;

-- Googleキャンペーン管理
--* RestoreFromTempTable
create table google_campaign_manage (
  google_campaign_manage_id bigint auto_increment not null comment 'Googleキャンペーン管理ID'
  , campaign_id bigint not null comment 'キャンペーンID'
  , campaign_name VARCHAR(240) not null comment 'キャンペーン名'
  , approval_flag CHAR(1) not null comment '審査フラグ:0:承認待ち 1:承認済み'
  , budget bigint not null comment '予算'
  , regions text comment '地域コード'
  , ad_type CHAR(2) not null comment '広告タイプ:01:レスポンシブ広告 02:イメージ広告 03:拡張テキスト広告'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint google_campaign_manage_PKC primary key (google_campaign_manage_id)
) comment 'Googleキャンペーン管理' ;

-- 店舗
--* RestoreFromTempTable
create table shop (
  shop_id bigint auto_increment not null comment '店舗ID'
  , shop_name VARCHAR(25) not null comment '店舗名'
  , corporation_id bigint not null comment '法人ID'
  , dsp_user_id INT comment 'DSPユーザーID'
  , google_account_id VARCHAR(80) comment 'GoogleアカウントID'
  , facebook_page_id VARCHAR(80) comment 'FacebookページID'
  , twitter_account_id VARCHAR(80) comment 'TwitterアカウントID'
  , dsp_distribution_ratio INT(3) comment 'DSP分配率'
  , google_distribution_ratio INT(3) comment 'Google分配率'
  , facebook_distribution_ratio INT(3) comment 'Facebook分配率'
  , twitter_distribution_ratio INT(3) comment 'Twitter分配率'
  , sales_check_flag CHAR(1) comment '営業審査フラグ'
  , margin_ratio INT comment 'マージン率'
  , shop_mail_list VARCHAR(320) comment '店舗通知メール先'
  , sales_mail_list VARCHAR(320) comment '営業通知メール先'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint shop_PKC primary key (shop_id)
) comment '店舗' ;

-- 法人
--* RestoreFromTempTable
create table corporation (
  corporation_id bigint auto_increment not null comment '法人ID'
  , corporation_name VARCHAR(120) comment '法人名'
  , agency_id bigint not null comment '代理店ID'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint corporation_PKC primary key (corporation_id)
) comment '法人' ;

-- ユーザー
--* RestoreFromTempTable
create table user (
  user_id bigint auto_increment not null comment 'ユーザーID'
  , user_name VARCHAR(120) not null comment 'ユーザー名'
  , email VARCHAR(320) not null comment 'メールアドレス'
  , password VARCHAR(80) not null comment 'パスワード'
  , old_system_password VARCHAR(80) comment '旧システムパスワード'
  , shop_id bigint not null comment '店舗ID'
  , role_id bigint not null comment 'ロールID'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint user_PKC primary key (user_id)
) comment 'ユーザー' ;

-- 権限
--* RestoreFromTempTable
create table authority (
  authority_id bigint auto_increment not null comment '権限ID'
  , role_id bigint not null comment 'ロールID'
  , authority VARCHAR(120) not null comment '権限'
  , version_no bigint not null comment 'バージョン番号'
  , created_at DATETIME(6) not null comment '登録日時'
  , created_by VARCHAR(15) not null comment '登録者'
  , updated_at DATETIME(6) not null comment '更新日時'
  , updated_by VARCHAR(15) not null comment '更新者'
  , is_actived INT(1) not null comment 'アクティブフラグ:0：無効、1：有効'
  , constraint authority_PKC primary key (authority_id)
) comment '権限' ;
