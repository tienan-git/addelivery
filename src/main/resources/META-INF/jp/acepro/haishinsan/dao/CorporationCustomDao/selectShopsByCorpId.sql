SELECT
	/*%expand*/*
FROM 
	shop
WHERE
	corporation_id = /* corporationId */'12345' and
	is_actived = 1
