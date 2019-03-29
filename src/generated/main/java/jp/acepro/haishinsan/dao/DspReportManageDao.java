/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.DspReportManage;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface DspReportManageDao {

    /**
     * @param dspReportManageId
     * @return the DspReportManage entity
     */
    @Select
    DspReportManage selectById(Long dspReportManageId);

    /**
     * @param dspReportManageId
     * @param versionNo
     * @return the DspReportManage entity
     */
    @Select(ensureResult = true)
    DspReportManage selectByIdAndVersion(Long dspReportManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(DspReportManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(DspReportManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(DspReportManage entity);
    
    /**
    * @param entities the DspReportManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<DspReportManage> entities);

    /**
    * @param entities the DspReportManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<DspReportManage> entities);

    /**
    * @param entities the DspReportManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<DspReportManage> entities);
}