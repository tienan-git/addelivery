package jp.acepro.haishinsan.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import jp.acepro.haishinsan.dto.account.AgencyDto;
import jp.acepro.haishinsan.dto.account.CorporationDto;
import jp.acepro.haishinsan.form.AgencyInputForm;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-08-24T14:36:44+0900",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.13.0.v20170516-1929, environment: Java 1.8.0_144 (Oracle Corporation)"
)
public class AgencyMapperImpl implements AgencyMapper {

    @Override
    public AgencyDto map(AgencyInputForm agencyInputForm) {
        if ( agencyInputForm == null ) {
            return null;
        }

        AgencyDto agencyDto = new AgencyDto();

        agencyDto.setAgencyId( agencyInputForm.getAgencyId() );
        agencyDto.setAgencyName( agencyInputForm.getAgencyName() );
        List<CorporationDto> list = agencyInputForm.getCorporationDtoList();
        if ( list != null ) {
            agencyDto.setCorporationDtoList(       new ArrayList<CorporationDto>( list )
            );
        }

        return agencyDto;
    }

    @Override
    public AgencyInputForm mapToForm(AgencyDto agencyDto) {
        if ( agencyDto == null ) {
            return null;
        }

        AgencyInputForm agencyInputForm = new AgencyInputForm();

        agencyInputForm.setAgencyId( agencyDto.getAgencyId() );
        agencyInputForm.setAgencyName( agencyDto.getAgencyName() );
        List<CorporationDto> list = agencyDto.getCorporationDtoList();
        if ( list != null ) {
            agencyInputForm.setCorporationDtoList(       new ArrayList<CorporationDto>( list )
            );
        }

        return agencyInputForm;
    }
}
