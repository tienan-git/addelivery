package jp.acepro.haishinsan.enums;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;

/**
 * 日時のフォーマットを処理するクラス
 */
public enum DateTimeFormatter {
	paymentDataSendDateTime("uuuuMMddHHmmss.SSS"), //
	yyyyMMddHHmmssSSS("uuuuMMddHHmmssSSS"), //
	yyyyMMddHHmmss("uuuuMMddHHmmss"), //
	yyyyMMddHHmmss_SLASH("uuuu/MM/dd HH:mm:ss"), //
	yyyyMMddHHmm_SLASH_COLON("uuuu/MM/dd HH:mm"), //
	yyyyMMddHHmm_HAIFUN_COLON("uuuu-MM-dd HH:mm"), //
	yyyyMdHm_SLASH_COLON("uuuu/M/d H:m");

	private final java.time.format.DateTimeFormatter formatter;

	DateTimeFormatter(String pattern) {
		if ("uuuuMMddHHmmssSSS".equals(pattern)) {
			// https://bugs.openjdk.java.net/browse/JDK-8031085
			formatter = new DateTimeFormatterBuilder().appendPattern("uuuuMMddHHmmss")
					.appendValue(ChronoField.MILLI_OF_SECOND, 3).toFormatter().withResolverStyle(ResolverStyle.STRICT);
		} else {
			formatter = java.time.format.DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.STRICT);
		}
	}

	/**
	 * 日時オブジェクトを文字列にフォーマットします。
	 *
	 * @param date 日時オブジェクト
	 * @return フォーマット後の文字列
	 */
	public String format(LocalDateTime date) {
		return formatter.format(date);
	}

	/**
	 * 文字列から日時オブジェクトを作成します。
	 *
	 * @param text 文字列
	 * @return 日時オブジェクト
	 */
	public LocalDateTime parse(String text) {

		LocalDateTime date = formatter.parse(text, LocalDateTime::from);
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
