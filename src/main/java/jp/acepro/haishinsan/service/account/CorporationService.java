package jp.acepro.haishinsan.service.account;

import java.util.List;

import jp.acepro.haishinsan.dto.account.CorporationDto;
import jp.acepro.haishinsan.dto.account.ShopDto;

public interface CorporationService {

	List<CorporationDto> search();

	List<ShopDto> searchShopsByCorpId(Long corporationId);

	CorporationDto create(CorporationDto corporationDto);

	CorporationDto getById(Long corporationId);

	void update(CorporationDto corporationDto);

	void delete(Long corporationId);

}
