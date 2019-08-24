package jp.acepro.haishinsan.service.google;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.bean.GoogleDateReportCsvBean;
import jp.acepro.haishinsan.bean.GoogleDeviceReportCsvBean;
import jp.acepro.haishinsan.bean.GoogleLocationReportCsvBean;
import jp.acepro.haishinsan.dao.GoogleCampaignManageCustomDao;
import jp.acepro.haishinsan.dao.GoogleDeviceReportCustomDao;
import jp.acepro.haishinsan.dao.GoogleDeviceReportDao;
import jp.acepro.haishinsan.dao.GoogleLocationReportCustomDao;
import jp.acepro.haishinsan.dao.GoogleLocationReportDao;
import jp.acepro.haishinsan.dao.IssueCustomDao;
import jp.acepro.haishinsan.dao.ShopCustomDao;
import jp.acepro.haishinsan.db.entity.GoogleCampaignManage;
import jp.acepro.haishinsan.db.entity.GoogleDeviceReport;
import jp.acepro.haishinsan.db.entity.GoogleLocationReport;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.dto.google.GoogleDeviceReportDto;
import jp.acepro.haishinsan.dto.google.GoogleLocationReportDto;
import jp.acepro.haishinsan.dto.google.GoogleReportDisplayDto;
import jp.acepro.haishinsan.dto.google.GoogleReportDto;
import jp.acepro.haishinsan.dto.google.GoogleReportSearchDto;
import jp.acepro.haishinsan.enums.GoogleDeviceType;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.google.api.GetDeviceReport;
import jp.acepro.haishinsan.service.google.api.GetLocationReport;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.ReportUtil;
import jp.acepro.haishinsan.util.StringFormatter;

@Service
public class GoogleReportServiceImpl implements GoogleReportService {

	@Autowired
	IssueCustomDao issueCustomDao;

	@Autowired
	GoogleCampaignManageCustomDao googleCampaignManageCustomDao;

	@Autowired
	GoogleDeviceReportDao googleDeviceReportDao;

	@Autowired
	GoogleDeviceReportCustomDao googleDeviceReportCustomDao;

	@Autowired
	GoogleLocationReportDao googleLocationReportDao;

	@Autowired
	GoogleLocationReportCustomDao googleLocationReportCustomDao;

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	ShopCustomDao shopCustomDao;

	@Autowired
	ApplicationProperties applicationProperties;

	// レポート取得（API経由）
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void getReport() {

		// 全体店舗を取得
		List<Shop> shopList = shopCustomDao.selectAllShop();
		if (shopList.size() > 0) {
			// 店舗存在の場合
			for (Shop shop : shopList) {
				if (!Strings.isEmpty(shop.getGoogleAccountId())) {
					// Googleアカウント登録有りの場合、店舗情報を取得
					getShopReport(shop.getShopId(), shop.getGoogleAccountId());
				}
			}
		}
	}

