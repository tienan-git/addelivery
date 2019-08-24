package jp.acepro.haishinsan.dto.youtube;

import java.util.List;

import lombok.Data;

@Data
public class YoutubeReportSearchDto {

	// キャンペーンリスト
	List<Long> campaignIdList;

	// キャンペイID
	Long campaignId;

	// 期間種別
	Integer period;

	// 開始日
	String startDate;

	// 終了日
	String endDate;

	// レポートタイプ
	Integer reportType;
}
