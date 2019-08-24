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
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.client.reporting.ReportingConfiguration;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.jaxb.v201809.DownloadFormat;
import com.google.api.ads.adwords.lib.jaxb.v201809.ReportDefinitionReportType;
import com.google.api.ads.adwords.lib.utils.DetailedReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponse;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.adwords.lib.utils.v201809.ReportDownloaderInterface;
import com.google.api.ads.adwords.lib.utils.v201809.ReportQuery;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.common.base.Splitter;
import com.google.common.primitives.Longs;

import jp.acepro.haishinsan.dto.google.GoogleLocationReportDto;
import jp.acepro.haishinsan.util.StringFormatter;
import lombok.extern.slf4j.Slf4j;

/**
 * This example streams the results of an ad hoc report, collecting total
 * impressions by campaign from each line. This demonstrates how you can extract
 * data from a large report without holding the entire result set in memory or
 * using files.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */

@Slf4j
public class GetLocationReport {

	public String propFileName;
	public String googleAccountId;
	public List<Long> campaignIdList = new ArrayList<Long>();
	public List<GoogleLocationReportDto> googleLocationReportDtoList = new ArrayList<GoogleLocationReportDto>();

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
		} catch (DetailedReportDownloadResponseException dre) {
			// A DetailedReportDownloadResponseException will be thrown if the HTTP status
			// code in the
			// response indicates an error occurred and the response body contains XML with
			// further
			// information, such as the fieldPath and trigger.
			System.err.printf(
					"Report was not downloaded due to a %s with errorText '%s', trigger '%s' and "
							+ "field path '%s'%n",
					dre.getClass().getSimpleName(), dre.getErrorText(), dre.getTrigger(), dre.getFieldPath());
		} catch (ReportDownloadResponseException rde) {
			// A ReportDownloadResponseException will be thrown if the HTTP status code in
			// the response
			// indicates an error occurred, but the response did not contain further
			// details.
			System.err.printf("Report was not downloaded due to: %s%n", rde);
		} catch (ReportException re) {
			// A ReportException will be thrown if the download failed due to a transport
			// layer exception.
			System.err.printf("Report was not downloaded due to transport layer exception: %s%n", re);
		} catch (IOException ioe) {
			// An IOException in this example indicates that the report's contents could not
			// be read from
			// the response.
			System.err.printf("Report was not read due to an IOException: %s%n", ioe);
		}
	}

	/**
	 * Runs the example.
	 *
	 * @param adWordsServices the services factory.
	 * @param session         the session.
	 * @throws DetailedReportDownloadResponseException if the report request failed
	 *                                                 with a detailed error from
	 *                                                 the reporting service.
	 * @throws ReportDownloadResponseException         if the report request failed
	 *                                                 with a general error from the
	 *                                                 reporting service.
	 * @throws ReportException                         if the report request failed
	 *                                                 due to a transport layer
	 *                                                 error.
	 * @throws IOException                             if the report's contents
	 *                                                 could not be read from the
	 *                                                 response.
	 */
	public void runExample(AdWordsServicesInterface adWordsServices, AdWordsSession session)
			throws ReportDownloadResponseException, ReportException, IOException {
		// Create the query.
		String[] arr = campaignIdList.stream().map(obj -> String.valueOf(obj)).toArray(String[]::new);
		LocalDate today = LocalDate.now();
		LocalDate yesterday = LocalDate.now().minusDays(1);
		ReportQuery query = new ReportQuery.Builder()
				.fields("CampaignId", "CampaignName", "Date", "CountryCriteriaId", "CityCriteriaId", "Impressions",
						"Clicks", "Cost")
				.from(ReportDefinitionReportType.GEO_PERFORMANCE_REPORT).where("CampaignId").in(arr)
				.during(yesterday, today).build();

		// Optional: Set the reporting configuration of the session to suppress header,
		// column name, or
		// summary rows in the report output. You can also configure this via your
		// ads.properties
		// configuration file. See AdWordsSession.Builder.from(Configuration) for
		// details.
		// In addition, you can set whether you want to explicitly include or exclude
		// zero impression
		// rows.
		ReportingConfiguration reportingConfiguration = new ReportingConfiguration.Builder()
				// Skip all header and summary lines since the loop below expects
				// every field to be present in each line.
				.skipReportHeader(true).skipColumnHeader(true).skipReportSummary(true)
				// Enable to include rows with zero impressions.
				// 表示回数が０の場合：
				// true : 取得する
				// false : 取得しない
				.includeZeroImpressions(false).build();
		session.setReportingConfiguration(reportingConfiguration);

		ReportDownloaderInterface reportDownloader = adWordsServices.getUtility(session,
				ReportDownloaderInterface.class);

		BufferedReader reader = null;
		try {
			// Set the property api.adwords.reportDownloadTimeout or call
			// ReportDownloader.setReportDownloadTimeout to set a timeout (in milliseconds)
			// for CONNECT and READ in report downloads.
			final ReportDownloadResponse response = reportDownloader.downloadReport(query.toString(),
					DownloadFormat.CSV);

			// Read the response as a BufferedReader.
			reader = new BufferedReader(new InputStreamReader(response.getInputStream(), UTF_8));

			// Stream the results one line at a time and perform any line-specific
			// processing.
			String line;
			Splitter splitter = Splitter.on(',');
			while ((line = reader.readLine()) != null) {
				// System.out.println(line);

				// Split the line into a list of field values.
				List<String> values = splitter.splitToList(line);

				GoogleLocationReportDto googleLocationReportDto = new GoogleLocationReportDto();
				googleLocationReportDto.setCampaignId(Longs.tryParse(values.get(0)));
				googleLocationReportDto.setCampaignName(values.get(1));
				String date = StringFormatter.dateFormat(values.get(2));
				googleLocationReportDto.setDate(date);
				googleLocationReportDto.setLocationId(Longs.tryParse(values.get(4)));
				googleLocationReportDto.setImpressions(Longs.tryParse(values.get(5)));
				googleLocationReportDto.setClicks(Longs.tryParse(values.get(6)));
				googleLocationReportDto.setCosts(Longs.tryParse(values.get(7)).doubleValue() / 1000000);

				googleLocationReportDtoList.add(googleLocationReportDto);
				log.debug("GoogleLocationReportDto : {}", googleLocationReportDto.toString());
			}

		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
}
