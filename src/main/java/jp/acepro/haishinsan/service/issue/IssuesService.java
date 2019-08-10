package jp.acepro.haishinsan.service.issue;

import java.util.List;

import jp.acepro.haishinsan.dto.IssuesDto;

public interface IssuesService {

    // 案件一覧検索（検索条件より）
    List<IssuesDto> searchIssuesList(IssuesDto issuesDto);

    // 案件を案件Idで削除
    void deleteIssueById(Long issueId);

    // 案件IdでcampaignIdを検索
    String selectCampaignIdByIssueId(Long issueId);

}
