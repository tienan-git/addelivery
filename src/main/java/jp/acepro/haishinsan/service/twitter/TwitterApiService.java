package jp.acepro.haishinsan.service.twitter;

import java.util.List;
import java.util.SortedMap;

import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.twitter.TwitterAdsDto;
import jp.acepro.haishinsan.dto.twitter.TwitterCampaignData;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;

public interface TwitterApiService {

	/**
	 * 広告作成
	 **/
	void createAds(TwitterAdsDto twitterAdsDto, IssueDto issueDto);

	/**
	 * 広告リスト
	 **/
	List<TwitterCampaignData> adsList();

	/**
	 * 広告ステータスの変更
	 **/
	void changeAdsStatus(String campaignId, String switchFlag);

	/**
	 * 広告詳細
	 **/
	TwitterCampaignData searchCampaignById(String groupId, String campaignId);

	/**
	 * 広告削除
	 **/
	void deleteAds(String campaignId);

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
