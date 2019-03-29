/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.DspTemplate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface DspTemplateDao {

    /**
     * @param templateId
     * @return the DspTemplate entity
     */
    @Select
    DspTemplate selectById(Long templateId);

    /**
     * @param templateId
     * @param versionNo
     * @return the DspTemplate entity
     */
    @Select(ensureResult = true)
    DspTemplate selectByIdAndVersion(Long templateId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(DspTemplate entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(DspTemplate entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(DspTemplate entity);
    
    /**
    * @param entities the DspTemplate
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<DspTemplate> entities);

    /**
    * @param entities the DspTemplate
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<DspTemplate> entities);

    /**
    * @param entities the DspTemplate
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<DspTemplate> entities);
}