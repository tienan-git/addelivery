package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.entity.YoutubeIssueDetail;

@Dao
@InjectConfig
public interface YoutubeCustomDao {

//    @Select
//    List<YoutubeCampaignManage> selectByShopId(Long shopId);

//    @Select
//    List<YoutubeArea> selectAllArea();
//    
//    @Select
//    List<YoutubeArea> selectAreaByAreaId(List<Long> locationIdList);

	@Select
	List<Issue> selectIssueByShopId(Long shopId);

	@Select
	YoutubeIssueDetail selectIssueDetail(Long issueId);

}
