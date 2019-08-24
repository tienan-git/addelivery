package jp.acepro.haishinsan.service.issue;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.AdsInsights;
import com.facebook.ads.sdk.AdsInsights.EnumBreakdowns;
import com.facebook.ads.sdk.AdsInsights.EnumDatePreset;
import com.google.gson.Gson;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.bean.FacebookDateReportCsvBean;
import jp.acepro.haishinsan.bean.FacebookDeviceReportCsvBean;
import jp.acepro.haishinsan.bean.FacebookRegionReportCsvBean;
import jp.acepro.haishinsan.dao.DspSegmentCustomDao;
import jp.acepro.haishinsan.dao.FacebookCampaignManageCustomDao;
import jp.acepro.haishinsan.dao.FacebookCampaignManageDao;
import jp.acepro.haishinsan.dao.FacebookDeviceReportCustomDao;
import jp.acepro.haishinsan.dao.FacebookDeviceReportDao;
import jp.acepro.haishinsan.dao.FacebookRegionReportCustomDao;
import jp.acepro.haishinsan.dao.FacebookRegionReportDao;
import jp.acepro.haishinsan.dao.FacebookTemplateCustomDao;
import jp.acepro.haishinsan.dao.FacebookTemplateDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.db.entity.FacebookDeviceReport;
import jp.acepro.haishinsan.db.entity.FacebookRegionReport;
import jp.acepro.haishinsan.dto.facebook.FbDeviceReportDto;
import jp.acepro.haishinsan.dto.facebook.FbGraphReportDto;
import jp.acepro.haishinsan.dto.facebook.FbRegionReportDto;
import jp.acepro.haishinsan.dto.facebook.FbReportDisplayDto;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.service.BaseService;
import jp.acepro.haishinsan.service.EmailService;
import jp.acepro.haishinsan.util.DateUtil;
import jp.acepro.haishinsan.util.ReportUtil;

@Service
public class FacebookReportingServiceImpl extends BaseService implements FacebookReportingService {

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	FacebookTemplateCustomDao facebookTemplateCustomDao;

	@Autowired
	FacebookTemplateDao facebookTemplateDao;

	@Autowired
	IssueDao issueDao;

	@Autowired
	DspSegmentCustomDao dspSegmentCustomDao;

	@Autowired
	FacebookCampaignManageDao facebookCampaignManageDao;

	@Autowired
	FacebookCampaignManageCustomDao facebookCampaignManageCustomDao;

	@Autowired
	FacebookRegionReportDao facebookRegionReportDao;

	@Autowired
	FacebookRegionReportCustomDao facebookRegionReportCustomDao;

	@Autowired
	FacebookDeviceReportDao facebookDeviceReportDao;

	@Autowired
	FacebookDeviceReportCustomDao facebookDeviceReportCustomDao;

