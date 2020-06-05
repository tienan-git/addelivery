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
import java.util.List;

import com.beust.jcommander.Parameter;
import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroup;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupCriterionOperation;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupCriterionReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupCriterionServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupOperation;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupStatus;
import com.google.api.ads.adwords.axis.v201809.cm.ApiError;
import com.google.api.ads.adwords.axis.v201809.cm.ApiException;
import com.google.api.ads.adwords.axis.v201809.cm.BiddableAdGroupCriterion;
import com.google.api.ads.adwords.axis.v201809.cm.BiddingStrategyConfiguration;
import com.google.api.ads.adwords.axis.v201809.cm.Bids;
import com.google.api.ads.adwords.axis.v201809.cm.Campaign;
import com.google.api.ads.adwords.axis.v201809.cm.CpcBid;
import com.google.api.ads.adwords.axis.v201809.cm.CpmBid;
import com.google.api.ads.adwords.axis.v201809.cm.Keyword;
import com.google.api.ads.adwords.axis.v201809.cm.KeywordMatchType;
import com.google.api.ads.adwords.axis.v201809.cm.Money;
import com.google.api.ads.adwords.axis.v201809.cm.Operator;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.utils.examples.ArgumentNames;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.common.lib.utils.examples.CodeSampleParams;
import com.google.api.client.auth.oauth2.Credential;

import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.UnitPriceType;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.util.ContextUtil;

/**
 * This example adds ad groups to a campaign. To get campaigns, run
 * GetCampaigns.java.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */

public class AddAdGroups {

	public String propFileName;
	public GoogleCampaignDto googleCampaignDto;
	public Campaign newCampaign;
	public AdGroup newAdGroupUser;
	public AdGroup newAdGroupKeyword;

	private static class AddAdGroupsParams extends CodeSampleParams {
		@Parameter(names = ArgumentNames.CAMPAIGN_ID, required = true)
		private Long campaignId;
	}

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

		AddAdGroupsParams params = new AddAdGroupsParams();

		// Either pass the required parameters for this example on the command line, or
		// insert them
		// into the code here. See the parameter class definition above for
		// descriptions.
		// キャンプーンID設定
		params.campaignId = newCampaign.getId();

		try {
			runExample(adWordsServices, session, params.campaignId);
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
	public void runExample(AdWordsServicesInterface adWordsServices, AdWordsSession session, long campaignId)
			throws RemoteException {
		// Get the AdGroupService.
		AdGroupServiceInterface adGroupService = adWordsServices.get(session, AdGroupServiceInterface.class);

		// 広告グループ（ユーザー属性）
		AdGroup adGroup = new AdGroup();
		// 広告グループ名設定
		adGroup.setName(googleCampaignDto.getCampaignName() + "（ユーザー属性）");
		// ステータス設定
		adGroup.setStatus(AdGroupStatus.ENABLED);
		// キャンプーンID設定
		adGroup.setCampaignId(campaignId);

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
		operation.setOperator(Operator.ADD);
		AdGroupOperation[] operations = new AdGroupOperation[] { operation };

		// Add ad groups.
		AdGroupReturnValue result1 = adGroupService.mutate(operations);

		// Display new ad groups.
		for (AdGroup adGroupResult : result1.getValue()) {
			// System.out.printf("Ad group with name '%s' and ID %d was added.%n",
			// adGroupResult.getName(), adGroupResult.getId());
			newAdGroupUser = adGroupResult;
		}

		// 広告グループ（キーワード）
		// 各種設定について、上記の広告グループ（ユーザー属性）からコピー
		AdGroup adGroup2 = new AdGroup();
		adGroup2 = adGroup;
		// 広告グループ名設定
		adGroup2.setName(googleCampaignDto.getCampaignName() + "（キーワード）");
		// ステータス設定
		adGroup2.setStatus(AdGroupStatus.ENABLED);

		// Create operations.
		AdGroupOperation operation2 = new AdGroupOperation();
		operation2.setOperand(adGroup2);
		operation2.setOperator(Operator.ADD);
		AdGroupOperation[] operations2 = new AdGroupOperation[] { operation2 };

		// Add ad groups.
		AdGroupReturnValue result2 = adGroupService.mutate(operations2);

		// Display new ad groups.
		for (AdGroup adGroupResult : result2.getValue()) {
			// System.out.printf("Ad group with name '%s' and ID %d was added.%n",
			// adGroupResult.getName(), adGroupResult.getId());
			newAdGroupKeyword = adGroupResult;
			// キーワード設定
			setAdGroupCriteria(adWordsServices, session, adGroupResult.getId());
		}
	}

	public void setAdGroupCriteria(AdWordsServicesInterface adWordsServices, AdWordsSession session, long adGroupId)
			throws RemoteException {
		// Get the AdGroupCriterionService.
		AdGroupCriterionServiceInterface adGroupCriterionService = adWordsServices.get(session,
				AdGroupCriterionServiceInterface.class);

		// キーワード設定
		List<String> keywordList = CodeMasterServiceImpl.keywordNameList;

		AdGroupCriterionOperation[] operations = new AdGroupCriterionOperation[keywordList.size()];
		for (int i = 0; i < keywordList.size(); i++) {

			// Create keywords.
			Keyword keyword = new Keyword();
			keyword.setText(keywordList.get(i));
			keyword.setMatchType(KeywordMatchType.BROAD);

			// Create biddable ad group criterion.
			BiddableAdGroupCriterion keywordBiddableAdGroupCriterion = new BiddableAdGroupCriterion();
			keywordBiddableAdGroupCriterion.setAdGroupId(adGroupId);
			keywordBiddableAdGroupCriterion.setCriterion(keyword);

			// Create operations.
			AdGroupCriterionOperation keywordAdGroupCriterionOperation = new AdGroupCriterionOperation();
			keywordAdGroupCriterionOperation.setOperand(keywordBiddableAdGroupCriterion);
			keywordAdGroupCriterionOperation.setOperator(Operator.ADD);
			operations[i] = keywordAdGroupCriterionOperation;
		}

		// Add keywords.
		AdGroupCriterionReturnValue result = adGroupCriterionService.mutate(operations);

		// Display results.
		// for (AdGroupCriterion adGroupCriterionResult : result.getValue()) {
		// System.out.printf("Keyword ad group criterion with ad group ID %d, criterion
		// ID %d, " + "text '%s', and match type '%s' was added.%n",
		// adGroupCriterionResult.getAdGroupId(),
		// adGroupCriterionResult.getCriterion().getId(), ((Keyword)
		// adGroupCriterionResult.getCriterion()).getText(),
		// ((Keyword) adGroupCriterionResult.getCriterion()).getMatchType());
		// }
	}
}
