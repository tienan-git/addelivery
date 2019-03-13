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
import jp.acepro.haishinsan.db.entity.Shop;

/**
 */
@Dao
@InjectConfig
public interface ShopDao {

    /**
     * @param shopId
     * @return the Shop entity
     */
    @Select
    Shop selectById(Long shopId);

    /**
     * @param shopId
     * @param versionNo
     * @return the Shop entity
     */
    @Select(ensureResult = true)
    Shop selectByIdAndVersion(Long shopId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(Shop entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(Shop entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(Shop entity);
    
    /**
    * @param entities the Shop
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<Shop> entities);

    /**
    * @param entities the Shop
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<Shop> entities);

    /**
    * @param entities the Shop
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<Shop> entities);
}