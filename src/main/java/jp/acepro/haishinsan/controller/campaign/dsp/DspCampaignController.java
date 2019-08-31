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
import jp.acepro.haishinsan.form.DspCampaignInputForm;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;
import jp.acepro.haishinsan.service.dsp.DspCreativeService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.util.ContextUtil;

@Controller
@RequestMapping("/campaign/dsp")
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

	@GetMapping("/selectCreative")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView selectCreative(@ModelAttribute DspCampaignInputForm dspCampaignInputForm) {

		// 作成したCreativeを取得
		List<DspCreativeDto> dspCreativeDtoList = dspCreativeService.creativeListFromDb();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("campaign/dsp/selectCreative");
		modelAndView.addObject("dspCreativeDtoList", dspCreativeDtoList);

		// 全てクリエイティブをsessionに格納する
		session.setAttribute("dspCreativeDtoList", dspCreativeDtoList);

		return modelAndView;
	}

	@PostMapping("/returnToCreativeList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView returnToCreativeList(@Validated DspCampaignInputForm dspCampaignInputForm, BindingResult result) {

		return null;
	}

	@PostMapping("/createCampaign")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView createCampaign(@Validated DspCampaignInputForm dspCampaignInputForm, BindingResult result) {

		if (dspCampaignInputForm.getIdList().isEmpty()) {
			result.reject("E30005");
			return selectCreative(dspCampaignInputForm);
		}

		// 最低予算金額
		BigDecimal monthBudgetFlag = BigDecimal.valueOf(30000).divide(BigDecimal.valueOf(100 - ContextUtil.getCurrentShop().getMarginRatio()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_UP), 2, BigDecimal.ROUND_UP);
		// 最低日次予算金額
		BigDecimal dailyBudgetFlag = monthBudgetFlag.divide(BigDecimal.valueOf(30), 2, BigDecimal.ROUND_UP);

		// 全てクリエイティブをsessionから取得
		List<DspCreativeDto> dspCreativeDtoList = (ArrayList<DspCreativeDto>) session.getAttribute("dspCreativeDtoList");

		List<DspCreativeDto> selectedDspCreativeDtoList = new ArrayList<DspCreativeDto>();

		// 選択したクリエイティブ情報の上、最も早い日付を取得
		LocalDateTime dateTime = null;
		for (DspCreativeDto dspCreativeDto : dspCreativeDtoList) {
			for (Integer id : dspCampaignInputForm.getIdList()) {
				if (dspCreativeDto.getCreativeId().equals(id)) {
					selectedDspCreativeDtoList.add(dspCreativeDto);
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
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.selectUrlByDateTimeWithNoCheck(dateTime);
		dspCampaignInputForm.setDeviceType(9);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("campaign/dsp/createCampaign");
		modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
		modelAndView.addObject("dailyBudgetFlag", dailyBudgetFlag.longValue());

		session.setAttribute("idList", dspCampaignInputForm.getIdList());
		session.setAttribute("selectedDspCreativeDtoList", selectedDspCreativeDtoList);
		session.setAttribute("dspSegmentDtoList", dspSegmentDtoList);

		return modelAndView;
	}

	@PostMapping("/confirmCampaign")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView confirmCampaign(@Validated DspCampaignInputForm dspCampaignInputForm, BindingResult result) {

		// テンプレート情報を取って、優先度一番高いの方で使う
		DspTemplateDto dspTemplateDto = dspApiService.getDefaultTemplate();

		List<DspSegmentListDto> dspSegmentDtoList = (ArrayList<DspSegmentListDto>) session.getAttribute("dspSegmentDtoList");

		// FormをDtoにして、キャンペーンを作成する
		DspCampaignDto dspCampaignDto = new DspCampaignDto();
		dspCampaignDto.setCampaignName(dspCampaignInputForm.getCampaignName());
		dspCampaignDto.setStartDatetime(dspCampaignInputForm.getStartDatetime());
		dspCampaignDto.setEndDatetime(dspCampaignInputForm.getEndDatetime());
		dspCampaignDto.setBudget(dspCampaignInputForm.getBudget());
		dspCampaignDto.setDeviceType(dspCampaignInputForm.getDeviceType());
		dspCampaignDto.setTemplateId(dspTemplateDto.getTemplateId());
		dspCampaignDto.setSegmentId(dspCampaignInputForm.getSegmentId());
		for (DspSegmentListDto dspSegmentListDto : dspSegmentDtoList) {
			if (dspSegmentListDto.getSegmentId().equals(dspCampaignInputForm.getSegmentId())) {
				dspCampaignDto.setUrl(dspSegmentListDto.getUrl());
				break;
			}
		}
		// 選択したクリエイティブをsessionから取得
		List<DspCreativeDto> dspCreativeDtoList = (ArrayList<DspCreativeDto>) session.getAttribute("selectedDspCreativeDtoList");
		// 選択したクリエイティブIDをsessionから取得
		List<Integer> ids = (ArrayList<Integer>) session.getAttribute("idList");
		dspCampaignDto.setDspCreativeDtoList(dspCreativeDtoList);
		dspCampaignDto.setIdList(ids);
		dspCampaignInputForm.setIdList(ids);
		try {
			dspCampaignDto = dspCampaignService.validate(dspCampaignDto);
		} catch (BusinessException e) {
			result.reject(e.getMessage());
			return createCampaign(dspCampaignInputForm, result);
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("campaign/dsp/confirmCampaign");
		modelAndView.addObject("dspCampaignDto", dspCampaignDto);
		modelAndView.addObject("dspTemplateDto", dspTemplateDto);

		session.setAttribute("dspCampaignDto", dspCampaignDto);

		return modelAndView;
	}

	@GetMapping("/completeCampaign")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView completeCampaign() {

		// sessionからdspCampaignDto対象を取得
		DspCampaignDto dspCampaignDto = (DspCampaignDto) session.getAttribute("dspCampaignDto");

		// 広告キャンペーン作成
		dspCampaignDto = dspCampaignService.createCampaign(dspCampaignDto, null);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("campaign/dsp/createSuccess");

		session.removeAttribute("dspCampaignDto");
		session.removeAttribute("idList");
		session.removeAttribute("selectedDspCreativeDtoList");
		session.removeAttribute("dspCreativeDtoList");

		// オペレーションログ記録
		operationService.create(Operation.DSP_CAMPAIGN_CREATE.getValue(), String.valueOf(dspCampaignDto.getCampaignId()));

		return modelAndView;
	}

	@GetMapping("/campaignList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
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
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
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
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
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
