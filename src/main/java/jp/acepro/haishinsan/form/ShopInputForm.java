package jp.acepro.haishinsan.form;

import java.util.List;

import jp.acepro.haishinsan.annotation.Trim;
import jp.acepro.haishinsan.dto.account.ShopDto;
import jp.acepro.haishinsan.dto.account.UserDto;
import lombok.Data;

@Data
public class ShopInputForm {

	Long shopId;
	@Trim
	String shopName;

	Long corporationId;
	@Trim
	String corporationName;

	Long agencyId;
	String agencyName;
	Integer dspUserId;
	String googleAccountId;
	String facebookPageId;
	String twitterAccountId;
	Integer dspDistributionRatio;
	Integer googleDistributionRatio;
	Integer facebookDistributionRatio;
	Integer twitterDistributionRatio;
	String salesCheckFlag;
	Integer marginRatio;
	String shopMailList;
	String salesMailList;

	List<ShopDto> ShopDtoList;
	List<UserDto> userDtoList;
}
