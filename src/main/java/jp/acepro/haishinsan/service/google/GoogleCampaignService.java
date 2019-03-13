package jp.acepro.haishinsan.service.google;

import java.util.List;

import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDetailDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignInfoDto;

public interface GoogleCampaignService {

	// キャンペーン新規作成（API経由）
	void createCampaign(GoogleCampaignDto googleCampaignDto, IssueDto issueDto);

	// キャンペーン状態変更（API経由）
	void updateCampaignStatus(Long campaignId, String switchFlag);

	// キャンペーン一覧取得（API経由）
	List<GoogleCampaignInfoDto> getCampaignList();

	// キャンペーン詳細取得（API経由）
	GoogleCampaignDetailDto getCampaign(Long campaignId);

	// キャンペーン削除（API経由）
	void deleteCampaign(Long campaignId);

	void createGoogleCampaign(GoogleCampaignDto googleCampaignDto, IssueDto issueDto);
}
