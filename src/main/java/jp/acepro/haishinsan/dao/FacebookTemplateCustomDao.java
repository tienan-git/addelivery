package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.FacebookTemplate;

@Dao
@InjectConfig
public interface FacebookTemplateCustomDao {

	@Select
	List<FacebookTemplate> selectAll(Long shopId);

	@Select
	List<FacebookTemplate> selectByTemplateName(Long shopId, String templateName);

	@Select
	List<FacebookTemplate> selectByTemplatePriority(Long shopId, Integer templatePriority);

}
