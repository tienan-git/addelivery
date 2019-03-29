/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.FacebookTemplate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface FacebookTemplateDao {

    /**
     * @param templateId
     * @return the FacebookTemplate entity
     */
    @Select
    FacebookTemplate selectById(Long templateId);

    /**
     * @param templateId
     * @param versionNo
     * @return the FacebookTemplate entity
     */
    @Select(ensureResult = true)
    FacebookTemplate selectByIdAndVersion(Long templateId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(FacebookTemplate entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(FacebookTemplate entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(FacebookTemplate entity);
    
    /**
    * @param entities the FacebookTemplate
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<FacebookTemplate> entities);

    /**
    * @param entities the FacebookTemplate
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<FacebookTemplate> entities);

    /**
    * @param entities the FacebookTemplate
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<FacebookTemplate> entities);
}