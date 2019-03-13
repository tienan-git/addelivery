package jp.acepro.haishinsan.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import jp.acepro.haishinsan.db.entity.TwitterTemplate;
import jp.acepro.haishinsan.dto.twitter.TwitterAdsDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTemplateDto;
import jp.acepro.haishinsan.form.TwitterAdsInputForm;
import jp.acepro.haishinsan.form.TwitterTemplateInputForm;

@Mapper
public interface TwitterMapper {

	TwitterMapper INSTANCE = Mappers.getMapper(TwitterMapper.class);

	
	//TemplateDto →　From
	TwitterTemplateDto tempFormToDto(TwitterTemplateInputForm twittertemplateInputForm);
	
	@Mapping(target = "startTime" , expression = "java( jp.acepro.haishinsan.util.StringFormatter.dateFormat(twitterTemplateDto.getStartTime()))")
	@Mapping(target = "endTime" , expression = "java( jp.acepro.haishinsan.util.StringFormatter.dateFormat(twitterTemplateDto.getEndTime()))")
	@Mapping(target = "regions" , expression = "java( jp.acepro.haishinsan.util.TwitterUtil.formatToOneString(twitterTemplateDto.getRegions()))")
	TwitterTemplate tempDtoToEntity(TwitterTemplateDto twitterTemplateDto);
	
	
	@Mapping(target = "startTime" , expression = "java( jp.acepro.haishinsan.util.StringFormatter.formatToHyphen(twitterTemplate.getStartTime()))")
	@Mapping(target = "endTime" , expression = "java( jp.acepro.haishinsan.util.StringFormatter.formatToHyphen(twitterTemplate.getEndTime()))")
	@Mapping(target = "regions" , expression = "java( jp.acepro.haishinsan.util.TwitterUtil.formatStringToList(twitterTemplate.getRegions()))")
	TwitterTemplateDto tempEntityToDto(TwitterTemplate twitterTemplate);
	
	
	List<TwitterTemplateDto> tempListEntityToDto(List<TwitterTemplate> twitterTemplateList);

	TwitterTemplateInputForm tempDtoToForm(TwitterTemplateDto twitterTemplateDto);
	
	//AdsDto →　From
	TwitterAdsDto adsFormToDto(TwitterAdsInputForm twitterAdsInputForm);
}
