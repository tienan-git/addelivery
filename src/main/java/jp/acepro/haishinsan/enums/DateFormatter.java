package jp.acepro.haishinsan.enums;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * 日付のフォーマットを処理するクラス
 */
public enum DateFormatter {
	yyyyMMdd("uuuuMMdd"), yyyyMM("uuuuMM"), yyyyMM_SLASH("uuuu/MM"), yyyyMMdd_HYPHEN("uuuu-MM-dd"), yyyyMMddHHmm_HYPHEN("uuuu-MM-dd HH:mm"),
	yyyyMMdd_SLASH("uuuu/MM/dd"), yyyyMd_SLASH("uuuu/M/d"), yyyyMMdd_JP("uuuu年MM月dd日");

	private final DateTimeFormatter formatter;

	DateFormatter(String pattern) {
		formatter = DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.STRICT);
	}

	/**
	 * 日付オブジェクトを文字列にフォーマットします。
	 *
	 * @param date 日付オブジェクト
	 * @return フォーマット後の文字列
	 */
	public String format(LocalDate date) {
		return formatter.format(date);
	}

	/**
	 * 文字列から日付オブジェクトを作成します。
	 *
	 * @param text 文字列
	 * @return 日付オブジェクト
	 */
	public LocalDate parse(String text) {
		final LocalDate date = formatter.parse(text, LocalDate::from);
		// 0以下は紀元前なのでサポート外。
		if (date.getYear() <= 0) {
			throw new DateTimeParseException(
					String.format("Text %s could not be parsed. Invalid value for year (valid value 1 - 9999) : %d",
							text, date.getYear()),
					text, 0);
		}
		return date;
	}
}
