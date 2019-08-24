package jp.acepro.haishinsan.dao;

import java.time.LocalDate;
import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.TwitterRegionReport;
import jp.acepro.haishinsan.dto.twitter.TwitterReportDto;

@Dao
@InjectConfig
public interface TwitterRegionReportCustomDao {

	@Select
	List<TwitterRegionReport> selectByDay(LocalDate day, String campaignId);

	// 地域別レポート
	@Select
	List<TwitterRegionReport> selectRegionReport(TwitterReportDto twitterReportDto);

	// 地域別レポート（グラフ用）
	@Select
	List<TwitterRegionReport> selectRegionReportGraph(TwitterReportDto twitterReportDto);
}
