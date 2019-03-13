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
import jp.acepro.haishinsan.db.entity.Regions;

/**
 */
@Dao
@InjectConfig
public interface RegionsDao {

    /**
     * @param regionsId
     * @return the Regions entity
     */
    @Select
    Regions selectById(Long regionsId);

    /**
     * @param regionsId
     * @param versionNo
     * @return the Regions entity
     */
    @Select(ensureResult = true)
    Regions selectByIdAndVersion(Long regionsId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(Regions entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(Regions entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(Regions entity);
    
    /**
    * @param entities the Regions
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<Regions> entities);

    /**
    * @param entities the Regions
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<Regions> entities);

    /**
    * @param entities the Regions
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<Regions> entities);
}