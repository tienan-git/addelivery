SELECT 
    campaign_id 
  , SUM(spend) as spend  
FROM 
  facebook_device_report 
WHERE 
  is_actived = 1 
  and campaign_id  = /* campaignId */'6986342834' 
  and date < /* date */'2019-08-07' 
  and date >= /* startDate */'2019-08-04' 
group by 
campaign_id 
