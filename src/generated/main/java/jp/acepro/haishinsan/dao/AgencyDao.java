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
import jp.acepro.haishinsan.db.entity.Agency;

/**
 */
@Dao
@InjectConfig
public interface AgencyDao {

    /**
     * @param agencyId
     * @return the Agency entity
     */
    @Select
    Agency selectById(Long agencyId);

    /**
     * @param agencyId
     * @param versionNo
     * @return the Agency entity
     */
    @Select(ensureResult = true)
    Agency selectByIdAndVersion(Long agencyId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(Agency entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(Agency entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(Agency entity);
    
    /**
    * @param entities the Agency
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<Agency> entities);

    /**
    * @param entities the Agency
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<Agency> entities);

    /**
    * @param entities the Agency
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<Agency> entities);
}