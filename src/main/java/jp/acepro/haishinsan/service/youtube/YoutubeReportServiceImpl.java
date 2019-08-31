package jp.acepro.haishinsan.service.youtube;

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
import jp.acepro.haishinsan.bean.YoutubeDateReportCsvBean;
import jp.acepro.haishinsan.bean.YoutubeDeviceReportCsvBean;
import jp.acepro.haishinsan.bean.YoutubeLocationReportCsvBean;
import jp.acepro.haishinsan.dao.IssueCustomDao;
import jp.acepro.haishinsan.dao.ShopCustomDao;
import jp.acepro.haishinsan.dao.YoutubeCampaignManageCustomDao;
import jp.acepro.haishinsan.dao.YoutubeDeviceReportCustomDao;
import jp.acepro.haishinsan.dao.YoutubeDeviceReportDao;
import jp.acepro.haishinsan.dao.YoutubeLocationReportCustomDao;
import jp.acepro.haishinsan.dao.YoutubeLocationReportDao;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.db.entity.YoutubeCampaignManage;
import jp.acepro.haishinsan.db.entity.YoutubeDeviceReport;
import jp.acepro.haishinsan.db.entity.YoutubeLocationReport;
import jp.acepro.haishinsan.dto.youtube.YoutubeCampaignInfoDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeDeviceReportDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeLocationReportDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeReportDisplayDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeReportDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeReportSearchDto;
import jp.acepro.haishinsan.enums.GoogleDeviceType;
import jp.acepro.haishinsan.enums.PeriodSet;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.youtube.api.GetYoutubeDeviceReport;
import jp.acepro.haishinsan.service.youtube.api.GetYoutubeLocationReport;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.ReportUtil;
import jp.acepro.haishinsan.util.StringFormatter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class YoutubeReportServiceImpl implements YoutubeReportService {

	@Autowired
	IssueCustomDao issueCustomDao;

	@Autowired
	YoutubeCampaignManageCustomDao youtubeCampaignManageCustomDao;

	@Autowired
	YoutubeDeviceReportDao youtubeDeviceReportDao;

	@Autowired
	YoutubeDeviceReportCustomDao youtubeDeviceReportCustomDao;

	@Autowired
	YoutubeLocationReportDao youtubeLocationReportDao;

	@Autowired
	YoutubeLocationReportCustomDao youtubeLocationReportCustomDao;

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
		GetYoutubeDeviceReport getYoutubeDeviceReport = new GetYoutubeDeviceReport();
		getYoutubeDeviceReport.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
		getYoutubeDeviceReport.googleAccountId = googleAccountId;
		getYoutubeDeviceReport.campaignIdList = campaignIdList;
		getYoutubeDeviceReport.run();
		List<YoutubeDeviceReportDto> youtubeDeviceReportDtoList = new ArrayList<YoutubeDeviceReportDto>();
		youtubeDeviceReportDtoList = getYoutubeDeviceReport.youtubeDeviceReportDtoList;
		// 地域別取得
		GetYoutubeLocationReport getYoutubeLocationReport = new GetYoutubeLocationReport();
		getYoutubeLocationReport.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
		getYoutubeLocationReport.googleAccountId = googleAccountId;
		getYoutubeLocationReport.campaignIdList = campaignIdList;
		getYoutubeLocationReport.run();
		List<YoutubeLocationReportDto> youtubeLocationReportDtoList = new ArrayList<YoutubeLocationReportDto>();
		youtubeLocationReportDtoList = getYoutubeLocationReport.youtubeLocationReportDtoList;

		// キャンペーンレポート情報更新（DBへ）
		// デバイス別更新
		if (youtubeDeviceReportDtoList.size() > 0) {
			for (YoutubeDeviceReportDto youtubeDeviceReportDto : youtubeDeviceReportDtoList) {
				YoutubeDeviceReport youtubeDeviceReportOld = new YoutubeDeviceReport();
				youtubeDeviceReportOld = youtubeDeviceReportCustomDao.selectForUpdate(
						youtubeDeviceReportDto.getCampaignId(), youtubeDeviceReportDto.getDate(),
						youtubeDeviceReportDto.getDeviceType());
				if (youtubeDeviceReportOld != null) {
					// 既存更新
					youtubeDeviceReportOld.setCampaignName(youtubeDeviceReportDto.getCampaignName());
					youtubeDeviceReportOld.setImpressions(youtubeDeviceReportDto.getImpressions());
					youtubeDeviceReportOld.setClicks(youtubeDeviceReportDto.getClicks());
					youtubeDeviceReportOld.setCosts(youtubeDeviceReportDto.getCosts());
					youtubeDeviceReportOld.setVideoViews(youtubeDeviceReportDto.getVideoViews());
					youtubeDeviceReportOld.setVideoViewRate(youtubeDeviceReportDto.getVideoViewRate());
					youtubeDeviceReportOld.setAvgCpv(youtubeDeviceReportDto.getAvgCpv());
					youtubeDeviceReportDao.update(youtubeDeviceReportOld);
				} else {
					// 新規追加
					YoutubeDeviceReport youtubeDeviceReportNew = new YoutubeDeviceReport();
					youtubeDeviceReportNew.setCampaignId(youtubeDeviceReportDto.getCampaignId());
					youtubeDeviceReportNew.setCampaignName(youtubeDeviceReportDto.getCampaignName());
					youtubeDeviceReportNew.setDate(youtubeDeviceReportDto.getDate());
					youtubeDeviceReportNew.setDeviceType(youtubeDeviceReportDto.getDeviceType());
					youtubeDeviceReportNew.setImpressions(youtubeDeviceReportDto.getImpressions());
					youtubeDeviceReportNew.setClicks(youtubeDeviceReportDto.getClicks());
					youtubeDeviceReportNew.setCosts(youtubeDeviceReportDto.getCosts());
					youtubeDeviceReportNew.setVideoViews(youtubeDeviceReportDto.getVideoViews());
					youtubeDeviceReportNew.setVideoViewRate(youtubeDeviceReportDto.getVideoViewRate());
					youtubeDeviceReportNew.setAvgCpv(youtubeDeviceReportDto.getAvgCpv());
					youtubeDeviceReportDao.insert(youtubeDeviceReportNew);
				}
			}
		}
		// 地域別更新
		if (youtubeLocationReportDtoList.size() > 0) {
			for (YoutubeLocationReportDto youtubeLocationReportDto : youtubeLocationReportDtoList) {
				YoutubeLocationReport youtubeLocationReportOld = new YoutubeLocationReport();
				youtubeLocationReportOld = youtubeLocationReportCustomDao.selectForUpdate(
						youtubeLocationReportDto.getCampaignId(), youtubeLocationReportDto.getDate(),
						youtubeLocationReportDto.getLocationId());
				if (youtubeLocationReportOld != null) {
					// 既存更新
					youtubeLocationReportOld.setCampaignName(youtubeLocationReportDto.getCampaignName());
					youtubeLocationReportOld.setImpressions(youtubeLocationReportDto.getImpressions());
					youtubeLocationReportOld.setClicks(youtubeLocationReportDto.getClicks());
					youtubeLocationReportOld.setCosts(youtubeLocationReportDto.getCosts());
					youtubeLocationReportOld.setVideoViews(youtubeLocationReportDto.getVideoViews());
					youtubeLocationReportOld.setVideoViewRate(youtubeLocationReportDto.getVideoViewRate());
					youtubeLocationReportOld.setAvgCpv(youtubeLocationReportDto.getAvgCpv());
					youtubeLocationReportDao.update(youtubeLocationReportOld);
				} else {
					// 新規追加
					YoutubeLocationReport youtubeLocationReportNew = new YoutubeLocationReport();
					youtubeLocationReportNew.setCampaignId(youtubeLocationReportDto.getCampaignId());
					youtubeLocationReportNew.setCampaignName(youtubeLocationReportDto.getCampaignName());
					youtubeLocationReportNew.setDate(youtubeLocationReportDto.getDate());
					youtubeLocationReportNew.setLocationId(youtubeLocationReportDto.getLocationId());
					youtubeLocationReportNew.setImpressions(youtubeLocationReportDto.getImpressions());
					youtubeLocationReportNew.setClicks(youtubeLocationReportDto.getClicks());
					youtubeLocationReportNew.setCosts(youtubeLocationReportDto.getCosts());
					youtubeLocationReportNew.setVideoViews(youtubeLocationReportDto.getVideoViews());
					youtubeLocationReportNew.setVideoViewRate(youtubeLocationReportDto.getVideoViewRate());
					youtubeLocationReportNew.setAvgCpv(youtubeLocationReportDto.getAvgCpv());
					youtubeLocationReportDao.insert(youtubeLocationReportNew);
				}
			}
		}
	}

	// レポート表示（デバイス別）
	@Override
	@Transactional
	public YoutubeReportDto showDeviceReport(YoutubeReportSearchDto youtubeReportSearchDto) {

		YoutubeReportDto youtubeReportDto = new YoutubeReportDto();

		// 表示対象キャンペーンを判定
		List<Long> campaignIdList = new ArrayList<Long>();
		if (youtubeReportSearchDto.getCampaignIdList() != null && youtubeReportSearchDto.getCampaignIdList().size() > 0) {
			campaignIdList = youtubeReportSearchDto.getCampaignIdList();
		} else {
			campaignIdList = getShopCampaignIdList(ContextUtil.getCurrentShopId());
			youtubeReportSearchDto.setCampaignIdList(campaignIdList);
		}

		// 表示対象期間を判定
		if (youtubeReportSearchDto.getPeriod().equals(PeriodSet.WHOLE.getValue())) {
			youtubeReportSearchDto.setStartDate(null);
			youtubeReportSearchDto.setEndDate(null);
		}

		// 表示対象キャンペーンのレポート情報を取得
		List<YoutubeDeviceReport> youtubeDeviceReportList = youtubeDeviceReportCustomDao.selectDeviceReport(
				campaignIdList, youtubeReportSearchDto.getStartDate(), youtubeReportSearchDto.getEndDate());

		// 表示対象キャンペーンのレポート情報を作成
		List<YoutubeReportDisplayDto> youtubeReportDisplayDtoList = new ArrayList<YoutubeReportDisplayDto>();
		if (youtubeDeviceReportList.size() > 0) {
			for (YoutubeDeviceReport youtubeDeviceReport : youtubeDeviceReportList) {
				YoutubeReportDisplayDto youtubeReportDisplayDto = new YoutubeReportDisplayDto();
				youtubeReportDisplayDto.setCampaignId(String.valueOf(youtubeDeviceReport.getCampaignId()));
				youtubeReportDisplayDto.setCampaignName(youtubeDeviceReport.getCampaignName());
				youtubeReportDisplayDto.setDeviceType(youtubeDeviceReport.getDeviceType());
				youtubeReportDisplayDto
						.setDeviceName(GoogleDeviceType.of(youtubeDeviceReport.getDeviceType()).getLabel());
				youtubeReportDisplayDto.setImpressions(youtubeDeviceReport.getImpressions());
				youtubeReportDisplayDto.setClicks(youtubeDeviceReport.getClicks());
				Long displayCosts = ReportUtil.calDisplaySpend(youtubeDeviceReport.getCosts());
				youtubeReportDisplayDto.setCosts(displayCosts);
				youtubeReportDisplayDto.setCtr(
						ReportUtil.calCtr(youtubeDeviceReport.getClicks(), youtubeDeviceReport.getImpressions()));
				youtubeReportDisplayDto.setCpc(ReportUtil.calCpc(youtubeDeviceReport.getClicks(), displayCosts));
				youtubeReportDisplayDto.setCpm(ReportUtil.calCpm(youtubeDeviceReport.getImpressions(), displayCosts));
				youtubeReportDisplayDto.setVideoViews(youtubeDeviceReport.getVideoViews());
				youtubeReportDisplayDto.setVideoViewRate(ReportUtil
						.calVideoViewRate(youtubeDeviceReport.getVideoViews(), youtubeDeviceReport.getImpressions()));
				youtubeReportDisplayDto
						.setAvgCpv(ReportUtil.calAvgCpc(youtubeDeviceReport.getVideoViews(), displayCosts));
				youtubeReportDisplayDtoList.add(youtubeReportDisplayDto);
			}
		}

		youtubeReportDto.setYoutubeReportDisplayDtoList(addTotal(youtubeReportDisplayDtoList));

		// 表示対象キャンペーンのグラフ情報を取得
		List<YoutubeDeviceReport> youtubeGraphList = youtubeDeviceReportCustomDao.selectDeviceGraph(campaignIdList,
				youtubeReportSearchDto.getStartDate(), youtubeReportSearchDto.getEndDate());

		// 表示対象キャンペーンのグラフ情報を作成
		List<YoutubeReportDisplayDto> youtubeReportGraphDtoList = new ArrayList<YoutubeReportDisplayDto>();
		if (youtubeGraphList.size() > 0) {
			for (YoutubeDeviceReport youtubeDeviceReport : youtubeGraphList) {
				YoutubeReportDisplayDto youtubeReportDisplayDto = new YoutubeReportDisplayDto();
				youtubeReportDisplayDto.setDeviceType(youtubeDeviceReport.getDeviceType());
				youtubeReportDisplayDto
						.setDeviceName(GoogleDeviceType.of(youtubeDeviceReport.getDeviceType()).getLabel());
				youtubeReportDisplayDto.setImpressions(youtubeDeviceReport.getImpressions());
				youtubeReportDisplayDto.setClicks(youtubeDeviceReport.getClicks());
				Long displayCosts = ReportUtil.calDisplaySpend(youtubeDeviceReport.getCosts());
				youtubeReportDisplayDto.setCosts(displayCosts);
				youtubeReportDisplayDto.setCtr(
						ReportUtil.calCtr(youtubeDeviceReport.getClicks(), youtubeDeviceReport.getImpressions()));
				youtubeReportDisplayDto.setCpc(ReportUtil.calCpc(youtubeDeviceReport.getClicks(), displayCosts));
				youtubeReportDisplayDto.setCpm(ReportUtil.calCpm(youtubeDeviceReport.getImpressions(), displayCosts));
				youtubeReportDisplayDto.setVideoViews(youtubeDeviceReport.getVideoViews());
				youtubeReportDisplayDto.setVideoViewRate(ReportUtil
						.calVideoViewRate(youtubeDeviceReport.getVideoViews(), youtubeDeviceReport.getImpressions()));
				youtubeReportDisplayDto
						.setAvgCpv(ReportUtil.calAvgCpc(youtubeDeviceReport.getVideoViews(), displayCosts));
				youtubeReportGraphDtoList.add(youtubeReportDisplayDto);
			}
		}

		youtubeReportDto.setYoutubeReportGraphDtoList(youtubeReportGraphDtoList);

		return youtubeReportDto;

	}

	// レポート表示（地域別）
	@Override
	@Transactional
	public YoutubeReportDto showLocationReport(YoutubeReportSearchDto youtubeReportSearchDto) {

		YoutubeReportDto youtubeReportDto = new YoutubeReportDto();

		// 表示対象キャンペーンを判定
		List<Long> campaignIdList = new ArrayList<Long>();
		if (youtubeReportSearchDto.getCampaignIdList().size() > 0) {
			campaignIdList = youtubeReportSearchDto.getCampaignIdList();
		} else {
			campaignIdList = getShopCampaignIdList(ContextUtil.getCurrentShopId());
			youtubeReportSearchDto.setCampaignIdList(campaignIdList);
		}

		// 選択した地域IDを取得(campaignId-locationId)
		List<YoutubeCampaignManage> youtubeCampaignManageList = youtubeCampaignManageCustomDao
				.selectByCampaignManageIdList(campaignIdList);
		List<String> locationIds = new ArrayList<String>();
		for (YoutubeCampaignManage youtubeCampaignManage : youtubeCampaignManageList) {
			StringTokenizer st = new StringTokenizer(youtubeCampaignManage.getArea(), ", ");
			while (st.hasMoreTokens()) {
				locationIds
						.add(youtubeCampaignManage.getCampaignId().toString() + "-" + String.valueOf(st.nextToken()));
			}
		}

		// 表示対象期間を判定
		if (youtubeReportSearchDto.getPeriod().equals(PeriodSet.WHOLE.getValue())) {
			youtubeReportSearchDto.setStartDate(null);
			youtubeReportSearchDto.setEndDate(null);
		}

		// 表示対象キャンペーンのレポート情報を取得
		List<YoutubeLocationReport> youtubeLocationReportList = youtubeLocationReportCustomDao.selectLocationReport(
				campaignIdList, youtubeReportSearchDto.getStartDate(), youtubeReportSearchDto.getEndDate());

		// 表示対象キャンペーンのレポート情報を作成
		List<YoutubeReportDisplayDto> youtubeReportDisplayDtoList = new ArrayList<YoutubeReportDisplayDto>();
		YoutubeReportDisplayDto otherDto = null;
		if (youtubeLocationReportList.size() > 0) {
			for (YoutubeLocationReport youtubeLocationReport : youtubeLocationReportList) {
				// 地域名を取得
				Long locationId = youtubeLocationReport.getLocationId();
				if (CodeMasterServiceImpl.googleAreaNameList == null) {
					codeMasterService.getGoogleAreaList();
				}
				Optional<Pair<Long, String>> locationPair = CodeMasterServiceImpl.googleAreaNameList.stream()
						.filter(obj -> obj.getFirst().equals(locationId)).findFirst();
				String locationName = null;
				if (locationPair.isPresent() && locationIds.contains(
						youtubeLocationReport.getCampaignId() + "-" + youtubeLocationReport.getLocationId())) {
					locationName = locationPair.get().getSecond();
					Long displayCosts = ReportUtil.calDisplaySpend(youtubeLocationReport.getCosts());
					YoutubeReportDisplayDto youtubeReportDisplayDto = new YoutubeReportDisplayDto();
					youtubeReportDisplayDto.setCampaignId(String.valueOf(youtubeLocationReport.getCampaignId()));
					youtubeReportDisplayDto.setCampaignName(youtubeLocationReport.getCampaignName());
					youtubeReportDisplayDto.setLocationId(youtubeLocationReport.getLocationId());
					youtubeReportDisplayDto.setLocationName(locationName);
					youtubeReportDisplayDto.setImpressions(youtubeLocationReport.getImpressions());
					youtubeReportDisplayDto.setClicks(youtubeLocationReport.getClicks());
					youtubeReportDisplayDto.setCosts(displayCosts);
					youtubeReportDisplayDto.setCtr(ReportUtil.calCtr(youtubeLocationReport.getClicks(),
							youtubeLocationReport.getImpressions()));
					youtubeReportDisplayDto.setCpc(ReportUtil.calCpc(youtubeLocationReport.getClicks(), displayCosts));
					youtubeReportDisplayDto
							.setCpm(ReportUtil.calCpm(youtubeLocationReport.getImpressions(), displayCosts));
					youtubeReportDisplayDto.setVideoViews(youtubeLocationReport.getVideoViews());
					youtubeReportDisplayDto.setVideoViewRate(ReportUtil.calVideoViewRate(
							youtubeLocationReport.getVideoViews(), youtubeLocationReport.getImpressions()));
					youtubeReportDisplayDto
							.setAvgCpv(ReportUtil.calAvgCpc(youtubeLocationReport.getVideoViews(), displayCosts));
					youtubeReportDisplayDtoList.add(youtubeReportDisplayDto);
				} else {
					if (otherDto == null
							|| !otherDto.getCampaignId().equals(youtubeLocationReport.getCampaignId().toString())) {
						otherDto = new YoutubeReportDisplayDto();
						otherDto.setCampaignId(String.valueOf(youtubeLocationReport.getCampaignId()));
						otherDto.setCampaignName(youtubeLocationReport.getCampaignName());
						otherDto.setLocationId(youtubeLocationReport.getLocationId());
						otherDto.setLocationName("その他");
						otherDto.setImpressions(youtubeLocationReport.getImpressions());
						otherDto.setClicks(youtubeLocationReport.getClicks());
						otherDto.setCosts(ReportUtil.calDisplaySpend(youtubeLocationReport.getCosts()));
						otherDto.setCtr(ReportUtil.calCtr(otherDto.getClicks(), otherDto.getImpressions()));
						otherDto.setCpc(ReportUtil.calCpc(otherDto.getClicks(), otherDto.getCosts()));
						otherDto.setCpm(ReportUtil.calCpm(otherDto.getImpressions(), otherDto.getCosts()));
						otherDto.setVideoViews(youtubeLocationReport.getVideoViews());
						otherDto.setVideoViewRate(ReportUtil.calVideoViewRate(youtubeLocationReport.getVideoViews(),
								youtubeLocationReport.getImpressions()));
						otherDto.setAvgCpv(
								ReportUtil.calAvgCpc(youtubeLocationReport.getVideoViews(), otherDto.getCosts()));
						youtubeReportDisplayDtoList.add(otherDto);
					} else {
						otherDto.setImpressions(otherDto.getImpressions() + youtubeLocationReport.getImpressions());
						otherDto.setClicks(otherDto.getClicks() + youtubeLocationReport.getClicks());
						otherDto.setCosts(
								otherDto.getCosts() + ReportUtil.calDisplaySpend(youtubeLocationReport.getCosts()));
						otherDto.setCtr(ReportUtil.calCtr(otherDto.getClicks(), otherDto.getImpressions()));
						otherDto.setCpc(ReportUtil.calCpc(otherDto.getClicks(), otherDto.getCosts()));
						otherDto.setCpm(ReportUtil.calCpm(otherDto.getImpressions(), otherDto.getCosts()));
						otherDto.setVideoViews(otherDto.getVideoViews() + youtubeLocationReport.getVideoViews());
						otherDto.setVideoViewRate(
								ReportUtil.calVideoViewRate(otherDto.getVideoViews(), otherDto.getImpressions()));
						otherDto.setAvgCpv(ReportUtil.calAvgCpc(otherDto.getVideoViews(), otherDto.getCosts()));
					}
				}

			}
		}

		Map<String, List<YoutubeReportDisplayDto>> grpForGraph = youtubeReportDisplayDtoList.stream()
				.collect(Collectors.groupingBy(YoutubeReportDisplayDto::getLocationName));
		List<YoutubeReportDisplayDto> youtubeReportGraphDtoList = new ArrayList<YoutubeReportDisplayDto>();
		for (String mapKey : grpForGraph.keySet()) {
			YoutubeReportDisplayDto newYoutubeReportDisplayDto = new YoutubeReportDisplayDto();
			newYoutubeReportDisplayDto.setLocationName(mapKey);
			youtubeReportGraphDtoList.add(newYoutubeReportDisplayDto);
			for (YoutubeReportDisplayDto youtubeReportDisplayDto : grpForGraph.get(mapKey)) {
				newYoutubeReportDisplayDto.setImpressions(
						newYoutubeReportDisplayDto.getImpressions() + youtubeReportDisplayDto.getImpressions());
				newYoutubeReportDisplayDto
						.setClicks(newYoutubeReportDisplayDto.getClicks() + youtubeReportDisplayDto.getClicks());
				newYoutubeReportDisplayDto
						.setCosts(newYoutubeReportDisplayDto.getCosts() + youtubeReportDisplayDto.getCosts());
				newYoutubeReportDisplayDto.setVideoViews(
						newYoutubeReportDisplayDto.getVideoViews() + youtubeReportDisplayDto.getVideoViews());
			}
			newYoutubeReportDisplayDto.setCtr(ReportUtil.calCtr(newYoutubeReportDisplayDto.getClicks(),
					newYoutubeReportDisplayDto.getImpressions()));
			newYoutubeReportDisplayDto.setCpc(
					ReportUtil.calCpc(newYoutubeReportDisplayDto.getClicks(), newYoutubeReportDisplayDto.getCosts()));
			newYoutubeReportDisplayDto.setCpm(ReportUtil.calCpm(newYoutubeReportDisplayDto.getImpressions(),
					newYoutubeReportDisplayDto.getCosts()));
			newYoutubeReportDisplayDto.setVideoViewRate(ReportUtil.calVideoViewRate(
					newYoutubeReportDisplayDto.getVideoViews(), newYoutubeReportDisplayDto.getImpressions()));
			newYoutubeReportDisplayDto.setAvgCpv(ReportUtil.calAvgCpc(newYoutubeReportDisplayDto.getVideoViews(),
					newYoutubeReportDisplayDto.getCosts()));
		}
		youtubeReportDto.setYoutubeReportDisplayDtoList(addTotal(youtubeReportDisplayDtoList));
		youtubeReportDto.setYoutubeReportGraphDtoList(youtubeReportGraphDtoList);

		return youtubeReportDto;
	}

	// レポート表示（日別）
	@Override
	@Transactional
	public YoutubeReportDto showDailyReport(YoutubeReportSearchDto youtubeReportSearchDto) {

		YoutubeReportDto youtubeReportDto = new YoutubeReportDto();

		// 表示対象キャンペーンを判定
		List<Long> campaignIdList = new ArrayList<Long>();
		if (youtubeReportSearchDto.getCampaignIdList().size() > 0) {
			campaignIdList = youtubeReportSearchDto.getCampaignIdList();
		} else {
			campaignIdList = getShopCampaignIdList(ContextUtil.getCurrentShopId());
			youtubeReportSearchDto.setCampaignIdList(campaignIdList);
		}

		// 表示対象期間を判定
		if (youtubeReportSearchDto.getPeriod().equals(PeriodSet.WHOLE.getValue())) {
			youtubeReportSearchDto.setStartDate(null);
			youtubeReportSearchDto.setEndDate(null);
		}

		// 表示対象キャンペーンのレポート情報を取得
		List<YoutubeDeviceReport> youtubeDeviceReportList = youtubeDeviceReportCustomDao.selectDailyReport(
				campaignIdList, youtubeReportSearchDto.getStartDate(), youtubeReportSearchDto.getEndDate());

		// 表示対象キャンペーンのレポート情報を作成
		List<YoutubeReportDisplayDto> youtubeReportDisplayDtoList = new ArrayList<YoutubeReportDisplayDto>();
		if (youtubeDeviceReportList.size() > 0) {
			for (YoutubeDeviceReport youtubeDeviceReport : youtubeDeviceReportList) {
				YoutubeReportDisplayDto youtubeReportDisplayDto = new YoutubeReportDisplayDto();
				youtubeReportDisplayDto.setCampaignId(String.valueOf(youtubeDeviceReport.getCampaignId()));
				youtubeReportDisplayDto.setCampaignName(youtubeDeviceReport.getCampaignName());
				youtubeReportDisplayDto.setDate(youtubeDeviceReport.getDate());
				youtubeReportDisplayDto.setDateSlash(StringFormatter.formatToSlash(youtubeDeviceReport.getDate()));
				youtubeReportDisplayDto.setImpressions(youtubeDeviceReport.getImpressions());
				youtubeReportDisplayDto.setClicks(youtubeDeviceReport.getClicks());
				Long displayCosts = ReportUtil.calDisplaySpend(youtubeDeviceReport.getCosts());
				youtubeReportDisplayDto.setCosts(displayCosts);
				youtubeReportDisplayDto.setCtr(
						ReportUtil.calCtr(youtubeDeviceReport.getClicks(), youtubeDeviceReport.getImpressions()));
				youtubeReportDisplayDto.setCpc(ReportUtil.calCpc(youtubeDeviceReport.getClicks(), displayCosts));
				youtubeReportDisplayDto.setCpm(ReportUtil.calCpm(youtubeDeviceReport.getImpressions(), displayCosts));
				youtubeReportDisplayDto.setVideoViews(youtubeDeviceReport.getVideoViews());
				youtubeReportDisplayDto.setVideoViewRate(ReportUtil
						.calVideoViewRate(youtubeDeviceReport.getVideoViews(), youtubeDeviceReport.getImpressions()));
				youtubeReportDisplayDto
						.setAvgCpv(ReportUtil.calAvgCpc(youtubeDeviceReport.getVideoViews(), displayCosts));
				youtubeReportDisplayDtoList.add(youtubeReportDisplayDto);
			}
		}

		youtubeReportDto.setYoutubeReportDisplayDtoList(addTotal(youtubeReportDisplayDtoList));

		// 表示対象キャンペーンのグラフ情報を取得
		List<YoutubeDeviceReport> youtubeGraphList = youtubeDeviceReportCustomDao.selectDailyGraph(campaignIdList,
				youtubeReportSearchDto.getStartDate(), youtubeReportSearchDto.getEndDate());

		// 表示対象キャンペーンのグラフ情報を作成
		List<YoutubeReportDisplayDto> youtubeReportGraphDtoList = new ArrayList<YoutubeReportDisplayDto>();
		if (youtubeGraphList.size() > 0) {
			for (YoutubeDeviceReport youtubeDeviceReport : youtubeGraphList) {
				YoutubeReportDisplayDto youtubeReportDisplayDto = new YoutubeReportDisplayDto();
				youtubeReportDisplayDto.setDate(youtubeDeviceReport.getDate());
				youtubeReportDisplayDto.setDateSlash(StringFormatter.formatToSlash(youtubeDeviceReport.getDate()));
				youtubeReportDisplayDto.setImpressions(youtubeDeviceReport.getImpressions());
				youtubeReportDisplayDto.setClicks(youtubeDeviceReport.getClicks());
				Long displayCosts = ReportUtil.calDisplaySpend(youtubeDeviceReport.getCosts());
				youtubeReportDisplayDto.setCosts(displayCosts);
				youtubeReportDisplayDto.setCtr(
						ReportUtil.calCtr(youtubeDeviceReport.getClicks(), youtubeDeviceReport.getImpressions()));
				youtubeReportDisplayDto.setCpc(ReportUtil.calCpc(youtubeDeviceReport.getClicks(), displayCosts));
				youtubeReportDisplayDto.setCpm(ReportUtil.calCpm(youtubeDeviceReport.getImpressions(), displayCosts));
				youtubeReportDisplayDto.setVideoViews(youtubeDeviceReport.getVideoViews());
				youtubeReportDisplayDto.setVideoViewRate(ReportUtil
						.calVideoViewRate(youtubeDeviceReport.getVideoViews(), youtubeDeviceReport.getImpressions()));
				youtubeReportDisplayDto
						.setAvgCpv(ReportUtil.calAvgCpc(youtubeDeviceReport.getVideoViews(), displayCosts));
				youtubeReportGraphDtoList.add(youtubeReportDisplayDto);
			}
		}

		youtubeReportDto.setYoutubeReportGraphDtoList(youtubeReportGraphDtoList);

		return youtubeReportDto;
	}

	// キャンペーン一覧取得
	@Override
	@Transactional
	public List<YoutubeCampaignInfoDto> getCampaignList() {

		// キャンペーン情報取得（DBから）
		List<Issue> issueList = issueCustomDao.selectByShopId(ContextUtil.getCurrentShop().getShopId());
		List<Long> campaignManageIdList = issueList.stream().filter(obj -> obj.getYoutubeCampaignManageId() != null)
				.map(obj -> obj.getYoutubeCampaignManageId()).collect(Collectors.toList());
		List<YoutubeCampaignManage> youtubeCampaignManageList = youtubeCampaignManageCustomDao
				.selectByCampaignManageIdList(campaignManageIdList);

		// キャンペーン一覧作成
		List<YoutubeCampaignInfoDto> youtubeCampaignInfoDtoList = new ArrayList<YoutubeCampaignInfoDto>();
		for (YoutubeCampaignManage youtubeCampaignManage : youtubeCampaignManageList) {
			YoutubeCampaignInfoDto youtubeCampaignInfoDto = new YoutubeCampaignInfoDto();
			youtubeCampaignInfoDto.setCampaignId(youtubeCampaignManage.getCampaignId());
			youtubeCampaignInfoDto.setCampaignName(youtubeCampaignManage.getCampaignName());
			youtubeCampaignInfoDtoList.add(youtubeCampaignInfoDto);
		}
		log.debug("youtubeCampaignInfoDtoList : {}", youtubeCampaignInfoDtoList);
		return youtubeCampaignInfoDtoList;
	}

	// CSVダウンロード
	@Override
	@Transactional
	public String download(YoutubeReportSearchDto youtubeReportSearchDto) {

		// 初期処理
		StringWriter out = new StringWriter();
		CsvWriterSettings settings = new CsvWriterSettings();
		CsvWriter writer = null;

		// 書き出し処理
		switch (ReportType.of(youtubeReportSearchDto.getReportType())) {
		case DEVICE:
			List<YoutubeReportDisplayDto> deviceDtoList = showDeviceReport(youtubeReportSearchDto)
					.getYoutubeReportDisplayDtoList();
			List<YoutubeDeviceReportCsvBean> youtubeDeviceReportCsvBeanList = new ArrayList<YoutubeDeviceReportCsvBean>();
			for (YoutubeReportDisplayDto youtubeReportDisplayDto : deviceDtoList) {
				YoutubeDeviceReportCsvBean youtubeDeviceReportCsvBean = new YoutubeDeviceReportCsvBean();
				youtubeDeviceReportCsvBean.setCampaignId(youtubeReportDisplayDto.getCampaignId());
				youtubeDeviceReportCsvBean.setCampaignName(youtubeReportDisplayDto.getCampaignName());
				youtubeDeviceReportCsvBean.setDeviceName(youtubeReportDisplayDto.getDeviceName());
				youtubeDeviceReportCsvBean.setImpressions(youtubeReportDisplayDto.getImpressions());
				youtubeDeviceReportCsvBean.setClicks(youtubeReportDisplayDto.getClicks());
				youtubeDeviceReportCsvBean.setCosts(youtubeReportDisplayDto.getCosts());
				youtubeDeviceReportCsvBean.setCtr(youtubeReportDisplayDto.getCtr() + "%");
				youtubeDeviceReportCsvBean.setCpc(youtubeReportDisplayDto.getCpc());
				youtubeDeviceReportCsvBean.setCpm(youtubeReportDisplayDto.getCpm());
				youtubeDeviceReportCsvBean.setVideoViews(youtubeReportDisplayDto.getVideoViews());
				youtubeDeviceReportCsvBean.setVideoViewRate(youtubeReportDisplayDto.getVideoViewRate() + "%");
				youtubeDeviceReportCsvBean.setAvgCpv(youtubeReportDisplayDto.getAvgCpv());
				youtubeDeviceReportCsvBeanList.add(youtubeDeviceReportCsvBean);
			}

			BeanWriterProcessor<YoutubeDeviceReportCsvBean> deviceWriterProcessor = new BeanWriterProcessor<>(
					YoutubeDeviceReportCsvBean.class);
			settings.setHeaders(YoutubeDeviceReportCsvBean.columnName);
			settings.setRowWriterProcessor(deviceWriterProcessor);
			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(youtubeDeviceReportCsvBeanList);
			break;
		case REGIONS:
			List<YoutubeReportDisplayDto> locationDtoList = showLocationReport(youtubeReportSearchDto)
					.getYoutubeReportDisplayDtoList();
			List<YoutubeLocationReportCsvBean> youtubeLocationReportCsvBeanList = new ArrayList<YoutubeLocationReportCsvBean>();
			for (YoutubeReportDisplayDto youtubeReportDisplayDto : locationDtoList) {
				YoutubeLocationReportCsvBean youtubeLocationReportCsvBean = new YoutubeLocationReportCsvBean();
				youtubeLocationReportCsvBean.setCampaignId(youtubeReportDisplayDto.getCampaignId());
				youtubeLocationReportCsvBean.setCampaignName(youtubeReportDisplayDto.getCampaignName());
				youtubeLocationReportCsvBean.setLocationName(youtubeReportDisplayDto.getLocationName());
				youtubeLocationReportCsvBean.setImpressions(youtubeReportDisplayDto.getImpressions());
				youtubeLocationReportCsvBean.setClicks(youtubeReportDisplayDto.getClicks());
				youtubeLocationReportCsvBean.setCosts(youtubeReportDisplayDto.getCosts());
				youtubeLocationReportCsvBean.setCtr(youtubeReportDisplayDto.getCtr() + "%");
				youtubeLocationReportCsvBean.setCpc(youtubeReportDisplayDto.getCpc());
				youtubeLocationReportCsvBean.setCpm(youtubeReportDisplayDto.getCpm());
				youtubeLocationReportCsvBean.setVideoViews(youtubeReportDisplayDto.getVideoViews());
				youtubeLocationReportCsvBean.setVideoViewRate(youtubeReportDisplayDto.getVideoViewRate() + "%");
				youtubeLocationReportCsvBean.setAvgCpv(youtubeReportDisplayDto.getAvgCpv());
				youtubeLocationReportCsvBeanList.add(youtubeLocationReportCsvBean);
			}

			BeanWriterProcessor<YoutubeLocationReportCsvBean> locationWriterProcessor = new BeanWriterProcessor<>(
					YoutubeLocationReportCsvBean.class);
			settings.setHeaders(YoutubeLocationReportCsvBean.columnName);
			settings.setRowWriterProcessor(locationWriterProcessor);
			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(youtubeLocationReportCsvBeanList);
			break;
		case DATE:
			List<YoutubeReportDisplayDto> dateDtoList = showDailyReport(youtubeReportSearchDto)
					.getYoutubeReportDisplayDtoList();
			List<YoutubeDateReportCsvBean> youtubeDateReportCsvBeanList = new ArrayList<YoutubeDateReportCsvBean>();
			for (YoutubeReportDisplayDto youtubeReportDisplayDto : dateDtoList) {
				YoutubeDateReportCsvBean youtubeDateReportCsvBean = new YoutubeDateReportCsvBean();
				youtubeDateReportCsvBean.setCampaignId(youtubeReportDisplayDto.getCampaignId());
				youtubeDateReportCsvBean.setCampaignName(youtubeReportDisplayDto.getCampaignName());
				youtubeDateReportCsvBean.setDateSlash(youtubeReportDisplayDto.getDateSlash());
				youtubeDateReportCsvBean.setImpressions(youtubeReportDisplayDto.getImpressions());
				youtubeDateReportCsvBean.setClicks(youtubeReportDisplayDto.getClicks());
				youtubeDateReportCsvBean.setCosts(youtubeReportDisplayDto.getCosts());
				youtubeDateReportCsvBean.setCtr(youtubeReportDisplayDto.getCtr() + "%");
				youtubeDateReportCsvBean.setCpc(youtubeReportDisplayDto.getCpc());
				youtubeDateReportCsvBean.setCpm(youtubeReportDisplayDto.getCpm());
				youtubeDateReportCsvBean.setVideoViews(youtubeReportDisplayDto.getVideoViews());
				youtubeDateReportCsvBean.setVideoViewRate(youtubeReportDisplayDto.getVideoViewRate() + "%");
				youtubeDateReportCsvBean.setAvgCpv(youtubeReportDisplayDto.getAvgCpv());
				youtubeDateReportCsvBeanList.add(youtubeDateReportCsvBean);
			}

			BeanWriterProcessor<YoutubeDateReportCsvBean> dateWriterProcessor = new BeanWriterProcessor<>(
					YoutubeDateReportCsvBean.class);
			settings.setHeaders(YoutubeDateReportCsvBean.columnName);
			settings.setRowWriterProcessor(dateWriterProcessor);
			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(youtubeDateReportCsvBeanList);
			break;
		}

		// 終了処理
		writer.close();
		return out.toString();
	}

	// 合計行作成
	private List<YoutubeReportDisplayDto> addTotal(List<YoutubeReportDisplayDto> youtubeReportDisplayDtoList) {

		// 合計計算
		Long tatalImpressions = 0L;
		Long tatalClicks = 0L;
		Long tatalCosts = 0L;
		String tatalCtr = "0";
		Integer tatalCpc = 0;
		Integer tatalCpm = 0;
		Long tatalVideoViews = 0L;
		String tatalVideoViewRate = "0";
		Integer tatalAvgCpv = 0;
		if (youtubeReportDisplayDtoList.size() > 0) {

			// 累計系項目
			for (YoutubeReportDisplayDto youtubeReportDisplayDto : youtubeReportDisplayDtoList) {
				tatalImpressions = tatalImpressions + youtubeReportDisplayDto.getImpressions();
				tatalClicks = tatalClicks + youtubeReportDisplayDto.getClicks();
				tatalCosts = tatalCosts + youtubeReportDisplayDto.getCosts();
				tatalVideoViews = tatalVideoViews + youtubeReportDisplayDto.getVideoViews();
			}

			// 計算系項目
			tatalCtr = ReportUtil.calCtr(tatalClicks, tatalImpressions);
			tatalCpc = ReportUtil.calCpc(tatalClicks, tatalCosts);
			tatalCpm = ReportUtil.calCpm(tatalImpressions, tatalCosts);
			tatalVideoViewRate = ReportUtil.calVideoViewRate(tatalVideoViews, tatalImpressions);
			tatalAvgCpv = ReportUtil.calAvgCpc(tatalVideoViews, tatalCosts);
		}

		// 合計作成
		YoutubeReportDisplayDto youtubeReportDisplayDto = new YoutubeReportDisplayDto();
		youtubeReportDisplayDto.setCampaignId("合計");
		youtubeReportDisplayDto.setCampaignName("-");
		youtubeReportDisplayDto.setDeviceName("-");
		youtubeReportDisplayDto.setLocationName("-");
		youtubeReportDisplayDto.setDateSlash("-");
		youtubeReportDisplayDto.setImpressions(tatalImpressions);
		youtubeReportDisplayDto.setClicks(tatalClicks);
		youtubeReportDisplayDto.setCosts(tatalCosts);
		youtubeReportDisplayDto.setCtr(tatalCtr);
		youtubeReportDisplayDto.setCpc(tatalCpc);
		youtubeReportDisplayDto.setCpm(tatalCpm);
		youtubeReportDisplayDto.setVideoViews(tatalVideoViews);
		youtubeReportDisplayDto.setVideoViewRate(tatalVideoViewRate);
		youtubeReportDisplayDto.setAvgCpv(tatalAvgCpv);
		youtubeReportDisplayDtoList.add(youtubeReportDisplayDto);
		return youtubeReportDisplayDtoList;
	}

	// 店舗キャンペーンIDリスト取得
	private List<Long> getShopCampaignIdList(Long shopId) {
		List<Long> campaignIdList = new ArrayList<Long>();
		// 店舗案件を取得
		List<Issue> issueList = issueCustomDao.selectByShopId(shopId);
		if (issueList.size() > 0) {
			// 店舗案件存在する場合
			List<Long> campaignManageIdList = issueList.stream().filter(obj -> obj.getYoutubeCampaignManageId() != null)
					.map(obj -> obj.getYoutubeCampaignManageId()).collect(Collectors.toList());
			if (campaignManageIdList.size() > 0) {
				// Youtubeキャンペーン存在する場合
				List<YoutubeCampaignManage> youtubeCampaignManageList = youtubeCampaignManageCustomDao
						.selectByCampaignManageIdList(campaignManageIdList);
				campaignIdList = youtubeCampaignManageList.stream().map(obj -> obj.getCampaignId())
						.collect(Collectors.toList());
			}
		}
		return campaignIdList;
	}
}
