package jp.acepro.haishinsan.dto.dsp;

import java.util.List;

import lombok.Data;

@Data
public class DspCampaignDetailDto {

	// キャンペーン情報
	Integer campaignId;
	String campaignName;
	String startDatetime;
	String endDatetime;
	Integer budget;
	Integer status;
	Integer device;

	// クリエイティブ情報
	List<DspCreativeDto> dspCreativeDtoList;

	// セグメント情報
	DspSegmentDto dspSegmentDto = new DspSegmentDto();
}
