package jp.acepro.haishinsan.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import jp.acepro.haishinsan.dto.youtube.YoutubeIssueDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeReportSearchDto;
import jp.acepro.haishinsan.form.YoutubeIssueinputForm;
import jp.acepro.haishinsan.form.YoutubeReportSearchForm;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-09-12T20:35:01+0900",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.13.0.v20170516-1929, environment: Java 1.8.0_144 (Oracle Corporation)"
)
public class YoutubeMapperImpl implements YoutubeMapper {

    @Override
    public YoutubeReportSearchDto map(YoutubeReportSearchForm youtubeReportSearchForm) {
        if ( youtubeReportSearchForm == null ) {
            return null;
        }

        YoutubeReportSearchDto youtubeReportSearchDto = new YoutubeReportSearchDto();

        youtubeReportSearchDto.setEndDate( youtubeReportSearchForm.getEndDateFormat() );
        youtubeReportSearchDto.setStartDate( youtubeReportSearchForm.getStartDateFormat() );
        List<Long> list = youtubeReportSearchForm.getCampaignIdList();
        if ( list != null ) {
            youtubeReportSearchDto.setCampaignIdList(       new ArrayList<Long>( list )
            );
        }
        youtubeReportSearchDto.setPeriod( youtubeReportSearchForm.getPeriod() );

        return youtubeReportSearchDto;
    }

    @Override
    public YoutubeIssueDto map(YoutubeIssueinputForm youtubeIssueinputForm) {
        if ( youtubeIssueinputForm == null ) {
            return null;
        }

        YoutubeIssueDto youtubeIssueDto = new YoutubeIssueDto();

        youtubeIssueDto.setAdType( youtubeIssueinputForm.getAdType() );
        youtubeIssueDto.setArea( youtubeIssueinputForm.getArea() );
        youtubeIssueDto.setBudget( youtubeIssueinputForm.getBudget() );
        youtubeIssueDto.setCampaignId( youtubeIssueinputForm.getCampaignId() );
        youtubeIssueDto.setCampaignName( youtubeIssueinputForm.getCampaignName() );
        youtubeIssueDto.setEndDate( youtubeIssueinputForm.getEndDate() );
        youtubeIssueDto.setEndHour( youtubeIssueinputForm.getEndHour() );
        youtubeIssueDto.setEndMin( youtubeIssueinputForm.getEndMin() );
        youtubeIssueDto.setIssueId( youtubeIssueinputForm.getIssueId() );
        youtubeIssueDto.setLp( youtubeIssueinputForm.getLp() );
        youtubeIssueDto.setStartDate( youtubeIssueinputForm.getStartDate() );
        youtubeIssueDto.setStartHour( youtubeIssueinputForm.getStartHour() );
        youtubeIssueDto.setStartMin( youtubeIssueinputForm.getStartMin() );
        youtubeIssueDto.setVideoUrl( youtubeIssueinputForm.getVideoUrl() );

        return youtubeIssueDto;
    }
}
