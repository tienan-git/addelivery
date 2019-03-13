select
  youtube_campaign_manage.* 
from
  youtube_campaign_manage
inner join 
  issue 
on youtube_campaign_manage.youtube_campaign_manage_id = issue.youtube_campaign_manage_id
   and issue.is_actived = 1
inner join 
   shop 
on shop.shop_id = issue.shop_id
   and shop.is_actived = 1
where
  youtube_campaign_manage.is_actived = 1
  and shop.shop_id = /* shopId */'123'