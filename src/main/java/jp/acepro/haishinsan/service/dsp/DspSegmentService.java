package jp.acepro.haishinsan.service.dsp;

import java.time.LocalDateTime;
import java.util.List;

import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.entity.SegmentManage;
import jp.acepro.haishinsan.dto.dsp.DspSegmentDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentGraphDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentSearchDto;
import jp.acepro.haishinsan.dto.dsp.SegmentReportDisplayDto;

public interface DspSegmentService {

	DspSegmentDto createSegment(DspSegmentDto dspSegmentDto);

	List<DspSegmentListDto> segmentList();

	List<SegmentReportDisplayDto> getSegmentReportList(DspSegmentSearchDto dspSegmentSearchDto);

	DspSegmentDto deleteSegment(Long segmentManageId);

	DspSegmentGraphDto getSegmentReportGraph(DspSegmentSearchDto dspSegmentSearchDto);

	void getSegmentReporting();

	SegmentReportDisplayDto getSegmentReportSummary(DspSegmentSearchDto dspSegmentSearchDto);

	String download(DspSegmentSearchDto dspSegmentSearchDto);

	/**
	 * URL検索
	 * 
	 * @param LocalDateTime
	 *            today
	 * @return
	 */
	List<DspSegmentListDto> selectUrlByDateTime(LocalDateTime dateTim);

	/**
	 * URL検索
	 * 
	 * @param LocalDateTime
	 *            today
	 * @return
	 */
	List<DspSegmentListDto> selectUrlByDateTimeWithNoCheck(LocalDateTime dateTim);

	/**
	 * URL一覧
	 * 
	 * @return
	 */
	List<DspSegmentListDto> selectUrlList();

	DspSegmentDto selectBySegmentId(Integer segmentId);

}
