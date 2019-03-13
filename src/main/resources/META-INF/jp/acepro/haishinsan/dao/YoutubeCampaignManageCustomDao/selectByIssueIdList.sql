select
  * 
from
  youtube_campaign_manage 
where
  is_actived = 1 
  and campaign_id is not null 
  and campaign_id != "" 
  and issue_id in /* issueIdList */('1', '2', '3') 
order by
  campaign_id desc