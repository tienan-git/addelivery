package jp.acepro.haishinsan.controller.campaign.youtube;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeIssueDto;
import jp.acepro.haishinsan.form.YoutubeIssueinputForm;
import jp.acepro.haishinsan.mapper.YoutubeMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.youtube.YoutubeService;

@Controller
@RequestMapping("/youtube")
public class YoutubeCampaignController {

    @Autowired
    HttpSession session;

    @Autowired
    YoutubeService youtubeService;

    @Autowired
    OperationService operationService;

    @Autowired
    DspSegmentService dspSegmentService;

    @Autowired
    CodeMasterService codeMasterService;

    @GetMapping("/issueCreate")
    public ModelAndView createIssue(ModelAndView mv) {

        // コードマスタを読込
        getGoogleAreaList();

        List<Pair<Integer, String>> segmentPairList = new ArrayList<Pair<Integer, String>>();

        // 入力ＦＯＲＭを初期化
        YoutubeIssueinputForm youtubeIssueinputForm = new YoutubeIssueinputForm();

        youtubeIssueinputForm.setSegmentList(segmentPairList);

        mv.setViewName("campaign/youtube/issueCreate");

        List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();
        mv.addObject("dspSegmentDtoList", dspSegmentDtoList);
        mv.addObject("youtubeIssueinputForm", youtubeIssueinputForm);
        return mv;

    }

    @PostMapping("/issueCreateConfirm")
    public ModelAndView createIssueComplete(@ModelAttribute YoutubeIssueinputForm youtubeIssueinputForm,
            BindingResult result, ModelAndView mv) {

        YoutubeIssueDto youtubeIssueDto = YoutubeMapper.INSTANCE.map(youtubeIssueinputForm);
        List<Long> locationIdList = youtubeIssueinputForm.getLocationIdList();
        if (locationIdList.isEmpty()) {
            result.reject(ErrorCodeConstant.E60001);

            mv.addObject("youtubeIssueinputForm", youtubeIssueinputForm);
            mv.setViewName("campaign/youtube/issueCreate");
            return mv;
        }

        List<Pair<Long, String>> locationList = getLocationrList(locationIdList);
        youtubeIssueDto.setLocationList(locationList);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < locationIdList.size(); i++) {
            sb.append(locationIdList.get(i));
            if (i != locationIdList.size() - 1) {
                sb.append(",");
            }
        }
        youtubeIssueDto.setArea(sb.toString());

        // Sessionに保存する
        session.setAttribute("youtubeIssueDto", youtubeIssueDto);

        mv.setViewName("campaign/youtube/issueCreateConfirm");
        mv.addObject("youtubeIssueDto", youtubeIssueDto);
        return mv;

    }

    @GetMapping("/issueSuccess")
    public ModelAndView issueSuccess(ModelAndView mv) {

        // Sessionから広告データ取得する
        YoutubeIssueDto youtubeIssueDto = (YoutubeIssueDto) session.getAttribute("youtubeIssueDto");
        youtubeIssueDto = youtubeService.createIssue(youtubeIssueDto);

        mv.setViewName("campaign/youtube/issueSuccess");
        return mv;
    }

    private List<Pair<Long, String>> getLocationrList(List<Long> locationIdList) {

        // コードマスタを読込
        getGoogleAreaList();

        List<Pair<Long, String>> locationList = new ArrayList<Pair<Long, String>>();
        for (Pair<Long, String> pair : CodeMasterServiceImpl.googleAreaNameList) {
            if (locationIdList.contains(pair.getFirst())) {
                locationList.add(pair);
            }
        }
        return locationList;
    }

    private void getGoogleAreaList() {
        if (CodeMasterServiceImpl.googleAreaNameList == null) {
            codeMasterService.getGoogleAreaList();
        }
    }
}
