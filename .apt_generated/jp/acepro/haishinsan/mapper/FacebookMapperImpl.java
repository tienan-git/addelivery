package jp.acepro.haishinsan.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import jp.acepro.haishinsan.db.entity.FacebookTemplate;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.facebook.FbIssueDto;
import jp.acepro.haishinsan.dto.facebook.FbTemplateDto;
import jp.acepro.haishinsan.form.FbCampaignInputForm;
import jp.acepro.haishinsan.form.FbIssueInputForm;
import jp.acepro.haishinsan.form.FbTemplateInputForm;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-09-02T20:06:41+0900",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.13.0.v20170516-1929, environment: Java 1.8.0_144 (Oracle Corporation)"
)
public class FacebookMapperImpl implements FacebookMapper {

    @Override
    public FbTemplateDto map(FbTemplateInputForm fbTemplateInputForm) {
        if ( fbTemplateInputForm == null ) {
            return null;
        }

        FbTemplateDto fbTemplateDto = new FbTemplateDto();

        fbTemplateDto.setDailyBudget( fbTemplateInputForm.getDailyBudget() );
        List<Long> list = fbTemplateInputForm.getLocationList();
        if ( list != null ) {
            fbTemplateDto.setLocationList(       new ArrayList<Long>( list )
            );
        }
        fbTemplateDto.setTemplateId( fbTemplateInputForm.getTemplateId() );
        fbTemplateDto.setTemplateName( fbTemplateInputForm.getTemplateName() );
        fbTemplateDto.setTemplatePriority( fbTemplateInputForm.getTemplatePriority() );
        fbTemplateDto.setUnitPriceType( fbTemplateInputForm.getUnitPriceType() );

        return fbTemplateDto;
    }

    @Override
    public FbTemplateDto mapEntityToDto(FacebookTemplate facebookTemplate) {
        if ( facebookTemplate == null ) {
            return null;
        }

        FbTemplateDto fbTemplateDto = new FbTemplateDto();

        fbTemplateDto.setDailyBudget( facebookTemplate.getDailyBudget() );
        fbTemplateDto.setGeolocation( facebookTemplate.getGeolocation() );
        fbTemplateDto.setShopId( facebookTemplate.getShopId() );
        fbTemplateDto.setTemplateId( facebookTemplate.getTemplateId() );
        fbTemplateDto.setTemplateName( facebookTemplate.getTemplateName() );
        fbTemplateDto.setTemplatePriority( facebookTemplate.getTemplatePriority() );
        fbTemplateDto.setUnitPriceType( facebookTemplate.getUnitPriceType() );

        return fbTemplateDto;
    }

    @Override
    public FbTemplateInputForm mapDtoToForm(FbTemplateDto fbTemplateDto) {
        if ( fbTemplateDto == null ) {
            return null;
        }

        FbTemplateInputForm fbTemplateInputForm = new FbTemplateInputForm();

        fbTemplateInputForm.setDailyBudget( fbTemplateDto.getDailyBudget() );
        List<Long> list = fbTemplateDto.getLocationList();
        if ( list != null ) {
            fbTemplateInputForm.setLocationList(       new ArrayList<Long>( list )
            );
        }
        fbTemplateInputForm.setTemplateId( fbTemplateDto.getTemplateId() );
        fbTemplateInputForm.setTemplateName( fbTemplateDto.getTemplateName() );
        fbTemplateInputForm.setTemplatePriority( fbTemplateDto.getTemplatePriority() );
        fbTemplateInputForm.setUnitPriceType( fbTemplateDto.getUnitPriceType() );

        return fbTemplateInputForm;
    }

    @Override
    public FbTemplateDto mapFormToDto(FbTemplateInputForm fbTemplateInputForm) {
        if ( fbTemplateInputForm == null ) {
            return null;
        }

        FbTemplateDto fbTemplateDto = new FbTemplateDto();

        fbTemplateDto.setDailyBudget( fbTemplateInputForm.getDailyBudget() );
        List<Long> list = fbTemplateInputForm.getLocationList();
        if ( list != null ) {
            fbTemplateDto.setLocationList(       new ArrayList<Long>( list )
            );
        }
        fbTemplateDto.setTemplateId( fbTemplateInputForm.getTemplateId() );
        fbTemplateDto.setTemplateName( fbTemplateInputForm.getTemplateName() );
        fbTemplateDto.setTemplatePriority( fbTemplateInputForm.getTemplatePriority() );
        fbTemplateDto.setUnitPriceType( fbTemplateInputForm.getUnitPriceType() );

        return fbTemplateDto;
    }

