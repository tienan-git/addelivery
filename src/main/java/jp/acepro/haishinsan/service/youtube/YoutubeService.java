package jp.acepro.haishinsan.service.youtube;

import java.util.List;

import jp.acepro.haishinsan.dto.youtube.YoutubeIssueDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeLocationDto;

public interface YoutubeService {

	List<YoutubeIssueDto> searchYoutubeIssueList();

	List<YoutubeLocationDto> getLocationList(List<Long> locationIdList);

	YoutubeIssueDto createIssue(YoutubeIssueDto youtubeIssueDto);

	YoutubeIssueDto getIssueDetail(Long issueId);

	YoutubeIssueDto deleteIssue(Long issueId);

	void updateIssue(Long issueId, Long campaignId);

}
