package jp.acepro.haishinsan.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import jp.acepro.haishinsan.dto.youtube.YoutubeIssueDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeReportSearchDto;
import jp.acepro.haishinsan.form.YoutubeIssueinputForm;
import jp.acepro.haishinsan.form.YoutubeReportSearchForm;

@Mapper
public interface YoutubeMapper {

	YoutubeMapper INSTANCE = Mappers.getMapper(YoutubeMapper.class);

	// ＦＯＲＭからＤＴＯへ変換
	@Mapping(target = "startDate", source = "startDateFormat")
	@Mapping(target = "endDate", source = "endDateFormat")
	YoutubeReportSearchDto map(YoutubeReportSearchForm youtubeReportSearchForm);

	YoutubeIssueDto map(YoutubeIssueinputForm youtubeIssueinputForm);
}