	// 店舗レポート取得（API経由）
	@Transactional
	private void getShopReport(Long shopId, String googleAccountId) {

		// キャンペーン情報取得（DBから）
		List<Long> campaignIdList = getShopCampaignIdList(shopId);
		if (campaignIdList.size() == 0) {
			// キャンペーン無しの場合、該当店舗をスキップ
			return;
		}

		// キャンペーンレポート情報取得（API経由）
		// デバイス別取得
		GetDeviceReport getDeviceReport = new GetDeviceReport();
		getDeviceReport.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
		getDeviceReport.googleAccountId = googleAccountId;
		getDeviceReport.campaignIdList = campaignIdList;
		getDeviceReport.run();
		List<GoogleDeviceReportDto> googleDeviceReportDtoList = new ArrayList<GoogleDeviceReportDto>();
		googleDeviceReportDtoList = getDeviceReport.googleDeviceReportDtoList;
		// 地域別取得
		GetLocationReport getLocationReport = new GetLocationReport();
		getLocationReport.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
		getLocationReport.googleAccountId = googleAccountId;
		getLocationReport.campaignIdList = campaignIdList;
		getLocationReport.run();
		List<GoogleLocationReportDto> googleLocationReportDtoList = new ArrayList<GoogleLocationReportDto>();
		googleLocationReportDtoList = getLocationReport.googleLocationReportDtoList;

		// キャンペーンレポート情報更新（DBへ）
		// デバイス別更新
		if (googleDeviceReportDtoList.size() > 0) {
			for (GoogleDeviceReportDto googleDeviceReportDto : googleDeviceReportDtoList) {
				GoogleDeviceReport googleDeviceReportOld = new GoogleDeviceReport();
				googleDeviceReportOld = googleDeviceReportCustomDao.selectForUpdate(
						googleDeviceReportDto.getCampaignId(), googleDeviceReportDto.getDate(),
						googleDeviceReportDto.getDeviceType());
				if (googleDeviceReportOld != null) {
					// 既存更新
					googleDeviceReportOld.setCampaignName(googleDeviceReportDto.getCampaignName());
					googleDeviceReportOld.setImpressions(googleDeviceReportDto.getImpressions());
					googleDeviceReportOld.setClicks(googleDeviceReportDto.getClicks());
					googleDeviceReportOld.setCosts(googleDeviceReportDto.getCosts());
					googleDeviceReportDao.update(googleDeviceReportOld);
				} else {
					// 新規追加
					GoogleDeviceReport googleDeviceReportNew = new GoogleDeviceReport();
					googleDeviceReportNew.setCampaignId(googleDeviceReportDto.getCampaignId());
					googleDeviceReportNew.setCampaignName(googleDeviceReportDto.getCampaignName());
					googleDeviceReportNew.setDate(googleDeviceReportDto.getDate());
					googleDeviceReportNew.setDeviceType(googleDeviceReportDto.getDeviceType());
					googleDeviceReportNew.setImpressions(googleDeviceReportDto.getImpressions());
					googleDeviceReportNew.setClicks(googleDeviceReportDto.getClicks());
					googleDeviceReportNew.setCosts(googleDeviceReportDto.getCosts());
					googleDeviceReportDao.insert(googleDeviceReportNew);
				}
			}
		}
		// 地域別更新
		if (googleLocationReportDtoList.size() > 0) {
			for (GoogleLocationReportDto googleLocationReportDto : googleLocationReportDtoList) {
				GoogleLocationReport googleLocationReportOld = new GoogleLocationReport();
				googleLocationReportOld = googleLocationReportCustomDao.selectForUpdate(
						googleLocationReportDto.getCampaignId(), googleLocationReportDto.getDate(),
						googleLocationReportDto.getLocationId());
				if (googleLocationReportOld != null) {
					// 既存更新
					googleLocationReportOld.setCampaignName(googleLocationReportDto.getCampaignName());
					googleLocationReportOld.setImpressions(googleLocationReportDto.getImpressions());
					googleLocationReportOld.setClicks(googleLocationReportDto.getClicks());
					googleLocationReportOld.setCosts(googleLocationReportDto.getCosts());
					googleLocationReportDao.update(googleLocationReportOld);
				} else {
					// 新規追加
					GoogleLocationReport googleLocationReportNew = new GoogleLocationReport();
					googleLocationReportNew.setCampaignId(googleLocationReportDto.getCampaignId());
					googleLocationReportNew.setCampaignName(googleLocationReportDto.getCampaignName());
					googleLocationReportNew.setDate(googleLocationReportDto.getDate());
					googleLocationReportNew.setLocationId(googleLocationReportDto.getLocationId());
					googleLocationReportNew.setImpressions(googleLocationReportDto.getImpressions());
					googleLocationReportNew.setClicks(googleLocationReportDto.getClicks());
					googleLocationReportNew.setCosts(googleLocationReportDto.getCosts());
					googleLocationReportDao.insert(googleLocationReportNew);
				}
			}
		}
	}

