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
import jp.acepro.haishinsan.db.entity.YahooCampaignManage;

/**
 */
@Dao
@InjectConfig
public interface YahooCampaignManageDao {

    /**
     * @param yahooCampaignManageId
     * @return the YahooCampaignManage entity
     */
    @Select
    YahooCampaignManage selectById(Long yahooCampaignManageId);

    /**
     * @param yahooCampaignManageId
     * @param versionNo
     * @return the YahooCampaignManage entity
     */
    @Select(ensureResult = true)
    YahooCampaignManage selectByIdAndVersion(Long yahooCampaignManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(YahooCampaignManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(YahooCampaignManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(YahooCampaignManage entity);
    
    /**
    * @param entities the YahooCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<YahooCampaignManage> entities);

    /**
    * @param entities the YahooCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<YahooCampaignManage> entities);

    /**
    * @param entities the YahooCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<YahooCampaignManage> entities);
}