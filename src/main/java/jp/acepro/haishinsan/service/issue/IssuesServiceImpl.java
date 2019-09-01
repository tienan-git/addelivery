package jp.acepro.haishinsan.service.issue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.acepro.haishinsan.dao.IssueCustomDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.IssuesDto;
import jp.acepro.haishinsan.dto.twitter.TwitterCampaignData;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.entity.IssueWithShopWithCorporation;
import jp.acepro.haishinsan.entity.TwitterCampaignWithIssueWithTweetList;
import jp.acepro.haishinsan.enums.ApprovalFlag;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.enums.IssueAdStatus;
import jp.acepro.haishinsan.enums.IssueAdtype;
import jp.acepro.haishinsan.enums.TwitterCampaignStatus;
import jp.acepro.haishinsan.service.BaseService;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.StringFormatter;

@Service
public class IssuesServiceImpl extends BaseService implements IssuesService {

    @Autowired
    IssueDao issueDao;

    @Autowired
    IssueCustomDao issueCustomDao;

    // DB: 案件一覧を取得する
    @Override
    @Transactional
    public List<IssuesDto> searchIssuesList(IssuesDto issuesSearch) {

        List<IssuesDto> issuesDtoList = new ArrayList<IssuesDto>();
        // DB検索
        List<IssueWithShopWithCorporation> IssueList = issueCustomDao
                .selectIssueList(ContextUtil.getCurrentShop().getShopId(), issuesSearch);
        for (IssueWithShopWithCorporation issue : IssueList) {
            IssuesDto issuesDto = new IssuesDto();
            issuesDto.setShopName(issue.getShopName());
            issuesDto.setCorporationName(issue.getCorporationName());
            issuesDto.setIssueId(issue.getIssueId());
            issuesDto.setCampaignName(issue.getCampaignName());
            issuesDto.setBudget(issue.getBudget());
            issuesDto.setStartDate(issue.getStartDate());
            issuesDto.setEndDate(issue.getEndDate());
            issuesDto.setApprovalFlag(issue.getApprovalFlag());
            // campaignIdの有無で媒体を判別
            // Google
            if (Objects.nonNull(issue.getGoogleCampaignId())) {
                issuesDto.setMedia(IssueAdtype.GOOGLE.getLabel());
                issuesDto.setMediaIcon(IssueAdtype.GOOGLE.getValue());
                issuesDto.setCampaignId(String.valueOf(issue.getGoogleCampaignId()));
            }
            // Facebook
            if (Objects.nonNull(issue.getFacebookCampaignId())) {
                issuesDto.setMedia(IssueAdtype.FACEBOOK.getLabel());
                issuesDto.setMediaIcon(IssueAdtype.FACEBOOK.getValue());
                issuesDto.setCampaignId(String.valueOf(issue.getFacebookCampaignId()));
            }
            // Instagram
            if (Objects.nonNull(issue.getInstagramCampaignId())) {
                issuesDto.setMedia(IssueAdtype.INSTAGRAM.getLabel());
                issuesDto.setMediaIcon(IssueAdtype.INSTAGRAM.getValue());
                issuesDto.setCampaignId(String.valueOf(issue.getInstagramCampaignId()));
            }
            // twitter
            if (Objects.nonNull(issue.getTwitterCampaignId())) {
                issuesDto.setMedia(IssueAdtype.TWITTER.getLabel());
                issuesDto.setMediaIcon(IssueAdtype.TWITTER.getValue());
                issuesDto.setCampaignId(issue.getTwitterCampaignId());
            }
            // dsp
            if (Objects.nonNull(issue.getDspCampaignId())) {
                issuesDto.setMedia(IssueAdtype.DSP.getLabel());
                issuesDto.setMediaIcon(IssueAdtype.DSP.getValue());
                issuesDto.setCampaignId(String.valueOf(issue.getDspCampaignId()));
            }
            // yahoo
            if (Objects.nonNull(issue.getYahooCampaignManageId())) {
                issuesDto.setMedia(IssueAdtype.YAHOO.getLabel());
                issuesDto.setMediaIcon(IssueAdtype.YAHOO.getValue());
            }
            // youtube
            if (Objects.nonNull(issue.getYoutubeCampaignManageId())) {
                issuesDto.setMedia(IssueAdtype.YOUTUBE.getLabel());
                issuesDto.setMediaIcon(IssueAdtype.YOUTUBE.getValue());
            }

            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            // 配信開始日と配信終了日で配信状態を判別
            LocalDateTime today = LocalDateTime.now();
            // date: 2019/10/10, time: 13:00
            String startDate = StringFormatter.dateHyphenToSlash(issue.getStartDate().substring(0, 10));
            String startTime = issue.getStartDate().substring(11);
            String endDate = StringFormatter.dateHyphenToSlash(issue.getEndDate().substring(0, 10));
            String endTime = issue.getEndDate().substring(11);
            LocalDateTime startDateTime = LocalDateTime.parse(startDate + " " + startTime, f);
            LocalDateTime endDateTime = LocalDateTime.parse(endDate + " " + endTime, f);
            // 配信待ち
            if (today.isBefore(startDateTime)) {
                issuesDto.setStatusIcon(IssueAdStatus.WAIT.getValue());
                issuesDto.setStatus(IssueAdStatus.WAIT.getLabel());
            }
            // 配信済み
            if (today.isAfter(endDateTime)) {
                issuesDto.setStatusIcon(IssueAdStatus.END.getValue());
                issuesDto.setStatus(IssueAdStatus.END.getLabel());
                issuesDto.setCampaignStatus(TwitterCampaignStatus.EXPIRED.getLabel());
            } else {
                if (issue.getApprovalFlag().equals(ApprovalFlag.WAITING.getValue())) {
                    issuesDto.setCampaignStatus(TwitterCampaignStatus.PAUSED.getLabel());
                } else {
                    issuesDto.setCampaignStatus(TwitterCampaignStatus.ACTIVE.getLabel());
                }
            }
            // 配信中
            if ((today.isAfter(startDateTime) || today.isEqual(startDateTime))
                    && (today.isBefore(endDateTime) || today.isEqual(endDateTime))) {
                issuesDto.setStatusIcon(IssueAdStatus.ALIVE.getValue());
                issuesDto.setStatus(IssueAdStatus.ALIVE.getLabel());
            }
            issuesDtoList.add(issuesDto);
        }
        return issuesDtoList;
    }

