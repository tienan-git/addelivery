SELECT
	c.corporation_id,
	c.corporation_name,
	a.agency_id,
	a.agency_name
FROM 
	corporation c LEFT JOIN agency a on c.agency_id = a.agency_id
WHERE
	c.corporation_id = /* corporationId */'12345' and
	c.is_actived = 1
