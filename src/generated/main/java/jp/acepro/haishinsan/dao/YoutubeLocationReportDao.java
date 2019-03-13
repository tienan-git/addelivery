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
import jp.acepro.haishinsan.db.entity.YoutubeLocationReport;

/**
 */
@Dao
@InjectConfig
public interface YoutubeLocationReportDao {

    /**
     * @param youtubeRegionReportId
     * @return the YoutubeLocationReport entity
     */
    @Select
    YoutubeLocationReport selectById(Long youtubeRegionReportId);

    /**
     * @param youtubeRegionReportId
     * @param versionNo
     * @return the YoutubeLocationReport entity
     */
    @Select(ensureResult = true)
    YoutubeLocationReport selectByIdAndVersion(Long youtubeRegionReportId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(YoutubeLocationReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(YoutubeLocationReport entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(YoutubeLocationReport entity);
    
    /**
    * @param entities the YoutubeLocationReport
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<YoutubeLocationReport> entities);

    /**
    * @param entities the YoutubeLocationReport
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<YoutubeLocationReport> entities);

    /**
    * @param entities the YoutubeLocationReport
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<YoutubeLocationReport> entities);
}