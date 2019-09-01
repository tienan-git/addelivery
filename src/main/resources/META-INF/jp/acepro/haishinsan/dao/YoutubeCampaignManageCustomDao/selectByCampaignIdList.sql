select
  * 
from
  youtube_campaign_manage 
where
  is_actived = 1 
  and campaign_id in /* campaignIdList */(1, 2)