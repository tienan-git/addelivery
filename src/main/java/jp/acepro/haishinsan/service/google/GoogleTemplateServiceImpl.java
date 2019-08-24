package jp.acepro.haishinsan.service.google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.GoogleTemplateCustomDao;
import jp.acepro.haishinsan.dao.GoogleTemplateDao;
import jp.acepro.haishinsan.db.entity.GoogleTemplate;
import jp.acepro.haishinsan.dto.google.GoogleTemplateDto;
import jp.acepro.haishinsan.enums.DeviceType;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.UnitPriceType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.mapper.GoogleMapper;
import jp.acepro.haishinsan.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoogleTemplateServiceImpl implements GoogleTemplateService {

	@Autowired
	GoogleTemplateDao googleTemplateDao;

	@Autowired
	GoogleTemplateCustomDao googleTemplateCustomDao;

	// テンプレート作成（新規ユーザー時のデフォルトテンプレート作成）
	@Override
	public void createDefaultTemplate(long shopId) {

		// テンプレート作成（クリック重視）
		GoogleTemplate googleTemplate1 = new GoogleTemplate();
		googleTemplate1.setTemplateName("クリック重視");
		googleTemplate1.setShopId(shopId);
		googleTemplate1.setTemplatePriority(1);
		googleTemplate1.setDeviceType(DeviceType.ALL.getValue());
		googleTemplate1.setUnitPriceType(UnitPriceType.CLICK.getValue());
		googleTemplate1.setAdType(GoogleAdType.IMAGE.getValue());
		googleTemplateDao.insert(googleTemplate1);

		// テンプレート作成（表示重視）
		GoogleTemplate googleTemplate2 = new GoogleTemplate();
		googleTemplate2.setTemplateName("表示重視");
		googleTemplate2.setShopId(shopId);
		googleTemplate2.setTemplatePriority(2);
		googleTemplate2.setDeviceType(DeviceType.ALL.getValue());
		googleTemplate2.setUnitPriceType(UnitPriceType.DISPLAY.getValue());
		googleTemplate2.setAdType(GoogleAdType.IMAGE.getValue());
		googleTemplateDao.insert(googleTemplate2);

		// テンプレート作成（カスタム）
		GoogleTemplate googleTemplate3 = new GoogleTemplate();
		googleTemplate3.setTemplateName("カスタム");
		googleTemplate3.setShopId(shopId);
		googleTemplate3.setTemplatePriority(3);
		googleTemplate3.setDeviceType(DeviceType.ALL.getValue());
		googleTemplate3.setUnitPriceType(UnitPriceType.CLICK.getValue());
		googleTemplate3.setAdType(GoogleAdType.IMAGE.getValue());
		googleTemplateDao.insert(googleTemplate3);
	}

	// テンプレート取得（店舗IDより）
	@Override
	@Transactional
	public List<GoogleTemplateDto> getTemplateList(Long shopId) {

		List<GoogleTemplateDto> googleTemplateDtoList = new ArrayList<GoogleTemplateDto>();

		// テンプレート取得
		List<GoogleTemplate> googleTemplateList = googleTemplateCustomDao.selectByShopId(shopId);
		if (googleTemplateList.size() > 0) {
			for (GoogleTemplate googleTemplate : googleTemplateList) {
				// 地域以外設定
				GoogleTemplateDto googleTemplateDto = GoogleMapper.INSTANCE.map(googleTemplate);
				// 地域設定
				List<String> strList = new ArrayList<String>();
				List<Long> longList = new ArrayList<Long>();
				if (googleTemplate.getLocationList() != null && !googleTemplate.getLocationList().isEmpty()) {
					strList = Arrays.asList(googleTemplate.getLocationList().split(","));
					longList = strList.stream().map(Long::parseLong).collect(Collectors.toList());
				}
				googleTemplateDto.setLocationList(longList);
				googleTemplateDtoList.add(googleTemplateDto);
			}
		}
		return googleTemplateDtoList;
	}

	// テンプレート取得（テンプレートIDより）
	@Override
	@Transactional
	public GoogleTemplateDto getTemplate(Long templateId) {

		// テンプレート取得
		GoogleTemplate googleTemplate = googleTemplateDao.selectById(templateId);

		// 地域以外設定
		GoogleTemplateDto googleTemplateDto = GoogleMapper.INSTANCE.map(googleTemplate);

		// 地域設定
		List<String> strList = new ArrayList<String>();
		List<Long> longList = new ArrayList<Long>();
		if (googleTemplate.getLocationList() != null && !googleTemplate.getLocationList().isEmpty()) {
			strList = Arrays.asList(googleTemplate.getLocationList().split(","));
			longList = strList.stream().map(Long::parseLong).collect(Collectors.toList());
		}
		googleTemplateDto.setLocationList(longList);

		return googleTemplateDto;
	}

	// テンプレート作成
	@Override
	@Transactional
	public void createTemplate(GoogleTemplateDto googleTemplateDto) {

		// 例外処理
		// テンプレート名チェック
		if (googleTemplateCustomDao
				.selectByTemplateName(ContextUtil.getCurrentShop().getShopId(), googleTemplateDto.getTemplateName())
				.size() > 0) {
			// 該当テンプレート名が既に登録されたため、修正してください。
			throw new BusinessException(ErrorCodeConstant.E70004);
		}

		// テンプレート優先度チェック
		if (googleTemplateCustomDao.selectByTemplatePriority(ContextUtil.getCurrentShop().getShopId(),
				googleTemplateDto.getTemplatePriority()).size() > 0) {
			// 該当テンプレート優先度が既に登録されたため、修正してください。
			throw new BusinessException(ErrorCodeConstant.E70007);
		}

		// 店舗、地域以外設定
		GoogleTemplate googleTemplate = GoogleMapper.INSTANCE.mapDtotoEntitiy(googleTemplateDto);

		// 店舗設定
		googleTemplate.setShopId(ContextUtil.getCurrentShop().getShopId());

		// 地域設定
		List<String> strList = googleTemplateDto.getLocationList().stream().map(String::valueOf)
				.collect(Collectors.toList());
		String strText = String.join(",", strList);
		googleTemplate.setLocationList(strText);

		// テンプレート作成
		googleTemplateDao.insert(googleTemplate);
		googleTemplateDto.setTemplateId(googleTemplate.getTemplateId());
	}

	// テンプレート更新
	@Override
	@Transactional
	public void updateTemplate(GoogleTemplateDto googleTemplateDto) {

		// 例外処理
		// テンプレート名チェック
		List<GoogleTemplate> googleTemplateListOld1 = googleTemplateCustomDao
				.selectByTemplateName(ContextUtil.getCurrentShop().getShopId(), googleTemplateDto.getTemplateName());
		if (googleTemplateListOld1.stream()
				.filter(obj -> !obj.getTemplateId().equals(googleTemplateDto.getTemplateId())).count() > 0) {
			// 該当テンプレート名が既に登録されたため、修正してください。
			throw new BusinessException(ErrorCodeConstant.E70004);
		}

		// テンプレート名チェック
		List<GoogleTemplate> googleTemplateListOld2 = googleTemplateCustomDao.selectByTemplatePriority(
				ContextUtil.getCurrentShop().getShopId(), googleTemplateDto.getTemplatePriority());
		if (googleTemplateListOld2.stream()
				.filter(obj -> !obj.getTemplateId().equals(googleTemplateDto.getTemplateId())).count() > 0) {
			// 該当テンプレート優先度が既に登録されたため、修正してください。
			throw new BusinessException(ErrorCodeConstant.E70007);
		}

		// 店舗、地域以外設定
		GoogleTemplate googleTemplateNew = GoogleMapper.INSTANCE.mapDtotoEntitiy(googleTemplateDto);

		// 店舗設定
		googleTemplateNew.setShopId(ContextUtil.getCurrentShop().getShopId());

		// 地域設定
		List<String> strList = googleTemplateDto.getLocationList().stream().map(String::valueOf)
				.collect(Collectors.toList());
		String strText = String.join(",", strList);
		googleTemplateNew.setLocationList(strText);

		// テンプレート取得
		GoogleTemplate googleTemplateOld = googleTemplateDao.selectById(googleTemplateDto.getTemplateId());

		// テンプレート更新
		googleTemplateOld.setTemplateName(googleTemplateNew.getTemplateName());
		googleTemplateOld.setTemplatePriority(googleTemplateNew.getTemplatePriority());
		googleTemplateOld.setCampaignName(googleTemplateNew.getCampaignName());
		googleTemplateOld.setBudget(googleTemplateNew.getBudget());
		googleTemplateOld.setDeviceType(googleTemplateNew.getDeviceType());
		googleTemplateOld.setUnitPriceType(googleTemplateNew.getUnitPriceType());
		googleTemplateOld.setLocationList(googleTemplateNew.getLocationList());
		googleTemplateOld.setAdType(googleTemplateNew.getAdType());
		googleTemplateOld.setResAdShortTitle(googleTemplateNew.getResAdShortTitle());
		googleTemplateOld.setResAdDescription(googleTemplateNew.getResAdDescription());
		googleTemplateOld.setResAdFinalPageUrl(googleTemplateNew.getResAdFinalPageUrl());
		googleTemplateOld.setImageAdFinalPageUrl(googleTemplateNew.getImageAdFinalPageUrl());
		googleTemplateOld.setTextAdDescription(googleTemplateNew.getTextAdDescription());
		googleTemplateOld.setTextAdFinalPageUrl(googleTemplateNew.getTextAdFinalPageUrl());
		googleTemplateOld.setTextAdTitle1(googleTemplateNew.getTextAdTitle1());
		googleTemplateOld.setTextAdTitle2(googleTemplateNew.getTextAdTitle2());
		googleTemplateDao.update(googleTemplateOld);
	}

	// テンプレート削除
	@Override
	@Transactional
	public void deleteTemplate(Long templateId) {

		// テンプレート取得
		GoogleTemplate googleTemplateOld = googleTemplateDao.selectById(templateId);

		// テンプレート削除
		googleTemplateOld.setIsActived(Flag.OFF.getValue());
		googleTemplateDao.update(googleTemplateOld);
	}
}
