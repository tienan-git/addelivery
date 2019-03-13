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
import jp.acepro.haishinsan.db.entity.DspCampaignManage;

/**
 */
@Dao
@InjectConfig
public interface DspCampaignManageDao {

    /**
     * @param dspCampaignManageId
     * @return the DspCampaignManage entity
     */
    @Select
    DspCampaignManage selectById(Long dspCampaignManageId);

    /**
     * @param dspCampaignManageId
     * @param versionNo
     * @return the DspCampaignManage entity
     */
    @Select(ensureResult = true)
    DspCampaignManage selectByIdAndVersion(Long dspCampaignManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(DspCampaignManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(DspCampaignManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(DspCampaignManage entity);
    
    /**
    * @param entities the DspCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<DspCampaignManage> entities);

    /**
    * @param entities the DspCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<DspCampaignManage> entities);

    /**
    * @param entities the DspCampaignManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<DspCampaignManage> entities);
}