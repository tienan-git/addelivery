package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.DspTemplate;

@Dao
@InjectConfig
public interface DspTemplateCustomDao {

	@Select
	List<DspTemplate> selectAll();

	@Select
	DspTemplate selectByName(String templateName, long shopId);

	@Select
	DspTemplate selectByPriority(Integer templatePriority, long shopId);

	@Select
	List<DspTemplate> selectByShopId(Long shopId);

	@Select
	DspTemplate selectDefaultTemplate(long currentShopId);
}
