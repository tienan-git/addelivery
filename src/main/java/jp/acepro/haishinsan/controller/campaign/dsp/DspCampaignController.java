package jp.acepro.haishinsan.controller.campaign.dsp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.dto.dsp.DspCampaignDetailDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDto;
import jp.acepro.haishinsan.dto.dsp.DspCreativeDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.dsp.DspTemplateDto;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.DspCampaignCreInputForm;
import jp.acepro.haishinsan.form.DspCampaignInputForm;
import jp.acepro.haishinsan.mapper.DspMapper;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;
import jp.acepro.haishinsan.service.dsp.DspCreativeService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.util.ContextUtil;

@Controller
@RequestMapping("/dsp")
public class DspCampaignController {

	@Autowired
	HttpSession session;

	@Autowired
	DspApiService dspApiService;

	@Autowired
	DspCreativeService dspCreativeService;

	@Autowired
	DspSegmentService dspSegmentService;

	@Autowired
	DspCampaignService dspCampaignService;

	@Autowired
	OperationService operationService;

	@GetMapping("/selectCampaign")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CAMPAIGN_MANAGE + "')")
	public ModelAndView selectCreative() {

		// 作成したCreativeを取得
		List<DspCreativeDto> dspCreativeDtoList = dspCreativeService.creativeListFromDb();

		DspCampaignInputForm dspCampaignInputForm = new DspCampaignInputForm();

		// dspCampaignCreInputFormList作成して、UIに添付
		List<DspCampaignCreInputForm> dspCampaignCreInputFormList = new ArrayList<DspCampaignCreInputForm>();
		for (DspCreativeDto dspCreativeDto : dspCreativeDtoList) {
			DspCampaignCreInputForm dspCampaignCreInputForm = new DspCampaignCreInputForm();
			dspCampaignCreInputForm.setCreativeId(dspCreativeDto.getCreativeId());
			dspCampaignCreInputForm.setCreativeName(dspCreativeDto.getCreativeName());
			dspCampaignCreInputFormList.add(dspCampaignCreInputForm);
		}
		dspCampaignInputForm.setDspCampaignCreInputFormList(dspCampaignCreInputFormList);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("campaign/dsp/selectCreative");
		modelAndView.addObject("dspCampaignInputForm", dspCampaignInputForm);

		session.setAttribute("dspCreativeDtoList", dspCreativeDtoList);
		return modelAndView;
	}

	@PostMapping("/createCampaign")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CAMPAIGN_MANAGE + "')")
	public ModelAndView createCampaign(@ModelAttribute DspCampaignInputForm dspCampaignInputForm) {

		// 最低予算金額
		BigDecimal monthBudgetFlag = BigDecimal.valueOf(30000).divide(BigDecimal.valueOf(100 - ContextUtil.getCurrentShop().getMarginRatio()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_UP), 2, BigDecimal.ROUND_UP);
		// 最低日次予算金額
		BigDecimal dailyBudgetFlag = monthBudgetFlag.divide(BigDecimal.valueOf(30), 2, BigDecimal.ROUND_UP);

		// 選択したクリエイティブ情報を取得
		List<DspCreativeDto> dspCreativeDtoList = (ArrayList<DspCreativeDto>) session.getAttribute("dspCreativeDtoList");

		// 選択したクリエイティブ情報の上、最も早い日付を取得
		LocalDateTime dateTime = null;
		for (DspCreativeDto dspCreativeDto : dspCreativeDtoList) {
			for (Integer id : dspCampaignInputForm.getIdList()) {
				if (dspCreativeDto.getCreativeId().equals(id)) {
					if (dateTime != null) {
						dateTime = dspCreativeDto.getCreatedAt().isAfter(dateTime) ? dateTime : dspCreativeDto.getCreatedAt();
					} else {
						dateTime = dspCreativeDto.getCreatedAt();
					}
				}
			}
		}

		if (dateTime == null) {
			dateTime = LocalDateTime.now();
		}

		// 日付によるセグメント情報を取得
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.selectUrlByDateTime(dateTime);
		dspCampaignInputForm.setDeviceType(9);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("campaign/dsp/createCampaign");
		modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
		modelAndView.addObject("dailyBudgetFlag", dailyBudgetFlag.longValue());

		session.setAttribute("idList", dspCampaignInputForm.getIdList());

		return modelAndView;
	}

	@PostMapping("/confirmCampaign")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CAMPAIGN_MANAGE + "')")
	public ModelAndView confirmCampaign(@ModelAttribute DspCampaignInputForm dspCampaignInputForm) {

		// テンプレート情報を取って、優先度一番高いの方で使う
		DspTemplateDto dspTemplateDto = dspApiService.getDefaultTemplate();

		// FormをDtoにして、キャンペーンを作成する
		DspCampaignDto dspCampaignDto = DspMapper.INSTANCE.campFormToDto(dspCampaignInputForm);
		dspCampaignDto.setTemplateId(dspTemplateDto.getTemplateId());
		List<Integer> ids = (List<Integer>) session.getAttribute("idList");
		for (Integer i : ids) {
			DspCampaignCreInputForm dspCampaignCreInputForm = new DspCampaignCreInputForm();
			dspCampaignCreInputForm.setCreativeId(i);
			dspCampaignDto.getDspCampaignCreInputFormList().add(dspCampaignCreInputForm);
			dspCampaignDto.getIdList().add(i);
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("campaign/dsp/confirmCampaign");
		modelAndView.addObject("dspCampaignDto", dspCampaignDto);
		modelAndView.addObject("dspTemplateDto", dspTemplateDto);
		session.setAttribute("dspCampaignDto", dspCampaignDto);

		return modelAndView;
	}

	@PostMapping("/completeCampaign")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CAMPAIGN_MANAGE + "')")
	public ModelAndView completeCampaign(@Validated DspCampaignInputForm dspCampaignInputForm, BindingResult result) {

		// 作成したCreativeを取得
		List<DspCreativeDto> dspCreativeDtoList = dspCreativeService.creativeListFromDb();

		// FormをDtoにして、キャンペーンを作成する
		DspCampaignDto dspCampaignDto = DspMapper.INSTANCE.campFormToDto(dspCampaignInputForm);
		for (Integer i : dspCampaignInputForm.getIdList()) {
			DspCampaignCreInputForm dspCampaignCreInputForm = new DspCampaignCreInputForm();
			dspCampaignCreInputForm.setCreativeId(i);
			dspCampaignDto.getDspCampaignCreInputFormList().add(dspCampaignCreInputForm);
			dspCampaignDto.getIdList().add(i);
		}
		DspCampaignDto newDspCampaignDto = null;
		try {
			newDspCampaignDto = dspCampaignService.createCampaign(dspCampaignDto, null);
		} catch (BusinessException e) {
			result.reject(e.getMessage());
			return createCampaign(dspCampaignInputForm);
		}
		// dspCampaignCreInputFormList作成して、UIに添付
		List<DspCampaignCreInputForm> dspCampaignCreInputFormList = new ArrayList<DspCampaignCreInputForm>();
		for (DspCreativeDto dspCreativeDto : dspCreativeDtoList) {
			DspCampaignCreInputForm dspCampaignCreInputForm = new DspCampaignCreInputForm();
			dspCampaignCreInputForm.setCreativeId(dspCreativeDto.getCreativeId());
			dspCampaignCreInputForm.setCreativeName(dspCreativeDto.getCreativeName());
			dspCampaignCreInputFormList.add(dspCampaignCreInputForm);
		}
		dspCampaignDto.setDspCampaignCreInputFormList(dspCampaignCreInputFormList);
		dspCampaignDto.setCampaignId(newDspCampaignDto.getCampaignId());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/completeCampaign");
		modelAndView.addObject("newDspCampaignDto", dspCampaignDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_CAMPAIGN_CREATE.getValue(), String.valueOf(newDspCampaignDto.getCampaignId()));

		return modelAndView;
	}

	@GetMapping("/campaignList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CAMPAIGN_VIEW + "')")
	public ModelAndView campaignList() {

		List<DspCampaignDto> dspCampaignDtoList = dspCampaignService.getCampaignList();

		Integer roleId = ContextUtil.getRoleId();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/campaignList");
		modelAndView.addObject("dspCampaignDtoList", dspCampaignDtoList);
		modelAndView.addObject("roleId", roleId);

		// オペレーションログ記録
		operationService.create(Operation.DSP_CAMPAIGN_LIST.getValue(), null);

		return modelAndView;
	}

	@GetMapping("/campaignDetail")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CAMPAIGN_VIEW + "')")
	public ModelAndView campaignDetail(@RequestParam Integer campaignId) {

		DspCampaignDetailDto dspCampaignDetailDto = dspCampaignService.getCampaignDetail(campaignId, ContextUtil.getCurrentShop().getDspUserId());

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/campaignDetail");
		modelAndView.addObject("dspCampaignDetailDto", dspCampaignDetailDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_CAMPAIGN_VIEW.getValue(), String.valueOf(campaignId));

		return modelAndView;
	}

	@PostMapping("/campaignDelete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CAMPAIGN_MANAGE + "')")
	public ModelAndView campaignDelete(@RequestParam Integer campaignId) {

		DspCampaignDetailDto dspCampaignDetailDto = dspCampaignService.deleteCampaign(campaignId);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/deleteCampaign");
		modelAndView.addObject("dspCampaignDetailDto", dspCampaignDetailDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_CAMPAIGN_DELETE.getValue(), String.valueOf(campaignId));

		return modelAndView;
	}

}
