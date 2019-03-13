package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.data.util.Pair;

import lombok.Data;

@Data
public class FbReportInputForm {

	List<Pair<String, String>> campaignPairList;
	List<String> campaignIdList;
	Integer period;
	String campaignIds;
	String campaignId;
	String startDate;
	String endDate;
	
}
