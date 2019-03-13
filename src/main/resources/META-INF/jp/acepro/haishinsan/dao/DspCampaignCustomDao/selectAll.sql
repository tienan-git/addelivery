select
  dsp_campaign_manage.* 
from
  dsp_campaign_manage
inner join 
  issue 
on dsp_campaign_manage.dsp_campaign_manage_id = issue.dsp_campaign_manage_id
   and issue.is_actived = 1
where
  dsp_campaign_manage.is_actived = 1
