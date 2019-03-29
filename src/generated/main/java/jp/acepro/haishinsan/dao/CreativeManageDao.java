/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.CreativeManage;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface CreativeManageDao {

    /**
     * @param creativeManageId
     * @return the CreativeManage entity
     */
    @Select
    CreativeManage selectById(Long creativeManageId);

    /**
     * @param creativeManageId
     * @param versionNo
     * @return the CreativeManage entity
     */
    @Select(ensureResult = true)
    CreativeManage selectByIdAndVersion(Long creativeManageId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(CreativeManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(CreativeManage entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(CreativeManage entity);
    
    /**
    * @param entities the CreativeManage
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<CreativeManage> entities);

    /**
    * @param entities the CreativeManage
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<CreativeManage> entities);

    /**
    * @param entities the CreativeManage
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<CreativeManage> entities);
}