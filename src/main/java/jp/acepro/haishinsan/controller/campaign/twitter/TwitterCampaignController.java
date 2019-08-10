package jp.acepro.haishinsan.controller.campaign.twitter;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.dto.twitter.TwitterAdsDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.TwitterLocationType;
import jp.acepro.haishinsan.enums.TwitterObjective;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.TwitterAdsInputForm;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.campaign.twitter.TwitterCampaignApiService;
import jp.acepro.haishinsan.service.twitter.TwitterReportingService;

@lombok.extern.slf4j.Slf4j
@Controller
@RequestMapping("/twitter")
public class TwitterCampaignController {

    @Autowired
    OperationService operationService;

    @Autowired
    HttpSession session;

    @Autowired
    CodeMasterService codeMasterService;

    @Autowired
    TwitterReportingService twitterReportingService;

    @Autowired
    TwitterCampaignApiService twitterCampaignApiService;

    // キャンペーン目的選択
    @GetMapping("/mediaDescription")
    public ModelAndView mediaDescription(ModelAndView mv) {

        mv.setViewName("campaign/twitter/mediaDescription");
        return mv;
    }

    // ツイート選択
    @GetMapping("/selectTweetList")
    public ModelAndView selectTweetList(@RequestParam Integer tweetType, ModelAndView mv,
            @ModelAttribute TwitterAdsInputForm twitterAdsInputForm, BindingResult result) {

        // 広告タイプをsessionに保存する
        session.setAttribute("objective", tweetType);

        switch (TwitterObjective.of(tweetType)) {
        // websiteの場合
        case WEBSITE:
            List<TwitterTweet> websiteTweetList = twitterCampaignApiService.searchWebsiteTweets();
            session.setAttribute("tweetList", websiteTweetList);
            twitterAdsInputForm.setTweetList(websiteTweetList);
            break;
        // followersの場合
        case FOLLOWER:
            List<TwitterTweet> followersTweetList = twitterCampaignApiService.searchFollowersTweets();
            session.setAttribute("tweetList", followersTweetList);
            twitterAdsInputForm.setTweetList(followersTweetList);
            break;
        }
        log.debug("-----------------------------------------------------");
        log.debug("Twitterのキャンペーン目的：" + tweetType);
        log.debug("-----------------------------------------------------");
        twitterAdsInputForm.setObjective(tweetType);
        mv.setViewName("campaign/twitter/selectTweetList");
        return mv;
    }

    // 配信地域
    @PostMapping("/createArea")
    public ModelAndView createArea(@Validated TwitterAdsInputForm twitterAdsInputForm, BindingResult result,
            ModelAndView mv) {

        // コードマスタを読込
        searchRegions();

        try {
            twitterCampaignApiService.tweetCheck(twitterAdsInputForm.getTweetIdList());
        } catch (BusinessException e) {
            // 異常時レスポンス
            result.reject(e.getMessage());
            // 1.sessionに保存したツイートリストを取得する
            List<TwitterTweet> tweetList = (List<TwitterTweet>) session.getAttribute("tweetList");
            // 2.sessionに保存した広告タイプを取得する
            Integer tweetType = (Integer) session.getAttribute("objective");
            twitterAdsInputForm.setTweetList(tweetList);
            twitterAdsInputForm.setObjective(tweetType);
            mv.setViewName("campaign/twitter/selectTweetList");
            return mv;
        }
        log.debug("-----------------------------------------------------");
        log.debug("TwitterIdList：" + twitterAdsInputForm.getTweetIdList().toString());
        log.debug("-----------------------------------------------------");
        // 正常時レスポンス
        // 1. 選択したツイートIDリストをsessionに保存
        session.setAttribute("tweetIdList", twitterAdsInputForm.getTweetIdList());
        // 地域初期値設定
        twitterAdsInputForm.setLocation(TwitterLocationType.ALLCITY.getValue());
        mv.setViewName("campaign/twitter/createArea");
        return mv;
    }

    // 日程
    @PostMapping("/createDate")
    public ModelAndView createDate(@Validated TwitterAdsInputForm twitterAdsInputForm, BindingResult result,
            ModelAndView mv) {

        // Form → Dto
        TwitterAdsDto twitterAdsDto = new TwitterAdsDto();
        twitterAdsDto.setLocation(twitterAdsInputForm.getLocation());
        twitterAdsDto.setRegions(twitterAdsInputForm.getRegions());
        log.debug("-----------------------------------------------------");
        log.debug("配信地域：" + twitterAdsDto.toString());
        log.debug("-----------------------------------------------------");
        try {
            twitterCampaignApiService.areaCheck(twitterAdsDto);
        } catch (BusinessException e) {
            // 異常時レスポンス
            result.reject(e.getMessage());
            mv.setViewName("campaign/twitter/createArea");
            return mv;
        }

        // 地域情報をsessionに保存する
        session.setAttribute("area", twitterAdsDto);
        mv.setViewName("campaign/twitter/createDate");
        return mv;
    }

    // 予算
    @PostMapping("/createBudget")
    public ModelAndView createBudget(@Validated TwitterAdsInputForm twitterAdsInputForm, BindingResult result,
            ModelAndView mv) {

        // Form → Dto
        TwitterAdsDto twitterAdsDto = new TwitterAdsDto();
        twitterAdsDto.setStartTime(twitterAdsInputForm.getStartTime());
        twitterAdsDto.setEndTime(twitterAdsInputForm.getEndTime());
        log.debug("-----------------------------------------------------");
        log.debug("配信日付：" + twitterAdsDto.toString());
        log.debug("-----------------------------------------------------");

        try {
            twitterCampaignApiService.dailyCheck(twitterAdsDto);
        } catch (BusinessException e) {
            // 異常時レスポンス
            result.reject(e.getMessage());
            mv.setViewName("campaign/twitter/createDate");
            return mv;
        }

        // 日程情報をsessionに保存する
        session.setAttribute("deliveryTime", twitterAdsDto);
        mv.setViewName("campaign/twitter/createBudget");
        return mv;
    }