	// レポート表示（デバイス別）
	@Override
	@Transactional
	public GoogleReportDto showDeviceReport(GoogleReportSearchDto googleReportSearchDto) {

		GoogleReportDto googleReportDto = new GoogleReportDto();

		// 表示対象キャンペーンを判定
		List<Long> campaignIdList = new ArrayList<Long>();
		if (googleReportSearchDto.getCampaignIdList().size() > 0) {
			campaignIdList = googleReportSearchDto.getCampaignIdList();
		} else {
			campaignIdList = getShopCampaignIdList(ContextUtil.getCurrentShopId());
			googleReportSearchDto.setCampaignIdList(campaignIdList);
		}

		// 表示対象期間を判定
		// 検索条件がないので、一旦こちらをコメントアウトする
//		if (googleReportSearchDto.getPeriod().equals(PeriodSet.WHOLE.getValue())) {
//			googleReportSearchDto.setStartDate(null);
//			googleReportSearchDto.setEndDate(null);
//		}

		// 表示対象キャンペーンのレポート情報を取得
		List<GoogleDeviceReport> googleDeviceReportList = googleDeviceReportCustomDao.selectDeviceReport(campaignIdList,
				googleReportSearchDto.getStartDate(), googleReportSearchDto.getEndDate());

		// 表示対象キャンペーンのレポート情報を作成
		List<GoogleReportDisplayDto> googleReportDisplayDtoList = new ArrayList<GoogleReportDisplayDto>();
		if (googleDeviceReportList.size() > 0) {
			for (GoogleDeviceReport googleDeviceReport : googleDeviceReportList) {
				GoogleReportDisplayDto googleReportDisplayDto = new GoogleReportDisplayDto();
				googleReportDisplayDto.setCampaignId(String.valueOf(googleDeviceReport.getCampaignId()));
				googleReportDisplayDto.setCampaignName(googleDeviceReport.getCampaignName());
				googleReportDisplayDto.setDeviceType(googleDeviceReport.getDeviceType());
				googleReportDisplayDto
						.setDeviceName(GoogleDeviceType.of(googleDeviceReport.getDeviceType()).getLabel());
				googleReportDisplayDto.setImpressions(googleDeviceReport.getImpressions());
				googleReportDisplayDto.setClicks(googleDeviceReport.getClicks());
				Long displayCosts = ReportUtil.calDisplaySpend(googleDeviceReport.getCosts());
				googleReportDisplayDto.setCosts(displayCosts);
				googleReportDisplayDto
						.setCtr(ReportUtil.calCtr(googleDeviceReport.getClicks(), googleDeviceReport.getImpressions()));
				googleReportDisplayDto.setCpc(ReportUtil.calCpc(googleDeviceReport.getClicks(), displayCosts));
				googleReportDisplayDto.setCpm(ReportUtil.calCpm(googleDeviceReport.getImpressions(), displayCosts));
				googleReportDisplayDtoList.add(googleReportDisplayDto);
			}
		}

		googleReportDto.setGoogleReportDisplayDtoList(addTotal(googleReportDisplayDtoList));

		// 表示対象キャンペーンのグラフ情報を取得
		List<GoogleDeviceReport> googleGraphList = googleDeviceReportCustomDao.selectDeviceGraph(campaignIdList,
				googleReportSearchDto.getStartDate(), googleReportSearchDto.getEndDate());

		// 表示対象キャンペーンのグラフ情報を作成
		List<GoogleReportDisplayDto> googleReportGraphDtoList = new ArrayList<GoogleReportDisplayDto>();
		if (googleGraphList.size() > 0) {
			for (GoogleDeviceReport googleDeviceReport : googleGraphList) {
				GoogleReportDisplayDto googleReportDisplayDto = new GoogleReportDisplayDto();
				googleReportDisplayDto.setDeviceType(googleDeviceReport.getDeviceType());
				googleReportDisplayDto
						.setDeviceName(GoogleDeviceType.of(googleDeviceReport.getDeviceType()).getLabel());
				googleReportDisplayDto.setImpressions(googleDeviceReport.getImpressions());
				googleReportDisplayDto.setClicks(googleDeviceReport.getClicks());
				Long displayCosts = ReportUtil.calDisplaySpend(googleDeviceReport.getCosts());
				googleReportDisplayDto.setCosts(displayCosts);
				googleReportDisplayDto
						.setCtr(ReportUtil.calCtr(googleDeviceReport.getClicks(), googleDeviceReport.getImpressions()));
				googleReportDisplayDto.setCpc(ReportUtil.calCpc(googleDeviceReport.getClicks(), displayCosts));
				googleReportDisplayDto.setCpm(ReportUtil.calCpm(googleDeviceReport.getImpressions(), displayCosts));
				googleReportGraphDtoList.add(googleReportDisplayDto);
			}
		}

		googleReportDto.setGoogleReportGraphDtoList(googleReportGraphDtoList);

		return googleReportDto;
	}

