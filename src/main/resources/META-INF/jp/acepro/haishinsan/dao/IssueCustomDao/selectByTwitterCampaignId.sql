select
  * 
from
  issue 
where
  is_actived = 1 
  and shop_id = /* shopId */'1' 
  and twitter_campaign_id  =  /* campaignId */'cv0bs' 
order by
  issue_id desc
