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
import com.google.api.ads.adwords.axis.v201809.cm.ApiError;
import com.google.api.ads.adwords.axis.v201809.cm.ApiException;
import com.google.api.ads.adwords.axis.v201809.cm.BiddingStrategyConfiguration;
import com.google.api.ads.adwords.axis.v201809.cm.BiddingStrategyType;
import com.google.api.ads.adwords.axis.v201809.cm.Budget;
import com.google.api.ads.adwords.axis.v201809.cm.BudgetBudgetDeliveryMethod;
import com.google.api.ads.adwords.axis.v201809.cm.BudgetOperation;
import com.google.api.ads.adwords.axis.v201809.cm.BudgetServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.Campaign;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignCriterion;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignCriterionOperation;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignCriterionPage;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignCriterionReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignCriterionServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignOperation;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignStatus;
import com.google.api.ads.adwords.axis.v201809.cm.GeoTargetTypeSetting;
import com.google.api.ads.adwords.axis.v201809.cm.GeoTargetTypeSettingPositiveGeoTargetType;
import com.google.api.ads.adwords.axis.v201809.cm.Location;
import com.google.api.ads.adwords.axis.v201809.cm.ManualCpmBiddingScheme;
import com.google.api.ads.adwords.axis.v201809.cm.Money;
import com.google.api.ads.adwords.axis.v201809.cm.Operator;
import com.google.api.ads.adwords.axis.v201809.cm.Selector;
import com.google.api.ads.adwords.axis.v201809.cm.Setting;
import com.google.api.ads.adwords.axis.v201809.cm.TargetSpendBiddingScheme;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.CampaignCriterionField;
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
import jp.acepro.haishinsan.util.CalculateUtil;
import jp.acepro.haishinsan.util.ContextUtil;

