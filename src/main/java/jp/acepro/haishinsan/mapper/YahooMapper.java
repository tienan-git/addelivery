package jp.acepro.haishinsan.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import jp.acepro.haishinsan.dto.yahoo.YahooIssueDto;
import jp.acepro.haishinsan.form.YahooIssueinputForm;

@Mapper
public interface YahooMapper {

	YahooMapper INSTANCE = Mappers.getMapper(YahooMapper.class);

	YahooIssueDto map(YahooIssueinputForm yahooIssueinputForm);

	YahooIssueinputForm mapToForm(YahooIssueDto yahooIssueDto);

}
