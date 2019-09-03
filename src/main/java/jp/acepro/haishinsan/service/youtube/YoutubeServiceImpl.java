package jp.acepro.haishinsan.service.youtube;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.DspSegmentCustomDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.dao.YoutubeCampaignManageDao;
import jp.acepro.haishinsan.dao.YoutubeCustomDao;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.YoutubeCampaignManage;
import jp.acepro.haishinsan.dto.EmailCampDetailDto;
import jp.acepro.haishinsan.dto.EmailDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeIssueDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeLocationDto;
import jp.acepro.haishinsan.entity.YoutubeIssueDetail;
import jp.acepro.haishinsan.enums.EmailTemplateType;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.service.EmailService;
import jp.acepro.haishinsan.util.ContextUtil;

@Service
public class YoutubeServiceImpl implements YoutubeService {

	@Autowired
	DspSegmentCustomDao dspSegmentCustomDao;

	@Autowired
	YoutubeCustomDao youtubeCustomDao;

	@Autowired
	YoutubeCampaignManageDao youtubeCampaignManageDao;

	@Autowired
	IssueDao issueDao;

	@Autowired
	EmailService emailService;

	@Override
	public List<YoutubeIssueDto> searchYoutubeIssueList() {

		// DB Access
		List<Issue> issueList = youtubeCustomDao.selectIssueByShopId(ContextUtil.getCurrentShop().getShopId());

		// Entity -> Dto
		List<YoutubeIssueDto> youtubeIssueDtoList = new ArrayList<YoutubeIssueDto>();
		for (Issue issue : issueList) {
			YoutubeIssueDto youtubeIssueDto = new YoutubeIssueDto();
			youtubeIssueDto.setBudget(issue.getBudget());
			youtubeIssueDto.setCampaignName(issue.getCampaignName());
			youtubeIssueDto.setEndDate(issue.getEndDate());
			youtubeIssueDto.setIssueId(issue.getIssueId());
			youtubeIssueDto.setStartDate(issue.getStartDate());

			youtubeIssueDtoList.add(youtubeIssueDto);
		}

		return youtubeIssueDtoList;
	}

	@Override
	public List<YoutubeLocationDto> getLocationList(List<Long> locationIdList) {
//		List<YoutubeArea> youtubeAreaList = youtubeCustomDao.selectAreaByAreaId(locationIdList);
//		List<YoutubeLocationDto> locationList = new ArrayList<YoutubeLocationDto>();
//		for (YoutubeArea youtubeArea : youtubeAreaList) {
//			YoutubeLocationDto youtubeLocationDto = new YoutubeLocationDto();
//			youtubeLocationDto.setLocationId(youtubeArea.getAreaId());
//			youtubeLocationDto.setLocationName(youtubeArea.getAreaName());
//			locationList.add(youtubeLocationDto);
//		}
		return null;
	}

