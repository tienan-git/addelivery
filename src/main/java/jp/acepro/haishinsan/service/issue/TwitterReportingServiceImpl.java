package jp.acepro.haishinsan.service.issue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.bean.TwitterDateReportCsvBean;
import jp.acepro.haishinsan.bean.TwitterDeviceReportCsvBean;
import jp.acepro.haishinsan.bean.TwitterRegionReportCsvBean;
import jp.acepro.haishinsan.dao.ShopCustomDao;
import jp.acepro.haishinsan.dao.TwitterCampaignManageCustomDao;
import jp.acepro.haishinsan.dao.TwitterDeviceReportCustomDao;
import jp.acepro.haishinsan.dao.TwitterDeviceReportDao;
import jp.acepro.haishinsan.dao.TwitterRegionReportCustomDao;
import jp.acepro.haishinsan.dao.TwitterRegionReportDao;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.db.entity.TwitterCampaignManage;
import jp.acepro.haishinsan.db.entity.TwitterDeviceReport;
import jp.acepro.haishinsan.db.entity.TwitterRegionReport;
import jp.acepro.haishinsan.dto.twitter.TwitterCampaignData;
import jp.acepro.haishinsan.dto.twitter.TwitterCampaignDataRes;
import jp.acepro.haishinsan.dto.twitter.TwitterDisplayReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterGraphReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterReport;
import jp.acepro.haishinsan.dto.twitter.TwitterReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterReportIdData;
import jp.acepro.haishinsan.dto.twitter.TwitterReportingJobsRes;
import jp.acepro.haishinsan.dto.twitter.TwitterReportingReq;
import jp.acepro.haishinsan.dto.twitter.TwitterReportingRes;
import jp.acepro.haishinsan.dto.twitter.TwitterShopDto;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.service.BaseService;
import jp.acepro.haishinsan.service.twitter.TwitterCampaignApiService;
import jp.acepro.haishinsan.util.CalculateUtil;
import jp.acepro.haishinsan.util.DateUtil;
import jp.acepro.haishinsan.util.ReportUtil;
import jp.acepro.haishinsan.util.TwitterUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 地域別、デバイス別をのレポートを取得する為Twitterの非同期Analytics APIを使う
 **/
@Slf4j
@Service
public class TwitterReportingServiceImpl extends BaseService implements TwitterReportingService {

    @Autowired
    TwitterCampaignApiService twitterCampaignApiService;

    @Autowired
    TwitterReportingService twitterReportingService;

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    TwitterCampaignManageCustomDao twitterCampaignManageCustomDao;

    @Autowired
    TwitterRegionReportDao twitterRegionReportDao;

    @Autowired
    TwitterRegionReportCustomDao twitterRegionReportCustomtDao;

    @Autowired
    TwitterDeviceReportDao twitterDeviceReportDao;

    @Autowired
    TwitterDeviceReportCustomDao twitterDeviceReportCustomtDao;

    public static String reportZipFileName = "regionsReporting.gz";
    public static String reportUnzipFileName = "regionsReporting.json";
    public static String deviceZipFileName = "deviceReporting.gz";
    public static String deviceUnzipFileName = "deviceReporting.json";

    @Autowired
    ShopCustomDao shopCustomDao;

    // DB : デバイス別レポート（グラフ用）
    @Override
    @Transactional
    public TwitterGraphReportDto getTwitterDeviceReportingGraph(TwitterReportDto twitterReportDto) {

        List<String> reportTypeList = new ArrayList<>(Arrays.asList("x"));
        List<String> impressionList = new ArrayList<>(Arrays.asList("impressions"));
        List<String> clicksList = new ArrayList<>(Arrays.asList("clicks"));
        List<String> followsList = new ArrayList<>(Arrays.asList("follows"));
        List<String> priceList = new ArrayList<>(Arrays.asList("costs"));
        List<String> CTRList = new ArrayList<>(Arrays.asList("ctr"));
        List<String> CPCList = new ArrayList<>(Arrays.asList("cpc"));
        List<String> CPMList = new ArrayList<>(Arrays.asList("cpm"));

        List<TwitterDeviceReport> twitterDeviceReportList = twitterDeviceReportCustomtDao
                .selectDeviceReportGraph(twitterReportDto);

        for (TwitterDeviceReport twitterDeviceReport : twitterDeviceReportList) {

            Long longPrice = CalculateUtil
                    .getRoundedPrice(Double.valueOf(twitterDeviceReport.getBilledChargeLoaclMicro()));
            reportTypeList.add(twitterDeviceReport.getDevice());
            impressionList.add(twitterDeviceReport.getImpressions());
            clicksList.add(twitterDeviceReport.getUrlClicks());
            followsList.add(twitterDeviceReport.getFollows());
            priceList.add(longPrice.toString());
            CTRList.add(ReportUtil.calCtr(Long.valueOf(twitterDeviceReport.getUrlClicks()),
                    Long.valueOf(twitterDeviceReport.getImpressions())).toString());
            CPCList.add(ReportUtil.calCpc(Long.valueOf(twitterDeviceReport.getUrlClicks()), longPrice).toString());
            CPMList.add(ReportUtil.calCpm(Long.valueOf(twitterDeviceReport.getImpressions()), longPrice).toString());
        }

        TwitterGraphReportDto twitterGraphReportDto = new TwitterGraphReportDto();
        twitterGraphReportDto.setReportTypeList(reportTypeList);
        twitterGraphReportDto.setImpressionList(impressionList);
        twitterGraphReportDto.setClicksList(clicksList);
        twitterGraphReportDto.setFollowsList(followsList);
        twitterGraphReportDto.setPriceList(priceList);
        twitterGraphReportDto.setCTRList(CTRList);
        twitterGraphReportDto.setCPCList(CPCList);
        twitterGraphReportDto.setCPMList(CPMList);

        return twitterGraphReportDto;
    }

