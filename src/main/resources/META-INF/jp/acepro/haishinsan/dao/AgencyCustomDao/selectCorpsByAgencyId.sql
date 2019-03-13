SELECT
	/*%expand*/*
FROM 
	corporation
WHERE
	agency_id = /* agencyId */'12345' and
	is_actived = 1
