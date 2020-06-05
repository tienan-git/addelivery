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

import java.io.IOException;
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
import com.google.api.ads.adwords.axis.v201809.cm.Image;
import com.google.api.ads.adwords.axis.v201809.cm.ImageAd;
import com.google.api.ads.adwords.axis.v201809.cm.Media;
import com.google.api.ads.adwords.axis.v201809.cm.MediaMediaType;
import com.google.api.ads.adwords.axis.v201809.cm.MediaServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.Operator;
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
 * This example adds an image representing the ad using the MediaService and
 * then adds a responsive display ad to a given ad group. To get ad groups, run
 * GetAdGroups.java.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
public class AddImageAd {

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
			System.err.printf("Example failed due to IOException: %s%n", ioe);
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
	 * @throws IOException     if uploading an image failed.
	 */
	public void runExample(AdWordsServicesInterface adWordsServices, AdWordsSession session, long adGroupId)
			throws IOException {
		// Set current AdGroupId
		this.adGroupId = adGroupId;

		// Get the MediaService.
		MediaServiceInterface mediaService = adWordsServices.get(session, MediaServiceInterface.class);

		// Get the AdGroupAdService.
		AdGroupAdServiceInterface adGroupAdService = adWordsServices.get(session, AdGroupAdServiceInterface.class);

		List<AdGroupAdOperation> adGroupAdOperations = new ArrayList<AdGroupAdOperation>();

		for (int i = 0; i < googleCampaignDto.getImageAdImageBytesList().size(); i++) {

			byte[] data = googleCampaignDto.getImageAdImageBytesList().get(i);

			// Create a responsive display ad.
			ImageAd imageAd = new ImageAd();

			// 最終ページURL設定
			imageAd.setFinalUrls(new String[] { googleCampaignDto.getImageAdFinalPageUrl() });

			// 画像設定
			long imageMediaId = uploadImage(mediaService, data);
			Image image = new Image();
			image.setMediaId(imageMediaId);
			imageAd.setImage(image);

			// 広告名設定
			imageAd.setName(googleCampaignDto.getImageAdImageFileNameList().get(i));

			// 広告表示URL設定
			imageAd.setDisplayUrl(googleCampaignDto.getImageAdFinalPageUrl());

			// Create ad group ad for the responsive display ad.
			AdGroupAd adGroupAd = new AdGroupAd();
			adGroupAd.setAdGroupId(adGroupId);
			adGroupAd.setAd(imageAd);

			// Optional: set the status.
			adGroupAd.setStatus(AdGroupAdStatus.ENABLED);

			// Create the operation.
			AdGroupAdOperation adGroupAdOperation = new AdGroupAdOperation();
			adGroupAdOperation.setOperand(adGroupAd);
			adGroupAdOperation.setOperator(Operator.ADD);

			adGroupAdOperations.add(adGroupAdOperation);
		}

		// Make the mutate request.
		AdGroupAdReturnValue result = adGroupAdService
				.mutate(adGroupAdOperations.toArray(new AdGroupAdOperation[adGroupAdOperations.size()]));

		// Display ads.
		// Arrays.stream(result.getValue()).map(adGroupAdResult -> (ImageAd)
		// adGroupAdResult.getAd()).forEach(newAd -> System.out.printf("Image ad with ID
		// %d and final url '%s' was added.%n", newAd.getId(),
		// newAd.getFinalUrls()[0]));

	}

	/**
	 * Uploads the image from the specified {@code url} via {@code MediaService}.
	 *
	 * @return the {@code mediaId} of the uploaded image.
	 */
	private long uploadImage(MediaServiceInterface mediaService, byte[] data) throws IOException {
		// Create image.
		Image image = new Image();
		image.setType(MediaMediaType.IMAGE);
		image.setData(data);

		// Upload image.
		Image uploadedImage = (Image) mediaService.upload(new Media[] { image })[0];

		// Get upload image URL.
		if (this.adGroupId.equals(newAdGroupUser.getId())) {
			// Preview size image URL
			this.imageUrls.add(uploadedImage.getUrls(1).getValue());
			// Original size image URL
//			this.imageUrls.add(uploadedImage.getUrls(0).getValue());
		}
		return uploadedImage.getMediaId();
	}
}
