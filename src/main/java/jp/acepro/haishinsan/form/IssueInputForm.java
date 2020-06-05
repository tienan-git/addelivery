package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import lombok.Data;

@Data
public class IssueInputForm {

	Long issueId;
	String issueName;
	String campaignStatus;
	String checkStatus;
	boolean dspSelected;
	boolean googleSelected;
	boolean facebookSelected;
	boolean twitterSelected;
	String startDate;
	String endDate;
	Long budget;

	String linkUrl;
	String url;

	// -------- Facebook関連 --------
	MultipartFile facebookImage;

	// -------- DSP関連 --------
	List<DspCampaignCreInputForm> dspCampaignCreInputFormList;
	List<Integer> idList;
	Integer segmentId;

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
}
