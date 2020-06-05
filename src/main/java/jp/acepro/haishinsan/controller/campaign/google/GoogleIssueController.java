package jp.acepro.haishinsan.controller.campaign.google;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.io.ByteSource;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.db.entity.GoogleCampaignManage;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.dto.google.GoogleIssueDto;
import jp.acepro.haishinsan.dto.google.GoogleTemplateDto;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.GoogleDeviceType;
import jp.acepro.haishinsan.enums.MediaType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.UnitPriceType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.GoogleBannerAdIssueForm;
import jp.acepro.haishinsan.form.GoogleBannerTextAdIssueForm;
import jp.acepro.haishinsan.form.GoogleIssueInputForm;
import jp.acepro.haishinsan.form.GoogleTextAdIssueForm;
import jp.acepro.haishinsan.form.UploadGoogleBannerAdCreateForm;
import jp.acepro.haishinsan.form.UploadGoogleBannerTextAdCreateForm;
import jp.acepro.haishinsan.form.UploadGoogleTextAdCreateForm;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.google.GoogleCampaignService;
import jp.acepro.haishinsan.service.google.GoogleTemplateService;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.ImageUtil;

@Controller
@RequestMapping("/campaign/google")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GoogleIssueController {

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	HttpSession session;

	@Autowired
	GoogleCampaignService googleCampaignService;

	@Autowired
	GoogleTemplateService googleTemplateService;

	@Autowired
	DspSegmentService dspSegmentService;

	@Autowired
	OperationService operationService;

	@Autowired
	ImageUtil imageUtil;

	@GetMapping("/selectAction")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView selectAction() {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("campaign/google/selectAction");

		return modelAndView;
	}

	@GetMapping("/typeSelectCreateNew")
	public ModelAndView typeSelectCreateNew(ModelAndView mv) {

		mv.setViewName("campaign/google/typeSelectCreateNew");
		return mv;
	}

	@GetMapping("/typeSelectUseExisting")
	public ModelAndView typeSelectUseExisting(ModelAndView mv) {

		mv.setViewName("campaign/google/typeSelectUseExisting");
		return mv;
	}

	@GetMapping("/bannerCampaignList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView bannerCampaignList(@ModelAttribute GoogleIssueInputForm googleIssueInputForm) {

		session.setAttribute("adType", GoogleAdType.IMAGE);

		List<GoogleCampaignManage> googleCampaignManageList = googleCampaignService
				.searchGoogleCampaignManageList(GoogleAdType.IMAGE.getValue());

		List<GoogleCampaignDto> googleCampaignDtoList = googleCampaignService.campaignList(googleCampaignManageList);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/bannerCampaignList");
		mv.addObject("googleCampaignDtoList", googleCampaignDtoList);

		return mv;
	}

	@GetMapping("/respCampaignList")
	public ModelAndView respCampaignList(@ModelAttribute GoogleIssueInputForm googleIssueInputForm) {

		session.setAttribute("adType", GoogleAdType.RESPONSIVE);

		List<GoogleCampaignManage> googleCampaignManageList = googleCampaignService
				.searchGoogleCampaignManageList(GoogleAdType.RESPONSIVE.getValue());

		List<GoogleCampaignDto> googleCampaignDtoList = googleCampaignService.campaignList(googleCampaignManageList);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/respCampaignList");
		mv.addObject("googleCampaignDtoList", googleCampaignDtoList);

		return mv;
	}

	@GetMapping("/textCampaignList")
	public ModelAndView textCampaignList(@ModelAttribute GoogleIssueInputForm googleIssueInputForm) {

		session.setAttribute("adType", GoogleAdType.TEXT);

		List<GoogleCampaignManage> googleCampaignManageList = googleCampaignService
				.searchGoogleCampaignManageList(GoogleAdType.TEXT.getValue());

		List<GoogleCampaignDto> googleCampaignDtoList = googleCampaignService.campaignList(googleCampaignManageList);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/textCampaignList");
		mv.addObject("googleCampaignDtoList", googleCampaignDtoList);

		return mv;
	}

	@PostMapping("/createIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView createIssue(@Validated GoogleIssueInputForm googleIssueInputForm, BindingResult result) {

		if (googleIssueInputForm.getIdList() == null || googleIssueInputForm.getIdList().isEmpty()) {
			result.reject("E00020");
			GoogleAdType adType = (GoogleAdType) session.getAttribute("adType");
			switch (adType) {
			case IMAGE:
				return bannerCampaignList(googleIssueInputForm);
			case RESPONSIVE:
				return respCampaignList(googleIssueInputForm);
			case TEXT:
				return textCampaignList(googleIssueInputForm);
			default:
				return bannerCampaignList(googleIssueInputForm);
			}
		}
		if (googleIssueInputForm.getIdList().size() > 1) {
			result.reject("E00021");
			GoogleAdType adType = (GoogleAdType) session.getAttribute("adType");
			switch (adType) {
			case IMAGE:
				return bannerCampaignList(googleIssueInputForm);
			case RESPONSIVE:
				return respCampaignList(googleIssueInputForm);
			case TEXT:
				return textCampaignList(googleIssueInputForm);
			default:
				return bannerCampaignList(googleIssueInputForm);
			}
		}

		// コードマスタを読込
		getGoogleAreaList();

		// テンプレートを読込
		List<GoogleTemplateDto> googleTemplateDtoList = getGoogleTemplateList();

		// -------- 優先度一番高いテンプレートで初期値を設定 --------
		if (googleTemplateDtoList != null && googleTemplateDtoList.size() > 0) {
			GoogleTemplateDto googleTemplateDto = googleTemplateDtoList.get(0);
			googleIssueInputForm.setBudget(googleTemplateDto.getBudget());
			googleIssueInputForm.setCampaignName(googleTemplateDto.getCampaignName());
			List<Long> list = googleTemplateDto.getLocationList();
			if (list != null) {
				googleIssueInputForm.setLocationList(new ArrayList<Long>(list));
			}
	        googleIssueInputForm.setUnitPriceType(googleTemplateDto.getUnitPriceType());
		}

		session.setAttribute("campaignId", googleIssueInputForm.getIdList().get(0));
		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/createIssue");
		return mv;

	}

	@PostMapping("/confirmIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView confirmIssue(@Validated GoogleIssueInputForm googleIssueInputForm, BindingResult result)
			throws IOException {

		ModelAndView mv = new ModelAndView();
		String campaignId = (String) session.getAttribute("campaignId");
		GoogleIssueDto googleIssueDto = googleCampaignService.mapToIssue(googleIssueInputForm);
		googleIssueDto.setCampaignId(Long.valueOf(campaignId));

        try {
        	googleCampaignService.dailyCheck(googleIssueDto);
        } catch (BusinessException e) {
            // 異常時レスポンス
            result.reject(e.getMessage());
            mv.setViewName("campaign/google/createIssue");
            return mv;
        }

		session.setAttribute("googleIssueDto", googleIssueDto);
		mv.setViewName("campaign/google/confirmIssue");
		mv.addObject("googleIssueDto", googleIssueDto);

		return mv;
	}

	@GetMapping("/completeIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView completeIssue() {

		GoogleIssueDto googleIssueDto = (GoogleIssueDto) session.getAttribute("googleIssueDto");

		Issue issue = googleCampaignService.createIssue(googleIssueDto);

		session.removeAttribute("googleIssueDto");
		session.removeAttribute("campaignId");
		session.removeAttribute("adType");
		ModelAndView mv = new ModelAndView("campaign/google/completeIssue");

		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_ISSUE_CREATE.getValue(), String.valueOf(issue.getIssueId()));
		return mv;
	}

	@GetMapping("/bannerCreateNew")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView bannerCreateNew(@ModelAttribute GoogleBannerAdIssueForm googleBannerAdIssueForm) {

		// コードマスタを読込
		getGoogleAreaList();

		// テンプレートを読込
		List<GoogleTemplateDto> googleTemplateDtoList = getGoogleTemplateList();

		// -------- 優先度一番高いテンプレートで初期値を設定 --------
		if (googleTemplateDtoList != null && googleTemplateDtoList.size() > 0) {
			GoogleTemplateDto googleTemplateDto = googleTemplateDtoList.get(0);
			googleBannerAdIssueForm.setBudget(googleTemplateDto.getBudget());
			googleBannerAdIssueForm.setCampaignName(googleTemplateDto.getCampaignName());
			List<Long> list = googleTemplateDto.getLocationList();
			if (list != null) {
				googleBannerAdIssueForm.setLocationList(new ArrayList<Long>(list));
			}
			googleBannerAdIssueForm.setUnitPriceType(googleTemplateDto.getUnitPriceType());
		}

		// 日付によるセグメント情報を取得
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.selectUrlByDateTimeWithNoCheck(LocalDateTime.now());

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/bannerAd/create");
		mv.addObject("dspSegmentDtoList", dspSegmentDtoList);

		session.setAttribute("dspSegmentDtoList", dspSegmentDtoList);
		return mv;
	}

	@PostMapping("/bannerAd/confirm")
	public ModelAndView bannerAdConfirm(@Validated GoogleBannerAdIssueForm googleBannerAdIssueForm, BindingResult result)
			throws IOException {
		GoogleIssueDto googleIssueDto = googleCampaignService.mapToIssue(googleBannerAdIssueForm);

		try {
			if (!googleBannerAdIssueForm.getImageFile01().isEmpty()) {
				googleBannerAdIssueForm.setImageFileName01(googleBannerAdIssueForm.getImageFile01().getOriginalFilename());
				googleBannerAdIssueForm.setImageData01("data:image/jpeg;base64,"
						+ imageUtil.getImageBytes(googleBannerAdIssueForm.getImageFile01(), MediaType.GOOGLEIMG1.getValue()));
				googleBannerAdIssueForm.setImageBytes01(getByteArrayFromStream(googleBannerAdIssueForm.getImageFile01().getInputStream()));
			}
			if (!googleBannerAdIssueForm.getImageFile02().isEmpty()) {
				googleBannerAdIssueForm.setImageFileName02(googleBannerAdIssueForm.getImageFile02().getOriginalFilename());
				googleBannerAdIssueForm.setImageData02("data:image/jpeg;base64,"
						+ imageUtil.getImageBytes(googleBannerAdIssueForm.getImageFile02(), MediaType.GOOGLEIMG2.getValue()));
				googleBannerAdIssueForm.setImageBytes02(getByteArrayFromStream(googleBannerAdIssueForm.getImageFile02().getInputStream()));
			}
			if (!googleBannerAdIssueForm.getImageFile03().isEmpty()) {
				googleBannerAdIssueForm.setImageFileName03(googleBannerAdIssueForm.getImageFile03().getOriginalFilename());
				googleBannerAdIssueForm.setImageData03("data:image/jpeg;base64,"
						+ imageUtil.getImageBytes(googleBannerAdIssueForm.getImageFile03(), MediaType.GOOGLEIMG3.getValue()));
				googleBannerAdIssueForm.setImageBytes03(getByteArrayFromStream(googleBannerAdIssueForm.getImageFile03().getInputStream()));
			}
			if (!googleBannerAdIssueForm.getImageFile04().isEmpty()) {
				googleBannerAdIssueForm.setImageFileName04(googleBannerAdIssueForm.getImageFile04().getOriginalFilename());
				googleBannerAdIssueForm.setImageData04("data:image/jpeg;base64,"
						+ imageUtil.getImageBytes(googleBannerAdIssueForm.getImageFile04(), MediaType.GOOGLEIMG4.getValue()));
				googleBannerAdIssueForm.setImageBytes04(getByteArrayFromStream(googleBannerAdIssueForm.getImageFile04().getInputStream()));
			}
			if (googleBannerAdIssueForm.getImageFile01().isEmpty() && googleBannerAdIssueForm.getImageFile02().isEmpty() && googleBannerAdIssueForm.getImageFile03().isEmpty()
					&& googleBannerAdIssueForm.getImageFile04().isEmpty()) {
				// バナーを少なくとも１枚アップロードしてください。
				throw new BusinessException(ErrorCodeConstant.E70009);
			}
			
			googleCampaignService.dailyCheck(googleIssueDto);
		} catch (BusinessException e) {
			// 異常時レスポンスを作成
			result.reject(e.getMessage(), e.getParams(), null);
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("campaign/google/bannerAd/create");
			modelAndView.addObject("googleBannerAdIssueForm", googleBannerAdIssueForm);
			return modelAndView;
		}
		
		List<DspSegmentListDto> dspSegmentDtoList = (ArrayList<DspSegmentListDto>) session.getAttribute("dspSegmentDtoList");
		for (DspSegmentListDto dspSegmentListDto : dspSegmentDtoList) {
			if (dspSegmentListDto.getSegmentId().equals(googleBannerAdIssueForm.getSegmentId())) {
				googleBannerAdIssueForm.setUrl(dspSegmentListDto.getUrl());
				break;
			}
		}

		session.setAttribute("googleIssueDto", googleIssueDto);
		session.setAttribute("googleBannerAdIssueForm", googleBannerAdIssueForm);
		session.removeAttribute("dspSegmentDtoList");

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/bannerAd/confirm");
		mv.addObject("googleIssueDto", googleIssueDto);
		mv.addObject("googleBannerAdIssueForm", googleBannerAdIssueForm);
		return mv;
	}

	@PostMapping("/bannerAd/complete")
	public ModelAndView bannerAdComplete() throws IOException {
		GoogleBannerAdIssueForm form = (GoogleBannerAdIssueForm) session.getAttribute("googleBannerAdIssueForm");
		GoogleIssueDto googleIssueDto = (GoogleIssueDto) session.getAttribute("googleIssueDto");

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/bannerAd/complete");
		session.removeAttribute("googleBannerAdIssueForm");
		session.removeAttribute("googleIssueDto");

		GoogleCampaignDto googleCampaignDto = new GoogleCampaignDto();
		// 広告種別（バナー）
		googleCampaignDto.setAdType(GoogleAdType.IMAGE.getValue());
		// バナー（画面入力）
		googleCampaignDto.setImageAdImageFileNameList(new ArrayList<String>());
		googleCampaignDto.setImageAdImageBytesList(new ArrayList<byte[]>());
		if (form.getImageBytes01() != null) {
			googleCampaignDto.getImageAdImageFileNameList().add(form.getImageFileName01());
			googleCampaignDto.getImageAdImageBytesList().add(form.getImageBytes01());
		}
		if (form.getImageBytes02() != null) {
			googleCampaignDto.getImageAdImageFileNameList().add(form.getImageFileName02());
			googleCampaignDto.getImageAdImageBytesList().add(form.getImageBytes02());
		}
		if (form.getImageBytes03() != null) {
			googleCampaignDto.getImageAdImageFileNameList().add(form.getImageFileName03());
			googleCampaignDto.getImageAdImageBytesList().add(form.getImageBytes03());
		}
		if (form.getImageBytes04() != null) {
			googleCampaignDto.getImageAdImageFileNameList().add(form.getImageFileName04());
			googleCampaignDto.getImageAdImageBytesList().add(form.getImageBytes04());
		}
		// 地域単価タイプ（クリック重視）
		googleCampaignDto.setUnitPriceType(UnitPriceType.CLICK.getValue());
		// 配信開始日（本日）
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = localDate.format(formatter);
		googleCampaignDto.setStartDate(formattedString);
		// 配信終了日（未指定）
		googleCampaignDto.setEndDate("2037-12-30");
		// 配信地域（日本）
		googleCampaignDto.setLocationList(new ArrayList<Long>(Arrays.asList(2392L)));
		// 日次予算（１円）
		googleCampaignDto.setBudget(1L);
		// ディバイスタイプ（パソコン）
		googleCampaignDto.setDeviceType(GoogleDeviceType.PC.getValue());

		// 最終ページURL（セグメントURL）
		googleCampaignDto.setImageAdFinalPageUrl(form.getUrl());

		// 広告名
		googleCampaignDto.setCampaignName(form.getImgAdName());

		// 広告作成
		Long campaignId = googleCampaignService.createCampaign(googleCampaignDto, null);

		// 案件作成
		googleIssueDto.setCampaignId(campaignId);
		Issue issue = googleCampaignService.createIssue(googleIssueDto);

		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_ISSUE_CREATE.getValue(), String.valueOf(issue.getIssueId()));
		return mv;
	}

	@GetMapping("/respCreateNew")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView respCreateNew(@ModelAttribute GoogleBannerTextAdIssueForm googleBannerTextAdIssueForm) {

		// コードマスタを読込
		getGoogleAreaList();

		// テンプレートを読込
		List<GoogleTemplateDto> googleTemplateDtoList = getGoogleTemplateList();

		// -------- 優先度一番高いテンプレートで初期値を設定 --------
		if (googleTemplateDtoList != null && googleTemplateDtoList.size() > 0) {
			GoogleTemplateDto googleTemplateDto = googleTemplateDtoList.get(0);
			googleBannerTextAdIssueForm.setBudget(googleTemplateDto.getBudget());
			googleBannerTextAdIssueForm.setCampaignName(googleTemplateDto.getCampaignName());
			List<Long> list = googleTemplateDto.getLocationList();
			if (list != null) {
				googleBannerTextAdIssueForm.setLocationList(new ArrayList<Long>(list));
			}
			googleBannerTextAdIssueForm.setUnitPriceType(googleTemplateDto.getUnitPriceType());
		}

		// 日付によるセグメント情報を取得
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.selectUrlByDateTimeWithNoCheck(LocalDateTime.now());

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/bannerTextAd/create");
		mv.addObject("dspSegmentDtoList", dspSegmentDtoList);

		session.setAttribute("dspSegmentDtoList", dspSegmentDtoList);
		return mv;
	}

	@PostMapping("/bannerTextAd/confirm")
	public ModelAndView bannerTextAdConfirm(@Validated GoogleBannerTextAdIssueForm googleBannerTextAdIssueForm, BindingResult result) throws IOException {
		GoogleIssueDto googleIssueDto = googleCampaignService.mapToIssue(googleBannerTextAdIssueForm);
		
		try {
			if (!googleBannerTextAdIssueForm.getImageFile01().isEmpty()) {
				googleBannerTextAdIssueForm.setImageFileName01(googleBannerTextAdIssueForm.getImageFile01().getOriginalFilename());
				googleBannerTextAdIssueForm.setImageData01("data:image/jpeg;base64,"
						+ imageUtil.getImageBytes(googleBannerTextAdIssueForm.getImageFile01(), MediaType.GOOGLERES1.getValue()));
				googleBannerTextAdIssueForm.setImageBytes01(getByteArrayFromStream(googleBannerTextAdIssueForm.getImageFile01().getInputStream()));
			}
			if (!googleBannerTextAdIssueForm.getImageFile02().isEmpty()) {
				googleBannerTextAdIssueForm.setImageFileName02(googleBannerTextAdIssueForm.getImageFile02().getOriginalFilename());
				googleBannerTextAdIssueForm.setImageData02("data:image/jpeg;base64,"
						+ imageUtil.getImageBytes(googleBannerTextAdIssueForm.getImageFile02(), MediaType.GOOGLERES2.getValue()));
				googleBannerTextAdIssueForm.setImageBytes02(getByteArrayFromStream(googleBannerTextAdIssueForm.getImageFile02().getInputStream()));
			}
			
			googleCampaignService.dailyCheck(googleIssueDto);
		} catch (BusinessException e) {
			// 異常時レスポンスを作成
			result.reject(e.getMessage(), e.getParams(), null);
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("campaign/google/bannerTextAd/create");
			modelAndView.addObject("googleBannerTextAdIssueForm", googleBannerTextAdIssueForm);
			return modelAndView;
		}

		List<DspSegmentListDto> dspSegmentDtoList = (ArrayList<DspSegmentListDto>) session.getAttribute("dspSegmentDtoList");
		for (DspSegmentListDto dspSegmentListDto : dspSegmentDtoList) {
			if (dspSegmentListDto.getSegmentId().equals(googleBannerTextAdIssueForm.getSegmentId())) {
				googleBannerTextAdIssueForm.setUrl(dspSegmentListDto.getUrl());
				break;
			}
		}

		session.setAttribute("googleIssueDto", googleIssueDto);
		session.setAttribute("googleBannerTextAdIssueForm", googleBannerTextAdIssueForm);
		session.removeAttribute("dspSegmentDtoList");

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/bannerTextAd/confirm");
		mv.addObject("googleIssueDto", googleIssueDto);
		mv.addObject("googleBannerTextAdIssueForm", googleBannerTextAdIssueForm);
		return mv;
	}

	@PostMapping("/bannerTextAd/complete")
	public ModelAndView bannerTextAdComplete() {
		GoogleBannerTextAdIssueForm form = (GoogleBannerTextAdIssueForm) session
				.getAttribute("googleBannerTextAdIssueForm");
		GoogleIssueDto googleIssueDto = (GoogleIssueDto) session.getAttribute("googleIssueDto");

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/bannerTextAd/complete");
		session.removeAttribute("googleBannerTextAdIssueForm");
		session.removeAttribute("googleIssueDto");

		GoogleCampaignDto googleCampaignDto = new GoogleCampaignDto();
		// 広告種別（バナー＋文字）
		googleCampaignDto.setAdType(GoogleAdType.RESPONSIVE.getValue());
		// 広告名（画面入力）
		googleCampaignDto.setCampaignName(form.getResAdName());
		// 広告短い見出し（画面入力）
		googleCampaignDto.setResAdShortTitle(form.getResAdShortTitle());
		// 広告説明文（画面入力）
		googleCampaignDto.setResAdDescription(form.getResAdDescription());
		// バナー（画面入力）
		googleCampaignDto.setResAdImageFileNameList(new ArrayList<String>());
		googleCampaignDto.setResAdImageBytesList(new ArrayList<byte[]>());
		if (form.getImageBytes01() != null) {
			googleCampaignDto.getResAdImageFileNameList().add(form.getImageFileName01());
			googleCampaignDto.getResAdImageBytesList().add(form.getImageBytes01());
		}
		if (form.getImageBytes02() != null) {
			googleCampaignDto.getResAdImageFileNameList().add(form.getImageFileName02());
			googleCampaignDto.getResAdImageBytesList().add(form.getImageBytes02());
		}
		// 地域単価タイプ（クリック重視）
		googleCampaignDto.setUnitPriceType(UnitPriceType.CLICK.getValue());
		// 配信開始日（本日）
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = localDate.format(formatter);
		googleCampaignDto.setStartDate(formattedString);
		// 配信終了日（未指定）
		googleCampaignDto.setEndDate("2037-12-30");
		// 配信地域（日本）
		googleCampaignDto.setLocationList(new ArrayList<Long>(Arrays.asList(2392L)));
		// 日次予算（１円）
		googleCampaignDto.setBudget(1L);
		// ディバイスタイプ（パソコン）
		googleCampaignDto.setDeviceType(GoogleDeviceType.PC.getValue());

		// 最終ページURL（セグメントURL）
		googleCampaignDto.setResAdFinalPageUrl(form.getUrl());

		// 広告名
		googleCampaignDto.setCampaignName(form.getResAdName());

		// 広告作成
		Long campaignId = googleCampaignService.createCampaign(googleCampaignDto, null);

		// 案件作成
		googleIssueDto.setCampaignId(campaignId);
		Issue issue = googleCampaignService.createIssue(googleIssueDto);

		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_ISSUE_CREATE.getValue(), String.valueOf(issue.getIssueId()));
		return mv;
	}

	@GetMapping("/textCreateNew")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView textCreateNew(@ModelAttribute GoogleTextAdIssueForm googleTextAdIssueForm) {

		// コードマスタを読込
		getGoogleAreaList();

		// テンプレートを読込
		List<GoogleTemplateDto> googleTemplateDtoList = getGoogleTemplateList();

		// -------- 優先度一番高いテンプレートで初期値を設定 --------
		if (googleTemplateDtoList != null && googleTemplateDtoList.size() > 0) {
			GoogleTemplateDto googleTemplateDto = googleTemplateDtoList.get(0);
			googleTextAdIssueForm.setBudget(googleTemplateDto.getBudget());
			googleTextAdIssueForm.setCampaignName(googleTemplateDto.getCampaignName());
			List<Long> list = googleTemplateDto.getLocationList();
			if (list != null) {
				googleTextAdIssueForm.setLocationList(new ArrayList<Long>(list));
			}
			googleTextAdIssueForm.setUnitPriceType(googleTemplateDto.getUnitPriceType());
		}

		// 日付によるセグメント情報を取得
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.selectUrlByDateTimeWithNoCheck(LocalDateTime.now());

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/textAd/create");
		mv.addObject("dspSegmentDtoList", dspSegmentDtoList);

		session.setAttribute("dspSegmentDtoList", dspSegmentDtoList);
		return mv;
	}
	
	@PostMapping("/textAd/confirm")
	public ModelAndView textAdConfirm(@Validated GoogleTextAdIssueForm googleTextAdIssueForm, BindingResult result) {
		GoogleIssueDto googleIssueDto = googleCampaignService.mapToIssue(googleTextAdIssueForm);

        try {
        	googleCampaignService.dailyCheck(googleIssueDto);
        } catch (BusinessException e) {
			// 異常時レスポンスを作成
			result.reject(e.getMessage(), e.getParams(), null);
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("campaign/google/textAd/create");
			modelAndView.addObject("googleTextAdIssueForm", googleTextAdIssueForm);
			return modelAndView;
        }

		List<DspSegmentListDto> dspSegmentDtoList = (ArrayList<DspSegmentListDto>) session.getAttribute("dspSegmentDtoList");
		for (DspSegmentListDto dspSegmentListDto : dspSegmentDtoList) {
			if (dspSegmentListDto.getSegmentId().equals(googleTextAdIssueForm.getSegmentId())) {
				googleTextAdIssueForm.setUrl(dspSegmentListDto.getUrl());
				break;
			}
		}

		session.setAttribute("googleIssueDto", googleIssueDto);
		session.setAttribute("googleTextAdIssueForm", googleTextAdIssueForm);
		session.removeAttribute("dspSegmentDtoList");

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/textAd/confirm");
		mv.addObject("googleIssueDto", googleIssueDto);
		mv.addObject("googleTextAdIssueForm", googleTextAdIssueForm);
		return mv;
	}

	@PostMapping("/textAd/complete")
	public ModelAndView textAdComplete() {
		GoogleTextAdIssueForm form = (GoogleTextAdIssueForm) session.getAttribute("googleTextAdIssueForm");
		GoogleIssueDto googleIssueDto = (GoogleIssueDto) session.getAttribute("googleIssueDto");

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/textAd/complete");
		session.removeAttribute("googleTextAdIssueForm");
		session.removeAttribute("googleIssueDto");

		GoogleCampaignDto googleCampaignDto = new GoogleCampaignDto();
		// 広告種別（バナー＋文字）
		googleCampaignDto.setAdType(GoogleAdType.TEXT.getValue());
		// 広告名（画面入力）
		googleCampaignDto.setCampaignName(form.getTextAdName());
		// 広告見出し１（画面入力）
		googleCampaignDto.setTextAdTitle1(form.getTextAdTitle1());
		// 広告見出し２（画面入力）
		googleCampaignDto.setTextAdTitle2(form.getTextAdTitle2());
		// 広告説明文（画面入力）
		googleCampaignDto.setTextAdDescription(form.getTextAdDescription());
		// 配信開始日（本日）
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = localDate.format(formatter);
		googleCampaignDto.setStartDate(formattedString);
		// 配信終了日（未指定）
		googleCampaignDto.setEndDate("2037-12-30");
		// 配信地域（日本）
		googleCampaignDto.setLocationList(new ArrayList<Long>(Arrays.asList(2392L)));
		// 日次予算（１円）
		googleCampaignDto.setBudget(1L);
		// ディバイスタイプ（パソコン）
		googleCampaignDto.setDeviceType(GoogleDeviceType.PC.getValue());

		// 最終ページURL（セグメントURL）
		googleCampaignDto.setTextAdFinalPageUrl(form.getUrl());

		// 広告名
		googleCampaignDto.setCampaignName(form.getTextAdName());

		// 広告作成
		Long campaignId = googleCampaignService.createCampaign(googleCampaignDto, null);

		// 案件作成
		googleIssueDto.setCampaignId(campaignId);
		Issue issue = googleCampaignService.createIssue(googleIssueDto);

		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_ISSUE_CREATE.getValue(), String.valueOf(issue.getIssueId()));
		return mv;
	}

	
	
	private void getGoogleAreaList() {
		if (CodeMasterServiceImpl.googleAreaNameList == null) {
			codeMasterService.getGoogleAreaList();
		}
	}

	private List<GoogleTemplateDto> getGoogleTemplateList() {
		List<GoogleTemplateDto> googleTemplateDtoList = googleTemplateService
				.getTemplateList(ContextUtil.getCurrentShop().getShopId());
		return googleTemplateDtoList;
	}

	private byte[] getByteArrayFromStream(final InputStream inputStream) throws IOException {
		return new ByteSource() {
			@Override
			public InputStream openStream() {
				return inputStream;
			}
		}.read();
	}

	private void getKeywordNameList() {
		if (CodeMasterServiceImpl.keywordNameList == null) {
			codeMasterService.getKeywordNameList();
		}
	}
}
