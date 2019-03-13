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
import jp.acepro.haishinsan.db.entity.TwitterTemplate;

/**
 */
@Dao
@InjectConfig
public interface TwitterTemplateDao {

    /**
     * @param templateId
     * @return the TwitterTemplate entity
     */
    @Select
    TwitterTemplate selectById(Long templateId);

    /**
     * @param templateId
     * @param versionNo
     * @return the TwitterTemplate entity
     */
    @Select(ensureResult = true)
    TwitterTemplate selectByIdAndVersion(Long templateId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(TwitterTemplate entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(TwitterTemplate entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(TwitterTemplate entity);
    
    /**
    * @param entities the TwitterTemplate
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<TwitterTemplate> entities);

    /**
    * @param entities the TwitterTemplate
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<TwitterTemplate> entities);

    /**
    * @param entities the TwitterTemplate
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<TwitterTemplate> entities);
}