package jp.acepro.haishinsan.dto.google;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class GoogleCampaignDto {

	// テンプレートID
	Long templateId;

	// テンプレート名
	String templateName;

	// キャンペインID
	Long campaignId;

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

	// 終了日
	String endDate;

	// デバイス
	String deviceType;

	// 広告タイプ
	String adType;

	// -------- レスポンシブ広告関連 --------
	// 画像ファイル
	List<MultipartFile> resAdImageFileList;
	List<String> resAdImageFileNameList;
	List<byte[]> resAdImageBytesList;

	// 短い広告見出し
	String resAdShortTitle;

	// 説明文
	String resAdDescription;

	// 最終ページURL
	String resAdFinalPageUrl;

	// -------- イメージ広告関連 --------
	// 画像ファイル
	List<MultipartFile> imageAdImageFileList;
	List<String> imageAdImageFileNameList;
	List<byte[]> imageAdImageBytesList;

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

	// イメージ１URL
	String image1Url;

	// イメージ２URL
	String image2Url;

	// イメージ３URL
	String image3Url;

	// イメージ４URL
	String image4Url;

	// 見出し１
	String adTitle1;

	// 見出し２
	String adTitle2;

	// 説明文
	String adDescription;

	// 最終ページURL
	String linkUrl;
}
