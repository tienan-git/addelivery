SELECT
	/*%expand*/*
FROM 
	twitter_region_report
WHERE
	is_actived = 1
	and
	 day = /* day */'2018-09-01'
     and
     campaign_id = /* campaignId */'bhtuh'