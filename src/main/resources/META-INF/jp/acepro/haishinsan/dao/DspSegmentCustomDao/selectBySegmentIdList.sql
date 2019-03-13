SELECT
	/*%expand*/*
FROM
	segment_report_manage
WHERE
	is_actived = 1
	/*%if dspSegmentSearchDto.segmentIdList != null && dspSegmentSearchDto.segmentIdList.size() != 0*/
	and segment_id in /*dspSegmentSearchDto.segmentIdList*/('688599','688600','688603')
	/*%end*/
	/*%if dspSegmentSearchDto.startDate != null */ 
	and date >= /* dspSegmentSearchDto.startDate */'2018-10-05' 
	/*%end*/ 
	/*%if dspSegmentSearchDto.endDate != null */ 
	and date <= /* dspSegmentSearchDto.endDate */'2018-10-12' 
	/*%end*/
