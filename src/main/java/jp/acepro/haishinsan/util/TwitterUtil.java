package jp.acepro.haishinsan.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 文字列都道府県のフォーマットを処理するクラス
 */
public class TwitterUtil {

	TwitterUtil() {

	}

	/**
	 * 文字列都道府県リストを "aaa,bbb,ccc"にフォーマットします。
	 *
	 * @param regionList
	 *            文字列
	 * @return フォーマット後の文字列
	 */

	public static String formatToOneString(List<String> regionList) {

		StringBuilder sb = new StringBuilder();

		if (regionList.size() == 0 || regionList.isEmpty()) {

			return null;

		} else {
			for (int i = 0; i < regionList.size(); i++) {
				sb.append(regionList.get(i)).append(",");
			}
			//System.out.println("RegionsString : " + sb.toString().substring(0, sb.toString().length() - 1));
			return sb.toString().substring(0, sb.toString().length() - 1);
		}

	}

	/**
	 * 文字列都道府県"aaa,bbb,ccc"を リストにフォーマットします。
	 *
	 * @param regionList
	 *            文字列
	 * @return フォーマット後の文字列
	 */

	public static List<String> formatStringToList(String regions) {

		if (regions != null) {
			List<String> result = Arrays.asList(regions.split(","));
			return result;
		} else {
			return null;
		}

	}

	/**
	 * String "Fri Oct 05 02:27:07 +0000 2018"を"10月05日"にformat。
	 *
	 * @param 文字列
	 * @return フォーマット後の文字列
	 */
	public static String getTwitterDate(String dateString) throws ParseException {

		SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.US);
		sf.setLenient(true);
		Date date = sf.parse(dateString);
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		return sdf.format(date);
	}

	/**
	 * Encodeメソット
	 **/
	public static String urlEncode(String string) {
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
