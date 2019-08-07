package jp.acepro.haishinsan.form;

import java.util.List;

import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import lombok.Data;

@Data
public class DspCampaignInputForm {

	String campaignName;
	String startDatetime;
	String endDatetime;
	Integer budget;
	Integer deviceType;
	List<Integer> idList;
	Integer segmentId;
	Long templateId;
}
