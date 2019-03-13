select
  dsp_campaign_manage.* 
from
  dsp_campaign_manage
inner join 
  issue 
on dsp_campaign_manage.dsp_campaign_manage_id = issue.dsp_campaign_manage_id
   and issue.is_actived = 1
inner join 
  shop 
on shop.shop_id = issue.shop_id
   and shop.is_actived = 1
where
  dsp_campaign_manage.is_actived = 1
  and shop.shop_id = /* shopId */'2'