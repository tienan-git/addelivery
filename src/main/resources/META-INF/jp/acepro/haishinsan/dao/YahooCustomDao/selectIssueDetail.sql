select
  i.issue_id
  ,i.budget
  ,i.start_date
  ,i.end_date
  ,y.campaign_id
  ,y.campaign_name
  ,y.adv_destination
  ,y.device_type
  ,y.area
  ,y.url
  ,y.ad_short_title
  ,y.ad_title1
  ,y.ad_title2
  ,y.ad_description
  ,y.image_name
  ,y.yahoo_campaign_manage_id
from
  yahoo_campaign_manage y
inner join 
  issue i
on y.yahoo_campaign_manage_id = i.yahoo_campaign_manage_id
   and i.is_actived = 1

where
  y.is_actived = 1
  and i.issue_id = /* issueId */'123'