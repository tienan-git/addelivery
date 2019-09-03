select 
  i.*
from 
  issue i 
inner join shop s on i.shop_id = s.shop_id and s.is_actived = 1 
inner join corporation c on s.corporation_id = c.corporation_id and c.is_actived = 1 
where 
  i.is_actived = 1 
and i.facebook_campaign_id = /* campaignId */'612438768' 
and ((i.start_date <= /* startTime */'2019-08-15 15:30' 
    and i.end_date > /* startTime */'2019-08-15 15:30' )
  or (i.start_date > /* startTime */'2019-08-15 15:30' 
    and i.start_date < /* endTime */'2019-09-16 05:00' )) 
