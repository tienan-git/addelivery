package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.User;
import jp.acepro.haishinsan.entity.UserWithAgency;



@Dao
@InjectConfig
public interface UserCustomDao {

    @Select
    User selectByEmail(String email);

    @Select
    List<UserWithAgency> selectAll();

    // 権限リストを取得
    @Select
    List<String> selectByRoleId(String roleId);
    
    @Select
    UserWithAgency selectById(Long userId);
}
