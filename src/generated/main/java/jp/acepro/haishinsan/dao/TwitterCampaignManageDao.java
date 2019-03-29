/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.TwitterCampaignManage;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface TwitterCampaignManageDao {

    /**
     * @param twitterCampaignManageId
     * @return the TwitterCampaignManage entity
     */
    @Select
    TwitterCampaignManage selectById(Long twitterCampaignManageId);

    /**
     * @param twitterCampaignManageId
     * @param versionNo
     * @return the TwitterCampaignManage entity
     */
    @Select(ensureResult = true)
    TwitterCampaignManage selectByIdAndVersion(Long twitterCampaignManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(TwitterCampaignManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(TwitterCampaignManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(TwitterCampaignManage entity);
    
    /**
    * @param entities the TwitterCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<TwitterCampaignManage> entities);

    /**
    * @param entities the TwitterCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<TwitterCampaignManage> entities);

    /**
    * @param entities the TwitterCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<TwitterCampaignManage> entities);
}