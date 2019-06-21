package jp.acepro.haishinsan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
import jp.acepro.haishinsan.form.CreateIssueForm;
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

	// Mockデータ
	List<NonTwitterAdDto> mockNonTwitterAdDtoList = new ArrayList<NonTwitterAdDto>();
	List<TwitterAdDto> mockTwitterAdDtoList = new ArrayList<TwitterAdDto>();
	List<DspSegmentListDto> mockDspSegmentDtoList = new ArrayList<DspSegmentListDto>();

	// Mockデータ作成
	private void mockDateCreate() {

		// Twitter以外広告Mockデータ
		mockNonTwitterAdDtoList.clear();
		// Dsp Ad
		NonTwitterAdDto nonTwitterAdDtoD1 = new NonTwitterAdDto();
		nonTwitterAdDtoD1.setAdId(1L);
		nonTwitterAdDtoD1.setAdImageName("image1.jpg\r\nimage2.jpg");
		nonTwitterAdDtoD1.setAdImageSize("100x100\r\n200x200");
		nonTwitterAdDtoD1.getAdImageUrlList().add("https://www.fout.co.jp/images/freakout/product/product_freakout.png");
		nonTwitterAdDtoD1.getAdImageUrlList().add("https://upload.wikimedia.org/wikipedia/commons/7/71/Freak_Out.jpg");
		nonTwitterAdDtoD1.setAdText("");
		nonTwitterAdDtoD1.setAdReviewStatus("承認済み");
		nonTwitterAdDtoD1.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoD1.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoD1.setAdIssue("案件一覧へ");
		nonTwitterAdDtoD1.setAdIcon("fa fa-envira");
		mockNonTwitterAdDtoList.add(nonTwitterAdDtoD1);

		NonTwitterAdDto nonTwitterAdDtoD2 = new NonTwitterAdDto();
		nonTwitterAdDtoD2.setAdId(2L);
		nonTwitterAdDtoD2.setAdImageName("image1.jpg");
		nonTwitterAdDtoD2.setAdImageSize("100x100");
		nonTwitterAdDtoD2.getAdImageUrlList().add("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");
		nonTwitterAdDtoD2.setAdText("");
		nonTwitterAdDtoD2.setAdReviewStatus("承認済み");
		nonTwitterAdDtoD2.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoD2.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoD2.setAdIssue("案件一覧へ");
		nonTwitterAdDtoD2.setAdIcon("fa fa-envira");
		mockNonTwitterAdDtoList.add(nonTwitterAdDtoD2);

		// Google Response Ad
		NonTwitterAdDto nonTwitterAdDtoG1 = new NonTwitterAdDto();
		nonTwitterAdDtoG1.setAdId(3L);
		nonTwitterAdDtoG1.setAdImageName("image1.jpg\r\nimage2.jpg");
		nonTwitterAdDtoG1.setAdImageSize("100x100\r\n200x200");
		nonTwitterAdDtoG1.getAdImageUrlList().add("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");
		nonTwitterAdDtoG1.getAdImageUrlList().add("https://www.gstatic.com/android/market_images/web/play_prism_hlock_2x.png");
		nonTwitterAdDtoG1.setAdText("短い広告見出し\r\n説明文");
		nonTwitterAdDtoG1.setAdReviewStatus("承認済み");
		nonTwitterAdDtoG1.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoG1.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoG1.setAdIssue("案件一覧へ");
		nonTwitterAdDtoG1.setAdIcon("fa fa-google");
		mockNonTwitterAdDtoList.add(nonTwitterAdDtoG1);

		// Google Image Ad
		NonTwitterAdDto nonTwitterAdDtoG2 = new NonTwitterAdDto();
		nonTwitterAdDtoG2.setAdId(4L);
		nonTwitterAdDtoG2.setAdImageName("image1.jpg\r\nimage2.jpg");
		nonTwitterAdDtoG2.setAdImageSize("100x100\r\n200x200");
		nonTwitterAdDtoG2.getAdImageUrlList().add("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");
		nonTwitterAdDtoG2.getAdImageUrlList().add("https://www.gstatic.com/android/market_images/web/play_prism_hlock_2x.png");
		nonTwitterAdDtoG2.setAdText("");
		nonTwitterAdDtoG2.setAdReviewStatus("承認済み");
		nonTwitterAdDtoG2.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoG2.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoG2.setAdIssue("案件一覧へ");
		nonTwitterAdDtoG2.setAdIcon("fa fa-google");
		mockNonTwitterAdDtoList.add(nonTwitterAdDtoG2);

		// Google Text Ad
		NonTwitterAdDto nonTwitterAdDtoG3 = new NonTwitterAdDto();
		nonTwitterAdDtoG3.setAdId(5L);
		nonTwitterAdDtoG3.setAdImageName("");
		nonTwitterAdDtoG3.setAdImageSize("");
		nonTwitterAdDtoG3.setAdText("広告見出し１\r\n広告見出し２\r\n説明文");
		nonTwitterAdDtoG3.setAdReviewStatus("承認済み");
		nonTwitterAdDtoG3.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoG3.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoG3.setAdIssue("案件一覧へ");
		nonTwitterAdDtoG3.setAdIcon("fa fa-google");
		mockNonTwitterAdDtoList.add(nonTwitterAdDtoG3);

		// Facebook Ad
		NonTwitterAdDto nonTwitterAdDtoF1 = new NonTwitterAdDto();
		nonTwitterAdDtoF1.setAdId(6L);
		nonTwitterAdDtoF1.setAdImageName("image1.jpg");
		nonTwitterAdDtoF1.setAdImageSize("500x500");
		nonTwitterAdDtoF1.getAdImageUrlList().add("https://cdn.pixabay.com/photo/2017/10/04/11/58/facebook-2815970_960_720.jpg");
		nonTwitterAdDtoF1.setAdText("説明文");
		nonTwitterAdDtoF1.setAdReviewStatus("承認済み");
		nonTwitterAdDtoF1.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoF1.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoF1.setAdIssue("案件一覧へ");
		nonTwitterAdDtoF1.setAdIcon("fa fa-facebook");
		mockNonTwitterAdDtoList.add(nonTwitterAdDtoF1);

		NonTwitterAdDto nonTwitterAdDtoF2 = new NonTwitterAdDto();
		nonTwitterAdDtoF2.setAdId(7L);
		nonTwitterAdDtoF2.setAdImageName("image2.jpg");
		nonTwitterAdDtoF2.setAdImageSize("500x500");
		nonTwitterAdDtoF2.getAdImageUrlList().add("https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/Facebook_New_Logo_%282015%29.svg/2000px-Facebook_New_Logo_%282015%29.svg.png");
		nonTwitterAdDtoF2.setAdText("");
		nonTwitterAdDtoF2.setAdReviewStatus("承認済み");
		nonTwitterAdDtoF2.setAdCreateDate("2019/04/01");
		nonTwitterAdDtoF2.setAdReviewDate("2019/04/03");
		nonTwitterAdDtoF2.setAdIssue("案件一覧へ");
		nonTwitterAdDtoF2.setAdIcon("fa fa-facebook");
		mockNonTwitterAdDtoList.add(nonTwitterAdDtoF2);

		// Twitter広告Mockデータ
		mockTwitterAdDtoList.clear();

		// Twiiter website
		TwitterAdDto twitterAdDto1 = new TwitterAdDto();
		twitterAdDto1.setAdId(8L);
		twitterAdDto1.setAdText("-----------------------------------------------------↓\r\n" + "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 02月26日\r\n" + "こんばんわ！MAX BULLETです！！💥💥\r\n" + "久々の更新となります😆\r\n" + "\r\n" + "MAX BULLETが移転予定の秋葉原のビルは絶賛工事中です！\r\n" + "OPENまで今しばらくお待ちください！！\r\n" + "\r\n"
				+ "秋葉原のオープンまで待てないよ！！という方にはMA… https://twitter.com/i/web/status/1100338392150274048\"\r\n" + "\r\n" + "-----------------------------------------------------↓\r\n" + "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 03月07日\r\n" + "こんばんわ🤗\r\n" + "春がきたと思ったら今日は寒いですね🌬❄️\r\n" + ".\r\n"
				+ "当店では様々なシューティングゲームをご用意しております🔫\r\n" + "どのゲームも盛り上がること間違いなし😎👌\r\n" + "ご来店お待ちしております💁🎶… https://www.instagram.com/p/BgBAy9jlpSF/\r\n" + "\r\n" + "-----------------------------------------------------↓\r\n" + "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 10月04日\r\n" + ".\r\n"
				+ "こんばんわ😸✨\r\n" + ".\r\n" + "あっという間に10月に突入してしまいましたね！！\r\n" + "10月は3連休やハロウィンなど楽しみがいっぱいありますね😍💘\r\n" + "みなさん予定はもうお決まりですか🦄？？\r\n" + "是非マックスバレットに遊びに来てください💁🏼💓\r\n" + ".... https://www.facebook.com/MAXBULLET.NSB/videos/107717326804617/\r\n" + "");
		mockTwitterAdDtoList.add(twitterAdDto1);

		TwitterAdDto twitterAdDto2 = new TwitterAdDto();
		twitterAdDto2.setAdId(9L);
		twitterAdDto2.setAdText("-----------------------------------------------------↓\r\n" + "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 02月26日\r\n" + "こんばんわ！MAX BULLETです！！💥💥\r\n" + "久々の更新となります😆\r\n" + "\r\n" + "MAX BULLETが移転予定の秋葉原のビルは絶賛工事中です！\r\n" + "OPENまで今しばらくお待ちください！！\r\n" + "\r\n"
				+ "秋葉原のオープンまで待てないよ！！という方にはMA… https://twitter.com/i/web/status/1100338392150274048\"\r\n" + "\r\n" + "-----------------------------------------------------↓\r\n" + "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 03月07日\r\n" + "こんばんわ🤗\r\n" + "春がきたと思ったら今日は寒いですね🌬❄️\r\n" + ".\r\n"
				+ "当店では様々なシューティングゲームをご用意しております🔫\r\n" + "どのゲームも盛り上がること間違いなし😎👌\r\n" + "ご来店お待ちしております💁🎶… https://www.instagram.com/p/BgBAy9jlpSF/");
		mockTwitterAdDtoList.add(twitterAdDto2);

		// Twiiter followers
		TwitterAdDto twitterAdDto3 = new TwitterAdDto();
		twitterAdDto3.setAdId(10L);
		twitterAdDto3.setAdText("-----------------------------------------------------↓\r\n" + "MAXBULLET（マックスバレット）  @max_bullet_jp ・ 12月05日\r\n" + "こんばんわ🌙\r\n" + "\r\n" + "本日も元気よくオープンしております💁\r\n" + "\r\n" + "こちらの一品は紅茶鴨のスモークでございます！かいわれとの相性も抜群でとても美味しいです！是非食べにきてはいかがでしょうか？🤤\r\n" + "\r\n"
				+ "皆… Bar-https://www.instagram.com/p/BcUGyPAFk3o/");
		mockTwitterAdDtoList.add(twitterAdDto3);

		// セグメントMockデータ
		mockDspSegmentDtoList.clear();

		DspSegmentListDto dspSegmentListDto1 = new DspSegmentListDto();
		dspSegmentListDto1.setSegmentId(1);
		dspSegmentListDto1.setUrl("https://www.google.com");
		mockDspSegmentDtoList.add(dspSegmentListDto1);

		DspSegmentListDto dspSegmentListDto2 = new DspSegmentListDto();
		dspSegmentListDto2.setSegmentId(2);
		dspSegmentListDto2.setUrl("https://www.yahoo.co.jp");
		mockDspSegmentDtoList.add(dspSegmentListDto2);
	}

	// 新規作成（STEP1：広告物選択）
	@GetMapping("/creativeList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_VIEW + "')")
	public ModelAndView creativeList(@ModelAttribute CreateIssueForm createIssueForm) {

		// Mockデータ作成
		mockDateCreate();

		// Mockデータを利用して、広告一覧画面を初期表示
		ModelAndView mv = new ModelAndView();
		mv.setViewName("issue/creativeList");
		mv.addObject("createIssueForm", createIssueForm);
		mv.addObject("nonTwitterAdDtoList", mockNonTwitterAdDtoList);
		mv.addObject("twitterAdDtoList", mockTwitterAdDtoList);
		return mv;
	}

	// 新規作成（STEP2：基本情報入力）
	//@PostMapping("/createIssue")
	@GetMapping("/createIssue")
	/*
	 * @PreAuthorize("hasAuthority('" +
	 * jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	 */
	public ModelAndView createIssue(@ModelAttribute @Validated CreateIssueForm createIssueForm, BindingResult result) {

		// 入力ＦＯＲＭ内容をＳＥＳＳＯＮへ一時退避
		session.setAttribute("createIssueForm", createIssueForm);

		// 広告物を選択していない時、
		// エラーメッセージ(広告物を選択してください。)を表示する
		if (createIssueForm.getNonTwitterAdIdList().size() == 0 && createIssueForm.getTwitterAdIdList().size() == 0) {
			// 広告未選択時、エラーメッセージを表示
			result.reject("MOCK01");
			// Mockデータを利用して、広告一覧画面を初期表示
			ModelAndView mv = new ModelAndView();
			mv.setViewName("issue/creativeList");
			mv.addObject("nonTwitterAdDtoList", mockNonTwitterAdDtoList);
			mv.addObject("twitterAdDtoList", mockTwitterAdDtoList);
			return mv;
		}

		// 広告物を選択している時、
		// 選択した広告物の内容を取得して、次画面（基本情報入力画面）へ遷移

		// 非Twitter広告を選択した時
		List<NonTwitterAdDto> nonTwitterAdDtoList = new ArrayList<NonTwitterAdDto>();
		if (createIssueForm.getNonTwitterAdIdList().size() != 0) {
			nonTwitterAdDtoList = mockNonTwitterAdDtoList.stream().filter(obj -> createIssueForm.getNonTwitterAdIdList().contains(obj.getAdId())).collect(Collectors.toList());
		}

		// Twitter広告を選択した時
		List<TwitterAdDto> twitterAdDtoList = new ArrayList<TwitterAdDto>();
		if (createIssueForm.getTwitterAdIdList().size() != 0) {
			twitterAdDtoList = mockTwitterAdDtoList.stream().filter(obj -> createIssueForm.getTwitterAdIdList().contains(obj.getAdId())).collect(Collectors.toList());
		}

		// 画面遷移
		ModelAndView mv = new ModelAndView();
		//mv.setViewName("issue/createIssue");
		mv.setViewName("creative/createLink");
		mv.addObject("createIssueForm", createIssueForm);
		mv.addObject("nonTwitterAdDtoList", nonTwitterAdDtoList);
		mv.addObject("twitterAdDtoList", twitterAdDtoList);
		mv.addObject("dspSegmentDtoList", mockDspSegmentDtoList);
		return mv;
	}

	// 新規作成（STEP3：基本情報入力確認）
	@PostMapping("/createIssueConfirm")
	public ModelAndView createIssueConfirm(@Validated CreateIssueForm createIssueForm, BindingResult result) {

		// 入力ＦＯＲＭ内容をＳＥＳＳＯＮから再取得
		CreateIssueForm firstCreateIssueForm = new CreateIssueForm();
		CreateIssueForm sessionForm = (CreateIssueForm) session.getAttribute("createIssueForm");
		BeanUtils.copyProperties(sessionForm, firstCreateIssueForm);

		// 選択した広告物を入力フォームに追加
		createIssueForm.setNonTwitterAdIdList(firstCreateIssueForm.getNonTwitterAdIdList());
		createIssueForm.setTwitterAdIdList(firstCreateIssueForm.getTwitterAdIdList());

		// 入力ＦＯＲＭ内容をＳＥＳＳＯＮへ一時退避
		session.setAttribute("createIssueForm", createIssueForm);

		// 非Twitter広告を選択した時
		List<NonTwitterAdDto> nonTwitterAdDtoList = new ArrayList<NonTwitterAdDto>();
		if (createIssueForm.getNonTwitterAdIdList().size() != 0) {
			nonTwitterAdDtoList = mockNonTwitterAdDtoList.stream().filter(obj -> createIssueForm.getNonTwitterAdIdList().contains(obj.getAdId())).collect(Collectors.toList());
		}

		// Twitter広告を選択した時
		List<TwitterAdDto> twitterAdDtoList = new ArrayList<TwitterAdDto>();
		if (createIssueForm.getTwitterAdIdList().size() != 0) {
			twitterAdDtoList = mockTwitterAdDtoList.stream().filter(obj -> createIssueForm.getTwitterAdIdList().contains(obj.getAdId())).collect(Collectors.toList());
		}

		// セグメントを選択した時
		DspSegmentListDto dspSegmentListDto = new DspSegmentListDto();
		if (createIssueForm.getSegmentId() != null) {
			dspSegmentListDto = mockDspSegmentDtoList.stream().filter(obj -> createIssueForm.getSegmentId().equals(obj.getSegmentId())).findFirst().orElse(null);
		}
		createIssueForm.setSegmentUrl(dspSegmentListDto.getUrl());

		ModelAndView mv = new ModelAndView();
		mv.addObject("nonTwitterAdDtoList", nonTwitterAdDtoList);
		mv.addObject("twitterAdDtoList", twitterAdDtoList);
		mv.addObject("createIssueForm", createIssueForm);
		mv.setViewName("issue/createIssueConfirm");
		return mv;
	}

	// 新規作成（STEP4：新規作成完了）
	@PostMapping("/createIssueComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	public ModelAndView createIssueComplete() {

		// 入力ＦＯＲＭ内容をＳＥＳＳＯＮから再取得
		CreateIssueForm createIssueForm = new CreateIssueForm();
		CreateIssueForm sessionForm = (CreateIssueForm) session.getAttribute("createIssueForm");
		BeanUtils.copyProperties(sessionForm, createIssueForm);

		// 非Twitter広告を選択した時
		List<NonTwitterAdDto> nonTwitterAdDtoList = new ArrayList<NonTwitterAdDto>();
		if (createIssueForm.getNonTwitterAdIdList().size() != 0) {
			nonTwitterAdDtoList = mockNonTwitterAdDtoList.stream().filter(obj -> createIssueForm.getNonTwitterAdIdList().contains(obj.getAdId())).collect(Collectors.toList());
		}

		// Twitter広告を選択した時
		List<TwitterAdDto> twitterAdDtoList = new ArrayList<TwitterAdDto>();
		if (createIssueForm.getTwitterAdIdList().size() != 0) {
			twitterAdDtoList = mockTwitterAdDtoList.stream().filter(obj -> createIssueForm.getTwitterAdIdList().contains(obj.getAdId())).collect(Collectors.toList());
		}

		// セグメントを選択した時
		DspSegmentListDto dspSegmentListDto = new DspSegmentListDto();
		if (createIssueForm.getSegmentId() != null) {
			dspSegmentListDto = mockDspSegmentDtoList.stream().filter(obj -> createIssueForm.getSegmentId().equals(obj.getSegmentId())).findFirst().orElse(null);
		}
		createIssueForm.setSegmentUrl(dspSegmentListDto.getUrl());

		// 画面遷移
		ModelAndView mv = new ModelAndView();
		mv.addObject("nonTwitterAdDtoList", nonTwitterAdDtoList);
		mv.addObject("twitterAdDtoList", twitterAdDtoList);
		mv.addObject("createIssueForm", createIssueForm);
		mv.setViewName("issue/createIssueComplete");
		return mv;
	}

	@PostMapping("/completeIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SIMPLE_CAMPAIGN_MANAGE + "')")
	public ModelAndView completeIssue(@Validated IssueInputForm issueInputForm, BindingResult result) throws IOException {

		// ツイート必須チェック
		if (issueInputForm.isTwitterSelected() && CollectionUtils.isEmpty(issueInputForm.getTweetIdList())) {
			result.reject(ErrorCodeConstant.E20005);

//			return createIssue(issueInputForm);
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
			facebookMsg = "Facebook:" + msg.getMessage(issueDto.getFacebookErrorCode(), issueDto.getFacebookParam(), null);
		}
		if (issueDto.getTwitterErrorCode() != null) {
			twitterMsg = "Twitter:" + msg.getMessage(issueDto.getTwitterErrorCode(), new Object[] { issueDto.getTwitterParam() }, null);
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
