package jp.acepro.haishinsan.service.dsp;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.bean.DspCreativeReportCsvBean;
import jp.acepro.haishinsan.bean.DspDateReportCsvBean;
import jp.acepro.haishinsan.bean.DspDeviceReportCsvBean;
import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.DspAdManageCustomDao;
import jp.acepro.haishinsan.dao.DspCampaignCustomDao;
import jp.acepro.haishinsan.dao.DspCreativeCustomDao;
import jp.acepro.haishinsan.dao.DspReportManageCustomDao;
import jp.acepro.haishinsan.dao.DspReportManageDao;
import jp.acepro.haishinsan.dao.DspTemplateCustomDao;
import jp.acepro.haishinsan.dao.DspTemplateDao;
import jp.acepro.haishinsan.dao.DspTokenCustomDao;
import jp.acepro.haishinsan.dao.DspTokenDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.dao.ShopCustomDao;
import jp.acepro.haishinsan.db.entity.CreativeManage;
import jp.acepro.haishinsan.db.entity.DspAdManage;
import jp.acepro.haishinsan.db.entity.DspCampaignManage;
import jp.acepro.haishinsan.db.entity.DspReportManage;
import jp.acepro.haishinsan.db.entity.DspTemplate;
import jp.acepro.haishinsan.db.entity.DspToken;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.dto.dsp.DspAdReportDto;
import jp.acepro.haishinsan.dto.dsp.DspAdReportReq;
import jp.acepro.haishinsan.dto.dsp.DspAdReportRes;
import jp.acepro.haishinsan.dto.dsp.DspAdReportResDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDetailDto;
import jp.acepro.haishinsan.dto.dsp.DspReportingGraphDto;
import jp.acepro.haishinsan.dto.dsp.DspReportingListDto;
import jp.acepro.haishinsan.dto.dsp.DspTemplateDto;
import jp.acepro.haishinsan.dto.dsp.DspTokenRes;
import jp.acepro.haishinsan.enums.BillingType;
import jp.acepro.haishinsan.enums.DspDeviceType;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.mapper.DspMapper;
import jp.acepro.haishinsan.service.BaseService;
import jp.acepro.haishinsan.util.CalculateUtil;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.ReportUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DspApiServiceImpl extends BaseService implements DspApiService {

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	DspTokenCustomDao dspTokenCustomDao;

	@Autowired
	DspTokenDao dspTokenDao;

	@Autowired
	DspTemplateDao dspTemplateDao;

	@Autowired
	DspTemplateCustomDao dspTemplateCustomDao;

	@Autowired
	DspApiService dspApiService;

	@Autowired
	DspCampaignCustomDao dspCampaignCustomDao;

	@Autowired
	DspAdManageCustomDao dspAdManageCustomDao;

	@Autowired
	DspReportManageDao dspReportManageDao;

	@Autowired
	DspReportManageCustomDao dspReportManageCustomDao;

	@Autowired
	DspCampaignService dspCampaignService;

	@Autowired
	DspCreativeCustomDao dspCreativeCustomDao;

	@Autowired
	ShopCustomDao shopCustomDao;

	@Autowired
	IssueDao issueDao;

	@Override
	@Transactional
	public DspToken getToken() {

		DspToken oldDspToken = dspTokenCustomDao.selectByDspTokenId();
		if (oldDspToken == null) {
			// Req URL組み立てる
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
			builder = builder.scheme(applicationProperties.getDspScheme());
			builder = builder.host(applicationProperties.getDspHost());
			builder = builder.path(applicationProperties.getDspApiGettoken());
			builder = builder.queryParam("id", applicationProperties.getDspAccountId());
			builder = builder.queryParam("password", applicationProperties.getDspPassword());
			String resource = builder.build().toUri().toString();

			DspTokenRes dspTokenRes = null;
			try {
				dspTokenRes = call(resource, HttpMethod.GET, null, null, DspTokenRes.class);
			} catch (Exception e) {
				log.debug("DSP:Token取得エラー、リクエストURL:{}", resource);
				e.printStackTrace();
				throw new SystemException("システムエラー発生しました");
			}
			// Res Tokenを返却
			DspToken dspToken = new DspToken();
			dspToken.setToken(dspTokenRes.getToken());
			dspToken.setRefreshToken(dspTokenRes.getRefresh_token());
			dspTokenDao.insert(dspToken);
			return dspToken;
		}
		LocalDateTime a = oldDspToken.getUpdatedAt();
		LocalDateTime b = LocalDateTime.now();
		int i = (int) a.until(b, ChronoUnit.DAYS);
		if (oldDspToken != null && i >= 1) {
			oldDspToken = refreshToken();
			return oldDspToken;
		}
		return oldDspToken;

	}

	@Transactional
	private DspToken refreshToken() {

		DspToken oldDspToken = dspTokenDao.selectById(1l);

		// Req refreshToken URL組み立てる
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder = builder.scheme(applicationProperties.getDspScheme());
		builder = builder.host(applicationProperties.getDspHost());
		builder = builder.path(applicationProperties.getDspApiRefreshtoken());
		builder = builder.queryParam("token", oldDspToken.getToken());
		builder = builder.queryParam("refresh_token", oldDspToken.getRefreshToken());
		String resource = builder.build().toUri().toString();

		DspTokenRes dspTokenRes = null;
		try {
			dspTokenRes = call(resource, HttpMethod.GET, null, null, DspTokenRes.class);
		} catch (Exception e) {
			log.debug("DSP:Tokenリフレッシュエラー、リクエストURL:{}", resource);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
		// Res Tokenを返却
		oldDspToken.setToken(dspTokenRes.getToken());
		oldDspToken.setRefreshToken(dspTokenRes.getRefresh_token());
		dspTokenDao.update(oldDspToken);
		return oldDspToken;
	}

	@Override
	@Transactional
	public DspTemplateDto createTemplate(DspTemplateDto dspTemplateDto) {

		// テンプレート名チェック
		DspTemplate existName = dspTemplateCustomDao.selectByName(dspTemplateDto.getTemplateName(), ContextUtil.getCurrentShopId());
		if (existName != null) {
			throw new BusinessException(ErrorCodeConstant.E30001);
		}

		// テンプレート優先順チェック
		DspTemplate existPriority = dspTemplateCustomDao.selectByPriority(dspTemplateDto.getTemplatePriority(), ContextUtil.getCurrentShopId());
		if (existPriority != null) {
			throw new BusinessException(ErrorCodeConstant.E30002);
		}

		// DtoからEntityまで変更して、他のパラメータが追加する
		DspTemplate dspTemplate = DspMapper.INSTANCE.tempDtoToEntity(dspTemplateDto);
		dspTemplate.setShopId(ContextUtil.getCurrentShop().getShopId());
		dspTemplate.setTemplatePriority(dspTemplateDto.getTemplatePriority());
		dspTemplate.setTemplateName(dspTemplateDto.getTemplateName());
		dspTemplate.setBidCpcPrice(dspTemplateDto.getBidCpcPrice());
		dspTemplate.setBillingType(dspTemplateDto.getBillingType());

		// DBに保存
		dspTemplateDao.insert(dspTemplate);

		DspTemplateDto newDspTemplateDto = DspMapper.INSTANCE.tempEntityToDto(dspTemplate);
		return newDspTemplateDto;
	}

	@Override
	@Transactional
	public List<DspTemplateDto> templateList() {

		// DBからテンプレートをすべて取得して、リストとして返却
		List<DspTemplate> dspTemplateList = dspTemplateCustomDao.selectByShopId(ContextUtil.getCurrentShopId());
		List<DspTemplateDto> dspTemplateDtoLiist = new ArrayList<DspTemplateDto>();
		// dspTemplateDtoLiistに変更して返す
		for (DspTemplate dspTemplate : dspTemplateList) {
			DspTemplateDto dspTemplateDto = new DspTemplateDto();
			dspTemplateDto.setTemplateId(dspTemplate.getTemplateId());
			dspTemplateDto.setTemplateName(dspTemplate.getTemplateName());
			dspTemplateDto.setTemplatePriority(dspTemplate.getTemplatePriority());
			dspTemplateDto.setBidCpcPrice(dspTemplate.getBidCpcPrice());
			dspTemplateDto.setBillingType(dspTemplate.getBillingType());
			dspTemplateDtoLiist.add(dspTemplateDto);
		}

		return dspTemplateDtoLiist;
	}

	@Override
	@Transactional
	public DspTemplateDto templateDetail(Long templateId) {

		// テンプレートIdでDBからテンプレート情報を取得
		DspTemplate dspTemplate = dspTemplateDao.selectById(templateId);
		// EntityからDtoまで変更して、返却する
		DspTemplateDto dspTemplateDto = DspMapper.INSTANCE.tempEntityToDto(dspTemplate);
		return dspTemplateDto;
	}

	@Override
	@Transactional
	public void templateUpdate(DspTemplateDto dspTemplateDto) {

		// テンプレート名チェック
		DspTemplate existName = dspTemplateCustomDao.selectByName(dspTemplateDto.getTemplateName(), ContextUtil.getCurrentShopId());
		if (existName != null && !existName.getTemplateId().equals(dspTemplateDto.getTemplateId())) {
			throw new BusinessException(ErrorCodeConstant.E30001);
		}

		// テンプレート優先順チェック
		DspTemplate existPriority = dspTemplateCustomDao.selectByPriority(dspTemplateDto.getTemplatePriority(), ContextUtil.getCurrentShopId());
		if (existPriority != null && !existPriority.getTemplateId().equals(dspTemplateDto.getTemplateId())) {
			throw new BusinessException(ErrorCodeConstant.E30002);
		}

		// テンプレートIdでDBからテンプレート情報を取得
		DspTemplate dspTemplate = dspTemplateDao.selectById(dspTemplateDto.getTemplateId());
		dspTemplate.setTemplateName(dspTemplateDto.getTemplateName());
		dspTemplate.setBidCpcPrice(dspTemplateDto.getBidCpcPrice());
		dspTemplate.setBillingType(dspTemplateDto.getBillingType());
		dspTemplate.setBillingType(dspTemplateDto.getBillingType());
		// DB更新
		dspTemplateDao.update(dspTemplate);
	}

	@Override
	@Transactional
	public DspTemplateDto templateDelete(Long templateId) {
		// テンプレートIdでDBからテンプレート情報を取得
		DspTemplate dspTemplate = dspTemplateDao.selectById(templateId);
		dspTemplate.setIsActived(Flag.OFF.getValue());
		// DB更新
		dspTemplateDao.update(dspTemplate);
		// Dtoとして返却
		DspTemplateDto dspTemplateDto = DspMapper.INSTANCE.tempEntityToDto(dspTemplate);
		return dspTemplateDto;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void getDspCampaignReporting() {

		List<Shop> shopList = shopCustomDao.selectAllShop();
		for (Shop shop : shopList) {
			getReporting(shop);
		}

	}

	private void getReporting(Shop shop) {
		/**********************************************
		 * 
		 * ****キャンペーンIDでAｄ Reportを取得（広告入札単位）**** *
		 * 
		 **********************************************/
		// ShopIDでDBから、今まで有効的なキャンペーンをすべて取得する
		List<DspCampaignManage> dspCampaignManageList = dspCampaignCustomDao.selectByShopId(shop.getShopId());
		if (dspCampaignManageList == null || dspCampaignManageList.size() == 0) {
			return;
		}
		List<Integer> campaignIds = new ArrayList<Integer>();
		dspCampaignManageList.forEach(s -> campaignIds.add(s.getCampaignId()));

		// Token取得
		DspToken dspToken = dspApiService.getToken();

		// Req Aｄ Report URL組み立てる
		UriComponentsBuilder adReportBuilder = UriComponentsBuilder.newInstance();
		adReportBuilder = adReportBuilder.scheme(applicationProperties.getDspScheme());
		adReportBuilder = adReportBuilder.host(applicationProperties.getDspHost());
		adReportBuilder = adReportBuilder.path(applicationProperties.getAdReport());
		adReportBuilder = adReportBuilder.queryParam("token", dspToken.getToken());
		String adReportResource = adReportBuilder.build().toUri().toString();

		// 今日と昨日の時間を取得
		Date dNow = new Date();
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dNow);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		dBefore = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(dBefore);
		String endDate = sdf.format(dNow);

		// Req Aｄ Report Body作成
		DspAdReportReq dspAdReportReq = new DspAdReportReq();
		dspAdReportReq.setUser_id(shop.getDspUserId());
		dspAdReportReq.setCampaign_ids(campaignIds);
		dspAdReportReq.setStart_date("2019-08-07");
		dspAdReportReq.setEnd_date("2019-08-10");

		DspAdReportRes dspAdReportRes = null;
		try {
			dspAdReportRes = call(adReportResource, HttpMethod.POST, dspAdReportReq, null, DspAdReportRes.class);
		} catch (Exception e) {
			log.debug("DSP:レポートを取得エラー、リクエストボディー:{}", dspAdReportReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		// システムDBに保存しているレポーティング情報を検索条件で削除する
		List<DspReportManage> dspReportManageListForDelete = dspReportManageCustomDao.selectByCampaignIdsAndDate(campaignIds, startDate, endDate);
		dspReportManageDao.delete(dspReportManageListForDelete);
		// 取得した広告レポートを編集して、システムDBに保存する
		List<DspReportManage> dspReportManageList = new ArrayList<DspReportManage>();
		for (DspAdReportResDto dspAdReportResDto : dspAdReportRes.getResult()) {
			// 今回で取得したキャンペーン
			DspAdManage dspAdManage = dspAdManageCustomDao.selectByAdId(dspAdReportResDto.getAd_id());
			DspCampaignDetailDto dspCampaignDetailDto = dspCampaignService.getCampaignDetail(dspAdReportResDto.getCampaign_id(), shop.getDspUserId());
			CreativeManage creativeManage = dspCreativeCustomDao.selectByCreativeId(dspAdManage.getCreativeId());
			DspReportManage dspReportManage = new DspReportManage();
			dspReportManage.setCampaignId(dspAdManage.getCampaignId());
			dspReportManage.setCampaignName(dspCampaignDetailDto.getCampaignName());
			dspReportManage.setAdGroupId(dspAdManage.getAdGroupId());
			dspReportManage.setAdId(dspAdManage.getAdId());
			dspReportManage.setCreativeId(dspAdManage.getCreativeId());
			dspReportManage.setCreativeName(creativeManage.getCreativeName());
			dspReportManage.setDeviceType(dspAdManage.getDeviceType());
			dspReportManage.setDate(LocalDate.parse(dspAdReportResDto.getDate()));
			dspReportManage.setImpressionCount(dspAdReportResDto.getImpression_count());
			dspReportManage.setClickCount(dspAdReportResDto.getClick_count());
			dspReportManage.setPrice(dspAdReportResDto.getPrice());
			dspReportManageList.add(dspReportManage);
		}
		dspReportManageDao.insert(dspReportManageList);

	}

	@Override
	@Transactional
	public DspReportingGraphDto getDspReportingGraph(DspAdReportDto dspAdReportDto) {
		// 入力した検索条件でDBからレポーティング情報を取得
		List<DspReportManage> dspReportManageList = new ArrayList<DspReportManage>();
		if (ReportType.DEVICE.getValue().equals(dspAdReportDto.getReportType())) {
			// デバイス別
			dspReportManageList = dspReportManageCustomDao.selectByDeviceForGraph(dspAdReportDto);
		} else if (ReportType.DATE.getValue().equals(dspAdReportDto.getReportType())) {
			// 日付別
			dspReportManageList = dspReportManageCustomDao.selectByDateForGraph(dspAdReportDto);
		} else {
			// クリエイティブ別
			dspReportManageList = dspReportManageCustomDao.selectByCreativeForGraph(dspAdReportDto);
		}
		if (dspReportManageList.size() == 0) {
			return null;
		}

		List<String> reportTypeList = new ArrayList<String>();
		List<String> impressionCount = new ArrayList<String>();
		List<String> clickCount = new ArrayList<String>();
		List<String> price = new ArrayList<String>();
		List<String> cpc = new ArrayList<String>();
		List<String> cpm = new ArrayList<String>();
		List<String> ctr = new ArrayList<String>();

		// 取得したレポーティング情報を正確にリストに編集する
		for (DspReportManage dspReportManage : dspReportManageList) {
			if (ReportType.DEVICE.getValue().equals(dspAdReportDto.getReportType())) {
				// デバイス別
				reportTypeList.add(DspDeviceType.of(dspReportManage.getDeviceType()).getLabel());
			} else if (ReportType.DATE.getValue().equals(dspAdReportDto.getReportType())) {
				// 日付別
				reportTypeList.add(dspReportManage.getDate().toString());
			} else {
				// クリエイティブ別
				reportTypeList.add(dspReportManage.getCreativeName());
			}
			Long longPrice = CalculateUtil.getRoundedPrice(dspReportManage.getPrice());
			impressionCount.add(dspReportManage.getImpressionCount().toString());
			clickCount.add(dspReportManage.getClickCount().toString());
			price.add(longPrice.toString());
			cpc.add(ReportUtil.calCpc(dspReportManage.getClickCount().longValue(), longPrice).toString());
			cpm.add(ReportUtil.calCpm(dspReportManage.getImpressionCount().longValue(), longPrice).toString());
			ctr.add(ReportUtil.calCtr(dspReportManage.getClickCount().longValue(), dspReportManage.getImpressionCount().longValue() * 100).toString());

		}
		// リストの名を付ける
		reportTypeList.add(0, "x");
		impressionCount.add(0, "impressionCount");
		clickCount.add(0, "clickCount");
		price.add(0, "price");
		cpc.add(0, "cpc");
		cpm.add(0, "cpm");
		ctr.add(0, "ctr");

		// グラフ用
		DspReportingGraphDto dspReportingGraphDto = new DspReportingGraphDto();
		dspReportingGraphDto.setReportType(reportTypeList);
		dspReportingGraphDto.setImpressionCount(impressionCount);
		dspReportingGraphDto.setClickCount(clickCount);
		dspReportingGraphDto.setPrice(price);
		dspReportingGraphDto.setCpc(cpc);
		dspReportingGraphDto.setCpm(cpm);
		dspReportingGraphDto.setCtr(ctr);

		return dspReportingGraphDto;
	}

	@Override
	@Transactional
	public List<DspReportingListDto> getDspReportingList(DspAdReportDto dspAdReportDto) {

		List<DspReportManage> dspReportManageList = new ArrayList<DspReportManage>();
		if (ReportType.DEVICE.getValue().equals(dspAdReportDto.getReportType())) {
			// デバイス別
			dspReportManageList = dspReportManageCustomDao.selectByDevice(dspAdReportDto);
		} else if (ReportType.DATE.getValue().equals(dspAdReportDto.getReportType())) {
			// 日付別
			dspReportManageList = dspReportManageCustomDao.selectByDate(dspAdReportDto);
		} else {
			// クリエイティブ別
			dspReportManageList = dspReportManageCustomDao.selectByCreative(dspAdReportDto);
		}

		if (dspReportManageList.size() == 0) {
			return null;
		}
		List<DspReportingListDto> dspReportingDtoList = new ArrayList<DspReportingListDto>();
		for (DspReportManage dspReportManage : dspReportManageList) {
			DspReportingListDto dspReportingDto = new DspReportingListDto();
			dspReportingDto.setCampaignId(dspReportManage.getCampaignId().toString());
			dspReportingDto.setCampaignName(dspReportManage.getCampaignName());
			if (ReportType.DEVICE.getValue().equals(dspAdReportDto.getReportType())) {
				dspReportingDto.setDeviceType(dspReportManage.getDeviceType());
			} else if (ReportType.DATE.getValue().equals(dspAdReportDto.getReportType())) {
				dspReportingDto.setDate(dspReportManage.getDate().toString());
			} else {
				dspReportingDto.setCreativeId(dspReportManage.getCreativeId());
				dspReportingDto.setCreativeName(dspReportManage.getCreativeName());
			}
			Long longPrice = CalculateUtil.getRoundedPrice(dspReportManage.getPrice());
			dspReportingDto.setImpressionCount(dspReportManage.getImpressionCount());
			dspReportingDto.setClickCount(dspReportManage.getClickCount());
			dspReportingDto.setPrice(longPrice);
			dspReportingDto.setCtr(ReportUtil.calCtr(dspReportManage.getClickCount().longValue(), dspReportManage.getImpressionCount().longValue()));
			dspReportingDto.setCpc(ReportUtil.calCpc(dspReportManage.getClickCount().longValue(), longPrice));
			dspReportingDto.setCpm(ReportUtil.calCpm(dspReportManage.getImpressionCount().longValue(), longPrice));
			dspReportingDtoList.add(dspReportingDto);
		}

		return dspReportingDtoList;
	}

	@Override
	@Transactional
	public DspReportingListDto getDspReportingSummary(DspAdReportDto dspAdReportDto) {
		List<DspReportManage> dspReportManageList = new ArrayList<DspReportManage>();
		DspReportingListDto dspReportingListDto = new DspReportingListDto();
		if (ReportType.DEVICE.getValue().equals(dspAdReportDto.getReportType())) {
			// デバイス別
			dspReportManageList = dspReportManageCustomDao.selectByDevice(dspAdReportDto);
		} else if (ReportType.DATE.getValue().equals(dspAdReportDto.getReportType())) {
			// 日付別
			dspReportManageList = dspReportManageCustomDao.selectByDate(dspAdReportDto);
		} else {
			// クリエイティブ別
			dspReportManageList = dspReportManageCustomDao.selectByCreative(dspAdReportDto);
		}

		if (dspReportManageList.size() == 0) {
			return null;
		}

		// 取得したレポーティング情報を編集して、返却する
		Integer impressionCount = 0;
		Integer clickCount = 0;
		Double price = 0.0d;
		for (DspReportManage dspReportManage : dspReportManageList) {
			impressionCount += dspReportManage.getImpressionCount();
			clickCount += dspReportManage.getClickCount();
			price += dspReportManage.getPrice();
		}
		Long totalPrice = CalculateUtil.getRoundedPrice(price);
		dspReportingListDto.setCampaignId("合計");
		dspReportingListDto.setDeviceType(DspDeviceType.ALL.getValue());
		dspReportingListDto.setImpressionCount(impressionCount);
		dspReportingListDto.setClickCount(clickCount);
		dspReportingListDto.setPrice(totalPrice);
		dspReportingListDto.setCtr(ReportUtil.calCtr(clickCount.longValue(), impressionCount.longValue()));
		dspReportingListDto.setCpc(ReportUtil.calCpc(clickCount.longValue(), totalPrice));
		dspReportingListDto.setCpm(ReportUtil.calCpm(impressionCount.longValue(), totalPrice));

		return dspReportingListDto;
	}

	@Override
	@Transactional
	public void createDefaultTemplate(long shopId) {

		List<DspTemplate> dspTemplateList = new ArrayList<DspTemplate>();
		// 表示重視
		DspTemplate forShow = new DspTemplate();
		forShow.setShopId(shopId);
		forShow.setTemplateName("表示重視");
		forShow.setTemplatePriority(1);
		forShow.setBidCpcPrice(100);
		forShow.setBillingType(BillingType.CPC.getValue());
		dspTemplateList.add(forShow);
		// クリック重視
		DspTemplate forClick = new DspTemplate();
		forClick.setShopId(shopId);
		forClick.setTemplateName("クリック重視");
		forClick.setTemplatePriority(2);
		forClick.setBidCpcPrice(100);
		forClick.setBillingType(BillingType.CPC.getValue());
		dspTemplateList.add(forClick);
		// カスタム
		DspTemplate forCustom = new DspTemplate();
		forCustom.setShopId(shopId);
		forCustom.setTemplateName("カスタム");
		forCustom.setTemplatePriority(3);
		forCustom.setBidCpcPrice(100);
		forCustom.setBillingType(BillingType.CPC.getValue());
		dspTemplateList.add(forCustom);
		dspTemplateDao.insert(dspTemplateList);
	}

	@Override
	@Transactional
	public String download(DspAdReportDto dspAdReportDto) {

		List<DspReportingListDto> dspReportingDtoList = dspApiService.getDspReportingList(dspAdReportDto);
		DspReportingListDto summaryDto = dspApiService.getDspReportingSummary(dspAdReportDto);
		dspReportingDtoList.add(summaryDto);

		List<DspDeviceReportCsvBean> dspDeviceReportCsvBeanList = new ArrayList<DspDeviceReportCsvBean>();
		List<DspDateReportCsvBean> dspDateReportCsvBeanList = new ArrayList<DspDateReportCsvBean>();
		List<DspCreativeReportCsvBean> dspCreativeReportCsvBeanList = new ArrayList<DspCreativeReportCsvBean>();
		for (DspReportingListDto dspReportingListDto : dspReportingDtoList) {
			switch (dspAdReportDto.getReportType()) {
			case 1:// デバイスの場合
				DspDeviceReportCsvBean dspDeviceReportCsvBean = new DspDeviceReportCsvBean();
				dspDeviceReportCsvBean.setCampaignName(dspReportingListDto.getCampaignName());
				dspDeviceReportCsvBean.setCampaignId(dspReportingListDto.getCampaignId());
				dspDeviceReportCsvBean.setDeviceType(dspReportingListDto.getDeviceType());
				dspDeviceReportCsvBean.setImpressionCount(dspReportingListDto.getImpressionCount());
				dspDeviceReportCsvBean.setClickCount(dspReportingListDto.getClickCount());
				dspDeviceReportCsvBean.setPrice(dspReportingListDto.getPrice());
				dspDeviceReportCsvBean.setCtr(dspReportingListDto.getCtr());
				dspDeviceReportCsvBean.setCpc(dspReportingListDto.getCpc());
				dspDeviceReportCsvBean.setCpm(dspReportingListDto.getCpm());
				dspDeviceReportCsvBeanList.add(dspDeviceReportCsvBean);
				break;
			case 2:// 対象日の場合
				DspDateReportCsvBean dspDateReportCsvBean = new DspDateReportCsvBean();
				dspDateReportCsvBean.setCampaignName(dspReportingListDto.getCampaignName());
				dspDateReportCsvBean.setCampaignId(dspReportingListDto.getCampaignId());
				dspDateReportCsvBean.setDate(dspReportingListDto.getDate());
				dspDateReportCsvBean.setImpressionCount(dspReportingListDto.getImpressionCount());
				dspDateReportCsvBean.setClickCount(dspReportingListDto.getClickCount());
				dspDateReportCsvBean.setPrice(dspReportingListDto.getPrice());
				dspDateReportCsvBean.setCtr(dspReportingListDto.getCtr());
				dspDateReportCsvBean.setCpc(dspReportingListDto.getCpc());
				dspDateReportCsvBean.setCpm(dspReportingListDto.getCpm());
				dspDateReportCsvBeanList.add(dspDateReportCsvBean);
				break;
			case 4:// クリエイティブの場合
				DspCreativeReportCsvBean dspCreativeReportCsvBean = new DspCreativeReportCsvBean();
				dspCreativeReportCsvBean.setCampaignName(dspReportingListDto.getCampaignName());
				dspCreativeReportCsvBean.setCampaignId(dspReportingListDto.getCampaignId());
				dspCreativeReportCsvBean.setCreativeName(dspReportingListDto.getCreativeName());
				dspCreativeReportCsvBean.setImpressionCount(dspReportingListDto.getImpressionCount());
				dspCreativeReportCsvBean.setClickCount(dspReportingListDto.getClickCount());
				dspCreativeReportCsvBean.setPrice(dspReportingListDto.getPrice());
				dspCreativeReportCsvBean.setCtr(dspReportingListDto.getCtr());
				dspCreativeReportCsvBean.setCpc(dspReportingListDto.getCpc());
				dspCreativeReportCsvBean.setCpm(dspReportingListDto.getCpm());
				dspCreativeReportCsvBeanList.add(dspCreativeReportCsvBean);
				break;
			}

		}

		StringWriter out = new StringWriter();
		CsvWriterSettings settings = new CsvWriterSettings();
		CsvWriter writer = null;
		BeanWriterProcessor<DspDeviceReportCsvBean> deviceWriterProcessor = null;
		BeanWriterProcessor<DspDateReportCsvBean> dateWriterProcessor = null;
		BeanWriterProcessor<DspCreativeReportCsvBean> creativeWriterProcessor = null;

		switch (dspAdReportDto.getReportType()) {
		case 1:
			settings.setHeaders(DspDeviceReportCsvBean.columnName);
			deviceWriterProcessor = new BeanWriterProcessor<>(DspDeviceReportCsvBean.class);
			settings.setRowWriterProcessor(deviceWriterProcessor);

			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(dspDeviceReportCsvBeanList);
			break;
		case 2:
			settings.setHeaders(DspDateReportCsvBean.columnName);
			dateWriterProcessor = new BeanWriterProcessor<>(DspDateReportCsvBean.class);
			settings.setRowWriterProcessor(dateWriterProcessor);

			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(dspDateReportCsvBeanList);
			break;
		case 4:
			settings.setHeaders(DspCreativeReportCsvBean.columnName);
			creativeWriterProcessor = new BeanWriterProcessor<>(DspCreativeReportCsvBean.class);
			settings.setRowWriterProcessor(creativeWriterProcessor);

			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(dspCreativeReportCsvBeanList);
			break;
		}

		// close
		writer.close();

		return out.toString();
	}

	@Override
	public DspTemplateDto getDefaultTemplate() {

		List<DspTemplate> dspTemplateList = dspTemplateCustomDao.selectDefaultTemplate(ContextUtil.getCurrentShopId());
		DspTemplateDto dspTemplateDto = new DspTemplateDto();
		dspTemplateDto.setTemplateId(dspTemplateList.get(0).getTemplateId());
		dspTemplateDto.setTemplateName(dspTemplateList.get(0).getTemplateName());
		dspTemplateDto.setBidCpcPrice(dspTemplateList.get(0).getBidCpcPrice());
		dspTemplateDto.setBillingType(dspTemplateList.get(0).getBillingType());
		dspTemplateDto.setTemplatePriority(dspTemplateList.get(0).getTemplatePriority());
		return dspTemplateDto;
	}

}
