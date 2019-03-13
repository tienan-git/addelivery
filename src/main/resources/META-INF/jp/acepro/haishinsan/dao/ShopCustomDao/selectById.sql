SELECT
agency.agency_id,
agency.agency_name,
shop.shop_id,
shop.shop_name,
shop.dsp_user_id,
shop.google_account_id,
shop.facebook_page_id,
shop.twitter_account_id,
shop.dsp_distribution_ratio,
shop.google_distribution_ratio,
shop.facebook_distribution_ratio,
shop.twitter_distribution_ratio,
shop.sales_check_flag,
shop.margin_ratio,
shop.shop_mail_list,
shop.sales_mail_list,
corporation.corporation_id,
corporation.corporation_name

FROM 
           shop
			join corporation  on  shop.corporation_id=corporation.corporation_id
			join agency on corporation.agency_id=agency.agency_id
WHERE
   shop.shop_id = /* shopId */'12345' and
	shop.is_actived = 1
