package jp.acepro.haishinsan.service.google;

import jp.acepro.haishinsan.dto.google.GoogleReportDto;
import jp.acepro.haishinsan.dto.google.GoogleReportSearchDto;

public interface GoogleReportService {

	// レポート取得（API経由）
	void getReport();

	// レポート表示（デバイス別）
	GoogleReportDto showDeviceReport(GoogleReportSearchDto googleReportSearchDto);

	// レポート表示（地域別）
	GoogleReportDto showLocationReport(GoogleReportSearchDto googleReportSearchDto);

	// レポート表示（日別）
	GoogleReportDto showDailyReport(GoogleReportSearchDto googleReportSearchDto);

	// CSVダウンロード
	String download(GoogleReportSearchDto googleReportSearchDto);
	
    void adjustDailyBudget();
}
