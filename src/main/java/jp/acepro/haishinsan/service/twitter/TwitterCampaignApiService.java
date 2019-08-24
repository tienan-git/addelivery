package jp.acepro.haishinsan.service.twitter;

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

	// API: 広告作成
	void createAds(TwitterAdsDto twitterAdsDto, IssueDto issueDto);

	// API: 広告削除(停止の状態にする)
	void deleteAds(String campaignId, Long issueId);

	// API: 広告ステータス変更
	void changeAdsStatus(String campaignId, String switchFlag);

	// DB検索：TweetIdListでTweetListを抽出 いらないかも。。。。
	List<TwitterTweet> searchTweetList(List<String> tweetIdList);

	// tweetListをsessionから洗い出す
	List<TwitterTweet> getTweetList(TwitterAdsDto twitterAdsDt);

	// API: すべてのWEBSITEツイートリストを検索
	List<TwitterTweet> searchWebsiteTweets();

	// API: すべてのFOLLOWERSツイートリストを検索
	List<TwitterTweet> searchFollowersTweets();

	void createTwitterCampaign(TwitterAdsDto twitterAdsDto, IssueDto issueDto);

	String getHeader(String httpMethod, String baseUrl, SortedMap<String, String> parameters);

}
