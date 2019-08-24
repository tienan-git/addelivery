package jp.acepro.haishinsan.dto.youtube;

import lombok.Data;

@Data
public class YoutubeDeviceReportDto {

	// キャンペインID
	Long campaignId;

	// キャンペイン名
	String campaignName;

	// 日付
	String date;

	// デバイス
	String deviceType;

	// 表示回数
	Long impressions;

	// クリック数
	Long clicks;

	// 費用
	Double costs;

	// 視聴回数
	Long videoViews;

	// 視聴率
	Double videoViewRate;

	// 視聴単価
	Double avgCpv;
}
