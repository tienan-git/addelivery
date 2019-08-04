package jp.acepro.haishinsan.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.ByteSource;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.DspSegmentCustomDao;
import jp.acepro.haishinsan.dao.DspTemplateCustomDao;
import jp.acepro.haishinsan.dao.FacebookTemplateCustomDao;
import jp.acepro.haishinsan.dao.GoogleTemplateCustomDao;
import jp.acepro.haishinsan.dao.IssueCustomDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.dao.TwitterTemplateCustomDao;
import jp.acepro.haishinsan.db.entity.DspTemplate;
import jp.acepro.haishinsan.db.entity.FacebookTemplate;
import jp.acepro.haishinsan.db.entity.GoogleTemplate;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.SegmentManage;
import jp.acepro.haishinsan.db.entity.TwitterTemplate;
import jp.acepro.haishinsan.dto.BudgetAllocationDto;
import jp.acepro.haishinsan.dto.IconDto;
import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDto;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.dto.twitter.TwitterAdsDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.enums.DeviceType;
import jp.acepro.haishinsan.enums.FacebookArrangePlace;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.MediaType;
import jp.acepro.haishinsan.enums.UnitPriceType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.DspCampaignCreInputForm;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.google.GoogleCampaignService;
import jp.acepro.haishinsan.service.twitter.TwitterApiService;
import jp.acepro.haishinsan.util.CalculateUtil;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.ImageUtil;

@Service
public class IssueServiceImpl implements IssueService {

	@Autowired
	HttpSession session;

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	IssueInsertService issueInsertService;

	@Autowired
	IssueDao issueDao;

	@Autowired
	IssueCustomDao issueCustomDao;

	@Autowired
	DspCampaignService dspCampaignService;

	@Autowired
	DspSegmentCustomDao dspSegmentCustomDao;

	@Autowired
	GoogleCampaignService googleCampaignService;

	@Autowired
	ImageUtil imageUtil;

	@Autowired
	FacebookService facebookService;

	@Autowired
	TwitterApiService twitterApiService;

	@Autowired
	DspTemplateCustomDao dspTemplateCustomDao;

	@Autowired
	GoogleTemplateCustomDao googleTemplateCustomDao;

	@Autowired
	FacebookTemplateCustomDao facebookTemplateCustomDao;

	@Autowired
	TwitterTemplateCustomDao twitterTemplateCustomDao;

	@Override
	@Transactional
	public List<IssueDto> issueList() {

		List<Issue> issueList = issueCustomDao.selectByShopId(ContextUtil.getCurrentShop().getShopId());

		List<IssueDto> issueDtoList = new ArrayList<IssueDto>();
		List<IconDto> iconList = new ArrayList<IconDto>();
		for (Issue issue : issueList) {
			IssueDto issueDto = new IssueDto();
			issueDto.setIssueId(issue.getIssueId());
			issueDto.setCampaignName(issue.getCampaignName());
			issueDto.setBudget(issue.getBudget());
			if (issue.getDspCampaignManageId() != null) {
				issueDto.setDspSelected(true);
				issueDto.setDspCampaignManageId(issue.getDspCampaignManageId());
				IconDto envira = new IconDto();
				envira.setIssueId(issue.getIssueId());
				envira.setIcon("fa fa-envira");
				iconList.add(envira);
			}
			if (issue.getGoogleCampaignManageId() != null) {
				issueDto.setGoogleSelected(true);
				issueDto.setGoogleCampaignManageId(issue.getGoogleCampaignManageId());
				IconDto google = new IconDto();
				google.setIssueId(issue.getIssueId());
				google.setIcon("fa fa-google");
				iconList.add(google);
			}
			if (issue.getFacebookCampaignManageId() != null) {
				issueDto.setFacebookSelected(true);
				issueDto.setFacebookCampaignManageId(issue.getFacebookCampaignManageId());
				IconDto facebook = new IconDto();
				facebook.setIssueId(issue.getIssueId());
				facebook.setIcon("fa fa-facebook");
				iconList.add(facebook);
			}
			if (issue.getTwitterCampaignManageId() != null) {
				issueDto.setTwitterSelected(true);
				issueDto.setTwitterCampaignManageId(issue.getTwitterCampaignManageId());
				IconDto twitter = new IconDto();
				twitter.setIssueId(issue.getIssueId());
				twitter.setIcon("fa fa-twitter");
				iconList.add(twitter);
			}
			if (issue.getYahooCampaignManageId() != null) {
				issueDto.setYahooSelected(true);
				issueDto.setYahooCampaignManageId(issue.getYahooCampaignManageId());
				IconDto yahoo = new IconDto();
				yahoo.setIssueId(issue.getIssueId());
				yahoo.setIcon("icofont icofont-social-yahoo");
				iconList.add(yahoo);
			}
			if (issue.getYoutubeCampaignManageId() != null) {
				issueDto.setYoutubeSelected(true);
				issueDto.setYoutubeCampaignManageId(issue.getYoutubeCampaignManageId());
				IconDto youtube = new IconDto();
				youtube.setIssueId(issue.getIssueId());
				youtube.setIcon("fa fa-youtube-play");
				iconList.add(youtube);
			}
			issueDto.setStartDate(issue.getStartDate());
			issueDto.setEndDate(issue.getEndDate());
			issueDto.setIconList(iconList);
			issueDtoList.add(issueDto);
		}
		return issueDtoList;

	}

