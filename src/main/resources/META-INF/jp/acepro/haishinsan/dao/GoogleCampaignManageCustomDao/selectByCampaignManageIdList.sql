select
  * 
from
  google_campaign_manage 
where
  is_actived = 1 
  and google_campaign_manage_id in /* campaignManageIdList */(1, 2)