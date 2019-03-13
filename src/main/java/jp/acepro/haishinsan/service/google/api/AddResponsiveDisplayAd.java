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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;

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
import com.google.api.ads.adwords.axis.v201809.cm.Media;
import com.google.api.ads.adwords.axis.v201809.cm.MediaMediaType;
import com.google.api.ads.adwords.axis.v201809.cm.MediaServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.Operator;
import com.google.api.ads.adwords.axis.v201809.cm.ResponsiveDisplayAd;
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
import lombok.extern.slf4j.Slf4j;

/**
 * This example adds an image representing the ad using the MediaService and
 * then adds a responsive display ad to a given ad group. To get ad groups, run
 * GetAdGroups.java.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
@Slf4j
public class AddResponsiveDisplayAd {

	public String propFileName;
	public GoogleCampaignDto googleCampaignDto;
	public AdGroup newAdGroupUser;
	public AdGroup newAdGroupKeyword;

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
	 * @param adWordsServices
	 *            the services factory.
	 * @param session
	 *            the session.
	 * @param adGroupId
	 *            the ID of the ad group where the ad will be created.
	 * @throws ApiException
	 *             if the API request failed with one or more service errors.
	 * @throws RemoteException
	 *             if the API request failed due to other errors.
	 * @throws IOException
	 *             if uploading an image failed.
	 */
	public void runExample(AdWordsServicesInterface adWordsServices, AdWordsSession session, long adGroupId) throws IOException {
		// Get the MediaService.
		MediaServiceInterface mediaService = adWordsServices.get(session, MediaServiceInterface.class);

		// Get the AdGroupAdService.
		AdGroupAdServiceInterface adGroupAdService = adWordsServices.get(session, AdGroupAdServiceInterface.class);

		// Create a responsive display ad.
		ResponsiveDisplayAd responsiveDisplayAd = new ResponsiveDisplayAd();

		// This ad format does not allow the creation of an image using the
		// Image.data field. An image must first be created using the MediaService,
		// and Image.mediaId must be populated when creating the ad.
		// 画像設定
		for (byte[] data : googleCampaignDto.getResAdImageDateList()) {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
			int width = image.getWidth();
			int height = image.getHeight();
			if (width == height) {
				log.debug("正方形イメージ：{}", image.toString());
				long squareMarketingImageMediaId = uploadImage(mediaService, data);
				Image squareMarketingImage = new Image();
				squareMarketingImage.setMediaId(squareMarketingImageMediaId);
				responsiveDisplayAd.setSquareMarketingImage(squareMarketingImage);
			} else {
				log.debug("非正方形イメージ：{}", image.toString());
				long marketingImageMediaId = uploadImage(mediaService, data);
				Image marketingImage = new Image();
				marketingImage.setMediaId(marketingImageMediaId);
				responsiveDisplayAd.setMarketingImage(marketingImage);
			}
		}

		// 短い広告見出し設定
		responsiveDisplayAd.setShortHeadline(googleCampaignDto.getResAdShortTitle());
		// 長い広告見出し設定
		responsiveDisplayAd.setLongHeadline(googleCampaignDto.getResAdShortTitle());
		// 説明文
		responsiveDisplayAd.setDescription(googleCampaignDto.getResAdDescription());
		// 会社名
		responsiveDisplayAd.setBusinessName(ContextUtil.getCurrentShop().getShopName());
		// 最終ページURL
		responsiveDisplayAd.setFinalUrls(new String[] { googleCampaignDto.getResAdFinalPageUrl() });

		// Create ad group ad for the responsive display ad.
		AdGroupAd adGroupAd = new AdGroupAd();
		adGroupAd.setAdGroupId(adGroupId);
		adGroupAd.setAd(responsiveDisplayAd);

		// Optional: set the status.
		adGroupAd.setStatus(AdGroupAdStatus.ENABLED);

		// Create the operation.
		AdGroupAdOperation adGroupAdOperation = new AdGroupAdOperation();
		adGroupAdOperation.setOperand(adGroupAd);
		adGroupAdOperation.setOperator(Operator.ADD);

		// Make the mutate request.
		AdGroupAdReturnValue result = adGroupAdService.mutate(new AdGroupAdOperation[] { adGroupAdOperation });

		// Display ads.
		//Arrays.stream(result.getValue()).map(adGroupAdResult -> (ResponsiveDisplayAd) adGroupAdResult.getAd()).forEach(newAd -> System.out.printf("Responsive display ad with ID %d and short headline '%s' was added.%n", newAd.getId(), newAd.getShortHeadline()));
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
		return uploadedImage.getMediaId();
	}
}
