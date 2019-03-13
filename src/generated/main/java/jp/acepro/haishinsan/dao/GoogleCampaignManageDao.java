/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.GoogleCampaignManage;

/**
 */
@Dao
@InjectConfig
public interface GoogleCampaignManageDao {

    /**
     * @param googleCampaignManageId
     * @return the GoogleCampaignManage entity
     */
    @Select
    GoogleCampaignManage selectById(Long googleCampaignManageId);

    /**
     * @param googleCampaignManageId
     * @param versionNo
     * @return the GoogleCampaignManage entity
     */
    @Select(ensureResult = true)
    GoogleCampaignManage selectByIdAndVersion(Long googleCampaignManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(GoogleCampaignManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(GoogleCampaignManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(GoogleCampaignManage entity);
    
    /**
    * @param entities the GoogleCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<GoogleCampaignManage> entities);

    /**
    * @param entities the GoogleCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<GoogleCampaignManage> entities);

    /**
    * @param entities the GoogleCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<GoogleCampaignManage> entities);
}