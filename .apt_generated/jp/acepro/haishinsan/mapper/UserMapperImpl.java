package jp.acepro.haishinsan.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import jp.acepro.haishinsan.db.entity.User;
import jp.acepro.haishinsan.dto.account.UserDto;
import jp.acepro.haishinsan.form.UserInputForm;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-08-24T14:36:44+0900",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.13.0.v20170516-1929, environment: Java 1.8.0_144 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public List<UserDto> map(List<User> userList) {
        if ( userList == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>();
        for ( User user : userList ) {
            list.add( map( user ) );
        }

        return list;
    }

    @Override
    public UserDto map(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto_ = new UserDto();

        userDto_.setEmail( user.getEmail() );
        userDto_.setPassword( user.getPassword() );
        userDto_.setRoleId( user.getRoleId() );
        userDto_.setShopId( user.getShopId() );
        userDto_.setUserId( user.getUserId() );
        userDto_.setUserName( user.getUserName() );

        return userDto_;
    }

    @Override
    public UserDto map(UserInputForm userInputForm) {
        if ( userInputForm == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setAgencyId( userInputForm.getAgencyId() );
        userDto.setAgencyName( userInputForm.getAgencyName() );
        userDto.setCorporationId( userInputForm.getCorporationId() );
        userDto.setCorporationName( userInputForm.getCorporationName() );
        userDto.setEmail( userInputForm.getEmail() );
        userDto.setPassword( userInputForm.getPassword() );
        userDto.setRoleId( userInputForm.getRoleId() );
        userDto.setShopId( userInputForm.getShopId() );
        userDto.setShopName( userInputForm.getShopName() );
        userDto.setUserId( userInputForm.getUserId() );
        userDto.setUserName( userInputForm.getUserName() );

        return userDto;
    }

    @Override
    public UserInputForm mapToForm(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserInputForm userInputForm = new UserInputForm();

        userInputForm.setAgencyId( userDto.getAgencyId() );
        userInputForm.setAgencyName( userDto.getAgencyName() );
        userInputForm.setCorporationId( userDto.getCorporationId() );
        userInputForm.setCorporationName( userDto.getCorporationName() );
        userInputForm.setEmail( userDto.getEmail() );
        userInputForm.setPassword( userDto.getPassword() );
        userInputForm.setRoleId( userDto.getRoleId() );
        userInputForm.setShopId( userDto.getShopId() );
        userInputForm.setShopName( userDto.getShopName() );
        userInputForm.setUserId( userDto.getUserId() );
        userInputForm.setUserName( userDto.getUserName() );

        return userInputForm;
    }

    @Override
    public User map(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setUserId( userDto.getUserId() );
        user.setUserName( userDto.getUserName() );
        user.setEmail( userDto.getEmail() );
        user.setPassword( userDto.getPassword() );
        user.setShopId( userDto.getShopId() );
        user.setRoleId( userDto.getRoleId() );

        return user;
    }
}
