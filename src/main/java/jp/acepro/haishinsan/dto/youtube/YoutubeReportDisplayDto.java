package jp.acepro.haishinsan.dto.youtube;

import lombok.Data;

@Data
public class YoutubeReportDisplayDto {

	// キャンペーンID
	String campaignId;
	
	// キャンペーン名
	String campaignName;

	// デバイスタイプ
	String deviceType;
	
	// デバイス名
    String deviceName;

	// 地域ID
	Long locationId;
	
	// 地域名
	String locationName;

	// 日付
	String date;
	String dateSlash;

	// 表示回数
	Long impressions = 0l;

	// クリック数
	Long clicks = 0l;

	// 費用
	Long costs = 0l;

	// クリック率
	String ctr;

	// クリック単価
	Integer cpc;

	// インプレッション単価
	Integer cpm;
	
    // 視聴回数
    Long videoViews = 0l;

    // 視聴率
    String videoViewRate;

    // 視聴単価
    Integer avgCpv;
}
