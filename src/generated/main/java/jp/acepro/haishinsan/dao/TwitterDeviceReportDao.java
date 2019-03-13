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
import jp.acepro.haishinsan.db.entity.TwitterDeviceReport;

/**
 */
@Dao
@InjectConfig
public interface TwitterDeviceReportDao {

    /**
     * @param twitterDeviceReport
     * @return the TwitterDeviceReport entity
     */
    @Select
    TwitterDeviceReport selectById(Long twitterDeviceReport);

    /**
     * @param twitterDeviceReport
     * @param versionNo
     * @return the TwitterDeviceReport entity
     */
    @Select(ensureResult = true)
    TwitterDeviceReport selectByIdAndVersion(Long twitterDeviceReport, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(TwitterDeviceReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(TwitterDeviceReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(TwitterDeviceReport entity);
    
    /**
    * @param entities the TwitterDeviceReport
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<TwitterDeviceReport> entities);

    /**
    * @param entities the TwitterDeviceReport
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<TwitterDeviceReport> entities);

    /**
    * @param entities the TwitterDeviceReport
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<TwitterDeviceReport> entities);
}