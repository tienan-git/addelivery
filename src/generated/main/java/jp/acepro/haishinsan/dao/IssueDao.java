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
import jp.acepro.haishinsan.db.entity.Issue;

/**
 */
@Dao
@InjectConfig
public interface IssueDao {

    /**
     * @param issueId
     * @return the Issue entity
     */
    @Select
    Issue selectById(Long issueId);

    /**
     * @param issueId
     * @param versionNo
     * @return the Issue entity
     */
    @Select(ensureResult = true)
    Issue selectByIdAndVersion(Long issueId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(Issue entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(Issue entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(Issue entity);
    
    /**
    * @param entities the Issue
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<Issue> entities);

    /**
    * @param entities the Issue
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<Issue> entities);

    /**
    * @param entities the Issue
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<Issue> entities);
}