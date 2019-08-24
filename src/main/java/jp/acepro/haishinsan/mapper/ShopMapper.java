package jp.acepro.haishinsan.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.dto.account.ShopDto;
import jp.acepro.haishinsan.form.ShopInputForm;

@Mapper
public interface ShopMapper {

	ShopMapper INSTANCE = Mappers.getMapper(ShopMapper.class);

	List<ShopDto> map(List<Shop> shopList);

	ShopDto map(Shop shop);

	ShopDto map(ShopInputForm shopInputForm);

	ShopInputForm mapToForm(ShopDto shopDto);

	Shop map(ShopDto shopDto);

}
