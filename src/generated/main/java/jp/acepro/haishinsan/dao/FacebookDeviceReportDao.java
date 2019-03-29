/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.FacebookDeviceReport;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface FacebookDeviceReportDao {

    /**
     * @param facebookDeviceReportId
     * @return the FacebookDeviceReport entity
     */
    @Select
    FacebookDeviceReport selectById(Long facebookDeviceReportId);

    /**
     * @param facebookDeviceReportId
     * @param versionNo
     * @return the FacebookDeviceReport entity
     */
    @Select(ensureResult = true)
    FacebookDeviceReport selectByIdAndVersion(Long facebookDeviceReportId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(FacebookDeviceReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(FacebookDeviceReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(FacebookDeviceReport entity);
    
    /**
    * @param entities the FacebookDeviceReport
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<FacebookDeviceReport> entities);

    /**
    * @param entities the FacebookDeviceReport
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<FacebookDeviceReport> entities);

    /**
    * @param entities the FacebookDeviceReport
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<FacebookDeviceReport> entities);
}