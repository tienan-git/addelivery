SELECT
	/*%expand*/*
FROM
	shop
WHERE
	is_actived=1
	AND
	corporation_id in (
			SELECT
				corporation_id
			FROM
				corporation
			INNER JOIN agency
			ON agency.agency_id = corporation.agency_id
			WHERE
				agency.agency_id = (
						SELECT
							agency.agency_id
						FROM
							agency
							INNER JOIN corporation
							ON corporation.agency_id=agency.agency_id
							INNER JOIN shop
							ON corporation.corporation_id = shop.corporation_id
						WHERE
							shop.is_actived = 1
							and
							shop.shop_id = /* shopId */'3'))