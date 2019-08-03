package jp.acepro.haishinsan.service.campaign.twitter;

import java.util.List;
import java.util.SortedMap;

import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.twitter.TwitterAdsDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;

public interface TwitterCampaignApiService {

    // ツイット必須チェック
    void tweetCheck(List<String> tweetIdList);

    // 地域必須チェック
    void areaCheck(TwitterAdsDto twitterAdsDto);

    // 配信日チェック
    void dailyCheck(TwitterAdsDto twitterAdsDto);

    // 予算_地域チェック
    void budgetCheck(TwitterAdsDto twitterAdsDto);

    // 広告作成
    void createAds(TwitterAdsDto twitterAdsDto, IssueDto issueDto);

    // TweetIdListでTweetListを抽出
    List<TwitterTweet> searchTweetList(List<String> tweetIdList);

    /**
     * すべてのWEBSITEツイートリストを検索
     **/
    List<TwitterTweet> searchWebsiteTweets();

    /**
     * IdでWEBSITEツイートリストを検索
     **/
    List<TwitterTweet> searchWebsiteTweetsById(TwitterAdsDto twitterAdsDto);

    /**
     * すべてのFOLLOWERSツイートリストを検索
     **/
    List<TwitterTweet> searchFollowersTweets();

    /**
     * IdでFOLLOWERSツイートリストを検索
     **/
    List<TwitterTweet> searchFollowersTweetsById(TwitterAdsDto twitterAdsDto);

    /**
     * ツイートリストを保存
     **/
    void saveTweetList(List<TwitterTweet> websiteTweetList, List<TwitterTweet> followersTweetList);

    void createTwitterCampaign(TwitterAdsDto twitterAdsDto, IssueDto issueDto);

    String getHeader(String httpMethod, String baseUrl, SortedMap<String, String> parameters);

}
