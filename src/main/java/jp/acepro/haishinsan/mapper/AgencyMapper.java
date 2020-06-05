package jp.acepro.haishinsan.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import jp.acepro.haishinsan.dto.account.AgencyDto;
import jp.acepro.haishinsan.form.AgencyInputForm;

@Mapper
public interface AgencyMapper {

	AgencyMapper INSTANCE = Mappers.getMapper(AgencyMapper.class);

	AgencyDto map(AgencyInputForm agencyInputForm);

	AgencyInputForm mapToForm(AgencyDto agencyDto);

}
