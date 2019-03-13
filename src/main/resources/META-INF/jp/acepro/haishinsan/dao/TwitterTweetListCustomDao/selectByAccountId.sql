SELECT
	/*%expand*/*
FROM 
	twitter_tweet_list
WHERE
	is_actived = 1
	and
	 account_id = /* accountId */'1'