/**
 * This example updates a campaign by setting the status to PAUSED. To get
 * campaigns, run GetCampaigns.java.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
public class UpdateCampaign {

	private static final int PAGE_SIZE = 100;

	public String propFileName;
	public String googleAccountId;
	public Integer ratio;
	public List<CampaignCriterion> campaignCriterionList = new ArrayList<CampaignCriterion>();
	public GoogleCampaignDto googleCampaignDto;

	public void run(Long campaignId, String switchFlag) {
		AdWordsSession session;
		try {
			// Generate a refreshable OAuth2 credential.
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).fromFile(propFileName).build().generateCredential();

			// Construct an AdWordsSession.
			session = new AdWordsSession.Builder().fromFile(propFileName).withOAuth2Credential(oAuth2Credential).build();
			// 店舗AdwordsIdを設定
			session.setClientCustomerId(googleAccountId);
		} catch (ConfigurationLoadException cle) {
			System.err.printf("Failed to load configuration from the %s file. Exception: %s%n", DEFAULT_CONFIGURATION_FILENAME, cle);
			return;
		} catch (ValidationException ve) {
			System.err.printf("Invalid configuration in the %s file. Exception: %s%n", DEFAULT_CONFIGURATION_FILENAME, ve);
			return;
		} catch (OAuthException oe) {
			System.err.printf("Failed to create OAuth credentials. Check OAuth settings in the %s file. " + "Exception: %s%n", DEFAULT_CONFIGURATION_FILENAME, oe);
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
	 * @param adWordsServices
	 *            the services factory.
	 * @param session
	 *            the session.
	 * @param campaignId
	 *            the ID of the campaign to update.
	 * @throws ApiException
	 *             if the API request failed with one or more service errors.
	 * @throws RemoteException
	 *             if the API request failed due to other errors.
	 */
	public void runExample(AdWordsServicesInterface adWordsServices, AdWordsSession session, Long campaignId, String switchFlag) throws RemoteException {
		// Get the CampaignService.
		CampaignCriterionServiceInterface campaignCriterionService = adWordsServices.get(session, CampaignCriterionServiceInterface.class);

		int offset = 0;
		String[] arr = { String.valueOf(campaignId) };
		// Create selector.
		SelectorBuilder builder = new SelectorBuilder();
		Selector selector = builder
				.fields(CampaignCriterionField.CampaignId, CampaignCriterionField.Id, CampaignCriterionField.CriteriaType, CampaignCriterionField.LocationName, CampaignCriterionField.BidModifier)
				.in(CampaignCriterionField.CriteriaType, "LOCATION")
				.in(CampaignCriterionField.CampaignId, arr)
				.offset(0)
				.limit(PAGE_SIZE)
				.build();

		CampaignCriterionPage page = null;
		do {
			page = campaignCriterionService.get(selector);

			if (page.getEntries() != null) {
				// Display campaigns.
				for (CampaignCriterion campaignCriterion : page.getEntries()) {
					// System.out.printf("Campaign criterion with campaign ID %d, criterion ID %d, "
					// + "and type '%s' was found.%n", campaignCriterion.getCampaignId(),
					// campaignCriterion.getCriterion().getId(),
					// campaignCriterion.getCriterion().getCriterionType());
					campaignCriterionList.add(campaignCriterion);
				}
			} else {
				//System.out.println("No campaign criteria were found.");
			}
			offset += PAGE_SIZE;
			selector = builder.increaseOffsetBy(PAGE_SIZE).build();
		} while (offset < page.getTotalNumEntries());
		
		List<CampaignCriterionOperation> campaignCriterionOperations = new ArrayList<>();
        for(CampaignCriterion campaignCriterion : campaignCriterionList) {
    		CampaignCriterionOperation campaignCriterionOperation = new CampaignCriterionOperation();
    		campaignCriterionOperation.setOperand(campaignCriterion);
    		campaignCriterionOperation.setOperator(Operator.REMOVE);
    		campaignCriterionOperations.add(campaignCriterionOperation);      	
        }
		// REMOVE the campaign targets.
		CampaignCriterionReturnValue returnRemoveTargetsValue = campaignCriterionService.mutate(campaignCriterionOperations.toArray(new CampaignCriterionOperation[campaignCriterionOperations.size()]));

		// 地域設定
		List<CampaignCriterionOperation> campaignCriterionOperationsNew = new ArrayList<>();

		for (Long locationId : googleCampaignDto.getLocationList()) {
			// Create location
			Location location = new Location();
			location.setId(locationId);
			// Create criterion
			CampaignCriterion campaignCriterion = new CampaignCriterion();
			campaignCriterion.setCampaignId(campaignId);
			campaignCriterion.setCriterion(location);
			// Create operation
			CampaignCriterionOperation campaignCriterionOperation = new CampaignCriterionOperation();
			campaignCriterionOperation.setOperand(campaignCriterion);
			campaignCriterionOperation.setOperator(Operator.ADD);
			campaignCriterionOperationsNew.add(campaignCriterionOperation);
		}
		// Set the campaign targets.
		CampaignCriterionReturnValue returnSetTargetsValue = campaignCriterionService.mutate(campaignCriterionOperationsNew.toArray(new CampaignCriterionOperation[campaignCriterionOperationsNew.size()]));


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
		// 単価設定
		switch (GoogleAdType.of(googleCampaignDto.getAdType())) {
		case RESPONSIVE:
		case IMAGE:
			// クリック重視
			if (googleCampaignDto.getUnitPriceType().equals(UnitPriceType.CLICK.getValue()) || googleCampaignDto.getUnitPriceType() == null || googleCampaignDto.getUnitPriceType().isEmpty()) {
				BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
				biddingStrategyConfiguration.setBiddingStrategyType(BiddingStrategyType.TARGET_SPEND);
				TargetSpendBiddingScheme targetSpendBiddingScheme = new TargetSpendBiddingScheme();
				
				// 単価設定
				Double averageClickUnitPriceDouble = CodeMasterServiceImpl.googleAreaUnitPriceClickList.stream().filter(obj -> googleCampaignDto.getLocationList().contains(obj.getFirst())).mapToInt(obj -> obj.getSecond()).average().getAsDouble();
				Long averageUnitPrice = Math.round(averageClickUnitPriceDouble);
				Money cpcBidMoney = new Money();
				cpcBidMoney.setMicroAmount(averageUnitPrice * 1000000);
				targetSpendBiddingScheme.setBidCeiling(cpcBidMoney);
				
				biddingStrategyConfiguration.setBiddingScheme(targetSpendBiddingScheme);
				campaign.setBiddingStrategyConfiguration(biddingStrategyConfiguration);
				break;
			}
			// 表示重視
			if (googleCampaignDto.getUnitPriceType().equals(UnitPriceType.DISPLAY.getValue())) {
				BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
				biddingStrategyConfiguration.setBiddingStrategyType(BiddingStrategyType.MANUAL_CPM);
				ManualCpmBiddingScheme manualCpmBiddingScheme = new ManualCpmBiddingScheme();
				
				biddingStrategyConfiguration.setBiddingScheme(manualCpmBiddingScheme);
				campaign.setBiddingStrategyConfiguration(biddingStrategyConfiguration);
				break;
			}
			break;
		case TEXT:
			BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
			biddingStrategyConfiguration.setBiddingStrategyType(BiddingStrategyType.TARGET_SPEND);

			// You can optionally provide a bidding scheme in place of the type.
			TargetSpendBiddingScheme targetSpendBiddingScheme = new TargetSpendBiddingScheme();

			Money biddingAmount = new Money();
			biddingAmount.setMicroAmount(50_000_000L);

			targetSpendBiddingScheme.setBidCeiling(biddingAmount);

			biddingStrategyConfiguration.setBiddingScheme(targetSpendBiddingScheme);

			campaign.setBiddingStrategyConfiguration(biddingStrategyConfiguration);
			break;
		}

		// Get the BudgetService.
		BudgetServiceInterface budgetService = adWordsServices.get(session, BudgetServiceInterface.class);

		// 予算作成
		// Create a budget, which can be shared by multiple campaigns.
		Budget sharedBudget = new Budget();
		sharedBudget.setIsExplicitlyShared(false);
		Money budgetAmount = new Money();
		Long realBudget = CalculateUtil.calRealBudgetWithShopRatio(googleCampaignDto.getBudget(), ratio);
		budgetAmount.setMicroAmount(realBudget * 1000000);
		sharedBudget.setAmount(budgetAmount);
		sharedBudget.setDeliveryMethod(BudgetBudgetDeliveryMethod.STANDARD);

		BudgetOperation budgetOperation = new BudgetOperation();
		budgetOperation.setOperand(sharedBudget);
		budgetOperation.setOperator(Operator.ADD);

		// Add the budget
		Long budgetId = budgetService.mutate(new BudgetOperation[] { budgetOperation }).getValue(0).getBudgetId();

		// 予算設定
		Budget budget = new Budget();
		budget.setBudgetId(budgetId);
		campaign.setBudget(budget);

		// Create operations.
		CampaignOperation operation = new CampaignOperation();
		operation.setOperand(campaign);
		operation.setOperator(Operator.SET);

		CampaignOperation[] operations = new CampaignOperation[] { operation };

		// Update campaign.
		CampaignReturnValue result = campaignService.mutate(operations);

	}
}
