package jp.acepro.haishinsan.dto.google;

import java.util.List;

import lombok.Data;

@Data
public class GoogleTemplateDto {

	// テンプレートID
	Long templateId;

	// テンプレート名
	String templateName;

	// 優先順
	Integer templatePriority;

	// キャンペイン名
	String campaignName;

	// 予算
	Long budget;

	// デバイスタイプ
	String deviceType;

	// 地域
	List<Long> locationList;

	// 単価タイプ
	String unitPriceType;

	// 広告タイプ
	String adType;

	// -------- レスポンシブ広告関連 --------

	// 短い広告見出し
	String resAdShortTitle;

	// 説明文
	String resAdDescription;

	// 最終ページURL
	String resAdFinalPageUrl;

	// -------- イメージ広告関連 --------

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
