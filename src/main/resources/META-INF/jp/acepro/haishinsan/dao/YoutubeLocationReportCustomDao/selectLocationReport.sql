select
  campaign_id
  , campaign_name
  , location_id
  , SUM(impressions) as impressions
  , SUM(clicks) as clicks
  , SUM(costs) as costs 
  , SUM(video_views) as video_views 
from
  youtube_location_report 
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
  campaign_id
  , campaign_name
  , location_id 
order by
  campaign_id desc
  , campaign_name
  , location_id asc
