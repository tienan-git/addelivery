SELECT
	/*%expand*/*
FROM 
    facebook_template
WHERE
	is_actived = 1
	and shop_id = /* shopId */'1' 
order by
    template_priority

