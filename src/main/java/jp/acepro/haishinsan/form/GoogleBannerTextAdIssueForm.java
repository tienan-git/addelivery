package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.acepro.haishinsan.util.StringFormatter;
import lombok.Data;

@Data
public class GoogleBannerTextAdIssueForm {

	// 広告名
	String resAdName;

	// 短い広告見出し
	String resAdShortTitle;

	// 説明文
	String resAdDescription;

	// 画像ファイル01
	// Original File
	MultipartFile imageFile01;
	// Original File Name
	String imageFileName01;
	// Data for HTML Displaying(Base64)
	String imageData01;
	// Data for Google Ads API Image Uploading(bytes)
	byte[] imageBytes01;

	// 画像ファイル02
	// Original File
	MultipartFile imageFile02;
	// Original File Name
	String imageFileName02;
	// Data for HTML Displaying(Base64)
	String imageData02;
	// Data for Google Ads API Image Uploading(bytes)
	byte[] imageBytes02;
	
	// テンプレートID
	Long templateId;

	// テンプレート名
	String templateName;

	// セグメントID
	Integer segmentId;

	// セグメントURL
	String url;

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
}
