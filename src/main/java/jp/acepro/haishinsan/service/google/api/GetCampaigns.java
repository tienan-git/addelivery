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
import com.google.api.ads.adwords.axis.v201809.cm.Budget;
import com.google.api.ads.adwords.axis.v201809.cm.BudgetPage;
import com.google.api.ads.adwords.axis.v201809.cm.BudgetServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.Campaign;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignCriterion;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignCriterionPage;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignCriterionServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignPage;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.Selector;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.BudgetField;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.CampaignCriterionField;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.CampaignField;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;

import jp.acepro.haishinsan.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * This example gets all campaigns. To add a campaign, run AddCampaign.java.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
@Slf4j
public class GetCampaigns {

	private static final int PAGE_SIZE = 100;

	public String propFileName;
	public String runOption;
	public List<Campaign> campaignList = new ArrayList<Campaign>();
	public List<CampaignCriterion> campaignCriterionList = new ArrayList<CampaignCriterion>();
	public List<Budget> budgetList = new ArrayList<Budget>();
	public List<Long> campaignIdList = new ArrayList<Long>();

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
			if (runOption.equals("DETAIL")) {
				// キャンペーン情報取得
				runExample1(adWordsServices, session);
				// キャンペーンターゲット情報取得
				runExample2(adWordsServices, session);
				// キャンペーン予算情報取得
				runExample3(adWordsServices, session);
			} 
			if (runOption.equals("INFO")) {
				// キャンペーン情報取得
				runExample1(adWordsServices, session);
			}
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
	 * @throws ApiException
	 *             if the API request failed with one or more service errors.
	 * @throws RemoteException
	 *             if the API request failed due to other errors.
	 */
	// キャンペーン基本情報取得
	public void runExample1(AdWordsServicesInterface adWordsServices, AdWordsSession session) throws RemoteException {
		// Get the CampaignService.
		CampaignServiceInterface campaignService = adWordsServices.get(session, CampaignServiceInterface.class);

		int offset = 0;
		String[] arr = campaignIdList.stream().map(obj -> String.valueOf(obj)).toArray(String[]::new);

		// Create selector.
		SelectorBuilder builder = new SelectorBuilder();
		Selector selector = builder
				.fields(CampaignField.Status, CampaignField.Id, CampaignField.Name, CampaignField.StartDate, CampaignField.EndDate, CampaignField.BudgetId)
				.orderDescBy(CampaignField.Id)
				.offset(offset)
				.limit(PAGE_SIZE)
				.in(CampaignField.Id, arr)
				.in(CampaignField.Status, "ENABLED", "PAUSED")
				.build();

		CampaignPage page;
		do {
			// Get all campaigns.
			page = campaignService.get(selector);

			// Display campaigns.
			if (page.getEntries() != null) {
				for (Campaign campaign : page.getEntries()) {
					// System.out.printf("Campaign with name '%s' and ID %d was found.%n",
					// campaign.getName(),campaign.getId());
					log.debug("Campaign : {}" + campaign.toString());
					campaignList.add(campaign);
				}
			} else {
				//System.out.println("No campaigns were found.");
			}

			offset += PAGE_SIZE;
			selector = builder.increaseOffsetBy(PAGE_SIZE).build();
		} while (offset < page.getTotalNumEntries());
	}

	// キャンペーンターゲット情報取得（地域、デバイス）
	public void runExample2(AdWordsServicesInterface adWordsServices, AdWordsSession session) throws RemoteException {
		// Get the CampaignService.
		CampaignCriterionServiceInterface campaignCriterionService = adWordsServices.get(session, CampaignCriterionServiceInterface.class);

		int offset = 0;
		String[] arr = campaignIdList.stream().map(obj -> String.valueOf(obj)).toArray(String[]::new);
		// Create selector.
		SelectorBuilder builder = new SelectorBuilder();
		Selector selector = builder
				.fields(CampaignCriterionField.CampaignId, CampaignCriterionField.Id, CampaignCriterionField.CriteriaType, CampaignCriterionField.PlatformName, CampaignCriterionField.LocationName, CampaignCriterionField.BidModifier)
				.in(CampaignCriterionField.CriteriaType, "LOCATION", "PLATFORM")
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
					log.debug("campaignCriterion : {}", campaignCriterion.toString());
					campaignCriterionList.add(campaignCriterion);
				}
			} else {
				//System.out.println("No campaign criteria were found.");
			}
			offset += PAGE_SIZE;
			selector = builder.increaseOffsetBy(PAGE_SIZE).build();
		} while (offset < page.getTotalNumEntries());
	}

	// キャンペーン予算情報取得
	public void runExample3(AdWordsServicesInterface adWordsServices, AdWordsSession session) throws RemoteException {
		// Get the CampaignService.
		BudgetServiceInterface budgetService = adWordsServices.get(session, BudgetServiceInterface.class);

		int offset = 0;

		String budgetId = String.valueOf(campaignList.get(0).getBudget().getBudgetId());
		// Create selector.
		SelectorBuilder builder = new SelectorBuilder();
		Selector selector = builder
				.fields(BudgetField.BudgetId, BudgetField.Amount)
				.orderAscBy(BudgetField.BudgetId)
				.offset(offset)
				.limit(PAGE_SIZE)
				.in(BudgetField.BudgetId, budgetId)
				.build();

		BudgetPage page;
		do {
			// Get all campaigns.
			page = budgetService.get(selector);

			// Display campaigns.
			if (page.getEntries() != null) {
				for (Budget budget : page.getEntries()) {
					// System.out.printf("Campaign with name '%s' and ID %d was found.%n",
					// campaign.getName(),
					// campaign.getId());
					log.debug("budget : {}", budget.toString());
					budgetList.add(budget);
				}
			} else {
				//System.out.println("No budgets were found.");
			}

			offset += PAGE_SIZE;
			selector = builder.increaseOffsetBy(PAGE_SIZE).build();
		} while (offset < page.getTotalNumEntries());
	}
}
