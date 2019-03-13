package jp.acepro.haishinsan.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import jp.acepro.haishinsan.dto.CorporationDto;
import jp.acepro.haishinsan.form.CorporationInputForm;


@Mapper
public interface CorporationMapper {

	CorporationMapper INSTANCE = Mappers.getMapper(CorporationMapper.class);

	CorporationDto map(CorporationInputForm corporationInputForm);

	CorporationInputForm mapToForm(CorporationDto corporationDto);
}
