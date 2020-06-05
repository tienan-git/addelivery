package jp.acepro.haishinsan.service.facebook;

import java.util.List;

import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.facebook.FbCreativeDto;
import jp.acepro.haishinsan.dto.facebook.FbIssueDto;
import jp.acepro.haishinsan.dto.facebook.FbTemplateDto;

public interface FacebookService {
	// 初期テンプレートを作成
	void createDefaultTemplate(long shopId);

	FbTemplateDto create(FbTemplateDto fbTempleteDto);

	FbTemplateDto templateDetail(Long templateId);

	void templateUpdate(FbTemplateDto fbTemplateDto);

	List<FbTemplateDto> searchList();

	FbTemplateDto templateDelete(Long templateId);

	void createCreative(FbCreativeDto fbCreativeDto, List<DspSegmentListDto> dspSegmentDtoList);

	FbCampaignDto createCampaign(FbCampaignDto fbCampaignDto, IssueDto issueDto);

	List<FbCampaignDto> campaignList(List<FacebookCampaignManage> facebookCampaignManageList);

	List<FacebookCampaignManage> searchFacebookCampaignManageList();

	FbCampaignDto campaignDetail(String campaignId);

	void deleteCampaign(String campaignId);

	// キャンペーンの配信ステータスを更新
	void updateCampaignStatus(String campaignId, String campaignStatus);

	// 案件の審査ステータスを更新
	void updateIssueCheckStatus(Long issueId, String checkStatus);

	void createFacebookCampaign(FbCampaignDto fbCampaignDto, IssueDto issueDto);

	Issue createIssue(FbIssueDto fbIssueDto);
	
    void dailyCheck(FbIssueDto fbIssueDto);

}