    @Override
    public List<FbTemplateDto> mapListEntityToDto(List<FacebookTemplate> facebookTemplateList) {
        if ( facebookTemplateList == null ) {
            return null;
        }

        List<FbTemplateDto> list = new ArrayList<FbTemplateDto>();
        for ( FacebookTemplate facebookTemplate : facebookTemplateList ) {
            list.add( mapEntityToDto( facebookTemplate ) );
        }

        return list;
    }

    @Override
    public FbCampaignDto map(FbCampaignInputForm fbCampaignInputForm) {
        if ( fbCampaignInputForm == null ) {
            return null;
        }

        FbCampaignDto fbCampaignDto = new FbCampaignDto();

        fbCampaignDto.setArrangePlace( fbCampaignInputForm.getArrangePlace() );
        fbCampaignDto.setBidAmount( fbCampaignInputForm.getBidAmount() );
        fbCampaignDto.setCampaignName( fbCampaignInputForm.getCampaignName() );
        if ( fbCampaignInputForm.getCampaignStatus() != null ) {
            fbCampaignDto.setCampaignStatus( Integer.parseInt( fbCampaignInputForm.getCampaignStatus() ) );
        }
        fbCampaignDto.setCheckStatus( fbCampaignInputForm.getCheckStatus() );
        fbCampaignDto.setDailyBudget( fbCampaignInputForm.getDailyBudget() );
        fbCampaignDto.setEndDate( fbCampaignInputForm.getEndDate() );
        fbCampaignDto.setLinkMessage( fbCampaignInputForm.getLinkMessage() );
        fbCampaignDto.setLinkUrl( fbCampaignInputForm.getLinkUrl() );
        List<Long> list = fbCampaignInputForm.getLocationList();
        if ( list != null ) {
            fbCampaignDto.setLocationList(       new ArrayList<Long>( list )
            );
        }
        fbCampaignDto.setPageId( fbCampaignInputForm.getPageId() );
        fbCampaignDto.setSegmentId( fbCampaignInputForm.getSegmentId() );
        fbCampaignDto.setStartDate( fbCampaignInputForm.getStartDate() );
        fbCampaignDto.setTemplateId( fbCampaignInputForm.getTemplateId() );
        fbCampaignDto.setUnitPriceType( fbCampaignInputForm.getUnitPriceType() );

        return fbCampaignDto;
    }

    @Override
    public FbIssueDto map(FbIssueInputForm fbIssueInputForm) {
        if ( fbIssueInputForm == null ) {
            return null;
        }

        FbIssueDto fbIssueDto = new FbIssueDto();

        fbIssueDto.setArrangePlace( fbIssueInputForm.getArrangePlace() );
        fbIssueDto.setBidAmount( fbIssueInputForm.getBidAmount() );
        fbIssueDto.setCampaignName( fbIssueInputForm.getCampaignName() );
        if ( fbIssueInputForm.getCampaignStatus() != null ) {
            fbIssueDto.setCampaignStatus( Integer.parseInt( fbIssueInputForm.getCampaignStatus() ) );
        }
        fbIssueDto.setCheckStatus( fbIssueInputForm.getCheckStatus() );
        fbIssueDto.setDailyBudget( fbIssueInputForm.getDailyBudget() );
        fbIssueDto.setEndHour( fbIssueInputForm.getEndHour() );
        fbIssueDto.setEndMin( fbIssueInputForm.getEndMin() );
        fbIssueDto.setEndTime( fbIssueInputForm.getEndTime() );
        fbIssueDto.setLinkMessage( fbIssueInputForm.getLinkMessage() );
        fbIssueDto.setLinkUrl( fbIssueInputForm.getLinkUrl() );
        List<Long> list = fbIssueInputForm.getLocationList();
        if ( list != null ) {
            fbIssueDto.setLocationList(       new ArrayList<Long>( list )
            );
        }
        fbIssueDto.setPageId( fbIssueInputForm.getPageId() );
        fbIssueDto.setSegmentId( fbIssueInputForm.getSegmentId() );
        fbIssueDto.setStartHour( fbIssueInputForm.getStartHour() );
        fbIssueDto.setStartMin( fbIssueInputForm.getStartMin() );
        fbIssueDto.setStartTime( fbIssueInputForm.getStartTime() );
        fbIssueDto.setTemplateId( fbIssueInputForm.getTemplateId() );
        fbIssueDto.setUnitPriceType( fbIssueInputForm.getUnitPriceType() );

        return fbIssueDto;
    }
}
