select
  device_type
  , SUM(impressions) as impressions
  , SUM(clicks) as clicks
  , SUM(costs) as costs 
from
  google_device_report 
where
  is_actived = 1 
/*%if campaignIdList != null */
  and campaign_id in /* campaignIdList */('1595486772', '1595486773') 
/*%end*/
/*%if startDate != null && startDate != "" */
  and date >= /* startDate */'20181001' 
/*%end*/
/*%if endDate != null && endDate !="" */
  and date <= /* endDate */'20181031' 
/*%end*/
group by
  device_type 
order by
  device_type asc
