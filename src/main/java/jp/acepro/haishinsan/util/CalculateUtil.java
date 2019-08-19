package jp.acepro.haishinsan.util;

import java.math.BigDecimal;

import jp.acepro.haishinsan.dto.BudgetAllocationDto;

public class CalculateUtil {

	/**
	 * 各媒体予算分配の計算
	 * 
	 * @param budget
	 * @param dspSelected
	 * @param googleSelected
	 * @param facebookSelected
	 * @param twitterSelected
	 * @return BudgetAllocationDto
	 * 
	 */
	public static BudgetAllocationDto calCtr(Long budget, boolean dspSelected, boolean googleSelected, boolean facebookSelected, boolean twitterSelected) {
		if (budget == null) {
			return null;
		}

		BudgetAllocationDto budgetAllocationDto = new BudgetAllocationDto();
		// 分母を集計
		Double denominator = 0d;
		if (dspSelected) {
			denominator = denominator + ContextUtil.getCurrentShop().getDspDistributionRatio();
		}
		if (googleSelected) {
			denominator = denominator + ContextUtil.getCurrentShop().getGoogleDistributionRatio();
		}
		if (facebookSelected) {
			denominator = denominator + ContextUtil.getCurrentShop().getFacebookDistributionRatio();
		}
		if (twitterSelected) {
			denominator = denominator + ContextUtil.getCurrentShop().getTwitterDistributionRatio();
		}

		BigDecimal budgetBigDecimal = BigDecimal.valueOf(budget);

		if (dspSelected) {
			// dspの分配予算を算出
			budgetAllocationDto.setDspBudget(budgetBigDecimal.multiply(BigDecimal.valueOf(ContextUtil.getCurrentShop().getDspDistributionRatio()).divide(BigDecimal.valueOf(denominator), 2, BigDecimal.ROUND_HALF_UP)).intValue());
		}
		if (googleSelected) {
			// googleの分配予算を算出
			budgetAllocationDto.setGoogleBudget(budgetBigDecimal.multiply(BigDecimal.valueOf(ContextUtil.getCurrentShop().getGoogleDistributionRatio()).divide(BigDecimal.valueOf(denominator), 2, BigDecimal.ROUND_HALF_UP)).longValue());
		}
		if (facebookSelected) {
			// facebookの分配予算を算出
			budgetAllocationDto.setFacebookBudget(budgetBigDecimal.multiply(BigDecimal.valueOf(ContextUtil.getCurrentShop().getFacebookDistributionRatio()).divide(BigDecimal.valueOf(denominator), 2, BigDecimal.ROUND_HALF_UP)).longValue());
		}
		if (twitterSelected) {
			// twitterの分配予算を算出
			budgetAllocationDto.setTwitterBudget(budgetBigDecimal.multiply(BigDecimal.valueOf(ContextUtil.getCurrentShop().getTwitterDistributionRatio()).divide(BigDecimal.valueOf(denominator), 2, BigDecimal.ROUND_HALF_UP)).longValue());
		}

		return budgetAllocationDto;
	}

	/**
	 * 総予算から一日の予算を計算
	 * 
	 * @param budget
	 * @param startDate
	 * @param endDate
	 * @return Long
	 * 
	 */
	public static Long calOneDayBudget(Long budget, String startDate, String endDate) {
		int days = DateUtil.distance_hyphen(startDate, endDate);

		// 予算から一日の予算を算出する 予算/日数
		return BigDecimal.valueOf(budget).divide(BigDecimal.valueOf(days)).longValue();

	}

	/**
	 * 一日の予算から総予算を計算
	 * 
	 * @param budget
	 * @param startDate
	 * @param endDate
	 * @return Long
	 * 
	 */
	public static Long calTotalBudget(Long budget, String startDate, String endDate) {
		int days = DateUtil.distance_hyphen(startDate, endDate);

		// 予算から一日の予算を算出する 予算/日数
		return BigDecimal.valueOf(budget).multiply(BigDecimal.valueOf(days)).longValue();

	}

	/**
	 * マージン率により、画面入力予算から、キャンプーン作成API実行時の実際予算を算出
	 * 
	 * @param inputBudget
	 * @return Long
	 * 
	 */
	public static Long calRealBudget(Long inputBudget) {

		// レアケース対応
		if (inputBudget == null) {
			return 0l;
		}
		if (ContextUtil.getCurrentShop().getMarginRatio().intValue() == 100) {
			return 0l;
		}

		// 実際予算 = 入力予算 * (100 - マージン率) / 100
		BigDecimal realBudgetBigDecimal = BigDecimal.valueOf(inputBudget).multiply(BigDecimal.valueOf(100 - ContextUtil.getCurrentShop().getMarginRatio())).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP);
		Long realBudget = realBudgetBigDecimal.longValue();
		return realBudget;
	}

	/**
	 * マージン率により、画面入力予算から、キャンプーン作成API実行時の実際予算を算出
	 * 
	 * @param inputBudget
	 * @return Long
	 * 
	 */
	public static Long calRealBudgetWithShopRatio(Long inputBudget, Integer ratio) {

		// レアケース対応
		if (inputBudget == null) {
			return 0l;
		}
		if (ratio.intValue() == 100) {
			return 0l;
		}

		// 実際予算 = 入力予算 * (100 - マージン率) / 100
		BigDecimal realBudgetBigDecimal = BigDecimal.valueOf(inputBudget).multiply(BigDecimal.valueOf(100 - ratio)).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP);
		Long realBudget = realBudgetBigDecimal.longValue();
		return realBudget;
	}

	/**
	 * 
	 * 四捨五入により、費用を算出
	 * 
	 * @param price
	 * @return Double
	 * 
	 */
	public static Long getRoundedPrice(Double price) {

		if (price == 0d) {
			return 0l;
		}
		BigDecimal bd = new BigDecimal(price);
		price = bd.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
		return price.longValue();
	}
	

	/**
	 * 全角文字は２桁、半角文字は１桁として文字数をカウントする
	 * @param str 対象文字列
	 * @return 文字数
	 */
	public static int strLenCounter(String str) {
	  
	  //戻り値
	  int ret = 0;
	  
	  //全角半角判定
	  char[] c = str.toCharArray();
	  for(int i=0;i<c.length;i++) {
	    if(String.valueOf(c[i]).getBytes().length <= 1){
	      ret += 1; //半角文字なら＋１
	    }else{
	      ret += 2; //全角文字なら＋２
	    }
	  }
	  
	  return ret;
	}
}
