package jp.acepro.haishinsan.service.issue;

import java.time.LocalDate;
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
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.enums.IssueAdStatus;
import jp.acepro.haishinsan.enums.IssueAdtype;
import jp.acepro.haishinsan.service.BaseService;
import jp.acepro.haishinsan.util.ContextUtil;

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
            // campaignIdの有無で媒体を判別
            // Google
            if (Objects.nonNull(issue.getGoogleCampaignManageId())) {
                issuesDto.setMedia(IssueAdtype.GOOGLE.getLabel());
                issuesDto.setMediaIcon(IssueAdtype.GOOGLE.getValue());
            }
            // Facebook
            if (Objects.nonNull(issue.getFacebookCampaignManageId())) {
                issuesDto.setMedia(IssueAdtype.FACEBOOK.getLabel());
                issuesDto.setMediaIcon(IssueAdtype.FACEBOOK.getValue());
            }
            // twitter
            if (Objects.nonNull(issue.getTwitterCampaignManageId())) {
                issuesDto.setMedia(IssueAdtype.TWITTER.getLabel());
                issuesDto.setMediaIcon(IssueAdtype.TWITTER.getValue());
            }
            // dsp
            if (Objects.nonNull(issue.getDspCampaignManageId())) {
                issuesDto.setMedia(IssueAdtype.DSP.getLabel());
                issuesDto.setMediaIcon(IssueAdtype.DSP.getValue());
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
            // 配信開始日と配信終了日で配信状態を判別
            LocalDate today = LocalDate.now();
            LocalDate startDate = LocalDate.parse(issue.getStartDate());
            LocalDate endDate = LocalDate.parse(issue.getEndDate());
            // 配信待ち
            if (today.isBefore(startDate)) {
                issuesDto.setStatusIcon(IssueAdStatus.WAIT.getValue());
                issuesDto.setStatus(IssueAdStatus.WAIT.getLabel());
            }
            // 配信済み
            if (today.isAfter(endDate)) {
                issuesDto.setStatusIcon(IssueAdStatus.END.getValue());
                issuesDto.setStatus(IssueAdStatus.END.getLabel());
            }
            // 配信中
            if ((today.isAfter(startDate) || today.isEqual(startDate))
                    && (today.isBefore(endDate) || today.isEqual(endDate))) {
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
