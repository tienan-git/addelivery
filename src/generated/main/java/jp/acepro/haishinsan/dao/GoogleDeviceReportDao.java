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
import jp.acepro.haishinsan.db.entity.GoogleDeviceReport;

/**
 */
@Dao
@InjectConfig
public interface GoogleDeviceReportDao {

    /**
     * @param googleDeviceReportId
     * @return the GoogleDeviceReport entity
     */
    @Select
    GoogleDeviceReport selectById(Long googleDeviceReportId);

    /**
     * @param googleDeviceReportId
     * @param versionNo
     * @return the GoogleDeviceReport entity
     */
    @Select(ensureResult = true)
    GoogleDeviceReport selectByIdAndVersion(Long googleDeviceReportId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(GoogleDeviceReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(GoogleDeviceReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(GoogleDeviceReport entity);
    
    /**
    * @param entities the GoogleDeviceReport
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<GoogleDeviceReport> entities);

    /**
    * @param entities the GoogleDeviceReport
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<GoogleDeviceReport> entities);

    /**
    * @param entities the GoogleDeviceReport
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<GoogleDeviceReport> entities);
}