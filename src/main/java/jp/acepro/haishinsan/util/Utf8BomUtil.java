package jp.acepro.haishinsan.util;

import java.io.IOException;

/**
 * utf-8 BOM付き関連を処理するクラス
 */
public class Utf8BomUtil {

	Utf8BomUtil() {
	}

	/**
	 * utf-8文字列をBOM付きに変更します。
	 *
	 * @param file 文字列
	 * @return 変更後のバイト配列
	 */
	public static byte[] utf8ToWithBom(String file) throws IOException {
		byte[] array1 = { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
		byte[] array2 = file.getBytes("utf-8");

		final byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

}
