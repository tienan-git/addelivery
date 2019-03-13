select
  /*%expand*/*
from
  operation_log
where
  operation_log_id = /* operationLogId */1
  and
  version_no = /* versionNo */1
