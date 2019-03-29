/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.OperationLog;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface OperationLogDao {

    /**
     * @param operationLogId
     * @return the OperationLog entity
     */
    @Select
    OperationLog selectById(Long operationLogId);

    /**
     * @param operationLogId
     * @param versionNo
     * @return the OperationLog entity
     */
    @Select(ensureResult = true)
    OperationLog selectByIdAndVersion(Long operationLogId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(OperationLog entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(OperationLog entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(OperationLog entity);
    
    /**
    * @param entities the OperationLog
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<OperationLog> entities);

    /**
    * @param entities the OperationLog
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<OperationLog> entities);

    /**
    * @param entities the OperationLog
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<OperationLog> entities);
}