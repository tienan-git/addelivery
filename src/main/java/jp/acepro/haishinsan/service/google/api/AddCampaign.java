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
import com.google.api.ads.adwords.axis.v201809.cm.AdvertisingChannelType;
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
import com.google.api.ads.adwords.axis.v201809.cm.CampaignCriterionReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignCriterionServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignOperation;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignStatus;
import com.google.api.ads.adwords.axis.v201809.cm.GeoTargetTypeSetting;
import com.google.api.ads.adwords.axis.v201809.cm.GeoTargetTypeSettingPositiveGeoTargetType;
import com.google.api.ads.adwords.axis.v201809.cm.Language;
import com.google.api.ads.adwords.axis.v201809.cm.Location;
import com.google.api.ads.adwords.axis.v201809.cm.ManualCpmBiddingScheme;
import com.google.api.ads.adwords.axis.v201809.cm.Money;
import com.google.api.ads.adwords.axis.v201809.cm.NetworkSetting;
import com.google.api.ads.adwords.axis.v201809.cm.Operator;
import com.google.api.ads.adwords.axis.v201809.cm.Platform;
import com.google.api.ads.adwords.axis.v201809.cm.Setting;
import com.google.api.ads.adwords.axis.v201809.cm.TargetSpendBiddingScheme;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.enums.DeviceType;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.UnitPriceType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.util.CalculateUtil;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.StringFormatter;

