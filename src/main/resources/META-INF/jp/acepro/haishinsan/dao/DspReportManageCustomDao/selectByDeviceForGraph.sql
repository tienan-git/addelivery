select
	device_type,
	SUM(impression_count) as impression_count, 
	SUM(click_count) as click_count, 
	SUM(price) as price
from
	dsp_report_manage
where
	is_actived = 1
	/*%if dspAdReportDto.campaignIdList != null && dspAdReportDto.campaignIdList.size()!=0 */ 
	and campaign_id in /* dspAdReportDto.campaignIdList */('1','2') 
	/*%end*/
	/*%if dspAdReportDto.startDate != null && dspAdReportDto.startDate != ""*/ 
	and date >= /* dspAdReportDto.startDate */'2018-10-05' 
	/*%end*/ 
	/*%if dspAdReportDto.endDate != null && dspAdReportDto.endDate != ""*/ 
	and date <= /* dspAdReportDto.endDate */'2018-10-12' 
	/*%end*/
GROUP BY
	device_type
ORDER BY   
	device_type