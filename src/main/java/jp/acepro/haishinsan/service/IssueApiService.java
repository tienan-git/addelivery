package jp.acepro.haishinsan.service;

public interface IssueApiService {

	void startFacebookIssueAsync();

	void stopFacebookIssueAsync();

	void startGoogleIssueAsync();
	
	void stopGoogleIssueAsync();
}
