/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.YahooArea;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface YahooAreaDao {

    /**
     * @param yahooAreaId
     * @return the YahooArea entity
     */
    @Select
    YahooArea selectById(Long yahooAreaId);

    /**
     * @param yahooAreaId
     * @param versionNo
     * @return the YahooArea entity
     */
    @Select(ensureResult = true)
    YahooArea selectByIdAndVersion(Long yahooAreaId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(YahooArea entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(YahooArea entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(YahooArea entity);
    
    /**
    * @param entities the YahooArea
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<YahooArea> entities);

    /**
    * @param entities the YahooArea
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<YahooArea> entities);

    /**
    * @param entities the YahooArea
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<YahooArea> entities);
}