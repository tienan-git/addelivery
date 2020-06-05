select 
  i.*
from 
  issue i 
inner join shop s on i.shop_id = s.shop_id and s.is_actived = 1 
inner join corporation c on s.corporation_id = c.corporation_id and c.is_actived = 1 
where 
  i.is_actived = 1 
and i.approval_flag = '1' 
and i.facebook_campaign_id is not null 
and i.start_timestamp is null 
and i.start_date <= /* date */'2019-08-15' 