	@Override
	public YoutubeIssueDto createIssue(YoutubeIssueDto youtubeIssueDto) {
		// DTO->Entity
		YoutubeCampaignManage youtubeCampaignManage = new YoutubeCampaignManage();
		youtubeCampaignManage.setCampaignName(youtubeIssueDto.getCampaignName());
		youtubeCampaignManage.setAdType(youtubeIssueDto.getAdType());
		youtubeCampaignManage.setBudget(youtubeIssueDto.getBudget());
		youtubeCampaignManage.setStartDate(youtubeIssueDto.getStartDate());
		youtubeCampaignManage.setEndDate(youtubeIssueDto.getEndDate());
		youtubeCampaignManage.setArea(youtubeIssueDto.getArea());
		youtubeCampaignManage.setLp(youtubeIssueDto.getLp());
		youtubeCampaignManage.setVideoUrl(youtubeIssueDto.getVideoUrl());

		// DB access
		youtubeCampaignManageDao.insert(youtubeCampaignManage);

		// DTO->Entity
		Issue issue = new Issue();
		issue.setShopId(ContextUtil.getCurrentShop().getShopId());
		issue.setYoutubeCampaignManageId(youtubeCampaignManage.getYoutubeCampaignManageId());
		issue.setCampaignName(youtubeIssueDto.getCampaignName());
		issue.setBudget(youtubeIssueDto.getBudget());		
		
        String startDateTime = youtubeIssueDto.getStartDate() + " " + youtubeIssueDto.getStartHour() + ":"
                + youtubeIssueDto.getStartMin();
        String endDateTime = youtubeIssueDto.getEndDate() + " " + youtubeIssueDto.getEndHour() + ":"
                + youtubeIssueDto.getEndMin();
		issue.setStartDate(startDateTime);
		issue.setEndDate(endDateTime);
		
		// DB access
		issueDao.insert(issue);
		youtubeIssueDto.setIssueId(issue.getIssueId());

		try {
			// メール送信
			EmailDto emailDto = new EmailDto();
			emailDto.setIssueId(youtubeIssueDto.getIssueId());
			EmailCampDetailDto emailCampDetailDto = new EmailCampDetailDto();
			emailCampDetailDto.setCampaignName(youtubeIssueDto.getCampaignName());
			// ６:youtube
			emailCampDetailDto.setMediaType(6);
			List<EmailCampDetailDto> emailCampDetailDtoList = new ArrayList<EmailCampDetailDto>();
			emailCampDetailDtoList.add(emailCampDetailDto);
			emailDto.setCampaignList(emailCampDetailDtoList);
			// Template type - 案件依頼
			emailDto.setTemplateType(EmailTemplateType.ISSUEREQUEST.getValue());
			emailService.sendEmail(emailDto);
		} catch (Exception e) {
			throw new BusinessException(ErrorCodeConstant.E60002);
		}

		return youtubeIssueDto;
	}

	@Override
	public YoutubeIssueDto deleteIssue(Long issueId) {

		YoutubeIssueDto YoutubeIssueDto = this.getIssueDetail(issueId);

		// 該当キャンペーンを論理削除
		Issue issue = issueDao.selectById(issueId);
		issue.setIsActived(Flag.OFF.getValue());
		issueDao.update(issue);

		YoutubeCampaignManage youtubeCampaignManage = youtubeCampaignManageDao
				.selectById(issue.getYoutubeCampaignManageId());
		youtubeCampaignManage.setIsActived(Flag.OFF.getValue());
		youtubeCampaignManageDao.update(youtubeCampaignManage);

		return YoutubeIssueDto;

	}

	@Override
	public YoutubeIssueDto getIssueDetail(Long issueId) {

		// DB access
		YoutubeIssueDetail youtubeIssueDetail = youtubeCustomDao.selectIssueDetail(issueId);

		// Entity -> Dto
		YoutubeIssueDto youtubeIssueDto = new YoutubeIssueDto();
		youtubeIssueDto.setIssueId(youtubeIssueDetail.getIssueId());
		youtubeIssueDto.setCampaignId(youtubeIssueDetail.getCampaignId());
		youtubeIssueDto.setCampaignName(youtubeIssueDetail.getCampaignName());
		youtubeIssueDto.setAdType(youtubeIssueDetail.getAdType());
		youtubeIssueDto.setBudget(youtubeIssueDetail.getBudget());
		youtubeIssueDto.setStartDate(youtubeIssueDetail.getStartDate());
		youtubeIssueDto.setEndDate(youtubeIssueDetail.getEndDate());
		youtubeIssueDto.setArea(youtubeIssueDetail.getArea());
		youtubeIssueDto.setLp(youtubeIssueDetail.getLp());
		youtubeIssueDto.setVideoUrl(youtubeIssueDetail.getVideoUrl());

		return youtubeIssueDto;
	}

	@Override
	public void updateIssue(Long issueId, Long campaignId) {

		Issue issue = issueDao.selectById(issueId);
		YoutubeCampaignManage youtubeCampaignManage = youtubeCampaignManageDao
				.selectById(issue.getYoutubeCampaignManageId());

		// キャンペーンIDを更新する
		youtubeCampaignManage.setCampaignId(campaignId);
		youtubeCampaignManageDao.update(youtubeCampaignManage);

	}

}
