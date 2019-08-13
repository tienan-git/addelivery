package jp.acepro.haishinsan.service.facebook;

import java.util.List;

import com.facebook.ads.sdk.AdsInsights.EnumDatePreset;

import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.facebook.FbCreativeDto;
import jp.acepro.haishinsan.dto.facebook.FbGraphReportDto;
import jp.acepro.haishinsan.dto.facebook.FbIssueDto;
import jp.acepro.haishinsan.dto.facebook.FbReportDisplayDto;
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

	// キャンペーンの審査ステータスを更新
	void updateCampaignCheckStatus(String campaignId, String checkStatus);

	void getReportDetails(EnumDatePreset enumDatePreset);

	List<FbReportDisplayDto> getDeviceReport(List<String> campaignIdList, String startDate, String endDate);

	List<FbReportDisplayDto> getRegionReport(List<String> campaignIdList, String startDate, String endDate);

	List<FbReportDisplayDto> getDateReport(List<String> campaignIdList, String startDate, String endDate);

	FbGraphReportDto getFacebookDeviceReportingGraph(List<String> campaignIdList, String startDate, String endDate);

	FbGraphReportDto getFacebookDateReportingGraph(List<String> campaignIdList, String startDate, String endDate);

	FbGraphReportDto getFacebookRegionReportingGraph(List<String> campaignIdList, String startDate, String endDate);

	String download(List<String> campaignIdList, String startDate, String endDate, Integer reportType);

	void createFacebookCampaign(FbCampaignDto fbCampaignDto, IssueDto issueDto);
	
	Issue createIssue(FbIssueDto fbIssueDto);


}