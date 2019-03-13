package jp.acepro.haishinsan.dto;

import java.util.List;

import lombok.Data;

@Data
public class ShopDto {

	Long shopId;
	String shopName;
	Long corporationId;
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

	List<UserDto> userDtoList;

}
