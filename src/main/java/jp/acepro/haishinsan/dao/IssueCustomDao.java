package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.TwitterCampaignManage;
import jp.acepro.haishinsan.dto.IssuesDto;
import jp.acepro.haishinsan.entity.IssueWithShopWithCorporation;

@Dao
@InjectConfig
public interface IssueCustomDao {

    @Select
    List<Issue> selectByShopId(Long shopId);

    @Select
    List<IssueWithShopWithCorporation> selectIssueList(Long shopId, IssuesDto issueSearchDto);

    @Select
    TwitterCampaignManage selectCampaignIdByIssueId(Long shopId, Long issueId);

}