	@Override
	@Transactional
	public void createIssue(IssueDto issueDto) {

		// 各媒体の予算分配を算出
		BudgetAllocationDto budgetAllocationDto = CalculateUtil.calCtr(issueDto.getBudget(), issueDto.isDspSelected(), issueDto.isGoogleSelected(), issueDto.isFacebookSelected(), issueDto.isTwitterSelected());

		// SegmentIdでDBからセグメント情報取得
		SegmentManage segmentManage = dspSegmentCustomDao.selectBySegmentId(issueDto.getSegmentId());
		issueDto.setUrl(segmentManage.getUrl());

		// DSPキャンペーン作成
		if (issueDto.isDspSelected()) {
			// キャンペーン作成を実施
			try {
				// テンプレート取得
				List<DspTemplate> dspTemplateList = dspTemplateCustomDao.selectAll();
				if (dspTemplateList == null || dspTemplateList.size() == 0) {
					throw new BusinessException(ErrorCodeConstant.E00016);
				}
				DspCampaignDto dspCampaignDto = new DspCampaignDto();
				dspCampaignDto.setCampaignName(issueDto.getCampaignName());
				dspCampaignDto.setStartDatetime(issueDto.getStartDate());
				dspCampaignDto.setEndDatetime(issueDto.getEndDate());
				dspCampaignDto.setBudget(budgetAllocationDto.getDspBudget());
				dspCampaignDto.setDeviceType(Integer.valueOf(DeviceType.ALL.getValue()));
				dspCampaignDto.setIdList(issueDto.getIdList());
				dspCampaignDto.setUrl(issueDto.getUrl());
				dspCampaignDto.setTemplateId(dspTemplateList.get(0).getTemplateId());
				dspCampaignService.createDspCampaign(dspCampaignDto, issueDto);
			} catch (BusinessException e) {
				e.printStackTrace();
				issueDto.setDspErrorCode(e.getMessage());
			}
		}

		// Googleキャンペーン作成
		if (issueDto.isGoogleSelected()) {
			// キャンペーン作成を実施
			try {
				if (CodeMasterServiceImpl.googleAreaNameList == null) {
					codeMasterService.getGoogleAreaList();
				}
				// テンプレート取得
				List<GoogleTemplate> googleTemplateList = googleTemplateCustomDao.selectByShopId(ContextUtil.getCurrentShop().getShopId());
				if (googleTemplateList == null || googleTemplateList.size() == 0) {
					throw new BusinessException(ErrorCodeConstant.E00013);
				}
				// 地域設定
				List<String> strList = new ArrayList<String>();
				List<Long> longList = new ArrayList<Long>();
				if (googleTemplateList.get(0).getLocationList() != null && !googleTemplateList.get(0).getLocationList().isEmpty()) {
					strList = Arrays.asList(googleTemplateList.get(0).getLocationList().split(","));
					longList = strList.stream().map(Long::parseLong).collect(Collectors.toList());
				}

				// キャンプーン作成用パラメタ設定（画像以外）
				GoogleCampaignDto googleCampaignDto = new GoogleCampaignDto();
				googleCampaignDto.setCampaignName(issueDto.getCampaignName());
				googleCampaignDto.setStartDate(issueDto.getStartDate());
				googleCampaignDto.setEndDate(issueDto.getEndDate());
				googleCampaignDto.setBudget(budgetAllocationDto.getGoogleBudget());
				googleCampaignDto.setDeviceType(DeviceType.ALL.getValue());
				googleCampaignDto.setLocationList(longList);
				googleCampaignDto.setUnitPriceType(UnitPriceType.CLICK.getValue());

				googleCampaignDto.setAdType(issueDto.getAdType());

				googleCampaignDto.setResAdImageBytesList(new ArrayList<byte[]>());
				googleCampaignDto.setResAdImageFileList(issueDto.getImageAdImageFileList());
				googleCampaignDto.setResAdDescription(issueDto.getResAdDescription());
				googleCampaignDto.setResAdFinalPageUrl(issueDto.getUrl());
				googleCampaignDto.setResAdShortTitle(issueDto.getResAdShortTitle());

				googleCampaignDto.setImageAdImageBytesList(new ArrayList<byte[]>());
				googleCampaignDto.setImageAdImageFileList(issueDto.getImageAdImageFileList());
				googleCampaignDto.setImageAdFinalPageUrl(issueDto.getUrl());

				googleCampaignDto.setTextAdDescription(issueDto.getTextAdDescription());
				googleCampaignDto.setTextAdFinalPageUrl(issueDto.getUrl());
				googleCampaignDto.setTextAdTitle1(issueDto.getTextAdTitle1());
				googleCampaignDto.setTextAdTitle2(issueDto.getTextAdTitle2());

				// キャンプーン作成用パラメタ設定（画像）
				switch (GoogleAdType.of(issueDto.getAdType())) {
				case RESPONSIVE:
					for (MultipartFile imageFile : issueDto.getResAdImageFileList()) {
						googleCampaignDto.getResAdImageBytesList().add(getByteArrayFromStream(imageFile.getInputStream()));
					}
					break;
				case IMAGE:
					for (MultipartFile imageFile : issueDto.getImageAdImageFileList()) {
						googleCampaignDto.getImageAdImageBytesList().add(getByteArrayFromStream(imageFile.getInputStream()));
					}
					break;
				case TEXT:
					break;
				}
				// キャンプーン作成
				googleCampaignService.createGoogleCampaign(googleCampaignDto, issueDto);

			} catch (Exception e) {
				e.printStackTrace();
				issueDto.setGoogleErrorCode(e.getMessage());
			}
		}

		// Facebookキャンペーン作成
		if (issueDto.isFacebookSelected()) {
			try {
				if (CodeMasterServiceImpl.facebookAreaNameList == null) {
					codeMasterService.getFacebookAreaList();
				}
				// テンプレート取得
				List<FacebookTemplate> facebookTemplateList = facebookTemplateCustomDao.selectAll(ContextUtil.getCurrentShop().getShopId());
				if (facebookTemplateList == null || facebookTemplateList.size() == 0) {
					throw new BusinessException(ErrorCodeConstant.E00014);
				}
				// 地域設定
				List<String> strList = new ArrayList<String>();
				List<Long> longList = new ArrayList<Long>();
				if (facebookTemplateList.get(0).getGeolocation() != null && !facebookTemplateList.get(0).getGeolocation().isEmpty()) {
					strList = Arrays.asList(facebookTemplateList.get(0).getGeolocation().split(","));
					longList = strList.stream().map(Long::parseLong).collect(Collectors.toList());
				}

				imageUtil.getImageBytes(issueDto.getFacebookImage(), MediaType.FACEBOOK.getValue());
				File imageFile = new File(issueDto.getFacebookImage().getOriginalFilename());
				FileOutputStream fo = new FileOutputStream(imageFile);
				fo.write(issueDto.getFacebookImage().getBytes());
				fo.close();

				FbCampaignDto fbCampaignDto = new FbCampaignDto();
				fbCampaignDto.setCampaignName(issueDto.getCampaignName());
				fbCampaignDto.setStartDate(issueDto.getStartDate());
				fbCampaignDto.setEndDate(issueDto.getEndDate());
				fbCampaignDto.setDailyBudget(CalculateUtil.calOneDayBudget(budgetAllocationDto.getFacebookBudget(), issueDto.getStartDate(), issueDto.getEndDate()));
				fbCampaignDto.setUnitPriceType(UnitPriceType.CLICK.getValue());
				fbCampaignDto.setArrangePlace(FacebookArrangePlace.BOTH.getValue());
				fbCampaignDto.setLocationList(longList);
				fbCampaignDto.setLinkUrl(issueDto.getUrl());
				fbCampaignDto.setSegmentId(issueDto.getSegmentId());
				fbCampaignDto.setImageFile(imageFile);
				facebookService.createFacebookCampaign(fbCampaignDto, issueDto);
			} catch (Exception e) {
				e.printStackTrace();
				issueDto.setFacebookErrorCode(e.getMessage());
				BusinessException businessException = (BusinessException) e;
				issueDto.setFacebookParam(businessException.getParams());
			}
		}

		// Twitterキャンペーン作成
		if (issueDto.isTwitterSelected()) {
			try {

				if (CodeMasterServiceImpl.twitterRegionList == null) {
					codeMasterService.searchRegions();
				}
				// テンプレート取得
				List<TwitterTemplate> twitterTemplateList = twitterTemplateCustomDao.selectAll(ContextUtil.getCurrentShop().getShopId());
				if (twitterTemplateList == null || twitterTemplateList.size() == 0) {
					throw new BusinessException(ErrorCodeConstant.E00015);
				}
				// 地域設定
				List<String> strList = new ArrayList<String>();
				if (twitterTemplateList.get(0).getRegions() != null && !twitterTemplateList.get(0).getRegions().isEmpty()) {
					strList = Arrays.asList(twitterTemplateList.get(0).getRegions().split(","));
				}
				// websiteTweetListをSettionから取得
				List<TwitterTweet> websiteTweetList = (List<TwitterTweet>) session.getAttribute("websiteTweetList");

				TwitterAdsDto twitterAdsDto = new TwitterAdsDto();
				twitterAdsDto.setTweetIdList(issueDto.getTweetIdList());
				List<TwitterTweet> selectedWebsiteTweetList = new ArrayList<>();

				// 選択したwebsiteTweetList
				for (String obj : twitterAdsDto.getTweetIdList()) {
					for (TwitterTweet twitterTweet : websiteTweetList) {
						if (obj.equals(twitterTweet.getTweetId())) {
							TwitterTweet selectedTwitterTweet = new TwitterTweet();
							selectedTwitterTweet.setTweetTitle(twitterTweet.getTweetTitle());
							selectedTwitterTweet.setTweetBody(twitterTweet.getTweetBody());
							selectedTwitterTweet.setPreviewUrl(twitterTweet.getPreviewUrl());
							selectedWebsiteTweetList.add(selectedTwitterTweet);
						}
					}
				}

				twitterAdsDto.setWebsiteTweetList(selectedWebsiteTweetList);
				twitterAdsDto.setCampaignName(issueDto.getCampaignName());
				twitterAdsDto.setStartTime(issueDto.getStartDate());
				twitterAdsDto.setEndTime(issueDto.getEndDate());
				twitterAdsDto.setDailyBudget(CalculateUtil.calOneDayBudget(budgetAllocationDto.getTwitterBudget(), issueDto.getStartDate(), issueDto.getEndDate()));
				twitterAdsDto.setTotalBudget(budgetAllocationDto.getTwitterBudget());
				// キャンペーン目的がwebsite
				twitterAdsDto.setObjective(0);
				twitterAdsDto.setLocation(Integer.valueOf(twitterTemplateList.get(0).getLocation()));
				twitterAdsDto.setRegions(strList);
				// 広告作成
				twitterApiService.createTwitterCampaign(twitterAdsDto, issueDto);
			} catch (Exception e) {
				e.printStackTrace();
				issueDto.setTwitterErrorCode(e.getMessage());
				BusinessException businessException = (BusinessException) e;
				issueDto.setTwitterParam(businessException.getParams()[0]);
			}
		}
		// 案件表にインサート
		issueInsertService.insertIssue(issueDto);

	}

	private byte[] getByteArrayFromStream(final InputStream inputStream) throws IOException {
		return new ByteSource() {
			@Override
			public InputStream openStream() {
				return inputStream;
			}
		}.read();
	}
}
