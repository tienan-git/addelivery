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
import java.util.ArrayList;
import java.util.List;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroup;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAd;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAdOperation;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAdReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAdServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAdStatus;
import com.google.api.ads.adwords.axis.v201809.cm.ApiError;
import com.google.api.ads.adwords.axis.v201809.cm.ApiException;
import com.google.api.ads.adwords.axis.v201809.cm.ExpandedTextAd;
import com.google.api.ads.adwords.axis.v201809.cm.Operator;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;

import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.util.ContextUtil;

/**
 * This example adds several expanded text ads to a given ad group. To get ad
 * groups, run GetAdGroups.java.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
public class AddExpandedTextAds {

	public String propFileName;
	public GoogleCampaignDto googleCampaignDto;
	public AdGroup newAdGroupUser;
	public AdGroup newAdGroupKeyword;

	public void run() {
		AdWordsSession session;
		try {
			// Generate a refreshable OAuth2 credential.
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).fromFile(propFileName)
					.build().generateCredential();

			// Construct an AdWordsSession.
			session = new AdWordsSession.Builder().fromFile(propFileName).withOAuth2Credential(oAuth2Credential)
					.build();
			// 店舗AdwordsIdを設定
			session.setClientCustomerId(ContextUtil.getCurrentShop().getGoogleAccountId());
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

		// 広告グループID設定
		try {
			runExample(adWordsServices, session, newAdGroupUser.getId());
			runExample(adWordsServices, session, newAdGroupKeyword.getId());
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
	 * @param adGroupId       the ID of the ad group where the ad will be created.
	 * @throws ApiException    if the API request failed with one or more service
	 *                         errors.
	 * @throws RemoteException if the API request failed due to other errors.
	 */
	public void runExample(AdWordsServicesInterface adWordsServices, AdWordsSession session, long adGroupId)
			throws RemoteException {
		// Get the AdGroupAdService.
		AdGroupAdServiceInterface adGroupAdService = adWordsServices.get(session, AdGroupAdServiceInterface.class);

		List<AdGroupAdOperation> operations = new ArrayList<>();

		// Create expanded text ad.
		ExpandedTextAd expandedTextAd = new ExpandedTextAd();
		// 広告見出し１設定
		expandedTextAd.setHeadlinePart1(String.format(googleCampaignDto.getTextAdTitle1()));
		// 広告見出し２設定
		expandedTextAd.setHeadlinePart2(googleCampaignDto.getTextAdTitle2());
		// 広告見出し３設定
		expandedTextAd.setHeadlinePart3("");
		// 説明文設定
		expandedTextAd.setDescription(googleCampaignDto.getTextAdDescription());
		// 説明文２設定
		expandedTextAd.setDescription2("");
		// 最終ページURL設定
		expandedTextAd.setFinalUrls(new String[] { googleCampaignDto.getTextAdFinalPageUrl() });

		// Create ad group ad.
		AdGroupAd expandedTextAdGroupAd = new AdGroupAd();
		expandedTextAdGroupAd.setAdGroupId(adGroupId);
		expandedTextAdGroupAd.setAd(expandedTextAd);

		// Optional: set the status.
		expandedTextAdGroupAd.setStatus(AdGroupAdStatus.ENABLED);

		// Create the operation.
		AdGroupAdOperation adGroupAdOperation = new AdGroupAdOperation();
		adGroupAdOperation.setOperand(expandedTextAdGroupAd);
		adGroupAdOperation.setOperator(Operator.ADD);

		operations.add(adGroupAdOperation);

		// Add ads.
		AdGroupAdReturnValue result = adGroupAdService
				.mutate(operations.toArray(new AdGroupAdOperation[operations.size()]));

		// Display ads.
//		Arrays.stream(result.getValue()).map(adGroupAdResult -> (ExpandedTextAd) adGroupAdResult.getAd())
//				.forEach(newAd -> System.out.printf("Expanded text ad with ID %d and headline '%s | %s%s' was added.%n", newAd.getId(), newAd.getHeadlinePart1(), newAd.getHeadlinePart2(), newAd.getHeadlinePart3() == null ? "" : String.format(" | %s", newAd.getHeadlinePart3())));
	}
}
