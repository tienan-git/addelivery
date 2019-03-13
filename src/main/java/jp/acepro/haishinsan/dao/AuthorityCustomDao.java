package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;

@Dao
@InjectConfig
public interface AuthorityCustomDao {

	// 権限リストを取得
	@Select
	List<String> selectByRoleId(String roleId);
}
