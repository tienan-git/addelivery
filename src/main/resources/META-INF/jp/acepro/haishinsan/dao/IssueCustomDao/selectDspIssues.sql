SELECT
	*
FROM
	issue i
WHERE
	i.is_actived = 1
    AND
    i.dsp_campaign_id is not null
    AND
    i.start_date <= /* now */'2019-08-18 12:00'
    AND
    i.end_date >= /* now */'2019-08-18 12:00'