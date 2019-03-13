SELECT
	u.user_id,
	u.user_name,
	u.email,
	u.password,
	u.role_id,
	s.shop_id,
	s.shop_name,
	c.corporation_id,
	c.corporation_name,
	a.agency_id,
	a.agency_name
FROM 
	user u
	LEFT JOIN shop s on u.shop_id = s.shop_id
	LEFT JOIN corporation c on s.corporation_id = c.corporation_id
	LEFT JOIN agency a on a.agency_id = c.agency_id
WHERE
	u.is_actived = 1
