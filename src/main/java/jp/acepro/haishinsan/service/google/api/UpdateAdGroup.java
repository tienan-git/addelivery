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
import com.google.api.ads.adwords.axis.v201809.cm.AdGroup;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupOperation;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.ApiError;
import com.google.api.ads.adwords.axis.v201809.cm.ApiException;
import com.google.api.ads.adwords.axis.v201809.cm.BiddingStrategyConfiguration;
import com.google.api.ads.adwords.axis.v201809.cm.Bids;
import com.google.api.ads.adwords.axis.v201809.cm.CpcBid;
import com.google.api.ads.adwords.axis.v201809.cm.CpmBid;
import com.google.api.ads.adwords.axis.v201809.cm.Money;
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
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.UnitPriceType;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;

/**
 * This example adds ad groups to a campaign. To get campaigns, run
 * GetCampaigns.java.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */

public class UpdateAdGroup {

	public String propFileName;
	public String googleAccountId;
	public Long adGroupId;
	public GoogleCampaignDto googleCampaignDto;

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
			runExample(adWordsServices, session);
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
	 * @param campaignId      the ID of the campaign where the ad groups will be
	 *                        created.
	 * @throws ApiException    if the API request failed with one or more service
	 *                         errors.
	 * @throws RemoteException if the API request failed due to other errors.
	 */
	public void runExample(AdWordsServicesInterface adWordsServices, AdWordsSession session) throws RemoteException {
		// Get the AdGroupService.
		AdGroupServiceInterface adGroupService = adWordsServices.get(session, AdGroupServiceInterface.class);

		// Create an ad group with the specified ID.
		AdGroup adGroup = new AdGroup();
		adGroup.setId(adGroupId);

		// 単価設定
		switch (GoogleAdType.of(googleCampaignDto.getAdType())) {
		case RESPONSIVE:
		case IMAGE:
			// クリック重視
			if (googleCampaignDto.getUnitPriceType().equals(UnitPriceType.CLICK.getValue())
					|| googleCampaignDto.getUnitPriceType() == null || googleCampaignDto.getUnitPriceType().isEmpty()) {
				break;
			}
			// 表示重視
			if (googleCampaignDto.getUnitPriceType().equals(UnitPriceType.DISPLAY.getValue())) {
				BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
				// 単価設定
				Double averageDisplayUnitPriceDouble = CodeMasterServiceImpl.googleAreaUnitPriceDisplayList.stream()
						.filter(obj -> googleCampaignDto.getLocationList().contains(obj.getFirst()))
						.mapToInt(obj -> obj.getSecond()).average().getAsDouble();
				Long averageUnitPrice = Math.round(averageDisplayUnitPriceDouble);
				Money cpmBidMoney = new Money();
				cpmBidMoney.setMicroAmount(averageUnitPrice * 1000000);
				CpmBid bid = new CpmBid();
				bid.setBid(cpmBidMoney);
				biddingStrategyConfiguration.setBids(new Bids[] { bid });
				adGroup.setBiddingStrategyConfiguration(biddingStrategyConfiguration);
				break;
			}
			break;
		case TEXT:
			BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
			Money cpcBidMoney = new Money();
			cpcBidMoney.setMicroAmount(50L * 1000000);
			CpcBid bid = new CpcBid();
			bid.setBid(cpcBidMoney);
			biddingStrategyConfiguration.setBids(new Bids[] { bid });
			adGroup.setBiddingStrategyConfiguration(biddingStrategyConfiguration);
			break;
		}

		// Create operations.
		AdGroupOperation operation = new AdGroupOperation();
		operation.setOperand(adGroup);
		operation.setOperator(Operator.SET);

		AdGroupOperation[] operations = new AdGroupOperation[] { operation };

		// Update ad group.
		AdGroupReturnValue result = adGroupService.mutate(operations);

	}
}
