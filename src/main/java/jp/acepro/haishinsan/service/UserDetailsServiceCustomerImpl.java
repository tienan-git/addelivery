package jp.acepro.haishinsan.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.acepro.haishinsan.constant.AuthConstant;
import jp.acepro.haishinsan.dao.AuthorityCustomDao;
import jp.acepro.haishinsan.dao.CorporationDao;
import jp.acepro.haishinsan.dao.ShopCustomDao;
import jp.acepro.haishinsan.dao.ShopDao;
import jp.acepro.haishinsan.dao.UserCustomDao;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.db.entity.User;
import jp.acepro.haishinsan.dto.LoginUser;
import jp.acepro.haishinsan.enums.Role;
import jp.acepro.haishinsan.exception.SystemException;

@Service
public class UserDetailsServiceCustomerImpl implements UserDetailsService {

	@Autowired
	HttpSession session;

	@Autowired
	UserCustomDao userCustomDao;

	@Autowired
	AuthorityCustomDao authorityCustomDao;

	@Autowired
	ShopDao shopDao;

	@Autowired
	CorporationDao corporationDao;

	@Autowired
	ShopCustomDao shopCustomDao;

	@Override
	public UserDetails loadUserByUsername(String email) {

		// email = "admin@ace-pro.com";
		try {
			User user = userCustomDao.selectByEmail(email);
			if (user == null) {
				throw new UsernameNotFoundException("該当ユーザ存在しません{" + email + "}");
			}
			// 権限を取得
			List<String> authorityList = authorityCustomDao.selectByRoleId(String.valueOf(user.getRoleId()));
			Collection<GrantedAuthority> authorityCollection = new ArrayList<GrantedAuthority>();
			for (String authority : authorityList) {
				authorityCollection.add(new SimpleGrantedAuthority(authority));
			}

			// ユーザの法人情報を取得
			LoginUser loginUser = new LoginUser(user, authorityCollection);

			// ユーザが切り替えできる店舗を取得
			List<Shop> shopList = new ArrayList<Shop>();
			if (user.getRoleId().intValue()==Role.ADMIN.getValue().intValue()) {
				shopList = shopCustomDao.selectAllShop();
			} else if (user.getRoleId().intValue()==Role.AGENCY.getValue().intValue()) {
				shopList = shopCustomDao.selectAgencyShops(user.getShopId());
			} else if (user.getRoleId().intValue()==Role.CORPORATION.getValue().intValue()) {
				shopList = shopCustomDao.selectCorporationShops(user.getShopId());
			} else if (user.getRoleId().intValue()==Role.SHOP.getValue().intValue()) {
				Shop shop = shopDao.selectById(user.getShopId());
				shopList.add(shop);
			}

			// ユーザの店舗情報を取得
			Shop shop = shopList.stream().filter(s -> s.getShopId().intValue() == user.getShopId().intValue())
					.findFirst().get();

			loginUser.changeShopList(shopList);
			loginUser.changeCurrent(shop);

			return loginUser;
		} catch (UsernameNotFoundException e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("認証失敗しました。", e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException("システムエラーが発生しました。");
		}
	}

}
