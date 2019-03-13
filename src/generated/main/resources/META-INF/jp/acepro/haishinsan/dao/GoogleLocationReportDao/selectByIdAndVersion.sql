select
  /*%expand*/*
from
  google_location_report
where
  google_region_report_id = /* googleRegionReportId */1
  and
  version_no = /* versionNo */1
