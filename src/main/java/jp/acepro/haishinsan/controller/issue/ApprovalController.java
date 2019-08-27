package jp.acepro.haishinsan.controller.issue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.dto.IssuesDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDto;
import jp.acepro.haishinsan.dto.google.GoogleSwitchRes;
import jp.acepro.haishinsan.dto.twitter.TwitterSwitchRes;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;
import jp.acepro.haishinsan.service.google.GoogleCampaignService;
import jp.acepro.haishinsan.service.issue.IssuesService;
import jp.acepro.haishinsan.service.twitter.TwitterCampaignApiService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/issue/approval")
public class ApprovalController {

    @Autowired
    IssuesService issueService;

    @Autowired
    TwitterCampaignApiService twitterCampaignApiService;

    @GetMapping("/twitter/{issueId}/{switchFlag}")
    public TwitterSwitchRes switchCampaignStatus(@PathVariable String issueId, @PathVariable String switchFlag) {

        IssuesDto issuesDto = issueService.selectIssuesById(issueId);
        // キャンペーンステータスを更新
        twitterCampaignApiService.changeAdsStatus(issuesDto.getCampaignId(), switchFlag);

        // 正常時レスポンスを作成
        TwitterSwitchRes res = new TwitterSwitchRes();
        res.setCode("0000");
        res.setMessage("キャンペーンのステータスが更新されました！");
        return res;
    }

    @Autowired
    DspCampaignService dspCampaignService;

    @Autowired
    OperationService operationService;

    @GetMapping("/dsp/{campaignId}/{switchFlag}")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CAMPAIGN_MANAGE + "')")
    public ModelAndView Creative(@PathVariable Integer campaignId, @PathVariable String switchFlag) {

        dspCampaignService.updateCampaign(campaignId, switchFlag);

        List<DspCampaignDto> dspCampaignDtoList = dspCampaignService.getCampaignList();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dsp/campaignList");
        modelAndView.addObject("dspCampaignDtoList", dspCampaignDtoList);

        // オペレーションログ記録
        operationService.create(Operation.DSP_CAMPAIGN_UPDATE.getValue(), String.valueOf(campaignId));

        return modelAndView;

    }

    @Autowired
    GoogleCampaignService googleService;

    @GetMapping("/google/{campaignId}/{switchFlag}")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_CAMPAIGN_MANAGE + "')")
    public GoogleSwitchRes switchCampaignStatus(@PathVariable Long campaignId, @PathVariable String switchFlag) {

        // キャンペーンステータスを更新
        googleService.updateCampaignStatus(campaignId, switchFlag);

        // 正常時レスポンスを作成
        GoogleSwitchRes res = new GoogleSwitchRes();
        res.setCode("0000");
        res.setMessage("キャンペーンのステータスが更新されました！");

        // オペレーションログ記録
        operationService.create(Operation.GOOGLE_CAMPAIGN_STATUS_UPDATE.getValue(), String.valueOf(campaignId));
        return res;
    }
}
