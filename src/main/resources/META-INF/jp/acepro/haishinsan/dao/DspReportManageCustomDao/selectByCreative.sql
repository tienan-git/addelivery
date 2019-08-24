select
	campaign_id,
	campaign_name,
	creative_id,
	creative_name,
	SUM(impression_count) as impression_count, 
	SUM(click_count) as click_count, 
	SUM(price) as price
from
	dsp_report_manage
where
	is_actived = 1
	/*%if dspAdReportDto.campaignId != null  */ 
	and campaign_id = /* dspAdReportDto.campaignId */'2'
	/*%end*/
	/*%if dspAdReportDto.startDate != null && dspAdReportDto.startDate != ""*/ 
	and date >= /* dspAdReportDto.startDate */'2018-10-05' 
	/*%end*/ 
	/*%if dspAdReportDto.endDate != null && dspAdReportDto.endDate != ""*/ 
	and date <= /* dspAdReportDto.endDate */'2018-10-12' 
	/*%end*/
GROUP BY
	campaign_id,
	campaign_name,
	creative_id,
	creative_name
ORDER BY   
	campaign_id, 
	campaign_name, 
	creative_id,
	creative_name