    // DB : 地域別レポート（グラフ用）
    @Override
    @Transactional
    public TwitterGraphReportDto getTwitterRegionReportingGraph(TwitterReportDto twitterReportDto) {

        List<String> reportTypeList = new ArrayList<>(Arrays.asList("x"));
        List<String> impressionList = new ArrayList<>(Arrays.asList("impressions"));
        List<String> clicksList = new ArrayList<>(Arrays.asList("clicks"));
        List<String> followsList = new ArrayList<>(Arrays.asList("follows"));
        List<String> priceList = new ArrayList<>(Arrays.asList("costs"));
        List<String> CTRList = new ArrayList<>(Arrays.asList("ctr"));
        List<String> CPCList = new ArrayList<>(Arrays.asList("cpc"));
        List<String> CPMList = new ArrayList<>(Arrays.asList("cpm"));

        List<TwitterRegionReport> twitterRegionReportList = twitterRegionReportCustomtDao
                .selectRegionReportGraph(twitterReportDto);

        for (TwitterRegionReport twitterRegionReport : twitterRegionReportList) {

            Long longPrice = CalculateUtil
                    .getRoundedPrice(Double.valueOf(twitterRegionReport.getBilledChargeLoaclMicro()));
            reportTypeList.add(twitterRegionReport.getRegion());
            impressionList.add(twitterRegionReport.getImpressions());
            clicksList.add(twitterRegionReport.getUrlClicks());
            followsList.add(twitterRegionReport.getFollows());
            priceList.add(longPrice.toString());
            CTRList.add(ReportUtil.calCtr(Long.valueOf(twitterRegionReport.getUrlClicks()),
                    Long.valueOf(twitterRegionReport.getImpressions())).toString());
            CPCList.add(ReportUtil.calCpc(Long.valueOf(twitterRegionReport.getUrlClicks()), longPrice).toString());
            CPMList.add(ReportUtil.calCpm(Long.valueOf(twitterRegionReport.getImpressions()), longPrice).toString());
        }

        // グラフ用
        TwitterGraphReportDto twitterGraphReportDto = new TwitterGraphReportDto();
        twitterGraphReportDto.setReportTypeList(reportTypeList);
        twitterGraphReportDto.setImpressionList(impressionList);
        twitterGraphReportDto.setClicksList(clicksList);
        twitterGraphReportDto.setFollowsList(followsList);
        twitterGraphReportDto.setPriceList(priceList);
        twitterGraphReportDto.setCTRList(CTRList);
        twitterGraphReportDto.setCPCList(CPCList);
        twitterGraphReportDto.setCPMList(CPMList);

        return twitterGraphReportDto;
    }

    // DB : 日別レポート（グラフ用）
    @Override
    @Transactional
    public TwitterGraphReportDto getTwitterDayReportingGraph(TwitterReportDto twitterReportDto) {

        List<String> reportTypeList = new ArrayList<>(Arrays.asList("x"));
        List<String> impressionList = new ArrayList<>(Arrays.asList("impressions"));
        List<String> clicksList = new ArrayList<>(Arrays.asList("clicks"));
        List<String> followsList = new ArrayList<>(Arrays.asList("follows"));
        List<String> priceList = new ArrayList<>(Arrays.asList("costs"));
        List<String> CTRList = new ArrayList<>(Arrays.asList("ctr"));
        List<String> CPCList = new ArrayList<>(Arrays.asList("cpc"));
        List<String> CPMList = new ArrayList<>(Arrays.asList("cpm"));

        List<TwitterDeviceReport> twitterDayReportList = twitterDeviceReportCustomtDao
                .selectDayReportGraph(twitterReportDto);

        for (TwitterDeviceReport twitterDeviceReport : twitterDayReportList) {

            Long longPrice = CalculateUtil
                    .getRoundedPrice(Double.valueOf(twitterDeviceReport.getBilledChargeLoaclMicro()));
            reportTypeList.add(DateUtil.fromLocalDate(twitterDeviceReport.getDay()));
            impressionList.add(twitterDeviceReport.getImpressions());
            clicksList.add(twitterDeviceReport.getUrlClicks());
            followsList.add(twitterDeviceReport.getFollows());
            priceList.add(longPrice.toString());
            CTRList.add(ReportUtil.calCtr(Long.valueOf(twitterDeviceReport.getUrlClicks()),
                    Long.valueOf(twitterDeviceReport.getImpressions())).toString());
            CPCList.add(ReportUtil.calCpc(Long.valueOf(twitterDeviceReport.getUrlClicks()), longPrice).toString());
            CPMList.add(ReportUtil.calCpm(Long.valueOf(twitterDeviceReport.getImpressions()), longPrice).toString());
        }

        // グラフ用
        TwitterGraphReportDto twitterGraphReportDto = new TwitterGraphReportDto();
        twitterGraphReportDto.setReportTypeList(reportTypeList);
        twitterGraphReportDto.setImpressionList(impressionList);
        twitterGraphReportDto.setClicksList(clicksList);
        twitterGraphReportDto.setFollowsList(followsList);
        twitterGraphReportDto.setPriceList(priceList);
        twitterGraphReportDto.setCTRList(CTRList);
        twitterGraphReportDto.setCPCList(CPCList);
        twitterGraphReportDto.setCPMList(CPMList);

        return twitterGraphReportDto;
    }

