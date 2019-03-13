package jp.acepro.haishinsan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.util.ContextUtil;

@Service
public class IssueInsertServiceImpl implements IssueInsertService {

	@Autowired
	IssueDao issueDao;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Issue insertIssue(IssueDto issueDto) {

		// 案件表にインサート
		Issue issue = new Issue();
		issue.setBudget(issueDto.getBudget());
		issue.setCampaignName(issueDto.getCampaignName());
		issue.setShopId(ContextUtil.getCurrentShopId());
		issue.setStartDate(issueDto.getStartDate());
		issue.setEndDate(issueDto.getEndDate());
		issue.setDspCampaignManageId(issueDto.getDspCampaignManageId());
		issue.setGoogleCampaignManageId(issueDto.getGoogleCampaignManageId());
		issue.setFacebookCampaignManageId(issueDto.getFacebookCampaignManageId());
		issue.setTwitterCampaignManageId(issueDto.getTwitterCampaignManageId());
		issueDao.insert(issue);
		
		return issue;
	}

}