	// レポート表示（地域別）
	@Override
	@Transactional
	public GoogleReportDto showLocationReport(GoogleReportSearchDto googleReportSearchDto) {

		GoogleReportDto googleReportDto = new GoogleReportDto();

		// 表示対象キャンペーンを判定
		List<Long> campaignIdList = new ArrayList<Long>();
		if (googleReportSearchDto.getCampaignIdList().size() > 0) {
			campaignIdList = googleReportSearchDto.getCampaignIdList();
		} else {
			campaignIdList = getShopCampaignIdList(ContextUtil.getCurrentShopId());
			googleReportSearchDto.setCampaignIdList(campaignIdList);
		}

		// 選択した地域IDを取得(campaignId-locationId)
		List<GoogleCampaignManage> googleCampaignManageList = googleCampaignManageCustomDao
				.selectByCampaignList(campaignIdList);
		List<String> locationIds = new ArrayList<String>();
		for (GoogleCampaignManage googleCampaignManage : googleCampaignManageList) {
			StringTokenizer st = new StringTokenizer(googleCampaignManage.getRegions(), ", ");
			while (st.hasMoreTokens()) {
				locationIds.add(googleCampaignManage.getCampaignId().toString() + "-" + String.valueOf(st.nextToken()));
			}
		}

		// 表示対象期間を判定
		// 検索条件がないので、一旦こちらをコメントアウトする
//		if (googleReportSearchDto.getPeriod().equals(PeriodSet.WHOLE.getValue())) {
//			googleReportSearchDto.setStartDate(null);
//			googleReportSearchDto.setEndDate(null);
//		}

		// 表示対象キャンペーンのレポート情報を取得
		List<GoogleLocationReport> googleLocationReportList = googleLocationReportCustomDao.selectLocationReport(
				campaignIdList, googleReportSearchDto.getStartDate(), googleReportSearchDto.getEndDate());
		List<GoogleReportDisplayDto> googleReportDisplayDtoList = new ArrayList<GoogleReportDisplayDto>();
		GoogleReportDisplayDto otherDto = null;
		if (googleLocationReportList.size() > 0) {
			for (GoogleLocationReport googleLocationReport : googleLocationReportList) {
				// 地域名を取得
				Long locationId = googleLocationReport.getLocationId();
				if (CodeMasterServiceImpl.googleAreaNameList == null) {
					codeMasterService.getGoogleAreaList();
				}
				Optional<Pair<Long, String>> locationPair = CodeMasterServiceImpl.googleAreaNameList.stream()
						.filter(obj -> obj.getFirst().equals(locationId)).findFirst();
				String locationName = null;
				if (locationPair.isPresent() && locationIds
						.contains(googleLocationReport.getCampaignId() + "-" + googleLocationReport.getLocationId())) {
					locationName = locationPair.get().getSecond();
					Long displayCosts = ReportUtil.calDisplaySpend(googleLocationReport.getCosts());
					GoogleReportDisplayDto googleReportDisplayDto = new GoogleReportDisplayDto();
					googleReportDisplayDto.setCampaignId(String.valueOf(googleLocationReport.getCampaignId()));
					googleReportDisplayDto.setCampaignName(googleLocationReport.getCampaignName());
					googleReportDisplayDto.setLocationId(googleLocationReport.getLocationId());
					googleReportDisplayDto.setLocationName(locationName);
					googleReportDisplayDto.setImpressions(googleLocationReport.getImpressions());
					googleReportDisplayDto.setClicks(googleLocationReport.getClicks());
					googleReportDisplayDto.setCosts(displayCosts);
					googleReportDisplayDto.setCtr(ReportUtil.calCtr(googleReportDisplayDto.getClicks(),
							googleReportDisplayDto.getImpressions()));
					googleReportDisplayDto.setCpc(
							ReportUtil.calCpc(googleReportDisplayDto.getClicks(), googleReportDisplayDto.getCosts()));
					googleReportDisplayDto.setCpm(ReportUtil.calCpm(googleReportDisplayDto.getImpressions(),
							googleReportDisplayDto.getCosts()));
					googleReportDisplayDtoList.add(googleReportDisplayDto);
				} else {
					if (otherDto == null
							|| !otherDto.getCampaignId().equals(googleLocationReport.getCampaignId().toString())) {
						otherDto = new GoogleReportDisplayDto();
						otherDto.setCampaignId(String.valueOf(googleLocationReport.getCampaignId()));
						otherDto.setCampaignName(googleLocationReport.getCampaignName());
						otherDto.setLocationId(googleLocationReport.getLocationId());
						otherDto.setLocationName("その他");
						otherDto.setImpressions(googleLocationReport.getImpressions());
						otherDto.setClicks(googleLocationReport.getClicks());
						otherDto.setCosts(ReportUtil.calDisplaySpend(googleLocationReport.getCosts()));
						otherDto.setCtr(ReportUtil.calCtr(otherDto.getClicks(), otherDto.getImpressions()));
						otherDto.setCpc(ReportUtil.calCpc(otherDto.getClicks(), otherDto.getCosts()));
						otherDto.setCpm(ReportUtil.calCpm(otherDto.getImpressions(), otherDto.getCosts()));
						googleReportDisplayDtoList.add(otherDto);
					} else {
						otherDto.setImpressions(otherDto.getImpressions() + googleLocationReport.getImpressions());
						otherDto.setClicks(otherDto.getClicks() + googleLocationReport.getClicks());
						otherDto.setCosts(
								otherDto.getCosts() + ReportUtil.calDisplaySpend(googleLocationReport.getCosts()));
						otherDto.setCtr(ReportUtil.calCtr(otherDto.getClicks(), otherDto.getImpressions()));
						otherDto.setCpc(ReportUtil.calCpc(otherDto.getClicks(), otherDto.getCosts()));
						otherDto.setCpm(ReportUtil.calCpm(otherDto.getImpressions(), otherDto.getCosts()));
					}
				}
			}
		}
		Map<String, List<GoogleReportDisplayDto>> grpForGraph = googleReportDisplayDtoList.stream()
				.collect(Collectors.groupingBy(GoogleReportDisplayDto::getLocationName));
		List<GoogleReportDisplayDto> googleReportGraphDtoList = new ArrayList<GoogleReportDisplayDto>();
		for (String mapKey : grpForGraph.keySet()) {
			GoogleReportDisplayDto newGoogleReportDisplayDto = new GoogleReportDisplayDto();
			newGoogleReportDisplayDto.setLocationName(mapKey);
			googleReportGraphDtoList.add(newGoogleReportDisplayDto);
			for (GoogleReportDisplayDto googleReportDisplayDto : grpForGraph.get(mapKey)) {
				newGoogleReportDisplayDto.setImpressions(
						newGoogleReportDisplayDto.getImpressions() + googleReportDisplayDto.getImpressions());
				newGoogleReportDisplayDto
						.setClicks(newGoogleReportDisplayDto.getClicks() + googleReportDisplayDto.getClicks());
				newGoogleReportDisplayDto
						.setCosts(newGoogleReportDisplayDto.getCosts() + googleReportDisplayDto.getCosts());
			}
			newGoogleReportDisplayDto.setCtr(ReportUtil.calCtr(newGoogleReportDisplayDto.getClicks(),
					newGoogleReportDisplayDto.getImpressions()));
			newGoogleReportDisplayDto.setCpc(
					ReportUtil.calCpc(newGoogleReportDisplayDto.getClicks(), newGoogleReportDisplayDto.getCosts()));
			newGoogleReportDisplayDto.setCpm(ReportUtil.calCpm(newGoogleReportDisplayDto.getImpressions(),
					newGoogleReportDisplayDto.getCosts()));
		}

		googleReportDto.setGoogleReportDisplayDtoList(addTotal(googleReportDisplayDtoList));
		googleReportDto.setGoogleReportGraphDtoList(googleReportGraphDtoList);

		return googleReportDto;
	}

