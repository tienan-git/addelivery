package jp.acepro.haishinsan.controller.campaign.google;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

import jp.acepro.haishinsan.db.entity.GoogleCampaignManage;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.dto.google.GoogleIssueDto;
import jp.acepro.haishinsan.dto.google.GoogleTemplateDto;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.GoogleIssueInputForm;
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

	@GetMapping("/adTypeSelect")
	public ModelAndView adTypeSelect(ModelAndView mv) {

		mv.setViewName("campaign/google/typeSelect");
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
//	        googleIssueInputForm.setResAdDescription( googleTemplateDto.getResAdDescription() );
//	        googleIssueInputForm.setResAdShortTitle( googleTemplateDto.getResAdShortTitle() );
//	        googleIssueInputForm.setTextAdDescription( googleTemplateDto.getTextAdDescription() );
//	        googleIssueInputForm.setTextAdTitle1( googleTemplateDto.getTextAdTitle1() );
//	        googleIssueInputForm.setTextAdTitle2( googleTemplateDto.getTextAdTitle2() );
//	        googleIssueInputForm.setUnitPriceType( googleTemplateDto.getUnitPriceType() );
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
