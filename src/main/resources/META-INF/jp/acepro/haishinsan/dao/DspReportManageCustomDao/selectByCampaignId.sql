SELECT
	*
FROM
	dsp_report_manage d
WHERE
	d.is_actived = 1
    AND
    d.campaign_id = /* dspCampaignId */96755
    AND
    date < /* now */'2019-08-18'