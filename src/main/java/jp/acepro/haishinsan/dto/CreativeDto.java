package jp.acepro.haishinsan.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.form.DspCampaignCreInputForm;
import lombok.Data;

@Data
public class CreativeDto {

	// MediaIcon
	List<IconDto> iconList;

	Long issueId;
	String campaignName;
	String campaignStatus;
	String checkStatus;
	String creativeType;
	boolean dspSelected = false;
	boolean googleSelected = false;
	boolean facebookSelected = false;
	boolean twitterSelected = false;
	boolean yahooSelected = false;
	boolean youtubeSelected = false;

	Long dspCampaignManageId;
	Long googleCampaignManageId;
	Long facebookCampaignManageId;
	Long twitterCampaignManageId;
	Long yahooCampaignManageId;
	Long youtubeCampaignManageId;

	String dspErrorCode;
	String googleErrorCode;
	String facebookErrorCode;
	String[] facebookParam;
	String twitterErrorCode;
	String twitterParam;

	String startDate;
	String endDate;
	Long budget;

	String url;

	// -------- DSP関連 --------
	List<DspCampaignCreInputForm> dspCampaignCreInputFormList = new ArrayList<DspCampaignCreInputForm>();
	List<Integer> idList = new ArrayList<Integer>();
	Integer segmentId;

	// -------- Facebook関連 --------
	MultipartFile facebookImage;
	// 説明文
	String textFacebookDescription;

	// -------- Google関連 --------
	// 広告タイプ
	String adType;
	// -------- レスポンシブ広告関連 --------
	// 画像ファイル
	List<MultipartFile> resAdImageFileList;
	List<byte[]> resAdImageDateList;

	// 短い広告見出し
	String resAdShortTitle;

	// 説明文
	String resAdDescription;

	// -------- イメージ広告関連 --------
	// 画像ファイル
	List<MultipartFile> imageAdImageFileList;
	List<byte[]> imageAdImageDataList;

	// -------- 拡張テキスト広告関連 --------
	// 広告見出し１
	String textAdTitle1;

	// 広告見出し２
	String textAdTitle2;

	// 説明文
	String textAdDescription;

	// -------- Twitter関連 --------
	List<String> tweetIdList;
	List<TwitterTweet> websiteTweetList;

	public String getMediaString() {
		String mediaString = "";
		if (dspSelected) {
			mediaString = mediaString + "DSP ";
		}
		if (googleSelected) {
			mediaString = mediaString + "Google ";
		}
		if (facebookSelected) {
			mediaString = mediaString + "Facebook ";
		}
		if (twitterSelected) {
			mediaString = mediaString + "Twitter ";
		}
		if (yahooSelected) {
			mediaString = mediaString + "Yahoo ";
		}
		if (youtubeSelected) {
			mediaString = mediaString + "Youtube ";
		}

		return mediaString;
	}
}
