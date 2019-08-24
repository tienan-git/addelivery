package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.DspCampaignManage;

@Dao
@InjectConfig
public interface DspCampaignCustomDao {

	@Select
	List<DspCampaignManage> selectAll();

	@Select
	List<DspCampaignManage> selectByShopId(Long shopId);

	@Select
	DspCampaignManage selectByCampaignId(Integer campaignId);

}
