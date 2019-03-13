package jp.acepro.haishinsan.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.context.SecurityContextHolder;

import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.dto.LoginUser;

/**
 * ログイン情報取得処理
 */
public class ContextUtil {

	public static long getUserId() {
		LoginUser loginUser = getLoginUser();
		if (loginUser != null) {
			return loginUser.getUserId();
		}
		return 0;
	}

	public static String getUserName() {
		LoginUser loginUser = getLoginUser();
		if (loginUser != null) {
			return loginUser.getUserName();
		}
		return null;
	}

	public static LoginUser getLoginUser() {// TODO 袁先生へ：修正ができましたら、privateにしてください
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal != null && principal instanceof LoginUser) {
			return (LoginUser) principal;
		} else {
			return null;
		}
	}
	
	public static String getOldSystemPassword() {
		if (Objects.isNull(getLoginUser().getOldSystemPassword())) {
			return "";
		}
		return getLoginUser().getOldSystemPassword();
	}
	
	public static String getOldSystemEmail() {
		if (Objects.isNull(getLoginUser().getOldSystemEmail())) {
			return "";
		}
		return getLoginUser().getOldSystemEmail();
	}

	public static long getCurrentShopId() {
		return getCurrentShop().getShopId();
	}

	public static Shop getCurrentShop() {
		return getLoginUser().getShop();
	}

	public static List<Shop> getShopList() {
		return getLoginUser().getShopList();
	}

	public static Integer getRoleId() {
		return getLoginUser().getRoleId();
	}
	
	public static boolean hasAuthority(String authority) {

		boolean flag = false;
		if (authority == null || authority == "") {
			return flag;
		}
		Collection<?> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		for (Object grantedAuthority : authorities) {
			if (grantedAuthority.toString().equals(authority)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
}
