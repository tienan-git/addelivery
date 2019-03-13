select
  * 
from
  youtube_campaign_manage 
where
  is_actived = 1 
  and youtube_campaign_manage_id in /* campaignManageIdList */(1, 2)