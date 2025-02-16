package jp.acepro.haishinsan.service.account;

import java.util.List;

import jp.acepro.haishinsan.dto.account.ShopDto;
import jp.acepro.haishinsan.dto.account.UserDto;

public interface ShopService {

	List<ShopDto> search();

	List<UserDto> searchUsersByShopId(Long shopId);

	ShopDto create(ShopDto shopDto);

	ShopDto getById(Long shopId);

	void update(ShopDto shopDto);

	void delete(Long shopId);

}