	// レポート表示（日別）
	@Override
	@Transactional
	public GoogleReportDto showDailyReport(GoogleReportSearchDto googleReportSearchDto) {

		GoogleReportDto googleReportDto = new GoogleReportDto();

		// 表示対象キャンペーンを判定
		List<Long> campaignIdList = new ArrayList<Long>();
		if (googleReportSearchDto.getCampaignIdList().size() > 0) {
			campaignIdList = googleReportSearchDto.getCampaignIdList();
		} else {
			campaignIdList = getShopCampaignIdList(ContextUtil.getCurrentShopId());
			googleReportSearchDto.setCampaignIdList(campaignIdList);
		}

		// 表示対象期間を判定
		// 検索条件がないので、一旦こちらをコメントアウトする
//		if (googleReportSearchDto.getPeriod().equals(PeriodSet.WHOLE.getValue())) {
//			googleReportSearchDto.setStartDate(null);
//			googleReportSearchDto.setEndDate(null);
//		}

		// 表示対象キャンペーンのレポート情報を取得
		List<GoogleDeviceReport> googleDeviceReportList = googleDeviceReportCustomDao.selectDailyReport(campaignIdList,
				googleReportSearchDto.getStartDate(), googleReportSearchDto.getEndDate());

		// 表示対象キャンペーンのレポート情報を作成
		List<GoogleReportDisplayDto> googleReportDisplayDtoList = new ArrayList<GoogleReportDisplayDto>();
		if (googleDeviceReportList.size() > 0) {
			for (GoogleDeviceReport googleDeviceReport : googleDeviceReportList) {
				GoogleReportDisplayDto googleReportDisplayDto = new GoogleReportDisplayDto();
				googleReportDisplayDto.setCampaignId(String.valueOf(googleDeviceReport.getCampaignId()));
				googleReportDisplayDto.setCampaignName(googleDeviceReport.getCampaignName());
				googleReportDisplayDto.setDate(googleDeviceReport.getDate());
				googleReportDisplayDto.setDateSlash(StringFormatter.formatToHyphen(googleDeviceReport.getDate()));
				googleReportDisplayDto.setImpressions(googleDeviceReport.getImpressions());
				googleReportDisplayDto.setClicks(googleDeviceReport.getClicks());
				Long displayCosts = ReportUtil.calDisplaySpend(googleDeviceReport.getCosts());
				googleReportDisplayDto.setCosts(displayCosts);
				googleReportDisplayDto
						.setCtr(ReportUtil.calCtr(googleDeviceReport.getClicks(), googleDeviceReport.getImpressions()));
				googleReportDisplayDto.setCpc(ReportUtil.calCpc(googleDeviceReport.getClicks(), displayCosts));
				googleReportDisplayDto.setCpm(ReportUtil.calCpm(googleDeviceReport.getImpressions(), displayCosts));
				googleReportDisplayDtoList.add(googleReportDisplayDto);
			}
		}

		googleReportDto.setGoogleReportDisplayDtoList(addTotal(googleReportDisplayDtoList));

		// 表示対象キャンペーンのグラフ情報を取得
		List<GoogleDeviceReport> googleGraphList = googleDeviceReportCustomDao.selectDailyGraph(campaignIdList,
				googleReportSearchDto.getStartDate(), googleReportSearchDto.getEndDate());

		// 表示対象キャンペーンのグラフ情報を作成
		List<GoogleReportDisplayDto> googleReportGraphDtoList = new ArrayList<GoogleReportDisplayDto>();
		if (googleGraphList.size() > 0) {
			for (GoogleDeviceReport googleDeviceReport : googleGraphList) {
				GoogleReportDisplayDto googleReportDisplayDto = new GoogleReportDisplayDto();
				googleReportDisplayDto.setCampaignId(String.valueOf(googleDeviceReport.getCampaignId()));
				googleReportDisplayDto.setCampaignName(googleDeviceReport.getCampaignName());
				googleReportDisplayDto.setDate(googleDeviceReport.getDate());
				googleReportDisplayDto.setDateSlash(StringFormatter.formatToHyphen(googleDeviceReport.getDate()));
				googleReportDisplayDto.setImpressions(googleDeviceReport.getImpressions());
				googleReportDisplayDto.setClicks(googleDeviceReport.getClicks());
				Long displayCosts = ReportUtil.calDisplaySpend(googleDeviceReport.getCosts());
				googleReportDisplayDto.setCosts(displayCosts);
				googleReportDisplayDto
						.setCtr(ReportUtil.calCtr(googleDeviceReport.getClicks(), googleDeviceReport.getImpressions()));
				googleReportDisplayDto.setCpc(ReportUtil.calCpc(googleDeviceReport.getClicks(), displayCosts));
				googleReportDisplayDto.setCpm(ReportUtil.calCpm(googleDeviceReport.getImpressions(), displayCosts));
				googleReportGraphDtoList.add(googleReportDisplayDto);
			}
		}

		googleReportDto.setGoogleReportGraphDtoList(googleReportGraphDtoList);

		return googleReportDto;
	}