    // DB : デバイス別レポート取得 (Table)
    @Override
    @Transactional
    public List<TwitterDisplayReportDto> getTwitterDeviceReporting(TwitterReportDto twitterReportDto) {
        // DBからレポートデータ取得
        List<TwitterDeviceReport> twitterDeviceReportList = twitterDeviceReportCustomtDao
                .selectDeviceReport(twitterReportDto);
        // campaignIdからcampaignNameを取得する
        TwitterCampaignManage twitterCampaign = twitterCampaignManageCustomDao
                .selectByCampaignId(twitterReportDto.getCampaignIdList().get(0));

        List<TwitterDisplayReportDto> twitterDeviceReportDtoList = new ArrayList<>();

        // Table表示用リスト
        if (twitterDeviceReportList.isEmpty() == false) {
            for (TwitterDeviceReport deviceReport : twitterDeviceReportList) {
                Long longPrice = CalculateUtil
                        .getRoundedPrice(Double.valueOf(deviceReport.getBilledChargeLoaclMicro()));
                TwitterDisplayReportDto twitterDisplayReportDto = new TwitterDisplayReportDto();
                twitterDisplayReportDto.setCampaignId(deviceReport.getCampaignId());
                twitterDisplayReportDto.setCampaignName(twitterCampaign.getCampaignName());
                twitterDisplayReportDto.setDeviceName(deviceReport.getDevice());
                twitterDisplayReportDto.setImpressions(Integer.valueOf(deviceReport.getImpressions()));
                twitterDisplayReportDto.setCosts(longPrice);
                twitterDisplayReportDto.setClicks(Integer.valueOf(deviceReport.getUrlClicks()));
                twitterDisplayReportDto.setFollows(Integer.valueOf(deviceReport.getFollows()));
                twitterDisplayReportDto.setCpc(ReportUtil.calCpc(Long.valueOf(deviceReport.getUrlClicks()), longPrice));
                twitterDisplayReportDto
                        .setCpm(ReportUtil.calCpm(Long.valueOf(deviceReport.getImpressions()), longPrice));
                twitterDisplayReportDto.setCtr(ReportUtil.calCtr(Long.valueOf(deviceReport.getUrlClicks()),
                        Long.valueOf(deviceReport.getImpressions())));
                twitterDeviceReportDtoList.add(twitterDisplayReportDto);
            }
        }

        return getSummary(twitterDeviceReportDtoList);
    }

    // DB : 地域別レポート取得 (Table)
    @Override
    @Transactional
    public List<TwitterDisplayReportDto> getTwitterRegionReporting(TwitterReportDto twitterReportDto) {

        // DBからレポートデータ取得
        List<TwitterRegionReport> twitterRegionReportList = twitterRegionReportCustomtDao
                .selectRegionReport(twitterReportDto);
        // campaignIdからcampaignNameを取得する
        TwitterCampaignManage twitterCampaign = twitterCampaignManageCustomDao
                .selectByCampaignId(twitterReportDto.getCampaignIdList().get(0));

        List<TwitterDisplayReportDto> twitterRegionReportDtoList = new ArrayList<>();

        if (twitterRegionReportList.isEmpty() == false) {
            for (TwitterRegionReport regionReport : twitterRegionReportList) {
                Long longPrice = CalculateUtil
                        .getRoundedPrice(Double.valueOf(regionReport.getBilledChargeLoaclMicro()));
                TwitterDisplayReportDto twitterDisplayReportDto = new TwitterDisplayReportDto();
                twitterDisplayReportDto.setCampaignId(regionReport.getCampaignId());
                twitterDisplayReportDto.setCampaignName(twitterCampaign.getCampaignName());
                twitterDisplayReportDto.setLocationName((regionReport.getRegion()));
                twitterDisplayReportDto.setImpressions(Integer.valueOf(regionReport.getImpressions()));
                twitterDisplayReportDto.setCosts(longPrice);
                twitterDisplayReportDto.setClicks(Integer.valueOf(regionReport.getUrlClicks()));
                twitterDisplayReportDto.setFollows(Integer.valueOf(regionReport.getFollows()));
                twitterDisplayReportDto.setCpc(ReportUtil.calCpc(Long.valueOf(regionReport.getUrlClicks()), longPrice));
                twitterDisplayReportDto
                        .setCpm(ReportUtil.calCpm(Long.valueOf(regionReport.getImpressions()), longPrice));
                twitterDisplayReportDto.setCtr(ReportUtil.calCtr(Long.valueOf(regionReport.getUrlClicks()),
                        Long.valueOf(regionReport.getImpressions())));
                twitterRegionReportDtoList.add(twitterDisplayReportDto);
            }
        }

        return getSummary(twitterRegionReportDtoList);
    }

    // DB : 日別レポート取得 (Table)
    @Override
    @Transactional
    public List<TwitterDisplayReportDto> getTwitterDayReporting(TwitterReportDto twitterReportDto) {
        // DBからレポートデータ取得
        List<TwitterDeviceReport> twitterDayReportList = twitterDeviceReportCustomtDao
                .selectDayReport(twitterReportDto);
        // campaignIdからcampaignNameを取得する
        TwitterCampaignManage twitterCampaign = twitterCampaignManageCustomDao
                .selectByCampaignId(twitterReportDto.getCampaignIdList().get(0));

        List<TwitterDisplayReportDto> twitterDayReportDtoList = new ArrayList<>();

        // Table表示用リスト
        if (twitterDayReportList.isEmpty() == false) {
            for (TwitterDeviceReport deviceReport : twitterDayReportList) {
                Long longPrice = CalculateUtil
                        .getRoundedPrice(Double.valueOf(deviceReport.getBilledChargeLoaclMicro()));
                TwitterDisplayReportDto twitterDisplayReportDto = new TwitterDisplayReportDto();
                twitterDisplayReportDto.setCampaignId(deviceReport.getCampaignId());
                twitterDisplayReportDto.setCampaignName(twitterCampaign.getCampaignName());
                twitterDisplayReportDto.setDay(DateUtil.fromLocalDate(deviceReport.getDay()));
                twitterDisplayReportDto.setImpressions(Integer.valueOf(deviceReport.getImpressions()));
                twitterDisplayReportDto.setCosts(longPrice);
                twitterDisplayReportDto.setClicks(Integer.valueOf(deviceReport.getUrlClicks()));
                twitterDisplayReportDto.setFollows(Integer.valueOf(deviceReport.getFollows()));
                twitterDisplayReportDto.setCpc(ReportUtil.calCpc(Long.valueOf(deviceReport.getUrlClicks()), longPrice));
                twitterDisplayReportDto
                        .setCpm(ReportUtil.calCpm(Long.valueOf(deviceReport.getImpressions()), longPrice));
                twitterDisplayReportDto.setCtr(ReportUtil.calCtr(Long.valueOf(deviceReport.getUrlClicks()),
                        Long.valueOf(deviceReport.getImpressions())));
                twitterDayReportDtoList.add(twitterDisplayReportDto);
            }
        }

        return getSummary(twitterDayReportDtoList);
    }

