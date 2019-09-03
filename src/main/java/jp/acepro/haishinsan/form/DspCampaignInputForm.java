package jp.acepro.haishinsan.form;

import java.util.List;

import lombok.Data;

@Data
public class DspCampaignInputForm {

	String campaignName;
	String startDatetime;
	String endDatetime;
	String startHour;
	String endHour;
	String startMin;
	String endMin;
	Integer budget;
	Integer deviceType;
	List<Integer> idList;
	Integer segmentId;
	Long templateId;
}
