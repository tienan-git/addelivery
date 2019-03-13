SELECT
	/*%expand*/*
FROM
	twitter_template
WHERE
	is_actived = 1
    and shop_id = /* shopId */'1' 
	and template_priority = /* templatePriority */'1'