package jp.acepro.haishinsan.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.form.IssueInputForm;

@Mapper
public interface IssueMapper {

	IssueMapper INSTANCE = Mappers.getMapper(IssueMapper.class);
	
	// ＦＯＲＭからＤＴＯへ変換
    //@Mapping(target = "startDate", source = "startDateFormat")
	IssueDto map(IssueInputForm issueInputForm);
	
}
