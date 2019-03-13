select
  facebook_campaign_manage.* 
from
  facebook_campaign_manage
where
  facebook_campaign_manage.is_actived = 1
  and facebook_campaign_manage.campaign_id = /* campaignId */'123'

