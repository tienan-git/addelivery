package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.SegmentManage;
import jp.acepro.haishinsan.db.entity.SegmentReportManage;
import jp.acepro.haishinsan.dto.dsp.DspSegmentSearchDto;

@Dao
@InjectConfig
public interface DspSegmentCustomDao {

	@Select
	List<SegmentManage> selectByShopId(Long shopId);
	
	@Select
	List<SegmentManage> selectByShopIdWithEmptyUrl(Long shopId);

	@Select
	SegmentManage selectBySegmentId(Integer segmentId);

	@Select
	List<SegmentReportManage> selectBySegmentIdList(DspSegmentSearchDto dspSegmentSearchDto);

	@Select
	List<SegmentReportManage> selectBySegmentIdListForReport(DspSegmentSearchDto dspSegmentSearchDto);

	@Delete(sqlFile = true)
	int deleteBySegmentIds(List<Integer> segmentIds, String startDate, String endDate);

	@Select
	List<SegmentReportManage> selectReportForList(DspSegmentSearchDto dspSegmentSearchDto);

	@Select
	List<SegmentReportManage> selectReportForGraph(DspSegmentSearchDto dspSegmentSearchDto);

}
