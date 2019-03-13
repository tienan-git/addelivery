package jp.acepro.haishinsan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.dto.dsp.DspCampaignDto;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;

@RestController
@RequestMapping("/dsp/api")
public class DspApiController {

	@Autowired
	DspCampaignService dspCampaignService;
	
	@Autowired
	OperationService operationService;

	@GetMapping("/{campaignId}/{switchFlag}")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CAMPAIGN_MANAGE + "')")
	public ModelAndView Creative(@PathVariable Integer campaignId, @PathVariable String switchFlag) {

		dspCampaignService.updateCampaign(campaignId, switchFlag);

		List<DspCampaignDto> dspCampaignDtoList = dspCampaignService.getCampaignList();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/campaignList");
		modelAndView.addObject("dspCampaignDtoList", dspCampaignDtoList);

		// オペレーションログ記録
		operationService.create(Operation.DSP_CAMPAIGN_UPDATE.getValue(), String.valueOf(campaignId));

		return modelAndView;

	}

}
