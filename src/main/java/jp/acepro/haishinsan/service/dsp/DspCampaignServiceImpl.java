package jp.acepro.haishinsan.service.dsp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.DspAdManageDao;
import jp.acepro.haishinsan.dao.DspCampaignCustomDao;
import jp.acepro.haishinsan.dao.DspCampaignManageDao;
import jp.acepro.haishinsan.dao.DspCreativeCustomDao;
import jp.acepro.haishinsan.dao.DspSegmentCustomDao;
import jp.acepro.haishinsan.dao.DspTemplateDao;
import jp.acepro.haishinsan.dao.DspTokenCustomDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.db.entity.CreativeManage;
import jp.acepro.haishinsan.db.entity.DspAdManage;
import jp.acepro.haishinsan.db.entity.DspCampaignManage;
import jp.acepro.haishinsan.db.entity.DspTemplate;
import jp.acepro.haishinsan.db.entity.DspToken;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.SegmentManage;
import jp.acepro.haishinsan.dto.EmailCampDetailDto;
import jp.acepro.haishinsan.dto.EmailDto;
import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.dsp.DspAdBitCreateReq;
import jp.acepro.haishinsan.dto.dsp.DspAdBitCreateRes;
import jp.acepro.haishinsan.dto.dsp.DspAdCreateReq;
import jp.acepro.haishinsan.dto.dsp.DspAdCreateRes;
import jp.acepro.haishinsan.dto.dsp.DspAdDto;
import jp.acepro.haishinsan.dto.dsp.DspAdGroupCreateReq;
import jp.acepro.haishinsan.dto.dsp.DspAdGroupCreateRes;
import jp.acepro.haishinsan.dto.dsp.DspAdGroupListReq;
import jp.acepro.haishinsan.dto.dsp.DspAdGroupListRes;
import jp.acepro.haishinsan.dto.dsp.DspCampaignCreateReq;
import jp.acepro.haishinsan.dto.dsp.DspCampaignCreateRes;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDetailDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignListDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignListReq;
import jp.acepro.haishinsan.dto.dsp.DspCampaignListRes;
import jp.acepro.haishinsan.dto.dsp.DspCampaignUpdateReq;
import jp.acepro.haishinsan.dto.dsp.DspCampaignUpdateRes;
import jp.acepro.haishinsan.dto.dsp.DspCreativeDto;
import jp.acepro.haishinsan.dto.dsp.Segment;
import jp.acepro.haishinsan.enums.ApprovalFlag;
import jp.acepro.haishinsan.enums.DspDeviceType;
import jp.acepro.haishinsan.enums.DspScheduleFlag;
import jp.acepro.haishinsan.enums.EmailTemplateType;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.enums.MediaCollection;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.service.BaseService;
import jp.acepro.haishinsan.service.EmailService;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DspCampaignServiceImpl extends BaseService implements DspCampaignService {

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	DspTokenCustomDao dspTokenCustomDao;

	@Autowired
	DspCreativeCustomDao dspCreativeCustomDao;

	@Autowired
	DspApiService dspApiService;

	@Autowired
	DspSegmentCustomDao dspSegmentCustomDao;

	@Autowired
	DspTemplateDao dspTemplateDao;

	@Autowired
	IssueDao issueDao;

	@Autowired
	DspCampaignCustomDao dspCampaignCustomDao;

	@Autowired
	DspCampaignManageDao dspCampaignManageDao;

	@Autowired
	DspAdManageDao dspAdManageDao;

	@Autowired
	EmailService emailService;

	// 案件簡単作成のため、新しいトランザクションで実行する
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createDspCampaign(DspCampaignDto dspCampaignDto, IssueDto issueDto) {
		createCampaign(dspCampaignDto, issueDto);
	}

	@Override
	@Transactional
	public DspCampaignDto createCampaign(DspCampaignDto dspCampaignDto, IssueDto issueDto) {

		// Token取得
		DspToken dspToken = dspApiService.getToken();
		/**********************************************
		 * 
		 * ***************キャンペーン作成**************** *
		 * 
		 **********************************************/
		// キャンペーン配信ステータス設定
		Integer campaignStatus = Flag.ON.getValue();
		// 審査状態を設定
		ApprovalFlag approvalFlag = ApprovalFlag.COMPLETED;
		if (Flag.ON.getValue().toString().equals(ContextUtil.getCurrentShop().getSalesCheckFlag())) {
			// 営業チェックが必要な場合、停止状態で作られる
			campaignStatus = Flag.OFF.getValue();
			// 営業チェックが必要な場合、審査状態を承認待ちにする
			approvalFlag = ApprovalFlag.WAITING;
		}

		// Req Campaign URL組み立てる
		UriComponentsBuilder campaignBuilder = UriComponentsBuilder.newInstance();
		campaignBuilder = campaignBuilder.scheme(applicationProperties.getDspScheme());
		campaignBuilder = campaignBuilder.host(applicationProperties.getDspHost());
		campaignBuilder = campaignBuilder.path(applicationProperties.getCreateDspCampaign());
		campaignBuilder = campaignBuilder.queryParam("token", dspToken.getToken());
		String campaignResource = campaignBuilder.build().toUri().toString();

		// 予算リセットの時刻
		String resetTime = "00:00";

		// 開始時刻と終了時刻のフォーマット
		String startDateTime = dspCampaignDto.getStartDatetime() + " 00:00";
		String endDateTime = dspCampaignDto.getEndDatetime() + " 23:30";

		// templateIdでDBからDspテンプレート情報取得
		DspTemplate dspTemplate = dspTemplateDao.selectById(dspCampaignDto.getTemplateId());

		// Req Campaign Body作成
		DspCampaignCreateReq dspCampaignCreateReq = new DspCampaignCreateReq();
		dspCampaignCreateReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		dspCampaignCreateReq.setName(dspCampaignDto.getCampaignName());
		dspCampaignCreateReq.setDaily_budget(dspCampaignDto.getDailyBudget());
		dspCampaignCreateReq.setMonthly_budget(dspCampaignDto.getMonthBudget());
		dspCampaignCreateReq.setIs_scheduled(DspScheduleFlag.SET.getValue());
		dspCampaignCreateReq.setBilling_type(dspTemplate.getBillingType());
		dspCampaignCreateReq.setStart_datetime(startDateTime);
		dspCampaignCreateReq.setEnd_datetime(endDateTime);
		dspCampaignCreateReq.setStatus(campaignStatus);
		dspCampaignCreateReq.setReset_time(resetTime);
		dspCampaignCreateReq.getPerformance_indicator().setType(applicationProperties.getTargetType());
		dspCampaignCreateReq.getPerformance_indicator().setValue(applicationProperties.getTargetValue());

		// キャンペーン作成
		DspCampaignCreateRes dspCampaignCreateRes = null;
		try {
			dspCampaignCreateRes = call(campaignResource, HttpMethod.POST, dspCampaignCreateReq, null, DspCampaignCreateRes.class);
		} catch (Exception e) {
			log.debug("DSP:キャンペーン作成エラー、リクエストボディー:{}", dspCampaignCreateReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		/**********************************************
		 * 
		 * ***************広告グループ作成*************** *
		 * 
		 **********************************************/
		// デバイスによって、広告グループを作成する
		DspAdGroupCreateRes dspAdGroupCreateMbRes = null;
		DspAdGroupCreateRes dspAdGroupCreatePcRes = null;
		Integer pc = 1;
		Integer mobile = 2;
		String pcName = "PC" + ContextUtil.getCurrentShop().getShopName();
		String mobileName = "SP" + ContextUtil.getCurrentShop().getShopName();
		if (DspDeviceType.MOBILE.getValue().equals(dspCampaignDto.getDeviceType())) {
			dspAdGroupCreateMbRes = createAdGroup(dspToken, mobile, mobileName, dspCampaignDto, dspCampaignCreateRes);
		} else if (DspDeviceType.PC.getValue().equals(dspCampaignDto.getDeviceType())) {

			dspAdGroupCreatePcRes = createAdGroup(dspToken, pc, pcName, dspCampaignDto, dspCampaignCreateRes);
		} else {
			dspAdGroupCreateMbRes = createAdGroup(dspToken, mobile, mobileName, dspCampaignDto, dspCampaignCreateRes);
			dspAdGroupCreatePcRes = createAdGroup(dspToken, pc, pcName, dspCampaignDto, dspCampaignCreateRes);
		}

		/**********************************************
		 * 
		 * ***************広告グループ入札*************** *
		 * 
		 **********************************************/
		// ShopIdでDBからセグメント情報取得
		List<SegmentManage> segmentManageList = dspSegmentCustomDao.selectByShopId(ContextUtil.getCurrentShop().getShopId());
		// PC広告グループのみ、入札する
		List<DspAdBitCreateRes> dspAdBitCreateResList = new ArrayList<DspAdBitCreateRes>();
		if (dspAdGroupCreatePcRes != null) {
			for (SegmentManage segmentManage : segmentManageList) {
				DspAdBitCreateRes dspAdBitCreatePcRes = createAdBit(dspToken, segmentManage, dspAdGroupCreatePcRes, dspTemplate);
				dspAdBitCreateResList.add(dspAdBitCreatePcRes);
			}
		}
		// Mobile広告グループのみ、入札する
		if (dspAdGroupCreateMbRes != null) {
			for (SegmentManage segmentManage : segmentManageList) {
				DspAdBitCreateRes dspAdBitCreatePcRes = createAdBit(dspToken, segmentManage, dspAdGroupCreateMbRes, dspTemplate);
				dspAdBitCreateResList.add(dspAdBitCreatePcRes);
			}
		}
		/**********************************************
		 * 
		 * ******************広告作成***************** *
		 * 
		 **********************************************/
		// PC広告グループのみ、広告作成
		if (dspAdGroupCreatePcRes != null) {
			for (Integer creativeId : dspCampaignDto.getIdList()) {
				DspAdCreateRes dspAdCreateRes = createAd(dspToken, dspAdGroupCreatePcRes, creativeId, dspCampaignDto);
				// 作成した広告をシステムDB保存する PC向
				DspAdDto dspAdDto = new DspAdDto();
				dspAdDto.setCampaignId(dspCampaignCreateRes.getId());
				dspAdDto.setAdGroupId(dspAdGroupCreatePcRes.getId());
				dspAdDto.setAdId(dspAdCreateRes.getId());
				dspAdDto.setCreativeId(creativeId);
				dspAdDto.setDeviceType(DspDeviceType.PC.getValue());
				insertAdToDb(dspAdDto);
			}
		}
		// Mobile広告グループのみ、広告作成
		if (dspAdGroupCreateMbRes != null) {
			for (Integer creativeId : dspCampaignDto.getIdList()) {
				DspAdCreateRes dspAdCreateRes = createAd(dspToken, dspAdGroupCreateMbRes, creativeId, dspCampaignDto);
				// 作成した広告をシステムDB保存する Mobile向
				DspAdDto dspAdDto = new DspAdDto();
				dspAdDto.setCampaignId(dspCampaignCreateRes.getId());
				dspAdDto.setAdGroupId(dspAdGroupCreateMbRes.getId());
				dspAdDto.setAdId(dspAdCreateRes.getId());
				dspAdDto.setCreativeId(creativeId);
				dspAdDto.setDeviceType(DspDeviceType.MOBILE.getValue());
				insertAdToDb(dspAdDto);
			}
		}

		/**********************************************
		 * 
		 * *************DBにキャンペーン情報登録*********** *
		 * 
		 **********************************************/
		// 選択したURLに対してセグメントを取得
		SegmentManage newSegmentManage = new SegmentManage();
		for (SegmentManage segmentManage : segmentManageList) {
			if (segmentManage.getUrl().equals(dspCampaignDto.getUrl())) {
				newSegmentManage = segmentManage;
			}
		}

		DspCampaignManage dspCampaignManage = new DspCampaignManage();
		dspCampaignManage.setCampaignId(dspCampaignCreateRes.getId());
		dspCampaignManage.setCreativeId(dspCampaignDto.getIdList().toString().replace("[", "").replace("]", ""));
		dspCampaignManage.setSegmentId(newSegmentManage.getSegmentId());
		dspCampaignManage.setBudget(dspCampaignDto.getBudget());
		dspCampaignManage.setApprovalFlag(approvalFlag.getValue());
		dspCampaignManageDao.insert(dspCampaignManage);

		// DBに案件情報登録
		Issue issue = new Issue();
		if (issueDto == null) {
			issue = new Issue();
			issue.setShopId(ContextUtil.getCurrentShop().getShopId());
			issue.setDspCampaignManageId(dspCampaignManage.getDspCampaignManageId());
			issue.setCampaignName(dspCampaignDto.getCampaignName());
			issue.setBudget(dspCampaignDto.getBudget().longValue());
			issue.setStartDate(dspCampaignDto.getStartDatetime());
			issue.setEndDate(dspCampaignDto.getEndDatetime());
			issueDao.insert(issue);
		} else {
			issueDto.setDspCampaignManageId(dspCampaignManage.getDspCampaignManageId());
		}

		// メール送信
		EmailDto emailDto = new EmailDto();
		emailDto.setIssueId(issue.getIssueId());
		EmailCampDetailDto emailCampDetailDto = new EmailCampDetailDto();
		emailCampDetailDto.setMediaType(MediaCollection.DSP.getValue());
		emailCampDetailDto.setCampaignId(String.valueOf(dspCampaignCreateRes.getId()));
		emailCampDetailDto.setCampaignName(dspCampaignDto.getCampaignName());
		List<EmailCampDetailDto> emailCampDetailDtoList = new ArrayList<EmailCampDetailDto>();
		emailCampDetailDtoList.add(emailCampDetailDto);
		emailDto.setCampaignList(emailCampDetailDtoList);
		emailDto.setTemplateType(EmailTemplateType.CAMPAIGN.getValue());
		emailService.sendEmail(emailDto);

		return dspCampaignDto;
	}

	@Override
	@Transactional
	public List<DspCampaignDto> getCampaignList() {

		// ShopIDで有効的なキャンペーンをDBから取得
		List<DspCampaignManage> dspCampaignManageList = dspCampaignCustomDao.selectByShopId(ContextUtil.getCurrentShop().getShopId());

		// キャンペーンIDをリストにして
		List<Integer> ids = new ArrayList<Integer>();
		for (DspCampaignManage dspCampaignManage : dspCampaignManageList) {
			ids.add(Integer.valueOf(dspCampaignManage.getCampaignId()));
		}

		// Token取得
		DspToken dspToken = dspApiService.getToken();
		/**********************************************
		 * 
		 * *************キャンペーンリスト取得************** *
		 * 
		 **********************************************/
		// Req CampaignList URL組み立てる
		UriComponentsBuilder campaignListBuilder = UriComponentsBuilder.newInstance();
		campaignListBuilder = campaignListBuilder.scheme(applicationProperties.getDspScheme());
		campaignListBuilder = campaignListBuilder.host(applicationProperties.getDspHost());
		campaignListBuilder = campaignListBuilder.path(applicationProperties.getCampaignList());
		campaignListBuilder = campaignListBuilder.queryParam("token", dspToken.getToken());
		String campaignListResource = campaignListBuilder.build().toUri().toString();

		// Req CampaignList Body作成
		DspCampaignListReq dspCampaignListReq = new DspCampaignListReq();
		dspCampaignListReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		dspCampaignListReq.setIds(ids);

		DspCampaignListRes dspCampaignListRes = null;
		try {
			dspCampaignListRes = call(campaignListResource, HttpMethod.POST, dspCampaignListReq, null, DspCampaignListRes.class);
		} catch (Exception e) {
			log.debug("DSP:キャンペーンリスト取得エラー、リクエストボディー:{}", dspCampaignListReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		List<DspCampaignDto> dspCampaignDtoList = new ArrayList<DspCampaignDto>();
		for (DspCampaignListDto dspCampaignListDto : dspCampaignListRes.getResult()) {
			for (DspCampaignManage dspCampaignManage : dspCampaignManageList) {
				if (dspCampaignListDto.getId().equals(dspCampaignManage.getCampaignId())) {
					DspCampaignDto dspCampaignDto = new DspCampaignDto();
					dspCampaignDto.setCampaignId(dspCampaignListDto.getId());
					dspCampaignDto.setCampaignName(dspCampaignListDto.getName());
					dspCampaignDto.setStartDatetime(dspCampaignListDto.getStart_datetime());
					dspCampaignDto.setEndDatetime(dspCampaignListDto.getEnd_datetime());
					dspCampaignDto.setApprovalFlag(dspCampaignManage.getApprovalFlag());
					dspCampaignDto.setStatus(dspCampaignListDto.getStatus());
					dspCampaignDtoList.add(dspCampaignDto);
				}
			}
		}

		return dspCampaignDtoList;
	}

	@Override
	@Transactional
	public DspCampaignDetailDto getCampaignDetail(Integer campaignId, Integer dspUserId) {

		// Token取得
		DspToken dspToken = dspApiService.getToken();
		/**********************************************
		 * 
		 * ***************キャンペーン取得**************** *
		 * 
		 **********************************************/
		// Req Campaign URL組み立てる
		UriComponentsBuilder campaignBuilder = UriComponentsBuilder.newInstance();
		campaignBuilder = campaignBuilder.scheme(applicationProperties.getDspScheme());
		campaignBuilder = campaignBuilder.host(applicationProperties.getDspHost());
		campaignBuilder = campaignBuilder.path(applicationProperties.getCampaignList());
		campaignBuilder = campaignBuilder.queryParam("token", dspToken.getToken());
		String campaignResource = campaignBuilder.build().toUri().toString();

		// Req Campaign Body作成
		DspCampaignListReq dspCampaignListReq = new DspCampaignListReq();
		dspCampaignListReq.setUser_id(dspUserId);
		dspCampaignListReq.setId(campaignId);

		DspCampaignListRes dspCampaignListRes = null;
		try {
			dspCampaignListRes = call(campaignResource, HttpMethod.POST, dspCampaignListReq, null, DspCampaignListRes.class);
		} catch (Exception e) {
			log.debug("DSP:キャンペーン詳細エラー、リクエストボディー:{}", dspCampaignListReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		// システムDBからキャンペーンに関する情報を取得
		DspCampaignManage dspCampaignManage = dspCampaignCustomDao.selectByCampaignId(campaignId);
		// セグメント情報を取得
		SegmentManage segmentManage = dspSegmentCustomDao.selectBySegmentId(dspCampaignManage.getSegmentId());
		// クリエイティブ情報を取得
		List<Integer> creativeIds = new ArrayList<Integer>();
		StringTokenizer st = new StringTokenizer(dspCampaignManage.getCreativeId(), ", ");
		while (st.hasMoreTokens()) {
			creativeIds.add(Integer.valueOf(st.nextToken()));
		}
		// キャンペーンIDで、APIから広告グループを取得
		// Req AdGroup URL組み立てる
		UriComponentsBuilder adGroupBuilder = UriComponentsBuilder.newInstance();
		adGroupBuilder = adGroupBuilder.scheme(applicationProperties.getDspScheme());
		adGroupBuilder = adGroupBuilder.host(applicationProperties.getDspHost());
		adGroupBuilder = adGroupBuilder.path(applicationProperties.getAdGroupList());
		adGroupBuilder = adGroupBuilder.queryParam("token", dspToken.getToken());
		String adGroupResource = adGroupBuilder.build().toUri().toString();

		// Req AdGroup Body作成
		DspAdGroupListReq dspAdGroupListReq = new DspAdGroupListReq();
		dspAdGroupListReq.setUser_id(dspUserId);
		dspAdGroupListReq.setCampaign_id(campaignId);

		DspAdGroupListRes dspAdGroupListRes = null;
		try {
			dspAdGroupListRes = call(adGroupResource, HttpMethod.POST, dspAdGroupListReq, null, DspAdGroupListRes.class);
		} catch (Exception e) {
			log.debug("DSP:広告グループ取得エラー、リクエストボディー:{}", dspAdGroupListReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		// デバイスタイプを判断する
		Integer deviceType;
		if (dspAdGroupListRes.getResult().size() == 1) {
			deviceType = dspAdGroupListRes.getResult().get(0).getDevice();
		} else {
			deviceType = DspDeviceType.ALL.getValue();
		}

		// クリエイティブ情報をAPIから取得して、リストに集める
		List<CreativeManage> creativeManageList = dspCreativeCustomDao.selectByCreativeIds(creativeIds);
		List<DspCreativeDto> dspCreativeDtoList = new ArrayList<DspCreativeDto>();
		for (CreativeManage creativeManage : creativeManageList) {
			DspCreativeDto dspCreativeDto = new DspCreativeDto();
			dspCreativeDto.setCreativeId(creativeManage.getCreativeId());
			dspCreativeDto.setCreativeName(creativeManage.getCreativeName());
			dspCreativeDto.setUrl(creativeManage.getUrl());
			dspCreativeDtoList.add(dspCreativeDto);
		}

		// クリエイティブ情報とセグメント情報を組み合わせて、画面に戻る
		DspCampaignDetailDto dspCampaignDetailDto = new DspCampaignDetailDto();
		dspCampaignDetailDto.setCampaignId(dspCampaignListRes.getResult().get(0).getId());
		dspCampaignDetailDto.setCampaignName(dspCampaignListRes.getResult().get(0).getName());
		dspCampaignDetailDto.setBudget(dspCampaignManage.getBudget());
		dspCampaignDetailDto.setStartDatetime(dspCampaignListRes.getResult().get(0).getStart_datetime());
		dspCampaignDetailDto.setEndDatetime(dspCampaignListRes.getResult().get(0).getEnd_datetime());
		dspCampaignDetailDto.setStatus(dspCampaignListRes.getResult().get(0).getStatus());
		dspCampaignDetailDto.setDevice(deviceType);
		dspCampaignDetailDto.getDspSegmentDto().setSegmentName(segmentManage.getSegmentName());
		dspCampaignDetailDto.getDspSegmentDto().setUrl(segmentManage.getUrl());
		dspCampaignDetailDto.setDspCreativeDtoList(dspCreativeDtoList);

		return dspCampaignDetailDto;
	}

	@Override
	@Transactional
	public DspCampaignDetailDto deleteCampaign(Integer campaignId) {
		// Token取得
		DspToken dspToken = dspApiService.getToken();

		/**********************************************
		 * 
		 * ************キャンペーンステータス更新************ *
		 * 
		 **********************************************/
		// Req CampaignUpdate URL組み立てる
		UriComponentsBuilder campaignUpdateBuilder = UriComponentsBuilder.newInstance();
		campaignUpdateBuilder = campaignUpdateBuilder.scheme(applicationProperties.getDspScheme());
		campaignUpdateBuilder = campaignUpdateBuilder.host(applicationProperties.getDspHost());
		campaignUpdateBuilder = campaignUpdateBuilder.path(applicationProperties.getCreateDspCampaign());
		campaignUpdateBuilder = campaignUpdateBuilder.queryParam("token", dspToken.getToken());
		String campaignUpdateResource = campaignUpdateBuilder.build().toUri().toString();

		// Req CampaignUpdate Body作成
		DspCampaignUpdateReq dspCampaignUpdateReq = new DspCampaignUpdateReq();
		dspCampaignUpdateReq.setId(campaignId);
		dspCampaignUpdateReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		dspCampaignUpdateReq.setStatus(Flag.OFF.getValue());

		try {
			call(campaignUpdateResource, HttpMethod.POST, dspCampaignUpdateReq, null, DspCampaignUpdateRes.class);
		} catch (Exception e) {
			log.debug("DSP:キャンペーン更新エラー、リクエストボディー:{}", dspCampaignUpdateReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		// 表示するのために、先に検索する
		DspCampaignDetailDto dspCampaignDetailDto = getCampaignDetail(campaignId, ContextUtil.getCurrentShop().getDspUserId());

		// システムDBを審査フラグを更新する
		DspCampaignManage dspCampaignManage = dspCampaignCustomDao.selectByCampaignId(campaignId);
		dspCampaignManage.setIsActived(Flag.OFF.getValue());
		dspCampaignManageDao.update(dspCampaignManage);

		return dspCampaignDetailDto;
	}

	@Override
	@Transactional
	public void updateCampaign(Integer campaignId, String status) {

		// Token取得
		DspToken dspToken = dspApiService.getToken();

		/**********************************************
		 * 
		 * ************キャンペーンステータス更新************ *
		 * 
		 **********************************************/
		// Req CampaignUpdate URL組み立てる
		UriComponentsBuilder campaignUpdateBuilder = UriComponentsBuilder.newInstance();
		campaignUpdateBuilder = campaignUpdateBuilder.scheme(applicationProperties.getDspScheme());
		campaignUpdateBuilder = campaignUpdateBuilder.host(applicationProperties.getDspHost());
		campaignUpdateBuilder = campaignUpdateBuilder.path(applicationProperties.getCreateDspCampaign());
		campaignUpdateBuilder = campaignUpdateBuilder.queryParam("token", dspToken.getToken());
		String campaignUpdateResource = campaignUpdateBuilder.build().toUri().toString();

		// Req CampaignUpdate Body作成
		DspCampaignUpdateReq dspCampaignUpdateReq = new DspCampaignUpdateReq();
		dspCampaignUpdateReq.setId(campaignId);
		dspCampaignUpdateReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		if (Flag.ON.getLabel().equals(status)) {
			dspCampaignUpdateReq.setStatus(Flag.ON.getValue());
		} else {
			dspCampaignUpdateReq.setStatus(Flag.OFF.getValue());
		}

		try {
			call(campaignUpdateResource, HttpMethod.POST, dspCampaignUpdateReq, null, DspCampaignUpdateRes.class);
		} catch (Exception e) {
			log.error("---------------DSP:キャンペーン更新の場合、エラーが発生しました。---------------");
			log.debug("DSP:キャンペーン更新エラー、リクエストボディー:{}", dspCampaignUpdateReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		// システムDBを審査フラグを更新する
		if (Flag.ON.getLabel().equals(status)) {
			DspCampaignManage dspCampaignManage = dspCampaignCustomDao.selectByCampaignId(campaignId);
			dspCampaignManage.setApprovalFlag(ApprovalFlag.COMPLETED.getValue());
			dspCampaignManageDao.update(dspCampaignManage);
		}
	}

	/**********************************************
	 * 広告グループ作成
	 * 
	 * @param dspTemplate
	 **********************************************/
	private DspAdGroupCreateRes createAdGroup(DspToken dspToken, Integer deviceType, String adGroupName, DspCampaignDto dspCampaignDto, DspCampaignCreateRes dspCampaignCreateRes) {

		// Req AdGroup URL組み立てる
		UriComponentsBuilder adGroupBuilder = UriComponentsBuilder.newInstance();
		adGroupBuilder = adGroupBuilder.scheme(applicationProperties.getDspScheme());
		adGroupBuilder = adGroupBuilder.host(applicationProperties.getDspHost());
		adGroupBuilder = adGroupBuilder.path(applicationProperties.getCreateDspGroup());
		adGroupBuilder = adGroupBuilder.queryParam("token", dspToken.getToken());
		String adGroupResource = adGroupBuilder.build().toUri().toString();

		// Req AdGroup Body作成
		DspAdGroupCreateReq dspAdGroupCreateReq = new DspAdGroupCreateReq();
		dspAdGroupCreateReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		dspAdGroupCreateReq.setCampaign_id(dspCampaignCreateRes.getId());
		dspAdGroupCreateReq.setName(adGroupName);
		dspAdGroupCreateReq.setDevice(deviceType);
		dspAdGroupCreateReq.setDaily_freq_cap(applicationProperties.getFrequencyCap());
		dspAdGroupCreateReq.setRotation_method(3);
		dspAdGroupCreateReq.setStatus(Flag.ON.getValue());

		DspAdGroupCreateRes dspAdGroupCreateRes = null;
		try {
			dspAdGroupCreateRes = call(adGroupResource, HttpMethod.POST, dspAdGroupCreateReq, null, DspAdGroupCreateRes.class);
		} catch (Exception e) {
			log.debug("DSP:広告グループ作成エラー、リクエストボディー:{}", dspAdGroupCreateReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
		return dspAdGroupCreateRes;
	}

	/**********************************************
	 * 広告グループ入札作成
	 * 
	 * @param segmentManage
	 * @param dspAdGroupCreatePcRes
	 * @param dspTemplate
	 **********************************************/
	private DspAdBitCreateRes createAdBit(DspToken dspToken, SegmentManage segmentManage, DspAdGroupCreateRes dspAdGroupCreateRes, DspTemplate dspTemplate) {
		// Req AdBit URL組み立てる
		UriComponentsBuilder adBitBuilder = UriComponentsBuilder.newInstance();
		adBitBuilder = adBitBuilder.scheme(applicationProperties.getDspScheme());
		adBitBuilder = adBitBuilder.host(applicationProperties.getDspHost());
		adBitBuilder = adBitBuilder.path(applicationProperties.getCreateDspBit());
		adBitBuilder = adBitBuilder.queryParam("token", dspToken.getToken());
		String adBitResource = adBitBuilder.build().toUri().toString();

		// Bodyのみセグメント作成
		List<Segment> segments = new ArrayList<Segment>();
		Segment segment = new Segment();
		segment.setSegment_id(segmentManage.getSegmentId());
		segment.setRetention_window(31536000);
		segment.setWait_interval(0);
		segments.add(segment);

		// Req AdBit Body作成
		DspAdBitCreateReq dspAdBitCreateReq = new DspAdBitCreateReq();
		dspAdBitCreateReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		dspAdBitCreateReq.setAdgroup_id(dspAdGroupCreateRes.getId());
		dspAdBitCreateReq.setType(11);
		dspAdBitCreateReq.setPrice(dspTemplate.getBidCpcPrice());
		dspAdBitCreateReq.setStatus(1);
		dspAdBitCreateReq.setSegments(segments);

		DspAdBitCreateRes dspAdBitCreateRes = null;
		try {
			dspAdBitCreateRes = call(adBitResource, HttpMethod.POST, dspAdBitCreateReq, null, DspAdBitCreateRes.class);
		} catch (Exception e) {
			log.debug("DSP:広告グループ入札エラー、リクエストボディー:{}", dspAdBitCreateReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
		return dspAdBitCreateRes;
	}

	/**********************************************
	 * 広告作成
	 * 
	 * @param dspCampaignDto
	 **********************************************/
	private DspAdCreateRes createAd(DspToken dspToken, DspAdGroupCreateRes dspAdGroupCreatePRes, Integer creativeId, DspCampaignDto dspCampaignDto) {

		// Req Ad URL組み立てる
		UriComponentsBuilder adBuilder = UriComponentsBuilder.newInstance();
		adBuilder = adBuilder.scheme(applicationProperties.getDspScheme());
		adBuilder = adBuilder.host(applicationProperties.getDspHost());
		adBuilder = adBuilder.path(applicationProperties.getCreateDspAd());
		adBuilder = adBuilder.queryParam("token", dspToken.getToken());
		String adResource = adBuilder.build().toUri().toString();

		// 配信開始時刻と配信終了時刻
		String start_time = "00:00:00";
		String end_time = "23:59:59";

		// Req Ad Body作成
		DspAdCreateReq dspAdCreateReq = new DspAdCreateReq();
		dspAdCreateReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		dspAdCreateReq.setAdgroup_id(dspAdGroupCreatePRes.getId());
		dspAdCreateReq.setCreative_id(creativeId);
		dspAdCreateReq.setStatus(Flag.ON.getValue());
		dspAdCreateReq.setUrl(dspCampaignDto.getUrl());
		dspAdCreateReq.setReview_landing_url(dspCampaignDto.getUrl());
		dspAdCreateReq.setStart_time(start_time);
		dspAdCreateReq.setEnd_time(end_time);

		DspAdCreateRes dspAdCreateRes = new DspAdCreateRes();
		try {
			dspAdCreateRes = call(adResource, HttpMethod.POST, dspAdCreateReq, null, DspAdCreateRes.class);
		} catch (Exception e) {
			log.debug("DSP:広告作成エラー、リクエストボディー:{}", dspAdCreateReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
		return dspAdCreateRes;
	}

	/**********************************************
	 * Ad情報をDBに保存
	 **********************************************/
	@Transactional
	private void insertAdToDb(DspAdDto dspAdDto) {

		DspAdManage dspAdManage = new DspAdManage();
		dspAdManage.setCampaignId(dspAdDto.getCampaignId());
		dspAdManage.setAdGroupId(dspAdDto.getAdGroupId());
		dspAdManage.setAdId(dspAdDto.getAdId());
		dspAdManage.setCreativeId(dspAdDto.getCreativeId());
		dspAdManage.setDeviceType(dspAdDto.getDeviceType());
		dspAdManageDao.insert(dspAdManage);
	}

	@Override
	@Transactional
	public DspCampaignDto validate(DspCampaignDto dspCampaignDto) {
		// クリエイティブ必須チェック
		if (dspCampaignDto.getDspCreativeDtoList() == null || dspCampaignDto.getDspCreativeDtoList().size() == 0) {
			throw new BusinessException(ErrorCodeConstant.E30005);
		}

		// 入力配信期間チェック
		LocalDate startDate = LocalDate.parse(dspCampaignDto.getStartDatetime());
		LocalDate endDate = LocalDate.parse(dspCampaignDto.getEndDatetime());
		if (endDate.isBefore(startDate)) {
			throw new BusinessException(ErrorCodeConstant.E30003);
		}

		// 配信期間日数
		BigDecimal periodBigDecimal = BigDecimal.valueOf(DateUtil.distance_hyphen(dspCampaignDto.getStartDatetime(), dspCampaignDto.getEndDatetime()));

		// 最低予算金額 金額を切り上げにする
		BigDecimal monthBudgetFlag = BigDecimal.valueOf(30000).divide(BigDecimal.valueOf(100 - ContextUtil.getCurrentShop().getMarginRatio()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_UP), 0, BigDecimal.ROUND_UP);
		// 最低日次予算金額
		BigDecimal dailyBudgetFlag = monthBudgetFlag.divide(BigDecimal.valueOf(30), 0, BigDecimal.ROUND_UP);
		// 実際総予算金額
		BigDecimal daileBudget = BigDecimal.valueOf(dspCampaignDto.getBudget());
		// 実際日次予算
		BigDecimal dailyBudgetBigDecimal = daileBudget.divide(periodBigDecimal, 0, BigDecimal.ROUND_UP);

		Integer dailyBudget = dailyBudgetBigDecimal.intValue();
		dspCampaignDto.setDailyBudget(dailyBudget);
		Integer monthBudget = dailyBudgetBigDecimal.multiply(BigDecimal.valueOf(30)).intValue();
		dspCampaignDto.setMonthBudget(monthBudget);
		// 入力金額チェック
		if (monthBudget < monthBudgetFlag.intValue() || dailyBudget < dailyBudgetFlag.intValue()) {
			throw new BusinessException(ErrorCodeConstant.E30004);
		}

		if (periodBigDecimal.intValue() > 30) {
			dspCampaignDto.setMonthBudget(dspCampaignDto.getBudget());
		}	
		return dspCampaignDto;
	}

}
