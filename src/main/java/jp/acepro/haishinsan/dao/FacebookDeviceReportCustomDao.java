package jp.acepro.haishinsan.dao;

import java.time.LocalDate;
import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.FacebookDeviceReport;

@Dao
@InjectConfig
public interface FacebookDeviceReportCustomDao {

	@Select
	FacebookDeviceReport selectByCampaignIdDateDevice(String campaignId, LocalDate date, String device);

	@Select
	List<FacebookDeviceReport> selectDeviceReport(List<String> campaignIdList, LocalDate startDate, LocalDate endDate);

	@Select
	List<FacebookDeviceReport> selectDateReport(List<String> campaignIdList, LocalDate startDate, LocalDate endDate);

	// グラフ用
	@Select
	List<FacebookDeviceReport> selectDeviceReportGraph(List<String> campaignIdList, LocalDate startDate,
			LocalDate endDate);

	// グラフ用
	@Select
	List<FacebookDeviceReport> selectDateReportGraph(List<String> campaignIdList, LocalDate startDate,
			LocalDate endDate);
	
    // 使った金額を検索（Batch:自動予算変更に使う）
    @Select
    FacebookDeviceReport selectCostFeeByCampaignId(String campaignId, String date, String startDate);

}
