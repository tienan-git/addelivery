select
  /*%expand*/*
from
  user
where
  user_id = /* userId */1
  and
  version_no = /* versionNo */1
