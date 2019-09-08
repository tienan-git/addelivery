select 
   i.* 
from 
  issue i 
inner join facebook_campaign_manage t on i.facebook_campaign_id = t.campaign_id and t.is_actived = 1 
where 
  i.facebook_campaign_id is not null 
and i.start_date <= /*date*/'2019-08-27 06:00' 
and i.end_date >= /*date*/'2019-08-27 06:00' 
and i.approval_flag = 1 
and i.is_actived = 1 
and i.shop_id = /* shopId*/'1' 
