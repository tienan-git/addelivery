select
  /*%expand*/*
from
  flyway_schema_history
where
  installed_rank = /* installedRank */1
