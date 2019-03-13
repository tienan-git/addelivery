select
  campaign_id, 
  campaign_name, 
  device, 
  SUM(impressions) as impressions, 
  SUM(clicks) as clicks, 
  SUM(spend) as spend 
from
  facebook_device_report 
where
  is_actived = 1 
  /*%if campaignIdList != null */ 
  and campaign_id in /* campaignIdList */('1','2') 
  /*%end*/ 
  /*%if startDate != null */ 
  and date >= /* startDate */'2018-10-05' 
  /*%end*/ 
  /*%if endDate != null */ 
  and date <= /* endDate */'2018-10-12' 
  /*%end*/ 
  GROUP BY
  campaign_id, 
  campaign_name, 
  device 
ORDER BY   
  campaign_id, 
  campaign_name, 
  device 
  