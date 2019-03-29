/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.YoutubeCampaignManage;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface YoutubeCampaignManageDao {

    /**
     * @param youtubeCampaignManageId
     * @return the YoutubeCampaignManage entity
     */
    @Select
    YoutubeCampaignManage selectById(Long youtubeCampaignManageId);

    /**
     * @param youtubeCampaignManageId
     * @param versionNo
     * @return the YoutubeCampaignManage entity
     */
    @Select(ensureResult = true)
    YoutubeCampaignManage selectByIdAndVersion(Long youtubeCampaignManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(YoutubeCampaignManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(YoutubeCampaignManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(YoutubeCampaignManage entity);
    
    /**
    * @param entities the YoutubeCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<YoutubeCampaignManage> entities);

    /**
    * @param entities the YoutubeCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<YoutubeCampaignManage> entities);

    /**
    * @param entities the YoutubeCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<YoutubeCampaignManage> entities);
}