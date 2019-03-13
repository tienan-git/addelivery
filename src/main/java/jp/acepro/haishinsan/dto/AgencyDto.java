package jp.acepro.haishinsan.dto;


import java.util.List;

import lombok.Data;

@Data
public class AgencyDto {
	Long agencyId;
	String agencyName;
	List<CorporationDto> corporationDtoList;
}
