select 
  facebook_campaign_manage.*  
from 
  facebook_campaign_manage 
inner join 
   shop 
on shop.shop_id = facebook_campaign_manage.shop_id 
   and shop.is_actived = 1 
inner join corporation 
on shop.corporation_id = corporation.corporation_id 
   and corporation.is_actived = 1 
where 
  facebook_campaign_manage.is_actived = 1 
