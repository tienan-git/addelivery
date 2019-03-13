package jp.acepro.haishinsan.dao;

import java.time.LocalDate;
import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.YahooArea;
import jp.acepro.haishinsan.db.entity.YahooCampaignManage;
import jp.acepro.haishinsan.db.entity.YahooReportManage;
import jp.acepro.haishinsan.entity.YahooIssueDetail;


@Dao
@InjectConfig
public interface YahooCustomDao {


    @Select
    List<YahooCampaignManage> selectByShopId(Long shopId);
    
    @Select
    List<YahooReportManage> selectRegionReport(List<String> campaignIdList, LocalDate startDate, LocalDate endDate);
    
    @Select
    List<YahooReportManage> selectDeviceReport(List<String> campaignIdList, LocalDate startDate, LocalDate endDate);

    @Select
    List<YahooReportManage> selectDateReport(List<String> campaignIdList, LocalDate startDate, LocalDate endDate);

    @Select
    List<YahooArea> selectAllArea();
    
    @Select
    List<YahooArea> selectAreaByAreaId(List<Long> locationIdList);
    
    @Select
    List<Issue> selectIssueByShopId(Long shopId);
    
    @Select
    YahooIssueDetail selectIssueDetail(Long issueId);
    
    // グラフ用
    @Select
    List<YahooReportManage> selectDeviceReportGraph(List<String> campaignIdList, LocalDate startDate, LocalDate endDate);
    
    // グラフ用
    @Select
    List<YahooReportManage> selectDateReportGraph(List<String> campaignIdList, LocalDate startDate, LocalDate endDate);
    
    // グラフ用
    @Select
    List<YahooReportManage> selectRegionReportGraph(List<String> campaignIdList, LocalDate startDate, LocalDate endDate);
    
}
