package jp.acepro.haishinsan.dto.twitter;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TwitterGraphReportDto {
	
	//日別用
	String day;
	
	//グラフ表示用
	List<String> reportTypeList = new ArrayList<>();
	List<String> impressionList;
	List<String> clicksList;
	List<String> followsList;
	List<String> priceList;
	List<String> CTRList;
	List<String> CPCList;
	List<String> CPMList;
}
