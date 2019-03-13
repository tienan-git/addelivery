package jp.acepro.haishinsan.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.acepro.haishinsan.dao.CampaignKeywordCustomDao;
import jp.acepro.haishinsan.dao.FacebookAreaPriceCustomDao;
import jp.acepro.haishinsan.dao.GoogleAreaPriceCustomDao;
import jp.acepro.haishinsan.dao.TwitterRegionPriceCustomDao;
import jp.acepro.haishinsan.dao.YahooCustomDao;
import jp.acepro.haishinsan.db.entity.CampaignKeyword;
import jp.acepro.haishinsan.db.entity.FacebookAreaPrice;
import jp.acepro.haishinsan.db.entity.GoogleAreaPrice;
import jp.acepro.haishinsan.db.entity.Regions;
import jp.acepro.haishinsan.db.entity.YahooArea;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CodeMasterServiceImpl implements CodeMasterService {

	@Autowired
	GoogleAreaPriceCustomDao googleAreaPriceCustomDao;
	
	@Autowired
	FacebookAreaPriceCustomDao facebookAreaPriceCustomDao;
	
	@Autowired
	TwitterRegionPriceCustomDao twitterRegionPriceCustomDao;
	
	@Autowired
	YahooCustomDao yahooCustomDao;
	
	@Autowired
	CampaignKeywordCustomDao campaignKeywordCustomDao;

	public static List<Pair<Long, String>> googleAreaNameList;
	public static List<Pair<Long, Integer>> googleAreaUnitPriceClickList;
	public static List<Pair<Long, Integer>> googleAreaUnitPriceDisplayList;
	
	public static List<Pair<String, String>> twitterRegionList;
	public static List<Pair<String, Integer>> twitterRegionUnitPriceLinkClickList;
	public static List<Pair<String, Integer>> twitterRegionUnitPriceFollowsList;

	public static List<Pair<Long, String>> facebookAreaNameList;
	public static List<Pair<Long, Integer>> facebookAreaUnitPriceClickList;
	public static List<Pair<Long, Integer>> facebookAreaUnitPriceDisplayList;
	public static List<Pair<Long, String>> yahooAreaNameList;
	public static List<String> keywordNameList;
	
	@Transactional
	@Override
	public void getGoogleAreaList() {

		List<GoogleAreaPrice> googleAreaPriceList = googleAreaPriceCustomDao.selectAll();
		
		googleAreaNameList = new ArrayList<>();
		googleAreaUnitPriceClickList = new ArrayList<>();
		googleAreaUnitPriceDisplayList = new ArrayList<>();
		for (GoogleAreaPrice googleAreaPrice : googleAreaPriceList) {
			googleAreaNameList.add(Pair.of(googleAreaPrice.getAreaId(), googleAreaPrice.getAreaName()));
			googleAreaUnitPriceClickList.add(Pair.of(googleAreaPrice.getAreaId(), googleAreaPrice.getUnitPriceClick()));
			googleAreaUnitPriceDisplayList.add(Pair.of(googleAreaPrice.getAreaId(), googleAreaPrice.getUnitPriceDisplay()));
		}
	}
	
	@Transactional
	@Override
	public void getFacebookAreaList() {

		List<FacebookAreaPrice> facebookAreaPriceList = facebookAreaPriceCustomDao.selectAll();
		
		facebookAreaNameList = new ArrayList<>();
		facebookAreaUnitPriceClickList = new ArrayList<>();
		facebookAreaUnitPriceDisplayList = new ArrayList<>();
		for (FacebookAreaPrice facebookAreaPrice : facebookAreaPriceList) {
			facebookAreaNameList.add(Pair.of(facebookAreaPrice.getAreaId(), facebookAreaPrice.getAreaName()));
			facebookAreaUnitPriceClickList.add(Pair.of(facebookAreaPrice.getAreaId(), facebookAreaPrice.getUnitPriceClick()));
			facebookAreaUnitPriceDisplayList.add(Pair.of(facebookAreaPrice.getAreaId(), facebookAreaPrice.getUnitPriceDisplay()));
		}
	}

	@Transactional
	@Override
	public void getYahooAreaList() {
		List<YahooArea> yahooAreaList = yahooCustomDao.selectAllArea();
		
		yahooAreaNameList = new ArrayList<>();
		for (YahooArea yahooArea : yahooAreaList) {
			yahooAreaNameList.add(Pair.of(yahooArea.getAreaId(), yahooArea.getAreaName()));
		}
		
	}
	
	@Transactional
	@Override
	public void getKeywordNameList() {
		
		List<CampaignKeyword> campaignKeywordList = campaignKeywordCustomDao.selectAll();
		keywordNameList = new ArrayList<String>();
		for (CampaignKeyword campaignKeyword : campaignKeywordList) {
			keywordNameList.add(campaignKeyword.getKeywordName());
		}
	}
	
	//Twitter 都道府県検索
	@Transactional
	@Override
	public void searchRegions() {
		List<Regions> regionsList = twitterRegionPriceCustomDao.selectAll();
		twitterRegionList = new ArrayList<>();
		twitterRegionUnitPriceLinkClickList = new ArrayList<>();
		twitterRegionUnitPriceFollowsList = new ArrayList<>();
		for (Regions regions : regionsList) {
			twitterRegionList.add(Pair.of(regions.getTargetValue(), regions.getRegionsName()));
			twitterRegionUnitPriceLinkClickList.add(Pair.of(regions.getTargetValue(), regions.getLinkClickPrice()));
			twitterRegionUnitPriceFollowsList.add(Pair.of(regions.getTargetValue(), regions.getFollowerPrice()));
		}
	}
	
//	@Transactional
//	@Override
//	public void getYoutubeAreaList() {
//		List<YoutubeArea> youtubeAreaList = youtubeCustomDao.selectAllArea();
//		
//		yahooAreaNameList = new ArrayList<>();
//		for (YoutubeArea youtubeArea : youtubeAreaList) {
//			yahooAreaNameList.add(Pair.of(youtubeArea.getAreaId(), youtubeArea.getAreaName()));
//		}
//		
//	}
}
