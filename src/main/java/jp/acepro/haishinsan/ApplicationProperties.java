package jp.acepro.haishinsan;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.ToString;

@Component
@Data
@ToString
public class ApplicationProperties {

	// DSPサーバ接続先
	@Value("${dsp.scheme}")
	private String dspScheme;
	// APIコモンネーム
	@Value("${dsp.host}")
	private String dspHost;

	// DSPAPI アクセストークン取得
	@Value("${dsp.api.gettoken}")
	private String dspApiGettoken;

	// DSPAPI アクセストークン更新
	@Value("${dsp.api.refreshtoken}")
	private String dspApiRefreshtoken;

	// DSPAPI クリエティブ新規作成 (Banner)
	@Value("${dsp.api.createCreative}")
	private String createCreative;

	// DSPAPI クリエティブリスト取得
	@Value("${dsp.api.creativeList}")
	private String creativeList;

	// DSPAPI セグメント作成
	@Value("${dsp.api.createSegment}")
	private String createSegment;

	// DSPAPI セグメントリスト取得
	@Value("${dsp.api.segmentList}")
	private String segmentList;

	// DSPAPI セグメント更新
	@Value("${dsp.api.segmentUpdate}")
	private String segmentUpdate;

	// DSPAPI Dspキャンペーン作成
	@Value("${dsp.api.createDspCampaign}")
	private String createDspCampaign;

	// DSPAPI Dsp広告グループ作成
	@Value("${dsp.api.createDspGroup}")
	private String createDspGroup;

	// DSPAPI Dsp広告作成
	@Value("${dsp.api.createDspAd}")
	private String createDspAd;

	// DSPAPI Dsp広告グループ入札作成
	@Value("${dsp.api.createDspBit}")
	private String createDspBit;

	// DSPAPI Dspセグメントレポーティング取得
	@Value("${dsp.api.segmentReporting}")
	private String segmentReporting;

	// DSPAPI Dspキャンペーン一覧取得
	@Value("${dsp.api.campaignList}")
	private String campaignList;

	// DSPAPI Dsp広告グループ一覧取得
	@Value("${dsp.api.adGroupList}")
	private String adGroupList;

	// DSPAPI Dsp広告レポーティング取得
	@Value("${dsp.api.adReport}")
	private String adReport;

	// DSPAPI DspのfrequencyCap
	@Value("${dsp.value.frequencyCap}")
	private Integer frequencyCap;

	// DSPAPI Dspの目標タイプ
	@Value("${dsp.value.targetType}")
	private String targetType;

	// DSPAPI Dspの目標値
	@Value("${dsp.value.targetValue}")
	private Integer targetValue;

	// DSP アカウントID
	@Value("${dsp.dspAccountId}")
	private String dspAccountId;

	// DSP アカウントパスワード
	@Value("${dsp.dspPassword}")
	private String dspPassword;

	// FacebookアカウントID
	@Value("${facebook.accountId}")
	private String facebookAccountId;

	// Facebookトークン
	@Value("${facebook.accessToken}")
	private String facebookAccessToken;

	// Facebookアプリシークレット
	@Value("${facebook.appSecret}")
	private String facebookAppSecret;

	// Facebookページトークン
	@Value("${facebook.pageToken}")
	private String pageToken;

	// dspのサポートする画像のタイプ
	@Value("${image.dsp.contentType}")
	private List<String> dspContentTypes;

	// dspのpcがサポートする画像のサイズ
	@Value("${image.dsp.maxSize}")
	private Long dspImageMaxSize;

	// dspがサポートする画像のDimension
	@Value("${image.dsp.dimensions}")
	private List<String> dspDimenstions;

	// dspのgifの毎秒フレーム数
	@Value("${image.dsp.gif.frames}")
	private int dspGifFrames;

	// dspのgifの長さ
	@Value("${image.dsp.gif.playTime}")
	private int dspgifPlayTime;

	// google広告がサポートする画像のサイズ
	@Value("${image.google.maxSize}")
	private Long googleImageMaxSize;

	// googleレスポンシブ広告がサポートする画像のタイプ
	@Value("${image.google.responsive.contentType}")
	private List<String> googleResponsiveContentTypes;

	// googleレスポンシブ広告がサポートする画像のDimension
	@Value("${image.google.responsive.dimensions}")
	private List<String> googleResponsiveDimenstions;

	// googleイメージ広告がサポートする画像のタイプ
	@Value("${image.google.image.contentType}")
	private List<String> googleImageContentTypes;

