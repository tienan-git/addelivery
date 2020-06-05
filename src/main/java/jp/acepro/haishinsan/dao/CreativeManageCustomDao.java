package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.CreativeManage;

@Dao
@InjectConfig
public interface CreativeManageCustomDao {

	@Select
	List<CreativeManage> selectCreativeManageByShopId(Long shopId);
}
