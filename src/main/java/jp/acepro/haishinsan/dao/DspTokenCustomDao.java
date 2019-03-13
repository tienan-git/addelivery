package jp.acepro.haishinsan.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.DspToken;

@Dao
@InjectConfig
public interface DspTokenCustomDao {

	@Select
	DspToken selectByDspTokenId();
}
