package jp.acepro.haishinsan.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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
import jp.acepro.haishinsan.bean.YahooCsvBean;
import jp.acepro.haishinsan.db.entity.YahooCampaignManage;
import jp.acepro.haishinsan.dto.dsp.DspSegmentDto;
import jp.acepro.haishinsan.dto.yahoo.YahooGraphReportDto;
import jp.acepro.haishinsan.dto.yahoo.YahooIssueDto;
import jp.acepro.haishinsan.dto.yahoo.YahooLocationDto;
import jp.acepro.haishinsan.dto.yahoo.YahooReportDisplayDto;
import jp.acepro.haishinsan.enums.AdvDestination;
import jp.acepro.haishinsan.enums.DateFormatter;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.PeriodSet;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.YahooCsvInputForm;
import jp.acepro.haishinsan.form.YahooIssueinputForm;
import jp.acepro.haishinsan.form.YahooReportInputForm;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.yahoo.YahooService;
import jp.acepro.haishinsan.util.Utf8BomUtil;

@Controller
@RequestMapping("/yahoo_bak")
public class YahooController {

    @Autowired
    HttpSession session;

    @Autowired
    YahooService yahooService;

    @Autowired
    OperationService operationService;

    @GetMapping("/csvUpload")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_CSV_UPLOAD + "')")
    public ModelAndView csvUpload(@Validated YahooCsvInputForm yahooCsvInputForm, ModelAndView mv) {

        mv.addObject("yahooCsvInputForm", yahooCsvInputForm);
        mv.setViewName("yahoo/csvUpload");
        return mv;
    }

    @PostMapping("/csvUploadConfirm")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_CSV_UPLOAD + "')")
    public ModelAndView csvUploadConfirm(@Validated YahooCsvInputForm yahooCsvInputForm, BindingResult result,
            ModelAndView mv) {

        List<YahooCsvBean> yahooCsvBeanList = new ArrayList<YahooCsvBean>();
        try {

            yahooCsvBeanList = yahooService.readCsv(yahooCsvInputForm.getCsvFile());
        } catch (BusinessException be) {
            result.reject(be.getMessage(), be.getParams(), null);
            mv.addObject("yahooCsvInputForm", yahooCsvInputForm);
            mv.setViewName("yahoo/csvUpload");
            return mv;
        }

        yahooCsvInputForm.setFileName(yahooCsvInputForm.getCsvFile().getOriginalFilename());
        yahooCsvInputForm.setYahooCsvBeanList(yahooCsvBeanList);

        session.setAttribute("yahooCsvBeanList", yahooCsvBeanList);

        mv.addObject("yahooCsvInputForm", yahooCsvInputForm);
        mv.setViewName("yahoo/csvUploadConfirm");

        return mv;
    }

    @PostMapping("/csvUploadComplete")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_CSV_UPLOAD + "')")
    public ModelAndView csvUploadComplete(@ModelAttribute YahooCsvInputForm yahooCsvInputForm, ModelAndView mv)
            throws IOException {

        List<YahooCsvBean> yahooCsvBeanList = (List<YahooCsvBean>) session.getAttribute("yahooCsvBeanList");
        yahooService.uploadData(yahooCsvBeanList);

        session.removeAttribute("yahooCsvBeanList");
        mv.setViewName("yahoo/csvUploadComplete");

        // オペレーションログ記録
        operationService.create(Operation.YAHOO_CSV_UPLOAD.getValue(), "ファイル名：" + yahooCsvInputForm.getFileName());
        return mv;
    }

    @GetMapping("/reporting")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_REPORT_VIEW + "')")
    public ModelAndView reporting(@ModelAttribute YahooReportInputForm yahooReportInputForm, ModelAndView mv) {

        List<Pair<String, String>> campaignPairList = getCampaignPairList();
        yahooReportInputForm.setCampaignList(campaignPairList);

        // 期間設定初期値
        yahooReportInputForm.setPeriod(PeriodSet.WHOLE.getValue());

        YahooGraphReportDto yahooGraphReportDto = new YahooGraphReportDto();

        mv.addObject("yahooGraphReportDto", yahooGraphReportDto);
        mv.setViewName("yahoo/reporting");

        return mv;
    }

    @GetMapping("/deviceReport")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_REPORT_VIEW + "')")
    public ModelAndView getDeviceReport(@ModelAttribute YahooReportInputForm yahooReportInputForm, ModelAndView mv) {

        List<String> campaignIdList = new ArrayList<String>();

        List<Pair<String, String>> campaignPairList = getCampaignPairList();

        if (yahooReportInputForm.getCampaignIdList() == null || yahooReportInputForm.getCampaignIdList().isEmpty()) {
            // 全てを選択する場合、該当店舗の全キャンペーンのレポートを取得する
            for (Pair<String, String> yahooCampaignList : campaignPairList) {
                campaignIdList.add(yahooCampaignList.getFirst());
            }
        } else {
            campaignIdList = yahooReportInputForm.getCampaignIdList();
        }
        String startDate = null;
        String endDate = null;
        if (PeriodSet.LIMITED.getValue().equals(yahooReportInputForm.getPeriod())) {
            startDate = yahooReportInputForm.getStartDate();
            endDate = yahooReportInputForm.getEndDate();
        }

        List<YahooReportDisplayDto> yahooReportDisplayDtoList = yahooService.getDeviceReport(campaignIdList, startDate,
                endDate);
        // Graph用
        YahooGraphReportDto yahooGraphReportDto = yahooService.getYahooDeviceReportingGraph(campaignIdList, startDate,
                endDate);

        yahooReportInputForm.setCampaignList(campaignPairList);
        yahooReportInputForm.setCampaignIdList(campaignIdList);
        mv.setViewName("yahoo/reporting");
        mv.addObject("yahooReportInputForm", yahooReportInputForm);
        mv.addObject("yahooGraphReportDto", yahooGraphReportDto);
        mv.addObject("yahooReportDisplayDtoList", yahooReportDisplayDtoList);
        mv.addObject("reportType", ReportType.DEVICE.getValue());

        // オペレーションログ記録
        String campaignIds = String.join(",", campaignIdList);
        startDate = startDate == null ? "なし" : startDate;
        endDate = endDate == null ? "なし" : endDate;
        operationService.create(Operation.YAHOO_DEVICE_REPORT_VIEW.getValue(),
                "案件ID：" + campaignIds + " 開始日：" + startDate + " 終了日：" + endDate);

        return mv;
    }

    @GetMapping("/regionReport")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_REPORT_VIEW + "')")
    public ModelAndView getRegionReport(@ModelAttribute YahooReportInputForm yahooReportInputForm, ModelAndView mv) {

        List<String> campaignIdList = new ArrayList<String>();

        List<Pair<String, String>> campaignPairList = getCampaignPairList();

        if (yahooReportInputForm.getCampaignIdList() == null || yahooReportInputForm.getCampaignIdList().isEmpty()) {
            // 全てを選択する場合、該当店舗の全キャンペーンのレポートを取得する
            for (Pair<String, String> yahooCampaignList : campaignPairList) {
                campaignIdList.add(yahooCampaignList.getFirst());
            }
        } else {
            campaignIdList = yahooReportInputForm.getCampaignIdList();
        }
        String startDate = null;
        String endDate = null;
        if (PeriodSet.LIMITED.getValue().equals(yahooReportInputForm.getPeriod())) {
            startDate = yahooReportInputForm.getStartDate();
            endDate = yahooReportInputForm.getEndDate();
        }

        List<YahooReportDisplayDto> yahooReportDisplayDtoList = yahooService.getRegionReport(campaignIdList, startDate,
                endDate);
        // Graph用
        YahooGraphReportDto yahooGraphReportDto = yahooService.getYahooRegionReportingGraph(campaignIdList, startDate,
                endDate);

        yahooReportInputForm.setCampaignList(campaignPairList);
        mv.setViewName("yahoo/reporting");
        mv.addObject("yahooReportInputForm", yahooReportInputForm);
        mv.addObject("yahooGraphReportDto", yahooGraphReportDto);
        mv.addObject("yahooReportDisplayDtoList", yahooReportDisplayDtoList);
        mv.addObject("reportType", ReportType.REGIONS.getValue());

        // オペレーションログ記録
        String campaignIds = String.join(",", campaignIdList);
        startDate = startDate == null ? "なし" : startDate;
        endDate = endDate == null ? "なし" : endDate;
        operationService.create(Operation.YAHOO_REGION_REPORT_VIEW.getValue(),
                "案件ID：" + campaignIds + " 開始日：" + startDate + " 終了日：" + endDate);

        return mv;
    }

    @GetMapping("/dateReport")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_REPORT_VIEW + "')")
    public ModelAndView getDateReport(@ModelAttribute YahooReportInputForm yahooReportInputForm, ModelAndView mv) {

        List<String> campaignIdList = new ArrayList<String>();

        List<Pair<String, String>> campaignPairList = getCampaignPairList();

        if (yahooReportInputForm.getCampaignIdList() == null || yahooReportInputForm.getCampaignIdList().isEmpty()) {
            // 全てを選択する場合、該当店舗の全キャンペーンのレポートを取得する
            for (Pair<String, String> yahooCampaignList : campaignPairList) {
                campaignIdList.add(yahooCampaignList.getFirst());
            }
        } else {
            campaignIdList = yahooReportInputForm.getCampaignIdList();
        }
        String startDate = null;
        String endDate = null;
        if (PeriodSet.LIMITED.getValue().equals(yahooReportInputForm.getPeriod())) {
            startDate = yahooReportInputForm.getStartDate();
            endDate = yahooReportInputForm.getEndDate();
        }

        List<YahooReportDisplayDto> yahooReportDisplayDtoList = yahooService.getDateReport(campaignIdList, startDate,
                endDate);
        // Graph用
        YahooGraphReportDto yahooGraphReportDto = yahooService.getYahooDateReportingGraph(campaignIdList, startDate,
                endDate);

        yahooReportInputForm.setCampaignList(campaignPairList);
        mv.setViewName("yahoo/reporting");
        mv.addObject("yahooReportInputForm", yahooReportInputForm);
        mv.addObject("yahooGraphReportDto", yahooGraphReportDto);
        mv.addObject("yahooReportDisplayDtoList", yahooReportDisplayDtoList);
        mv.addObject("reportType", ReportType.DATE.getValue());

        // オペレーションログ記録
        String campaignIds = String.join(",", campaignIdList);
        startDate = startDate == null ? "なし" : startDate;
        endDate = endDate == null ? "なし" : endDate;
        operationService.create(Operation.YAHOO_DATE_REPORT_VIEW.getValue(),
                "案件ID：" + campaignIds + " 開始日：" + startDate + " 終了日：" + endDate);

        return mv;
    }

    @Autowired
    CodeMasterService codeMasterService;

    @GetMapping("/issueCreate")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_CAMPAIGN_REQUEST + "')")
    public ModelAndView createIssue(@ModelAttribute YahooIssueinputForm yahooIssueinputForm, ModelAndView mv) {

        // コードマスタをメモリへロード
        getYahooAreaList();

        List<DspSegmentDto> segmentList = yahooService.searchSegmentList();

        List<Pair<Integer, String>> segmentPairList = new ArrayList<Pair<Integer, String>>();
        for (DspSegmentDto dspSegmentDto : segmentList) {
            segmentPairList.add(Pair.of(dspSegmentDto.getSegmentId(), dspSegmentDto.getUrl()));
        }

        yahooIssueinputForm.setSegmentList(segmentPairList);
        yahooIssueinputForm.setAdvDestination(AdvDestination.IMAGE.getValue());

        mv.setViewName("yahoo/issueCreate");
        mv.addObject("yahooIssueinputForm", yahooIssueinputForm);
        return mv;

    }

