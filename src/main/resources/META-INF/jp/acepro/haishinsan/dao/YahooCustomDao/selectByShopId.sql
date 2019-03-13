select
  yahoo_campaign_manage.* 
from
  yahoo_campaign_manage
inner join 
  issue 
on yahoo_campaign_manage.yahoo_campaign_manage_id = issue.yahoo_campaign_manage_id
   and issue.is_actived = 1
inner join 
   shop 
on shop.shop_id = issue.shop_id
   and shop.is_actived = 1
where
  yahoo_campaign_manage.is_actived = 1
  and shop.shop_id = /* shopId */'123'