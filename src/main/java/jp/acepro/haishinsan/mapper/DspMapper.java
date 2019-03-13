package jp.acepro.haishinsan.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import jp.acepro.haishinsan.db.entity.DspTemplate;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDto;
import jp.acepro.haishinsan.dto.dsp.DspTemplateDto;
import jp.acepro.haishinsan.form.DspCampaignInputForm;
import jp.acepro.haishinsan.form.DspTemplateInputForm;

@Mapper
public interface DspMapper {

	DspMapper INSTANCE = Mappers.getMapper(DspMapper.class);

	DspTemplateDto tempFormToDto(DspTemplateInputForm dsptemplateInputForm);
	
	DspTemplate tempDtoToEntity(DspTemplateDto dspTemplateDto);
	
	DspTemplateDto tempEntityToDto(DspTemplate dspTemplate);

	List<DspTemplateDto> tempListEntityToDto(List<DspTemplate> dspTemplateList);

	DspTemplateInputForm tempDtoToForm(DspTemplateDto dspTemplateDto);

	DspCampaignDto campFormToDto(DspCampaignInputForm dspCampaignInputForm);
}
