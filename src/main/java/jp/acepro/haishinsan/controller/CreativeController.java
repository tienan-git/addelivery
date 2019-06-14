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
import jp.acepro.haishinsan.dto.NonTwitterAdDto;
import jp.acepro.haishinsan.dto.TwitterAdDto;
import jp.acepro.haishinsan.dto.CreativeDto;
import jp.acepro.haishinsan.dto.dsp.DspCreativeDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.twitter.TwitterAdsDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.enums.CreativeType;
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

	// @Autowired
	// CreativeService creativeService;

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

	@PostMapping("/creativeConfirm_bk")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	public ModelAndView confirmCreative(@Validated CreativeInputForm creativeInputForm, BindingResult result)
			throws IOException {

		CreativeDto creativeDto = CreativeMapper.INSTANCE.map(creativeInputForm);

		List<String> resAdImageList = new ArrayList<String>();
		List<String> imageAdImageList = new ArrayList<String>();

		List<String> dspImageList = new ArrayList<String>();
		List<String> facebookImageList = new ArrayList<String>();

		if (CodeMasterServiceImpl.keywordNameList == null) {
			codeMasterService.getKeywordNameList();
		}

		if (CreativeType.DSP.getValue().equals(creativeInputForm.getCreativeType())) {
			for (MultipartFile imageFile : creativeInputForm.getMyfile1()) {
				String base64Str = Base64.getEncoder().encodeToString(imageFile.getBytes());
				StringBuffer data = new StringBuffer();
				data.append("data:image/jpeg;base64,");
				data.append(base64Str);
				dspImageList.add(data.toString());
			}
		}

		// 完了画面にGoogle画像を表示するため、画像データを取得
		if (CreativeType.GOOGLE.getValue().equals(creativeInputForm.getCreativeType())) {
			// キャンプーン作成用パラメタ設定（画像）
			switch (GoogleAdType.of(creativeInputForm.getAdType())) {
			case RESPONSIVE:
				for (MultipartFile imageFile : creativeInputForm.getMyfile2()) {
					String base64Str = Base64.getEncoder().encodeToString(imageFile.getBytes());
					StringBuffer data = new StringBuffer();
					data.append("data:image/jpeg;base64,");
					data.append(base64Str);
					resAdImageList.add(data.toString());
				}
				break;
			case IMAGE:
				for (MultipartFile imageFile : creativeInputForm.getMyfile3()) {
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

		if (CreativeType.FACEBOOK.getValue().equals(creativeInputForm.getCreativeType())) {
			for (MultipartFile imageFile : creativeInputForm.getMyfile4()) {
				String base64Str = Base64.getEncoder().encodeToString(imageFile.getBytes());
				StringBuffer data = new StringBuffer();
				data.append("data:image/jpeg;base64,");
				data.append(base64Str);
				facebookImageList.add(data.toString());
			}
		}

		if (CreativeType.INSTAGRAM.getValue().equals(creativeInputForm.getCreativeType())) {
			for (MultipartFile imageFile : creativeInputForm.getMyfile5()) {
				String base64Str = Base64.getEncoder().encodeToString(imageFile.getBytes());
				StringBuffer data = new StringBuffer();
				data.append("data:image/jpeg;base64,");
				data.append(base64Str);
				facebookImageList.add(data.toString());
			}
		}

		// 完了画面にTwitterリストを表示するため、セッションからリストを取得
		if (CreativeType.TWITTER.getValue().equals(creativeInputForm.getCreativeType())) {
//			TwitterAdsDto twitterAdsDto = new TwitterAdsDto();
//			twitterAdsDto.setTweetIdList(creativeDto.getTweetIdList());
//			// キャンペーン目的がwebsiteのみ
//			List<TwitterTweet> selectedWebsiteTweetList = twitterApiService.searchWebsiteTweetsById(twitterAdsDto);
//			creativeDto.setWebsiteTweetList(selectedWebsiteTweetList);
		}

		// creativeService.createCreative(creativeDto);
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
			twitterMsg = "Twitter:" + msg.getMessage(creativeDto.getTwitterErrorCode(),
					new Object[] { creativeDto.getTwitterParam() }, null);
		}
		ModelAndView mv = new ModelAndView("creative/creativeConfirm");
		mv.addObject("creativeDto", creativeDto);
		mv.addObject("resAdImageList", resAdImageList);
		mv.addObject("imageAdImageList", imageAdImageList);
		mv.addObject("dspImageList", dspImageList);
		mv.addObject("facebookImageList", facebookImageList);
		mv.addObject("creativeInputForm", creativeInputForm);
		mv.addObject("twitterAdDtoList", twitterAdDtoList);
		mv.addObject("dspMsg", dspMsg);
		mv.addObject("googleMsg", googleMsg);
		mv.addObject("facebookMsg", facebookMsg);
		mv.addObject("twitterMsg", twitterMsg);

		session.setAttribute("creativeDto", creativeDto);
		session.setAttribute("resAdImageList", resAdImageList);
		session.setAttribute("imageAdImageList", imageAdImageList);
		session.setAttribute("dspImageList", dspImageList);
		session.setAttribute("facebookImageList", facebookImageList);
		session.setAttribute("twitterAdDtoList", twitterAdDtoList);
		session.setAttribute("dspMsg", dspMsg);
		session.setAttribute("googleMsg", googleMsg);
		session.setAttribute("facebookMsg", facebookMsg);
		session.setAttribute("twitterMsg", twitterMsg);

		// キャンペーン作成成功したらツイートリストをsessionから削除
		session.removeAttribute("websiteTweetList");

		// オペレーションログ記録
		// operationService.create(Operation.ISSUE_CREATE.getValue(),
		// String.valueOf(creativeDto.getCreativeId()));
		return mv;

	}

	@GetMapping("/creativeComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	public ModelAndView completeCreative() throws IOException {

		CreativeDto creativeDto = (CreativeDto) session.getAttribute("creativeDto");

		List<String> resAdImageList = (ArrayList<String>) session.getAttribute("resAdImageList");
		List<String> imageAdImageList = (ArrayList<String>) session.getAttribute("imageAdImageList");

		List<String> dspImageList = (ArrayList<String>) session.getAttribute("dspImageList");
		List<String> facebookImageList = (ArrayList<String>) session.getAttribute("facebookImageList");

		String dspMsg = (String) session.getAttribute("dspMsg");
		String googleMsg = (String) session.getAttribute("googleMsg");
		String facebookMsg = (String) session.getAttribute("facebookMsg");
		String twitterMsg = (String) session.getAttribute("twitterMsg");
		List<TwitterAdDto> twitterAdDtoList = (ArrayList<TwitterAdDto>) session.getAttribute("twitterAdDtoList");

		ModelAndView mv = new ModelAndView("creative/creativeComplete");
		mv.addObject("creativeDto", creativeDto);
		mv.addObject("resAdImageList", resAdImageList);
		mv.addObject("imageAdImageList", imageAdImageList);
		mv.addObject("dspImageList", dspImageList);
		mv.addObject("facebookImageList", facebookImageList);
		mv.addObject("twitterAdDtoList", twitterAdDtoList);
		mv.addObject("dspMsg", dspMsg);
		mv.addObject("googleMsg", googleMsg);
		mv.addObject("facebookMsg", facebookMsg);
		mv.addObject("twitterMsg", twitterMsg);

		session.removeAttribute("creativeDto");
		session.removeAttribute("resAdImageList");
		session.removeAttribute("imageAdImageList");
		session.removeAttribute("dspImageList");
		session.removeAttribute("facebookImageList");
		session.removeAttribute("twitterAdDtoList");
		session.removeAttribute("dspMsg");
		session.removeAttribute("googleMsg");
		session.removeAttribute("facebookMsg");
		session.removeAttribute("twitterMsg");

		// オペレーションログ記録
		// operationService.create(Operation.ISSUE_CREATE.getValue(),
		// String.valueOf(creativeDto.getCreativeId()));
		return mv;

	}

	@GetMapping("/creativeList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_VIEW + "')")
	public ModelAndView creativeList() {

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

		NonTwitterAdDto nonTwitterAdDtoD2 = new NonTwitterAdDto();
		nonTwitterAdDtoD2.setAdImageName("image1.jpg");
		nonTwitterAdDtoD2.setAdImageSize("100x100");
		nonTwitterAdDtoD2.getAdImageUrlList()
				.add("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");
		nonTwitterAdDtoD2.setAdText("");
		nonTwitterAdDtoD2.setAdReviewStatus("承認済み");
		nonTwitterAdDtoD2.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoD2.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoD2.setAdIssue("案件一覧へ");
		nonTwitterAdDtoD2.setAdIcon("fa fa-envira");
		nonTwitterAdDtoList.add(nonTwitterAdDtoD2);

		// Google Response Ad
		NonTwitterAdDto nonTwitterAdDtoG1 = new NonTwitterAdDto();
		nonTwitterAdDtoG1.setAdImageName("image1.jpg\r\nimage2.jpg");
		nonTwitterAdDtoG1.setAdImageSize("100x100\r\n200x200");
		nonTwitterAdDtoG1.getAdImageUrlList()
				.add("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");
		nonTwitterAdDtoG1.getAdImageUrlList()
				.add("https://www.gstatic.com/android/market_images/web/play_prism_hlock_2x.png");
		nonTwitterAdDtoG1.setAdText("短い広告見出し\r\n説明文");
		nonTwitterAdDtoG1.setAdReviewStatus("承認済み");
		nonTwitterAdDtoG1.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoG1.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoG1.setAdIssue("案件一覧へ");
		nonTwitterAdDtoG1.setAdIcon("fa fa-google");
		nonTwitterAdDtoList.add(nonTwitterAdDtoG1);

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

		// Google Text Ad
		NonTwitterAdDto nonTwitterAdDtoG3 = new NonTwitterAdDto();
		nonTwitterAdDtoG3.setAdImageName("");
		nonTwitterAdDtoG3.setAdImageSize("");
		nonTwitterAdDtoG3.setAdText("広告見出し１\r\n広告見出し２\r\n説明文");
		nonTwitterAdDtoG3.setAdReviewStatus("承認済み");
		nonTwitterAdDtoG3.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoG3.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoG3.setAdIssue("案件一覧へ");
		nonTwitterAdDtoG3.setAdIcon("fa fa-google");
		nonTwitterAdDtoList.add(nonTwitterAdDtoG3);

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

		NonTwitterAdDto nonTwitterAdDtoF2 = new NonTwitterAdDto();
		nonTwitterAdDtoF2.setAdImageName("image2.jpg");
		nonTwitterAdDtoF2.setAdImageSize("500x500");
		nonTwitterAdDtoF2.getAdImageUrlList().add(
				"https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/Facebook_New_Logo_%282015%29.svg/2000px-Facebook_New_Logo_%282015%29.svg.png");
		nonTwitterAdDtoF2.setAdText("");
		nonTwitterAdDtoF2.setAdReviewStatus("承認済み");
		nonTwitterAdDtoF2.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoF2.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoF2.setAdIssue("案件一覧へ");
		nonTwitterAdDtoF2.setAdIcon("fa fa-facebook");
		nonTwitterAdDtoList.add(nonTwitterAdDtoF2);

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

		TwitterAdDto twitterAdDto2 = new TwitterAdDto();
		twitterAdDto2.setAdText("-----------------------------------------------------↓\r\n"
				+ "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 02月26日\r\n" + "こんばんわ！MAX BULLETです！！💥💥\r\n"
				+ "久々の更新となります😆\r\n" + "\r\n" + "MAX BULLETが移転予定の秋葉原のビルは絶賛工事中です！\r\n" + "OPENまで今しばらくお待ちください！！\r\n"
				+ "\r\n" + "秋葉原のオープンまで待てないよ！！という方にはMA… https://twitter.com/i/web/status/1100338392150274048\"\r\n"
				+ "\r\n" + "-----------------------------------------------------↓\r\n"
				+ "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 03月07日\r\n" + "こんばんわ🤗\r\n" + "春がきたと思ったら今日は寒いですね🌬❄️\r\n"
				+ ".\r\n" + "当店では様々なシューティングゲームをご用意しております🔫\r\n" + "どのゲームも盛り上がること間違いなし😎👌\r\n"
				+ "ご来店お待ちしております💁🎶… https://www.instagram.com/p/BgBAy9jlpSF/");
		twitterAdDtoList.add(twitterAdDto2);

		// Twiiter followers
		TwitterAdDto twitterAdDto3 = new TwitterAdDto();
		twitterAdDto3.setAdText("-----------------------------------------------------↓\r\n"
				+ "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 12月05日\r\n" + "こんばんわ🌙\r\n" + "\r\n"
				+ "本日も元気よくオープンしております💁\r\n" + "\r\n"
				+ "こちらの一品は紅茶鴨のスモークでございます！かいわれとの相性も抜群でとても美味しいです！是非食べにきてはいかがでしょうか？🤤\r\n" + "\r\n"
				+ "皆… Bar-https://www.instagram.com/p/BcUGyPAFk3o/");
		twitterAdDtoList.add(twitterAdDto3);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("creative/creativeList");
		mv.addObject("nonTwitterAdDtoList", nonTwitterAdDtoList);
		mv.addObject("twitterAdDtoList", twitterAdDtoList);
		return mv;
	}

	// mock_paku
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

	@GetMapping("/mediaSelection")
	public ModelAndView mediaSelection(ModelAndView mv) {

		mv.setViewName("creative/mediaSelection");
		return mv;
	}

	@GetMapping("/mediaDescription")
	public ModelAndView mediaDescription(ModelAndView mv) {

		mv.setViewName("creative/mediaDescription");
		return mv;
	}

	@GetMapping("/googleBanner")
	public ModelAndView googleBanner(ModelAndView mv) {

		mv.setViewName("creative/googleBanner");
		return mv;
	}

	@GetMapping("/createLink")
	public ModelAndView createLink(ModelAndView mv) {

		mv.setViewName("creative/createLink");
		return mv;
	}

	@GetMapping("/createArea")
	public ModelAndView createArea(ModelAndView mv) {
		getGoogleAreaList();
		mv.setViewName("creative/createArea");
		return mv;
	}

	@GetMapping("/createDate")
	public ModelAndView createDate(ModelAndView mv) {

		mv.setViewName("creative/createDate");
		return mv;
	}

	@GetMapping("/createBudget")
	public ModelAndView createBudget(ModelAndView mv) {

		mv.setViewName("creative/createBudget");
		return mv;
	}

	@GetMapping("/createConfirm")
	public ModelAndView createConfirm(ModelAndView mv) {

		mv.setViewName("creative/createConfirm");
		return mv;
	}
	
	@GetMapping("/createSuccess")
	public ModelAndView createSuccess(ModelAndView mv) {

		mv.setViewName("creative/createSuccess");
		return mv;
	}
	
	@GetMapping("/uploadMediaSelection")
	public ModelAndView uploadMediaSelection(ModelAndView mv) {

		mv.setViewName("creative/uploadMediaSelection");
		return mv;
	}
	
	@GetMapping("/uploadMediaDescription")
	public ModelAndView uploadMediaDescription(ModelAndView mv) {

		mv.setViewName("creative/uploadMediaDescription");
		return mv;
	}

	@GetMapping("/uploadGoogleBanner")
	public ModelAndView uploadGoogleBanner(ModelAndView mv) {

		mv.setViewName("creative/uploadGoogleBanner");
		return mv;
	}
	
	@GetMapping("/uploadCreateConfirm")
	public ModelAndView uploadCreateConfirm(ModelAndView mv) {

		mv.setViewName("creative/uploadCreateConfirm");
		return mv;
	}
	
	@GetMapping("/uploadCreateSuccess")
	public ModelAndView uploadCreateSuccess(ModelAndView mv) {

		mv.setViewName("creative/uploadCreateSuccess");
		return mv;
	}

	private void getGoogleAreaList() {

		if (CodeMasterServiceImpl.googleAreaNameList == null) {
			codeMasterService.getGoogleAreaList();
		}
	}
}
