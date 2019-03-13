SELECT
	/*%expand*/*
FROM
	shop
WHERE
	is_actived=1
	AND
	corporation_id in (
			SELECT
				corporation.corporation_id
			FROM
				corporation
			INNER JOIN shop
			ON shop.corporation_id = corporation.corporation_id
			where
				shop.shop_id= /* shopId */'3')