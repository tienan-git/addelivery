package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.GoogleTemplate;

@Dao
@InjectConfig
public interface GoogleTemplateCustomDao {

	@Select
	List<GoogleTemplate> selectByShopId(Long shopId);

	@Select
	List<GoogleTemplate> selectByTemplateName(Long shopId, String templateName);

	@Select
	List<GoogleTemplate> selectByTemplatePriority(Long shopId, Integer templatePriority);
}
