select
  i.issue_id,
  i.shop_id,
  i.approval_flag,
  s.shop_name,
  c.corporation_id,
  c.corporation_name,
  i.campaign_name,
  i.dsp_campaign_id,
  i.facebook_campaign_id,
  i.instagram_campaign_id,
  i.twitter_campaign_id,
  i.google_campaign_id,
  i.yahoo_campaign_manage_id,
  i.youtube_campaign_manage_id,
  i.budget,
  i.start_date,
  i.end_date, 
  i.start_timestamp, 
  i.end_timestamp 
from
  issue i
inner join shop s on i.shop_id = s.shop_id and s.is_actived = 1
inner join corporation c on s.corporation_id = c.corporation_id and c.is_actived = 1
where
  i.is_actived = 1