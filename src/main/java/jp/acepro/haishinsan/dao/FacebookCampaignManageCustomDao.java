package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;

@Dao
@InjectConfig
public interface FacebookCampaignManageCustomDao {

	@Select
	List<FacebookCampaignManage> selectByShopId(Long shopId);

	@Select
	FacebookCampaignManage selectByCampaignId(String campaignId);
	
	@Select
	List<FacebookCampaignManage> selectWithActiveShop();
}
