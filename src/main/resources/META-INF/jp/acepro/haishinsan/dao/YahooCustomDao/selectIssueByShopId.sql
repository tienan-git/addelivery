select
  issue_id
  ,campaign_name
  ,budget
  ,start_date
  ,end_date
from
  issue
inner join 
   shop
on issue.shop_id = shop.shop_id
   and shop.is_actived = 1
where
  issue.is_actived = 1
  and issue.yahoo_campaign_manage_id is not null
  and shop.shop_id = /* shopId */'123'