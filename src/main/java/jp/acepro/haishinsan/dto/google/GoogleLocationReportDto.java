package jp.acepro.haishinsan.dto.google;

import lombok.Data;

@Data
public class GoogleLocationReportDto {

	// キャンペインID
	Long campaignId;

	// キャンペイン名
	String campaignName;

	// 日付
	String date;

	// 地域ID
	Long locationId;

	// 表示回数
	Long impressions;

	// クリック数
	Long clicks;

	// 費用
	Double costs;
}
