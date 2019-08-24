package jp.acepro.haishinsan.dto.account;

import lombok.Data;

@Data
public class UserDto {
	Long userId;
	String userName;
	String email;
	String password;
	Long shopId;
	Long roleId;
	String shopName;
	Long corporationId;
	String corporationName;
	Long agencyId;
	String agencyName;
}
