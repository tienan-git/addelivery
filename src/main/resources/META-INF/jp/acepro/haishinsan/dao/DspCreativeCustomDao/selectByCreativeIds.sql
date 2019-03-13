SELECT
	/*%expand*/*
FROM
	creative_manage
WHERE
	is_actived = 1
	/*%if creativeIdList != null && creativeIdList.size() !=0*/
	and creative_id in /*creativeIdList*/('688599','688600','688603')
	/*%end*/