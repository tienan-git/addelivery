// Copyright 2017 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package jp.acepro.haishinsan.service.google.api;

import static com.google.api.ads.common.lib.utils.Builder.DEFAULT_CONFIGURATION_FILENAME;

import java.rmi.RemoteException;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201809.cm.ApiError;
import com.google.api.ads.adwords.axis.v201809.cm.ApiException;
import com.google.api.ads.adwords.axis.v201809.cm.Campaign;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignOperation;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignStatus;
import com.google.api.ads.adwords.axis.v201809.cm.Operator;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;

/**
 * This example updates a campaign by setting the status to PAUSED. To get
 * campaigns, run GetCampaigns.java.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
public class UpdateCampaignStatus {

	public String propFileName;
	public String googleAccountId;

	public void run(Long campaignId, String switchFlag) {
		AdWordsSession session;
		try {
			// Generate a refreshable OAuth2 credential.
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).fromFile(propFileName)
					.build().generateCredential();

			// Construct an AdWordsSession.
			session = new AdWordsSession.Builder().fromFile(propFileName).withOAuth2Credential(oAuth2Credential)
					.build();
			// 店舗AdwordsIdを設定
			session.setClientCustomerId(googleAccountId);
		} catch (ConfigurationLoadException cle) {
			System.err.printf("Failed to load configuration from the %s file. Exception: %s%n",
					DEFAULT_CONFIGURATION_FILENAME, cle);
			return;
		} catch (ValidationException ve) {
			System.err.printf("Invalid configuration in the %s file. Exception: %s%n", DEFAULT_CONFIGURATION_FILENAME,
					ve);
			return;
		} catch (OAuthException oe) {
			System.err.printf(
					"Failed to create OAuth credentials. Check OAuth settings in the %s file. " + "Exception: %s%n",
					DEFAULT_CONFIGURATION_FILENAME, oe);
			return;
		}

		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();

		try {
			runExample(adWordsServices, session, campaignId, switchFlag);
		} catch (ApiException apiException) {
			// ApiException is the base class for most exceptions thrown by an API request.
			// Instances
			// of this exception have a message and a collection of ApiErrors that indicate
			// the
			// type and underlying cause of the exception. Every exception object in the
			// adwords.axis
			// packages will return a meaningful value from toString
			//
			// ApiException extends RemoteException, so this catch block must appear before
			// the
			// catch block for RemoteException.
			System.err.println("Request failed due to ApiException. Underlying ApiErrors:");
			if (apiException.getErrors() != null) {
				int i = 0;
				for (ApiError apiError : apiException.getErrors()) {
					System.err.printf("  Error %d: %s%n", i++, apiError);
				}
			}
		} catch (RemoteException re) {
			System.err.printf("Request failed unexpectedly due to RemoteException: %s%n", re);
		}
	}

	/**
	 * Runs the example.
	 *
	 * @param adWordsServices the services factory.
	 * @param session         the session.
	 * @param campaignId      the ID of the campaign to update.
	 * @throws ApiException    if the API request failed with one or more service
	 *                         errors.
	 * @throws RemoteException if the API request failed due to other errors.
	 */
	public void runExample(AdWordsServicesInterface adWordsServices, AdWordsSession session, Long campaignId,
			String switchFlag) throws RemoteException {
		// Get the CampaignService.
		CampaignServiceInterface campaignService = adWordsServices.get(session, CampaignServiceInterface.class);

		// Create campaign with updated status.
		Campaign campaign = new Campaign();
		campaign.setId(campaignId);
		if (switchFlag.equals("ON")) {
			campaign.setStatus(CampaignStatus.ENABLED);
		} else {
			campaign.setStatus(CampaignStatus.PAUSED);
		}

		// Create operations.
		CampaignOperation operation = new CampaignOperation();
		operation.setOperand(campaign);
		operation.setOperator(Operator.SET);

		CampaignOperation[] operations = new CampaignOperation[] { operation };

		// Update campaign.
		CampaignReturnValue result = campaignService.mutate(operations);

		// Display campaigns.
//		for (Campaign campaignResult : result.getValue()) {
//			System.out.printf("The status of Campaign with name '%s', ID %d " + "was updated to '%s' .%n", campaignResult.getName(), campaignResult.getId(), campaignResult.getStatus().toString());
//		}
	}
}
