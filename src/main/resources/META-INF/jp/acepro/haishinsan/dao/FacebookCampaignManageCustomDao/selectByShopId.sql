select
  facebook_campaign_manage.* 
from
  facebook_campaign_manage
inner join 
  issue 
on facebook_campaign_manage.facebook_campaign_manage_id = issue.facebook_campaign_manage_id
   and issue.is_actived = 1
inner join 
   shop 
on shop.shop_id = issue.shop_id
   and shop.is_actived = 1
where
  facebook_campaign_manage.is_actived = 1
  and shop.shop_id = /* shopId */'123'

