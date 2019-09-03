package jp.acepro.haishinsan.dto.google;

import java.util.List;

import jp.acepro.haishinsan.util.StringFormatter;
import lombok.Data;

@Data
public class GoogleIssueDto {

	// 承認フラグ
	String approvalFlag;

	// キャンペーンステータス
	String campaignStatus;

	// キャンペーンID
	Long campaignId;

	// キャンペーン名
	String campaignName;

	// 地域
	List<Long> locationList;

	// 予算
	Long budget;

	// 単価タイプ
	String unitPriceType;

	// 開始日
	String startDate;

	public String getStartDateSlash() {
		return StringFormatter.formatToSlash(startDate);
	}

	// 終了日
	String endDate;

	public String getEndDateSlash() {
		return StringFormatter.formatToSlash(endDate);
	}

    String startTime;
    String endTime;
    String startHour;
    String startMin;
    String endHour;
    String endMin;

	// デバイスタイプ
	String deviceType;

	// 広告タイプ
	String adType;

	// -------- レスポンシブ広告関連 --------
	// 画像ファイル
	List<String> resAdImageUrlList;

	// 短い広告見出し
	String resAdShortTitle;

	// 説明文
	String resAdDescription;

	// 最終ページURL
	String resAdFinalPageUrl;

	// -------- イメージ広告関連 --------
	// 画像ファイル
	List<String> imageAdImageUrlList;

	// 最終ページURL
	String imageAdFinalPageUrl;

	// -------- 拡張テキスト広告関連 --------
	// 最終ページURL
	String textAdFinalPageUrl;

	// 広告見出し１
	String textAdTitle1;

	// 広告見出し２
	String textAdTitle2;

	// 説明文
	String textAdDescription;
}
