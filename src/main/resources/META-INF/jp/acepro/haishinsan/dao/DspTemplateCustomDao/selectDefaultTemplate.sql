SELECT
	/*%expand*/*
FROM
	haishinsan.dsp_template
WHERE
	is_actived = 1
	and shop_id = /* currentShopId */1
ORDER BY 
template_priority
