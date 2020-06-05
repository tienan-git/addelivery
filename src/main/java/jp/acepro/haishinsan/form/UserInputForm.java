package jp.acepro.haishinsan.form;

import jp.acepro.haishinsan.annotation.Trim;
import lombok.Data;

@Data
public class UserInputForm {

	Long userId;
	@Trim
	String userName;
	@Trim
	String email;
	@Trim
	String password;
	Long roleId;
	Long shopId;
	String shopName;
	Long corporationId;
	String corporationName;
	Long agencyId;
	String agencyName;

}
