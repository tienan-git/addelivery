SELECT
shop.shop_id,
shop.shop_name,
agency.agency_id,
agency.agency_name,
corporation.corporation_id,
corporation.corporation_name
FROM
    shop
	join corporation  on  shop.corporation_id=corporation.corporation_Id  
    join agency  on  corporation.agency_id=agency.agency_id    

WHERE
    shop.is_actived = 1
   
	