    // 合計データ
    private List<TwitterDisplayReportDto> getSummary(List<TwitterDisplayReportDto> twitterReportList) {

        TwitterDisplayReportDto sumReportDto = new TwitterDisplayReportDto();
        sumReportDto.setCampaignId("合計");
        sumReportDto.setCampaignName("-");
        sumReportDto.setDeviceName("-");
        sumReportDto.setLocationName("-");
        sumReportDto.setDay("-");
        int sumImpressions = twitterReportList.stream().mapToInt(TwitterDisplayReportDto::getImpressions).sum();
        int sumClicks = twitterReportList.stream().mapToInt(TwitterDisplayReportDto::getClicks).sum();
        Long sumCosts = twitterReportList.stream().mapToLong(TwitterDisplayReportDto::getCosts).sum();
        sumReportDto.setImpressions(sumImpressions);
        sumReportDto.setFollows(twitterReportList.stream().mapToInt(TwitterDisplayReportDto::getFollows).sum());
        sumReportDto.setClicks(sumClicks);
        sumReportDto.setCosts(sumCosts);
        sumReportDto.setCpc(ReportUtil.calCpc(Long.valueOf(sumClicks), sumCosts));
        sumReportDto.setCpm(ReportUtil.calCpm(Long.valueOf(sumImpressions), sumCosts));
        sumReportDto.setCtr(ReportUtil.calCtr(Long.valueOf(sumClicks), Long.valueOf(sumImpressions)));
        twitterReportList.add(sumReportDto);

        return twitterReportList;
    }

    // Batch : 全レポート取得
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void getReport(LocalDate date) {

        List<Shop> shopList = shopCustomDao.selectAllShop();
        log.debug("レポート 全shopList：" + shopList.toString());

        LocalDate dYesterday = date.minusDays(1);
        regionsReportingDetail(date, shopList);
        regionsReportingDetail(dYesterday, shopList);
        deviceReportingDetail(date, shopList);
        deviceReportingDetail(dYesterday, shopList);
    }

    // Batch : デバイス別レポート
    private void deviceReportingDetail(LocalDate date, List<Shop> shopList) {

        // API : デバイスレポートデータ
        List<TwitterReport> allReportList = new ArrayList<TwitterReport>();

        // 全ショップのjobId取得
        List<TwitterShopDto> twitterShopDtoList = new ArrayList<>();
        for (Shop shop : shopList) {
            if (Objects.nonNull(shop.getTwitterAccountId()) && !shop.getTwitterAccountId().isEmpty()) {
                TwitterShopDto twitterShopDto = new TwitterShopDto();
                twitterShopDto.setTwitterAccountId(shop.getTwitterAccountId());
                twitterShopDto.setShopId(shop.getShopId());
                // API : JobIdを取得
                String jobId = getDeviceReportingJobId(date, twitterShopDto);
                twitterShopDto.setJobId(jobId);
                log.debug("デバイスレポート JobId: " + jobId);
                twitterShopDtoList.add(twitterShopDto);
            }
        }

        // TwitterのサーバーからZipの準備は少し時間がかかりそうなので10秒ぐらい待つ
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // jobIdより一つずつレポート取得
        for (TwitterShopDto twitterShopDto : twitterShopDtoList) {
            if (Objects.nonNull(twitterShopDto.getJobId())) {
                List<TwitterReport> reportList = getDeviceReportingList(twitterShopDto);
                allReportList.addAll(reportList);
            }
        }

        // ローカルDB更新
        if (allReportList.isEmpty() == false) {
            for (TwitterReport twitterReport : allReportList) {
                // デバイスレポートリスト
                List<TwitterReportIdData> reportDataList = twitterReport.getId_data();
                // DB検索
                List<TwitterDeviceReport> reportDBList = twitterDeviceReportCustomtDao.selectByDay(date,
                        twitterReport.getId());
                // DBにデータがなければInsert
                if (reportDBList.isEmpty() == true && reportDataList.isEmpty() == false) {
                    saveDeviveReport(reportDataList, reportDBList, twitterReport.getId(), date);
                }
                // DBにデータがあれば削除して再Insert
                if (reportDBList.isEmpty() == false && reportDataList.isEmpty() == false) {
                    twitterDeviceReportDao.delete(reportDBList);
                    saveDeviveReport(reportDataList, reportDBList, twitterReport.getId(), date);
                }

            }
        }

    }

    // Batch : 地域別レポート
    private void regionsReportingDetail(LocalDate date, List<Shop> shopList) {
        // API : デバイスレポートデータ
        List<TwitterReport> allReportList = new ArrayList<TwitterReport>();

        // 全ショップのjobId取得
        List<TwitterShopDto> twitterShopDtoList = new ArrayList<>();
        for (Shop shop : shopList) {
            if (Objects.nonNull(shop.getTwitterAccountId()) && !shop.getTwitterAccountId().isEmpty()) {
                TwitterShopDto twitterShopDto = new TwitterShopDto();
                twitterShopDto.setShopId(shop.getShopId());
                twitterShopDto.setTwitterAccountId(shop.getTwitterAccountId());
                String jobId = getRegionsReportingJobId(date, twitterShopDto);
                twitterShopDto.setJobId(jobId);
                log.debug("地域別レポート JobId: " + jobId);
                twitterShopDtoList.add(twitterShopDto);
            }
        }

        // TwitterのサーバーからZipの準備は少し時間がかかりそうなので10秒ぐらい待つ
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // jobIdより一つずつレポート取得
        for (TwitterShopDto twitterShopDto : twitterShopDtoList) {
            if (Objects.nonNull(twitterShopDto.getJobId())) {
                List<TwitterReport> reportList = getRegionsReportingList(twitterShopDto);
                allReportList.addAll(reportList);
            }
        }

        // ローカルDB更新
        if (allReportList.isEmpty() == false) {
            for (TwitterReport twitterReport : allReportList) {
                // デバイスレポートリスト
                List<TwitterReportIdData> reportDataList = twitterReport.getId_data();
                // DB検索
                List<TwitterRegionReport> reportDBList = twitterRegionReportCustomtDao.selectByDay(date,
                        twitterReport.getId());
                // DBにデータがなければInsert
                if (reportDBList.isEmpty() == true && reportDataList.isEmpty() == false) {
                    saveRegionReport(reportDataList, reportDBList, twitterReport.getId(), date);
                }
                // DBにデータがあれば削除して再Insert
                if (reportDBList.isEmpty() == false && reportDataList.isEmpty() == false) {
                    twitterRegionReportDao.delete(reportDBList);
                    saveRegionReport(reportDataList, reportDBList, twitterReport.getId(), date);
                }

            }
        }
    }

