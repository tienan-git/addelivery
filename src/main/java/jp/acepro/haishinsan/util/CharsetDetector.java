package jp.acepro.haishinsan.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

/**
 * @author jinzhenghan 指定したCharsetかを検知する
 */
public class CharsetDetector {

	public static String detectCharset(File f, List<String> charsetNames) {

		for (String charsetName : charsetNames) {
			try {
				BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));

				CharsetDecoder decoder = Charset.forName(charsetName).newDecoder();
				decoder.reset();

				byte[] buffer = new byte[512];
				boolean identified = false;
				while ((input.read(buffer) != -1) && (!identified)) {
					identified = identify(buffer, decoder);
				}
				if (identified) {
					input.close();
					return charsetName;
				}
				input.close();
			} catch (Exception e) {
				return null;
			}
		}
		return null;

	}

	private static boolean identify(byte[] bytes, CharsetDecoder decoder) {
		try {
			decoder.decode(ByteBuffer.wrap(bytes));
		} catch (CharacterCodingException e) {
			return false;
		}
		return true;
	}
}
