package jp.acepro.haishinsan.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import jp.acepro.haishinsan.db.entity.GoogleTemplate;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.dto.google.GoogleReportSearchDto;
import jp.acepro.haishinsan.dto.google.GoogleTemplateDto;
import jp.acepro.haishinsan.form.GoogleCampaignForm;
import jp.acepro.haishinsan.form.GoogleReportSearchForm;
import jp.acepro.haishinsan.form.GoogleTemplateForm;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-09-06T22:22:59+0900",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.13.0.v20170516-1929, environment: Java 1.8.0_144 (Oracle Corporation)"
)
public class GoogleMapperImpl implements GoogleMapper {

    @Override
    public GoogleCampaignDto map(GoogleCampaignForm googleCampaignForm) {
        if ( googleCampaignForm == null ) {
            return null;
        }

        GoogleCampaignDto googleCampaignDto = new GoogleCampaignDto();

        googleCampaignDto.setAdType( googleCampaignForm.getAdType() );
        googleCampaignDto.setBudget( googleCampaignForm.getBudget() );
        googleCampaignDto.setCampaignName( googleCampaignForm.getCampaignName() );
        googleCampaignDto.setDeviceType( googleCampaignForm.getDeviceType() );
        googleCampaignDto.setEndDate( googleCampaignForm.getEndDate() );
        googleCampaignDto.setImageAdFinalPageUrl( googleCampaignForm.getImageAdFinalPageUrl() );
        List<MultipartFile> list = googleCampaignForm.getImageAdImageFileList();
        if ( list != null ) {
            googleCampaignDto.setImageAdImageFileList(       new ArrayList<MultipartFile>( list )
            );
        }
        List<Long> list_ = googleCampaignForm.getLocationList();
        if ( list_ != null ) {
            googleCampaignDto.setLocationList(       new ArrayList<Long>( list_ )
            );
        }
        googleCampaignDto.setResAdDescription( googleCampaignForm.getResAdDescription() );
        googleCampaignDto.setResAdFinalPageUrl( googleCampaignForm.getResAdFinalPageUrl() );
        List<MultipartFile> list__ = googleCampaignForm.getResAdImageFileList();
        if ( list__ != null ) {
            googleCampaignDto.setResAdImageFileList(       new ArrayList<MultipartFile>( list__ )
            );
        }
        googleCampaignDto.setResAdShortTitle( googleCampaignForm.getResAdShortTitle() );
        googleCampaignDto.setStartDate( googleCampaignForm.getStartDate() );
        googleCampaignDto.setTemplateId( googleCampaignForm.getTemplateId() );
        googleCampaignDto.setTemplateName( googleCampaignForm.getTemplateName() );
        googleCampaignDto.setTextAdDescription( googleCampaignForm.getTextAdDescription() );
        googleCampaignDto.setTextAdFinalPageUrl( googleCampaignForm.getTextAdFinalPageUrl() );
        googleCampaignDto.setTextAdTitle1( googleCampaignForm.getTextAdTitle1() );
        googleCampaignDto.setTextAdTitle2( googleCampaignForm.getTextAdTitle2() );
        googleCampaignDto.setUnitPriceType( googleCampaignForm.getUnitPriceType() );

        return googleCampaignDto;
    }

    @Override
    public GoogleTemplateDto map(GoogleTemplateForm googleTemplateForm) {
        if ( googleTemplateForm == null ) {
            return null;
        }

        GoogleTemplateDto googleTemplateDto = new GoogleTemplateDto();

        googleTemplateDto.setAdType( googleTemplateForm.getAdType() );
        googleTemplateDto.setBudget( googleTemplateForm.getBudget() );
        googleTemplateDto.setCampaignName( googleTemplateForm.getCampaignName() );
        googleTemplateDto.setDeviceType( googleTemplateForm.getDeviceType() );
        googleTemplateDto.setImageAdFinalPageUrl( googleTemplateForm.getImageAdFinalPageUrl() );
        List<Long> list = googleTemplateForm.getLocationList();
        if ( list != null ) {
            googleTemplateDto.setLocationList(       new ArrayList<Long>( list )
            );
        }
        googleTemplateDto.setResAdDescription( googleTemplateForm.getResAdDescription() );
        googleTemplateDto.setResAdFinalPageUrl( googleTemplateForm.getResAdFinalPageUrl() );
        googleTemplateDto.setResAdShortTitle( googleTemplateForm.getResAdShortTitle() );
        googleTemplateDto.setTemplateId( googleTemplateForm.getTemplateId() );
        googleTemplateDto.setTemplateName( googleTemplateForm.getTemplateName() );
        googleTemplateDto.setTemplatePriority( googleTemplateForm.getTemplatePriority() );
        googleTemplateDto.setTextAdDescription( googleTemplateForm.getTextAdDescription() );
        googleTemplateDto.setTextAdFinalPageUrl( googleTemplateForm.getTextAdFinalPageUrl() );
        googleTemplateDto.setTextAdTitle1( googleTemplateForm.getTextAdTitle1() );
        googleTemplateDto.setTextAdTitle2( googleTemplateForm.getTextAdTitle2() );
        googleTemplateDto.setUnitPriceType( googleTemplateForm.getUnitPriceType() );

        return googleTemplateDto;
    }

