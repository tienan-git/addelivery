package jp.acepro.haishinsan.service.dsp;

import java.util.List;

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

}
