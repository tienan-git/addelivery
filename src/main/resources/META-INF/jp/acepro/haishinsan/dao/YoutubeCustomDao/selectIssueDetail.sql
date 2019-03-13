select
  i.issue_id
  ,i.budget
  ,i.start_date
  ,i.end_date
  ,y.campaign_id
  ,y.campaign_name
  ,y.ad_type
  ,y.lp
  ,y.video_url
  ,y.area
from
  youtube_campaign_manage y
inner join 
  issue i
on y.youtube_campaign_manage_id = i.youtube_campaign_manage_id
   and i.is_actived = 1

where
  y.is_actived = 1
  and i.issue_id = /* issueId */'4'