    // DB: 案件を案件Idで論理削除する
    @Override
    @Transactional
    public void deleteIssueById(Long issueId) {

        Issue issue = issueDao.selectById(issueId);
        issue.setIsActived(Flag.OFF.getValue());
        // DB更新
        issueDao.update(issue);

    }

    // 案件IdでcampaignIdを検索
    @Override
    public TwitterCampaignData selectCampaignIdByIssueId(Long issueId) {
        List<TwitterCampaignWithIssueWithTweetList> twitterCampaignList = issueCustomDao.selectCampaignIdByIssueId(
                ContextUtil.getCurrentShop().getShopId(), ContextUtil.getCurrentShop().getTwitterAccountId(), issueId);
        TwitterCampaignData twitterCampaignData = new TwitterCampaignData();
        // DBから検索されたデータ→Dto
        if (twitterCampaignList.isEmpty() == false) {
            twitterCampaignData.setId(twitterCampaignList.get(0).getCampaignId());
            twitterCampaignData.setName(twitterCampaignList.get(0).getCampaignName());
            twitterCampaignData.setDaily_budget_amount_local_micro(twitterCampaignList.get(0).getDailyBudget());
            twitterCampaignData.setTotal_budget_amount_local_micro(twitterCampaignList.get(0).getTotalBudget());
            twitterCampaignData.setStart_time(twitterCampaignList.get(0).getStartDate());
            twitterCampaignData.setEnd_time(twitterCampaignList.get(0).getEndDate());
            List<TwitterTweet> TwitterTweetList = new ArrayList<>();
            for (TwitterCampaignWithIssueWithTweetList twitterCampaign : twitterCampaignList) {
                TwitterTweet twitterTweet = new TwitterTweet();
                twitterTweet.setTweetId(twitterCampaign.getTweetId());
                twitterTweet.setTweetTitle(twitterCampaign.getTweetTitle());
                twitterTweet.setTweetBody(twitterCampaign.getTweetBody());
                twitterTweet.setPreviewUrl(twitterCampaign.getPreviewUrl());
                TwitterTweetList.add(twitterTweet);
            }
            twitterCampaignData.setTweetList(TwitterTweetList);
        }
        return twitterCampaignData;
    }

}
