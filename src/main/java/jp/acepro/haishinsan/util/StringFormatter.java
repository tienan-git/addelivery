package jp.acepro.haishinsan.util;

/**
 * 文字列日付のフォーマットを処理するクラス
 */
public class StringFormatter {

	StringFormatter() {
	}

	/**
	 * 日付文字列をyyyy/MM/ddにフォーマットします。
	 *
	 * @param date 8桁文字列
	 * @return フォーマット後の文字列
	 */
	public static String formatToSlash(String date) {
		if (date == null || date.length() != 8) {
			return "";
		}
		return date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8);
	}

	/**
	 * 日付文字列をyyyy-MM-ddにフォーマットします。
	 *
	 * @param date 8桁文字列
	 * @return フォーマット後の文字列
	 */
	public static String formatToHyphen(String date) {
		if (date == null || date.length() != 8) {
			return "";
		}
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
	}

	/**
	 * yyyy/MM/ddやyyyy-MM-dd日付文字列をyyyyMMddにフォーマットします。
	 *
	 * @param date 文字列
	 * @return フォーマット後の文字列
	 */
	public static String dateFormat(String date) {
		if (date == null || date.length() == 0) {
			return "";
		}
		String result = date.replace("/", "");
		return result.replace("-", "");
	}

	/**
	 * yyyy/MM/dd日付文字列をyyyy-MM-ddにフォーマットします。
	 *
	 * @param date 文字列
	 * @return フォーマット後の文字列
	 */
	public static String dateToDisplay(String date) {
		if (date == null || date.length() != 10) {
			return "";
		}
		return date.replace("/", "-");
	}

	/**
	 * yyyy-MM-dd日付文字列をyyyy/MM/ddにフォーマットします。
	 *
	 * @param date 文字列
	 * @return フォーマット後の文字列
	 */
	public static String dateHyphenToSlash(String date) {
		if (date == null || date.length() != 10) {
			return "";
		}
		return date.replace("-", "/");
	}

	/**
	 * 郵便番号を***-****にフォーマットします。
	 *
	 * @param zip 郵便番号
	 * @return フォーマット後の文字列
	 */
	public static String zipFormat(String zip) {
		if (zip == null || zip.length() != 7) {
			return "";
		}
		return zip.substring(0, 3) + "-" + zip.substring(3, 7);
	}

	/**
	 * 電話番号を***-****にフォーマットします。
	 *
	 * @param phoneNo 電話番号
	 * @return フォーマット後の文字列
	 */
	public static String phoneNoFormat(String phoneNo) {
		if (phoneNo == null || phoneNo.length() < 10) {
			return "";
		}
		return phoneNo.substring(0, phoneNo.length() - 8) + "-"
				+ phoneNo.substring(phoneNo.length() - 8, phoneNo.length() - 4) + "-"
				+ phoneNo.substring(phoneNo.length() - 4, phoneNo.length());
	}
}
