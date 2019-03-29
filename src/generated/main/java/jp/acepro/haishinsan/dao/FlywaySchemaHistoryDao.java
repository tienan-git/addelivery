/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.FlywaySchemaHistory;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface FlywaySchemaHistoryDao {

    /**
     * @param installedRank
     * @return the FlywaySchemaHistory entity
     */
    @Select
    FlywaySchemaHistory selectById(Integer installedRank);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(FlywaySchemaHistory entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(FlywaySchemaHistory entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(FlywaySchemaHistory entity);
    
    /**
    * @param entities the FlywaySchemaHistory
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<FlywaySchemaHistory> entities);

    /**
    * @param entities the FlywaySchemaHistory
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<FlywaySchemaHistory> entities);

    /**
    * @param entities the FlywaySchemaHistory
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<FlywaySchemaHistory> entities);
}