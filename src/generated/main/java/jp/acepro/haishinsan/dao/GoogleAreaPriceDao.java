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
import jp.acepro.haishinsan.db.entity.GoogleAreaPrice;

/**
 */
@Dao
@InjectConfig
public interface GoogleAreaPriceDao {

    /**
     * @param googleAreaPriceId
     * @return the GoogleAreaPrice entity
     */
    @Select
    GoogleAreaPrice selectById(Long googleAreaPriceId);

    /**
     * @param googleAreaPriceId
     * @param versionNo
     * @return the GoogleAreaPrice entity
     */
    @Select(ensureResult = true)
    GoogleAreaPrice selectByIdAndVersion(Long googleAreaPriceId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(GoogleAreaPrice entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(GoogleAreaPrice entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(GoogleAreaPrice entity);
    
    /**
    * @param entities the GoogleAreaPrice
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<GoogleAreaPrice> entities);

    /**
    * @param entities the GoogleAreaPrice
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<GoogleAreaPrice> entities);

    /**
    * @param entities the GoogleAreaPrice
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<GoogleAreaPrice> entities);
}