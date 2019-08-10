select
  t.campaign_id
from
  issue i
left join twitter_campaign_manage t on i.twitter_campaign_manage_id = t.twitter_campaign_manage_id
and t.is_actived = 1
where
  i.is_actived = 1 
  and i.issue_id = /* issueId */'1'
  and shop_id = /* shopId */'1' 