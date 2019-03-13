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
import jp.acepro.haishinsan.db.entity.DspToken;

/**
 */
@Dao
@InjectConfig
public interface DspTokenDao {

    /**
     * @param dspTokenId
     * @return the DspToken entity
     */
    @Select
    DspToken selectById(Long dspTokenId);

    /**
     * @param dspTokenId
     * @param versionNo
     * @return the DspToken entity
     */
    @Select(ensureResult = true)
    DspToken selectByIdAndVersion(Long dspTokenId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(DspToken entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(DspToken entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(DspToken entity);
    
    /**
    * @param entities the DspToken
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<DspToken> entities);

    /**
    * @param entities the DspToken
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<DspToken> entities);

    /**
    * @param entities the DspToken
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<DspToken> entities);
}