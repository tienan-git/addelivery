package jp.acepro.haishinsan.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.dto.twitter.TwitterAdsDto;
import jp.acepro.haishinsan.dto.twitter.TwitterCampaignData;
import jp.acepro.haishinsan.dto.twitter.TwitterDisplayReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterGraphReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTemplateDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.enums.DateFormatter;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.PeriodSet;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.enums.TwitterLocationType;
import jp.acepro.haishinsan.enums.TwitterObjective;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.TwitterAdsInputForm;
import jp.acepro.haishinsan.form.TwitterReportInputForm;
import jp.acepro.haishinsan.form.TwitterTemplateInputForm;
import jp.acepro.haishinsan.mapper.TwitterMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.issue.TwitterReportingService;
import jp.acepro.haishinsan.service.twitter.TwitterApiService;
import jp.acepro.haishinsan.service.twitter.TwitterTemplateService;
import jp.acepro.haishinsan.util.TwitterUtil;
import jp.acepro.haishinsan.util.Utf8BomUtil;

@Controller
@RequestMapping("/twitter_bak")
public class TwitterController {

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    HttpSession session;

    @Autowired
    CodeMasterService codeMasterService;

    @Autowired
    TwitterApiService twitterApiService;

    @Autowired
    TwitterTemplateService twitterTemplateService;

    @Autowired
    TwitterReportingService twitterReportingService;

    @Autowired
    OperationService operationService;

