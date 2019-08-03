package jp.acepro.haishinsan.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadGoogleBannerTextAdCreateForm {

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
}
