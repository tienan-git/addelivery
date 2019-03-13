package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.TwitterTweetList;

@Dao
@InjectConfig
public interface TwitterTweetListCustomDao {

	@Select
	List<TwitterTweetList> selectByAccountId(String accountId);
	
	@Select
	TwitterTweetList selectByAccountIdAndTweetId(String accountId,String tweetId);
	
}
