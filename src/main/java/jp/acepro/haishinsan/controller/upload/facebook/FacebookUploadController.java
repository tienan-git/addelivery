package jp.acepro.haishinsan.controller.upload.facebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dto.CreativeDto;
import jp.acepro.haishinsan.dto.NonTwitterAdDto;
import jp.acepro.haishinsan.dto.TwitterAdDto;
import jp.acepro.haishinsan.dto.CreativeDto;
import jp.acepro.haishinsan.dto.dsp.DspCreativeDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.facebook.FbTemplateDto;
import jp.acepro.haishinsan.dto.twitter.TwitterAdsDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.enums.CreativeType;
import jp.acepro.haishinsan.enums.FacebookArrangePlace;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.form.CreativeInputForm;
import jp.acepro.haishinsan.form.DspCampaignCreInputForm;
import jp.acepro.haishinsan.form.FbCampaignInputForm;
import jp.acepro.haishinsan.form.CreativeInputForm;
import jp.acepro.haishinsan.mapper.CreativeMapper;
import jp.acepro.haishinsan.mapper.CreativeMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;
import jp.acepro.haishinsan.service.dsp.DspCreativeService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.google.GoogleCampaignService;
import jp.acepro.haishinsan.service.twitter.TwitterApiService;
import jp.acepro.haishinsan.util.ContextUtil;

@Controller
@RequestMapping("/upload")
public class FacebookUploadController {

	@Autowired
	HttpSession session;

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	FacebookService facebookService;

	@Autowired
	DspApiService dspApiService;

	@Autowired
	DspCreativeService dspCreativeService;

	@Autowired
	DspSegmentService dspSegmentService;

	@Autowired
	DspCampaignService dspCampaignService;

	@Autowired
	GoogleCampaignService googleCampaignService;

	@Autowired
	TwitterApiService twitterApiService;

	// @Autowired
	// CreativeService creativeService;

	@Autowired
	OperationService operationService;

	@Autowired
	MessageSource msg;



	@GetMapping("/createFacebookCreative")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_CAMPAIGN_MANAGE + "')")
	public ModelAndView createFacebookCreative(@ModelAttribute FbCampaignInputForm fbCampaignInputForm) {

		// テンプレート一覧を取得
		List<FbTemplateDto> fbTemplateDtoList = facebookService.searchList();
		// コードマスタをメモリへロード
//		getFacebookAreaList();
		// ＤＳＰＵＲＬを読込
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();

		// -------- 優先度一番高いテンプレートで初期値を設定 --------
		if (fbTemplateDtoList != null && fbTemplateDtoList.size() > 0) {
			fbCampaignInputForm.setLocationList(fbTemplateDtoList.get(0).getLocationList());
			fbCampaignInputForm.setTemplateId(fbTemplateDtoList.get(0).getTemplateId());
			fbCampaignInputForm.setUnitPriceType(fbTemplateDtoList.get(0).getUnitPriceType());
		}
		// 配置場所の初期値を両方に設定
		fbCampaignInputForm.setArrangePlace(FacebookArrangePlace.BOTH.getValue());

		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/createCampaign");
		mv.addObject("fbTemplateDtoList", fbTemplateDtoList);
		mv.addObject("dspSegmentDtoList", dspSegmentDtoList);
		return mv;

	}
}
