package jp.acepro.haishinsan.dao;

import java.time.LocalDate;
import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.TwitterDeviceReport;
import jp.acepro.haishinsan.dto.twitter.TwitterReportDto;

@Dao
@InjectConfig
public interface TwitterDeviceReportCustomDao {

	@Select
	List<TwitterDeviceReport> selectByDay(LocalDate day, String campaignId);

	// デバイス別で検索
	@Select
	List<TwitterDeviceReport> selectDeviceReport(TwitterReportDto twitterReportDto);

	// 日別で検索
	@Select
	List<TwitterDeviceReport> selectDayReport(TwitterReportDto twitterReportDto);

	// デバイス別で検索（グラフ）
	@Select
	List<TwitterDeviceReport> selectDeviceReportGraph(TwitterReportDto twitterReportDto);

	// 日別で検索（グラフ）
	@Select
	List<TwitterDeviceReport> selectDayReportGraph(TwitterReportDto twitterReportDto);
}
