package jp.acepro.haishinsan.service.dsp;

import java.util.List;

import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDetailDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDto;

public interface DspCampaignService {

	DspCampaignDto createCampaign(DspCampaignDto dspCampaignDto, IssueDto issueDto);

	List<DspCampaignDto> getCampaignList();

	DspCampaignDetailDto getCampaignDetail(Integer campaignId, Integer dspUserId);

	void updateCampaign(Long issueId, String status);

	DspCampaignDetailDto deleteCampaign(Integer campaignId);

	void createDspCampaign(DspCampaignDto dspCampaignDto, IssueDto issueDto);

	/**
	 * 入力チェック
	 * 
	 * @param dspCampaignDto
	 * @return
	 */
	DspCampaignDto validate(DspCampaignDto dspCampaignDto);

	Long saveCampaign(DspCampaignDto dspCampaignDto);

	void validateCreative(DspCampaignDto dspCampaignDto);

}
