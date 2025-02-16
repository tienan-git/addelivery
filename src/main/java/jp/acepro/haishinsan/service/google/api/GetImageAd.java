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
import com.google.api.ads.adwords.axis.utils.v201809.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAd;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAdPage;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupAdServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.ApiError;
import com.google.api.ads.adwords.axis.v201809.cm.ApiException;
import com.google.api.ads.adwords.axis.v201809.cm.ImageAd;
import com.google.api.ads.adwords.axis.v201809.cm.PolicyApprovalStatus;
import com.google.api.ads.adwords.axis.v201809.cm.Selector;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.AdGroupAdField;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;

import jp.acepro.haishinsan.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * This example gets non-removed expanded text ads in an ad group. To add
 * expanded text ads, run AddExpandedTextAds.java.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
@Slf4j
public class GetImageAd {

	private static final int PAGE_SIZE = 100;

	public String propFileName;
	public Long adGroupId;
	public String googleAccountId;
	public List<ImageAd> imageAdList = new ArrayList<ImageAd>();
	public List<PolicyApprovalStatus> policyApprovalStatusList = new ArrayList<PolicyApprovalStatus>();
	
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
			runExample(adWordsServices, session, adGroupId);
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
	 * @param adGroupId       the ID of the ad group to use to find expanded text
	 *                        ads.
	 * @throws ApiException    if the API request failed with one or more service
	 *                         errors.
	 * @throws RemoteException if the API request failed due to other errors.
	 */
	public void runExample(AdWordsServicesInterface adWordsServices, AdWordsSession session, Long adGroupId)
			throws RemoteException {
		// Get the AdGroupAdService.
		AdGroupAdServiceInterface adGroupAdService = adWordsServices.get(session, AdGroupAdServiceInterface.class);

		int offset = 0;
		boolean morePages = true;

		// Create selector.
		SelectorBuilder builder = new SelectorBuilder();
		Selector selector = builder.fields(AdGroupAdField.CreativeFinalUrls, AdGroupAdField.MarketingImage, AdGroupAdField.PolicySummary, AdGroupAdField.CombinedApprovalStatus)
				.orderAscBy(AdGroupAdField.Id).offset(offset).limit(PAGE_SIZE)
				.equals(AdGroupAdField.AdGroupId, adGroupId.toString()).in(AdGroupAdField.Status, "ENABLED", "PAUSED")
				.equals("AdType", "IMAGE_AD").build();

		while (morePages) {
			// Get all ads.
			AdGroupAdPage page = adGroupAdService.get(selector);

			// Display ads.
			if (page.getEntries() != null && page.getEntries().length > 0) {
				for (AdGroupAd adGroupAd : page.getEntries()) {
					ImageAd imageAd = (ImageAd) adGroupAd.getAd();
					imageAdList.add(imageAd);
					policyApprovalStatusList.add(adGroupAd.getPolicySummary().getCombinedApprovalStatus());
					// System.out.printf(
					// "Expanded text ad with ID %d, status '%s', and headline '%s - %s' was
					// found.%n",
					// adGroupAd.getAd().getId(),
					// adGroupAd.getStatus(),
					// expandedTextAd.getHeadlinePart1(),
					// expandedTextAd.getHeadlinePart2());
					// log.debug("ImageAd : {}", imageAd.toString());
				}
			} else {
				// System.out.println("No expanded text ads were found.");
			}

			offset += PAGE_SIZE;
			selector = builder.increaseOffsetBy(PAGE_SIZE).build();
			morePages = offset < page.getTotalNumEntries();
		}
	}
}