	@Autowired
	EmailService emailService;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void getReportDetails(EnumDatePreset enumDatePreset) {

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(),
				applicationProperties.getFacebookAppSecret());
		Gson gson = new Gson();
		LocalDate localDate = LocalDate.now();
		;
		if (EnumDatePreset.VALUE_YESTERDAY.equals(enumDatePreset)) {
			localDate = localDate.minusDays(1);
		}
		try {
			// String idList = "[\"23843032433030277\",\"23843046668180277\"]}]";

			APINodeList<AdsInsights> requestGetRegionInsights = new AdAccount(
					applicationProperties.getFacebookAccountId(), context).getInsights()
							.setLevel(AdsInsights.EnumLevel.VALUE_CAMPAIGN)

							// .setFiltering("[]")
							.setTimeIncrement("1").setBreakdowns(Arrays.asList(EnumBreakdowns.VALUE_REGION))
							// .setFiltering("[{\"field\":\"campaign.id\",\"operator\":\"IN\",\"value\":[\"ARCHIVED\",\"DELETED\"]}]")
							// .setFiltering("[{\"field\":\"campaign.id\",\"operator\":\"IN\",\"value\":" +
							// idList)
							.setDatePreset(enumDatePreset)
							// .setActionReportTime(EnumActionReportTime.VALUE_IMPRESSION)
							// .setDatePreset(AdsInsights.EnumDatePreset.VALUE_LIFETIME)
							// .setTimeRange("{\"since\":\"2018-08-08\",\"until\":\"2018-11-07\"}")
							// .requestField("actions:link_click")
							// .requestField("actions:photo_view")
							// .requestField("cost_per_result")
							.requestField("campaign_id").requestField("campaign_name").requestField("impressions")
							.requestField("clicks").requestField("spend")
							// .requestAllFields()
							.execute();
			// List<FacebookRegionReport> facebookRegionReportList = new
			// ArrayList<FacebookRegionReport>();
			for (AdsInsights requestGetRegionInsight : requestGetRegionInsights) {

				FbRegionReportDto fbRegionReportDto = gson.fromJson(requestGetRegionInsight.getRawValue(),
						FbRegionReportDto.class);

				FacebookRegionReport facebookRegionReportOld = facebookRegionReportCustomDao
						.selectByCampaignIdDateRegion(fbRegionReportDto.getCampaign_id(),
								DateUtil.toLocalDate(fbRegionReportDto.getDate_start()), fbRegionReportDto.getRegion());
				if (facebookRegionReportOld != null && facebookRegionReportOld.getCampaignId() != null) {
					facebookRegionReportOld.setImpressions(Long.parseLong(fbRegionReportDto.getImpressions()));
					facebookRegionReportOld.setClicks(Long.parseLong(fbRegionReportDto.getClicks()));
					facebookRegionReportOld.setSpend(Long.parseLong(fbRegionReportDto.getSpend()));
					facebookRegionReportDao.update(facebookRegionReportOld);
				} else {
					FacebookRegionReport facebookRegionReportNew = new FacebookRegionReport();
					facebookRegionReportNew.setCampaignId(fbRegionReportDto.getCampaign_id());
					facebookRegionReportNew.setCampaignName(fbRegionReportDto.getCampaign_name());
					facebookRegionReportNew.setDate(DateUtil.toLocalDate(fbRegionReportDto.getDate_start()));
					facebookRegionReportNew.setRegion(fbRegionReportDto.getRegion());
					facebookRegionReportNew.setImpressions(Long.parseLong(fbRegionReportDto.getImpressions()));
					facebookRegionReportNew.setClicks(Long.parseLong(fbRegionReportDto.getClicks()));
					facebookRegionReportNew.setSpend(Long.parseLong(fbRegionReportDto.getSpend()));
					facebookRegionReportDao.insert(facebookRegionReportNew);
				}

				// facebookRegionReportList.add(facebookRegionReport);
			}

			APINodeList<AdsInsights> requestGetDeviceInsights = new AdAccount(
					applicationProperties.getFacebookAccountId(), context).getInsights()
							.setLevel(AdsInsights.EnumLevel.VALUE_CAMPAIGN).setTimeIncrement("1")
							// .setDatePreset(AdsInsights.EnumDatePreset.VALUE_LIFETIME)
							.setBreakdowns(Arrays.asList(EnumBreakdowns.VALUE_DEVICE_PLATFORM))
							.setDatePreset(enumDatePreset)
							// .setTimeRange("{\"since\":\"2018-08-08\",\"until\":\"2018-11-07\"}")
							// .requestField("actions:link_click")
							// .requestField("actions:photo_view")
							// .requestField("cost_per_result")
							.requestField("campaign_id").requestField("campaign_name").requestField("impressions")
							.requestField("clicks").requestField("spend")
							// .requestAllFields()
							.execute();
			// List<FacebookDeviceReport> facebookDeviceReportList = new
			// ArrayList<FacebookDeviceReport>();
			for (AdsInsights requestGetDeviceInsight : requestGetDeviceInsights) {

				FbDeviceReportDto fbDeviceReportDto = gson.fromJson(requestGetDeviceInsight.getRawValue(),
						FbDeviceReportDto.class);

				FacebookDeviceReport facebookDeviceReportOld = facebookDeviceReportCustomDao
						.selectByCampaignIdDateDevice(fbDeviceReportDto.getCampaign_id(),
								DateUtil.toLocalDate(fbDeviceReportDto.getDate_start()), fbDeviceReportDto.getDevice());
				if (facebookDeviceReportOld != null && facebookDeviceReportOld.getCampaignId() != null) {
					facebookDeviceReportOld.setImpressions(Long.parseLong(fbDeviceReportDto.getImpressions()));
					facebookDeviceReportOld.setClicks(Long.parseLong(fbDeviceReportDto.getClicks()));
					facebookDeviceReportOld.setSpend(Long.parseLong(fbDeviceReportDto.getSpend()));

					facebookDeviceReportDao.update(facebookDeviceReportOld);
				} else {
					FacebookDeviceReport facebookDeviceReportNew = new FacebookDeviceReport();
					facebookDeviceReportNew.setCampaignId(fbDeviceReportDto.getCampaign_id());
					facebookDeviceReportNew.setCampaignName(fbDeviceReportDto.getCampaign_name());
					facebookDeviceReportNew.setDate(DateUtil.toLocalDate(fbDeviceReportDto.getDate_start()));
					facebookDeviceReportNew
							.setDevice(fbDeviceReportDto.getDevice() == null ? "不明" : fbDeviceReportDto.getDevice());
					facebookDeviceReportNew.setImpressions(Long.parseLong(fbDeviceReportDto.getImpressions()));
					facebookDeviceReportNew.setClicks(Long.parseLong(fbDeviceReportDto.getClicks()));
					facebookDeviceReportNew.setSpend(Long.parseLong(fbDeviceReportDto.getSpend()));
					facebookDeviceReportDao.insert(facebookDeviceReportNew);
				}
				// facebookDeviceReportList.add(facebookDeviceReport);
			}

		} catch (APIException e) {
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
	}

