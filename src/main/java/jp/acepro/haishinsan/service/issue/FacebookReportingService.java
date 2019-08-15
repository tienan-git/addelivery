package jp.acepro.haishinsan.service.issue;

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

public interface FacebookReportingService {

	void getReportDetails(EnumDatePreset enumDatePreset);

	List<FbReportDisplayDto> getDeviceReport(List<String> campaignIdList, String startDate, String endDate);

	List<FbReportDisplayDto> getRegionReport(List<String> campaignIdList, String startDate, String endDate);

	List<FbReportDisplayDto> getDateReport(List<String> campaignIdList, String startDate, String endDate);

	FbGraphReportDto getFacebookDeviceReportingGraph(List<String> campaignIdList, String startDate, String endDate);

	FbGraphReportDto getFacebookDateReportingGraph(List<String> campaignIdList, String startDate, String endDate);

	FbGraphReportDto getFacebookRegionReportingGraph(List<String> campaignIdList, String startDate, String endDate);

	String download(List<String> campaignIdList, String startDate, String endDate, Integer reportType);


}