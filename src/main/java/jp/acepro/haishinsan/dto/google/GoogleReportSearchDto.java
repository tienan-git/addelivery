package jp.acepro.haishinsan.dto.google;

import java.util.List;

import org.springframework.data.util.Pair;

import lombok.Data;

@Data
public class GoogleReportSearchDto {
	
	// キャンペーン名リスト
	List<Pair<Long, String>> campaignPairList;
	
	// キャンペーンIDリスト
	List<Long> campaignIdList;
	
	// 期間種別
	Integer period;

	// 開始日
	String startDate;

	// 終了日
	String endDate;
	
	// レポートタイプ
	Integer reportType;
}