    // DBに保存 : デバイス
    @Transactional
    private void saveDeviveReport(List<TwitterReportIdData> reportDataList, List<TwitterDeviceReport> dbReportList,
            String campaignId, LocalDate day) {

        for (TwitterReportIdData reportIdData : reportDataList) {
            TwitterDeviceReport deiveReport = new TwitterDeviceReport();
            deiveReport.setDevice((reportIdData.getSegment().getSegment_name()));
            deiveReport.setDay(day);
            deiveReport.setBilledEngagements(reportIdData.getMetrics().getBilled_engagements() == null ? "0"
                    : reportIdData.getMetrics().getBilled_engagements().get(0));
            deiveReport.setBilledChargeLoaclMicro(reportIdData.getMetrics().getBilled_charge_local_micro() == null ? "0"
                    : reportIdData.getMetrics().getBilled_charge_local_micro().get(0));
            deiveReport.setCampaignId(campaignId);
            deiveReport.setEngagements(reportIdData.getMetrics().getEngagements() == null ? "0"
                    : reportIdData.getMetrics().getEngagements().get(0));
            deiveReport.setFollows(reportIdData.getMetrics().getFollows() == null ? "0"
                    : reportIdData.getMetrics().getFollows().get(0));
            deiveReport.setImpressions(reportIdData.getMetrics().getImpressions() == null ? "0"
                    : reportIdData.getMetrics().getImpressions().get(0));
            deiveReport.setLikes(
                    reportIdData.getMetrics().getLikes() == null ? "0" : reportIdData.getMetrics().getLikes().get(0));
            deiveReport.setRetweets(reportIdData.getMetrics().getRetweets() == null ? "0"
                    : reportIdData.getMetrics().getRetweets().get(0));
            deiveReport.setUrlClicks(reportIdData.getMetrics().getUrl_clicks() == null ? "0"
                    : reportIdData.getMetrics().getUrl_clicks().get(0));
            twitterDeviceReportDao.insert(deiveReport);
        }

    }

    // DBに保存 : 地域
    @Transactional
    private void saveRegionReport(List<TwitterReportIdData> reportDataList, List<TwitterRegionReport> dbReportList,
            String campaignId, LocalDate day) {

        for (TwitterReportIdData reportIdData : reportDataList) {
            TwitterRegionReport regionReport = new TwitterRegionReport();
            regionReport.setRegion(reportIdData.getSegment().getSegment_name());
            regionReport.setDay(day);
            regionReport.setBilledEngagements(reportIdData.getMetrics().getBilled_engagements() == null ? "0"
                    : reportIdData.getMetrics().getBilled_engagements().get(0));
            regionReport
                    .setBilledChargeLoaclMicro(reportIdData.getMetrics().getBilled_charge_local_micro() == null ? "0"
                            : reportIdData.getMetrics().getBilled_charge_local_micro().get(0));
            regionReport.setCampaignId(campaignId);
            regionReport.setEngagements(reportIdData.getMetrics().getEngagements() == null ? "0"
                    : reportIdData.getMetrics().getEngagements().get(0));
            regionReport.setFollows(reportIdData.getMetrics().getFollows() == null ? "0"
                    : reportIdData.getMetrics().getFollows().get(0));
            regionReport.setImpressions(reportIdData.getMetrics().getImpressions() == null ? "0"
                    : reportIdData.getMetrics().getImpressions().get(0));
            regionReport.setLikes(
                    reportIdData.getMetrics().getLikes() == null ? "0" : reportIdData.getMetrics().getLikes().get(0));
            regionReport.setRetweets(reportIdData.getMetrics().getRetweets() == null ? "0"
                    : reportIdData.getMetrics().getRetweets().get(0));
            regionReport.setUrlClicks(reportIdData.getMetrics().getUrl_clicks() == null ? "0"
                    : reportIdData.getMetrics().getUrl_clicks().get(0));
            twitterRegionReportDao.insert(regionReport);
        }

    }

