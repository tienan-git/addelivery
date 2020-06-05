package jp.acepro.haishinsan.dto.account;

import java.util.List;

import lombok.Data;

@Data
public class CorporationDto {
	Long corporationId;
	String corporationName;
	Long agencyId;
	String agencyName;
	List<ShopDto> shopDtoList;

}
