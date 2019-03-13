package jp.acepro.haishinsan.form;

import java.util.List;

import lombok.Data;

@Data
public class DspCampaignInputForm {

	String campaignName;
	String startDatetime;
	String endDatetime;
	Integer budget;
	Integer deviceType;
	List<DspCampaignCreInputForm> dspCampaignCreInputFormList;
	List<Integer> idList;
	String url;

	Long templateId;

	Integer status;
}
