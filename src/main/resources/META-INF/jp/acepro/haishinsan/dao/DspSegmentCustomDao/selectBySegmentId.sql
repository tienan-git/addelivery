select
  /*%expand*/*
from
  segment_manage
where
  is_actived = 1
  and
  segment_id = /* segmentId */'1'