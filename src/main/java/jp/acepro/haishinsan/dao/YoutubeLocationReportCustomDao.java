package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.YoutubeLocationReport;

@Dao
@InjectConfig
public interface YoutubeLocationReportCustomDao {

	@Select
	YoutubeLocationReport selectForUpdate(Long campaignId, String date, Long locationId);

	@Select
	List<YoutubeLocationReport> selectLocationReport(List<Long> campaignIdList, String startDate, String endDate);

	@Select
	List<YoutubeLocationReport> selectLocationGraph(List<Long> campaignIdList, String startDate, String endDate);
}
