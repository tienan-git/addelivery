SELECT
	*
 FROM 
	segment_report_manage
WHERE 
	is_actived = 1
	and
	/*%if segmentIds != null */ 
	segment_id in /* segmentIds */('1')
	/*%end*/ 
	/*%if startDate != null */ 
	and date >= /* startDate */'2018-10-05' 
	/*%end*/ 
    /*%if endDate != null */ 
	and date <= /* endDate */'2018-10-12' 
	/*%end*/
