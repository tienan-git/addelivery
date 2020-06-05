package jp.acepro.haishinsan.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class DataConvertUtil {

	public InputStream convertToInputStream(String encodedString) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		InputStream is = new ByteArrayInputStream(decodedBytes);

		return is;
	}
}