/**
 * This example adds campaigns.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
public class AddCampaign {

	public String propFileName;
	public GoogleCampaignDto googleCampaignDto;
	public Campaign newCampaign;

	public void run() {
		AdWordsSession session;
		try {
			// Generate a refreshable OAuth2 credential.
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).fromFile(propFileName).build().generateCredential();
			// Construct an AdWordsSession.
			session = new AdWordsSession.Builder().fromFile(propFileName).withOAuth2Credential(oAuth2Credential).build();
			// 店舗AdwordsIdを設定
			session.setClientCustomerId(ContextUtil.getCurrentShop().getGoogleAccountId());
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
					if (apiError.getErrorString().equals("CampaignError.DUPLICATE_CAMPAIGN_NAME")) {
						// 該当キャンペーン名が既に登録されたため、修正してください。
						throw new BusinessException(ErrorCodeConstant.E70001);
					}
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
	 * @throws ApiException
	 *             if the API request failed with one or more service errors.
	 * @throws RemoteException
	 *             if the API request failed due to other errors.
	 */
	public void runExample(AdWordsServicesInterface adWordsServices, AdWordsSession session) throws RemoteException {
		// Get the BudgetService.
		BudgetServiceInterface budgetService = adWordsServices.get(session, BudgetServiceInterface.class);

		// 予算作成
		// Create a budget, which can be shared by multiple campaigns.
		Budget sharedBudget = new Budget();
		sharedBudget.setName(googleCampaignDto.getCampaignName() + "（予算）");
		sharedBudget.setIsExplicitlyShared(false);
		Money budgetAmount = new Money();
		Long realBudget = CalculateUtil.calRealBudget(googleCampaignDto.getBudget());
		budgetAmount.setMicroAmount(realBudget * 1000000);
		sharedBudget.setAmount(budgetAmount);
		sharedBudget.setDeliveryMethod(BudgetBudgetDeliveryMethod.STANDARD);

		BudgetOperation budgetOperation = new BudgetOperation();
		budgetOperation.setOperand(sharedBudget);
		budgetOperation.setOperator(Operator.ADD);

		// Add the budget
		Long budgetId = budgetService.mutate(new BudgetOperation[] { budgetOperation }).getValue(0).getBudgetId();

		//System.out.printf("budget with ID %d was added.%n", budgetId);

		// Get the CampaignService.
		CampaignServiceInterface campaignService = adWordsServices.get(session, CampaignServiceInterface.class);

		// Create campaign.
		Campaign campaign = new Campaign();

		// キャンプーン名設定
		campaign.setName(googleCampaignDto.getCampaignName());

		// Recommendation: Set the campaign to PAUSED when creating it to prevent
		// the ads from immediately serving. Set to ENABLED once you've added
		// targeting and the ads are ready to serve.

		// キャンプーン状態設定
		if (Flag.ON.getValue().toString().equals(ContextUtil.getCurrentShop().getSalesCheckFlag())) {
			// 営業チェックが必要有りの場合
			campaign.setStatus(CampaignStatus.PAUSED);
		} else {
			// 営業チェックが必要無しの場合
			campaign.setStatus(CampaignStatus.ENABLED);
		}
		campaign.setStatus(CampaignStatus.PAUSED);

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

		// 開始日設定
		campaign.setStartDate(StringFormatter.dateFormat(googleCampaignDto.getStartDate()));
		// 終了日設定
		campaign.setEndDate(StringFormatter.dateFormat(googleCampaignDto.getEndDate()));

		// 予算設定
		Budget budget = new Budget();
		budget.setBudgetId(budgetId);
		campaign.setBudget(budget);

		// キャンプーンタイプ設定
		switch (GoogleAdType.of(googleCampaignDto.getAdType())) {
		case RESPONSIVE:
		case IMAGE:
			campaign.setAdvertisingChannelType(AdvertisingChannelType.DISPLAY);
			break;
		case TEXT:
			campaign.setAdvertisingChannelType(AdvertisingChannelType.SEARCH);
			break;
		}

		// ネットワーク設定
		switch (GoogleAdType.of(googleCampaignDto.getAdType())) {
		case RESPONSIVE:
		case IMAGE:
			break;
		case TEXT:
			NetworkSetting networkSetting = new NetworkSetting();
			networkSetting.setTargetGoogleSearch(true);
			networkSetting.setTargetSearchNetwork(true);
			networkSetting.setTargetContentNetwork(true);
			networkSetting.setTargetPartnerSearchNetwork(false);
			campaign.setNetworkSetting(networkSetting);
			break;
		}

		// 地域設定
		GeoTargetTypeSetting geoTarget = new GeoTargetTypeSetting();
		geoTarget.setPositiveGeoTargetType(GeoTargetTypeSettingPositiveGeoTargetType.DONT_CARE);
		campaign.setSettings(new Setting[] { geoTarget });

		// Create operations.
		CampaignOperation operation = new CampaignOperation();
		operation.setOperand(campaign);
		operation.setOperator(Operator.ADD);

		CampaignOperation[] operations = new CampaignOperation[] { operation };

		// Add campaigns.
		CampaignReturnValue result = campaignService.mutate(operations);

		// Display the results.
		for (Campaign newCampaign : result.getValue()) {
			//System.out.printf("campaign with name '%s' and ID %d was added.%n", newCampaign.getName(), newCampaign.getId());
			this.newCampaign = newCampaign;

			// Optional: Set the campaign's location and language and device targeting
			setCampaignTargetingCriteria(newCampaign, adWordsServices, session);
		}
	}

	/** Sets the campaign's targeting criteria. */
	private void setCampaignTargetingCriteria(Campaign campaign, AdWordsServicesInterface adWordsServices, AdWordsSession session) throws ApiException, RemoteException {
		// Get the CampaignCriterionService.
		CampaignCriterionServiceInterface campaignCriterionService = adWordsServices.get(session, CampaignCriterionServiceInterface.class);
		List<CampaignCriterionOperation> operations = new ArrayList<>();

		// 地域設定
		for (Long locationId : googleCampaignDto.getLocationList()) {
			// Create location
			Location location = new Location();
			location.setId(locationId);
			// Create criterion
			CampaignCriterion campaignCriterion = new CampaignCriterion();
			campaignCriterion.setCampaignId(newCampaign.getId());
			campaignCriterion.setCriterion(location);
			// Create operation
			CampaignCriterionOperation operation = new CampaignCriterionOperation();
			operation.setOperand(campaignCriterion);
			operation.setOperator(Operator.ADD);
			operations.add(operation);
		}

		// 言語設定
		// Create language
		Language japanese = new Language();
		japanese.setId(1005L);
		// Create criterion
		CampaignCriterion campaignCriterion = new CampaignCriterion();
		campaignCriterion.setCampaignId(newCampaign.getId());
		campaignCriterion.setCriterion(japanese);
		// Create operation
		CampaignCriterionOperation operation = new CampaignCriterionOperation();
		operation.setOperand(campaignCriterion);
		operation.setOperator(Operator.ADD);
		operations.add(operation);

		// デバイス設定
		switch (DeviceType.of(googleCampaignDto.getDeviceType())) {
		case ALL:
			break;
		case PC:
			// Create mobile
			Platform mobile = new Platform();
			mobile.setId(30001L);
			/// Create criterion with modified bid
			CampaignCriterion campaignCriterion1 = new CampaignCriterion();
			campaignCriterion1.setCampaignId(newCampaign.getId());
			campaignCriterion1.setCriterion(mobile);
			campaignCriterion1.setBidModifier(0.0);
			// Create operation
			CampaignCriterionOperation operation1 = new CampaignCriterionOperation();
			operation1.setOperand(campaignCriterion1);
			operation1.setOperator(Operator.SET);
			operations.add(operation1);

			// Create tablet
			Platform tablet = new Platform();
			tablet.setId(30002L);
			// Create criterion with modified bid
			CampaignCriterion campaignCriterion2 = new CampaignCriterion();
			campaignCriterion2.setCampaignId(newCampaign.getId());
			campaignCriterion2.setCriterion(tablet);
			campaignCriterion2.setBidModifier(0.0);
			// Create operation
			CampaignCriterionOperation operation2 = new CampaignCriterionOperation();
			operation2.setOperand(campaignCriterion2);
			operation2.setOperator(Operator.SET);
			operations.add(operation2);
			break;
		case MOBILE:
			// Create PC
			Platform pc = new Platform();
			pc.setId(30000L);
			// Create criterion with modified bid
			CampaignCriterion campaignCriterion3 = new CampaignCriterion();
			campaignCriterion3.setCampaignId(newCampaign.getId());
			campaignCriterion3.setCriterion(pc);
			campaignCriterion3.setBidModifier(0.0);
			// Create operation.
			CampaignCriterionOperation operation3 = new CampaignCriterionOperation();
			operation3.setOperand(campaignCriterion3);
			operation3.setOperator(Operator.SET);
			operations.add(operation3);
			break;
		}

		// Set the campaign targets.
		CampaignCriterionReturnValue returnValue = campaignCriterionService.mutate(operations.toArray(new CampaignCriterionOperation[operations.size()]));

//		if (returnValue != null && returnValue.getValue() != null) {
//			// Display added campaign targets.
//			for (CampaignCriterion riterion : returnValue.getValue()) {
//				System.out.printf("Campaign criteria of type '%s' and ID %d was added.%n", riterion.getCriterion().getCriterionType(), riterion.getCriterion().getId());
//			}
//		}
	}
}
