package jp.acepro.haishinsan.dto.dsp;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DspCampaignDto {

	Long dspCampaignManageId;
	Integer campaignId;
	String campaignName;
	String startDatetime;
	String endDatetime;
	String startHour;
	String endHour;
	String startMin;
	String endMin;
	Integer budget;
	Integer deviceType;
	List<Integer> idList = new ArrayList<Integer>();
	List<DspCreativeDto> dspCreativeDtoList = new ArrayList<DspCreativeDto>();
	String url;
	Long templateId;
	Integer dailyBudget;
	Integer monthBudget;
	Integer segmentId;
}