    // レポートCSVダウンロード
    @Override
    @Transactional
    public String download(TwitterReportDto twitterReportDto) {

        StringWriter out = new StringWriter();
        CsvWriterSettings settings = new CsvWriterSettings();
        CsvWriter writer = null;

        switch (ReportType.of(twitterReportDto.getReportType())) {
        case DEVICE:
            // デバイス
            List<TwitterDisplayReportDto> deviceReportList = twitterReportingService
                    .getTwitterDeviceReporting(twitterReportDto);
            List<TwitterDeviceReportCsvBean> twitterDeviceReportCsvBean = new ArrayList<TwitterDeviceReportCsvBean>();
            for (TwitterDisplayReportDto reportDto : deviceReportList) {
                TwitterDeviceReportCsvBean deviceReportCsvBean = new TwitterDeviceReportCsvBean();
                deviceReportCsvBean.setCampaignId(reportDto.getCampaignId());
                deviceReportCsvBean.setCampaignName(reportDto.getCampaignName());
                deviceReportCsvBean.setDeviceType(reportDto.getDeviceName());
                deviceReportCsvBean.setImpressionCount(reportDto.getImpressions());
                deviceReportCsvBean.setClickCount(reportDto.getClicks());
                deviceReportCsvBean.setFollows(reportDto.getFollows());
                deviceReportCsvBean.setPrice(reportDto.getCosts());
                deviceReportCsvBean.setCpc(reportDto.getCpc().toString());
                deviceReportCsvBean.setCpm(reportDto.getCpm().toString());
                deviceReportCsvBean.setCtr(reportDto.getCtr().toString() + "%");
                twitterDeviceReportCsvBean.add(deviceReportCsvBean);
            }

            BeanWriterProcessor<TwitterDeviceReportCsvBean> deviceWriterProcessor = null;
            settings.setHeaders(TwitterDeviceReportCsvBean.columnName);
            deviceWriterProcessor = new BeanWriterProcessor<>(TwitterDeviceReportCsvBean.class);
            settings.setRowWriterProcessor(deviceWriterProcessor);
            writer = new CsvWriter(out, settings);
            writer.writeHeaders();
            writer.processRecordsAndClose(twitterDeviceReportCsvBean);

            break;
        case REGIONS:
            // 地域別
            List<TwitterDisplayReportDto> regionReportList = getTwitterRegionReporting(twitterReportDto);
            List<TwitterRegionReportCsvBean> twitterRegionReportCsvBean = new ArrayList<TwitterRegionReportCsvBean>();
            for (TwitterDisplayReportDto reportDto : regionReportList) {
                TwitterRegionReportCsvBean regionReportCsvBean = new TwitterRegionReportCsvBean();
                regionReportCsvBean.setCampaignId(reportDto.getCampaignId());
                regionReportCsvBean.setCampaignName(reportDto.getCampaignName());
                regionReportCsvBean.setDeviceType(reportDto.getLocationName());
                regionReportCsvBean.setImpressionCount(reportDto.getImpressions());
                regionReportCsvBean.setClickCount(reportDto.getClicks());
                regionReportCsvBean.setFollows(reportDto.getFollows());
                regionReportCsvBean.setPrice(reportDto.getCosts());
                regionReportCsvBean.setCpc(reportDto.getCpc().toString());
                regionReportCsvBean.setCpm(reportDto.getCpm().toString());
                regionReportCsvBean.setCtr(reportDto.getCtr().toString() + "%");
                twitterRegionReportCsvBean.add(regionReportCsvBean);
            }

            BeanWriterProcessor<TwitterRegionReportCsvBean> dateWriterProcessor = null;
            settings.setHeaders(TwitterRegionReportCsvBean.columnName);
            dateWriterProcessor = new BeanWriterProcessor<>(TwitterRegionReportCsvBean.class);
            settings.setRowWriterProcessor(dateWriterProcessor);
            writer = new CsvWriter(out, settings);
            writer.writeHeaders();
            writer.processRecordsAndClose(twitterRegionReportCsvBean);
            break;
        case DATE:
            // 日別
            List<TwitterDisplayReportDto> dayReportList = getTwitterDayReporting(twitterReportDto);
            List<TwitterDateReportCsvBean> twitterDateReportCsvBean = new ArrayList<TwitterDateReportCsvBean>();
            for (TwitterDisplayReportDto reportDto : dayReportList) {
                TwitterDateReportCsvBean dateReportCsvBean = new TwitterDateReportCsvBean();
                dateReportCsvBean.setCampaignId(reportDto.getCampaignId());
                dateReportCsvBean.setCampaignName(reportDto.getCampaignName());
                dateReportCsvBean.setDate(reportDto.getDay());
                dateReportCsvBean.setImpressionCount(reportDto.getImpressions());
                dateReportCsvBean.setClickCount(reportDto.getClicks());
                dateReportCsvBean.setFollows(reportDto.getFollows());
                dateReportCsvBean.setPrice(reportDto.getCosts());
                dateReportCsvBean.setCpc(reportDto.getCpc().toString());
                dateReportCsvBean.setCpm(reportDto.getCpm().toString());
                dateReportCsvBean.setCtr(reportDto.getCtr().toString() + "%");
                twitterDateReportCsvBean.add(dateReportCsvBean);
            }

            BeanWriterProcessor<TwitterDateReportCsvBean> creativeWriterProcessor = null;
            settings.setHeaders(TwitterDateReportCsvBean.columnName);
            creativeWriterProcessor = new BeanWriterProcessor<>(TwitterDateReportCsvBean.class);
            settings.setRowWriterProcessor(creativeWriterProcessor);
            writer = new CsvWriter(out, settings);
            writer.writeHeaders();
            writer.processRecordsAndClose(twitterDateReportCsvBean);
            break;
        case CREATIVE:
            break;
        }

        // close
        writer.close();

        return out.toString();
    }

    // ＜地域別＞report:jobId取得
    private String getRegionsReportingJobId(LocalDate day, TwitterShopDto twitterShopDto) {

        String jobsId = null;
        try {
            // キャンペーンIdリスト
            String campaignIds = getCampaignIds(twitterShopDto);

            if (Objects.isNull(campaignIds)) {
                return null;
            }

            // パラーメター
            SortedMap<String, String> parameters = new TreeMap<String, String>();
            parameters.put("entity", TwitterUtil.urlEncode("CAMPAIGN"));
            parameters.put("entity_ids", TwitterUtil.urlEncode(campaignIds));// campaignIds
            parameters.put("start_time", TwitterUtil.urlEncode(DateUtil.fromLocalDate(day) + "T00:00:00Z"));
            parameters.put("end_time", TwitterUtil.urlEncode(DateUtil.fromLocalDate(day) + "T23:00:00Z"));
            parameters.put("granularity", TwitterUtil.urlEncode("TOTAL"));
            parameters.put("placement", TwitterUtil.urlEncode("ALL_ON_TWITTER"));
            parameters.put("metric_groups", TwitterUtil.urlEncode("ENGAGEMENT,BILLING"));
            parameters.put("segmentation_type", TwitterUtil.urlEncode("REGIONS"));
            parameters.put("country", TwitterUtil.urlEncode("06ef846bfc783874"));// japan
            // Http Request URL
            String call_url = applicationProperties.getTwitterReporting() + twitterShopDto.getTwitterAccountId()
                    + "?entity=" + TwitterUtil.urlEncode("CAMPAIGN") + "&entity_ids="
                    + TwitterUtil.urlEncode(campaignIds) + "&start_time="
                    + TwitterUtil.urlEncode(DateUtil.fromLocalDate(day) + "T00:00:00Z") + "&end_time="
                    + TwitterUtil.urlEncode(DateUtil.fromLocalDate(day) + "T23:00:00Z") + "&granularity="
                    + TwitterUtil.urlEncode("TOTAL") + "&placement=" + TwitterUtil.urlEncode("ALL_ON_TWITTER")
                    + "&metric_groups=" + TwitterUtil.urlEncode("ENGAGEMENT,BILLING") + "&segmentation_type="
                    + TwitterUtil.urlEncode("REGIONS") + "&country=" + TwitterUtil.urlEncode("06ef846bfc783874");
            // oauth URL
            String auth_url = applicationProperties.getTwitterReporting() + twitterShopDto.getTwitterAccountId();
            String method = "POST";
            // oauth Header
            String auth = twitterCampaignApiService.getHeader(method, auth_url, parameters);
            // Request body
            TwitterReportingReq body = new TwitterReportingReq();
            body.setEntity("CAMPAIGN");
            body.setEntity_ids(campaignIds);
            body.setStart_time(DateUtil.fromLocalDate(day) + "T00:00:00Z");
            body.setEnd_time(DateUtil.fromLocalDate(day) + "T23:00:00Z");
            body.setGranularity("TOTAL");
            body.setPlacement("ALL_ON_TWITTER");
            body.setMetric_groups("ENGAGEMENT,BILLING");
            body.setSegmentation_type("REGIONS");
            body.setCountry("06ef846bfc783874");
            // Call API
            TwitterReportingJobsRes reportingJobs = call(call_url, HttpMethod.POST, body, auth,
                    TwitterReportingJobsRes.class);
            // JobID
            jobsId = reportingJobs.getData().getId_str();

        } catch (Exception e1) {
            e1.printStackTrace();
            throw new SystemException("システムエラー発生しました");
        }

        return jobsId;
    }

