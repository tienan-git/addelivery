package jp.acepro.haishinsan.service.account;

import java.util.List;

import jp.acepro.haishinsan.dto.UserDto;


public interface UserService {

	List<UserDto> search();

	UserDto create(UserDto userDto);

	UserDto getById(Long userId);

	void update(UserDto userDto);

	void delete(Long userId);

	UserDto getByEmail(String email);
	
	UserDto getByShopId(Long shopId);
}