	// googleイメージ広告がサポートする画像のDimension
	@Value("${image.google.image.dimensions}")
	private List<String> googleImageDimenstions;

	// googleイメージ広告のgifの毎秒フレーム数
	@Value("${image.google.image.gif.frames}")
	private int googleImageGifFrames;

	// googleイメージ広告のgifの長さ
	@Value("${image.google.image.gif.playTime}")
	private int googleImageGifPlayTime;

	// yahooインフィード広告がサポートする画像のDimension
	@Value("${image.yahoo.infeed.dimensions}")
	private List<String> yahooInfeedDimenstions;

	// yahooターゲティング広告がサポートする画像のDimension
	@Value("${image.yahoo.targeting.dimensions}")
	private List<String> yahooTargetingDimenstions;

	// TwitterHost：TwitterHost
	@Value("${twitter.host}")
	private String Twitterhost;

	// TwitterPreviewHost：PreviewHost
	@Value("${twitter.previewHost}")
	private String TwitterPreviewHost;

	// TwitterPreviewPath：PreviewPath
	@Value("${twitter.previewPath}")
	private String TwitterPreviewPath;

	// TwitterApi：FundingInstrumentIdを取得
	@Value("${twitter.api.getFundingInstrumentId}")
	private String TwitterGetFundingInstrumentId;

	// TwitterApi：キャンペーンステータス変更
	@Value("${twitter.api.changeCampaignStatus}")
	private String TwitterChangeCampaignStatus;

	// TwitterApi：キャンペーンステータス作成
	@Value("${twitter.api.creatCampaign}")
	private String TwitterCreatCampaign;

	// TwitterApi：グループ作成
	@Value("${twitter.api.creatgroup}")
	private String TwitterCreatgroup;

	// TwitterApi：ツイートを設定
	@Value("${twitter.api.promoteTweets}")
	private String TwitterPromoteTweets;

	// TwitterApi：ターゲットを設定
	@Value("${twitter.api.setTarget}")
	private String TwitterSetTarget;

	// TwitterApi：タームラインの検索
	@Value("${twitter.api.searchTweetsTimline}")
	private String TwitterSearchTimeline;

	// TwitterApi：レポート
	@Value("${twitter.api.reporting}")
	private String TwitterReporting;

	// Twitter Api Key
	@Value("${twitter.twitterApiKey}")
	private String twitterApiKey;

	// Twitter Api Secret Key
	@Value("${twitter.twitterApiSecretKey}")
	private String twitterApiSecretKey;

	// Twitter Access Token
	@Value("${twitter.twitterAccessToken}")
	private String twitterAccessToken;

	// Twitter Access Token Secret
	@Value("${twitter.twitterAccessTokenSecret}")
	private String twitterAccessTokenSecret;

	// メールアドレスFrom
	@Value("${email.sendFrom}")
	private String emailSendFrom;

	// メールアドレスAdmin
	@Value("${email.admin}")
	private String emailAdmin;

	// emailEncoding
	@Value("${email.encoding}")
	private String emailEncoding;

	// emailPrefix
	@Value("${email.prefix}")
	private String emailPrefix;

	// emailSuffix
	@Value("${email.suffix}")
	private String emailSuffix;

	// emailPattern
	@Value("${email.pattern}")
	private String emailPattern;

	// メールテンプレート-campaign
	@Value("${email.templateName.campaign}")
	private String emailTemplateNameCampaign;

	// メールテンプレート-issue request
	@Value("${email.templateName.issueRequest}")
	private String emailTemplateNameIssueRequest;

	// メールテンプレート貼り付け用URL
	@Value("${email.url.prefix}")
	private String urlPrefix;

	@Value("${csv-download.contentType}")
	private String contentTypeCsvDownload;

	@Value("${csv-download.bytesCode}")
	private String bytesCodeCsvDownload;

	@Value("${csv-upload.charset}")
	private List<String> csvUploadCharsets;

	@Value("${email.senderName}")
	private String senderName;

	@Value("${spring.profiles.active}")
	private String active;

	@Value("${inputLength.title}")
	private int titleLength;

	@Value("${inputLength.description}")
	private int descriptionLength;
	
	@Value("${image.facebook.dimensions}")
	private List<String> facebookDimensions;
	
	@Value("${awsConfig.region}")
	private String region;
	
	@Value("${awsConfig.bucket}")
	private String bucket;
}
