select
  i.issue_id,
  i.shop_id,
  s.shop_name,
  c.corporation_id,
  c.corporation_name,
  i.campaign_name,
  i.dsp_campaign_manage_id,
  i.facebook_campaign_manage_id,
  i.twitter_campaign_manage_id,
  i.google_campaign_manage_id,
  i.yahoo_campaign_manage_id,
  i.youtube_campaign_manage_id,
  i.budget,
  i.start_date,
  i.end_date
from
  issue i
inner join shop s on i.shop_id = s.shop_id and s.is_actived = 1
inner join corporation c on s.corporation_id = c.corporation_id and c.is_actived = 1
where
  i.is_actived = 1
and i.shop_id = /* shopId */'1' 
/*%if issueSearchDto.shopName != null && issueSearchDto.shopName != "" */
and s.shop_name like  /* @infix(issueSearchDto.shopName) */'秋葉原店舗' escape '$' 
/*%end*/
/*%if issueSearchDto.campaignName != null && issueSearchDto.campaignName != "" */
and i.campaign_name like  /* @infix(issueSearchDto.campaignName) */'Twitterキャンペーン2019-08-04' escape '$' 
/*%end*/
/*%if issueSearchDto.media != null*/
/*%end*/
/*%if issueSearchDto.startDate != null*/
and i.start_date = /* issueSearchDto.startDate */'2019-08-05'
/*%end*/