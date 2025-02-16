select
  location_id
  , SUM(impressions) as impressions
  , SUM(clicks) as clicks
  , SUM(costs) as costs 
from
  google_location_report 
where
  is_actived = 1 
/*%if campaignIdList != null */
  and campaign_id in /* campaignIdList */('1596138914', '1596142034') 
/*%end*/
/*%if startDate != null && startDate != "" */
  and date >= /* startDate */'20181001' 
/*%end*/
/*%if endDate != null && endDate !="" */
  and date <= /* endDate */'20181031' 
/*%end*/
group by
  location_id 
order by
  location_id asc