	// CSVダウンロード
	@Override
	@Transactional
	public String download(GoogleReportSearchDto googleReportSearchDto) {

		// 初期処理
		StringWriter out = new StringWriter();
		CsvWriterSettings settings = new CsvWriterSettings();
		CsvWriter writer = null;

		// 書き出し処理
		switch (ReportType.of(googleReportSearchDto.getReportType())) {
		case DEVICE:
			List<GoogleReportDisplayDto> deviceDtoList = showDeviceReport(googleReportSearchDto)
					.getGoogleReportDisplayDtoList();
			List<GoogleDeviceReportCsvBean> googleDeviceReportCsvBeanList = new ArrayList<GoogleDeviceReportCsvBean>();
			for (GoogleReportDisplayDto googleReportDisplayDto : deviceDtoList) {
				GoogleDeviceReportCsvBean googleDeviceReportCsvBean = new GoogleDeviceReportCsvBean();
				googleDeviceReportCsvBean.setCampaignId(googleReportDisplayDto.getCampaignId());
				googleDeviceReportCsvBean.setCampaignName(googleReportDisplayDto.getCampaignName());
				googleDeviceReportCsvBean.setDeviceName(googleReportDisplayDto.getDeviceName());
				googleDeviceReportCsvBean.setImpressions(googleReportDisplayDto.getImpressions());
				googleDeviceReportCsvBean.setClicks(googleReportDisplayDto.getClicks());
				googleDeviceReportCsvBean.setCosts(googleReportDisplayDto.getCosts());
				googleDeviceReportCsvBean.setCtr(googleReportDisplayDto.getCtr() + "%");
				googleDeviceReportCsvBean.setCpc(googleReportDisplayDto.getCpc());
				googleDeviceReportCsvBean.setCpm(googleReportDisplayDto.getCpm());
				googleDeviceReportCsvBeanList.add(googleDeviceReportCsvBean);
			}

			BeanWriterProcessor<GoogleDeviceReportCsvBean> deviceWriterProcessor = new BeanWriterProcessor<>(
					GoogleDeviceReportCsvBean.class);
			settings.setHeaders(GoogleDeviceReportCsvBean.columnName);
			settings.setRowWriterProcessor(deviceWriterProcessor);
			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(googleDeviceReportCsvBeanList);
			break;
		case REGIONS:
			List<GoogleReportDisplayDto> locationDtoList = showLocationReport(googleReportSearchDto)
					.getGoogleReportDisplayDtoList();
			List<GoogleLocationReportCsvBean> googleLocationReportCsvBeanList = new ArrayList<GoogleLocationReportCsvBean>();
			for (GoogleReportDisplayDto googleReportDisplayDto : locationDtoList) {
				GoogleLocationReportCsvBean googleLocationReportCsvBean = new GoogleLocationReportCsvBean();
				googleLocationReportCsvBean.setCampaignId(googleReportDisplayDto.getCampaignId());
				googleLocationReportCsvBean.setCampaignName(googleReportDisplayDto.getCampaignName());
				googleLocationReportCsvBean.setLocationName(googleReportDisplayDto.getLocationName());
				googleLocationReportCsvBean.setImpressions(googleReportDisplayDto.getImpressions());
				googleLocationReportCsvBean.setClicks(googleReportDisplayDto.getClicks());
				googleLocationReportCsvBean.setCosts(googleReportDisplayDto.getCosts());
				googleLocationReportCsvBean.setCtr(googleReportDisplayDto.getCtr() + "%");
				googleLocationReportCsvBean.setCpc(googleReportDisplayDto.getCpc());
				googleLocationReportCsvBean.setCpm(googleReportDisplayDto.getCpm());
				googleLocationReportCsvBeanList.add(googleLocationReportCsvBean);
			}

			BeanWriterProcessor<GoogleLocationReportCsvBean> locationWriterProcessor = new BeanWriterProcessor<>(
					GoogleLocationReportCsvBean.class);
			settings.setHeaders(GoogleLocationReportCsvBean.columnName);
			settings.setRowWriterProcessor(locationWriterProcessor);
			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(googleLocationReportCsvBeanList);
			break;
		case DATE:
			List<GoogleReportDisplayDto> dateDtoList = showDailyReport(googleReportSearchDto)
					.getGoogleReportDisplayDtoList();
			List<GoogleDateReportCsvBean> googleDateReportCsvBeanList = new ArrayList<GoogleDateReportCsvBean>();
			for (GoogleReportDisplayDto googleReportDisplayDto : dateDtoList) {
				GoogleDateReportCsvBean googleDateReportCsvBean = new GoogleDateReportCsvBean();
				googleDateReportCsvBean.setCampaignId(googleReportDisplayDto.getCampaignId());
				googleDateReportCsvBean.setCampaignName(googleReportDisplayDto.getCampaignName());
				googleDateReportCsvBean.setDateSlash(googleReportDisplayDto.getDateSlash());
				googleDateReportCsvBean.setImpressions(googleReportDisplayDto.getImpressions());
				googleDateReportCsvBean.setClicks(googleReportDisplayDto.getClicks());
				googleDateReportCsvBean.setCosts(googleReportDisplayDto.getCosts());
				googleDateReportCsvBean.setCtr(googleReportDisplayDto.getCtr() + "%");
				googleDateReportCsvBean.setCpc(googleReportDisplayDto.getCpc());
				googleDateReportCsvBean.setCpm(googleReportDisplayDto.getCpm());
				googleDateReportCsvBeanList.add(googleDateReportCsvBean);
			}

			BeanWriterProcessor<GoogleDateReportCsvBean> dateWriterProcessor = new BeanWriterProcessor<>(
					GoogleDateReportCsvBean.class);
			settings.setHeaders(GoogleDateReportCsvBean.columnName);
			settings.setRowWriterProcessor(dateWriterProcessor);
			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(googleDateReportCsvBeanList);
			break;
		}

		// 終了処理
		writer.close();
		return out.toString();
	}

