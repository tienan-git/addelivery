/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.Corporation;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface CorporationDao {

    /**
     * @param corporationId
     * @return the Corporation entity
     */
    @Select
    Corporation selectById(Long corporationId);

    /**
     * @param corporationId
     * @param versionNo
     * @return the Corporation entity
     */
    @Select(ensureResult = true)
    Corporation selectByIdAndVersion(Long corporationId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(Corporation entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(Corporation entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(Corporation entity);
    
    /**
    * @param entities the Corporation
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<Corporation> entities);

    /**
    * @param entities the Corporation
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<Corporation> entities);

    /**
    * @param entities the Corporation
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<Corporation> entities);
}