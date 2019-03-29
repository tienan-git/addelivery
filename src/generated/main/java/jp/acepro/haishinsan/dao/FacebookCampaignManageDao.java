/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface FacebookCampaignManageDao {

    /**
     * @param facebookCampaignManageId
     * @return the FacebookCampaignManage entity
     */
    @Select
    FacebookCampaignManage selectById(Long facebookCampaignManageId);

    /**
     * @param facebookCampaignManageId
     * @param versionNo
     * @return the FacebookCampaignManage entity
     */
    @Select(ensureResult = true)
    FacebookCampaignManage selectByIdAndVersion(Long facebookCampaignManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(FacebookCampaignManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(FacebookCampaignManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(FacebookCampaignManage entity);
    
    /**
    * @param entities the FacebookCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<FacebookCampaignManage> entities);

    /**
    * @param entities the FacebookCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<FacebookCampaignManage> entities);

    /**
    * @param entities the FacebookCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<FacebookCampaignManage> entities);
}