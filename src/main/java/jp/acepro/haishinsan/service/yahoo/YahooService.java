package jp.acepro.haishinsan.service.yahoo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.acepro.haishinsan.bean.YahooCsvBean;
import jp.acepro.haishinsan.db.entity.YahooCampaignManage;
import jp.acepro.haishinsan.dto.dsp.DspSegmentDto;
import jp.acepro.haishinsan.dto.yahoo.YahooGraphReportDto;
import jp.acepro.haishinsan.dto.yahoo.YahooIssueDto;
import jp.acepro.haishinsan.dto.yahoo.YahooLocationDto;
import jp.acepro.haishinsan.dto.yahoo.YahooReportDisplayDto;

public interface YahooService {

    List<YahooCsvBean> readCsv(MultipartFile csvFile);

    void uploadData(List<YahooCsvBean> yahooCsvBeanList);

    List<YahooCampaignManage> searchYahooCampaignManageList();

    List<YahooReportDisplayDto> getDeviceReport(List<String> campaignIdList, String startDate, String endDate);

    List<YahooReportDisplayDto> getRegionReport(List<String> campaignIdList, String startDate, String endDate);

    List<YahooReportDisplayDto> getDateReport(List<String> campaignIdList, String startDate, String endDate);

    List<YahooIssueDto> searchYahooIssueList();

    List<DspSegmentDto> searchSegmentList();

    List<YahooLocationDto> getLocationList(List<Long> locationIdList);

    YahooIssueDto createIssue(YahooIssueDto yahooIssueDto, List<String> imaBase64List);

    YahooIssueDto getIssueDetail(Long issueId);

    YahooIssueDto deleteIssue(Long issueId);

    void updateIssue(Long issueId, String campaignId);

    YahooGraphReportDto getYahooDeviceReportingGraph(List<String> campaignIdList, String startDate, String endDate);

    YahooGraphReportDto getYahooDateReportingGraph(List<String> campaignIdList, String startDate, String endDate);

    YahooGraphReportDto getYahooRegionReportingGraph(List<String> campaignIdList, String startDate, String endDate);

    String download(List<String> campaignIdList, String startDate, String endDate, Integer reportType);

    // 入力チェック
    void yahooCampaignCheck(YahooIssueDto yahooIssueDto);
}
