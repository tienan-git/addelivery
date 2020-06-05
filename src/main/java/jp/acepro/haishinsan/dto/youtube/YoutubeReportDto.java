package jp.acepro.haishinsan.dto.youtube;

import java.util.List;

import lombok.Data;

@Data
public class YoutubeReportDto {

	// テーブル表示用
	List<YoutubeReportDisplayDto> youtubeReportDisplayDtoList;

	// グラフ表示用
	List<YoutubeReportDisplayDto> youtubeReportGraphDtoList;
}
