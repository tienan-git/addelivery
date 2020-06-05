//Copyright 2018 Google LLC
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//  https://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package jp.acepro.haishinsan.service.google.api;

import static com.google.api.ads.common.lib.utils.Builder.DEFAULT_CONFIGURATION_FILENAME;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.google.api.ads.adwords.axis.v201809.cm.AssetLink;
import com.google.api.ads.adwords.axis.v201809.cm.AssetOperation;
import com.google.api.ads.adwords.axis.v201809.cm.AssetReturnValue;
import com.google.api.ads.adwords.axis.v201809.cm.AssetServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.DisplayAdFormatSetting;
import com.google.api.ads.adwords.axis.v201809.cm.ImageAsset;
import com.google.api.ads.adwords.axis.v201809.cm.MultiAssetResponsiveDisplayAd;
import com.google.api.ads.adwords.axis.v201809.cm.Operator;
import com.google.api.ads.adwords.axis.v201809.cm.TextAsset;
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
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.util.ContextUtil;

/**
 * This example adds a responsive display ad (MultiAssetResponsiveDisplayAd) to
 * an ad group. Image assets are uploaded using AssetService. To get ad groups,
 * run GetAdGroups.java.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
public class AddMultiAssetResponsiveDisplayAd {

	public String propFileName;
	public GoogleCampaignDto googleCampaignDto;
	public AdGroup newAdGroupUser;
	public AdGroup newAdGroupKeyword;
	public List<String> imageUrls = new ArrayList<String>();
	private Long adGroupId;

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
					if (apiError.getApiErrorType().equals("ImageError")) {
						// Google広告の画像ポリシーを確認してください。
						throw new BusinessException(ErrorCodeConstant.E70008);
					}
				}
			}
		} catch (RemoteException re) {
			System.err.printf("Request failed unexpectedly due to RemoteException: %s%n", re);
		} catch (IOException ioe) {
			System.err.printf("Example failed unexpectedly due to IOException: %s%n", ioe);
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
	 * @throws IOException     if unable to retrieve an image from a URL.
	 */
	public void runExample(AdWordsServicesInterface adWordsServices, AdWordsSession session, long adGroupId)
			throws IOException {
		// Set current AdGroupId
		this.adGroupId = adGroupId;

		// Get the AdGroupAdService.
		AdGroupAdServiceInterface adGroupAdService = adWordsServices.get(session, AdGroupAdServiceInterface.class);

		List<AdGroupAdOperation> operations = new ArrayList<>();

		MultiAssetResponsiveDisplayAd ad = new MultiAssetResponsiveDisplayAd();
		// 短い広告見出し設定（最大５個）
		List<AssetLink> headlines = new ArrayList<>();
		headlines.add(createAssetLinkForText(googleCampaignDto.getResAdShortTitle()));
//		headlines.add(createAssetLinkForText("Travel to Jupiter"));
//		headlines.add(createAssetLinkForText("Travel to Pluto"));
//		headlines.add(createAssetLinkForText("Experience the Stars"));
		ad.setHeadlines(headlines.toArray(new AssetLink[0]));
		// 説明文（最大５個）
		List<AssetLink> descriptions = new ArrayList<>();
		descriptions.add(createAssetLinkForText(googleCampaignDto.getResAdDescription()));
//		descriptions.add(createAssetLinkForText("See the planet in style."));
		ad.setDescriptions(descriptions.toArray(new AssetLink[0]));
		// 会社名
		ad.setBusinessName(ContextUtil.getCurrentShop().getShopName());
		// 長い広告見出し設定
		ad.setLongHeadline(createAssetLinkForText(googleCampaignDto.getResAdShortTitle()));

		// This ad format does not allow the creation of an image asset by setting the
		// asset.imageData
		// field. An image asset must first be created using the AssetService, and
		// asset.assetId must be
		// populated when creating the ad.
		// 非正方形イメージ
		ad.setMarketingImages(new AssetLink[] { createAssetLinkForImageAsset(
				uploadImageAsset(adWordsServices, session, googleCampaignDto.getResAdImageBytesList().get(0))) });
		// 正方形イメージ
		AssetLink[] assetLinks = new AssetLink[] { createAssetLinkForImageAsset(
				uploadImageAsset(adWordsServices, session, googleCampaignDto.getResAdImageBytesList().get(1))) };
		ad.setSquareMarketingImages(assetLinks);
		// ロゴイメージ
		ad.setLogoImages(assetLinks);
		// 最終ページURL
		ad.setFinalUrls(new String[] { googleCampaignDto.getResAdFinalPageUrl() });

//		// Optional: set call to action text.
//		ad.setCallToActionText("Shop Now");
//
//		// Set color settings using hexadecimal values. Set allowFlexibleColor to false
//		// if you want
//		// your ads to render by always using your colors strictly.
//		ad.setMainColor("#0000ff");
//		ad.setAccentColor("#ffff00");
//		ad.setAllowFlexibleColor(false);
//
		// Set the format setting that the ad will be served in.
		ad.setFormatSetting(DisplayAdFormatSetting.NON_NATIVE);
//
//		// Optional: Set dynamic display ad settings, composed of landscape logo image,
//		// promotion text,
//		// and price prefix.
//		ad.setDynamicSettingsPricePrefix("as low as");
//		ad.setDynamicSettingsPromoText("Free shipping!");

		// Create ad group ad.
		AdGroupAd adGroupAd = new AdGroupAd();
		adGroupAd.setAdGroupId(adGroupId);
		adGroupAd.setAd(ad);

		// Optional: set the status.
		adGroupAd.setStatus(AdGroupAdStatus.ENABLED);

		// Create the operation.
		AdGroupAdOperation adGroupAdOperation = new AdGroupAdOperation();
		adGroupAdOperation.setOperand(adGroupAd);
		adGroupAdOperation.setOperator(Operator.ADD);

		operations.add(adGroupAdOperation);

		// Add ad.
		AdGroupAdReturnValue result = adGroupAdService
				.mutate(operations.toArray(new AdGroupAdOperation[operations.size()]));

		Arrays.stream(result.getValue()).map(adGroupAdResult -> (MultiAssetResponsiveDisplayAd) adGroupAdResult.getAd())
				.forEach(newAd -> System.out.printf(
						"New responsive display ad with ID %d and long headline '%s' was added.%n", newAd.getId(),
						((TextAsset) newAd.getLongHeadline().getAsset()).getAssetText()));
	}

	/**
	 * Creates an {@link AssetLink} containing a {@link TextAsset} with the
	 * specified string.
	 *
	 * @param text the text for the text asset.
	 * @return a new {@link AssetLink}
	 */
	private static AssetLink createAssetLinkForText(String text) {
		AssetLink assetLink = new AssetLink();
		TextAsset textAsset = new TextAsset();
		textAsset.setAssetText(text);
		assetLink.setAsset(textAsset);
		return assetLink;
	}

	/**
	 * Creates an {@link AssetLink} containing a {@link ImageAsset} with the
	 * specified asset ID.
	 *
	 * @param assetId ID of the image asset.
	 * @return a new {@link AssetLink}
	 */
	private static AssetLink createAssetLinkForImageAsset(long assetId) {
		AssetLink assetLink = new AssetLink();
		ImageAsset imageAsset = new ImageAsset();
		imageAsset.setAssetId(assetId);
		assetLink.setAsset(imageAsset);
		return assetLink;
	}

	/**
	 * Creates and uploads an {@link ImageAsset} for the specified URL.
	 *
	 * @return the ID of the {@link ImageAsset}.
	 * @throws IOException if unable to read the image from the specified URL.
	 */
	private long uploadImageAsset(AdWordsServicesInterface adWordsServices, AdWordsSession session, byte[] bytesFile)
			throws IOException {

		AssetServiceInterface assetService = adWordsServices.get(session, AssetServiceInterface.class);

		// Create the image asset.
		ImageAsset image = new ImageAsset();
		// Optional: Provide a unique friendly name to identify your asset. If you
		// specify the assetName
		// field, then both the asset name and the image being uploaded should be
		// unique, and should not
		// match another ACTIVE asset in this customer account.
		// image.setAssetName("Image asset #" + System.currentTimeMillis());
//		image.setImageData(com.google.api.ads.common.lib.utils.Media.getMediaDataFromUrl(url));
		image.setImageData(bytesFile);

		// Create the operation.
		AssetOperation operation = new AssetOperation();
		operation.setOperator(Operator.ADD);
		operation.setOperand(image);

		// Create the asset and return the ID.
		AssetReturnValue result = assetService.mutate(new AssetOperation[] { operation });

		// Get upload image URL.
		if (this.adGroupId.equals(newAdGroupUser.getId())) {
			ImageAsset imageAsset = (ImageAsset) result.getValue(0);
			// Original size image URL
			this.imageUrls.add((imageAsset.getFullSizeInfo().getImageUrl()));
		}
		return result.getValue(0).getAssetId();
	}
}