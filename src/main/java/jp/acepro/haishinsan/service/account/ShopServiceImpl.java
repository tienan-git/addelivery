package jp.acepro.haishinsan.service.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.DspTemplateCustomDao;
import jp.acepro.haishinsan.dao.DspTemplateDao;
import jp.acepro.haishinsan.dao.FacebookTemplateCustomDao;
import jp.acepro.haishinsan.dao.FacebookTemplateDao;
import jp.acepro.haishinsan.dao.GoogleTemplateCustomDao;
import jp.acepro.haishinsan.dao.GoogleTemplateDao;
import jp.acepro.haishinsan.dao.ShopCustomDao;
import jp.acepro.haishinsan.dao.ShopDao;
import jp.acepro.haishinsan.dao.TwitterTemplateCustomDao;
import jp.acepro.haishinsan.dao.TwitterTemplateDao;
import jp.acepro.haishinsan.db.entity.DspTemplate;
import jp.acepro.haishinsan.db.entity.FacebookTemplate;
import jp.acepro.haishinsan.db.entity.GoogleTemplate;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.db.entity.TwitterTemplate;
import jp.acepro.haishinsan.dto.ShopDto;
import jp.acepro.haishinsan.dto.UserDto;
import jp.acepro.haishinsan.entity.ShopWithAgency;
import jp.acepro.haishinsan.entity.ShopWithCorporation;
import jp.acepro.haishinsan.entity.UserWithAgency;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.google.GoogleTemplateService;
import jp.acepro.haishinsan.service.twitter.TwitterTemplateService;
import jp.acepro.haishinsan.util.ContextUtil;

