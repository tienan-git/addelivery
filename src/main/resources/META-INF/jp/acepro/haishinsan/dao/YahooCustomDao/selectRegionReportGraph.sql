select
  region_name, 
  SUM(impression_count) as impression_count, 
  SUM(click_count) as click_count, 
  SUM(cost) as cost 
from
  yahoo_report_manage 
where
  is_actived = 1 
  /*%if campaignIdList != null */ 
  and campaign_id in /* campaignIdList */('1','2') 
  /*%end*/ 
  /*%if startDate != null */ 
  and create_date >= /* startDate */'2018-10-05' 
  /*%end*/ 
  /*%if endDate != null */ 
  and create_date <= /* endDate */'2018-10-12' 
  /*%end*/ 
  GROUP BY
  region_name 
ORDER BY   
  region_name 
  