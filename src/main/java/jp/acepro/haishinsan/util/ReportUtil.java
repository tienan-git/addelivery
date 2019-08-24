package jp.acepro.haishinsan.util;

import java.math.BigDecimal;

public class ReportUtil {

	/**
	 * CTR(Click Through Rate)の計算(%表示)
	 * 
	 * @param clicks
	 * @param impressions
	 * @return CTR
	 * 
	 */
	public static String calCtr(Long clicks, Long impressions) {
		if (clicks == null || impressions == null) {
			return "0";
		}
		if (impressions == 0) {
			return "0";
		}
		BigDecimal clicksBigDecimal = BigDecimal.valueOf(clicks);
		BigDecimal impressionsBigDecimal = BigDecimal.valueOf(impressions);
		BigDecimal ctr = BigDecimal.valueOf(100).multiply(clicksBigDecimal).divide(impressionsBigDecimal, 2,
				BigDecimal.ROUND_HALF_UP);
		return ctr.toPlainString();

	}

	/**
	 * CPC(Cost Per Click)の計算
	 * 
	 * @param clicks
	 * @param spend
	 * @return CPC
	 * 
	 */
	public static Integer calCpc(Long clicks, Long spend) {
		if (clicks == null || spend == null) {
			return 0;
		}
		if (clicks == 0) {
			return 0;
		}
		BigDecimal clicksBigDecimal = BigDecimal.valueOf(clicks);
		BigDecimal spendBigDecimal = BigDecimal.valueOf(spend);
		BigDecimal cpc = spendBigDecimal.divide(clicksBigDecimal, 0, BigDecimal.ROUND_HALF_UP);
		return cpc.intValue();

	}

	/**
	 * CPM(Cost Per Mille)の計算
	 * 
	 * @param impressions
	 * @param spend
	 * @return CPM
	 * 
	 */
	public static Integer calCpm(Long impressions, Long spend) {
		if (impressions == null || spend == null) {
			return 0;
		}
		if (impressions == 0) {
			return 0;
		}
		BigDecimal thoundBigDecimal = BigDecimal.valueOf(1000);
		BigDecimal impressionsBigDecimal = BigDecimal.valueOf(impressions);
		BigDecimal spendBigDecimal = BigDecimal.valueOf(spend);
		BigDecimal cpm = thoundBigDecimal.multiply(spendBigDecimal).divide(impressionsBigDecimal, 0,
				BigDecimal.ROUND_HALF_UP);
		return cpm.intValue();

	}

	/**
	 * 視聴率の計算(%表示)
	 * 
	 * @param videoViews
	 * @param impressions
	 * @return videoViewRate
	 * 
	 */
	public static String calVideoViewRate(Long videoViews, Long impressions) {

		if (videoViews == null || impressions == null) {
			return "0";
		}
		if (impressions == 0) {
			return "0";
		}
		BigDecimal videoViewsBigDecimal = BigDecimal.valueOf(videoViews);
		BigDecimal impressionsBigDecimal = BigDecimal.valueOf(impressions);
		BigDecimal videoViewRate = BigDecimal.valueOf(100).multiply(videoViewsBigDecimal).divide(impressionsBigDecimal,
				2, BigDecimal.ROUND_HALF_UP);
		return videoViewRate.toPlainString();
	}

	/**
	 * 視聴単価の計算
	 * 
	 * @param videoViews
	 * @param spend
	 * @return avgCpc
	 * 
	 */
	public static Integer calAvgCpc(Long videoViews, Long spend) {

		if (videoViews == null || spend == null) {
			return 0;
		}
		if (videoViews == 0) {
			return 0;
		}
		BigDecimal videoViewsBigDecimal = BigDecimal.valueOf(videoViews);
		BigDecimal spendBigDecimal = BigDecimal.valueOf(spend);
		BigDecimal avgCpc = spendBigDecimal.divide(videoViewsBigDecimal, 0, BigDecimal.ROUND_HALF_UP);
		return avgCpc.intValue();
	}

	/**
	 * マージン率により、APIで取得した実際費用から、レポート画面表示時の表示費用を算出
	 * 
	 * @param realSpend
	 * @return Long
	 * 
	 */
	public static Long calDisplaySpend(Double realSpend) {

		// レアケース対応
		if (realSpend == null) {
			return 0l;
		}
		if (ContextUtil.getCurrentShop().getMarginRatio().intValue() == 100) {
			return 0l;
		}

		// 表示費用 = 実際費用 * 100 / (100 - マージン率)
		BigDecimal displaySpendBigDecimal = BigDecimal.valueOf(realSpend).multiply(BigDecimal.valueOf(100)).divide(
				BigDecimal.valueOf(100 - ContextUtil.getCurrentShop().getMarginRatio()), 0, BigDecimal.ROUND_HALF_UP);
		Double displaySpend = displaySpendBigDecimal.doubleValue();
		return displaySpend.longValue();
	}
}
