package jp.acepro.haishinsan.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import jp.acepro.haishinsan.db.entity.GoogleTemplate;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.dto.google.GoogleReportSearchDto;
import jp.acepro.haishinsan.dto.google.GoogleTemplateDto;
import jp.acepro.haishinsan.form.GoogleCampaignForm;
import jp.acepro.haishinsan.form.GoogleReportSearchForm;
import jp.acepro.haishinsan.form.GoogleTemplateForm;

@Mapper
public interface GoogleMapper {

	GoogleMapper INSTANCE = Mappers.getMapper(GoogleMapper.class);

	// ＦＯＲＭからＤＴＯへ変換
	GoogleCampaignDto map(GoogleCampaignForm googleCampaignForm);

	GoogleTemplateDto map(GoogleTemplateForm googleTemplateForm);

	@Mapping(target = "startDate", source = "startDateFormat")
	@Mapping(target = "endDate", source = "endDateFormat")
	GoogleReportSearchDto map(GoogleReportSearchForm googleReportSearchForm);

	// ＥＮＴＩＴＹからＤＴＯへ変換
	@Mapping(target = "locationList", ignore = true)
	GoogleTemplateDto map(GoogleTemplate googleTemplate);

	List<GoogleTemplateDto> map(List<GoogleTemplate> googleTemplateList);

	// ＤＴＯからからＦＯＲＭへ変換
	GoogleCampaignForm map(GoogleTemplateDto googleTemplateDto);

	GoogleTemplateForm mapDtoToForm(GoogleTemplateDto googleTemplateDto);

	// ＤＴＯからからＥＮＴＩＴＹへ変換
	@Mapping(target = "locationList", ignore = true)
	GoogleTemplate mapDtotoEntitiy(GoogleTemplateDto googleTemplateDto);
}