	@Override
	@Transactional
	public List<FbReportDisplayDto> getDeviceReport(List<String> campaignIdList, String startDate, String endDate) {

		List<FbReportDisplayDto> fbReportDisplayDtoList = new ArrayList<FbReportDisplayDto>();

		List<FacebookDeviceReport> facebookDeviceReportList = facebookDeviceReportCustomDao
				.selectDeviceReport(campaignIdList, DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));
		for (FacebookDeviceReport facebookDeviceReport : facebookDeviceReportList) {
			FbReportDisplayDto fbReportDisplayDto = new FbReportDisplayDto();
			fbReportDisplayDto.setCampaignId(facebookDeviceReport.getCampaignId());
			fbReportDisplayDto.setCampaignName(facebookDeviceReport.getCampaignName());
			fbReportDisplayDto.setDevice(facebookDeviceReport.getDevice());
			fbReportDisplayDto.setImpressions(facebookDeviceReport.getImpressions());
			fbReportDisplayDto.setClicks(facebookDeviceReport.getClicks());
			Long displayCosts = ReportUtil.calDisplaySpend((double) facebookDeviceReport.getSpend());
			fbReportDisplayDto.setSpend(displayCosts.longValue());
			fbReportDisplayDto
					.setCtr(ReportUtil.calCtr(facebookDeviceReport.getClicks(), facebookDeviceReport.getImpressions()));
			fbReportDisplayDto.setCpc(ReportUtil.calCpc(facebookDeviceReport.getClicks(), displayCosts));
			fbReportDisplayDto.setCpm(ReportUtil.calCpm(facebookDeviceReport.getImpressions(), displayCosts));

			fbReportDisplayDtoList.add(fbReportDisplayDto);
		}

