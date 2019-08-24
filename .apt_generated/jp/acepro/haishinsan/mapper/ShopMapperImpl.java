package jp.acepro.haishinsan.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.dto.account.ShopDto;
import jp.acepro.haishinsan.dto.account.UserDto;
import jp.acepro.haishinsan.form.ShopInputForm;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-08-24T14:36:44+0900",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.13.0.v20170516-1929, environment: Java 1.8.0_144 (Oracle Corporation)"
)
public class ShopMapperImpl implements ShopMapper {

    @Override
    public List<ShopDto> map(List<Shop> shopList) {
        if ( shopList == null ) {
            return null;
        }

        List<ShopDto> list = new ArrayList<ShopDto>();
        for ( Shop shop : shopList ) {
            list.add( map( shop ) );
        }

        return list;
    }

    @Override
    public ShopDto map(Shop shop) {
        if ( shop == null ) {
            return null;
        }

        ShopDto shopDto_ = new ShopDto();

        shopDto_.setCorporationId( shop.getCorporationId() );
        shopDto_.setDspDistributionRatio( shop.getDspDistributionRatio() );
        shopDto_.setDspUserId( shop.getDspUserId() );
        shopDto_.setFacebookDistributionRatio( shop.getFacebookDistributionRatio() );
        shopDto_.setFacebookPageId( shop.getFacebookPageId() );
        shopDto_.setGoogleAccountId( shop.getGoogleAccountId() );
        shopDto_.setGoogleDistributionRatio( shop.getGoogleDistributionRatio() );
        shopDto_.setMarginRatio( shop.getMarginRatio() );
        shopDto_.setSalesCheckFlag( shop.getSalesCheckFlag() );
        shopDto_.setSalesMailList( shop.getSalesMailList() );
        shopDto_.setShopId( shop.getShopId() );
        shopDto_.setShopMailList( shop.getShopMailList() );
        shopDto_.setShopName( shop.getShopName() );
        shopDto_.setTwitterAccountId( shop.getTwitterAccountId() );
        shopDto_.setTwitterDistributionRatio( shop.getTwitterDistributionRatio() );

        return shopDto_;
    }

    @Override
    public ShopDto map(ShopInputForm shopInputForm) {
        if ( shopInputForm == null ) {
            return null;
        }

        ShopDto shopDto = new ShopDto();

        shopDto.setAgencyId( shopInputForm.getAgencyId() );
        shopDto.setAgencyName( shopInputForm.getAgencyName() );
        shopDto.setCorporationId( shopInputForm.getCorporationId() );
        shopDto.setCorporationName( shopInputForm.getCorporationName() );
        shopDto.setDspDistributionRatio( shopInputForm.getDspDistributionRatio() );
        shopDto.setDspUserId( shopInputForm.getDspUserId() );
        shopDto.setFacebookDistributionRatio( shopInputForm.getFacebookDistributionRatio() );
        shopDto.setFacebookPageId( shopInputForm.getFacebookPageId() );
        shopDto.setGoogleAccountId( shopInputForm.getGoogleAccountId() );
        shopDto.setGoogleDistributionRatio( shopInputForm.getGoogleDistributionRatio() );
        shopDto.setMarginRatio( shopInputForm.getMarginRatio() );
        shopDto.setSalesCheckFlag( shopInputForm.getSalesCheckFlag() );
        shopDto.setSalesMailList( shopInputForm.getSalesMailList() );
        shopDto.setShopId( shopInputForm.getShopId() );
        shopDto.setShopMailList( shopInputForm.getShopMailList() );
        shopDto.setShopName( shopInputForm.getShopName() );
        shopDto.setTwitterAccountId( shopInputForm.getTwitterAccountId() );
        shopDto.setTwitterDistributionRatio( shopInputForm.getTwitterDistributionRatio() );
        List<UserDto> list = shopInputForm.getUserDtoList();
        if ( list != null ) {
            shopDto.setUserDtoList(       new ArrayList<UserDto>( list )
            );
        }

        return shopDto;
    }

    @Override
    public ShopInputForm mapToForm(ShopDto shopDto) {
        if ( shopDto == null ) {
            return null;
        }

        ShopInputForm shopInputForm = new ShopInputForm();

        shopInputForm.setAgencyId( shopDto.getAgencyId() );
        shopInputForm.setAgencyName( shopDto.getAgencyName() );
        shopInputForm.setCorporationId( shopDto.getCorporationId() );
        shopInputForm.setCorporationName( shopDto.getCorporationName() );
        shopInputForm.setDspDistributionRatio( shopDto.getDspDistributionRatio() );
        shopInputForm.setDspUserId( shopDto.getDspUserId() );
        shopInputForm.setFacebookDistributionRatio( shopDto.getFacebookDistributionRatio() );
        shopInputForm.setFacebookPageId( shopDto.getFacebookPageId() );
        shopInputForm.setGoogleAccountId( shopDto.getGoogleAccountId() );
        shopInputForm.setGoogleDistributionRatio( shopDto.getGoogleDistributionRatio() );
        shopInputForm.setMarginRatio( shopDto.getMarginRatio() );
        shopInputForm.setSalesCheckFlag( shopDto.getSalesCheckFlag() );
        shopInputForm.setSalesMailList( shopDto.getSalesMailList() );
        shopInputForm.setShopId( shopDto.getShopId() );
        shopInputForm.setShopMailList( shopDto.getShopMailList() );
        shopInputForm.setShopName( shopDto.getShopName() );
        shopInputForm.setTwitterAccountId( shopDto.getTwitterAccountId() );
        shopInputForm.setTwitterDistributionRatio( shopDto.getTwitterDistributionRatio() );
        List<UserDto> list = shopDto.getUserDtoList();
        if ( list != null ) {
            shopInputForm.setUserDtoList(       new ArrayList<UserDto>( list )
            );
        }

        return shopInputForm;
    }

    @Override
    public Shop map(ShopDto shopDto) {
        if ( shopDto == null ) {
            return null;
        }

        Shop shop = new Shop();

        shop.setShopId( shopDto.getShopId() );
        shop.setShopName( shopDto.getShopName() );
        shop.setCorporationId( shopDto.getCorporationId() );
        shop.setDspUserId( shopDto.getDspUserId() );
        shop.setGoogleAccountId( shopDto.getGoogleAccountId() );
        shop.setFacebookPageId( shopDto.getFacebookPageId() );
        shop.setTwitterAccountId( shopDto.getTwitterAccountId() );
        shop.setDspDistributionRatio( shopDto.getDspDistributionRatio() );
        shop.setGoogleDistributionRatio( shopDto.getGoogleDistributionRatio() );
        shop.setFacebookDistributionRatio( shopDto.getFacebookDistributionRatio() );
        shop.setTwitterDistributionRatio( shopDto.getTwitterDistributionRatio() );
        shop.setSalesCheckFlag( shopDto.getSalesCheckFlag() );
        shop.setMarginRatio( shopDto.getMarginRatio() );
        shop.setShopMailList( shopDto.getShopMailList() );
        shop.setSalesMailList( shopDto.getSalesMailList() );

        return shop;
    }
}
