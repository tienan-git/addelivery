/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.dao;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.User;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

/**
 */
@Dao
@InjectConfig
public interface UserDao {

    /**
     * @param userId
     * @return the User entity
     */
    @Select
    User selectById(Long userId);

    /**
     * @param userId
     * @param versionNo
     * @return the User entity
     */
    @Select(ensureResult = true)
    User selectByIdAndVersion(Long userId, Long versionNo);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(User entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(User entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(User entity);
    
    /**
    * @param entities the User
    * @return affected rows
    */
    @org.seasar.doma.BatchInsert
    int[] insert(Iterable<User> entities);

    /**
    * @param entities the User
    * @return affected rows
    */
    @org.seasar.doma.BatchUpdate
    int[] update(Iterable<User> entities);

    /**
    * @param entities the User
    * @return affected rows
    */
    @org.seasar.doma.BatchDelete
    int[] delete(Iterable<User> entities);
}