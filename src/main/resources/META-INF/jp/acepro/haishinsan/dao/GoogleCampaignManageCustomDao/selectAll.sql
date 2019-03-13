select
  google_campaign_manage.* 
from
  google_campaign_manage
inner join 
  issue 
on google_campaign_manage.google_campaign_manage_id = issue.google_campaign_manage_id
   and issue.is_actived = 1
where
   google_campaign_manage.is_actived = 1
