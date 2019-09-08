package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.IssuesDto;
import jp.acepro.haishinsan.entity.IssueTwitterCampaign;
import jp.acepro.haishinsan.entity.IssueWithShopWithCorporation;
import jp.acepro.haishinsan.entity.TwitterCampaignWithIssueWithTweetList;

@Dao
@InjectConfig
public interface IssueCustomDao {

    @Select
    List<Issue> selectByShopId(Long shopId);

    // issueListを検索する(Role:SHOP)
    @Select
    List<IssueWithShopWithCorporation> selectIssueList(Long shopId, IssuesDto issueSearchDto);

    @Select
    List<TwitterCampaignWithIssueWithTweetList> selectCampaignIdByIssueId(Long shopId, String accountId, Long issueId);

    @Select
    List<Issue> selectFacebookIssueNeededStart(String date);

    @Select
    List<Issue> selectFacebookIssueNeededStop(String date);

    @Select
    List<Issue> selectGoogleIssueNeededStart(String date);

    @Select
    List<Issue> selectGoogleIssueNeededStop(String date);

    // twitterCampaignIdで検索する
    @Select
    Issue selectByTwitterCampaignId(Long shopId, String campaignId);

    @Select
    List<Issue> selectExistFacebookDuplicateIssue(String campaignId, String startTime, String endTime);

    @Select
    List<Issue> selectExistGoogleDuplicateIssue(Long campaignId, String startTime, String endTime);

    // TwitterのissueList検索する
    @Select
    List<IssueTwitterCampaign> selectTwitterIssueListByShopId(Long shopId, String date);

    // issueListを検索する(Role:ADMIN)
    @Select
    List<IssueWithShopWithCorporation> selectAllShop();

    // issueListを検索する(Role:AGENCY)
    @Select
    List<IssueWithShopWithCorporation> selectAgencyShops(Long shopId);

    // issueListを検索する(Role:CORPORATION)
    @Select
    List<IssueWithShopWithCorporation> selectCorporationShops(Long shopId);

}
