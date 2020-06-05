package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class YahooIssueinputForm {

	// 案件ID
	Long issueId;

	// キャンペーン名
	String campaignName;

	// キャンペーンID
	String campaignId;

	// デバイスタイプ
	String deviceType;

	// 予算
	Long budget;

	// 配信開始日
	String startDate;
	
	// 配信開始日(時)
	String startHour;
	
	// 配信開始日(分)
	String startMin;

	// 配信終了日
	String endDate;
	
	// 配信終了日(時)
	String endHour;
	
	// 配信終了日(分)
	String endMin;
	
	// 配信地域IDリスト
	List<Long> locationIdList;

	List<Pair<Integer, String>> segmentList;

	// 出稿先タイプ
	String advDestination;

	// -------- インフィード広告関連 --------
	// 画像ファイル
	List<MultipartFile> infeedAdImageFileList;
	List<byte[]> infeedAdImageDateList;

	// 短い広告見出し
	String infeedAdShortTitle;

	// 説明文
	String infeedAdDescription;

	// 最終ページURL
	String infeedAdFinalPageUrl;

	// -------- ターゲティング広告関連 --------
	// 画像ファイル
	List<MultipartFile> targetAdImageFileList;
	List<byte[]> targetAdImageDataList;

	// 最終ページURL
	String targetAdFinalPageUrl;

	// -------- リスティング広告関連 --------
	// 最終ページURL
	String listingAdFinalPageUrl;

	// 広告見出し１
	String listingAdTitle1;

	// 広告見出し２
	String listingAdTitle2;

	// 説明文
	String listingAdDescription;

}
