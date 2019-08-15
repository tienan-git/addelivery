package jp.acepro.haishinsan.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.facebook.ads.sdk.AdsInsights.EnumDatePreset;

import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.google.GoogleReportService;
import jp.acepro.haishinsan.service.issue.FacebookReportingService;
import jp.acepro.haishinsan.service.issue.TwitterReportingService;
import jp.acepro.haishinsan.service.youtube.YoutubeReportService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportApiServiceImpl implements ReportApiService {

    @Autowired
    OperationService operationService;

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

    @Async
    @Override
    @Transactional
    public void executeAsync() {
        try {
            // DSPのレポートを取得
            dspApiService.getDspCampaignReporting();
            // オペレーションログ記録
            operationService.createWithoutUser(Operation.GET_DSP_REPORT_RAWDATA.getValue(), "DSPレポート生データ取得が成功しました。");
        } catch (Exception e) {
            log.error("DSPレポートデータ取得中エラー発生", e);
            // オペレーションログ記録
            operationService.createWithoutUser(Operation.GET_DSP_REPORT_RAWDATA.getValue(), e.getMessage());
        }
        try {
            // Googleのレポートを取得
            googleReportService.getReport();
            // オペレーションログ記録
            operationService.createWithoutUser(Operation.GET_GOOGLE_REPORT_RAWDATA.getValue(),
                    "Googleレポート生データ取得が成功しました。");
        } catch (Exception e) {
            log.error("Googleレポートデータ取得中エラー発生", e);
            // オペレーションログ記録
            operationService.createWithoutUser(Operation.GET_GOOGLE_REPORT_RAWDATA.getValue(), e.getMessage());
        }
        try {
            // Facebookのレポートを取得
        	facebookReportingService.getReportDetails(EnumDatePreset.VALUE_TODAY);
        	facebookReportingService.getReportDetails(EnumDatePreset.VALUE_YESTERDAY);
            // オペレーションログ記録
            operationService.createWithoutUser(Operation.GET_FACEBOOK_REPORT_RAWDATA.getValue(),
                    "Facebookレポート生データ取得が成功しました。");
        } catch (Exception e) {
            log.error("Facebookレポートデータ取得中エラー発生", e);
            // オペレーションログ記録
            operationService.createWithoutUser(Operation.GET_FACEBOOK_REPORT_RAWDATA.getValue(), e.getMessage());
        }
        try {
            // Twitterのレポートを取得
            // 今日と昨日の時間を取得
            LocalDate dNow = LocalDate.now();
            twitterReportingService.getReport(dNow);
            // オペレーションログ記録
            operationService.createWithoutUser(Operation.GET_TWITTER_REPORT_RAWDATA.getValue(),
                    "Twitterレポート生データ取得が成功しました。");
        } catch (Exception e) {
            log.error("Twitterレポートデータ取得中エラー発生", e);
            // オペレーションログ記録
            operationService.createWithoutUser(Operation.GET_TWITTER_REPORT_RAWDATA.getValue(), e.getMessage());
        }
        try {
            // Youtubeのレポートを取得
            youtubeReportService.getReport();
            // オペレーションログ記録
            operationService.createWithoutUser(Operation.GET_YOUTUBE_REPORT_RAWDATA.getValue(),
                    "Youtubeレポート生データ取得が成功しました。");
        } catch (Exception e) {
            log.error("Youtubeレポートデータ取得中エラー発生", e);
            // オペレーションログ記録
            operationService.createWithoutUser(Operation.GET_YOUTUBE_REPORT_RAWDATA.getValue(), e.getMessage());
        }
        try {
            // セグメントのレポートを取得
            dspSegmentService.getSegmentReporting();
            // オペレーションログ記録
            operationService.createWithoutUser(Operation.GET_SEGMENT_REPORT_RAWDATA.getValue(),
                    "セグメントレポート生データ取得が成功しました。");
        } catch (Exception e) {
            log.error("セグメントレポートデータ取得中エラー発生", e);
            // オペレーションログ記録
            operationService.createWithoutUser(Operation.GET_SEGMENT_REPORT_RAWDATA.getValue(), e.getMessage());
        }
    }

}
