/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.Authority;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface AuthorityDao {

    /**
     * @param authorityId
     * @return the Authority entity
     */
    @Select
    Authority selectById(Long authorityId);

    /**
     * @param authorityId
     * @param versionNo
     * @return the Authority entity
     */
    @Select(ensureResult = true)
    Authority selectByIdAndVersion(Long authorityId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(Authority entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(Authority entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(Authority entity);
    
    /**
    * @param entities the Authority
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<Authority> entities);

    /**
    * @param entities the Authority
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<Authority> entities);

    /**
    * @param entities the Authority
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<Authority> entities);
}