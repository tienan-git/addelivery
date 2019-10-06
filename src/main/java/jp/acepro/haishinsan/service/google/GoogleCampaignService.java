package jp.acepro.haishinsan.service.google;

import java.util.List;

import jp.acepro.haishinsan.db.entity.GoogleCampaignManage;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.facebook.FbIssueDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDetailDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignInfoDto;
import jp.acepro.haishinsan.dto.google.GoogleIssueDto;
import jp.acepro.haishinsan.form.GoogleBannerAdIssueForm;
import jp.acepro.haishinsan.form.GoogleBannerTextAdIssueForm;
import jp.acepro.haishinsan.form.GoogleIssueInputForm;
import jp.acepro.haishinsan.form.GoogleTextAdIssueForm;

public interface GoogleCampaignService {

	// キャンペーン新規作成（API経由）
	Long createCampaign(GoogleCampaignDto googleCampaignDto, IssueDto issueDto);

	// キャンペーン状態変更（API経由）
	void updateCampaignStatus(Long campaignId, String switchFlag);

	// 案件審査状態変更
	void updateIssueCheckStatus(Long issueId, String switchFlag);

	// キャンペーン一覧取得（API経由）
	List<GoogleCampaignInfoDto> getCampaignList();

	// キャンペーン詳細取得（API経由）
	GoogleCampaignDetailDto getCampaign(Long campaignId);

	// キャンペーン削除（API経由）
	void deleteCampaign(Long campaignId);

	void createGoogleCampaign(GoogleCampaignDto googleCampaignDto, IssueDto issueDto);

	List<GoogleCampaignManage> searchGoogleCampaignManageList(String adType);

	List<GoogleCampaignDto> campaignList(List<GoogleCampaignManage> googleCampaignManageList);

	Issue createIssue(GoogleIssueDto googleIssueDto);

	GoogleIssueDto mapToIssue(GoogleIssueInputForm googleIssueInputForm);
	
    void dailyCheck(GoogleIssueDto googleIssueDto);

	GoogleIssueDto mapToIssue(GoogleBannerAdIssueForm googleBannerAdIssueForm);

	GoogleIssueDto mapToIssue(GoogleBannerTextAdIssueForm googleBannerTextAdIssueForm);

	GoogleIssueDto mapToIssue(GoogleTextAdIssueForm googleTextAdIssueForm);
}
