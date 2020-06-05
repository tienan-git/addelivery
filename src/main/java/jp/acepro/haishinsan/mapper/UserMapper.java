package jp.acepro.haishinsan.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import jp.acepro.haishinsan.db.entity.User;
import jp.acepro.haishinsan.dto.account.UserDto;
import jp.acepro.haishinsan.form.UserInputForm;

@Mapper
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	List<UserDto> map(List<User> userList);

	UserDto map(User user);

	UserDto map(UserInputForm userInputForm);

	UserInputForm mapToForm(UserDto userDto);

	User map(UserDto userDto);

}
