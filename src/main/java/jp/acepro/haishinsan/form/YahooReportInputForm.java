package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.data.util.Pair;

import lombok.Data;

@Data
public class YahooReportInputForm {

	List<Pair<String, String>> campaignList;
	List<String> campaignIdList;
	Integer period;
	String campaignId;
	String startDate;
	String endDate;

}
