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
import jp.acepro.haishinsan.db.entity.YahooReportManage;

/**
 */
@Dao
@InjectConfig
public interface YahooReportManageDao {

    /**
     * @param yahooReportManageId
     * @return the YahooReportManage entity
     */
    @Select
    YahooReportManage selectById(Long yahooReportManageId);

    /**
     * @param yahooReportManageId
     * @param versionNo
     * @return the YahooReportManage entity
     */
    @Select(ensureResult = true)
    YahooReportManage selectByIdAndVersion(Long yahooReportManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(YahooReportManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(YahooReportManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(YahooReportManage entity);
    
    /**
    * @param entities the YahooReportManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<YahooReportManage> entities);

    /**
    * @param entities the YahooReportManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<YahooReportManage> entities);

    /**
    * @param entities the YahooReportManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<YahooReportManage> entities);
}