package jp.acepro.haishinsan.service.account;

import java.util.List;

import jp.acepro.haishinsan.dto.account.AgencyDto;
import jp.acepro.haishinsan.dto.account.CorporationDto;

public interface AgencyService {

	List<AgencyDto> search();

	List<CorporationDto> searchCorpsByAgencyId(Long agencyId);

	AgencyDto create(AgencyDto agencyDto);

	AgencyDto getById(Long agencyId);

	void update(AgencyDto agencyDto);

	void delete(Long agencyId);

}
