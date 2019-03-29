/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.GoogleTemplate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface GoogleTemplateDao {

    /**
     * @param templateId
     * @return the GoogleTemplate entity
     */
    @Select
    GoogleTemplate selectById(Long templateId);

    /**
     * @param templateId
     * @param versionNo
     * @return the GoogleTemplate entity
     */
    @Select(ensureResult = true)
    GoogleTemplate selectByIdAndVersion(Long templateId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(GoogleTemplate entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(GoogleTemplate entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(GoogleTemplate entity);
    
    /**
    * @param entities the GoogleTemplate
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<GoogleTemplate> entities);

    /**
    * @param entities the GoogleTemplate
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<GoogleTemplate> entities);

    /**
    * @param entities the GoogleTemplate
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<GoogleTemplate> entities);
}