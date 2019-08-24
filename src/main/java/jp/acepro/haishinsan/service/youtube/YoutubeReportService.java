package jp.acepro.haishinsan.service.youtube;

import java.util.List;

import jp.acepro.haishinsan.dto.youtube.YoutubeCampaignInfoDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeReportDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeReportSearchDto;

public interface YoutubeReportService {

	// レポート取得（API経由）
	void getReport();

	// レポート表示（デバイス別）
	YoutubeReportDto showDeviceReport(YoutubeReportSearchDto youtubeReportSearchDto);

	// レポート表示（地域別）
	YoutubeReportDto showLocationReport(YoutubeReportSearchDto youtubeReportSearchDto);

	// レポート表示（日別）
	YoutubeReportDto showDailyReport(YoutubeReportSearchDto youtubeReportSearchDto);

	// キャンペーン一覧取得（API経由）
	List<YoutubeCampaignInfoDto> getCampaignList();

	// CSVダウンロード
	String download(YoutubeReportSearchDto youtubeReportSearchDto);
}
