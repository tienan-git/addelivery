SELECT
  device
  , SUM(impressions) as impressions
  , SUM(follows) as follows
  , SUM(url_clicks) as url_clicks
  , SUM(billed_charge_loacl_micro)/1000000 as billed_charge_loacl_micro 
FROM 
	twitter_device_report
WHERE
  is_actived = 1 
/*%if twitterReportDto.campaignIdList != null */ 
  and campaign_id in /* twitterReportDto.campaignIdList */('bhtuh','bkh7s')
/*%end*/ 
/*%if twitterReportDto.startDate != null && twitterReportDto.startDate != "" */
  and day >= /* twitterReportDto.startDate */'20181015' 
/*%end*/
/*%if twitterReportDto.endDate != null && twitterReportDto.endDate != "" */
  and day <= /* twitterReportDto.endDate */'20181016' 
/*%end*/
group by
device 
order by
device desc
