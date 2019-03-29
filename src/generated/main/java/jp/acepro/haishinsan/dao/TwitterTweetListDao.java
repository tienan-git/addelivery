/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.TwitterTweetList;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface TwitterTweetListDao {

    /**
     * @param twitterTweetListId
     * @return the TwitterTweetList entity
     */
    @Select
    TwitterTweetList selectById(Long twitterTweetListId);

    /**
     * @param twitterTweetListId
     * @param versionNo
     * @return the TwitterTweetList entity
     */
    @Select(ensureResult = true)
    TwitterTweetList selectByIdAndVersion(Long twitterTweetListId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(TwitterTweetList entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(TwitterTweetList entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(TwitterTweetList entity);
    
    /**
    * @param entities the TwitterTweetList
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<TwitterTweetList> entities);

    /**
    * @param entities the TwitterTweetList
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<TwitterTweetList> entities);

    /**
    * @param entities the TwitterTweetList
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<TwitterTweetList> entities);
}