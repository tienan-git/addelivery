select
	s.segment_manage_id,
    s.segment_id,
    s.shop_id,
    s.segment_name,
    s.url
from
	segment_manage s
where
	is_actived = 1
	/*%if dateTime != null */ 
	and s.created_at <= /* dateTime */'2020-12-03T10:15:30' 
	/*%end*/
    and s.shop_id = /* shopId */1
GROUP BY
	s.segment_manage_id
ORDER BY  
	s.segment_manage_id