package jp.acepro.haishinsan.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import jp.acepro.haishinsan.db.entity.FacebookTemplate;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.facebook.FbTemplateDto;
import jp.acepro.haishinsan.form.FbCampaignInputForm;
import jp.acepro.haishinsan.form.FbTemplateInputForm;


@Mapper
public interface FacebookMapper {

	FacebookMapper INSTANCE = Mappers.getMapper(FacebookMapper.class);

	FbTemplateDto map(FbTemplateInputForm fbTemplateInputForm);
	
	FbTemplateDto mapEntityToDto(FacebookTemplate facebookTemplate);

	FbTemplateInputForm mapDtoToForm(FbTemplateDto fbTemplateDto);
	
	FbTemplateDto mapFormToDto(FbTemplateInputForm fbTemplateInputForm);
	
	List<FbTemplateDto> mapListEntityToDto(List<FacebookTemplate> facebookTemplateList);
	
	FbCampaignDto map(FbCampaignInputForm fbCampaignInputForm);

	
	//FacebookTemplate map(FbTemplateDto fbTemplateDto);


}
