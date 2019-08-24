package jp.acepro.haishinsan.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.dto.S3UploadDto;
import jp.acepro.haishinsan.util.AwsClientInitialization;
import jp.acepro.haishinsan.util.DataConvertUtil;
import jp.acepro.haishinsan.util.ImageUtil;

@Service
public class S3ServiceImpl implements S3Service {

	@Autowired
	DataConvertUtil dataConvertUtil;

	@Autowired
	ImageUtil imageUtil;

	@Autowired
	AwsClientInitialization awsClient;

	@Autowired
	ApplicationProperties applicationProperties;

	/*************
	 * イメージをS3にアップロードする
	 * 
	 * @param S3UploadDto
	 * 
	 *************/
	@Override
	public void uploadImageToS3(S3UploadDto s3UploadDto) {

		// MultipartFileをBase64コードに変更する
		String base64Str = "";
		try {
			base64Str = imageUtil.getImageBytes(s3UploadDto.getMultipartFile(), s3UploadDto.getMediaType());
		} catch (IOException e) {
			// TODO S3 Base64コード変更するときの異常処理
			e.printStackTrace();
		}

		// AWSアカウント情報を取得する
		AmazonS3 awsS3Client = awsClient.getAwsS3Client();
		// S3にアップロードする
		saveBase64StrToS3(base64Str, s3UploadDto.getName(), awsS3Client);
	}

	/*************
	 * S3からファイルのURLをダウンロードする
	 * 
	 * @param imageName
	 * @return String
	 * @throws IOException
	 * 
	 *************/
	public String getFileFromS3(String imageName) throws IOException {

		// AWSアカウント情報を取得する
		AmazonS3 awsS3Client = awsClient.getAwsS3Client();

		/************* リクエストを取得 *************/
		GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(applicationProperties.getBucket(),
				imageName);
		/************* S3から保存した部品のURLを取得 *************/
		URL url = awsS3Client.generatePresignedUrl(urlRequest);
		return String.valueOf(url);
	}

	/*************
	 * 部品をS3へ保存する
	 * 
	 * @param base64Str
	 * @param name
	 * @param awsS3Client
	 * 
	 *************/
	private void saveBase64StrToS3(String base64Str, String name, AmazonS3 awsS3Client) {

		// applicationPropertiesから取得
		String bucket = applicationProperties.getBucket();
		/************* Base64StrをInputStreamに変更する *************/
		InputStream fis = dataConvertUtil.convertToInputStream(base64Str);
		/************* リクエストを取得 *************/
		PutObjectRequest request = new PutObjectRequest(bucket, name, fis, null);
		/************* 部品をS3へ保存する *************/
		awsS3Client.putObject(request);
	}

}
