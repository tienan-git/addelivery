package jp.acepro.haishinsan.service.issue;

import java.time.LocalDate;
import java.util.List;

import jp.acepro.haishinsan.dto.twitter.TwitterDisplayReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterGraphReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterReportDto;

public interface TwitterReportingService {

	// Batch : 全レポート取得
	void getReport(LocalDate date);

	// デバイス別
	List<TwitterDisplayReportDto> getTwitterDeviceReporting(TwitterReportDto twitterReportDto);

	// 地域別
	List<TwitterDisplayReportDto> getTwitterRegionReporting(TwitterReportDto twitterReportDto);

	// 日別
	List<TwitterDisplayReportDto> getTwitterDayReporting(TwitterReportDto twitterReportDto);

	// デバイス別 (グラフ用)
	TwitterGraphReportDto getTwitterDeviceReportingGraph(TwitterReportDto twitterReportDto);

	// 地域別 (グラフ用)
	TwitterGraphReportDto getTwitterRegionReportingGraph(TwitterReportDto twitterReportDto);

	// 日別 (グラフ用)
	TwitterGraphReportDto getTwitterDayReportingGraph(TwitterReportDto twitterReportDto);

	// レポートダウンロード
	String download(TwitterReportDto twitterReportDto);

}
