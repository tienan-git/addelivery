package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.TwitterCampaignManage;

@Dao
@InjectConfig
public interface TwitterCampaignManageCustomDao {

	@Select
	List<TwitterCampaignManage> selectAll(Long shopId);
	
	@Select
	TwitterCampaignManage selectByCampaignId(String campaignId);
	
}
