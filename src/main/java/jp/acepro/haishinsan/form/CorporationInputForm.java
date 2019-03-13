package jp.acepro.haishinsan.form;


import java.util.List;

import jp.acepro.haishinsan.annotation.Trim;
import jp.acepro.haishinsan.dto.ShopDto;
import lombok.Data;

@Data
public class CorporationInputForm {

	Long corporationId;
	@Trim
	String corporationName;
	
	Long agencyId;
	@Trim
	String agencyName;
	List<ShopDto> shopDtoList;
}
