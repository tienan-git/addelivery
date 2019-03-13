package jp.acepro.haishinsan.util;

import org.slf4j.MDC;

/**
 * MDCを取得設定処理
 */
public class MDCUtil {

	MDCUtil() {
	}

	public static final String USER_ID_KEY = "userId";
	public static final String USERAGENT_KEY = "userAgent";
	public static final String IPADDRESS_KEY = "ipAddress";

	public static String getUserId() {
		return MDC.get(USER_ID_KEY);
	}

	public static void setUserId(String userId) {
		MDC.put(USER_ID_KEY, userId);
	}

	public static String getUserAgent() {
		return MDC.get(USERAGENT_KEY);
	}

	public static void setUserAgent(String userAgent) {
		MDC.put(USERAGENT_KEY, userAgent);
	}

	public static String getIpAddress() {
		return MDC.get(IPADDRESS_KEY);
	}

	public static void setIpAddress(String ipAddress) {
		MDC.put(IPADDRESS_KEY, ipAddress);
	}
}