	// 合計行作成
	private List<GoogleReportDisplayDto> addTotal(List<GoogleReportDisplayDto> googleReportDisplayDtoList) {

		// 合計計算
		Long tatalImpressions = 0L;
		Long tatalClicks = 0L;
		Long tatalCosts = 0l;
		String tatalCtr = "0";
		Integer tatalCpc = 0;
		Integer tatalCpm = 0;
		if (googleReportDisplayDtoList.size() > 0) {

			for (GoogleReportDisplayDto googleReportDisplayDto : googleReportDisplayDtoList) {
				tatalImpressions = tatalImpressions + googleReportDisplayDto.getImpressions();
				tatalClicks = tatalClicks + googleReportDisplayDto.getClicks();
				tatalCosts = tatalCosts + googleReportDisplayDto.getCosts();
			}
			tatalCtr = ReportUtil.calCtr(tatalClicks, tatalImpressions);
			tatalCpc = ReportUtil.calCpc(tatalClicks, tatalCosts);
			tatalCpm = ReportUtil.calCpm(tatalImpressions, tatalCosts);
		}

		// 合計作成
		GoogleReportDisplayDto googleReportDisplayDto = new GoogleReportDisplayDto();
		googleReportDisplayDto.setCampaignId("合計");
		googleReportDisplayDto.setCampaignName("-");
		googleReportDisplayDto.setDeviceName("-");
		googleReportDisplayDto.setLocationName("-");
		googleReportDisplayDto.setDateSlash("-");
		googleReportDisplayDto.setImpressions(tatalImpressions);
		googleReportDisplayDto.setClicks(tatalClicks);
		googleReportDisplayDto.setCosts(tatalCosts);
		googleReportDisplayDto.setCtr(tatalCtr);
		googleReportDisplayDto.setCpc(tatalCpc);
		googleReportDisplayDto.setCpm(tatalCpm);
		googleReportDisplayDtoList.add(googleReportDisplayDto);
		return googleReportDisplayDtoList;
	}

	// 店舗キャンペーンIDリスト取得
	@Transactional
	private List<Long> getShopCampaignIdList(Long shopId) {
		List<Long> campaignIdList = new ArrayList<Long>();
		// 店舗案件を取得
		List<Issue> issueList = issueCustomDao.selectByShopId(shopId);
		if (issueList.size() > 0) {
			// 店舗案件存在する場合
			List<Long> campaignManageIdList = issueList.stream().filter(obj -> obj.getGoogleCampaignManageId() != null)
					.map(obj -> obj.getGoogleCampaignManageId()).collect(Collectors.toList());
			if (campaignManageIdList.size() > 0) {
				// Googleキャンペーン存在する場合
				List<GoogleCampaignManage> googleCampaignManageList = googleCampaignManageCustomDao
						.selectByCampaignManageIdList(campaignManageIdList);
				campaignIdList = googleCampaignManageList.stream().map(obj -> obj.getCampaignId())
						.collect(Collectors.toList());
			}
		}
		return campaignIdList;
	}
}
