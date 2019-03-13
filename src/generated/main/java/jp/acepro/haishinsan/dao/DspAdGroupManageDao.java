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
import jp.acepro.haishinsan.db.entity.DspAdGroupManage;

/**
 */
@Dao
@InjectConfig
public interface DspAdGroupManageDao {

    /**
     * @param dspAdGroupManageId
     * @return the DspAdGroupManage entity
     */
    @Select
    DspAdGroupManage selectById(Long dspAdGroupManageId);

    /**
     * @param dspAdGroupManageId
     * @param versionNo
     * @return the DspAdGroupManage entity
     */
    @Select(ensureResult = true)
    DspAdGroupManage selectByIdAndVersion(Long dspAdGroupManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(DspAdGroupManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(DspAdGroupManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(DspAdGroupManage entity);
    
    /**
    * @param entities the DspAdGroupManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<DspAdGroupManage> entities);

    /**
    * @param entities the DspAdGroupManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<DspAdGroupManage> entities);

    /**
    * @param entities the DspAdGroupManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<DspAdGroupManage> entities);
}