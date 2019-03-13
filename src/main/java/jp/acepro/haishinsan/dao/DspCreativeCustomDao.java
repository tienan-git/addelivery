package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.CreativeManage;

@Dao
@InjectConfig
public interface DspCreativeCustomDao {

	@Select
	List<CreativeManage> selectByShopId(Long shopId);
	
	@Select
	CreativeManage selectByCreativeId(Integer creativeId);

	@Select
	List<CreativeManage> selectByCreativeIds(List<Integer> creativeIdList);

}