@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	ShopCustomDao shopCustomDao;

	@Autowired
	ShopDao shopDao;

	private final String emailFormat = "^[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+(\\.[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+)*+(.*)@[a-zA-Z0-9][a-zA-Z0-9\\-]*(\\.[a-zA-Z0-9\\-]+)+$";

	@Override
	public List<ShopDto> search() {

		List<ShopWithAgency> shopList = shopCustomDao.selectAll();

		List<ShopDto> shopDtoList = new ArrayList<ShopDto>();

		for (ShopWithAgency shop : shopList) {

			ShopDto shopDto = new ShopDto();

			shopDto.setShopId(shop.getShopId());
			shopDto.setShopName(shop.getShopName());
			shopDto.setCorporationId(shop.getCorporationId());
			shopDto.setCorporationName(shop.getCorporationName());
			shopDto.setAgencyId(shop.getAgencyId());
			shopDto.setAgencyName(shop.getAgencyName());

			shopDtoList.add(shopDto);
		}

		return shopDtoList;
	}

	@Autowired
	TwitterTemplateService twitterTemplateService;
	@Autowired
	DspApiService dspApiService;
	@Autowired
	FacebookService facebookService;
	@Autowired
	GoogleTemplateService googleTemplateService;

	@Transactional
	@Override
	public ShopDto create(ShopDto shopDto) {
		String invalidEmail = isValidEmail(shopDto.getShopMailList());
		if (!"".equals(invalidEmail)) {
			throw new BusinessException(ErrorCodeConstant.E10004, invalidEmail);
		}
		invalidEmail = isValidEmail(shopDto.getSalesMailList());
		if (!"".equals(invalidEmail)) {
			throw new BusinessException(ErrorCodeConstant.E10004, invalidEmail);
		}
		// DTO->Entity
		Shop shop = new Shop();
		shop.setShopName(shopDto.getShopName());
		shop.setCorporationId(shopDto.getCorporationId());
		shop.setDspDistributionRatio(shopDto.getDspDistributionRatio());
		shop.setGoogleAccountId(shopDto.getGoogleAccountId());
		shop.setTwitterAccountId(shopDto.getTwitterAccountId());
		shop.setGoogleDistributionRatio(shopDto.getGoogleDistributionRatio());
		shop.setFacebookDistributionRatio(shopDto.getFacebookDistributionRatio());
		shop.setTwitterDistributionRatio(shopDto.getTwitterDistributionRatio());
		shop.setSalesCheckFlag(shopDto.getSalesCheckFlag());
		shop.setMarginRatio(shopDto.getMarginRatio());
		shop.setDspUserId(shopDto.getDspUserId());
		shop.setFacebookPageId(shopDto.getFacebookPageId());
		shop.setSalesMailList(shopDto.getSalesMailList());
		shop.setShopMailList(shopDto.getShopMailList());

		// DB access
		shopDao.insert(shop);
		shopDto.setShopId(shop.getShopId());

		List<Shop> shopList = ContextUtil.getShopList();
		shopList.add(shop);

		// create default template
		dspApiService.createDefaultTemplate(shop.getShopId());
		facebookService.createDefaultTemplate(shop.getShopId());
		googleTemplateService.createDefaultTemplate(shop.getShopId());
		twitterTemplateService.createDefaultTemplate(shop.getShopId());

		return shopDto;
	}

	@Override
	public ShopDto getById(Long shopId) {

		ShopWithCorporation shopWithCorporation = shopCustomDao.selectById(shopId);
		ShopDto shopDto = new ShopDto();
		shopDto.setShopId(shopWithCorporation.getShopId());
		shopDto.setShopName(shopWithCorporation.getShopName());
		shopDto.setCorporationId(shopWithCorporation.getCorporationId());
		shopDto.setCorporationName(shopWithCorporation.getCorporationName());
		shopDto.setAgencyId(shopWithCorporation.getAgencyId());
		shopDto.setAgencyName(shopWithCorporation.getAgencyName());
		shopDto.setDspUserId(shopWithCorporation.getDspUserId());
		shopDto.setDspDistributionRatio(shopWithCorporation.getDspDistributionRatio());
		shopDto.setGoogleAccountId(shopWithCorporation.getGoogleAccountId());
		shopDto.setFacebookPageId(shopWithCorporation.getFacebookPageId());
		shopDto.setTwitterAccountId(shopWithCorporation.getTwitterAccountId());
		shopDto.setGoogleDistributionRatio(shopWithCorporation.getGoogleDistributionRatio());
		shopDto.setFacebookDistributionRatio(shopWithCorporation.getFacebookDistributionRatio());
		shopDto.setTwitterDistributionRatio(shopWithCorporation.getTwitterDistributionRatio());
		shopDto.setSalesCheckFlag(shopWithCorporation.getSalesCheckFlag());
		shopDto.setMarginRatio(shopWithCorporation.getMarginRatio());
		shopDto.setSalesMailList(shopWithCorporation.getSalesMailList());
		shopDto.setShopMailList(shopWithCorporation.getShopMailList());

		return shopDto;
	}

	@Override
	public List<UserDto> searchUsersByShopId(Long shopId) {
		List<UserWithAgency> userList = shopCustomDao.selectUsersByShopId(shopId);

		List<UserDto> userDtoList = new ArrayList<UserDto>();

		for (UserWithAgency user : userList) {

			UserDto userDto = new UserDto();
			userDto.setUserId(user.getUserId());
			userDto.setUserName(user.getUserName());
			userDto.setEmail(user.getEmail());
			userDto.setRoleId(user.getRoleId());
			userDtoList.add(userDto);
		}

		return userDtoList;
	}

	@Transactional
	@Override
	public void update(ShopDto shopDto) {

		String invalidEmail = isValidEmail(shopDto.getShopMailList());
		if (!"".equals(invalidEmail)) {
			throw new BusinessException(ErrorCodeConstant.E10004, invalidEmail);
		}
		invalidEmail = isValidEmail(shopDto.getSalesMailList());
		if (!"".equals(invalidEmail)) {
			throw new BusinessException(ErrorCodeConstant.E10004, invalidEmail);
		}

		Shop shop = null;
		if (ContextUtil.getCurrentShop().getShopId().equals(shopDto.getShopId())) {
			  shop =ContextUtil.getCurrentShop();
		}else {
			  shop = shopDao.selectById(shopDto.getShopId());
		}

		shop.setShopName(shopDto.getShopName());
		shop.setDspDistributionRatio(shopDto.getDspDistributionRatio());
		shop.setGoogleAccountId(shopDto.getGoogleAccountId());
		shop.setTwitterAccountId(shopDto.getTwitterAccountId());
		shop.setGoogleDistributionRatio(shopDto.getGoogleDistributionRatio());
		shop.setFacebookDistributionRatio(shopDto.getFacebookDistributionRatio());
		shop.setTwitterDistributionRatio(shopDto.getTwitterDistributionRatio());
		shop.setSalesCheckFlag(shopDto.getSalesCheckFlag());
		shop.setMarginRatio(shopDto.getMarginRatio());
		shop.setDspUserId(shopDto.getDspUserId());
		shop.setFacebookPageId(shopDto.getFacebookPageId());
		shop.setSalesMailList(shopDto.getSalesMailList());
		shop.setShopMailList(shopDto.getShopMailList());

		shopDao.update(shop);

	}

	@Autowired
	DspTemplateCustomDao dspTemplateCustomDao;
	@Autowired
	DspTemplateDao dspTemplateDao;
	@Autowired
	FacebookTemplateCustomDao facebookTemplateCustomDao;
	@Autowired
	FacebookTemplateDao facebookTemplateDao;
	@Autowired
	GoogleTemplateCustomDao googleTemplateCustomDao;
	@Autowired
	GoogleTemplateDao googleTemplateDao;
	@Autowired
	TwitterTemplateCustomDao twitterTemplateCustomDao;
	@Autowired
	TwitterTemplateDao twitterTemplateDao;

	@Transactional
	@Override
	public void delete(Long shopId) {

		List<UserWithAgency> userList = shopCustomDao.selectUsersByShopId(shopId);

		if (userList != null && !userList.isEmpty()) {
			throw new BusinessException(ErrorCodeConstant.E10003);
		}

		Shop shop = shopDao.selectById(shopId);
		shopDao.delete(shop);

		// 店舗配下のDSPテンプレートを削除
		List<DspTemplate> dspTemplateList = dspTemplateCustomDao.selectByShopId(shopId);
		dspTemplateDao.delete(dspTemplateList);
		// 店舗配下のFacebookテンプレートを削除
		List<FacebookTemplate> facebookTemplateList = facebookTemplateCustomDao.selectAll(shopId);
		facebookTemplateDao.delete(facebookTemplateList);
		// 店舗配下のGoogleテンプレートを削除
		List<GoogleTemplate> googleTemplateList = googleTemplateCustomDao.selectByShopId(shopId);
		googleTemplateDao.delete(googleTemplateList);
		// 店舗配下のTwitterテンプレートを削除
		List<TwitterTemplate> twitterTemplateList = twitterTemplateCustomDao.selectAll(shopId);
		twitterTemplateDao.delete(twitterTemplateList);

		List<Shop> shopList = ContextUtil.getShopList();
		shopList.remove(shop);

	}

	private String isValidEmail(String mailList) {
		if ("".equals(mailList.replaceAll(" ", ""))) {
			return "";
		}
		String[] array = mailList.replaceAll(" ", "").split(";");
		for (String str : array) {
			if (!str.matches(emailFormat)) {
				return str;
			}
		}
		return "";
	}
}
