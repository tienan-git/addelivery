package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.TwitterTemplate;

@Dao
@InjectConfig
public interface TwitterTemplateCustomDao {

	// すべてのテンプレートを検索
	@Select
	List<TwitterTemplate> selectAll(long shopId);

	// テンプレート名で検索
	@Select
	TwitterTemplate selectByName(String templateName, long shopId);

	// テンプレート優先度で検索
	@Select
	TwitterTemplate selectByPriority(Integer templatePriority, long shopId);
}
