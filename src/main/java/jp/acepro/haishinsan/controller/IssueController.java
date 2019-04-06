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
import jp.acepro.haishinsan.dto.NonTwitterAdDto;
import jp.acepro.haishinsan.dto.TwitterAdDto;
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

		// Dsp Google Facebook
		List<NonTwitterAdDto> nonTwitterAdDtoList = new ArrayList<NonTwitterAdDto>();

		// Dsp Ad
		NonTwitterAdDto nonTwitterAdDtoD1 = new NonTwitterAdDto();
		nonTwitterAdDtoD1.setAdImageName("image1.jpg\r\nimage2.jpg");
		nonTwitterAdDtoD1.setAdImageSize("100x100\r\n200x200");
		nonTwitterAdDtoD1.getAdImageUrlList()
				.add("https://www.fout.co.jp/images/freakout/product/product_freakout.png");
		nonTwitterAdDtoD1.getAdImageUrlList().add("https://upload.wikimedia.org/wikipedia/commons/7/71/Freak_Out.jpg");
		nonTwitterAdDtoD1.setAdText("");
		nonTwitterAdDtoD1.setAdReviewStatus("承認済み");
		nonTwitterAdDtoD1.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoD1.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoD1.setAdIssue("案件一覧へ");
		nonTwitterAdDtoD1.setAdIcon("fa fa-envira");
		nonTwitterAdDtoList.add(nonTwitterAdDtoD1);

		// Google Image Ad
		NonTwitterAdDto nonTwitterAdDtoG2 = new NonTwitterAdDto();
		nonTwitterAdDtoG2.setAdImageName("image1.jpg\r\nimage2.jpg");
		nonTwitterAdDtoG2.setAdImageSize("100x100\r\n200x200");
		nonTwitterAdDtoG2.getAdImageUrlList()
				.add("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");
		nonTwitterAdDtoG2.getAdImageUrlList()
				.add("https://www.gstatic.com/android/market_images/web/play_prism_hlock_2x.png");
		nonTwitterAdDtoG2.setAdText("");
		nonTwitterAdDtoG2.setAdReviewStatus("承認済み");
		nonTwitterAdDtoG2.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoG2.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoG2.setAdIssue("案件一覧へ");
		nonTwitterAdDtoG2.setAdIcon("fa fa-google");
		nonTwitterAdDtoList.add(nonTwitterAdDtoG2);

		// Facebook Ad
		NonTwitterAdDto nonTwitterAdDtoF1 = new NonTwitterAdDto();
		nonTwitterAdDtoF1.setAdImageName("image1.jpg");
		nonTwitterAdDtoF1.setAdImageSize("500x500");
		nonTwitterAdDtoF1.getAdImageUrlList()
				.add("https://cdn.pixabay.com/photo/2017/10/04/11/58/facebook-2815970_960_720.jpg");
		nonTwitterAdDtoF1.setAdText("説明文");
		nonTwitterAdDtoF1.setAdReviewStatus("承認済み");
		nonTwitterAdDtoF1.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoF1.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoF1.setAdIssue("案件一覧へ");
		nonTwitterAdDtoF1.setAdIcon("fa fa-facebook");
		nonTwitterAdDtoList.add(nonTwitterAdDtoF1);

		// Twitter
		List<TwitterAdDto> twitterAdDtoList = new ArrayList<TwitterAdDto>();

		// Twiiter website
		TwitterAdDto twitterAdDto1 = new TwitterAdDto();
		twitterAdDto1.setAdText("-----------------------------------------------------↓\r\n"
				+ "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 02月26日\r\n" + "こんばんわ！MAX BULLETです！！💥💥\r\n"
				+ "久々の更新となります😆\r\n" + "\r\n" + "MAX BULLETが移転予定の秋葉原のビルは絶賛工事中です！\r\n" + "OPENまで今しばらくお待ちください！！\r\n"
				+ "\r\n" + "秋葉原のオープンまで待てないよ！！という方にはMA… https://twitter.com/i/web/status/1100338392150274048\"\r\n"
				+ "\r\n" + "-----------------------------------------------------↓\r\n"
				+ "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 03月07日\r\n" + "こんばんわ🤗\r\n" + "春がきたと思ったら今日は寒いですね🌬❄️\r\n"
				+ ".\r\n" + "当店では様々なシューティングゲームをご用意しております🔫\r\n" + "どのゲームも盛り上がること間違いなし😎👌\r\n"
				+ "ご来店お待ちしております💁🎶… https://www.instagram.com/p/BgBAy9jlpSF/\r\n" + "\r\n"
				+ "-----------------------------------------------------↓\r\n"
				+ "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 10月04日\r\n" + ".\r\n" + "こんばんわ😸✨\r\n" + ".\r\n"
				+ "あっという間に10月に突入してしまいましたね！！\r\n" + "10月は3連休やハロウィンなど楽しみがいっぱいありますね😍💘\r\n" + "みなさん予定はもうお決まりですか🦄？？\r\n"
				+ "是非マックスバレットに遊びに来てください💁🏼💓\r\n"
				+ ".... https://www.facebook.com/MAXBULLET.NSB/videos/107717326804617/\r\n" + "");
		twitterAdDtoList.add(twitterAdDto1);

		// 作成したSegmentを取得
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("issue/createIssue");
		mv.addObject("nonTwitterAdDtoList", nonTwitterAdDtoList);
		mv.addObject("twitterAdDtoList", twitterAdDtoList);
		mv.addObject("dspSegmentDtoList", dspSegmentDtoList);
		return mv;
	}

	@GetMapping("/createIssueConfirm")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	public ModelAndView createIssueConfirm() {

		// Dsp Google Facebook
		List<NonTwitterAdDto> nonTwitterAdDtoList = new ArrayList<NonTwitterAdDto>();

		// Dsp Ad
		NonTwitterAdDto nonTwitterAdDtoD1 = new NonTwitterAdDto();
		nonTwitterAdDtoD1.setAdImageName("image1.jpg\r\nimage2.jpg");
		nonTwitterAdDtoD1.setAdImageSize("100x100\r\n200x200");
		nonTwitterAdDtoD1.getAdImageUrlList()
				.add("https://www.fout.co.jp/images/freakout/product/product_freakout.png");
		nonTwitterAdDtoD1.getAdImageUrlList().add("https://upload.wikimedia.org/wikipedia/commons/7/71/Freak_Out.jpg");
		nonTwitterAdDtoD1.setAdText("");
		nonTwitterAdDtoD1.setAdReviewStatus("承認済み");
		nonTwitterAdDtoD1.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoD1.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoD1.setAdIssue("案件一覧へ");
		nonTwitterAdDtoD1.setAdIcon("fa fa-envira");
		nonTwitterAdDtoList.add(nonTwitterAdDtoD1);

		// Google Image Ad
		NonTwitterAdDto nonTwitterAdDtoG2 = new NonTwitterAdDto();
		nonTwitterAdDtoG2.setAdImageName("image1.jpg\r\nimage2.jpg");
		nonTwitterAdDtoG2.setAdImageSize("100x100\r\n200x200");
		nonTwitterAdDtoG2.getAdImageUrlList()
				.add("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");
		nonTwitterAdDtoG2.getAdImageUrlList()
				.add("https://www.gstatic.com/android/market_images/web/play_prism_hlock_2x.png");
		nonTwitterAdDtoG2.setAdText("");
		nonTwitterAdDtoG2.setAdReviewStatus("承認済み");
		nonTwitterAdDtoG2.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoG2.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoG2.setAdIssue("案件一覧へ");
		nonTwitterAdDtoG2.setAdIcon("fa fa-google");
		nonTwitterAdDtoList.add(nonTwitterAdDtoG2);

		// Facebook Ad
		NonTwitterAdDto nonTwitterAdDtoF1 = new NonTwitterAdDto();
		nonTwitterAdDtoF1.setAdImageName("image1.jpg");
		nonTwitterAdDtoF1.setAdImageSize("500x500");
		nonTwitterAdDtoF1.getAdImageUrlList()
				.add("https://cdn.pixabay.com/photo/2017/10/04/11/58/facebook-2815970_960_720.jpg");
		nonTwitterAdDtoF1.setAdText("説明文");
		nonTwitterAdDtoF1.setAdReviewStatus("承認済み");
		nonTwitterAdDtoF1.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoF1.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoF1.setAdIssue("案件一覧へ");
		nonTwitterAdDtoF1.setAdIcon("fa fa-facebook");
		nonTwitterAdDtoList.add(nonTwitterAdDtoF1);

		// Twitter
		List<TwitterAdDto> twitterAdDtoList = new ArrayList<TwitterAdDto>();

		// Twiiter website
		TwitterAdDto twitterAdDto1 = new TwitterAdDto();
		twitterAdDto1.setAdText("-----------------------------------------------------↓\r\n"
				+ "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 02月26日\r\n" + "こんばんわ！MAX BULLETです！！💥💥\r\n"
				+ "久々の更新となります😆\r\n" + "\r\n" + "MAX BULLETが移転予定の秋葉原のビルは絶賛工事中です！\r\n" + "OPENまで今しばらくお待ちください！！\r\n"
				+ "\r\n" + "秋葉原のオープンまで待てないよ！！という方にはMA… https://twitter.com/i/web/status/1100338392150274048\"\r\n"
				+ "\r\n" + "-----------------------------------------------------↓\r\n"
				+ "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 03月07日\r\n" + "こんばんわ🤗\r\n" + "春がきたと思ったら今日は寒いですね🌬❄️\r\n"
				+ ".\r\n" + "当店では様々なシューティングゲームをご用意しております🔫\r\n" + "どのゲームも盛り上がること間違いなし😎👌\r\n"
				+ "ご来店お待ちしております💁🎶… https://www.instagram.com/p/BgBAy9jlpSF/\r\n" + "\r\n"
				+ "-----------------------------------------------------↓\r\n"
				+ "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 10月04日\r\n" + ".\r\n" + "こんばんわ😸✨\r\n" + ".\r\n"
				+ "あっという間に10月に突入してしまいましたね！！\r\n" + "10月は3連休やハロウィンなど楽しみがいっぱいありますね😍💘\r\n" + "みなさん予定はもうお決まりですか🦄？？\r\n"
				+ "是非マックスバレットに遊びに来てください💁🏼💓\r\n"
				+ ".... https://www.facebook.com/MAXBULLET.NSB/videos/107717326804617/\r\n" + "");
		twitterAdDtoList.add(twitterAdDto1);

		// Issue
		IssueInputForm issueInputForm = new IssueInputForm();

		issueInputForm.setIssueName("案件名");
		issueInputForm.setBudget(1000L);
		issueInputForm.setStartDate("2019/04/01");
		issueInputForm.setEndDate("2019/04/10");
		issueInputForm.setUrl("https://www.haishisan.com");

		ModelAndView mv = new ModelAndView();
		mv.addObject("nonTwitterAdDtoList", nonTwitterAdDtoList);
		mv.addObject("twitterAdDtoList", twitterAdDtoList);
		mv.addObject("issueInputForm", issueInputForm);
		mv.setViewName("issue/createIssueConfirm");
		return mv;
	}

	@GetMapping("/createIssueComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	public ModelAndView createIssueComplete() {

		// Dsp Google Facebook
		List<NonTwitterAdDto> nonTwitterAdDtoList = new ArrayList<NonTwitterAdDto>();

		// Dsp Ad
		NonTwitterAdDto nonTwitterAdDtoD1 = new NonTwitterAdDto();
		nonTwitterAdDtoD1.setAdImageName("image1.jpg\r\nimage2.jpg");
		nonTwitterAdDtoD1.setAdImageSize("100x100\r\n200x200");
		nonTwitterAdDtoD1.getAdImageUrlList()
				.add("https://www.fout.co.jp/images/freakout/product/product_freakout.png");
		nonTwitterAdDtoD1.getAdImageUrlList().add("https://upload.wikimedia.org/wikipedia/commons/7/71/Freak_Out.jpg");
		nonTwitterAdDtoD1.setAdText("");
		nonTwitterAdDtoD1.setAdReviewStatus("承認済み");
		nonTwitterAdDtoD1.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoD1.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoD1.setAdIssue("案件一覧へ");
		nonTwitterAdDtoD1.setAdIcon("fa fa-envira");
		nonTwitterAdDtoList.add(nonTwitterAdDtoD1);

		// Google Image Ad
		NonTwitterAdDto nonTwitterAdDtoG2 = new NonTwitterAdDto();
		nonTwitterAdDtoG2.setAdImageName("image1.jpg\r\nimage2.jpg");
		nonTwitterAdDtoG2.setAdImageSize("100x100\r\n200x200");
		nonTwitterAdDtoG2.getAdImageUrlList()
				.add("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");
		nonTwitterAdDtoG2.getAdImageUrlList()
				.add("https://www.gstatic.com/android/market_images/web/play_prism_hlock_2x.png");
		nonTwitterAdDtoG2.setAdText("");
		nonTwitterAdDtoG2.setAdReviewStatus("承認済み");
		nonTwitterAdDtoG2.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoG2.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoG2.setAdIssue("案件一覧へ");
		nonTwitterAdDtoG2.setAdIcon("fa fa-google");
		nonTwitterAdDtoList.add(nonTwitterAdDtoG2);

		// Facebook Ad
		NonTwitterAdDto nonTwitterAdDtoF1 = new NonTwitterAdDto();
		nonTwitterAdDtoF1.setAdImageName("image1.jpg");
		nonTwitterAdDtoF1.setAdImageSize("500x500");
		nonTwitterAdDtoF1.getAdImageUrlList()
				.add("https://cdn.pixabay.com/photo/2017/10/04/11/58/facebook-2815970_960_720.jpg");
		nonTwitterAdDtoF1.setAdText("説明文");
		nonTwitterAdDtoF1.setAdReviewStatus("承認済み");
		nonTwitterAdDtoF1.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoF1.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoF1.setAdIssue("案件一覧へ");
		nonTwitterAdDtoF1.setAdIcon("fa fa-facebook");
		nonTwitterAdDtoList.add(nonTwitterAdDtoF1);

		// Twitter
		List<TwitterAdDto> twitterAdDtoList = new ArrayList<TwitterAdDto>();

		// Twiiter website
		TwitterAdDto twitterAdDto1 = new TwitterAdDto();
		twitterAdDto1.setAdText("-----------------------------------------------------↓\r\n"
				+ "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 02月26日\r\n" + "こんばんわ！MAX BULLETです！！💥💥\r\n"
				+ "久々の更新となります😆\r\n" + "\r\n" + "MAX BULLETが移転予定の秋葉原のビルは絶賛工事中です！\r\n" + "OPENまで今しばらくお待ちください！！\r\n"
				+ "\r\n" + "秋葉原のオープンまで待てないよ！！という方にはMA… https://twitter.com/i/web/status/1100338392150274048\"\r\n"
				+ "\r\n" + "-----------------------------------------------------↓\r\n"
				+ "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 03月07日\r\n" + "こんばんわ🤗\r\n" + "春がきたと思ったら今日は寒いですね🌬❄️\r\n"
				+ ".\r\n" + "当店では様々なシューティングゲームをご用意しております🔫\r\n" + "どのゲームも盛り上がること間違いなし😎👌\r\n"
				+ "ご来店お待ちしております💁🎶… https://www.instagram.com/p/BgBAy9jlpSF/\r\n" + "\r\n"
				+ "-----------------------------------------------------↓\r\n"
				+ "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 10月04日\r\n" + ".\r\n" + "こんばんわ😸✨\r\n" + ".\r\n"
				+ "あっという間に10月に突入してしまいましたね！！\r\n" + "10月は3連休やハロウィンなど楽しみがいっぱいありますね😍💘\r\n" + "みなさん予定はもうお決まりですか🦄？？\r\n"
				+ "是非マックスバレットに遊びに来てください💁🏼💓\r\n"
				+ ".... https://www.facebook.com/MAXBULLET.NSB/videos/107717326804617/\r\n" + "");
		twitterAdDtoList.add(twitterAdDto1);

		// Issue
		IssueInputForm issueInputForm = new IssueInputForm();

		issueInputForm.setIssueName("案件名");
		issueInputForm.setBudget(1000L);
		issueInputForm.setStartDate("2019/04/01");
		issueInputForm.setEndDate("2019/04/10");
		issueInputForm.setUrl("https://www.haishisan.com");

		ModelAndView mv = new ModelAndView();
		mv.addObject("nonTwitterAdDtoList", nonTwitterAdDtoList);
		mv.addObject("twitterAdDtoList", twitterAdDtoList);
		mv.addObject("issueInputForm", issueInputForm);
		mv.setViewName("issue/createIssueComplete");
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

	// mock_paku
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
