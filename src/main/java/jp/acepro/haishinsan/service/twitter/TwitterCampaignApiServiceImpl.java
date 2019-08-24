package jp.acepro.haishinsan.service.twitter;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.dao.ShopDao;
import jp.acepro.haishinsan.dao.TwitterCampaignManageCustomDao;
import jp.acepro.haishinsan.dao.TwitterCampaignManageDao;
import jp.acepro.haishinsan.dao.TwitterRegionPriceCustomDao;
import jp.acepro.haishinsan.dao.TwitterTweetListCustomDao;
import jp.acepro.haishinsan.dao.TwitterTweetListDao;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.TwitterCampaignManage;
import jp.acepro.haishinsan.db.entity.TwitterTweetList;
import jp.acepro.haishinsan.dto.EmailCampDetailDto;
import jp.acepro.haishinsan.dto.EmailDto;
import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.twitter.TweetUrls;
import jp.acepro.haishinsan.dto.twitter.TwitterAdsDto;
import jp.acepro.haishinsan.dto.twitter.TwitterCampaignData;
import jp.acepro.haishinsan.dto.twitter.TwitterCampaignDataRes;
import jp.acepro.haishinsan.dto.twitter.TwitterCampaignIdReq;
import jp.acepro.haishinsan.dto.twitter.TwitterCampaignReq;
import jp.acepro.haishinsan.dto.twitter.TwitterCampaignRes;
import jp.acepro.haishinsan.dto.twitter.TwitterFundingInstrumentRes;
import jp.acepro.haishinsan.dto.twitter.TwitterGroupReq;
import jp.acepro.haishinsan.dto.twitter.TwitterGroupRes;
import jp.acepro.haishinsan.dto.twitter.TwitterPromotTweetsReq;
import jp.acepro.haishinsan.dto.twitter.TwitterPromotTweetsRes;
import jp.acepro.haishinsan.dto.twitter.TwitterTargetObjReq;
import jp.acepro.haishinsan.dto.twitter.TwitterTargetParamsReq;
import jp.acepro.haishinsan.dto.twitter.TwitterTargetReq;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.dto.twitter.TwitterTweetListReq;
import jp.acepro.haishinsan.dto.twitter.TwitterTweetListRes;
import jp.acepro.haishinsan.dto.twitter.TwitterTweets;
import jp.acepro.haishinsan.enums.ApprovalFlag;
import jp.acepro.haishinsan.enums.EmailTemplateType;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.enums.MediaCollection;
import jp.acepro.haishinsan.enums.TwitterCampaignStatus;
import jp.acepro.haishinsan.enums.TwitterLocationType;
import jp.acepro.haishinsan.enums.TwitterObjective;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.service.BaseService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.EmailService;
import jp.acepro.haishinsan.util.CalculateUtil;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.TwitterUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TwitterCampaignApiServiceImpl extends BaseService implements TwitterCampaignApiService {

	@Autowired
	ApplicationProperties applicationProperties;

	// 店舗
	@Autowired
	ShopDao shopDao;

	// 案件
	@Autowired
	IssueDao issueDao;

	// tweetリスト
	@Autowired
	TwitterTweetListDao twitterTweetListDao;

	// tweetリスト（カスタム）
	@Autowired
	TwitterTweetListCustomDao twitterTweetListCustomDao;

	// キャンペーン
	@Autowired
	TwitterCampaignManageDao twitterCampaignManageDao;

	// キャンペーン（カスタム）
	@Autowired
	TwitterCampaignManageCustomDao twitterCampaignManageCustomDao;

	@Autowired
	TwitterRegionPriceCustomDao twitterRegionPriceCustomDao;

	@Autowired
	EmailService emailService;

	// ---------------------------------------------------------------------------
	// ---------------------------------------------------------------------------
	// ツイート非空チェック
	@Override
	public void tweetCheck(List<String> tweetIdList) {

		if (tweetIdList == null) {
			throw new BusinessException(ErrorCodeConstant.E20005);
		}
	}

	// ---------------------------------------------------------------------------
	// ---------------------------------------------------------------------------
	// 都道府県非空チェック
	@Override
	public void areaCheck(TwitterAdsDto twitterAdsDto) {

		if (TwitterLocationType.REGION.getValue() == twitterAdsDto.getLocation()
				&& twitterAdsDto.getRegions().isEmpty() == true) {
			// 配信都道府県を選択してください。
			throw new BusinessException(ErrorCodeConstant.E20006);
		}
	}

	// ---------------------------------------------------------------------------
	// ---------------------------------------------------------------------------
	// 配信日チェック
	@Override
	public void dailyCheck(TwitterAdsDto twitterAdsDto) {
		LocalDate startDate = LocalDate.parse(twitterAdsDto.getStartTime());
		LocalDate endDate = LocalDate.parse(twitterAdsDto.getEndTime());
		if (endDate.isBefore(startDate)) {
			throw new BusinessException(ErrorCodeConstant.E20003);
		}
	}

	// ---------------------------------------------------------------------------
	// ---------------------------------------------------------------------------
	// 予算_地域チェック
	@Override
	public void budgetCheck(TwitterAdsDto twitterAdsDto) {
		Long averagePrice = getAveragePrice(twitterAdsDto);
		if (averagePrice > twitterAdsDto.getDailyBudget()) {
			throw new BusinessException(ErrorCodeConstant.E20007, String.valueOf(averagePrice));
		}
	}

	// ---------------------------------------------------------------------------
	// ---------------------------------------------------------------------------
	// TweetIdListでTweetListを抽出する
	@Override
	public List<TwitterTweet> searchTweetList(List<String> tweetIdList) {

		// tweetIDリストでDBからtweetを取得してリストを作る
		List<TwitterTweet> tweetList = new ArrayList<>();
		if (tweetIdList.size() != 0 || tweetIdList.isEmpty() == false) {
			for (String tweetId : tweetIdList) {
				TwitterTweet twitterTweet = new TwitterTweet();
				TwitterTweetList tweet = twitterTweetListCustomDao
						.selectByAccountIdAndTweetId(ContextUtil.getCurrentShop().getTwitterAccountId(), tweetId);
				// Twitterが見つからなかった場合（削除されたり...）
				if (tweet.getTweetId() == null) {
					twitterTweet.setTweetId("");
					twitterTweet.setTweetTitle("");
					twitterTweet.setTweetBody("Tweetが存在しません");
					twitterTweet.setPreviewUrl("");
					tweetList.add(twitterTweet);
				}
				twitterTweet.setTweetId(tweet.getTweetId());
				twitterTweet.setTweetTitle(tweet.getTweetTitle());
				twitterTweet.setTweetBody(tweet.getTweetBody());
				twitterTweet.setPreviewUrl(tweet.getPreviewUrl());
				tweetList.add(twitterTweet);
			}
		}
		return tweetList;
	}

	// ---------------------------------------------------------------------------
	// ---------------------------------------------------------------------------
	// 地域情報を抽出する

	public static String japanTargetKey = "06ef846bfc783874";

	// 案件簡単作成のため、新しいトランザクションで実行する
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createTwitterCampaign(TwitterAdsDto twitterAdsDto, IssueDto issueDto) {
		createAds(twitterAdsDto, issueDto);
	}

	// ---------------------------------------------------------------------------
	// ---------------------------------------------------------------------------
	// tweetListをsessionから洗い出
	@Override
	@Transactional
	public List<TwitterTweet> getTweetList(TwitterAdsDto twitterAdsDto) {

		List<TwitterTweet> tweetList = new ArrayList<TwitterTweet>();
		List<TwitterTweet> tweetListFromSession = twitterAdsDto.getTweetList();
		List<String> tweetIdList = twitterAdsDto.getTweetIdList();
		log.debug("-------------------------------------------------");
		log.debug("-------------------------------------------------");
		log.debug("tweetListFromSession : " + tweetListFromSession.toString());
		log.debug("-------------------------------------------------");
		log.debug("-------------------------------------------------");
		log.debug("tweetIdList : " + tweetIdList.toString());
		log.debug("-------------------------------------------------");
		log.debug("-------------------------------------------------");

		for (TwitterTweet twitterTweet : tweetListFromSession) {
			for (String tweetId : tweetIdList) {
				if (tweetId.equals(twitterTweet.getTweetId())) {
					TwitterTweet tweet = new TwitterTweet();
					tweet.setTweetId(tweetId);
					tweet.setTweetTitle(twitterTweet.getTweetTitle());
					tweet.setTweetBody(twitterTweet.getTweetBody());
					tweet.setPreviewUrl(twitterTweet.getPreviewUrl());
					tweetList.add(tweet);
				}
			}
		}
		return tweetList;
	}

	// ---------------------------------------------------------------------------
	// ---------------------------------------------------------------------------
	// 広告作成
	@Override
	@Transactional
	public void createAds(TwitterAdsDto twitterAdsDto, IssueDto issueDto) {

		String fundingInstrumentId = null;
		String campaignId = null;
		String lineItemId = null;

		// 広告配信日チェック
		LocalDate startDate = LocalDate.parse(twitterAdsDto.getStartTime());
		LocalDate endDate = LocalDate.parse(twitterAdsDto.getEndTime());
		if (endDate.isBefore(startDate)) {
			throw new BusinessException(ErrorCodeConstant.E20003);
		}

		// 広告予算チェック
		long dailyBudget = twitterAdsDto.getDailyBudget();
		if (twitterAdsDto.getTotalBudget() != 0 && dailyBudget > twitterAdsDto.getTotalBudget()) {
			throw new BusinessException(ErrorCodeConstant.E20004);
		}

		// 広告ツイート設定チェック
		if (twitterAdsDto.getTweetIdList() == null) {
			throw new BusinessException(ErrorCodeConstant.E20005);
		}

		// 都道府県非空チェック
		if (TwitterLocationType.REGION.getValue() == twitterAdsDto.getLocation()
				&& twitterAdsDto.getRegions().isEmpty() == true) {
			// 配信都道府県を選択してください。
			throw new BusinessException(ErrorCodeConstant.E20006);
		}

		// 予算のチェック
		Long averagePrice = getAveragePrice(twitterAdsDto);
		if (averagePrice > twitterAdsDto.getDailyBudget()) {
			throw new BusinessException(ErrorCodeConstant.E20007, String.valueOf(averagePrice));
		}
		// 支払い方法IDを取得する
		fundingInstrumentId = getFundingInstrumentId();

		// １: 支払い方法Iを指定してキャンペーンを作成 , キャンペーンIDを取得
		campaignId = creatCampaign(fundingInstrumentId, twitterAdsDto);

		// 2: campaignIdでグループを作成 , グループIDを取得;
		lineItemId = creatgroup(campaignId, twitterAdsDto);

		// 3: グループIDでツイート設定
		promoteTweets(lineItemId, twitterAdsDto);

		// 4: グループIDでターゲットを設定
		setTarget(lineItemId, twitterAdsDto);

		// tweetIdList
		List<String> tweetIdList = twitterAdsDto.getTweetIdList();

		// tweetIds
		String tweetIds = TwitterUtil.formatToOneString(tweetIdList);

		// DBにキャンペーンを保存
		TwitterCampaignManage twitterCampaignManage = new TwitterCampaignManage();
		twitterCampaignManage.setCampaignId(campaignId);
		twitterCampaignManage.setCampaignName(twitterAdsDto.getCampaignName());
		if (Flag.ON.getValue().toString().equals(ContextUtil.getCurrentShop().getSalesCheckFlag())) {
			// 営業チェックが必要有りの場合
			twitterCampaignManage.setApprovalFlag(ApprovalFlag.WAITING.getValue());
		} else {
			// 営業チェックが必要無しの場合
			twitterCampaignManage.setApprovalFlag(ApprovalFlag.COMPLETED.getValue());
		}
		twitterCampaignManage.setGroupId(lineItemId);
		twitterCampaignManage.setTweetIds(tweetIds);
		twitterCampaignManage.setDailyBudget(Long.valueOf(twitterAdsDto.getDailyBudget()));
		twitterCampaignManage
				.setTotalBudget(twitterAdsDto.getTotalBudget() == 0 ? null : twitterAdsDto.getTotalBudget());
		twitterCampaignManageDao.insert(twitterCampaignManage);

		// DBにtweetListを保存
		for (TwitterTweet twitterTweet : twitterAdsDto.getTweetList()) {
			for (String tweetId : twitterAdsDto.getTweetIdList()) {
				if (tweetId.equals(twitterTweet.getTweetId())) {
					// Entity
					TwitterTweetList tweet = new TwitterTweetList();
					tweet.setAccountId(ContextUtil.getCurrentShop().getTwitterAccountId());
					tweet.setCampaignId(campaignId);
					tweet.setTweetId(tweetId);
					tweet.setTweetTitle(twitterTweet.getTweetTitle());
					tweet.setTweetBody(twitterTweet.getTweetBody());
					tweet.setPreviewUrl(twitterTweet.getPreviewUrl());
					twitterTweetListDao.insert(tweet);
				}
			}
		}

		// 案件追加
		Issue issue = new Issue();
		if (issueDto == null) {
			issue.setTwitterCampaignManageId(Long.valueOf(twitterCampaignManage.getTwitterCampaignManageId()));
			issue.setCampaignName(twitterAdsDto.getCampaignName());
			issue.setBudget(
					twitterAdsDto.getTotalBudget() == 0 ? CalculateUtil.calTotalBudget(twitterAdsDto.getDailyBudget(),
							twitterAdsDto.getStartTime(), twitterAdsDto.getEndTime()) : twitterAdsDto.getTotalBudget());
			issue.setStartDate(twitterAdsDto.getStartTime());
			issue.setEndDate(twitterAdsDto.getEndTime());
			issue.setShopId(ContextUtil.getCurrentShop().getShopId());
			issueDao.insert(issue);
		} else {
			issueDto.setTwitterCampaignManageId(Long.valueOf(twitterCampaignManage.getTwitterCampaignManageId()));
		}

		// メール送信
		EmailDto emailDto = new EmailDto();
		emailDto.setIssueId(issue.getIssueId());
		EmailCampDetailDto emailCampDetailDto = new EmailCampDetailDto();
		emailCampDetailDto.setCampaignId(twitterCampaignManage.getCampaignId());
		emailCampDetailDto.setCampaignName(twitterCampaignManage.getCampaignName());
		emailCampDetailDto.setMediaType(MediaCollection.TWITTER.getValue());
		List<EmailCampDetailDto> emailCampDetailDtoList = new ArrayList<EmailCampDetailDto>();
		emailCampDetailDtoList.add(emailCampDetailDto);
		emailDto.setCampaignList(emailCampDetailDtoList);
		// emailDto.setAttachment(image);
		emailDto.setTemplateType(EmailTemplateType.CAMPAIGN.getValue());
		emailService.sendEmail(emailDto);

	}

	// API : 支払い方法ID取得
	@Transactional
	private String getFundingInstrumentId() {

		String fundingInstrumentId = null;

		try {
			// Http request URL
			String url = applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
					+ "/funding_instruments";
			String method = "GET";
			// パラメーター
			SortedMap<String, String> parameters = new TreeMap<String, String>();
			// oauth Header
			String auth = getHeader(method, url, parameters);
			// Call API
			TwitterFundingInstrumentRes twitterFundingInstrumentRes = call(url, HttpMethod.GET, null, auth,
					TwitterFundingInstrumentRes.class);
			// 支払い方法ID
			fundingInstrumentId = twitterFundingInstrumentRes.getData().get(0).getId();

		} catch (Exception e1) {
			e1.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
		return fundingInstrumentId;
	}

	// API : キャンペーン作成
	@Transactional
	private String creatCampaign(String fundingInstrumentId, TwitterAdsDto twitterAdsDto) {

		String campaignId = null;
		String url = null;
		String url_after = null;
		String campaignStatus = null;

		// 店舗の承認フラグをチェック
		if (ContextUtil.getCurrentShop().getSalesCheckFlag().equals(ApprovalFlag.WAITING.getValue())) {
			campaignStatus = TwitterCampaignStatus.ACTIVE.getLabel();
		} else {
			campaignStatus = TwitterCampaignStatus.PAUSED.getLabel();
		}

		// 半角スペース削除
		String campaignName = (twitterAdsDto.getCampaignName()).replaceAll(" ", "");

		try {
			// Request Body
			TwitterCampaignReq body = new TwitterCampaignReq();
			body.setFunding_instrument_id(fundingInstrumentId);
			body.setName(campaignName);
			body.setStart_time(twitterAdsDto.getStartTime());
			body.setDaily_budget_amount_local_micro(String.valueOf(twitterAdsDto.getDailyBudget() * 1000000));
			body.setEntity_status(campaignStatus);
			// パラメーターの設定
			SortedMap<String, String> parameters = new TreeMap<String, String>();
			parameters.put("funding_instrument_id", TwitterUtil.urlEncode(fundingInstrumentId));
			parameters.put("name", TwitterUtil.urlEncode(campaignName));
			parameters.put("start_time", TwitterUtil.urlEncode(twitterAdsDto.getStartTime()));
			if (twitterAdsDto.getEndTime() != null) {
				parameters.put("end_time", TwitterUtil.urlEncode(twitterAdsDto.getEndTime()));
				body.setEnd_time(twitterAdsDto.getEndTime());
			}
			parameters.put("daily_budget_amount_local_micro",
					TwitterUtil.urlEncode(String.valueOf(twitterAdsDto.getDailyBudget() * 1000000)));// 10円
			if (twitterAdsDto.getTotalBudget() != 0) {
				parameters.put("total_budget_amount_local_micro",
						TwitterUtil.urlEncode(String.valueOf(twitterAdsDto.getTotalBudget() * 1000000)));// 2000円
				body.setTotal_budget_amount_local_micro(String.valueOf(twitterAdsDto.getTotalBudget() * 1000000));
			}
			parameters.put("entity_status", TwitterUtil.urlEncode(campaignStatus));

			// URLの設定
			url = applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
					+ applicationProperties.getTwitterCreatCampaign() + "?funding_instrument_id="
					+ TwitterUtil.urlEncode(fundingInstrumentId) + "&name=" + TwitterUtil.urlEncode(campaignName)
					+ "&start_time=" + TwitterUtil.urlEncode(twitterAdsDto.getStartTime())
					+ "&daily_budget_amount_local_micro="
					+ TwitterUtil.urlEncode(String.valueOf(twitterAdsDto.getDailyBudget() * 1000000))
					+ "&entity_status=" + TwitterUtil.urlEncode(campaignStatus);

			String endTimeUrl = "&end_time=" + TwitterUtil.urlEncode(twitterAdsDto.getEndTime());
			String totalBudgetUrl = "&total_budget_amount_local_micro="
					+ TwitterUtil.urlEncode(String.valueOf(twitterAdsDto.getTotalBudget() * 1000000));

			// URL：配信終了日と総予算の非空判断
			if (twitterAdsDto.getEndTime() == null && twitterAdsDto.getTotalBudget() == 0) {
				url_after = url;
			} else if (twitterAdsDto.getEndTime() == null && twitterAdsDto.getTotalBudget() != 0) {
				url_after = url + totalBudgetUrl;
			} else if (twitterAdsDto.getEndTime() != null && twitterAdsDto.getTotalBudget() == 0) {
				url_after = url + endTimeUrl;
			} else {
				url_after = url + endTimeUrl + totalBudgetUrl;
			}

			String method = "POST";
			// oauth Header
			String auth = getHeader(method,
					applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
							+ applicationProperties.getTwitterCreatCampaign(),
					parameters);
			// Call API
			TwitterCampaignRes twitterCampaignRes = call(url_after, HttpMethod.POST, body, auth,
					TwitterCampaignRes.class);
			// キャンペーンIDを取得
			campaignId = twitterCampaignRes.getData().getId();

		} catch (Exception e1) {
			e1.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		return campaignId;
	}

	private Long getAveragePrice(TwitterAdsDto twitterAdsDto) {
		Double clickPrice = null;
		Long averagePrice = null;
		switch (TwitterLocationType.of(twitterAdsDto.getLocation())) {
		case ALLCITY:
			if (twitterAdsDto.getObjective() == TwitterObjective.WEBSITE.getValue()) {
				clickPrice = CodeMasterServiceImpl.twitterRegionUnitPriceLinkClickList.stream()
						.mapToInt(obj -> obj.getSecond()).average().getAsDouble();
			} else {
				clickPrice = CodeMasterServiceImpl.twitterRegionUnitPriceFollowsList.stream()
						.mapToInt(obj -> obj.getSecond()).average().getAsDouble();
			}
			averagePrice = Math.round(clickPrice);
			break;
		case JAPAN:
			if (twitterAdsDto.getObjective() == TwitterObjective.WEBSITE.getValue()) {
				clickPrice = CodeMasterServiceImpl.twitterRegionUnitPriceLinkClickList.stream()
						.mapToInt(obj -> obj.getSecond()).average().getAsDouble();
			} else {
				clickPrice = CodeMasterServiceImpl.twitterRegionUnitPriceFollowsList.stream()
						.mapToInt(obj -> obj.getSecond()).average().getAsDouble();
			}

			averagePrice = Math.round(clickPrice);
			break;
		case REGION:
			if (twitterAdsDto.getObjective() == TwitterObjective.WEBSITE.getValue()) {
				clickPrice = CodeMasterServiceImpl.twitterRegionUnitPriceLinkClickList.stream()
						.filter(obj -> twitterAdsDto.getRegions().contains(obj.getFirst()))
						.mapToInt(obj -> obj.getSecond()).average().getAsDouble();
			} else {
				clickPrice = CodeMasterServiceImpl.twitterRegionUnitPriceFollowsList.stream()
						.filter(obj -> twitterAdsDto.getRegions().contains(obj.getFirst()))
						.mapToInt(obj -> obj.getSecond()).average().getAsDouble();
			}
			averagePrice = Math.round(clickPrice);
			break;
		}
		return averagePrice;
	}

	// API : グループ作成
	@Transactional
	private String creatgroup(String campaignId, TwitterAdsDto twitterAdsDto) {

		String lineItemId = null;
		Long averagePrice = null;
		try {
			// キャンペーン目的がwebsiteの場合
			if (twitterAdsDto.getObjective() == TwitterObjective.WEBSITE.getValue()) {
				// 平均単価を精算する
				averagePrice = getAveragePrice(twitterAdsDto);
				SortedMap<String, String> parameters = new TreeMap<String, String>();
				parameters.put("campaign_id", TwitterUtil.urlEncode(campaignId));
				parameters.put("objective", TwitterUtil.urlEncode("WEBSITE_CLICKS"));
				parameters.put("placements", TwitterUtil.urlEncode("ALL_ON_TWITTER"));
				parameters.put("product_type", TwitterUtil.urlEncode("PROMOTED_TWEETS"));
				parameters.put("bid_type", TwitterUtil.urlEncode("MAX"));
				parameters.put("bid_amount_local_micro", TwitterUtil.urlEncode(String.valueOf(averagePrice * 1000000)));

				String url = applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
						+ applicationProperties.getTwitterCreatgroup() + "?campaign_id="
						+ TwitterUtil.urlEncode(campaignId) + "&objective=" + TwitterUtil.urlEncode("WEBSITE_CLICKS")
						+ "&placements=" + TwitterUtil.urlEncode("ALL_ON_TWITTER") + "&product_type="
						+ TwitterUtil.urlEncode("PROMOTED_TWEETS") + "&bid_type=" + TwitterUtil.urlEncode("MAX")
						+ "&bid_amount_local_micro=" + TwitterUtil.urlEncode(String.valueOf(averagePrice * 1000000));

				String method = "POST";
				String auth = getHeader(method,
						applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
								+ applicationProperties.getTwitterCreatgroup(),
						parameters);
				// create Campaign request body
				TwitterGroupReq body = new TwitterGroupReq();
				body.setCampaign_id(campaignId);
				body.setObjective("WEBSITE_CLICKS");
				body.setPlacements("ALL_ON_TWITTER");
				body.setProduct_type("PROMOTED_ACCOUNT");
				body.setBid_type("MAX");
				body.setBid_amount_local_micro(String.valueOf(averagePrice * 1000000));
				// Call API
				TwitterGroupRes twitterGroupRes = call(url, HttpMethod.POST, body, auth, TwitterGroupRes.class);
				lineItemId = twitterGroupRes.getData().getId();

				// キャンペーン目的がfollowersの場合
			} else {
				averagePrice = getAveragePrice(twitterAdsDto);
				// パラーメター
				SortedMap<String, String> parameters = new TreeMap<String, String>();
				parameters.put("campaign_id", TwitterUtil.urlEncode(campaignId));
				parameters.put("objective", TwitterUtil.urlEncode("FOLLOWERS"));
				parameters.put("placements", TwitterUtil.urlEncode("ALL_ON_TWITTER"));
				parameters.put("product_type", TwitterUtil.urlEncode("PROMOTED_ACCOUNT"));
				parameters.put("bid_type", TwitterUtil.urlEncode("TARGET"));
				parameters.put("bid_amount_local_micro", TwitterUtil.urlEncode(String.valueOf(averagePrice * 1000000)));
				// Http Request URL
				String url = applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
						+ applicationProperties.getTwitterCreatgroup() + "?campaign_id="
						+ TwitterUtil.urlEncode(campaignId) + "&objective=" + TwitterUtil.urlEncode("FOLLOWERS")
						+ "&placements=" + TwitterUtil.urlEncode("ALL_ON_TWITTER") + "&product_type="
						+ TwitterUtil.urlEncode("PROMOTED_ACCOUNT") + "&bid_type=" + TwitterUtil.urlEncode("TARGET")
						+ "&bid_amount_local_micro=" + TwitterUtil.urlEncode(String.valueOf(averagePrice * 1000000));
				String method = "POST";
				// oauth Header
				String auth = getHeader(method,
						applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
								+ applicationProperties.getTwitterCreatgroup(),
						parameters);
				// Request body
				TwitterGroupReq body = new TwitterGroupReq();
				body.setCampaign_id(campaignId);
				body.setObjective("FOLLOWERS");
				body.setPlacements("ALL_ON_TWITTER");
				body.setProduct_type("PROMOTED_ACCOUNT");
				body.setBid_type("TARGET");
				body.setBid_amount_local_micro(String.valueOf(averagePrice * 1000000));
				// Call API
				TwitterGroupRes twitterGroupRes = call(url, HttpMethod.POST, body, auth, TwitterGroupRes.class);
				lineItemId = twitterGroupRes.getData().getId();
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
		return lineItemId;
	}

	// API : ツイート設定
	@Transactional
	private void promoteTweets(String lineItemId, TwitterAdsDto twitterAdsDto) {

		// ツイートID文字列(カンマ)
		String tweetIds = TwitterUtil.formatToOneString(twitterAdsDto.getTweetIdList());
		try {
			// パラメーター
			SortedMap<String, String> parameters = new TreeMap<String, String>();
			parameters.put("line_item_id", TwitterUtil.urlEncode(lineItemId));
			parameters.put("tweet_ids", TwitterUtil.urlEncode(tweetIds));
			// Http request URL
			String url = applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
					+ applicationProperties.getTwitterPromoteTweets() + "?line_item_id="
					+ TwitterUtil.urlEncode(lineItemId) + "&tweet_ids=" + TwitterUtil.urlEncode(tweetIds);
			String method = "POST";
			// oauth Header
			String auth = getHeader(method,
					applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
							+ applicationProperties.getTwitterPromoteTweets(),
					parameters);
			// Rquest Body
			TwitterPromotTweetsReq body = new TwitterPromotTweetsReq();
			body.setLine_item_id(lineItemId);
			body.setTweet_ids(tweetIds);
			// Call API
			call(url, HttpMethod.POST, body, auth, TwitterPromotTweetsRes.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

	}

	// API : ターゲット設定
	@Transactional
	private void setTarget(String lineItemId, TwitterAdsDto twitterAdsDto) {

		try {
			// 署名用パラメータ
			SortedMap<String, String> parameters = new TreeMap<String, String>();
			//// Http request URL
			String url = "https://ads-api.twitter.com/4/batch/accounts/"
					+ ContextUtil.getCurrentShop().getTwitterAccountId() + "/targeting_criteria";
			String method = "POST";
			// oauth Header
			String auth = getHeader(method, "https://ads-api.twitter.com/4/batch/accounts/"
					+ ContextUtil.getCurrentShop().getTwitterAccountId() + "/targeting_criteria", parameters);
			List<TwitterTargetObjReq> twitterTargetList = new ArrayList<>();
			// ターゲット : 地域
			switch (TwitterLocationType.of(twitterAdsDto.getLocation())) {
			// すべての国と地域の場合
			case ALLCITY:
				break;
			// 日本の場合
			case JAPAN:
				TwitterTargetObjReq japanTarget = new TwitterTargetObjReq();
				japanTarget.setOperation_type("Create");
				TwitterTargetParamsReq japanParams = new TwitterTargetParamsReq();
				japanParams.setLine_item_id(lineItemId);
				japanParams.setTargeting_type("LOCATION");
				japanParams.setTargeting_value(japanTargetKey);
				japanTarget.setParams(japanParams);
				twitterTargetList.add(japanTarget);
				break;
			// 地域の場合
			case REGION:
				List<String> regionList = twitterAdsDto.getRegions();
				for (String region : regionList) {
					TwitterTargetObjReq regionTarget = new TwitterTargetObjReq();
					regionTarget.setOperation_type("Create");
					TwitterTargetParamsReq regionParams = new TwitterTargetParamsReq();
					regionParams.setLine_item_id(lineItemId);
					regionParams.setTargeting_type("LOCATION");
					regionParams.setTargeting_value(region);
					regionTarget.setParams(regionParams);
					twitterTargetList.add(regionTarget);
				}
				break;
			}
			// ターゲット : キーワード
			List<String> keywordList = CodeMasterServiceImpl.keywordNameList;
			for (String keyword : keywordList) {
				TwitterTargetObjReq keywordTarget = new TwitterTargetObjReq();
				keywordTarget.setOperation_type("Create");
				TwitterTargetParamsReq keywordParams = new TwitterTargetParamsReq();
				keywordParams.setLine_item_id(lineItemId);
				keywordParams.setTargeting_type("BROAD_KEYWORD");
				keywordParams.setTargeting_value(keyword);
				keywordTarget.setParams(keywordParams);
				twitterTargetList.add(keywordTarget);
			}

			// ターゲット : フォロワーと類似ユーザー
			// ターゲット : 年齢：18歳以上
			TwitterTargetObjReq twitterTargetObjReq = new TwitterTargetObjReq();
			twitterTargetObjReq.setOperation_type("Create");
			TwitterTargetParamsReq params = new TwitterTargetParamsReq();
			params.setLine_item_id(lineItemId);
			params.setTargeting_type("AGE");
			params.setTargeting_value("AGE_OVER_18");
			twitterTargetObjReq.setParams(params);
			twitterTargetList.add(twitterTargetObjReq);

			// Request Body
			TwitterTargetReq body = new TwitterTargetReq();
			body.setRequest(twitterTargetList);
			// call API
			call(url, HttpMethod.POST, body.getRequest(), auth, TwitterPromotTweetsRes.class);

		} catch (Exception e1) {
			e1.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

	}

	// 広告削除(停止の状態にする)
	@Override
	@Transactional
	public void deleteAds(String campaignId, Long issueId) {

		// Call API: campaignIdでcampaign情報を取得する
		TwitterCampaignData campaignData = getCampaignById(campaignId);
		// campaignがconsoleから見つからなかった場合（もしくはdeleteされた）
		if (Objects.nonNull(campaignData.getId())) {
			// campaignステータスを判断する
			if (campaignData.getReasons_not_servable().isEmpty() == true
					|| campaignData.getReasons_not_servable().isEmpty() == false
							&& (TwitterCampaignStatus.RESERVATION.getLabel())
									.equals(campaignData.getReasons_not_servable().get(0))
							&& campaignData.getEntity_status().equals(TwitterCampaignStatus.ACTIVE.getLabel())) {
				// Call API: campaignステータスを停止状態にする
				changeAdsStatus(campaignId, TwitterCampaignStatus.PAUSED.getLabel());
			}
		}
		// DB更新：論理削除
		// tbl: twitter_campaign_manage
		TwitterCampaignManage twitterCampaignManage = twitterCampaignManageCustomDao.selectByCampaignId(campaignId);
		twitterCampaignManage.setIsActived(Flag.OFF.getValue());
		twitterCampaignManageDao.update(twitterCampaignManage);
		// tbl: issue
		Issue issue = issueDao.selectById(issueId);
		issue.setIsActived(Flag.OFF.getValue());
		issueDao.update(issue);

	}

	// GETキャンペーン
	private TwitterCampaignData getCampaignById(String campaignId) {

		TwitterCampaignData twitterCampaignData = new TwitterCampaignData();
		// Call API
		TwitterCampaignDataRes twitterCampaignDataRes = getCampaign(campaignId);
		List<TwitterCampaignData> twitterCampaignDataList = twitterCampaignDataRes.getData();
		for (TwitterCampaignData obj : twitterCampaignDataList) {
			twitterCampaignData.setId(obj.getId());
			twitterCampaignData.setName(obj.getName());
			twitterCampaignData.setStart_time(obj.getStart_time());
			twitterCampaignData.setEnd_time(obj.getEnd_time());
			twitterCampaignData.setDaily_budget_amount_local_micro(obj.getDaily_budget_amount_local_micro());
			twitterCampaignData.setTotal_budget_amount_local_micro(obj.getTotal_budget_amount_local_micro());
			twitterCampaignData.setEntity_status(obj.getEntity_status());
			twitterCampaignData.setServable(obj.getServable());
			twitterCampaignData.setReasons_not_servable(obj.getReasons_not_servable());
		}
		return twitterCampaignData;

	}

	// API : GETキャンペーン
	private TwitterCampaignDataRes getCampaign(String campaignIds) {

		TwitterCampaignDataRes twitterCampaignDataRes = new TwitterCampaignDataRes();
		try {
			// Http request URL
			String url = applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
					+ applicationProperties.getTwitterCreatCampaign() + "?campaign_ids=" + campaignIds;
			// oauth URL
			String url_oauth = applicationProperties.getTwitterhost()
					+ ContextUtil.getCurrentShop().getTwitterAccountId()
					+ applicationProperties.getTwitterCreatCampaign();
			String method = "GET";
			// oauth用 パラメータ
			SortedMap<String, String> parameters = new TreeMap<String, String>();
			parameters.put("campaign_ids", TwitterUtil.urlEncode(campaignIds));
			// Twitter oauth用 Header
			String auth = getHeader(method, url_oauth, parameters);
			// Request Body
			TwitterCampaignIdReq body = new TwitterCampaignIdReq();
			body.setCampaign_ids(campaignIds);
			// Call API
			twitterCampaignDataRes = call(url, HttpMethod.GET, body, auth, TwitterCampaignDataRes.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
		return twitterCampaignDataRes;
	}

	// API : 広告ステータスの変更
	@Override
	@Transactional
	public void changeAdsStatus(String campaignId, String switchFlag) {

		try {
			// パラメーターの設定
			SortedMap<String, String> parameters = new TreeMap<String, String>();
			parameters.put("entity_status", TwitterUtil.urlEncode(switchFlag));
			// call_url
			String call_url = applicationProperties.getTwitterhost()
					+ ContextUtil.getCurrentShop().getTwitterAccountId()
					+ applicationProperties.getTwitterChangeCampaignStatus() + campaignId + "?entity_status="
					+ switchFlag;
			// auth_url
			String auth_url = applicationProperties.getTwitterhost()
					+ ContextUtil.getCurrentShop().getTwitterAccountId()
					+ applicationProperties.getTwitterChangeCampaignStatus() + campaignId;
			// Request body
			TwitterCampaignReq body = new TwitterCampaignReq();
			body.setEntity_status(switchFlag);
			String method = "PUT";
			// oauth Header
			String auth = getHeader(method, auth_url, parameters);
			// Call API
			call(call_url, HttpMethod.PUT, body, auth, TwitterCampaignRes.class);
			// キャンペーン情報更新（DB）
			TwitterCampaignManage twitterCampaignManage = twitterCampaignManageCustomDao.selectByCampaignId(campaignId);
			if (twitterCampaignManage.getApprovalFlag().equals(ApprovalFlag.WAITING.getValue())) {
				// 承認フラグ設定
				if (switchFlag.equals(TwitterCampaignStatus.ACTIVE.getLabel())) {
					twitterCampaignManage.setApprovalFlag(ApprovalFlag.COMPLETED.getValue());
					twitterCampaignManageDao.update(twitterCampaignManage);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
	}

	// API: すべてのWEBSITEツイートリストを検索
	@Transactional
	@Override
	public List<TwitterTweet> searchWebsiteTweets() {

		List<TwitterTweet> tweetList = new ArrayList<>();
		try {
			// Http Request URL
			String url = applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
					+ applicationProperties.getTwitterSearchTimeline() + "?objective=WEBSITE_CLICKS&count=50";
			// oauth URL
			String url1 = applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
					+ applicationProperties.getTwitterSearchTimeline();
			String method = "GET";
			// パラーメター
			SortedMap<String, String> parameters = new TreeMap<String, String>();
			parameters.put("objective", TwitterUtil.urlEncode("WEBSITE_CLICKS"));
			parameters.put("count", "50");
			// oauth Hearder
			String auth = getHeader(method, url1, parameters);
			// Request Body
			TwitterTweetListReq body = new TwitterTweetListReq();
			body.setObjective("WEBSITE_CLICKS");
			body.setCount("50");
			// Call API
			TwitterTweetListRes websiteTweetListRes = call(url, HttpMethod.GET, body, auth, TwitterTweetListRes.class);
			List<TwitterTweets> twitterTweetList = websiteTweetListRes.getData();
			String tweetBody;
			// ツイート内容の処理
			for (TwitterTweets twitterTweet : twitterTweetList) {
				tweetBody = twitterTweet.getText();
				for (TweetUrls tweetUrls : twitterTweet.getEntities().getUrls()) {
					tweetBody = tweetBody.replaceAll(tweetUrls.getUrl(), tweetUrls.getExpanded_url());
				}
				TwitterTweet tweet = new TwitterTweet();
				tweet.setTweetId(twitterTweet.getId_str());
				tweet.setTweetTitle(
						twitterTweet.getUser().getName() + "  " + "@" + twitterTweet.getUser().getScreen_name() + " ・ "
								+ TwitterUtil.getTwitterDate(twitterTweet.getCreated_at()));
				tweet.setTweetBody(tweetBody);
				tweet.setPreviewUrl(
						applicationProperties.getTwitterPreviewHost() + twitterTweet.getUser().getScreen_name()
								+ applicationProperties.getTwitterPreviewPath() + twitterTweet.getId_str());
				tweetList.add(tweet);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		return tweetList;

	}

	// API: すべてのFOLLOWERSツイートリストを検索
	@Transactional
	@Override
	public List<TwitterTweet> searchFollowersTweets() {

		List<TwitterTweet> tweetList = new ArrayList<>();

		try {
			// Http Request URL
			String url = applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
					+ applicationProperties.getTwitterSearchTimeline() + "?objective=FOLLOWERS&count=50";
			// oauth URL
			String url1 = applicationProperties.getTwitterhost() + ContextUtil.getCurrentShop().getTwitterAccountId()
					+ applicationProperties.getTwitterSearchTimeline();
			String method = "GET";
			// パラメーター
			SortedMap<String, String> parameters = new TreeMap<String, String>();
			parameters.put("objective", TwitterUtil.urlEncode("FOLLOWERS"));
			parameters.put("count", "50");
			// oauth Header
			String auth = getHeader(method, url1, parameters);
			// Request Body
			TwitterTweetListReq body = new TwitterTweetListReq();
			body.setObjective("FOLLOWERS");
			body.setCount("50");
			// Call API
			TwitterTweetListRes followersTweetListRes = call(url, HttpMethod.GET, body, auth,
					TwitterTweetListRes.class);
			List<TwitterTweets> twitterTweetList = followersTweetListRes.getData();
			String tweetBody;
			// ツイート内容の処理
			for (TwitterTweets twitterTweet : twitterTweetList) {
				tweetBody = twitterTweet.getText();
				for (TweetUrls tweetUrls : twitterTweet.getEntities().getUrls()) {
					tweetBody = tweetBody.replaceAll(tweetUrls.getUrl(), tweetUrls.getExpanded_url());
				}
				TwitterTweet tweet = new TwitterTweet();
				tweet.setTweetId(twitterTweet.getId_str());
				tweet.setTweetTitle(
						twitterTweet.getUser().getName() + "  " + "@" + twitterTweet.getUser().getScreen_name() + " ・ "
								+ TwitterUtil.getTwitterDate(twitterTweet.getCreated_at()));
				tweet.setTweetBody(tweetBody);
				tweet.setPreviewUrl(
						applicationProperties.getTwitterPreviewHost() + twitterTweet.getUser().getScreen_name()
								+ applicationProperties.getTwitterPreviewPath() + twitterTweet.getId_str());
				tweetList.add(tweet);

			}

		} catch (Exception e1) {
			e1.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		return tweetList;
	}

	/**
	 * Authentication：Headerの取得
	 * 
	 * @param httpMethod
	 * @param baseUrl
	 * @param parameters
	 **/
	public String getHeader(String httpMethod, String baseUrl, SortedMap<String, String> parameters) {

		// ログイン中店舗IDによってTwitterのTokenとapiのkey,secretを取得
		String consumerkey = applicationProperties.getTwitterApiKey();
		String consumerSecret = applicationProperties.getTwitterApiSecretKey();
		String oauthToken = applicationProperties.getTwitterAccessToken();
		String oauthTokenSecret = applicationProperties.getTwitterAccessTokenSecret();

		String method = httpMethod;
		String urlStr = baseUrl;

		// oauth* 共通パラメーター
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put("oauth_consumer_key", consumerkey);
		params.put("oauth_signature_method", "HMAC-SHA1");
		params.put("oauth_timestamp", String.valueOf(getUnixTime()));
		params.put("oauth_nonce", String.valueOf(Math.random()));
		params.put("oauth_version", "1.0");
		params.put("oauth_token", oauthToken);

		// Map parameters
		Set<Entry<String, String>> set = parameters.entrySet();
		for (Iterator<Entry<String, String>> it = set.iterator(); it.hasNext();) {
			SortedMap.Entry<String, String> entry = it.next();
			params.put(entry.getKey(), entry.getValue());
		}

		{
			/*
			 * 署名（oauth_signature）の生成 リクエストトークン取得時と全く同じ処理
			 */
			String paramStr = "";
			for (Entry<String, String> param : params.entrySet()) {
				paramStr += "&" + param.getKey() + "=" + param.getValue();
			}
			paramStr = paramStr.substring(1);

			String text = method + "&" + TwitterUtil.urlEncode(urlStr) + "&" + TwitterUtil.urlEncode(paramStr);

			String key = TwitterUtil.urlEncode(consumerSecret) + "&" + TwitterUtil.urlEncode(oauthTokenSecret);

			params.put("oauth_signature", genHMAC(text, key));
		}

		// urlに含めるprametersをparamsから削除する
		SortedMap<String, String> guavaMap = getDifferenceSetByGuava(params, parameters);

		// Authorizationヘッダの作成
		String paramStr = "";
		for (Entry<String, String> param : guavaMap.entrySet()) {
			paramStr += ", " + param.getKey() + "=\"" + TwitterUtil.urlEncode(param.getValue()) + "\"";
		}
		paramStr = paramStr.substring(2);
		String authorizationHeader = "OAuth " + paramStr;

		// System.out.println("header : " + authorizationHeader);

		return authorizationHeader;
	}

	/**
	 * Timestampの取得
	 **/
	private static int getUnixTime() {
		return (int) (System.currentTimeMillis() / 1000L);
	}

	/**
	 * HMAC-SHA1方法でサイン
	 **/
	private static String genHMAC(String text, String key) {
		byte[] result = null;
		try {
			final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
			// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
			SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
			// 生成一个指定 Mac 算法 的 Mac 对象
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			// 用给定密钥初始化 Mac 对象
			mac.init(signinKey);
			// 完成 Mac 操作
			byte[] rawHmac = mac.doFinal(text.getBytes());
			result = Base64.encodeBase64(rawHmac);

		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
		} catch (InvalidKeyException e) {
			System.err.println(e.getMessage());
		}
		if (null != result) {
			return new String(result);
		} else {
			return null;
		}
	}

	/**
	 * Map差分
	 **/
	private static SortedMap<String, String> getDifferenceSetByGuava(SortedMap<String, String> bigMap,
			SortedMap<String, String> smallMap) {
		Set<String> bigMapKey = bigMap.keySet();
		Set<String> smallMapKey = smallMap.keySet();
		Set<String> differenceSet = Sets.difference(bigMapKey, smallMapKey);
		SortedMap<String, String> result = Maps.newTreeMap();
		for (String key : differenceSet) {
			result.put(key, bigMap.get(key));
		}

		return result;
	}

}
