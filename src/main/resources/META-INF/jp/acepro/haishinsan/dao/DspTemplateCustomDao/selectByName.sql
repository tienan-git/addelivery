SELECT
	/*%expand*/*
FROM
	dsp_template
WHERE
	is_actived = 1
	and shop_id = /* shopId */1 
	and template_name = /* templateName */'testuser@sparkworks.co.jp'