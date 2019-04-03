package jp.acepro.haishinsan.controller;

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
import jp.acepro.haishinsan.dto.CreativeDto;
import jp.acepro.haishinsan.dto.dsp.DspCreativeDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.twitter.TwitterAdsDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.form.CreativeInputForm;
import jp.acepro.haishinsan.form.DspCampaignCreInputForm;
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
@RequestMapping("/creative")
public class CreativeController {

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

	//@Autowired
	//CreativeService creativeService;

	@Autowired
	OperationService operationService;

	@Autowired
	MessageSource msg;

	@GetMapping("/createCreative")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	public ModelAndView createCreative(@ModelAttribute CreativeInputForm creativeInputForm) {

		// 作成したCreativeを取得
		List<DspCreativeDto> dspCreativeDtoList = dspCreativeService.creativeListFromDb();

		// dspCampaignCreInputFormList作成して、UIに添付
		List<DspCampaignCreInputForm> dspCampaignCreInputFormList = new ArrayList<DspCampaignCreInputForm>();
		for (DspCreativeDto dspCreativeDto : dspCreativeDtoList) {
			DspCampaignCreInputForm dspCampaignCreInputForm = new DspCampaignCreInputForm();
			dspCampaignCreInputForm.setCreativeId(dspCreativeDto.getCreativeId());
			dspCampaignCreInputForm.setCreativeName(dspCreativeDto.getCreativeName());
			dspCampaignCreInputFormList.add(dspCampaignCreInputForm);
		}
		creativeInputForm.setDspCampaignCreInputFormList(dspCampaignCreInputFormList);

		// 作成したSegmentを取得
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();

		if (!StringUtils.isEmpty(ContextUtil.getCurrentShop().getTwitterAccountId())) {

			// WebSite & Follower TweetsListを取得
			List<TwitterTweet> websiteTweetList = twitterApiService.searchWebsiteTweets();
			List<TwitterTweet> followersTweetList = twitterApiService.searchFollowersTweets();
			session.setAttribute("websiteTweetList", websiteTweetList);
			// APIから取得したTweetsListをDBに保存
			twitterApiService.saveTweetList(websiteTweetList, followersTweetList);
			// -------- WebSite 一覧表示 --------
			creativeInputForm.setWebsiteTweetList(websiteTweetList);
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("creative/createCreative");
		mv.addObject("dspSegmentDtoList", dspSegmentDtoList);
		return mv;

	}

	@PostMapping("/completeCreative")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	public ModelAndView completeCreative(@Validated CreativeInputForm creativeInputForm, BindingResult result)
			throws IOException {

		// ツイート必須チェック
		if (creativeInputForm.isTwitterSelected() && CollectionUtils.isEmpty(creativeInputForm.getTweetIdList())) {
			result.reject(ErrorCodeConstant.E20005);

			return createCreative(creativeInputForm);
		}

		CreativeDto creativeDto = CreativeMapper.INSTANCE.map(creativeInputForm);

		List<String> resAdImageList = new ArrayList<String>();
		List<String> imageAdImageList = new ArrayList<String>();

		if (CodeMasterServiceImpl.keywordNameList == null) {
			codeMasterService.getKeywordNameList();
		}

		// 完了画面にGoogle画像を表示するため、画像データを取得
		if (creativeInputForm.isGoogleSelected()) {
			// キャンプーン作成用パラメタ設定（画像）
			switch (GoogleAdType.of(creativeInputForm.getAdType())) {
			case RESPONSIVE:
				for (MultipartFile imageFile : creativeInputForm.getResAdImageFileList()) {
					String base64Str = Base64.getEncoder().encodeToString(imageFile.getBytes());
					StringBuffer data = new StringBuffer();
					data.append("data:image/jpeg;base64,");
					data.append(base64Str);
					resAdImageList.add(data.toString());
				}
				break;
			case IMAGE:
				for (MultipartFile imageFile : creativeInputForm.getImageAdImageFileList()) {
					String base64Str = Base64.getEncoder().encodeToString(imageFile.getBytes());
					StringBuffer data = new StringBuffer();
					data.append("data:image/jpeg;base64,");
					data.append(base64Str);
					imageAdImageList.add(data.toString());
				}
				break;
			case TEXT:
				break;
			}
		}
		// 完了画面にTwitterリストを表示するため、セッションからリストを取得
		if (creativeInputForm.isTwitterSelected()) {
			TwitterAdsDto twitterAdsDto = new TwitterAdsDto();
			twitterAdsDto.setTweetIdList(creativeDto.getTweetIdList());
			// キャンペーン目的がwebsiteのみ
			List<TwitterTweet> selectedWebsiteTweetList = twitterApiService.searchWebsiteTweetsById(twitterAdsDto);
			creativeDto.setWebsiteTweetList(selectedWebsiteTweetList);
		}

		//creativeService.createCreative(creativeDto);
		// 作成したCreativeを取得
		List<DspCreativeDto> dspCreativeDtoList = dspCreativeService.creativeListFromDb();

		// dspCampaignCreInputFormList作成して、UIに添付
		List<DspCampaignCreInputForm> dspCampaignCreInputFormList = new ArrayList<DspCampaignCreInputForm>();
		for (DspCreativeDto dspCreativeDto : dspCreativeDtoList) {
			DspCampaignCreInputForm dspCampaignCreInputForm = new DspCampaignCreInputForm();
			dspCampaignCreInputForm.setCreativeId(dspCreativeDto.getCreativeId());
			dspCampaignCreInputForm.setCreativeName(dspCreativeDto.getCreativeName());
			dspCampaignCreInputFormList.add(dspCampaignCreInputForm);
		}

		creativeDto.setDspCampaignCreInputFormList(dspCampaignCreInputFormList);

		String dspMsg = null;
		String googleMsg = null;
		String facebookMsg = null;
		String twitterMsg = null;
		if (creativeDto.getDspErrorCode() != null) {
			dspMsg = "DSP:" + msg.getMessage(creativeDto.getDspErrorCode(), null, null);
		}
		if (creativeDto.getGoogleErrorCode() != null) {
			googleMsg = "Google:" + msg.getMessage(creativeDto.getGoogleErrorCode(), null, null);
		}
		if (creativeDto.getFacebookErrorCode() != null) {
			facebookMsg = "Facebook:"
					+ msg.getMessage(creativeDto.getFacebookErrorCode(), creativeDto.getFacebookParam(), null);
		}
		if (creativeDto.getTwitterErrorCode() != null) {
			twitterMsg = "Twitter:"
					+ msg.getMessage(creativeDto.getTwitterErrorCode(), new Object[] { creativeDto.getTwitterParam() }, null);
		}
		ModelAndView mv = new ModelAndView("creative/completeCreative");
		mv.addObject("creativeDto", creativeDto);
		mv.addObject("resAdImageList", resAdImageList);
		mv.addObject("imageAdImageList", imageAdImageList);
		mv.addObject("creativeInputForm", creativeInputForm);
		mv.addObject("dspMsg", dspMsg);
		mv.addObject("googleMsg", googleMsg);
		mv.addObject("facebookMsg", facebookMsg);
		mv.addObject("twitterMsg", twitterMsg);

		// キャンペーン作成成功したらツイートリストをsessionから削除
		session.removeAttribute("websiteTweetList");

		// オペレーションログ記録
		//operationService.create(Operation.ISSUE_CREATE.getValue(), String.valueOf(creativeDto.getCreativeId()));
		return mv;

	}

	@GetMapping("/creativeList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_VIEW + "')")
	public ModelAndView creativeList() {

		//List<CreativeDto> creativeDtoList = creativeService.creativeList();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("creative/creativeList");
		//mv.addObject("creativeDtoList", creativeDtoList);
		return mv;
	}
	
	//mock_paku
	@GetMapping("/newCreativeList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_VIEW + "')")
	public ModelAndView newCreativeList() {

//		List<CreativeDto> creativeDtoList = creativeService.creativeList();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("creative/newCreativeList");
//		mv.addObject("creativeDtoList", creativeDtoList);
		return mv;
	}

	@PostMapping("/deleteCreative")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	public ModelAndView deleteCreative(@RequestParam String campaignId) {

		facebookService.deleteCampaign(campaignId);
		// 一覧を再表示
		return creativeList();

	}

}
