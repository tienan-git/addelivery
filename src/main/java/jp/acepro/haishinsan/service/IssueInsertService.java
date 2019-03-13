package jp.acepro.haishinsan.service;

import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.IssueDto;

public interface IssueInsertService {

	Issue insertIssue(IssueDto issueDto);

}
