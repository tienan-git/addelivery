select
  /*%expand*/*
from
  yahoo_area
where
  area_id in /* locationIdList */(1,2) and
  is_actived = 1