//    @PostMapping("/issueCreateComplete")
//    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_CAMPAIGN_REQUEST + "')")
//    public ModelAndView createIssueComplete(@ModelAttribute YahooIssueinputForm yahooIssueinputForm,
//            BindingResult result, ModelAndView mv) throws Exception {
//
//        List<MultipartFile> imageList = null;
//        String imageName = null;
//        String url = null;
//        String adShortTitle = null;
//        String adTitle1 = null;
//        String adTitle2 = null;
//        String adDescription = null;
//        switch (AdvDestination.of(yahooIssueinputForm.getAdvDestination())) {
//        case RESPONSIVE:
//            // infeedの場合、デバイスタイプをモバイルで固定する
//            yahooIssueinputForm.setDeviceType(DeviceType.MOBILE.getValue());
//            adShortTitle = yahooIssueinputForm.getInfeedAdShortTitle();
//            adDescription = yahooIssueinputForm.getInfeedAdDescription();
//            url = yahooIssueinputForm.getInfeedAdFinalPageUrl();
//            imageList = yahooIssueinputForm.getInfeedAdImageFileList();
//            break;
//        case LISTING:
//            adTitle1 = yahooIssueinputForm.getListingAdTitle1();
//            adTitle2 = yahooIssueinputForm.getListingAdTitle2();
//            adDescription = yahooIssueinputForm.getListingAdDescription();
//            url = yahooIssueinputForm.getListingAdFinalPageUrl();
//            break;
//        case IMAGE:
//            url = yahooIssueinputForm.getTargetAdFinalPageUrl();
//            imageList = yahooIssueinputForm.getTargetAdImageFileList();
//            break;
//        }
//        YahooIssueDto yahooIssueDto = YahooMapper.INSTANCE.map(yahooIssueinputForm);
//        List<Long> locationIdList = yahooIssueinputForm.getLocationIdList();
//        if (locationIdList.isEmpty()) {
//            result.reject(ErrorCodeConstant.E60001);
//
//            List<DspSegmentDto> segmentList = yahooService.searchSegmentList();
//
//            List<Pair<Integer, String>> segmentPairList = new ArrayList<Pair<Integer, String>>();
//            for (DspSegmentDto dspSegmentDto : segmentList) {
//                segmentPairList.add(Pair.of(dspSegmentDto.getSegmentId(), dspSegmentDto.getUrl()));
//            }
//
//            yahooIssueinputForm.setSegmentList(segmentPairList);
//
//            mv.addObject("yahooIssueinputForm", yahooIssueinputForm);
//            mv.setViewName("yahoo/issueCreate");
//            return mv;
//        }
//        List<YahooLocationDto> locationList = yahooService.getLocationList(locationIdList);
//        yahooIssueDto.setLocationList(locationList);
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < locationIdList.size(); i++) {
//            sb.append(locationIdList.get(i));
//            if (i != locationIdList.size() - 1) {
//                sb.append(",");
//            }
//        }
//
//        // 画像名を','で連接する
//        if (Objects.nonNull(imageList)) {
//            imageName = imageList.stream().map(image -> image.getOriginalFilename()).collect(Collectors.joining(","));
//        }
//        yahooIssueDto.setLocationIds(sb.toString());
//        yahooIssueDto.setImageName(imageName);
//        yahooIssueDto.setAdShortTitle(adShortTitle);
//        yahooIssueDto.setAdTitle1(adTitle1);
//        yahooIssueDto.setAdTitle2(adTitle2);
//        yahooIssueDto.setAdDescription(adDescription);
//        yahooIssueDto.setUrl(url);
//        try {
//            yahooIssueDto = yahooService.createIssue(yahooIssueDto, imageList);
//        } catch (BusinessException be) {
//            result.reject(be.getMessage(), be.getParams(), null);
//
//            List<DspSegmentDto> segmentList = yahooService.searchSegmentList();
//
//            List<Pair<Integer, String>> segmentPairList = new ArrayList<Pair<Integer, String>>();
//            for (DspSegmentDto dspSegmentDto : segmentList) {
//                segmentPairList.add(Pair.of(dspSegmentDto.getSegmentId(), dspSegmentDto.getUrl()));
//            }
//
//            yahooIssueinputForm.setSegmentList(segmentPairList);
//
//            mv.addObject("yahooIssueinputForm", yahooIssueinputForm);
//            mv.setViewName("yahoo/issueCreate");
//            return mv;
//        }
//        if (Objects.nonNull(yahooIssueDto.getImageName())) {
//            yahooIssueDto.setImageNameList(Arrays.asList(yahooIssueDto.getImageName().split(",")));
//        }
//
//        mv.setViewName("yahoo/issueCreateComplete");
//        mv.addObject("yahooIssueDto", yahooIssueDto);
//
//        // オペレーションログ記録
//        operationService.create(Operation.YAHOO_CAMPAIGN_REQUEST.getValue(),
//                "案件ID：" + String.valueOf(yahooIssueDto.getIssueId()));
//
//        return mv;
//
//    }

    @GetMapping("/issueList")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_CAMPAIGN_VIEW + "')")
    public ModelAndView issueList(ModelAndView mv) {
        List<YahooIssueDto> yahooIssueDtoList = yahooService.searchYahooIssueList();

        mv.setViewName("yahoo/issueList");
        mv.addObject("yahooIssueDtoList", yahooIssueDtoList);

        // オペレーションログ記録
        operationService.create(Operation.YAHOO_CAMPAIGN_VIEW.getValue(), "案件ID：" + yahooIssueDtoList.stream()
                .map(yahooIssueDto -> String.valueOf(yahooIssueDto.getIssueId())).collect(Collectors.joining(",")));

        return mv;
    }

    @GetMapping("/issueDetail")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_CAMPAIGN_VIEW + "')")
    public ModelAndView issueDetail(Long issueId, ModelAndView mv) {
        YahooIssueDto yahooIssueDto = yahooService.getIssueDetail(issueId);

        List<String> locationIdListString = Arrays.asList(yahooIssueDto.getLocationIds().split(","));
        List<Long> locationIdList = locationIdListString.stream().map(s -> Long.parseLong(s))
                .collect(Collectors.toList());
        List<YahooLocationDto> locationList = yahooService.getLocationList(locationIdList);

        yahooIssueDto.setLocationList(locationList);
        mv.setViewName("yahoo/issueDetail");
        mv.addObject("yahooIssueDto", yahooIssueDto);

        // オペレーションログ記録
        operationService.create(Operation.YAHOO_CAMPAIGN_VIEW.getValue(),
                "案件ID：" + String.valueOf(yahooIssueDto.getIssueId()));

        return mv;
    }

    @PostMapping("/issueDelete")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_CAMPAIGN_MANAGE + "')")
    public ModelAndView issueDelete(@Validated YahooIssueinputForm yahooIssueinputForm, ModelAndView mv) {

        YahooIssueDto yahooIssueDto = yahooService.deleteIssue(yahooIssueinputForm.getIssueId());

        List<String> locationIdListString = Arrays.asList(yahooIssueDto.getLocationIds().split(","));
        List<Long> locationIdList = locationIdListString.stream().map(s -> Long.parseLong(s))
                .collect(Collectors.toList());
        List<YahooLocationDto> locationList = yahooService.getLocationList(locationIdList);

        yahooIssueDto.setLocationList(locationList);

        mv.setViewName("yahoo/issueDeleteComplete");
        mv.addObject("yahooIssueDto", yahooIssueDto);

        // オペレーションログ記録
        operationService.create(Operation.YAHOO_CAMPAIGN_DELETE.getValue(),
                "案件ID：" + String.valueOf(yahooIssueDto.getIssueId()));

        return mv;
    }

    @GetMapping("/issueUpdate")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_CAMPAIGN_MANAGE + "')")
    public ModelAndView issueUpdate(@Validated YahooIssueinputForm yahooIssueinputForm, ModelAndView mv, Long issueId) {

        YahooIssueDto yahooIssueDto = yahooService.getIssueDetail(issueId);

        List<String> locationIdListString = Arrays.asList(yahooIssueDto.getLocationIds().split(","));
        List<Long> locationIdList = locationIdListString.stream().map(s -> Long.parseLong(s))
                .collect(Collectors.toList());
        List<YahooLocationDto> locationList = yahooService.getLocationList(locationIdList);

        yahooIssueDto.setLocationList(locationList);

        mv.setViewName("yahoo/issueUpdate");
        mv.addObject("yahooIssueDto", yahooIssueDto);

        return mv;
    }

    @PostMapping("/issueUpdateComplete")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_CAMPAIGN_MANAGE + "')")
    public ModelAndView issueUpdateComplete(@Validated YahooIssueinputForm yahooIssueinputForm, ModelAndView mv) {

        String campaignId = yahooIssueinputForm.getCampaignId();
        Long issueId = yahooIssueinputForm.getIssueId();
        yahooService.updateIssue(issueId, campaignId);

        YahooIssueDto yahooIssueDto = yahooService.getIssueDetail(issueId);

        List<String> locationIdListString = Arrays.asList(yahooIssueDto.getLocationIds().split(","));
        List<Long> locationIdList = locationIdListString.stream().map(s -> Long.parseLong(s))
                .collect(Collectors.toList());
        List<YahooLocationDto> locationList = yahooService.getLocationList(locationIdList);

        yahooIssueDto.setLocationList(locationList);

        mv.setViewName("yahoo/issueUpdateComplete");
        mv.addObject("yahooIssueDto", yahooIssueDto);

        // オペレーションログ記録
        operationService.create(Operation.YAHOO_CAMPAIGN_UPDATE.getValue(),
                "案件ID：" + String.valueOf(yahooIssueDto.getIssueId()));

        return mv;
    }

    @Autowired
    ApplicationProperties applicationProperties;

    @PostMapping("/download")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_REPORT_VIEW + "')")
    public ResponseEntity<byte[]> download(@ModelAttribute YahooReportInputForm yahooReportInputForm,
            @RequestParam Integer reportType) throws IOException {

        List<String> campaignIdList = new ArrayList<String>();

        List<Pair<String, String>> campaignPairList = getCampaignPairList();

        if (yahooReportInputForm.getCampaignIdList().isEmpty()) {
            // 全てを選択する場合、該当店舗の全キャンペーンのレポートを取得する
            for (Pair<String, String> yahooCampaignList : campaignPairList) {
                campaignIdList.add(yahooCampaignList.getFirst());
            }
        } else {
            campaignIdList = yahooReportInputForm.getCampaignIdList();
        }
        String startDate = null;
        String endDate = null;
        if (PeriodSet.LIMITED.getValue().equals(yahooReportInputForm.getPeriod())) {
            startDate = yahooReportInputForm.getStartDate();
            endDate = yahooReportInputForm.getEndDate();
        }

        // ダウンロードファイルを作成
        String file = yahooService.download(campaignIdList, startDate, endDate, reportType);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", applicationProperties.getContentTypeCsvDownload());
        String fimeName = "Yahoo_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";

        switch (ReportType.of(reportType)) {
        case DEVICE:
            fimeName = "Yahoo_Device_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
            // オペレーションログ記録
            operationService.create(Operation.YAHOO_DEVICE_REPORT_DOWNLOAD.getValue(), "ファイル名：" + fimeName);
            break;
        case REGIONS:
            fimeName = "Yahoo_Region_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
            // オペレーションログ記録
            operationService.create(Operation.YAHOO_REGION_REPORT_DOWNLOAD.getValue(), "ファイル名：" + fimeName);
            break;
        case DATE:
            fimeName = "Yahoo_Date_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
            // オペレーションログ記録
            operationService.create(Operation.YAHOO_DATE_REPORT_DOWNLOAD.getValue(), "ファイル名：" + fimeName);
            break;

        default:
            break;
        }
        httpHeaders.setContentDispositionFormData("filename", fimeName);

        return new ResponseEntity<>(Utf8BomUtil.utf8ToWithBom(file), httpHeaders, HttpStatus.OK);
    }

    private List<Pair<String, String>> getCampaignPairList() {

        List<YahooCampaignManage> yahooCampaignManageList = yahooService.searchYahooCampaignManageList();
        List<Pair<String, String>> campaignPairList = new ArrayList<Pair<String, String>>();

        for (YahooCampaignManage yahooCampaignManage : yahooCampaignManageList) {
            if (yahooCampaignManage.getCampaignId() != null) {
                campaignPairList.add(
                        Pair.of(yahooCampaignManage.getCampaignId().toString(), yahooCampaignManage.getCampaignName()));
            }
        }

        return campaignPairList;
    }

    private void getYahooAreaList() {
        if (CodeMasterServiceImpl.yahooAreaNameList == null) {
            codeMasterService.getYahooAreaList();
        }
    }
}
