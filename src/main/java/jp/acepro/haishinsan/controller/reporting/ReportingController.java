package jp.acepro.haishinsan.controller.reporting;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.dto.twitter.TwitterDisplayReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterGraphReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterReportDto;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.issue.TwitterReportingService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/reporting")
public class ReportingController {

    @Autowired
    HttpSession session;

    @Autowired
    OperationService operationService;

    @Autowired
    TwitterReportingService twitterReportingService;

    @GetMapping("/allReporting")
    public ModelAndView getReporting() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("reporting/allReporting");
        return mv;
    }

    @GetMapping("/dspReporting")
    public ModelAndView getDspReporting() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("reporting/dspReporting");
        return mv;
    }

    @GetMapping("/twitterReporting")
    public ModelAndView getTwitterReporting() {

        TwitterReportDto twitterReportDto = new TwitterReportDto();
        twitterReportDto.setCampaignId("cv0bs");
        /**
         * 地域別の場合
         **/
        List<TwitterDisplayReportDto> twitterRegionsTableDtoList = new ArrayList<>();
        TwitterGraphReportDto twitterRegionsGraphReportDto = new TwitterGraphReportDto();
        // Table用データ取得
        twitterRegionsTableDtoList = twitterReportingService.getTwitterRegionReporting(twitterReportDto);
        // Graph用データ取得
        twitterRegionsGraphReportDto = twitterReportingService.getTwitterRegionReportingGraph(twitterReportDto);
        log.debug("----------------------------------------------------------------------");
        log.debug("地域別レポート_tableデータ：" + twitterRegionsTableDtoList.toString());
        log.debug("地域別レポート_graphデータ：" + twitterRegionsGraphReportDto.toString());
        log.debug("----------------------------------------------------------------------");
        /**
         * 日別の場合
         **/
        List<TwitterDisplayReportDto> twitterDateTableDtoList = new ArrayList<>();
        TwitterGraphReportDto twitterDateGraphReportDto = new TwitterGraphReportDto();
        // Table用
        twitterDateTableDtoList = twitterReportingService.getTwitterDayReporting(twitterReportDto);
        // Graph用
        twitterDateGraphReportDto = twitterReportingService.getTwitterDayReportingGraph(twitterReportDto);
        log.debug("----------------------------------------------------------------------");
        log.debug("日別レポート_tableデータ：" + twitterDateTableDtoList.toString());
        log.debug("日別レポート_graphデータ：" + twitterDateGraphReportDto.toString());
        log.debug("----------------------------------------------------------------------");
        /**
         * デバイスの場合
         **/
        List<TwitterDisplayReportDto> twitterDeviceTableDtoList = new ArrayList<>();
        TwitterGraphReportDto twitterDeviceGraphReportDto = new TwitterGraphReportDto();
        twitterDeviceTableDtoList = twitterReportingService.getTwitterDeviceReporting(twitterReportDto);
        // Graph用
        twitterDeviceGraphReportDto = twitterReportingService.getTwitterDeviceReportingGraph(twitterReportDto);
        log.debug("----------------------------------------------------------------------");
        log.debug("デバイス別レポート_tableデータ：" + twitterDeviceTableDtoList.toString());
        log.debug("デバイス別レポート_graphデータ：" + twitterDeviceGraphReportDto.toString());
        log.debug("----------------------------------------------------------------------");
        // viewに設定
        ModelAndView mv = new ModelAndView();
        mv.addObject("twitterRegionsTableDtoList", twitterRegionsTableDtoList);
        mv.addObject("twitterRegionsGraphReportDto", twitterRegionsGraphReportDto);
        mv.addObject("twitterDateTableDtoList", twitterDateTableDtoList);
        mv.addObject("twitterDateGraphReportDto", twitterDateGraphReportDto);
        mv.addObject("twitterDeviceTableDtoList", twitterDeviceTableDtoList);
        mv.addObject("twitterDeviceGraphReportDto", twitterDeviceGraphReportDto);
        mv.setViewName("reporting/twitterReporting");
        return mv;
    }

    @GetMapping("/facebookReporting")
    public ModelAndView getFacebookReporting() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("reporting/facebookReporting");
        return mv;
    }

    @GetMapping("/googleReporting")
    public ModelAndView getGoogleReporting() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("reporting/googleReporting");
        return mv;
    }
}
