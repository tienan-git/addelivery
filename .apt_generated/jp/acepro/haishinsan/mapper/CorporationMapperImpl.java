package jp.acepro.haishinsan.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import jp.acepro.haishinsan.dto.account.CorporationDto;
import jp.acepro.haishinsan.dto.account.ShopDto;
import jp.acepro.haishinsan.form.CorporationInputForm;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-09-12T20:35:01+0900",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.13.0.v20170516-1929, environment: Java 1.8.0_144 (Oracle Corporation)"
)
public class CorporationMapperImpl implements CorporationMapper {

    @Override
    public CorporationDto map(CorporationInputForm corporationInputForm) {
        if ( corporationInputForm == null ) {
            return null;
        }

        CorporationDto corporationDto = new CorporationDto();

        corporationDto.setAgencyId( corporationInputForm.getAgencyId() );
        corporationDto.setAgencyName( corporationInputForm.getAgencyName() );
        corporationDto.setCorporationId( corporationInputForm.getCorporationId() );
        corporationDto.setCorporationName( corporationInputForm.getCorporationName() );
        List<ShopDto> list = corporationInputForm.getShopDtoList();
        if ( list != null ) {
            corporationDto.setShopDtoList(       new ArrayList<ShopDto>( list )
            );
        }

        return corporationDto;
    }

    @Override
    public CorporationInputForm mapToForm(CorporationDto corporationDto) {
        if ( corporationDto == null ) {
            return null;
        }

        CorporationInputForm corporationInputForm = new CorporationInputForm();

        corporationInputForm.setAgencyId( corporationDto.getAgencyId() );
        corporationInputForm.setAgencyName( corporationDto.getAgencyName() );
        corporationInputForm.setCorporationId( corporationDto.getCorporationId() );
        corporationInputForm.setCorporationName( corporationDto.getCorporationName() );
        List<ShopDto> list = corporationDto.getShopDtoList();
        if ( list != null ) {
            corporationInputForm.setShopDtoList(       new ArrayList<ShopDto>( list )
            );
        }

        return corporationInputForm;
    }
}
