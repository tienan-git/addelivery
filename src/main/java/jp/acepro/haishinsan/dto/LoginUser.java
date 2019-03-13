package jp.acepro.haishinsan.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.enums.Role;
//import jp.kefir.fund.backoffice.enums.Role;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class LoginUser extends User {
	private static final long serialVersionUID = 1L;

	private Long userId;
	private String userName;
	private Integer roleId;
	private String roleName;
	private String email;
	private String oldSystemEmail;
	private String oldSystemPassword;
	private Shop shop;
	private List<Shop> shopList;

	public LoginUser(jp.acepro.haishinsan.db.entity.User user, Collection<GrantedAuthority> authorities) {
		super(user.getEmail(), user.getPassword(), authorities);

		this.userId = user.getUserId();
		this.userName = user.getUserName();
		this.roleId = Role.of(user.getRoleId().intValue()).getValue();
		this.roleName = Role.of(user.getRoleId().intValue()).getLabel();
		this.email = user.getEmail();
		//this.oldSystemEmail = user.getOldSystemEmail();
		this.oldSystemPassword = user.getOldSystemPassword();
	}

	public void changeCurrent(Shop shop) {
		this.shop = shop;
	}

	public void changeShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}

}
