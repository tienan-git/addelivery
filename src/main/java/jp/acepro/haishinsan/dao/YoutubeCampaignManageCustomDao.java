package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.YoutubeCampaignManage;

@Dao
@InjectConfig
public interface YoutubeCampaignManageCustomDao {

	@Select
	List<YoutubeCampaignManage> selectByIssueIdList(List<Long> issueIdList);

	@Select
	List<YoutubeCampaignManage> selectByCampaignManageIdList(List<Long> campaignManageIdList);
}