		return getSummary(fbReportDisplayDtoList);

	}

	@Override
	@Transactional
	public List<FbReportDisplayDto> getRegionReport(List<String> campaignIdList, String startDate, String endDate) {

		List<FbReportDisplayDto> fbReportDisplayDtoList = new ArrayList<FbReportDisplayDto>();

		List<FacebookRegionReport> facebookRegionReportList = facebookRegionReportCustomDao
				.selectRegionReport(campaignIdList, DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));
		for (FacebookRegionReport facebookRegionReport : facebookRegionReportList) {
			FbReportDisplayDto fbReportDisplayDto = new FbReportDisplayDto();
			fbReportDisplayDto.setCampaignId(facebookRegionReport.getCampaignId());
			fbReportDisplayDto.setCampaignName(facebookRegionReport.getCampaignName());
			fbReportDisplayDto.setRegion(facebookRegionReport.getRegion());
			fbReportDisplayDto.setImpressions(facebookRegionReport.getImpressions());
			fbReportDisplayDto.setClicks(facebookRegionReport.getClicks());
			Long displayCosts = ReportUtil.calDisplaySpend((double) facebookRegionReport.getSpend());
			fbReportDisplayDto.setSpend(displayCosts.longValue());
			fbReportDisplayDto
					.setCtr(ReportUtil.calCtr(facebookRegionReport.getClicks(), facebookRegionReport.getImpressions()));
			fbReportDisplayDto.setCpc(ReportUtil.calCpc(facebookRegionReport.getClicks(), displayCosts));
			fbReportDisplayDto.setCpm(ReportUtil.calCpm(facebookRegionReport.getImpressions(), displayCosts));

			fbReportDisplayDtoList.add(fbReportDisplayDto);
		}

		return getSummary(fbReportDisplayDtoList);

	}

	@Override
	@Transactional
	public List<FbReportDisplayDto> getDateReport(List<String> campaignIdList, String startDate, String endDate) {

		List<FbReportDisplayDto> fbReportDisplayDtoList = new ArrayList<FbReportDisplayDto>();

		List<FacebookDeviceReport> facebookDeviceReportList = facebookDeviceReportCustomDao
				.selectDateReport(campaignIdList, DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));
		for (FacebookDeviceReport facebookDeviceReport : facebookDeviceReportList) {
			FbReportDisplayDto fbReportDisplayDto = new FbReportDisplayDto();
			fbReportDisplayDto.setCampaignId(facebookDeviceReport.getCampaignId());
			fbReportDisplayDto.setCampaignName(facebookDeviceReport.getCampaignName());
			fbReportDisplayDto.setDate(DateUtil.fromLocalDate(facebookDeviceReport.getDate()));
			fbReportDisplayDto.setImpressions(facebookDeviceReport.getImpressions());
			fbReportDisplayDto.setClicks(facebookDeviceReport.getClicks());
			Long displayCosts = ReportUtil.calDisplaySpend((double) facebookDeviceReport.getSpend());
			fbReportDisplayDto.setSpend(displayCosts.longValue());
			fbReportDisplayDto
					.setCtr(ReportUtil.calCtr(facebookDeviceReport.getClicks(), facebookDeviceReport.getImpressions()));
			fbReportDisplayDto.setCpc(ReportUtil.calCpc(facebookDeviceReport.getClicks(), displayCosts));
			fbReportDisplayDto.setCpm(ReportUtil.calCpm(facebookDeviceReport.getImpressions(), displayCosts));

			fbReportDisplayDtoList.add(fbReportDisplayDto);
		}

		return getSummary(fbReportDisplayDtoList);

	}

	// DB : デバイス別レポート（グラフ用）
	@Override
	@Transactional
	public FbGraphReportDto getFacebookDeviceReportingGraph(List<String> campaignIdList, String startDate,
			String endDate) {

		List<String> reportTypeList = new ArrayList<>(Arrays.asList("x"));
		List<String> impressionList = new ArrayList<>(Arrays.asList("impressions"));
		List<String> clicksList = new ArrayList<>(Arrays.asList("clicks"));
		List<String> spendList = new ArrayList<>(Arrays.asList("spend"));
		List<String> CTRList = new ArrayList<>(Arrays.asList("ctr"));
		List<String> CPCList = new ArrayList<>(Arrays.asList("cpc"));
		List<String> CPMList = new ArrayList<>(Arrays.asList("cpm"));

		List<FacebookDeviceReport> facebookDeviceReportList = facebookDeviceReportCustomDao.selectDeviceReportGraph(
				campaignIdList, DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));

		for (FacebookDeviceReport facebookDeviceReport : facebookDeviceReportList) {
			reportTypeList.add(facebookDeviceReport.getDevice());
			impressionList.add(facebookDeviceReport.getImpressions().toString());
			clicksList.add(facebookDeviceReport.getClicks().toString());
			Long displayCosts = ReportUtil.calDisplaySpend((double) facebookDeviceReport.getSpend()).longValue();
			spendList.add(displayCosts.toString());
			CTRList.add(ReportUtil.calCtr(facebookDeviceReport.getClicks(), facebookDeviceReport.getImpressions())
					.toString());
			CPCList.add(ReportUtil.calCpc(facebookDeviceReport.getClicks(), displayCosts).toString());
			CPMList.add(ReportUtil.calCpm(facebookDeviceReport.getImpressions(), displayCosts).toString());
		}

		FbGraphReportDto facebookGraphReportDto = new FbGraphReportDto();
		facebookGraphReportDto.setReportTypeList(reportTypeList);
		facebookGraphReportDto.setImpressionList(impressionList);
		facebookGraphReportDto.setClicksList(clicksList);
		facebookGraphReportDto.setSpendList(spendList);
		facebookGraphReportDto.setCTRList(CTRList);
		facebookGraphReportDto.setCPCList(CPCList);
		facebookGraphReportDto.setCPMList(CPMList);

		return facebookGraphReportDto;
	}

	// DB : 日別レポート（グラフ用）
	@Override
	@Transactional
	public FbGraphReportDto getFacebookDateReportingGraph(List<String> campaignIdList, String startDate,
			String endDate) {

		List<String> reportTypeList = new ArrayList<>(Arrays.asList("x"));
		List<String> impressionList = new ArrayList<>(Arrays.asList("impressions"));
		List<String> clicksList = new ArrayList<>(Arrays.asList("clicks"));
		List<String> spendList = new ArrayList<>(Arrays.asList("spend"));
		List<String> CTRList = new ArrayList<>(Arrays.asList("ctr"));
		List<String> CPCList = new ArrayList<>(Arrays.asList("cpc"));
		List<String> CPMList = new ArrayList<>(Arrays.asList("cpm"));

		List<FacebookDeviceReport> facebookDateReportList = facebookDeviceReportCustomDao
				.selectDateReportGraph(campaignIdList, DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));

		for (FacebookDeviceReport facebookDateReport : facebookDateReportList) {
			reportTypeList.add(facebookDateReport.getDate().toString());
			impressionList.add(facebookDateReport.getImpressions().toString());
			clicksList.add(facebookDateReport.getClicks().toString());
			Long displayCosts = ReportUtil.calDisplaySpend((double) facebookDateReport.getSpend()).longValue();
			spendList.add(displayCosts.toString());
			CTRList.add(
					ReportUtil.calCtr(facebookDateReport.getClicks(), facebookDateReport.getImpressions()).toString());
			CPCList.add(ReportUtil.calCpc(facebookDateReport.getClicks(), displayCosts).toString());
			CPMList.add(ReportUtil.calCpm(facebookDateReport.getImpressions(), displayCosts).toString());
		}

		FbGraphReportDto facebookGraphReportDto = new FbGraphReportDto();
		facebookGraphReportDto.setReportTypeList(reportTypeList);
		facebookGraphReportDto.setImpressionList(impressionList);
		facebookGraphReportDto.setClicksList(clicksList);
		facebookGraphReportDto.setSpendList(spendList);
		facebookGraphReportDto.setCTRList(CTRList);
		facebookGraphReportDto.setCPCList(CPCList);
		facebookGraphReportDto.setCPMList(CPMList);

		return facebookGraphReportDto;
	}

	// DB : 地域別レポート（グラフ用）
	@Override
	@Transactional
	public FbGraphReportDto getFacebookRegionReportingGraph(List<String> campaignIdList, String startDate,
			String endDate) {

		List<String> reportTypeList = new ArrayList<>(Arrays.asList("x"));
		List<String> impressionList = new ArrayList<>(Arrays.asList("impressions"));
		List<String> clicksList = new ArrayList<>(Arrays.asList("clicks"));
		List<String> spendList = new ArrayList<>(Arrays.asList("spend"));
		List<String> CTRList = new ArrayList<>(Arrays.asList("ctr"));
		List<String> CPCList = new ArrayList<>(Arrays.asList("cpc"));
		List<String> CPMList = new ArrayList<>(Arrays.asList("cpm"));

		List<FacebookRegionReport> facebookRegionReportList = facebookRegionReportCustomDao.selectRegionReportGraph(
				campaignIdList, DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));

		for (FacebookRegionReport facebookRegionReport : facebookRegionReportList) {
			reportTypeList.add(facebookRegionReport.getRegion());
			impressionList.add(facebookRegionReport.getImpressions().toString());
			clicksList.add(facebookRegionReport.getClicks().toString());
			Long displayCosts = ReportUtil.calDisplaySpend((double) facebookRegionReport.getSpend()).longValue();
			spendList.add(displayCosts.toString());
			CTRList.add(ReportUtil.calCtr(facebookRegionReport.getClicks(), facebookRegionReport.getImpressions())
					.toString());
			CPCList.add(ReportUtil.calCpc(facebookRegionReport.getClicks(), displayCosts).toString());
			CPMList.add(ReportUtil.calCpm(facebookRegionReport.getImpressions(), displayCosts).toString());
		}

		FbGraphReportDto facebookGraphReportDto = new FbGraphReportDto();
		facebookGraphReportDto.setReportTypeList(reportTypeList);
		facebookGraphReportDto.setImpressionList(impressionList);
		facebookGraphReportDto.setClicksList(clicksList);
		facebookGraphReportDto.setSpendList(spendList);
		facebookGraphReportDto.setCTRList(CTRList);
		facebookGraphReportDto.setCPCList(CPCList);
		facebookGraphReportDto.setCPMList(CPMList);

		return facebookGraphReportDto;
	}

	@Override
	@Transactional
	public String download(List<String> campaignIdList, String startDate, String endDate, Integer reportType) {

		List<FbReportDisplayDto> fbReportDisplayDtoList = null;

		List<FacebookDeviceReportCsvBean> facebookDeviceReportCsvBeanList = new ArrayList<FacebookDeviceReportCsvBean>();
		List<FacebookDateReportCsvBean> facebookDateReportCsvBeanList = new ArrayList<FacebookDateReportCsvBean>();
		List<FacebookRegionReportCsvBean> facebookRegionReportCsvBeanList = new ArrayList<FacebookRegionReportCsvBean>();
		switch (ReportType.of(reportType)) {
		case DEVICE:// デバイス別の場合
			fbReportDisplayDtoList = getDeviceReport(campaignIdList, startDate, endDate);
			for (FbReportDisplayDto fbReportDisplayDto : fbReportDisplayDtoList) {
				FacebookDeviceReportCsvBean facebookDeviceReportCsvBean = new FacebookDeviceReportCsvBean();
				facebookDeviceReportCsvBean.setCampaignName(fbReportDisplayDto.getCampaignName());
				facebookDeviceReportCsvBean.setCampaignId(fbReportDisplayDto.getCampaignId());
				facebookDeviceReportCsvBean.setImpressions(fbReportDisplayDto.getImpressions().toString());
				facebookDeviceReportCsvBean.setClicks(fbReportDisplayDto.getClicks().toString());
				facebookDeviceReportCsvBean.setCosts(fbReportDisplayDto.getSpend().toString());
				facebookDeviceReportCsvBean.setCtr(fbReportDisplayDto.getCtr().toString());
				facebookDeviceReportCsvBean.setCpc(fbReportDisplayDto.getCpc().toString());
				facebookDeviceReportCsvBean.setCpm(fbReportDisplayDto.getCpm().toString());
				facebookDeviceReportCsvBeanList.add(facebookDeviceReportCsvBean);
			}
		case REGIONS:// 地域別の場合
			fbReportDisplayDtoList = getRegionReport(campaignIdList, startDate, endDate);
			for (FbReportDisplayDto fbReportDisplayDto : fbReportDisplayDtoList) {
				FacebookRegionReportCsvBean facebookRegionReportCsvBean = new FacebookRegionReportCsvBean();
				facebookRegionReportCsvBean.setCampaignName(fbReportDisplayDto.getCampaignName());
				facebookRegionReportCsvBean.setCampaignId(fbReportDisplayDto.getCampaignId());
				facebookRegionReportCsvBean.setImpressions(fbReportDisplayDto.getImpressions().toString());
				facebookRegionReportCsvBean.setClicks(fbReportDisplayDto.getClicks().toString());
				facebookRegionReportCsvBean.setCosts(fbReportDisplayDto.getSpend().toString());
				facebookRegionReportCsvBean.setCtr(fbReportDisplayDto.getCtr().toString());
				facebookRegionReportCsvBean.setCpc(fbReportDisplayDto.getCpc().toString());
				facebookRegionReportCsvBean.setCpm(fbReportDisplayDto.getCpm().toString());
				facebookRegionReportCsvBeanList.add(facebookRegionReportCsvBean);
			}
		case DATE:// 日別の場合
			fbReportDisplayDtoList = getDateReport(campaignIdList, startDate, endDate);
			for (FbReportDisplayDto fbReportDisplayDto : fbReportDisplayDtoList) {
				FacebookDateReportCsvBean facebookDateReportCsvBean = new FacebookDateReportCsvBean();
				facebookDateReportCsvBean.setCampaignName(fbReportDisplayDto.getCampaignName());
				facebookDateReportCsvBean.setCampaignId(fbReportDisplayDto.getCampaignId());
				facebookDateReportCsvBean.setImpressions(fbReportDisplayDto.getImpressions().toString());
				facebookDateReportCsvBean.setClicks(fbReportDisplayDto.getClicks().toString());
				facebookDateReportCsvBean.setCosts(fbReportDisplayDto.getSpend().toString());
				facebookDateReportCsvBean.setCtr(fbReportDisplayDto.getCtr().toString());
				facebookDateReportCsvBean.setCpc(fbReportDisplayDto.getCpc().toString());
				facebookDateReportCsvBean.setCpm(fbReportDisplayDto.getCpm().toString());
				facebookDateReportCsvBeanList.add(facebookDateReportCsvBean);
			}
		}

		StringWriter out = new StringWriter();
		CsvWriterSettings settings = new CsvWriterSettings();
		CsvWriter writer = null;
		BeanWriterProcessor<FacebookDeviceReportCsvBean> deviceWriterProcessor = null;
		BeanWriterProcessor<FacebookDateReportCsvBean> dateWriterProcessor = null;
		BeanWriterProcessor<FacebookRegionReportCsvBean> regionWriterProcessor = null;

		switch (ReportType.of(reportType)) {
		case DEVICE:// デバイス別の場合
			settings.setHeaders(FacebookDeviceReportCsvBean.columnName);
			deviceWriterProcessor = new BeanWriterProcessor<>(FacebookDeviceReportCsvBean.class);
			settings.setRowWriterProcessor(deviceWriterProcessor);

			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(facebookDeviceReportCsvBeanList);
			break;
		case REGIONS:// 地域別の場合
			settings.setHeaders(FacebookRegionReportCsvBean.columnName);
			regionWriterProcessor = new BeanWriterProcessor<>(FacebookRegionReportCsvBean.class);
			settings.setRowWriterProcessor(regionWriterProcessor);

			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(facebookRegionReportCsvBeanList);
			break;
		case DATE:// 日別の場合
			settings.setHeaders(FacebookDateReportCsvBean.columnName);
			dateWriterProcessor = new BeanWriterProcessor<>(FacebookDateReportCsvBean.class);
			settings.setRowWriterProcessor(dateWriterProcessor);

			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(facebookDateReportCsvBeanList);
			break;
		}

		// close
		writer.close();

		return out.toString();
	}

	// 合計データ
	private List<FbReportDisplayDto> getSummary(List<FbReportDisplayDto> fbReportDisplayDtoList) {

		FbReportDisplayDto sumReportDto = new FbReportDisplayDto();
		sumReportDto.setCampaignId("合計");
		sumReportDto.setCampaignName("-");
		sumReportDto.setDevice("-");
		sumReportDto.setRegion("-");
		sumReportDto.setDate("-");
		sumReportDto
				.setImpressions(fbReportDisplayDtoList.stream().mapToLong(FbReportDisplayDto::getImpressions).sum());
		sumReportDto.setClicks(fbReportDisplayDtoList.stream().mapToLong(FbReportDisplayDto::getClicks).sum());
		sumReportDto.setSpend(fbReportDisplayDtoList.stream().mapToLong(FbReportDisplayDto::getSpend).sum());
		sumReportDto.setCtr(ReportUtil.calCtr(sumReportDto.getClicks(), sumReportDto.getImpressions()));
		sumReportDto.setCpc(ReportUtil.calCpc(sumReportDto.getClicks(), sumReportDto.getSpend()));
		sumReportDto.setCpm(ReportUtil.calCpm(sumReportDto.getImpressions(), sumReportDto.getSpend()));

		fbReportDisplayDtoList.add(sumReportDto);

		return fbReportDisplayDtoList;
	}

}
