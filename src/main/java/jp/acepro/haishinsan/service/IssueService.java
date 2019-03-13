package jp.acepro.haishinsan.service;

import java.util.List;

import jp.acepro.haishinsan.dto.IssueDto;

public interface IssueService {


	List<IssueDto> issueList();

	void createIssue(IssueDto issueDto);

}
