package jp.acepro.haishinsan.service.api;

public interface IssueApiService {

	void startFacebookIssueAsync();

	void stopFacebookIssueAsync();

	void startGoogleIssueAsync();

	void stopGoogleIssueAsync();
}
