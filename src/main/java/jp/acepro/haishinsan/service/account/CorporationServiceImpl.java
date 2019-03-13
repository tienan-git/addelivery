package jp.acepro.haishinsan.service.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.CorporationCustomDao;
import jp.acepro.haishinsan.dao.CorporationDao;
import jp.acepro.haishinsan.db.entity.Corporation;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.dto.CorporationDto;
import jp.acepro.haishinsan.dto.ShopDto;
import jp.acepro.haishinsan.entity.CorporationWithAgency;
import jp.acepro.haishinsan.exception.BusinessException;


@Service
public class CorporationServiceImpl implements CorporationService {

    @Autowired
    CorporationCustomDao corporationCustomDao;

    @Autowired
    CorporationDao corporationDao;

    @Transactional
    @Override
    public List<CorporationDto> search() {

        List<CorporationWithAgency> corporationList = corporationCustomDao.selectAll();

        List<CorporationDto> corporationDtoList = new ArrayList<CorporationDto>();

        for (CorporationWithAgency corporationWithAgency : corporationList) {

            CorporationDto corporationDto = new CorporationDto();
            corporationDto.setCorporationId(corporationWithAgency.getCorporationId());
            corporationDto.setCorporationName(corporationWithAgency.getCorporationName());
            corporationDto.setAgencyId(corporationWithAgency.getAgencyId());
            corporationDto.setAgencyName(corporationWithAgency.getAgencyName());

            corporationDtoList.add(corporationDto);
        }

        return corporationDtoList;
    }

    @Transactional
    @Override
    public CorporationDto create(CorporationDto corporationDto) {

        // DTO->Entity
        Corporation corporation = new Corporation();
        corporation.setCorporationId(corporationDto.getCorporationId());
        corporation.setCorporationName(corporationDto.getCorporationName());
        corporation.setAgencyId(corporationDto.getAgencyId());

        // DB access
        corporationDao.insert(corporation);

        // Entity->DTO
        CorporationDto newCorporationDto = new CorporationDto();
        newCorporationDto.setCorporationId(corporation.getCorporationId());
        newCorporationDto.setCorporationName(corporation.getCorporationName());
        newCorporationDto.setAgencyId(corporation.getAgencyId());

        return newCorporationDto;
    }

    @Transactional
    @Override
    public CorporationDto getById(Long corporationId) {

    	CorporationWithAgency corporationWithAgency = corporationCustomDao.selectById(corporationId);
        CorporationDto corporationDto = new CorporationDto();
        corporationDto.setCorporationId(corporationWithAgency.getCorporationId());
        corporationDto.setCorporationName(corporationWithAgency.getCorporationName());
        corporationDto.setAgencyId(corporationWithAgency.getAgencyId());
        corporationDto.setAgencyName(corporationWithAgency.getAgencyName());

        return corporationDto;

    }

    @Transactional
    @Override
    public void update(CorporationDto corporationDto) {

        Corporation corporation = corporationDao.selectById(corporationDto.getCorporationId());

        corporation.setCorporationName(corporationDto.getCorporationName());
        corporationDao.update(corporation);

    }

    @Transactional
    @Override
    public void delete(Long corporationId) {
    	
    	List<Shop> shopList = corporationCustomDao.selectShopsByCorpId(corporationId);
    	
    	if (shopList != null && !shopList.isEmpty()) {
    		throw new BusinessException(ErrorCodeConstant.E10002);
		}

        Corporation corporation = corporationDao.selectById(corporationId);
        corporationDao.delete(corporation);

    }

	@Override
	public List<ShopDto> searchShopsByCorpId(Long corporationId) {
        List<Shop> shopList = corporationCustomDao.selectShopsByCorpId(corporationId);

        List<ShopDto> shopDtoList = new ArrayList<ShopDto>();

        for (Shop shop : shopList) {

        	ShopDto shopDto = new ShopDto();
        	shopDto.setShopId(shop.getShopId());
        	shopDto.setShopName(shop.getShopName());
        	shopDto.setCorporationId(shop.getCorporationId());
        	shopDto.setGoogleAccountId(shop.getGoogleAccountId());
        	shopDto.setTwitterAccountId(shop.getTwitterAccountId());
        	shopDto.setDspDistributionRatio(shop.getDspDistributionRatio());
        	shopDto.setGoogleDistributionRatio(shop.getGoogleDistributionRatio());
        	shopDto.setFacebookDistributionRatio(shop.getFacebookDistributionRatio());
        	shopDto.setTwitterDistributionRatio(shop.getTwitterDistributionRatio());
        	shopDto.setSalesCheckFlag(shop.getSalesCheckFlag());
        	shopDto.setMarginRatio(shop.getMarginRatio());

            shopDtoList.add(shopDto);
        }

        return shopDtoList;
	}

}
