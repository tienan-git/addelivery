select
  y.youtube_campaign_manage_id,
  y.campaign_id,
  y.campaign_name,
  y.ad_type,
  y.budget,
  y.start_date,
  y.end_date,
  y.area,
  y.lp,
  y.video_url,
  y.version_no,
  y.created_at,
  y.updated_at,
  y.updated_by,
  y.is_actived
from
  youtube_campaign_manage y,
  issue i
where
  y.is_actived = 1 
  and y.campaign_id is not null 
  and y.campaign_id != "" 
  and i.issue_id in /* issueIdList */('1', '2', '3') 
order by
  y.campaign_id desc