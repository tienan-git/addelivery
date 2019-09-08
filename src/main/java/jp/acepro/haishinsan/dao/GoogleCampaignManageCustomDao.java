package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.GoogleCampaignManage;

@Dao
@InjectConfig
public interface GoogleCampaignManageCustomDao {

	@Select
	List<GoogleCampaignManage> selectAll();

	@Select
	List<GoogleCampaignManage> selectByIssueIdList(List<Long> issueIdList);

	@Select
	GoogleCampaignManage selectByCampaignId(Long campaignId);

	@Select
	List<GoogleCampaignManage> selectByCampaignManageIdList(List<Long> campaignManageIdList);

	@Select
	List<GoogleCampaignManage> selectByCampaignList(List<Long> campaignIdList);

	@Select
	List<GoogleCampaignManage> selectByShopIdAndAdType(Long shopId, String adType);
	
	@Select
	List<GoogleCampaignManage> selectWithActiveShop();
	
}
