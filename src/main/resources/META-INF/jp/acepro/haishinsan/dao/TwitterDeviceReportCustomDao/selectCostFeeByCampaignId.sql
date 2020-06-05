SELECT
    campaign_id
  , SUM(billed_charge_loacl_micro)/1000000 as billed_charge_loacl_micro 
FROM 
	twitter_device_report
WHERE
  is_actived = 1 
  and campaign_id  = /* campaignId */'cv0bs'
  and day < /* date */'20190807' 
group by
campaign_id
and
day

