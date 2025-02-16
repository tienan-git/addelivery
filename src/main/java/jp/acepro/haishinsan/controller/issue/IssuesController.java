package jp.acepro.haishinsan.controller.issue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.IssuesDto;
import jp.acepro.haishinsan.dto.twitter.TwitterCampaignData;
import jp.acepro.haishinsan.enums.IssueAdtype;
import jp.acepro.haishinsan.form.IssueSearchForm;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.google.GoogleCampaignService;
import jp.acepro.haishinsan.service.issue.IssuesService;
import jp.acepro.haishinsan.service.twitter.TwitterCampaignApiService;

@lombok.extern.slf4j.Slf4j
@Controller
@RequestMapping("/issue/issues")
public class IssuesController {

    @Autowired
    IssuesService issuesService;

    @Autowired
    TwitterCampaignApiService twitterCampaignApiService;

    @Autowired
    FacebookService facebookService;

    @Autowired
    GoogleCampaignService googleCampaignService;

    @GetMapping("/issueList")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.ISSUE_LIST + "')")
    public ModelAndView issueList(@ModelAttribute IssueSearchForm issueSearchForm) {

        IssuesDto issuesDto = new IssuesDto();
        List<IssuesDto> issuesDtoList = issuesService.searchIssuesList(issuesDto);

        ModelAndView mv = new ModelAndView();
        mv.addObject(issuesDtoList);
        mv.setViewName("issue/issueList");
        return mv;
    }

    @PostMapping("/issueSearch")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.ISSUE_LIST + "')")
    public ModelAndView searchIssueList(@ModelAttribute IssueSearchForm issueSearchForm) {

        // Form → dto (検索条件)
        IssuesDto issuesDto = new IssuesDto();
        issuesDto.setShopName(issueSearchForm.getShopName());
        issuesDto.setCampaignName(issueSearchForm.getCampaignName());
        issuesDto.setMedia(issueSearchForm.getMedia());
        issuesDto.setStatus(issueSearchForm.getStatus());
        issuesDto.setStartDate(issueSearchForm.getStartDate());

        log.debug("----------------------------------------------------------");
        log.debug("issueSearchForm: " + issueSearchForm.toString());
        log.debug("----------------------------------------------------------");

        List<IssuesDto> issuesDtoList = issuesService.searchIssuesList(issuesDto);

        ModelAndView mv = new ModelAndView();
        mv.addObject(issuesDtoList);
        mv.setViewName("issue/issueList");
        return mv;
    }

    @PostMapping("/deleteIssue")
    @PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.ISSUE_DELETE + "')")
    public ModelAndView deleteIssue(@RequestParam Long issueId, @RequestParam String media) {

        if ("Twitter".equals(media)) {
            TwitterCampaignData twitterCampaignData = issuesService.selectCampaignIdByIssueId(issueId);
            String campaignId = twitterCampaignData.getId();
            // Call Api: Twitter広告状態を停止にする
            twitterCampaignApiService.deleteAds(campaignId, issueId);
        } else if (IssueAdtype.GOOGLE.getLabel().equals(media)) {
            Issue issue = issuesService.selectIssueByIssueId(issueId);
            // Googleキャンペーンが存在する場合、且つ配信中と判断する場合、配信ステータスを停止する
            if (issue != null && issue.getGoogleCampaignId() != null && issue.getStartTimestamp() != null
                    && issue.getEndTimestamp() == null) {
                googleCampaignService.updateCampaignStatus(issue.getGoogleCampaignId(), "OFF");
            }
            // 案件Idで案件を論理削除
            issuesService.deleteIssueById(issueId);
        } else if (IssueAdtype.FACEBOOK.getLabel().equals(media)) {
            Issue issue = issuesService.selectIssueByIssueId(issueId);
            // Facebookキャンペーンが存在する場合、配信ステータスを停止する
            if (issue != null && issue.getFacebookCampaignId() != null) {
                facebookService.updateCampaignStatus(issue.getFacebookCampaignId(), "OFF");
            }
            // 案件Idで案件を論理削除
            issuesService.deleteIssueById(issueId);
        } else {
            // 案件Idで案件を論理削除
            issuesService.deleteIssueById(issueId);
        }

        log.debug("----------------------------------------------------------");
        log.debug("削除した issueId: " + issueId);
        log.debug("----------------------------------------------------------");

        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/issue/issues/issueList");

        return mv;
    }

}
