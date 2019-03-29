/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.YoutubeDeviceReport;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface YoutubeDeviceReportDao {

    /**
     * @param youtubeDeviceReportId
     * @return the YoutubeDeviceReport entity
     */
    @Select
    YoutubeDeviceReport selectById(Long youtubeDeviceReportId);

    /**
     * @param youtubeDeviceReportId
     * @param versionNo
     * @return the YoutubeDeviceReport entity
     */
    @Select(ensureResult = true)
    YoutubeDeviceReport selectByIdAndVersion(Long youtubeDeviceReportId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(YoutubeDeviceReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(YoutubeDeviceReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(YoutubeDeviceReport entity);
    
    /**
    * @param entities the YoutubeDeviceReport
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<YoutubeDeviceReport> entities);

    /**
    * @param entities the YoutubeDeviceReport
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<YoutubeDeviceReport> entities);

    /**
    * @param entities the YoutubeDeviceReport
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<YoutubeDeviceReport> entities);
}