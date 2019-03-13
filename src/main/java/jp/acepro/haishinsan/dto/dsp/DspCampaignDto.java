package jp.acepro.haishinsan.dto.dsp;

import java.util.ArrayList;
import java.util.List;

import jp.acepro.haishinsan.form.DspCampaignCreInputForm;
import lombok.Data;

@Data
public class DspCampaignDto {

	Long dspCampaignManageId;
	Integer campaignId;
	String campaignName;
	String startDatetime;
	String endDatetime;
	Integer budget;
	Integer deviceType;
	List<DspCampaignCreInputForm> dspCampaignCreInputFormList  = new ArrayList<DspCampaignCreInputForm>();
	List<Integer> idList = new ArrayList<Integer>();
	String url;
	Long templateId;
	String approvalFlag;
	Integer status;
}
