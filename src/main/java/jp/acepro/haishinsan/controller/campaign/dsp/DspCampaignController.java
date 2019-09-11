package jp.acepro.haishinsan.controller.campaign.dsp;

import java.io.IOException;
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

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDetailDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDto;
import jp.acepro.haishinsan.dto.dsp.DspCreativeDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.dsp.DspTemplateDto;
import jp.acepro.haishinsan.enums.MediaType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.DspCampaignInputForm;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;
import jp.acepro.haishinsan.service.dsp.DspCreativeService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.ImageUtil;

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

	@Autowired
	ImageUtil imageUtil;

	@GetMapping("/selectAction")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView selectAction() {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("campaign/dsp/selectAction");

		return modelAndView;
	}

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
		dspCampaignDto.setStartHour(dspCampaignInputForm.getStartHour());
		dspCampaignDto.setEndHour(dspCampaignInputForm.getEndHour());
		dspCampaignDto.setStartMin(dspCampaignInputForm.getStartMin());
		dspCampaignDto.setEndMin(dspCampaignInputForm.getEndMin());
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
			dspCampaignService.validateCreative(dspCampaignDto);
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

	@PostMapping("/createNewCampaign")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView createNewCampaign(@Validated DspCampaignInputForm dspCampaignInputForm, BindingResult result) {

		// 最低予算金額
		BigDecimal monthBudgetFlag = BigDecimal.valueOf(30000).divide(BigDecimal.valueOf(100 - ContextUtil.getCurrentShop().getMarginRatio()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_UP), 2, BigDecimal.ROUND_UP);
		// 最低日次予算金額
		BigDecimal dailyBudgetFlag = monthBudgetFlag.divide(BigDecimal.valueOf(30), 2, BigDecimal.ROUND_UP);

		// 日付によるセグメント情報を取得
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.selectUrlByDateTimeWithNoCheck(LocalDateTime.now());
		dspCampaignInputForm.setDeviceType(9);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("campaign/dsp/createNewCampaign");
		modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
		modelAndView.addObject("dailyBudgetFlag", dailyBudgetFlag.longValue());

		session.setAttribute("dspSegmentDtoList", dspSegmentDtoList);

		return modelAndView;
	}

	@PostMapping("/confirmNewCampaign")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView confirmNewCampaign(@Validated DspCampaignInputForm dspCampaignInputForm, BindingResult result) throws IOException {

		// クリエイティブ表示させる処理
		String imaBase64 = null;
		byte[] bytes = null;
		String imaBase64_2 = null;
		byte[] bytes2 = null;
		String imaBase64_3 = null;
		byte[] bytes3 = null;
		String imaBase64_4 = null;
		byte[] bytes4 = null;
		String imaBase64_5 = null;
		byte[] bytes5 = null;

		// FormからDtoまで変更
		DspCreativeDto dspCreativeDto = new DspCreativeDto();
		try {
			if (dspCampaignInputForm.getImage() != null && dspCampaignInputForm.getCreativeName() != null) {
				imaBase64 = imageUtil.getImageBytes(dspCampaignInputForm.getImage(), MediaType.DSP.getValue());
				bytes = dspCampaignInputForm.getImage().getBytes();
				dspCreativeDto.setCreativeName(dspCampaignInputForm.getCreativeName());
				dspCreativeDto.setBytes(bytes);
				dspCreativeDto.setBase64Str(imaBase64);
			}
			if (dspCampaignInputForm.getImage2() != null && dspCampaignInputForm.getCreativeName2() != null) {
				imaBase64_2 = imageUtil.getImageBytes(dspCampaignInputForm.getImage2(), MediaType.DSP.getValue());
				bytes2 = dspCampaignInputForm.getImage2().getBytes();
				dspCreativeDto.setCreativeName2(dspCampaignInputForm.getCreativeName2());
				dspCreativeDto.setBytes2(bytes2);
				dspCreativeDto.setBase64Str2(imaBase64_2);
			}
			if (dspCampaignInputForm.getImage3() != null && dspCampaignInputForm.getCreativeName3() != null) {
				imaBase64_3 = imageUtil.getImageBytes(dspCampaignInputForm.getImage3(), MediaType.DSP.getValue());
				bytes3 = dspCampaignInputForm.getImage3().getBytes();
				dspCreativeDto.setCreativeName3(dspCampaignInputForm.getCreativeName3());
				dspCreativeDto.setBytes3(bytes3);
				dspCreativeDto.setBase64Str3(imaBase64_3);
			}
			if (dspCampaignInputForm.getImage4() != null && dspCampaignInputForm.getCreativeName4() != null) {
				imaBase64_4 = imageUtil.getImageBytes(dspCampaignInputForm.getImage4(), MediaType.DSP.getValue());
				bytes4 = dspCampaignInputForm.getImage4().getBytes();
				dspCreativeDto.setCreativeName4(dspCampaignInputForm.getCreativeName4());
				dspCreativeDto.setBytes4(bytes4);
				dspCreativeDto.setBase64Str4(imaBase64_4);
			}
			if (dspCampaignInputForm.getImage5() != null && dspCampaignInputForm.getCreativeName5() != null) {
				imaBase64_5 = imageUtil.getImageBytes(dspCampaignInputForm.getImage5(), MediaType.DSP.getValue());
				bytes5 = dspCampaignInputForm.getImage5().getBytes();
				dspCreativeDto.setCreativeName5(dspCampaignInputForm.getCreativeName5());
				dspCreativeDto.setBytes5(bytes5);
				dspCreativeDto.setBase64Str5(imaBase64_5);
			}
			if (dspCampaignInputForm.getImage5() == null && dspCampaignInputForm.getImage2() == null && dspCampaignInputForm.getImage3() == null && dspCampaignInputForm.getImage4() == null && dspCampaignInputForm.getImage5() == null) {
				throw new BusinessException(ErrorCodeConstant.E30005);
			}
		} catch (BusinessException e) {
			result.reject(e.getMessage(), null);
			// 最低予算金額
			BigDecimal monthBudgetFlag = BigDecimal.valueOf(30000).divide(BigDecimal.valueOf(100 - ContextUtil.getCurrentShop().getMarginRatio()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_UP), 2, BigDecimal.ROUND_UP);
			// 最低日次予算金額
			BigDecimal dailyBudgetFlag = monthBudgetFlag.divide(BigDecimal.valueOf(30), 2, BigDecimal.ROUND_UP);

			// 日付によるセグメント情報を取得
			List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.selectUrlByDateTimeWithNoCheck(LocalDateTime.now());
			dspCampaignInputForm.setDeviceType(9);

			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("campaign/dsp/createNewCampaign");
			modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
			modelAndView.addObject("dailyBudgetFlag", dailyBudgetFlag.longValue());
			modelAndView.addObject("dspCampaignInputForm", dspCampaignInputForm);
			return modelAndView;
		}

		// テンプレート情報を取って、優先度一番高いの方で使う
		DspTemplateDto dspTemplateDto = dspApiService.getDefaultTemplate();

		List<DspSegmentListDto> dspSegmentDtoList = (ArrayList<DspSegmentListDto>) session.getAttribute("dspSegmentDtoList");

		// FormをDtoにして、キャンペーンを作成する
		DspCampaignDto dspCampaignDto = new DspCampaignDto();
		dspCampaignDto.setCampaignName(dspCampaignInputForm.getCampaignName());
		dspCampaignDto.setStartDatetime(dspCampaignInputForm.getStartDatetime());
		dspCampaignDto.setEndDatetime(dspCampaignInputForm.getEndDatetime());
		dspCampaignDto.setStartHour(dspCampaignInputForm.getStartHour());
		dspCampaignDto.setEndHour(dspCampaignInputForm.getEndHour());
		dspCampaignDto.setStartMin(dspCampaignInputForm.getStartMin());
		dspCampaignDto.setEndMin(dspCampaignInputForm.getEndMin());
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

		try {
			dspCampaignDto = dspCampaignService.validate(dspCampaignDto);
		} catch (BusinessException e) {
			result.reject(e.getMessage());
			return createCampaign(dspCampaignInputForm, result);
		}

		session.setAttribute("dspCreativeDto", dspCreativeDto);
		session.setAttribute("dspCampaignDto", dspCampaignDto);

		StringBuffer data = new StringBuffer();
		StringBuffer data2 = new StringBuffer();
		StringBuffer data3 = new StringBuffer();
		StringBuffer data4 = new StringBuffer();
		StringBuffer data5 = new StringBuffer();
		data.append("data:image/jpeg;base64,");
		data.append(imaBase64);
		data2.append("data:image/jpeg;base64,");
		data2.append(imaBase64_2);
		data3.append("data:image/jpeg;base64,");
		data3.append(imaBase64_3);
		data4.append("data:image/jpeg;base64,");
		data4.append(imaBase64_4);
		data5.append("data:image/jpeg;base64,");
		data5.append(imaBase64_5);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("dspCreativeDto", dspCreativeDto);
		modelAndView.addObject("dspCampaignDto", dspCampaignDto);
		modelAndView.addObject("dspTemplateDto", dspTemplateDto);
		modelAndView.addObject("base64Data", data.toString());
		modelAndView.addObject("base64Data2", data2.toString());
		modelAndView.addObject("base64Data3", data3.toString());
		modelAndView.addObject("base64Data4", data4.toString());
		modelAndView.addObject("base64Data5", data5.toString());
		return modelAndView;
	}

	@GetMapping("/completeNewCampaign")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView completeNewCampaign() {

		// クリエイティブ登録
		DspCreativeDto dspCreativeDto = (DspCreativeDto) session.getAttribute("dspCreativeDto");
		DspCreativeDto tempDto = new DspCreativeDto();
		List<Integer> idList = new ArrayList<Integer>();
		if (dspCreativeDto.getBase64Str() != null && dspCreativeDto.getCreativeName() != null) {
			tempDto.setCreativeName(dspCreativeDto.getCreativeName());
			tempDto.setBase64Str(dspCreativeDto.getBase64Str());
			tempDto = dspCreativeService.createCreative(tempDto);
			idList.add(tempDto.getCreativeId());
		}
		if (dspCreativeDto.getBase64Str2() != null && dspCreativeDto.getCreativeName2() != null) {
			tempDto.setCreativeName(dspCreativeDto.getCreativeName2());
			tempDto.setBase64Str(dspCreativeDto.getBase64Str2());
			tempDto = dspCreativeService.createCreative(tempDto);
			idList.add(tempDto.getCreativeId());
		}
		if (dspCreativeDto.getBase64Str3() != null && dspCreativeDto.getCreativeName3() != null) {
			tempDto.setCreativeName(dspCreativeDto.getCreativeName3());
			tempDto.setBase64Str(dspCreativeDto.getBase64Str3());
			tempDto = dspCreativeService.createCreative(tempDto);
			idList.add(tempDto.getCreativeId());
		}
		if (dspCreativeDto.getBase64Str4() != null && dspCreativeDto.getCreativeName4() != null) {
			tempDto.setCreativeName(dspCreativeDto.getCreativeName4());
			tempDto.setBase64Str(dspCreativeDto.getBase64Str4());
			tempDto = dspCreativeService.createCreative(tempDto);
			idList.add(tempDto.getCreativeId());
		}
		if (dspCreativeDto.getBase64Str5() != null && dspCreativeDto.getCreativeName5() != null) {
			tempDto.setCreativeName(dspCreativeDto.getCreativeName5());
			tempDto.setBase64Str(dspCreativeDto.getBase64Str5());
			tempDto = dspCreativeService.createCreative(tempDto);
			idList.add(tempDto.getCreativeId());
		}

		// sessionからdspCampaignDto対象を取得
		DspCampaignDto dspCampaignDto = (DspCampaignDto) session.getAttribute("dspCampaignDto");
		dspCampaignDto.setIdList(idList);

		// 広告キャンペーン作成
		dspCampaignDto = dspCampaignService.createCampaign(dspCampaignDto, null);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("campaign/dsp/createSuccess");

		session.removeAttribute("dspCampaignDto");
		session.removeAttribute("idList");
		session.removeAttribute("selectedDspCreativeDtoList");
		session.removeAttribute("dspCreativeDtoList");
		session.removeAttribute("dspCreativeDto");

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
