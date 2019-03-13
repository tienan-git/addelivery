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
import jp.acepro.haishinsan.db.entity.SegmentManage;

/**
 */
@Dao
@InjectConfig
public interface SegmentManageDao {

    /**
     * @param segmentManageId
     * @return the SegmentManage entity
     */
    @Select
    SegmentManage selectById(Long segmentManageId);

    /**
     * @param segmentManageId
     * @param versionNo
     * @return the SegmentManage entity
     */
    @Select(ensureResult = true)
    SegmentManage selectByIdAndVersion(Long segmentManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(SegmentManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(SegmentManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(SegmentManage entity);
    
    /**
    * @param entities the SegmentManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<SegmentManage> entities);

    /**
    * @param entities the SegmentManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<SegmentManage> entities);

    /**
    * @param entities the SegmentManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<SegmentManage> entities);
}