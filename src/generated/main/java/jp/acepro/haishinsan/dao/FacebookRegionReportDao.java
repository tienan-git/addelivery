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
import jp.acepro.haishinsan.db.entity.FacebookRegionReport;

/**
 */
@Dao
@InjectConfig
public interface FacebookRegionReportDao {

    /**
     * @param facebookRegionReportId
     * @return the FacebookRegionReport entity
     */
    @Select
    FacebookRegionReport selectById(Long facebookRegionReportId);

    /**
     * @param facebookRegionReportId
     * @param versionNo
     * @return the FacebookRegionReport entity
     */
    @Select(ensureResult = true)
    FacebookRegionReport selectByIdAndVersion(Long facebookRegionReportId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(FacebookRegionReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(FacebookRegionReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(FacebookRegionReport entity);
    
    /**
    * @param entities the FacebookRegionReport
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<FacebookRegionReport> entities);

    /**
    * @param entities the FacebookRegionReport
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<FacebookRegionReport> entities);

    /**
    * @param entities the FacebookRegionReport
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<FacebookRegionReport> entities);
}