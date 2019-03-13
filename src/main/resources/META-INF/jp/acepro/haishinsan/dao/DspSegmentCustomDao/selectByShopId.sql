select
  /*%expand*/*
from
  segment_manage
where
  is_actived = 1
  and
  url <> ""
  and
  shop_id = /* shopId */'1'