package jp.acepro.haishinsan.dto.yahoo;

import java.util.List;

import lombok.Data;

@Data
public class YahooGraphReportDto {

	// 日別用
	String day;

	// グラフ表示用
	List<String> reportTypeList;
	List<String> impressionList;
	List<String> clicksList;
	List<String> spendList;
	List<String> CTRList;
	List<String> CPCList;
	List<String> CPMList;
}
