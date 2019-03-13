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
import jp.acepro.haishinsan.db.entity.TwitterRegionReport;

/**
 */
@Dao
@InjectConfig
public interface TwitterRegionReportDao {

    /**
     * @param twitterRegionReport
     * @return the TwitterRegionReport entity
     */
    @Select
    TwitterRegionReport selectById(Long twitterRegionReport);

    /**
     * @param twitterRegionReport
     * @param versionNo
     * @return the TwitterRegionReport entity
     */
    @Select(ensureResult = true)
    TwitterRegionReport selectByIdAndVersion(Long twitterRegionReport, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(TwitterRegionReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(TwitterRegionReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(TwitterRegionReport entity);
    
    /**
    * @param entities the TwitterRegionReport
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<TwitterRegionReport> entities);

    /**
    * @param entities the TwitterRegionReport
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<TwitterRegionReport> entities);

    /**
    * @param entities the TwitterRegionReport
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<TwitterRegionReport> entities);
}