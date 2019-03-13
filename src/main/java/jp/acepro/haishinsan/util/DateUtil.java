package jp.acepro.haishinsan.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import jp.acepro.haishinsan.enums.DateFormatter;

public class DateUtil {

	/**
	 * 2つの日付の差を求めます。 日付文字列 fromDate から toDate までが何日かを返します。
	 * 
	 * @param fromDate
	 *            日付文字列 yyyy-MM-dd
	 * @param toDate
	 *            日付文字列 yyyy-MM-dd
	 * @return 経過日数
	 * 
	 */
	public static int distance_hyphen(String fromDate, String toDate) {

		LocalDate localFromDate = DateFormatter.yyyyMMdd_HYPHEN.parse(fromDate);
		LocalDate localToDate = DateFormatter.yyyyMMdd_HYPHEN.parse(toDate);
		int days = (int) localFromDate.until(localToDate, ChronoUnit.DAYS);
		days += 1;

		return days;
	}

	/**
	 * 2つの日付の差を求めます。 日付文字列 fromDate から toDate までが何日かを返します。
	 * 
	 * @param fromDate
	 *            日付文字列 yyyyMMdd
	 * @param toDate
	 *            日付文字列 yyyyMMdd
	 * @return 経過日数
	 * 
	 */
	public static int distance(String fromDate, String toDate) {
		LocalDate localFromDate = DateFormatter.yyyyMMdd.parse(fromDate);
		LocalDate localToDate = DateFormatter.yyyyMMdd.parse(toDate);
		return (int) localFromDate.until(localToDate, ChronoUnit.DAYS);
	}

	/**
	 * 日付文字列 yyyy-MM-ddをLocalDateにフォーマット
	 * 
	 * @param date
	 *            日付文字列 yyyy-MM-dd
	 * @return LocalDate
	 * 
	 */
	public static LocalDate toLocalDate(String date) {
		if (date == null || date.length() == 0) {
			return null;
		}
		return DateFormatter.yyyyMMdd_HYPHEN.parse(date);
	}

	/**
	 * LocalDateを日付文字列 yyyy-MM-ddにフォーマット
	 * 
	 * @param LocalDate
	 * 
	 * @return String 日付文字列 yyyy-MM-dd
	 * 
	 */
	public static String fromLocalDate(LocalDate date) {
		if (date != null) {
			return DateFormatter.yyyyMMdd_HYPHEN.format(date);
		} else {
			return null;
		}

	}

	/**
	 * 2018-10-20T09:10:55+0900を日付文字列 2018-10-20 09:10:55にフォーマット
	 * 
	 * @param String
	 * 
	 * @return String 日付文字列 yyyy-MM-dd **:**:**
	 * 
	 */
	public static String toDateTime(String date) {
		if (date != null) {
			return date.substring(0, 19).replace('T', ' ');
		} else {
			return null;
		}

	}
}
