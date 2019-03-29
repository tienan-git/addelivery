/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.CampaignKeyword;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface CampaignKeywordDao {

    /**
     * @param keywordId
     * @return the CampaignKeyword entity
     */
    @Select
    CampaignKeyword selectById(Long keywordId);

    /**
     * @param keywordId
     * @param versionNo
     * @return the CampaignKeyword entity
     */
    @Select(ensureResult = true)
    CampaignKeyword selectByIdAndVersion(Long keywordId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(CampaignKeyword entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(CampaignKeyword entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(CampaignKeyword entity);
    
    /**
    * @param entities the CampaignKeyword
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<CampaignKeyword> entities);

    /**
    * @param entities the CampaignKeyword
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<CampaignKeyword> entities);

    /**
    * @param entities the CampaignKeyword
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<CampaignKeyword> entities);
}