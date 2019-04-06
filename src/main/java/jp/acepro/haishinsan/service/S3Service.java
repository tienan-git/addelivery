package jp.acepro.haishinsan.service;

import java.io.IOException;

import jp.acepro.haishinsan.dto.S3UploadDto;

public interface S3Service {

	void uploadImageToS3(S3UploadDto s3UploadDto);

	String getFileFromS3(String imageName) throws IOException;

}
