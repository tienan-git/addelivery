SELECT
	/*%expand*/*
FROM
	dsp_report_manage
WHERE
	is_actived = 1
	/*%if campaignIds != null && campaignIds.size() !=0*/
	and campaign_id in /*campaignIds*/('688599','688600','688603')
	/*%end*/