    @Override
    public GoogleReportSearchDto map(GoogleReportSearchForm googleReportSearchForm) {
        if ( googleReportSearchForm == null ) {
            return null;
        }

        GoogleReportSearchDto googleReportSearchDto = new GoogleReportSearchDto();

        googleReportSearchDto.setEndDate( googleReportSearchForm.getEndDateFormat() );
        googleReportSearchDto.setStartDate( googleReportSearchForm.getStartDateFormat() );
        List<Long> list = googleReportSearchForm.getCampaignIdList();
        if ( list != null ) {
            googleReportSearchDto.setCampaignIdList(       new ArrayList<Long>( list )
            );
        }
        List<Pair<Long, String>> list_ = googleReportSearchForm.getCampaignPairList();
        if ( list_ != null ) {
            googleReportSearchDto.setCampaignPairList(       new ArrayList<Pair<Long, String>>( list_ )
            );
        }
        googleReportSearchDto.setPeriod( googleReportSearchForm.getPeriod() );

        return googleReportSearchDto;
    }

    @Override
    public GoogleTemplateDto map(GoogleTemplate googleTemplate) {
        if ( googleTemplate == null ) {
            return null;
        }

        GoogleTemplateDto googleTemplateDto = new GoogleTemplateDto();

        googleTemplateDto.setAdType( googleTemplate.getAdType() );
        googleTemplateDto.setBudget( googleTemplate.getBudget() );
        googleTemplateDto.setCampaignName( googleTemplate.getCampaignName() );
        googleTemplateDto.setDeviceType( googleTemplate.getDeviceType() );
        googleTemplateDto.setImageAdFinalPageUrl( googleTemplate.getImageAdFinalPageUrl() );
        googleTemplateDto.setResAdDescription( googleTemplate.getResAdDescription() );
        googleTemplateDto.setResAdFinalPageUrl( googleTemplate.getResAdFinalPageUrl() );
        googleTemplateDto.setResAdShortTitle( googleTemplate.getResAdShortTitle() );
        googleTemplateDto.setTemplateId( googleTemplate.getTemplateId() );
        googleTemplateDto.setTemplateName( googleTemplate.getTemplateName() );
        googleTemplateDto.setTemplatePriority( googleTemplate.getTemplatePriority() );
        googleTemplateDto.setTextAdDescription( googleTemplate.getTextAdDescription() );
        googleTemplateDto.setTextAdFinalPageUrl( googleTemplate.getTextAdFinalPageUrl() );
        googleTemplateDto.setTextAdTitle1( googleTemplate.getTextAdTitle1() );
        googleTemplateDto.setTextAdTitle2( googleTemplate.getTextAdTitle2() );
        googleTemplateDto.setUnitPriceType( googleTemplate.getUnitPriceType() );

        return googleTemplateDto;
    }

    @Override
    public List<GoogleTemplateDto> map(List<GoogleTemplate> googleTemplateList) {
        if ( googleTemplateList == null ) {
            return null;
        }

        List<GoogleTemplateDto> list = new ArrayList<GoogleTemplateDto>();
        for ( GoogleTemplate googleTemplate : googleTemplateList ) {
            list.add( map( googleTemplate ) );
        }

        return list;
    }

    @Override
    public GoogleCampaignForm map(GoogleTemplateDto googleTemplateDto) {
        if ( googleTemplateDto == null ) {
            return null;
        }

        GoogleCampaignForm googleCampaignForm = new GoogleCampaignForm();

        googleCampaignForm.setAdType( googleTemplateDto.getAdType() );
        googleCampaignForm.setBudget( googleTemplateDto.getBudget() );
        googleCampaignForm.setCampaignName( googleTemplateDto.getCampaignName() );
        googleCampaignForm.setDeviceType( googleTemplateDto.getDeviceType() );
        googleCampaignForm.setImageAdFinalPageUrl( googleTemplateDto.getImageAdFinalPageUrl() );
        List<Long> list = googleTemplateDto.getLocationList();
        if ( list != null ) {
            googleCampaignForm.setLocationList(       new ArrayList<Long>( list )
            );
        }
        googleCampaignForm.setResAdDescription( googleTemplateDto.getResAdDescription() );
        googleCampaignForm.setResAdFinalPageUrl( googleTemplateDto.getResAdFinalPageUrl() );
        googleCampaignForm.setResAdShortTitle( googleTemplateDto.getResAdShortTitle() );
        googleCampaignForm.setTemplateId( googleTemplateDto.getTemplateId() );
        googleCampaignForm.setTemplateName( googleTemplateDto.getTemplateName() );
        googleCampaignForm.setTextAdDescription( googleTemplateDto.getTextAdDescription() );
        googleCampaignForm.setTextAdFinalPageUrl( googleTemplateDto.getTextAdFinalPageUrl() );
        googleCampaignForm.setTextAdTitle1( googleTemplateDto.getTextAdTitle1() );
        googleCampaignForm.setTextAdTitle2( googleTemplateDto.getTextAdTitle2() );
        googleCampaignForm.setUnitPriceType( googleTemplateDto.getUnitPriceType() );

        return googleCampaignForm;
    }

