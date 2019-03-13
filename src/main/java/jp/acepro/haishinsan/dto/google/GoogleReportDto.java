package jp.acepro.haishinsan.dto.google;

import java.util.List;

import lombok.Data;

@Data
public class GoogleReportDto {

	// テーブル表示用
	List<GoogleReportDisplayDto> googleReportDisplayDtoList;
	
	// グラフ表示用
	List<GoogleReportDisplayDto> googleReportGraphDtoList;
}
