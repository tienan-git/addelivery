package jp.acepro.haishinsan.dto.account;

import java.util.List;

import lombok.Data;

@Data
public class AgencyDto {
	Long agencyId;
	String agencyName;
	List<CorporationDto> corporationDtoList;
}
