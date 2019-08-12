package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.acepro.haishinsan.util.StringFormatter;
import lombok.Data;

@Data
public class GoogleIssueInputForm {

	// テンプレートID
	Long templateId;
	
	// テンプレート名
	String templateName;

	// キャンペイン名
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
		return StringFormatter.dateHyphenToSlash(startDate);
	}

	// 終了日
	String endDate;
    public String getEndDateSlash() {
		return StringFormatter.dateHyphenToSlash(endDate);
	}

	// デバイスタイプ
	String deviceType;

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

	// 最終ページURL
	String resAdFinalPageUrl;

	// -------- イメージ広告関連 --------
	// 画像ファイル
	List<MultipartFile> imageAdImageFileList;
	List<byte[]> imageAdImageDataList;

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
