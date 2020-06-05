package jp.acepro.haishinsan.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import jp.acepro.haishinsan.dto.CreativeDto;
import jp.acepro.haishinsan.form.CreativeInputForm;

@Mapper
public interface CreativeMapper {

	CreativeMapper INSTANCE = Mappers.getMapper(CreativeMapper.class);

	// ＦＯＲＭからＤＴＯへ変換
	// @Mapping(target = "startDate", source = "startDateFormat")
	CreativeDto map(CreativeInputForm creativeInputForm);

}
