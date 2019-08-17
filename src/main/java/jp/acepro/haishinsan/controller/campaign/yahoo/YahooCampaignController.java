package jp.acepro.haishinsan.controller.campaign.yahoo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dto.dsp.DspSegmentDto;
import jp.acepro.haishinsan.dto.yahoo.YahooIssueDto;
import jp.acepro.haishinsan.dto.yahoo.YahooLocationDto;
import jp.acepro.haishinsan.enums.AdvDestination;
import jp.acepro.haishinsan.enums.DeviceType;
import jp.acepro.haishinsan.enums.MediaType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.YahooIssueinputForm;
import jp.acepro.haishinsan.mapper.YahooMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.yahoo.YahooService;
import jp.acepro.haishinsan.util.ImageUtil;

@Controller
@RequestMapping("/yahoo")
public class YahooCampaignController {

    @Autowired
    ImageUtil imageUtil;

    @Autowired
    HttpSession session;

    @Autowired
    YahooService yahooService;

    @Autowired
    OperationService operationService;

    @Autowired
    CodeMasterService codeMasterService;

    // Yahoo広告作成
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

        mv.setViewName("campaign/yahoo/issueCreate");
        mv.addObject("yahooIssueinputForm", yahooIssueinputForm);
        return mv;

    }

    // Yahoo広告作成確認
    @PostMapping("/issueCreateConfirm")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_CAMPAIGN_REQUEST + "')")
    public ModelAndView createIssueComplete(@ModelAttribute YahooIssueinputForm yahooIssueinputForm,
            BindingResult result, ModelAndView mv) throws Exception {

        List<MultipartFile> imageList = null;
        String imaBase64 = null;
        List<String> imaBase64List = new ArrayList<>();
        String imageName = null;
        String url = null;
        String adShortTitle = null;
        String adTitle1 = null;
        String adTitle2 = null;
        String adDescription = null;
        YahooIssueDto yahooIssueDto;
        try {
            switch (AdvDestination.of(yahooIssueinputForm.getAdvDestination())) {
            case RESPONSIVE:
                // infeedの場合、デバイスタイプをモバイルで固定する
                yahooIssueinputForm.setDeviceType(DeviceType.MOBILE.getValue());
                adShortTitle = yahooIssueinputForm.getInfeedAdShortTitle();
                adDescription = yahooIssueinputForm.getInfeedAdDescription();
                url = yahooIssueinputForm.getInfeedAdFinalPageUrl();
                imageList = yahooIssueinputForm.getInfeedAdImageFileList();
                for (MultipartFile multipartFile : imageList) {
                    imaBase64 = imageUtil.getImageBytes(multipartFile, MediaType.YAHOORESPONSIVE.getValue());
                    imaBase64List.add(imaBase64);
                }
                break;
            case LISTING:
                adTitle1 = yahooIssueinputForm.getListingAdTitle1();
                adTitle2 = yahooIssueinputForm.getListingAdTitle2();
                adDescription = yahooIssueinputForm.getListingAdDescription();
                url = yahooIssueinputForm.getListingAdFinalPageUrl();
                break;
            case IMAGE:
                url = yahooIssueinputForm.getTargetAdFinalPageUrl();
                imageList = yahooIssueinputForm.getTargetAdImageFileList();
                for (MultipartFile multipartFile : imageList) {
                    imaBase64 = imageUtil.getImageBytes(multipartFile, MediaType.YAHOOIMAGE.getValue());
                    imaBase64List.add(imaBase64);
                }
                break;
            }
            yahooIssueDto = YahooMapper.INSTANCE.map(yahooIssueinputForm);

            List<Long> locationIdList = yahooIssueinputForm.getLocationIdList();
            if (locationIdList.isEmpty()) {
                result.reject(ErrorCodeConstant.E60001);

                List<DspSegmentDto> segmentList = yahooService.searchSegmentList();

                List<Pair<Integer, String>> segmentPairList = new ArrayList<Pair<Integer, String>>();
                for (DspSegmentDto dspSegmentDto : segmentList) {
                    segmentPairList.add(Pair.of(dspSegmentDto.getSegmentId(), dspSegmentDto.getUrl()));
                }

                yahooIssueinputForm.setSegmentList(segmentPairList);

                mv.addObject("yahooIssueinputForm", yahooIssueinputForm);
                mv.setViewName("campaign/yahoo/issueCreate");
                return mv;
            }
            List<YahooLocationDto> locationList = yahooService.getLocationList(locationIdList);
            yahooIssueDto.setLocationList(locationList);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < locationIdList.size(); i++) {
                sb.append(locationIdList.get(i));
                if (i != locationIdList.size() - 1) {
                    sb.append(",");
                }
            }

            // 画像名を','で連接する
            if (Objects.nonNull(imageList)) {
                imageName = imageList.stream().map(image -> image.getOriginalFilename())
                        .collect(Collectors.joining(","));
            }
            yahooIssueDto.setLocationIds(sb.toString());
            yahooIssueDto.setImageName(imageName);
            yahooIssueDto.setAdShortTitle(adShortTitle);
            yahooIssueDto.setAdTitle1(adTitle1);
            yahooIssueDto.setAdTitle2(adTitle2);
            yahooIssueDto.setAdDescription(adDescription);
            yahooIssueDto.setUrl(url);
            if (Objects.nonNull(yahooIssueDto.getImageName())) {
                yahooIssueDto.setImageNameList(Arrays.asList(yahooIssueDto.getImageName().split(",")));
            }

            yahooService.yahooCampaignCheck(yahooIssueDto);
        } catch (BusinessException be) {
            result.reject(be.getMessage(), be.getParams(), null);

            List<DspSegmentDto> segmentList = yahooService.searchSegmentList();

            List<Pair<Integer, String>> segmentPairList = new ArrayList<Pair<Integer, String>>();
            for (DspSegmentDto dspSegmentDto : segmentList) {
                segmentPairList.add(Pair.of(dspSegmentDto.getSegmentId(), dspSegmentDto.getUrl()));
            }

            yahooIssueinputForm.setSegmentList(segmentPairList);

            mv.addObject("yahooIssueinputForm", yahooIssueinputForm);
            mv.setViewName("campaign/yahoo/issueCreate");
            return mv;
        }

        // Sessionに保存する
        session.setAttribute("imageList", imageList);
        session.setAttribute("imaBase64List", imaBase64List);
        session.setAttribute("yahooIssueDto", yahooIssueDto);

        mv.setViewName("campaign/yahoo/issueCreateConfirm");
        mv.addObject("yahooIssueDto", yahooIssueDto);
        return mv;
    }

    @GetMapping("/issueSuccess")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YAHOO_CAMPAIGN_REQUEST + "')")
    public ModelAndView issueSuccess(ModelAndView mv) throws Exception {

        // Sessionから広告データを取得する
        YahooIssueDto yahooIssueDto = (YahooIssueDto) session.getAttribute("yahooIssueDto");
        List<String> imaBase64List = (List<String>) session.getAttribute("imaBase64List");

        yahooIssueDto = yahooService.createIssue(yahooIssueDto, imaBase64List);

        // オペレーションログ記録
        operationService.create(Operation.YAHOO_CAMPAIGN_REQUEST.getValue(),
                "案件ID：" + String.valueOf(yahooIssueDto.getIssueId()));

        mv.setViewName("campaign/yahoo/issueSuccess");
        return mv;
    }

    private void getYahooAreaList() {
        if (CodeMasterServiceImpl.yahooAreaNameList == null) {
            codeMasterService.getYahooAreaList();
        }
    }
}
