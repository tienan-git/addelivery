SELECT 
shop.* 
FROM 
    shop 
	inner join issue  on  shop.shop_id=issue.shop_id 
WHERE 
    issue.issue_id = /* issueId */'12345' and 
	shop.is_actived = 1 and 
	issue.is_actived = 1 