    /**
     * ---------------------------------------- キャンペーン管理
     * ----------------------------------------
     **/
    // 広告作成
    @GetMapping("/createAds")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_CAMPAIGN_MANAGE + "')")
    public ModelAndView adsCreate(@ModelAttribute TwitterAdsInputForm twitterAdsInputForm) {

        ModelAndView modelAndView = new ModelAndView();

        // コードマスタを読込
        searchRegions();

        // WebSite & Follower TweetsListを取得
        List<TwitterTweet> websiteTweetList = twitterApiService.searchWebsiteTweets();
        List<TwitterTweet> followersTweetList = twitterApiService.searchFollowersTweets();

        session.setAttribute("websiteTweetList", websiteTweetList);
        session.setAttribute("followersTweetList", followersTweetList);

        // APIから取得したTweetsListをDBに保存
//		twitterApiService.saveTweetList(websiteTweetList, followersTweetList);

        // WebSite & Follower 一覧表示
        twitterAdsInputForm.setWebsiteTweetList(websiteTweetList);
        twitterAdsInputForm.setFollowersTweetList(followersTweetList);

        // テンプレート読込
        List<TwitterTemplateDto> twitterTemplateDtoList = twitterTemplateService.templateList();
        twitterAdsInputForm.setTemplateList(twitterTemplateDtoList);
        if (twitterTemplateDtoList.isEmpty() == false) {
            twitterAdsInputForm.setCampaignName(twitterTemplateDtoList.get(0).getCampaignName());
            twitterAdsInputForm.setStartTime(twitterTemplateDtoList.get(0).getStartTime());
            twitterAdsInputForm.setEndTime(twitterTemplateDtoList.get(0).getEndTime());
            twitterAdsInputForm.setDailyBudget(twitterTemplateDtoList.get(0).getDailyBudget());
            twitterAdsInputForm.setTotalBudget(twitterTemplateDtoList.get(0).getTotalBudget());
            twitterAdsInputForm.setLocation(Integer.valueOf(twitterTemplateDtoList.get(0).getLocation()));
            twitterAdsInputForm.setRegions(twitterTemplateDtoList.get(0).getRegions());
        }

        // キャンペーン目的初期値設定
        twitterAdsInputForm.setObjective(TwitterObjective.WEBSITE.getValue());
        // 地域初期値設定
        twitterAdsInputForm.setLocation(TwitterLocationType.ALLCITY.getValue());
        modelAndView.setViewName("twitter/createAds");
        modelAndView.addObject("twitterTemplateDtoList", twitterTemplateDtoList);
        modelAndView.addObject("twitterAdsDto", new TwitterAdsDto());
        return modelAndView;
    }

    // 広告作成完了
    @PostMapping("/completeAds")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_CAMPAIGN_MANAGE + "')")
    public ModelAndView adsConplete(@Validated TwitterAdsInputForm twitterAdsInputForm, BindingResult result) {

        // コードマスタを読込
        getKeywordNameList();

        // Form → Dto
        TwitterAdsDto twitterAdsDto = TwitterMapper.INSTANCE.adsFormToDto(twitterAdsInputForm);
        ModelAndView modelAndView = new ModelAndView();

        try {
            // 広告作成
            twitterApiService.createAds(twitterAdsDto, null);
        } catch (BusinessException e) {
            // 異常時レスポンス
            result.reject(e.getMessage(),
                    Objects.isNull(e.getParams()) ? null : new Object[] { e.getParams()[0] + "円" }, null);
            // System.out.println("e.getParams()" + e.getParams());
            List<TwitterTweet> websiteTweetList = (List<TwitterTweet>) session.getAttribute("websiteTweetList");
            List<TwitterTweet> followersTweetList = (List<TwitterTweet>) session.getAttribute("followersTweetList");
            twitterAdsInputForm.setWebsiteTweetList(websiteTweetList);
            twitterAdsInputForm.setFollowersTweetList(followersTweetList);
            // テンプレート読込
            List<TwitterTemplateDto> twitterTemplateDtoList = twitterTemplateService.templateList();
            twitterAdsInputForm.setTemplateList(twitterTemplateDtoList);
            modelAndView.setViewName("twitter/createAds");
            modelAndView.addObject("twitterAdsInputForm", twitterAdsInputForm);
            modelAndView.addObject("twitterTemplateDtoList", twitterTemplateDtoList);
            modelAndView.addObject("twitterAdsDto", twitterAdsDto);
            return modelAndView;
        }

        switch (TwitterObjective.of(twitterAdsDto.getObjective())) {
        // キャンペーン目的がwebsiteの場合
        case WEBSITE:
            List<TwitterTweet> selectedWebsiteTweetList = twitterApiService.searchWebsiteTweetsById(twitterAdsDto);
            twitterAdsDto.setWebsiteTweetList(selectedWebsiteTweetList);
            twitterAdsInputForm.setWebsiteTweetList(selectedWebsiteTweetList);
            break;
        // キャンペーン目的がfollowersの場合
        case FOLLOWER:
            List<TwitterTweet> selectedfollowersTweetList = twitterApiService.searchFollowersTweetsById(twitterAdsDto);
            twitterAdsDto.setWebsiteTweetList(selectedfollowersTweetList);
            twitterAdsInputForm.setFollowersTweetList(selectedfollowersTweetList);
            break;
        }
        // 正常時レスポンス
        modelAndView.setViewName("twitter/completeAds");
        modelAndView.addObject("twitterAdsDto", twitterAdsDto);

        // キャンペーン作成成功したらツイートリストをsessionから削除
        session.removeAttribute("websiteTweetList");
        session.removeAttribute("followersTweetList");

        // オペレーションログ記録
        operationService.create(Operation.TWITTER_CAMPAIGN_CREATE.getValue(),
                String.valueOf(twitterAdsDto.getCampaignId()));
        return modelAndView;
    }

    // 広告リスト
    @GetMapping("/adsList")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_CAMPAIGN_VIEW + "')")
    public ModelAndView adsList() {

        // コードマスタをメモリへロード
        searchRegions();

        ModelAndView modelAndView = new ModelAndView();
        List<TwitterCampaignData> twitterCampaignDataResList = twitterApiService.adsList();

        // 詳細画面で保存したデーターをsessionから削除
        session.removeAttribute("twitterCampaignDetail");

        // 正常時レスポンス
        modelAndView.setViewName("twitter/adsList");
        modelAndView.addObject("twitterCampaignDataResList", twitterCampaignDataResList);

        // オペレーションログ記録
        operationService.create(Operation.TWITTER_CAMPAIGN_LIST.getValue(), String.valueOf(""));
        return modelAndView;
    }

    // 広告詳細
    @GetMapping("/adsDetail")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_CAMPAIGN_VIEW + "')")
    public ModelAndView adsDetail(@RequestParam String groupId, @RequestParam String campaignId) {

        ModelAndView modelAndView = new ModelAndView();
        TwitterCampaignData twitterCampaignData = twitterApiService.searchCampaignById(groupId, campaignId);

        // sessionに一時的保存
        session.setAttribute("twitterCampaignDetail", twitterCampaignData);

        modelAndView.setViewName("twitter/adsDetail");
        modelAndView.addObject("twitterCampaignData", twitterCampaignData);

        // オペレーションログ記録
        operationService.create(Operation.TWITTER_CAMPAIGN_VIEW.getValue(), String.valueOf(campaignId));
        return modelAndView;
    }

    // 広告削除
    @PostMapping("/deleteAds")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_CAMPAIGN_MANAGE + "')")
    public ModelAndView deleteAds(@RequestParam String campaignId) {

        ModelAndView modelAndView = new ModelAndView();
        twitterApiService.deleteAds(campaignId);

        // キャンペーン詳細をsessionから取得
        TwitterCampaignData twitterCampaignData = (TwitterCampaignData) session.getAttribute("twitterCampaignDetail");

        modelAndView.setViewName("twitter/deleteAds");
        modelAndView.addObject("twitterCampaignData", twitterCampaignData);

        // オペレーションログ記録
        operationService.create(Operation.TWITTER_CAMPAIGN_DELETE.getValue(), String.valueOf(campaignId));
        return modelAndView;
    }

    /**
     * ---------------------------------------- テンプレート管理
     * ----------------------------------------
     **/

    // テンプレート作成
    @GetMapping("/createTemplate")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_TEMPLATE_MANAGE + "')")
    public ModelAndView templateaCreate(@ModelAttribute TwitterTemplateInputForm twittertemplateInputForm) {

        // コードマスタをメモリへロード
        searchRegions();

        ModelAndView modelAndView = new ModelAndView();

        // -------- 地域初期値設定 --------
        twittertemplateInputForm.setLocation(TwitterLocationType.ALLCITY.getValue());
        // レスポンス
        modelAndView.setViewName("twitter/createTemplate");

        return modelAndView;
    }

    // テンプレート作成完了
    @PostMapping("/completeTemplate")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_TEMPLATE_MANAGE + "')")
    public ModelAndView templateComplete(@Validated TwitterTemplateInputForm twitterTemplateInputForm,
            BindingResult result) {

        TwitterTemplateDto twitterTemplateDto = TwitterMapper.INSTANCE.tempFormToDto(twitterTemplateInputForm);
        ModelAndView modelAndView = new ModelAndView();
        try {
            twitterTemplateService.createTemplate(twitterTemplateDto);
        } catch (BusinessException e) {
            // 異常時レスポンス
            result.reject(e.getMessage());
            modelAndView.setViewName("twitter/createTemplate");
            modelAndView.addObject("twitterTemplateDto", twitterTemplateDto);
            modelAndView.addObject("twitterTemplateInputForm", twitterTemplateInputForm);
            return modelAndView;
        }
        // 正常時レスポンス
        modelAndView.setViewName("twitter/completeTemplate");
        modelAndView.addObject("twitterTemplateDto", twitterTemplateDto);

        // オペレーションログ記録
        operationService.create(Operation.TWITTER_TEMPLATE_CREATE.getValue(),
                String.valueOf(twitterTemplateDto.getTemplateId()));

        return modelAndView;
    }

    // テンプレートリスト
    @GetMapping("/templateList")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_TEMPLATE_VIEW + "')")
    public ModelAndView templateList() {

        // コードマスタをメモリへロード
        searchRegions();

        ModelAndView modelAndView = new ModelAndView();
        List<TwitterTemplateDto> twitterTemplateDtoList = twitterTemplateService.templateList();
        modelAndView.setViewName("twitter/templateList");
        modelAndView.addObject("twitterTemplateDtoList", twitterTemplateDtoList);

        // オペレーションログ記録
        operationService.create(Operation.TWITTER_TEMPLATE_LIST.getValue(), String.valueOf(""));
        return modelAndView;
    }

    // テンプレート詳細
    @GetMapping("/templateDetail")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_TEMPLATE_VIEW + "')")
    public ModelAndView templateDetail(@RequestParam Long templateId) {

        ModelAndView modelAndView = new ModelAndView();
        TwitterTemplateDto twitterTemplateDto = twitterTemplateService.templateDetail(templateId);
        modelAndView.setViewName("twitter/templateDetail");
        modelAndView.addObject("twitterTemplateDto", twitterTemplateDto);

        // オペレーションログ記録
        operationService.create(Operation.TWITTER_TEMPLATE_VIEW.getValue(), String.valueOf(templateId));
        return modelAndView;
    }

    // テンプレート更新
    @PostMapping("/updateTemplate")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_TEMPLATE_MANAGE + "')")
    public ModelAndView templateUpdate(@RequestParam Long templateId) {

        TwitterTemplateDto twitterTemplateDto = twitterTemplateService.templateDetail(templateId);
        TwitterTemplateInputForm twitterTemplateInputForm = TwitterMapper.INSTANCE.tempDtoToForm(twitterTemplateDto);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("twitter/updateTemplate");
        modelAndView.addObject("twitterTemplateInputForm", twitterTemplateInputForm);
        return modelAndView;
    }

    // テンプレート更新完了
    @PostMapping("/updateTemplateComplete")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_TEMPLATE_MANAGE + "')")
    public ModelAndView templateUpdateComplete(@Validated TwitterTemplateInputForm twitterTemplateInputForm,
            BindingResult result) {

        TwitterTemplateDto twitterTemplateDto = TwitterMapper.INSTANCE.tempFormToDto(twitterTemplateInputForm);
        ModelAndView modelAndView = new ModelAndView();
        try {
            twitterTemplateService.templateUpdate(twitterTemplateDto);
        } catch (BusinessException e) {
            // 異常時レスポンス
            result.reject(e.getMessage());
            modelAndView.setViewName("twitter/updateTemplate");
            modelAndView.addObject("twitterTemplateInputForm", twitterTemplateInputForm);
            return modelAndView;
        }
        // 正常時レスポンス
        modelAndView.setViewName("twitter/updateTemplateComplete");
        modelAndView.addObject("twitterTemplateDto", twitterTemplateDto);

        // オペレーションログ記録
        operationService.create(Operation.TWITTER_TEMPLATE_UPDATE.getValue(),
                String.valueOf(twitterTemplateInputForm.getTemplateId()));
        return modelAndView;
    }

    // テンプレート削除
    @PostMapping("/deleteTemplate")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_TEMPLATE_MANAGE + "')")
    public ModelAndView templateDelete(@RequestParam Long templateId) {

        TwitterTemplateDto twitterTemplateDto = twitterTemplateService.templateDelete(templateId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("twitter/deleteTemplate");
        modelAndView.addObject("twitterTemplateDto", twitterTemplateDto);
        // オペレーションログ記録
        operationService.create(Operation.TWITTER_TEMPLATE_UPDATE.getValue(), String.valueOf(templateId));
        return modelAndView;

    }

    /**
     * ---------------------------------------- レポート管理
     * ----------------------------------------
     **/
    @GetMapping("/reporting")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_REPORT_VIEW + "')")
    public ModelAndView reporting(@ModelAttribute TwitterReportInputForm twitterReportInputForm) {

        // Batch Reporting : テスト
        // LocalDate today = LocalDate.now();
        // twitterReportingService.getReport(today);

        // コードマスタをメモリへロード
        searchRegions();

        // 期間設定初期値
        twitterReportInputForm.setPeriod(PeriodSet.WHOLE.getValue());

        List<TwitterCampaignData> twitterCampaignDataList = twitterApiService.adsList();
        ModelAndView modelAndView = new ModelAndView();
        // レスポンス
        modelAndView.setViewName("twitter/reporting");
        modelAndView.addObject("twitterCampaignDataList", twitterCampaignDataList);
        modelAndView.addObject("twitterGraphReportDto", new TwitterGraphReportDto());

        // オペレーションログ記録
        operationService.create(Operation.TWITTER_REPORT_VIEW.getValue(), String.valueOf(""));
        return modelAndView;
    }

    @PostMapping("/reportingList")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_REPORT_VIEW + "')")
    public ModelAndView reporting(TwitterReportInputForm twitterReportInputForm, @RequestParam Integer reportType) {

        List<TwitterCampaignData> twitterCampaignDataList = twitterApiService.adsList();

        List<String> campaignIdList = TwitterUtil.formatStringToList(twitterReportInputForm.getCampaignIds());
        // 検索条件
        TwitterReportDto twitterReportDto = new TwitterReportDto();
        twitterReportDto.setCampaignIdList(campaignIdList);
        twitterReportDto.setStartDate(twitterReportInputForm.getStartDate());
        twitterReportDto.setEndDate(twitterReportInputForm.getEndDate());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("twitter/reporting");

        List<TwitterDisplayReportDto> twitterReportDisplayDtoList = new ArrayList<>();
        TwitterGraphReportDto twitterGraphReportDto = new TwitterGraphReportDto();
        List<String> campaignIds = new ArrayList<>();
        // レポートタイプ
        switch (ReportType.of(reportType)) {
        // デバイス別の場合
        case DEVICE:
            // Table用
            twitterReportDisplayDtoList = twitterReportingService.getTwitterDeviceReporting(twitterReportDto);
            campaignIds = getCampaignIdList(twitterReportDisplayDtoList, campaignIdList, twitterCampaignDataList);
            // Graph用
            twitterGraphReportDto = twitterReportingService.getTwitterDeviceReportingGraph(twitterReportDto);
            // オペレーションログ記録
            operationService.create(Operation.TWITTER_DEVICE_REPORT_VIEW.getValue(), String.valueOf(""));
            modelAndView.addObject("reportType", ReportType.DEVICE.getValue());
            break;
        // 地域別の場合
        case REGIONS:
            // Table用
            twitterReportDisplayDtoList = twitterReportingService.getTwitterRegionReporting(twitterReportDto);
            campaignIds = getCampaignIdList(twitterReportDisplayDtoList, campaignIdList, twitterCampaignDataList);
            // Graph用
            twitterGraphReportDto = twitterReportingService.getTwitterRegionReportingGraph(twitterReportDto);
            // オペレーションログ記録
            operationService.create(Operation.TWITTER_REGION_REPORT_VIEW.getValue(), String.valueOf(""));
            modelAndView.addObject("reportType", ReportType.REGIONS.getValue());
            break;
        // 日別の場合
        case DATE:
            // Table用
            twitterReportDisplayDtoList = twitterReportingService.getTwitterDayReporting(twitterReportDto);
            campaignIds = getCampaignIdList(twitterReportDisplayDtoList, campaignIdList, twitterCampaignDataList);
            // Graph用
            twitterGraphReportDto = twitterReportingService.getTwitterDayReportingGraph(twitterReportDto);
            // オペレーションログ記録
            operationService.create(Operation.TWITTER_DATE_REPORT_VIEW.getValue(), String.valueOf(""));
            modelAndView.addObject("reportType", ReportType.DATE.getValue());
            break;
        case CREATIVE:
            break;
        }
        modelAndView.addObject("campaignIds", campaignIds);
        modelAndView.addObject("twitterGraphReportDto", twitterGraphReportDto);
        modelAndView.addObject("twitterReportDisplayDtoList", twitterReportDisplayDtoList);
        modelAndView.addObject("twitterReportInputForm", twitterReportInputForm);
        modelAndView.addObject("twitterCampaignDataList", twitterCampaignDataList);

        return modelAndView;
    }

    // CSVダウンロード
    @PostMapping("/download")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_REPORT_VIEW + "')")
    public ResponseEntity<byte[]> download(@ModelAttribute TwitterReportInputForm twitterReportInputForm,
            @RequestParam Integer reportType) throws IOException {

        // 検索条件を集める
        TwitterReportDto twitterReportDto = new TwitterReportDto();
        twitterReportDto.setCampaignIdList(TwitterUtil.formatStringToList(twitterReportInputForm.getCampaignIds()));
        twitterReportDto.setStartDate(twitterReportInputForm.getStartDate());
        twitterReportDto.setEndDate(twitterReportInputForm.getEndDate());
        twitterReportDto.setReportType(reportType);
        // CSVファイル中身を取得し、文字列にする
        String file = twitterReportingService.download(twitterReportDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", applicationProperties.getContentTypeCsvDownload());
        String fimeName = "Twitter_REPORT" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
        httpHeaders.setContentDispositionFormData("filename", fimeName);

        // オペレーションログ記録
        switch (ReportType.of(reportType)) {
        case DEVICE:
            operationService.create(Operation.TWITTER_DEVICE_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
            break;
        case REGIONS:
            operationService.create(Operation.TWITTER_REGION_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
            break;
        case DATE:
            operationService.create(Operation.TWITTER_DATE_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
            break;
        }
        return new ResponseEntity<>(Utf8BomUtil.utf8ToWithBom(file), httpHeaders, HttpStatus.OK);
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

    // キャンペーンIdリスト
    private List<String> getCampaignIdList(List<TwitterDisplayReportDto> twitterReportDisplayDtoList,
            List<String> selectedCampaignIdList, List<TwitterCampaignData> twitterCampaignDataList) {
        List<String> campaignIdList = new ArrayList<>();
        Set<String> set = new HashSet<>();
        List<String> newCampaignIdList = new ArrayList<>();
        // すべてのcampaignIdを選択状態にする
        if (selectedCampaignIdList == null) {
            for (TwitterCampaignData twitterCampaignData : twitterCampaignDataList) {
                newCampaignIdList.add(twitterCampaignData.getId());
            }
            // 選択状態のcampaginIdリスト
        } else {
            for (TwitterDisplayReportDto twitterDisplayReportDto : twitterReportDisplayDtoList) {
                campaignIdList.add(twitterDisplayReportDto.getCampaignId());
            }
            campaignIdList.addAll(selectedCampaignIdList);
            // "合計"を削除
            campaignIdList.remove("合計");
            // 重複キャンペーンIDを削除
            for (String obj : campaignIdList) {
                if (set.add(obj)) {
                    newCampaignIdList.add(obj);
                }
            }
        }
        return newCampaignIdList;
    };

}
