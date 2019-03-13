select
	segment_id,
	date,
	SUM(uunum) as uunum, 
	SUM(uunum_pc) as uunum_pc, 
	SUM(uunum_sp) as uunum_sp
from
	segment_report_manage
where
	is_actived = 1
	/*%if dspSegmentSearchDto.segmentIdList != null && dspSegmentSearchDto.segmentIdList.size() != 0*/ 
	and segment_id in /* dspSegmentSearchDto.segmentIdList */('1','2') 
	/*%end*/
	/*%if dspSegmentSearchDto.startDate != null */ 
	and date >= /* dspSegmentSearchDto.startDate */'2018-10-05' 
	/*%end*/ 
	/*%if dspSegmentSearchDto.endDate != null */ 
	and date <= /* dspSegmentSearchDto.endDate */'2018-10-12' 
	/*%end*/
GROUP BY
	segment_id, 
	date
ORDER BY   
	segment_id, 
	date