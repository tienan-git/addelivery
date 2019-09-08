package jp.acepro.haishinsan.service.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdSet;
import com.facebook.ads.sdk.Campaign;
import com.facebook.ads.sdk.IDName;
import com.facebook.ads.sdk.Targeting;
import com.facebook.ads.sdk.TargetingGeoLocation;
import com.facebook.ads.sdk.TargetingGeoLocationCity;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.dao.GoogleCampaignManageCustomDao;
import jp.acepro.haishinsan.dao.IssueCustomDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.dao.ShopCustomDao;
import jp.acepro.haishinsan.db.entity.GoogleCampaignManage;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.enums.UnitPriceType;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.google.GoogleReportService;
import jp.acepro.haishinsan.service.google.api.GetAdGroups;
import jp.acepro.haishinsan.service.google.api.UpdateAdGroup;
import jp.acepro.haishinsan.service.google.api.UpdateCampaign;
import jp.acepro.haishinsan.service.google.api.UpdateCampaignStatus;
import jp.acepro.haishinsan.service.issue.FacebookReportingService;
import jp.acepro.haishinsan.service.issue.TwitterReportingService;
import jp.acepro.haishinsan.service.youtube.YoutubeReportService;
import jp.acepro.haishinsan.util.CalculateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreativeStatusApiServiceImpl implements CreativeStatusApiService {

	@Autowired
	OperationService operationService;

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	DspApiService dspApiService;

	@Autowired
	GoogleReportService googleReportService;

	@Autowired
	FacebookService facebookService;

	@Autowired
	FacebookReportingService facebookReportingService;

	@Autowired
	TwitterReportingService twitterReportingService;

	@Autowired
	YoutubeReportService youtubeReportService;

	@Autowired
	DspSegmentService dspSegmentService;

	@Autowired
	IssueDao issueDao;

	@Autowired
	IssueCustomDao issueCustomDao;

	@Autowired
	ShopCustomDao shopCustomDao;

	@Autowired
	GoogleCampaignManageCustomDao googleCampaignManageCustomDao;

	@Async
	@Override
	@Transactional
	public void executeAsync() {
		
		// DSP
		
		
		// Facebook


		// Google

		
	}


}
