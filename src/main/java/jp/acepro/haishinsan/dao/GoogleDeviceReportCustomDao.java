package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.FacebookDeviceReport;
import jp.acepro.haishinsan.db.entity.GoogleDeviceReport;

@Dao
@InjectConfig
public interface GoogleDeviceReportCustomDao {

	@Select
	GoogleDeviceReport selectForUpdate(Long campaignId, String date, String deviceType);

	@Select
	List<GoogleDeviceReport> selectDeviceReport(List<Long> campaignIdList, String startDate, String endDate);

	@Select
	List<GoogleDeviceReport> selectDeviceGraph(List<Long> campaignIdList, String startDate, String endDate);

	@Select
	List<GoogleDeviceReport> selectDailyReport(List<Long> campaignIdList, String startDate, String endDate);

	@Select
	List<GoogleDeviceReport> selectDailyGraph(List<Long> campaignIdList, String startDate, String endDate);

    // 使った金額を検索（Batch:自動予算変更に使う）
    @Select
    GoogleDeviceReport selectCostFeeByCampaignId(Long campaignId, String date, String startDate);

}
