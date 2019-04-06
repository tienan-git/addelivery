package jp.acepro.haishinsan.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import jp.acepro.haishinsan.ApplicationProperties;

@Component
public class AwsClientInitialization {

	@Autowired
	ApplicationProperties applicationProperties;

	private AmazonS3 awsS3Client;

	public AmazonS3 getAwsS3Client() {

		// applicationPropertiesから取得
		String region = applicationProperties.getRegion();

		// 認証情報
		AWSCredentials credentials = new BasicAWSCredentials("AKIAS7JE6REG6UEUDEWT", "WNWL1fv+mJAX657F7+g30ZZV3zmrnUaXJiHS+HfY");
		// awsS3Client インスタンス化
		awsS3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();

		return awsS3Client;
	}
}
