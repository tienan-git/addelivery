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
import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.dsp.DspCreativeDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.twitter.TwitterAdsDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.form.DspCampaignCreInputForm;
import jp.acepro.haishinsan.form.IssueInputForm;
import jp.acepro.haishinsan.mapper.IssueMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.IssueService;
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
@RequestMapping("/issue")
public class IssueController {

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

	@Autowired
	IssueService issueService;

	@Autowired
	OperationService operationService;

	@Autowired
	MessageSource msg;

	@GetMapping("/createIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	public ModelAndView createIssue(@ModelAttribute IssueInputForm issueInputForm) {

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
		issueInputForm.setDspCampaignCreInputFormList(dspCampaignCreInputFormList);

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
			issueInputForm.setWebsiteTweetList(websiteTweetList);
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("issue/createIssue");
		mv.addObject("dspSegmentDtoList", dspSegmentDtoList);
		return mv;

	}

	@PostMapping("/completeIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	public ModelAndView completeIssue(@Validated IssueInputForm issueInputForm, BindingResult result)
			throws IOException {

		// ツイート必須チェック
		if (issueInputForm.isTwitterSelected() && CollectionUtils.isEmpty(issueInputForm.getTweetIdList())) {
			result.reject(ErrorCodeConstant.E20005);

			return createIssue(issueInputForm);
		}

		IssueDto issueDto = IssueMapper.INSTANCE.map(issueInputForm);

		List<String> resAdImageList = new ArrayList<String>();
		List<String> imageAdImageList = new ArrayList<String>();

		if (CodeMasterServiceImpl.keywordNameList == null) {
			codeMasterService.getKeywordNameList();
		}

		// 完了画面にGoogle画像を表示するため、画像データを取得
		if (issueInputForm.isGoogleSelected()) {
			// キャンプーン作成用パラメタ設定（画像）
			switch (GoogleAdType.of(issueInputForm.getAdType())) {
			case RESPONSIVE:
				for (MultipartFile imageFile : issueInputForm.getResAdImageFileList()) {
					String base64Str = Base64.getEncoder().encodeToString(imageFile.getBytes());
					StringBuffer data = new StringBuffer();
					data.append("data:image/jpeg;base64,");
					data.append(base64Str);
					resAdImageList.add(data.toString());
				}
				break;
			case IMAGE:
				for (MultipartFile imageFile : issueInputForm.getImageAdImageFileList()) {
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
		if (issueInputForm.isTwitterSelected()) {
			TwitterAdsDto twitterAdsDto = new TwitterAdsDto();
			twitterAdsDto.setTweetIdList(issueDto.getTweetIdList());
			// キャンペーン目的がwebsiteのみ
			List<TwitterTweet> selectedWebsiteTweetList = twitterApiService.searchWebsiteTweetsById(twitterAdsDto);
			issueDto.setWebsiteTweetList(selectedWebsiteTweetList);
		}

		issueService.createIssue(issueDto);
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

		issueDto.setDspCampaignCreInputFormList(dspCampaignCreInputFormList);

		String dspMsg = null;
		String googleMsg = null;
		String facebookMsg = null;
		String twitterMsg = null;
		if (issueDto.getDspErrorCode() != null) {
			dspMsg = "DSP:" + msg.getMessage(issueDto.getDspErrorCode(), null, null);
		}
		if (issueDto.getGoogleErrorCode() != null) {
			googleMsg = "Google:" + msg.getMessage(issueDto.getGoogleErrorCode(), null, null);
		}
		if (issueDto.getFacebookErrorCode() != null) {
			facebookMsg = "Facebook:"
					+ msg.getMessage(issueDto.getFacebookErrorCode(), issueDto.getFacebookParam(), null);
		}
		if (issueDto.getTwitterErrorCode() != null) {
			twitterMsg = "Twitter:"
					+ msg.getMessage(issueDto.getTwitterErrorCode(), new Object[] { issueDto.getTwitterParam() }, null);
		}
		ModelAndView mv = new ModelAndView("issue/completeIssue");
		mv.addObject("issueDto", issueDto);
		mv.addObject("resAdImageList", resAdImageList);
		mv.addObject("imageAdImageList", imageAdImageList);
		mv.addObject("issueInputForm", issueInputForm);
		mv.addObject("dspMsg", dspMsg);
		mv.addObject("googleMsg", googleMsg);
		mv.addObject("facebookMsg", facebookMsg);
		mv.addObject("twitterMsg", twitterMsg);

		// キャンペーン作成成功したらツイートリストをsessionから削除
		session.removeAttribute("websiteTweetList");

		// オペレーションログ記録
		operationService.create(Operation.ISSUE_CREATE.getValue(), String.valueOf(issueDto.getIssueId()));
		return mv;

	}

	@GetMapping("/issueList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_VIEW + "')")
	public ModelAndView issueList() {

		List<IssueDto> issueDtoList = issueService.issueList();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("issue/issueList");
		mv.addObject("issueDtoList", issueDtoList);
		return mv;
	}
	
	//mock_paku
	@GetMapping("/newIssueList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_VIEW + "')")
	public ModelAndView newIssueList() {

//		List<IssueDto> issueDtoList = issueService.issueList();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("issue/newIssueList");
//		mv.addObject("issueDtoList", issueDtoList);
		return mv;
	}

	@PostMapping("/deleteIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	public ModelAndView deleteIssue(@RequestParam String campaignId) {

		facebookService.deleteCampaign(campaignId);
		// 一覧を再表示
		return issueList();

	}

}