    // 確認
    @PostMapping("/createConfirm")
    public ModelAndView createConfirm(@Validated TwitterAdsInputForm twitterAdsInputForm, BindingResult result,
            ModelAndView mv) {

        // Form → Dto
        TwitterAdsDto twitterAdsDto = new TwitterAdsDto();
        twitterAdsDto.setDailyBudget(twitterAdsInputForm.getDailyBudget());
        twitterAdsDto.setTotalBudget(twitterAdsInputForm.getTotalBudget());
        twitterAdsDto.setCampaignName(twitterAdsInputForm.getCampaignName());
        // 地域をsessionから取得する
        twitterAdsDto.setLocation(((TwitterAdsDto) session.getAttribute("area")).getLocation());
        twitterAdsDto.setRegions(((TwitterAdsDto) session.getAttribute("area")).getRegions());

        log.debug("-----------------------------------------------------");
        log.debug("予算：" + twitterAdsDto.toString());
        log.debug("-----------------------------------------------------");

        try {
            twitterCampaignApiService.budgetCheck(twitterAdsDto);
        } catch (BusinessException e) {
            // 異常時レスポンス
            result.reject(e.getMessage(),
                    Objects.isNull(e.getParams()) ? null : new Object[] { e.getParams()[0] + "円" }, null);
            mv.setViewName("campaign/twitter/createBudget");
            return mv;
        }

        // 予算とキャンペーン名をsessionに保存する
        session.setAttribute("budget", twitterAdsDto);

        // 広告タイプをsessionから取得
        twitterAdsDto.setObjective((Integer) session.getAttribute("objective"));
        // ツイートIdリストをsessionから取得する
        twitterAdsDto.setTweetIdList((List<String>) session.getAttribute("tweetIdList"));
        // ツイートJリストをsessionから取得する
        twitterAdsDto.setTweetList((List<TwitterTweet>) session.getAttribute("tweetList"));
        // 日程をsessionから取得する
        twitterAdsDto.setStartTime(((TwitterAdsDto) session.getAttribute("deliveryTime")).getStartTime());
        twitterAdsDto.setEndTime(((TwitterAdsDto) session.getAttribute("deliveryTime")).getEndTime());
        // 予算をsessionから取得する
        twitterAdsDto.setDailyBudget(((TwitterAdsDto) session.getAttribute("budget")).getDailyBudget());
        twitterAdsDto.setTotalBudget(((TwitterAdsDto) session.getAttribute("budget")).getTotalBudget());

        // tweetList取得
        List<TwitterTweet> tweetList = twitterCampaignApiService.getTweetList(twitterAdsDto);
        twitterAdsDto.setTweetList(tweetList);

        mv.addObject("twitterAdsDto", twitterAdsDto);
        mv.setViewName("campaign/twitter/createConfirm");
        return mv;
    }

    // 成功
    @GetMapping("/createSuccess")
    public ModelAndView createSuccess(ModelAndView mv) {

        // コードマスタを読込
        getKeywordNameList();

        TwitterAdsDto twitterAdsDto = new TwitterAdsDto();

        // 広告タイプをsessionから取得
        twitterAdsDto.setObjective((Integer) session.getAttribute("objective"));
        // ツイートIdリストをsessionから取得する
        twitterAdsDto.setTweetIdList((List<String>) session.getAttribute("tweetIdList"));
        // ツイートJリストをsessionから取得する
        twitterAdsDto.setTweetList((List<TwitterTweet>) session.getAttribute("tweetList"));
        // 地域をsessionから取得する
        twitterAdsDto.setLocation(((TwitterAdsDto) session.getAttribute("area")).getLocation());
        twitterAdsDto.setRegions(((TwitterAdsDto) session.getAttribute("area")).getRegions());
        // 日程をsessionから取得する
        twitterAdsDto.setStartTime(((TwitterAdsDto) session.getAttribute("deliveryTime")).getStartTime());
        twitterAdsDto.setEndTime(((TwitterAdsDto) session.getAttribute("deliveryTime")).getEndTime());
        // 予算をsessionから取得する
        twitterAdsDto.setDailyBudget(((TwitterAdsDto) session.getAttribute("budget")).getDailyBudget());
        twitterAdsDto.setTotalBudget(((TwitterAdsDto) session.getAttribute("budget")).getTotalBudget());
        // キャンペーン名をsessionから取得する
        twitterAdsDto.setCampaignName(((TwitterAdsDto) session.getAttribute("budget")).getCampaignName());

        log.debug("-----------------------------------------------------");
        log.debug("Success：広告Params：" + twitterAdsDto.toString());
        log.debug("-----------------------------------------------------");

        twitterCampaignApiService.createAds(twitterAdsDto, null);
        // オペレーションログ記録
        operationService.create(Operation.TWITTER_CAMPAIGN_CREATE.getValue(),
                String.valueOf(twitterAdsDto.getCampaignId()));

        mv.setViewName("campaign/twitter/createSuccess");
        return mv;
    }

    // DBからすべての都道府県を検索
    private void searchRegions() {
        if (CodeMasterServiceImpl.twitterRegionList == null) {
            codeMasterService.searchRegions();
        }
    }

    // DBからすべてのキーワードを検索
    private void getKeywordNameList() {
        if (CodeMasterServiceImpl.keywordNameList == null) {
            codeMasterService.getKeywordNameList();
        }
    }

}
