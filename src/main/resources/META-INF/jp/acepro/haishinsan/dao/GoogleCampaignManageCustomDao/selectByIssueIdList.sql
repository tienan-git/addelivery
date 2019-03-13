select
  * 
from
  google_campaign_manage 
where
  is_actived = 1 
  and issue_id in /* issueIdList */('1', '2', '3') 
order by
  campaign_id desc