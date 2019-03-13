SELECT
shop.shop_id,
shop.shop_name,
user.user_id,
user.user_name,
user.email,
user.role_id
FROM 
user
join shop  on  shop.shop_id=user.shop_Id  
WHERE
shop.shop_id = /* shopId */'12345' and
shop.is_actived = 1
