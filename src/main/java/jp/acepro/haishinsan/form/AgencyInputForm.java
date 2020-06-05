package jp.acepro.haishinsan.form;

import java.util.List;

import jp.acepro.haishinsan.annotation.Trim;
import jp.acepro.haishinsan.dto.account.CorporationDto;
import lombok.Data;

@Data
public class AgencyInputForm {

	Long agencyId;
	@Trim
	String agencyName;

	List<CorporationDto> corporationDtoList;

}
