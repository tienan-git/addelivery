package jp.acepro.haishinsan.service.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.UserCustomDao;
import jp.acepro.haishinsan.dao.UserDao;
import jp.acepro.haishinsan.db.entity.User;
import jp.acepro.haishinsan.dto.account.UserDto;
import jp.acepro.haishinsan.entity.UserWithAgency;
import jp.acepro.haishinsan.exception.BusinessException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserCustomDao userCustomDao;

	@Autowired
	UserDao userDao;

	@Autowired
	ShopService shopService;

	@Transactional
	@Override
	public List<UserDto> search() {

		List<UserWithAgency> userWithAgencyList = userCustomDao.selectAll();

		List<UserDto> userDtoList = new ArrayList<UserDto>();

		for (UserWithAgency userWithAgency : userWithAgencyList) {

			UserDto userDto = new UserDto();
			userDto.setUserId(userWithAgency.getUserId());
			userDto.setUserName(userWithAgency.getUserName());
			userDto.setEmail(userWithAgency.getEmail());
			userDto.setRoleId(userWithAgency.getRoleId());
			userDto.setPassword(userWithAgency.getPassword());
			userDto.setAgencyId(userWithAgency.getAgencyId());
			userDto.setAgencyName(userWithAgency.getAgencyName());
			userDto.setCorporationId(userWithAgency.getCorporationId());
			userDto.setCorporationName(userWithAgency.getCorporationName());
			userDto.setShopId(userWithAgency.getShopId());
			userDto.setShopName(userWithAgency.getShopName());

			userDtoList.add(userDto);
		}

		return userDtoList;
	}

	@Transactional
	@Override
	public UserDto create(UserDto userDto) {

		User existUser = userCustomDao.selectByEmail(userDto.getEmail());
		if (existUser != null) {
			throw new BusinessException(ErrorCodeConstant.E50011);
		}

		// DTO->Entity
		User user = new User();
		user.setUserId(userDto.getUserId());
		user.setUserName(userDto.getUserName());
		user.setEmail(userDto.getEmail());
		user.setRoleId(userDto.getRoleId());
		user.setShopId(userDto.getShopId());

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(userDto.getPassword()));// パスワードを暗号化する

		// DB access
		userDao.insert(user);

		// Entity->DTO
		UserDto newUserDto = getById(user.getUserId());

		return newUserDto;
	}

	@Transactional
	@Override
	public UserDto getById(Long userId) {

		UserWithAgency userWithAgency = userCustomDao.selectById(userId);

		UserDto userDto = new UserDto();
		userDto.setUserId(userWithAgency.getUserId());
		userDto.setUserName(userWithAgency.getUserName());
		userDto.setEmail(userWithAgency.getEmail());
		userDto.setRoleId(userWithAgency.getRoleId());
		userDto.setPassword(userWithAgency.getPassword());
		userDto.setAgencyId(userWithAgency.getAgencyId());
		userDto.setAgencyName(userWithAgency.getAgencyName());
		userDto.setCorporationId(userWithAgency.getCorporationId());
		userDto.setCorporationName(userWithAgency.getCorporationName());
		userDto.setShopId(userWithAgency.getShopId());
		userDto.setShopName(userWithAgency.getShopName());

		return userDto;

	}

	@Transactional
	@Override
	public void update(UserDto userDto) {

		User user = userCustomDao.selectByEmail(userDto.getEmail());

		// 該当メールがすでに使われている場合
		if (user != null && !user.getUserId().equals(userDto.getUserId())) {
			throw new BusinessException(ErrorCodeConstant.E50011);
		}

		if (user == null) {
			user = userDao.selectById(userDto.getUserId());
		}

		user.setEmail(userDto.getEmail());
		user.setUserName(userDto.getUserName());
		if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(userDto.getPassword()));
		}
		user.setRoleId(userDto.getRoleId());

		userDao.update(user);

	}

	@Transactional
	@Override
	public void delete(Long userId) {

		User user = userDao.selectById(userId);
		userDao.delete(user);

	}

	@Transactional
	@Override
	public UserDto getByEmail(String email) {

		User user = userCustomDao.selectByEmail(email);
		if (user == null) {
			throw new BusinessException(ErrorCodeConstant.E50012);
		}

		UserDto userDto = new UserDto();
		userDto.setUserId(user.getUserId());
		userDto.setUserName(user.getUserName());
		userDto.setEmail(user.getEmail());
		userDto.setRoleId(user.getRoleId());
		userDto.setPassword(user.getPassword());

		return userDto;

	}

	@Override
	public UserDto getByShopId(Long shopId) {

		UserDto userDto = new UserDto();
//		ShopDto shopDto = shopService.getById(shopId);
//		userDto.setAgencyId(shopDto.getAgencyId());
//		userDto.setAgencyName(shopDto.getAgencyName());
//		userDto.setCorporationId(shopDto.getCorporationId());
//		userDto.setCorporationName(shopDto.getCorporationName());
//		userDto.setShopId(shopDto.getShopId());
//		userDto.setShopName(shopDto.getShopName());

		userDto.setAgencyId(1L);
		userDto.setAgencyName("test-name");
		userDto.setCorporationId(2L);
		userDto.setCorporationName("test-name");
		userDto.setShopId(1L);
		userDto.setShopName("test-name");

		return userDto;
	}

}
