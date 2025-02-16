select
  i.twitter_campaign_id,
  i.campaign_name,
  t.daily_budget,
  t.total_budget,
  i.start_date,
  i.end_date,
  tl.tweet_id,
  tl.tweet_title,
  tl.tweet_body,
  tl.preview_url
from
  issue i
inner join twitter_campaign_manage t on i.twitter_campaign_id = t.campaign_id
and t.is_actived = 1
inner join twitter_tweet_list tl on t.campaign_id = tl.campaign_id
and tl.is_actived = 1
and tl.account_id = /* accountId*/'18ce54rova5'
where
  i.is_actived = 1 
  and i.issue_id = /* issueId */'1'
  and shop_id = /* shopId */'1' 