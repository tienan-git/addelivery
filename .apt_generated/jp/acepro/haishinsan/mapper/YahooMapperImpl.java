package jp.acepro.haishinsan.mapper;

import javax.annotation.Generated;
import jp.acepro.haishinsan.dto.yahoo.YahooIssueDto;
import jp.acepro.haishinsan.form.YahooIssueinputForm;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-09-06T22:22:59+0900",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.13.0.v20170516-1929, environment: Java 1.8.0_144 (Oracle Corporation)"
)
public class YahooMapperImpl implements YahooMapper {

    @Override
    public YahooIssueDto map(YahooIssueinputForm yahooIssueinputForm) {
        if ( yahooIssueinputForm == null ) {
            return null;
        }

        YahooIssueDto yahooIssueDto = new YahooIssueDto();

        yahooIssueDto.setAdvDestination( yahooIssueinputForm.getAdvDestination() );
        yahooIssueDto.setBudget( yahooIssueinputForm.getBudget() );
        yahooIssueDto.setCampaignId( yahooIssueinputForm.getCampaignId() );
        yahooIssueDto.setCampaignName( yahooIssueinputForm.getCampaignName() );
        yahooIssueDto.setDeviceType( yahooIssueinputForm.getDeviceType() );
        yahooIssueDto.setEndDate( yahooIssueinputForm.getEndDate() );
        yahooIssueDto.setEndHour( yahooIssueinputForm.getEndHour() );
        yahooIssueDto.setEndMin( yahooIssueinputForm.getEndMin() );
        yahooIssueDto.setIssueId( yahooIssueinputForm.getIssueId() );
        yahooIssueDto.setStartDate( yahooIssueinputForm.getStartDate() );
        yahooIssueDto.setStartHour( yahooIssueinputForm.getStartHour() );
        yahooIssueDto.setStartMin( yahooIssueinputForm.getStartMin() );

        return yahooIssueDto;
    }

    @Override
    public YahooIssueinputForm mapToForm(YahooIssueDto yahooIssueDto) {
        if ( yahooIssueDto == null ) {
            return null;
        }

        YahooIssueinputForm yahooIssueinputForm = new YahooIssueinputForm();

        yahooIssueinputForm.setAdvDestination( yahooIssueDto.getAdvDestination() );
        yahooIssueinputForm.setBudget( yahooIssueDto.getBudget() );
        yahooIssueinputForm.setCampaignId( yahooIssueDto.getCampaignId() );
        yahooIssueinputForm.setCampaignName( yahooIssueDto.getCampaignName() );
        yahooIssueinputForm.setDeviceType( yahooIssueDto.getDeviceType() );
        yahooIssueinputForm.setEndDate( yahooIssueDto.getEndDate() );
        yahooIssueinputForm.setEndHour( yahooIssueDto.getEndHour() );
        yahooIssueinputForm.setEndMin( yahooIssueDto.getEndMin() );
        yahooIssueinputForm.setIssueId( yahooIssueDto.getIssueId() );
        yahooIssueinputForm.setStartDate( yahooIssueDto.getStartDate() );
        yahooIssueinputForm.setStartHour( yahooIssueDto.getStartHour() );
        yahooIssueinputForm.setStartMin( yahooIssueDto.getStartMin() );

        return yahooIssueinputForm;
    }
}
