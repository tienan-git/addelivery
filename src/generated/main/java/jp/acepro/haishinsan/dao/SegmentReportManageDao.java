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
import jp.acepro.haishinsan.db.entity.SegmentReportManage;

/**
 */
@Dao
@InjectConfig
public interface SegmentReportManageDao {

    /**
     * @param segmentReportManageId
     * @return the SegmentReportManage entity
     */
    @Select
    SegmentReportManage selectById(Long segmentReportManageId);

    /**
     * @param segmentReportManageId
     * @param versionNo
     * @return the SegmentReportManage entity
     */
    @Select(ensureResult = true)
    SegmentReportManage selectByIdAndVersion(Long segmentReportManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(SegmentReportManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(SegmentReportManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(SegmentReportManage entity);
    
    /**
    * @param entities the SegmentReportManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<SegmentReportManage> entities);

    /**
    * @param entities the SegmentReportManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<SegmentReportManage> entities);

    /**
    * @param entities the SegmentReportManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<SegmentReportManage> entities);
}