    // ＜地域別＞report:jobIdよりレポート取得
    private List<TwitterReport> getRegionsReportingList(TwitterShopDto twitterShopDto) {

        List<TwitterReport> reportList = new ArrayList<>();
        try {

            // レポートZipのURLを取得
            String url = getReporting(twitterShopDto);
            log.debug("＜地域別＞report Url : " + url);
            if (url != null) {
                // レポートZipのダウンロード
                String filePath = downloadFile(url, reportZipFileName);
                // Zip解圧
                unGzipFile(filePath, reportUnzipFileName);
                // .jsonファイルを読み込んで文字列Jsonを取得
                String stringJson = readJsonData(reportUnzipFileName);
                // System.out.println("stringJson : " + stringJson);
                // Json解析
                Gson gson = new Gson();
                TwitterReportingRes reportRes = gson.fromJson(stringJson, TwitterReportingRes.class);
                // レポートリスト
                reportList = reportRes.getData();
                log.debug("＜地域別＞report Data : " + reportList.toString());
            } else {
                throw new SystemException("Zipファイルはまだ用意されていません");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new SystemException("システムエラー発生しました");
        }

        return reportList;
    }

    // ＜デバイス別＞report:jobId取得
    private String getDeviceReportingJobId(LocalDate day, TwitterShopDto twitterShopDto) {

        String jobsId = null;
        // キャンペーンIdリスト
        String campaignIds = getCampaignIds(twitterShopDto);

        if (Objects.isNull(campaignIds)) {
            return null;
        }

        try {
            // パラーメター
            SortedMap<String, String> parameters = new TreeMap<String, String>();
            parameters.put("entity", TwitterUtil.urlEncode("CAMPAIGN"));
            parameters.put("entity_ids", TwitterUtil.urlEncode(campaignIds));// campaignIds
            parameters.put("start_time", TwitterUtil.urlEncode(DateUtil.fromLocalDate(day) + "T00:00:00Z"));
            parameters.put("end_time", TwitterUtil.urlEncode(DateUtil.fromLocalDate(day) + "T23:00:00Z"));
            parameters.put("granularity", TwitterUtil.urlEncode("TOTAL"));
            parameters.put("placement", TwitterUtil.urlEncode("ALL_ON_TWITTER"));
            parameters.put("metric_groups", TwitterUtil.urlEncode("ENGAGEMENT,BILLING"));
            parameters.put("segmentation_type", TwitterUtil.urlEncode("PLATFORMS"));
            // Http Request URL
            String call_url = applicationProperties.getTwitterReporting() + twitterShopDto.getTwitterAccountId()
                    + "?entity=" + TwitterUtil.urlEncode("CAMPAIGN") + "&entity_ids="
                    + TwitterUtil.urlEncode(campaignIds) + "&start_time="
                    + TwitterUtil.urlEncode(DateUtil.fromLocalDate(day) + "T00:00:00Z") + "&end_time="
                    + TwitterUtil.urlEncode(DateUtil.fromLocalDate(day) + "T23:00:00Z") + "&granularity="
                    + TwitterUtil.urlEncode("TOTAL") + "&placement=" + TwitterUtil.urlEncode("ALL_ON_TWITTER")
                    + "&metric_groups=" + TwitterUtil.urlEncode("ENGAGEMENT,BILLING") + "&segmentation_type="
                    + TwitterUtil.urlEncode("PLATFORMS");
            // oauth URL
            String auth_url = applicationProperties.getTwitterReporting() + twitterShopDto.getTwitterAccountId();
            String method = "POST";
            // oauth Header
            String auth = twitterCampaignApiService.getHeader(method, auth_url, parameters);
            // Request body
            TwitterReportingReq body = new TwitterReportingReq();
            body.setEntity("CAMPAIGN");
            body.setEntity_ids(campaignIds);
            body.setStart_time(DateUtil.fromLocalDate(day) + "T00:00:00Z");
            body.setEnd_time(DateUtil.fromLocalDate(day) + "T23:00:00Z");
            body.setGranularity("TOTAL");
            body.setPlacement("ALL_ON_TWITTER");
            body.setMetric_groups("ENGAGEMENT,BILLING");
            body.setSegmentation_type("PLATFORMS");
            // Call API
            TwitterReportingJobsRes reportingJobs = call(call_url, HttpMethod.POST, body, auth,
                    TwitterReportingJobsRes.class);
            // JobId
            jobsId = reportingJobs.getData().getId_str();

        } catch (Exception e1) {
            e1.printStackTrace();
            throw new SystemException("システムエラー発生しました");
        }
        return jobsId;

    }

    // ＜デバイス別＞report:jobIdよりレポート取得
    private List<TwitterReport> getDeviceReportingList(TwitterShopDto twitterShopDto) {

        List<TwitterReport> reportList = new ArrayList<>();

        try {
            // レポートZipのURLを取得
            String url = getReporting(twitterShopDto);
            log.debug("＜デバイス別＞report Url : " + url);
            if (url != null) {
                // レポートZipのダウンロード
                String filePath = downloadFile(url, deviceZipFileName);
                // Zip解圧
                unGzipFile(filePath, deviceUnzipFileName);
                // .jsonファイルを読み込んで文字列Jsonを取得
                String stringJson = readJsonData(deviceUnzipFileName);
                // Json解析
                Gson gson = new Gson();
                TwitterReportingRes reportRes = gson.fromJson(stringJson, TwitterReportingRes.class);
                // レポートリスト
                reportList = reportRes.getData();
                log.debug("＜デバイス別＞report Data : " + reportList.toString());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new SystemException("システムエラー発生しました");
        }
        return reportList;
    }

    // API : レポートZipファイルの取得
    @Transactional
    private String getReporting(TwitterShopDto twitterShopDto) {

        String zipUrl = null;
        try {
            // パラーメター
            SortedMap<String, String> parameters = new TreeMap<String, String>();
            parameters.put("job_ids", TwitterUtil.urlEncode(twitterShopDto.getJobId()));
            // Http Request URL
            String call_url = applicationProperties.getTwitterReporting() + twitterShopDto.getTwitterAccountId()
                    + "?job_ids=" + TwitterUtil.urlEncode(twitterShopDto.getJobId());
            // oauth URL
            String auth_url = applicationProperties.getTwitterReporting() + twitterShopDto.getTwitterAccountId();
            String method = "GET";
            // oauth Header
            String auth = twitterCampaignApiService.getHeader(method, auth_url, parameters);
            // Request body
            TwitterReportingReq body = new TwitterReportingReq();
            body.setJob_ids(twitterShopDto.getJobId());
            // Call API
            TwitterReportingRes reportingJobs = call(call_url, HttpMethod.GET, body, auth, TwitterReportingRes.class);
            List<TwitterReport> reportUrlZip = reportingJobs.getData();
            TwitterReport report = reportUrlZip.get(0);
            // レポートZipファイルURL
            zipUrl = report.getUrl();
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new SystemException("システムエラー発生しました");
        }
        return zipUrl;
    }

    // API : TwitterキャンペーンIdリストを取得
    @Transactional
    private String getCampaignIds(TwitterShopDto twitterShopDto) {

        String campaignIds = null;
        try {
            // パラーメター
            SortedMap<String, String> parameters = new TreeMap<String, String>();
            // Http Request URL
            String call_url = applicationProperties.getTwitterhost() + twitterShopDto.getTwitterAccountId()
                    + applicationProperties.getTwitterCreatCampaign();
            String method = "GET";
            // oauth Header
            String auth = twitterCampaignApiService.getHeader(method, call_url, parameters);
            // Call API
            TwitterCampaignDataRes campaignRes = call(call_url, HttpMethod.GET, null, auth,
                    TwitterCampaignDataRes.class);
            // キャンペーンリスト
            List<TwitterCampaignData> campaignList = campaignRes.getData();
            List<String> campaignIdList = new ArrayList<>();
            if (Objects.nonNull(campaignList) && !campaignList.isEmpty()) {
                for (TwitterCampaignData campaign : campaignList) {
                    if (campaign.getServable().equals("true") || campaign.getEntity_status().equals("PAUSED")) {
                        campaignIdList.add(campaign.getId());
                    }
                }
            }
            // DBからキャンペーンリストを取得する
            List<TwitterCampaignManage> dbCampaignList = twitterCampaignManageCustomDao
                    .selectAll(twitterShopDto.getShopId());
            List<String> dbCampaignIdList = new ArrayList<>();
            if (Objects.nonNull(dbCampaignList) && !dbCampaignList.isEmpty()) {
                for (TwitterCampaignManage campaign : dbCampaignList) {
                    dbCampaignIdList.add(campaign.getCampaignId());
                }
            }
            // 交集
            campaignIdList.retainAll(dbCampaignIdList);
            if (campaignIdList.isEmpty()) {
                return null;
            }
            // 文字列のcampaignIds（カンマ）
            campaignIds = TwitterUtil.formatToOneString(campaignIdList);
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new SystemException("システムエラー発生しました");
        }
        return campaignIds;

    }

    // WebSiteからファイルをダウンロード
    @Transactional
    public String downloadFile(String url, String fileName) throws Exception {

        Path path = Paths.get(fileName);
        path.toAbsolutePath().toString();
        try (InputStream in = URI.create(url).toURL().openStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        }
        return path.toAbsolutePath().toString();
    }

    // ZipファイルをUnZip解压
    @Transactional
    public void unGzipFile(String sourcedir, String opfile) {

        String ouputfile = "";
        try {
            // 建立gzip压缩文件输入流 
            FileInputStream fin = new FileInputStream(sourcedir);
            // 建立gzip解压工作流
            GZIPInputStream gzin = new GZIPInputStream(fin);
            // 建立解压文件输出流
            ouputfile = opfile;
            FileOutputStream fout = new FileOutputStream(ouputfile);
            int num;
            byte[] buf = new byte[1024];
            while ((num = gzin.read(buf, 0, buf.length)) != -1) {
                fout.write(buf, 0, num);
            }
            gzin.close();
            fout.close();
            fin.close();
        } catch (Exception ex) {
            throw new SystemException("システムエラー発生しました");
        }
        return;
    }

    // .jsonファイルを読み込んでStringに変更
    @Transactional
    public String readJsonData(String jsonFile) throws IOException {

        StringBuffer strbuffer = new StringBuffer();
        File myFile = new File(jsonFile);
        if (!myFile.exists()) {
            System.err.println("Can't Find" + jsonFile);
        }
        try {
            FileInputStream fis = new FileInputStream(jsonFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
            BufferedReader in = new BufferedReader(inputStreamReader);

            String str;
            while ((str = in.readLine()) != null) {
                strbuffer.append(str);
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
            throw new SystemException("システムエラー発生しました");
        }
        return strbuffer.toString();
    }
}
