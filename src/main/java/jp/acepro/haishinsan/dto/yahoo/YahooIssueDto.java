package jp.acepro.haishinsan.dto.yahoo;

import java.util.List;

import lombok.Data;

@Data
public class YahooIssueDto {

	// yahooキャンペインmanage ID
	Long yahooCampaignManageId;

	// 案件ID
	Long issueId;

	// キャンペーンID
	String campaignId;

	// キャンペーン名
	String campaignName;

	// 出稿先
	String advDestination;

	// デバイスタイプ
	String deviceType;

	// 予算
	Long budget;

	// 配信開始日
	String startDate;

	// 配信終了日
	String endDate;

	// 配信地域
	List<YahooLocationDto> locationList;

	// 配信地域コード
	String locationIds;

	// url
	String url;

	// 短い広告見出し
	String adShortTitle;

	// 広告見出し１
	String adTitle1;

	// 広告見出し２
	String adTitle2;

	// 説明文
	String adDescription;

	// 画像名
	String imageName;
	
	// 画像名リスト
	List<String> imageNameList;

}