    @Override
    public GoogleTemplateForm mapDtoToForm(GoogleTemplateDto googleTemplateDto) {
        if ( googleTemplateDto == null ) {
            return null;
        }

        GoogleTemplateForm googleTemplateForm = new GoogleTemplateForm();

        googleTemplateForm.setAdType( googleTemplateDto.getAdType() );
        googleTemplateForm.setBudget( googleTemplateDto.getBudget() );
        googleTemplateForm.setCampaignName( googleTemplateDto.getCampaignName() );
        googleTemplateForm.setDeviceType( googleTemplateDto.getDeviceType() );
        googleTemplateForm.setImageAdFinalPageUrl( googleTemplateDto.getImageAdFinalPageUrl() );
        List<Long> list = googleTemplateDto.getLocationList();
        if ( list != null ) {
            googleTemplateForm.setLocationList(       new ArrayList<Long>( list )
            );
        }
        googleTemplateForm.setResAdDescription( googleTemplateDto.getResAdDescription() );
        googleTemplateForm.setResAdFinalPageUrl( googleTemplateDto.getResAdFinalPageUrl() );
        googleTemplateForm.setResAdShortTitle( googleTemplateDto.getResAdShortTitle() );
        googleTemplateForm.setTemplateId( googleTemplateDto.getTemplateId() );
        googleTemplateForm.setTemplateName( googleTemplateDto.getTemplateName() );
        googleTemplateForm.setTemplatePriority( googleTemplateDto.getTemplatePriority() );
        googleTemplateForm.setTextAdDescription( googleTemplateDto.getTextAdDescription() );
        googleTemplateForm.setTextAdFinalPageUrl( googleTemplateDto.getTextAdFinalPageUrl() );
        googleTemplateForm.setTextAdTitle1( googleTemplateDto.getTextAdTitle1() );
        googleTemplateForm.setTextAdTitle2( googleTemplateDto.getTextAdTitle2() );
        googleTemplateForm.setUnitPriceType( googleTemplateDto.getUnitPriceType() );

        return googleTemplateForm;
    }

    @Override
    public GoogleTemplate mapDtotoEntitiy(GoogleTemplateDto googleTemplateDto) {
        if ( googleTemplateDto == null ) {
            return null;
        }

        GoogleTemplate googleTemplate = new GoogleTemplate();

        googleTemplate.setTemplateId( googleTemplateDto.getTemplateId() );
        googleTemplate.setTemplateName( googleTemplateDto.getTemplateName() );
        googleTemplate.setTemplatePriority( googleTemplateDto.getTemplatePriority() );
        googleTemplate.setCampaignName( googleTemplateDto.getCampaignName() );
        googleTemplate.setBudget( googleTemplateDto.getBudget() );
        googleTemplate.setDeviceType( googleTemplateDto.getDeviceType() );
        googleTemplate.setUnitPriceType( googleTemplateDto.getUnitPriceType() );
        googleTemplate.setAdType( googleTemplateDto.getAdType() );
        googleTemplate.setResAdShortTitle( googleTemplateDto.getResAdShortTitle() );
        googleTemplate.setResAdDescription( googleTemplateDto.getResAdDescription() );
        googleTemplate.setResAdFinalPageUrl( googleTemplateDto.getResAdFinalPageUrl() );
        googleTemplate.setImageAdFinalPageUrl( googleTemplateDto.getImageAdFinalPageUrl() );
        googleTemplate.setTextAdFinalPageUrl( googleTemplateDto.getTextAdFinalPageUrl() );
        googleTemplate.setTextAdTitle1( googleTemplateDto.getTextAdTitle1() );
        googleTemplate.setTextAdTitle2( googleTemplateDto.getTextAdTitle2() );
        googleTemplate.setTextAdDescription( googleTemplateDto.getTextAdDescription() );

        return googleTemplate;
    }
}
