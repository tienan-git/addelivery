package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.GoogleLocationReport;

@Dao
@InjectConfig
public interface GoogleLocationReportCustomDao {

	@Select
	GoogleLocationReport selectForUpdate(Long campaignId, String date, Long locationId);

	@Select
	List<GoogleLocationReport> selectLocationReport(List<Long> campaignIdList, String startDate, String endDate);

	@Select
	List<GoogleLocationReport> selectLocationGraph(List<Long> campaignIdList, String startDate, String endDate);
}
