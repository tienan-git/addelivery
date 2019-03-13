package jp.acepro.haishinsan.dao;

import java.time.LocalDate;
import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.FacebookRegionReport;

@Dao
@InjectConfig
public interface FacebookRegionReportCustomDao {
	
    @Select
    FacebookRegionReport selectByCampaignIdDateRegion(String campaignId, LocalDate date, String region);

    @Select
    List<FacebookRegionReport> selectRegionReport(List<String> campaignIdList, LocalDate startDate, LocalDate endDate);

    // グラフ用
    @Select
    List<FacebookRegionReport> selectRegionReportGraph(List<String> campaignIdList, LocalDate startDate, LocalDate endDate);
    
}
