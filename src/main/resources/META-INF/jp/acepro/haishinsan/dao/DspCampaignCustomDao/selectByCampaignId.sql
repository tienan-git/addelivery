select
  /*%expand*/*
from
  dsp_campaign_manage
where
  is_actived = 1
  and
  campaign_id = /* campaignId */'1'