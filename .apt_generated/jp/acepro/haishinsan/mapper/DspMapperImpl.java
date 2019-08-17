package jp.acepro.haishinsan.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import jp.acepro.haishinsan.db.entity.DspTemplate;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDto;
import jp.acepro.haishinsan.dto.dsp.DspTemplateDto;
import jp.acepro.haishinsan.form.DspCampaignInputForm;
import jp.acepro.haishinsan.form.DspTemplateInputForm;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-08-10T10:11:32+0900",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.13.0.v20170516-1929, environment: Java 1.8.0_144 (Oracle Corporation)"
)
public class DspMapperImpl implements DspMapper {

    @Override
    public DspTemplateDto tempFormToDto(DspTemplateInputForm dsptemplateInputForm) {
        if ( dsptemplateInputForm == null ) {
            return null;
        }

        DspTemplateDto dspTemplateDto = new DspTemplateDto();

        dspTemplateDto.setBidCpcPrice( dsptemplateInputForm.getBidCpcPrice() );
        dspTemplateDto.setBillingType( dsptemplateInputForm.getBillingType() );
        dspTemplateDto.setTemplateId( dsptemplateInputForm.getTemplateId() );
        dspTemplateDto.setTemplateName( dsptemplateInputForm.getTemplateName() );
        dspTemplateDto.setTemplatePriority( dsptemplateInputForm.getTemplatePriority() );

        return dspTemplateDto;
    }

    @Override
    public DspTemplate tempDtoToEntity(DspTemplateDto dspTemplateDto) {
        if ( dspTemplateDto == null ) {
            return null;
        }

        DspTemplate dspTemplate = new DspTemplate();

        dspTemplate.setTemplateId( dspTemplateDto.getTemplateId() );
        dspTemplate.setTemplateName( dspTemplateDto.getTemplateName() );
        dspTemplate.setTemplatePriority( dspTemplateDto.getTemplatePriority() );
        dspTemplate.setBillingType( dspTemplateDto.getBillingType() );
        dspTemplate.setBidCpcPrice( dspTemplateDto.getBidCpcPrice() );

        return dspTemplate;
    }

    @Override
    public DspTemplateDto tempEntityToDto(DspTemplate dspTemplate) {
        if ( dspTemplate == null ) {
            return null;
        }

        DspTemplateDto dspTemplateDto = new DspTemplateDto();

        dspTemplateDto.setBidCpcPrice( dspTemplate.getBidCpcPrice() );
        dspTemplateDto.setBillingType( dspTemplate.getBillingType() );
        dspTemplateDto.setTemplateId( dspTemplate.getTemplateId() );
        dspTemplateDto.setTemplateName( dspTemplate.getTemplateName() );
        dspTemplateDto.setTemplatePriority( dspTemplate.getTemplatePriority() );

        return dspTemplateDto;
    }

    @Override
    public List<DspTemplateDto> tempListEntityToDto(List<DspTemplate> dspTemplateList) {
        if ( dspTemplateList == null ) {
            return null;
        }

        List<DspTemplateDto> list = new ArrayList<DspTemplateDto>();
        for ( DspTemplate dspTemplate : dspTemplateList ) {
            list.add( tempEntityToDto( dspTemplate ) );
        }

        return list;
    }

    @Override
    public DspTemplateInputForm tempDtoToForm(DspTemplateDto dspTemplateDto) {
        if ( dspTemplateDto == null ) {
            return null;
        }

        DspTemplateInputForm dspTemplateInputForm = new DspTemplateInputForm();

        dspTemplateInputForm.setBidCpcPrice( dspTemplateDto.getBidCpcPrice() );
        dspTemplateInputForm.setBillingType( dspTemplateDto.getBillingType() );
        dspTemplateInputForm.setTemplateId( dspTemplateDto.getTemplateId() );
        dspTemplateInputForm.setTemplateName( dspTemplateDto.getTemplateName() );
        dspTemplateInputForm.setTemplatePriority( dspTemplateDto.getTemplatePriority() );

        return dspTemplateInputForm;
    }

    @Override
    public DspCampaignDto campFormToDto(DspCampaignInputForm dspCampaignInputForm) {
        if ( dspCampaignInputForm == null ) {
            return null;
        }

        DspCampaignDto dspCampaignDto = new DspCampaignDto();

        dspCampaignDto.setBudget( dspCampaignInputForm.getBudget() );
        dspCampaignDto.setCampaignName( dspCampaignInputForm.getCampaignName() );
        dspCampaignDto.setDeviceType( dspCampaignInputForm.getDeviceType() );
        dspCampaignDto.setEndDatetime( dspCampaignInputForm.getEndDatetime() );
        List<Integer> list = dspCampaignInputForm.getIdList();
        if ( list != null ) {
            dspCampaignDto.setIdList(       new ArrayList<Integer>( list )
            );
        }
        dspCampaignDto.setSegmentId( dspCampaignInputForm.getSegmentId() );
        dspCampaignDto.setStartDatetime( dspCampaignInputForm.getStartDatetime() );
        dspCampaignDto.setTemplateId( dspCampaignInputForm.getTemplateId() );

        return dspCampaignDto;
    }
}
