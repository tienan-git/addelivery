/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.FacebookAreaPrice;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface FacebookAreaPriceDao {

    /**
     * @param facebookAreaPriceId
     * @return the FacebookAreaPrice entity
     */
    @Select
    FacebookAreaPrice selectById(Long facebookAreaPriceId);

    /**
     * @param facebookAreaPriceId
     * @param versionNo
     * @return the FacebookAreaPrice entity
     */
    @Select(ensureResult = true)
    FacebookAreaPrice selectByIdAndVersion(Long facebookAreaPriceId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(FacebookAreaPrice entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(FacebookAreaPrice entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(FacebookAreaPrice entity);
    
    /**
    * @param entities the FacebookAreaPrice
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<FacebookAreaPrice> entities);

    /**
    * @param entities the FacebookAreaPrice
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<FacebookAreaPrice> entities);

    /**
    * @param entities the FacebookAreaPrice
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<FacebookAreaPrice> entities);
}