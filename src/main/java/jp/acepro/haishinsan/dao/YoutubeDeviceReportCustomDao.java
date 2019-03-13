package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.YoutubeDeviceReport;

@Dao
@InjectConfig
public interface YoutubeDeviceReportCustomDao {

	@Select
	YoutubeDeviceReport selectForUpdate(Long campaignId, String date, String deviceType);
	
	@Select
	List<YoutubeDeviceReport> selectDeviceReport(List<Long> campaignIdList, String startDate, String endDate);
	
	@Select
	List<YoutubeDeviceReport> selectDeviceGraph(List<Long> campaignIdList, String startDate, String endDate);
	
	@Select
	List<YoutubeDeviceReport> selectDailyReport(List<Long> campaignIdList, String startDate, String endDate);
	
	@Select
	List<YoutubeDeviceReport> selectDailyGraph(List<Long> campaignIdList, String startDate, String endDate);
}
