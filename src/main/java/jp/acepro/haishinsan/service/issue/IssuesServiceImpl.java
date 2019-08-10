package jp.acepro.haishinsan.service.issue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.acepro.haishinsan.dao.IssueCustomDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.IssuesDto;
import jp.acepro.haishinsan.entity.IssueWithShopWithCorporation;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.service.BaseService;
import jp.acepro.haishinsan.util.ContextUtil;

@Service
public class IssuesServiceImpl extends BaseService implements IssuesService {

    @Autowired
    IssueDao issueDao;

    @Autowired
    IssueCustomDao issueCustomDao;

    // DB: 案件一覧を取得する
    @Override
    @Transactional
    public List<IssuesDto> searchIssuesList(IssuesDto issuesSearch) {

        List<IssuesDto> issuesDtoList = new ArrayList<IssuesDto>();
        // DB検索
        List<IssueWithShopWithCorporation> IssueList = issueCustomDao
                .selectIssueList(ContextUtil.getCurrentShop().getShopId(), issuesSearch);
        for (IssueWithShopWithCorporation issue : IssueList) {
            IssuesDto issuesDto = new IssuesDto();
            issuesDto.setShopName(issue.getShopName());
            issuesDto.setCorporationName(issue.getCorporationName());
            issuesDto.setIssueId(issue.getIssueId());
            issuesDto.setCampaignName(issue.getCampaignName());
            issuesDto.setBudget(issue.getBudget());
            issuesDto.setStartDate(issue.getStartDate());
            issuesDto.setEndDate(issue.getEndDate());
            // Google
            if (Objects.nonNull(issue.getGoogleCampaignManageId())) {
                issuesDto.setMedia("Google");
                issuesDto.setMediaIcon("label google");
            }
            // Facebook
            if (Objects.nonNull(issue.getFacebookCampaignManageId())) {
                issuesDto.setMedia("FaceBook");
                issuesDto.setMediaIcon("label faceBook");
            }
            // twitter
            if (Objects.nonNull(issue.getTwitterCampaignManageId())) {
                issuesDto.setMedia("Twitter");
                issuesDto.setMediaIcon("label twitter");
            }
            // dsp
            if (Objects.nonNull(issue.getDspCampaignManageId())) {
                issuesDto.setMedia("FreakOut");
                issuesDto.setMediaIcon("label dsp");
            }
            issuesDtoList.add(issuesDto);
        }
        return issuesDtoList;
    }

    // DB: 案件を案件Idで論理削除する
    @Override
    @Transactional
    public void deleteIssueById(Long issueId) {

        Issue issue = issueDao.selectById(issueId);
        issue.setIsActived(Flag.OFF.getValue());
        // DB更新
        issueDao.update(issue);

    }

}
