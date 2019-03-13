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
import jp.acepro.haishinsan.db.entity.DspAdManage;

/**
 */
@Dao
@InjectConfig
public interface DspAdManageDao {

    /**
     * @param dspAdManageId
     * @return the DspAdManage entity
     */
    @Select
    DspAdManage selectById(Long dspAdManageId);

    /**
     * @param dspAdManageId
     * @param versionNo
     * @return the DspAdManage entity
     */
    @Select(ensureResult = true)
    DspAdManage selectByIdAndVersion(Long dspAdManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(DspAdManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(DspAdManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(DspAdManage entity);
    
    /**
    * @param entities the DspAdManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<DspAdManage> entities);

    /**
    * @param entities the DspAdManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<DspAdManage> entities);

    /**
    * @param entities the DspAdManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<DspAdManage> entities);
}