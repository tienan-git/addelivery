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
import jp.acepro.haishinsan.db.entity.GoogleLocationReport;

/**
 */
@Dao
@InjectConfig
public interface GoogleLocationReportDao {

    /**
     * @param googleRegionReportId
     * @return the GoogleLocationReport entity
     */
    @Select
    GoogleLocationReport selectById(Long googleRegionReportId);

    /**
     * @param googleRegionReportId
     * @param versionNo
     * @return the GoogleLocationReport entity
     */
    @Select(ensureResult = true)
    GoogleLocationReport selectByIdAndVersion(Long googleRegionReportId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(GoogleLocationReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(GoogleLocationReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(GoogleLocationReport entity);
    
    /**
    * @param entities the GoogleLocationReport
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<GoogleLocationReport> entities);

    /**
    * @param entities the GoogleLocationReport
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<GoogleLocationReport> entities);

    /**
    * @param entities the GoogleLocationReport
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<GoogleLocationReport> entities);
}