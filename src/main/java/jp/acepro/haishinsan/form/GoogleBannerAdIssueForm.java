package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.acepro.haishinsan.util.StringFormatter;
import lombok.Data;

@Data
public class GoogleBannerAdIssueForm {

	// 広告名
	String imgAdName;

	// 画像ファイル01
	// Original File
	MultipartFile imageFile01;
	// Original File Name
	String imageFileName01;
	// Data for HTML Displaying(Base64)
	String imageData01;
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

	// 画像ファイル03
	// Original File
	MultipartFile imageFile03;
	// Original File Name
	String imageFileName03;
	// Data for HTML Displaying(Base64)
	String imageData03;
	// Data for Google Ads API Image Uploading(bytes)
	byte[] imageBytes03;

	// 画像ファイル04
	// Original File
	MultipartFile imageFile04;
	// Original File Name
	String imageFileName04;
	// Data for HTML Displaying(Base64)
	String imageData04;
	// Data for Google Ads API Image Uploading(bytes)
	byte[] imageBytes04;
	
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
