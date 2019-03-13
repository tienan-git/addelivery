package jp.acepro.haishinsan.service.dsp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.dao.CreativeManageDao;
import jp.acepro.haishinsan.dao.DspCreativeCustomDao;
import jp.acepro.haishinsan.dao.DspTokenCustomDao;
import jp.acepro.haishinsan.db.entity.CreativeManage;
import jp.acepro.haishinsan.db.entity.DspToken;
import jp.acepro.haishinsan.dto.dsp.DspCreateCreativeReq;
import jp.acepro.haishinsan.dto.dsp.DspCreateCreativeRes;
import jp.acepro.haishinsan.dto.dsp.DspCreativeDto;
import jp.acepro.haishinsan.dto.dsp.DspCreativeListDto;
import jp.acepro.haishinsan.dto.dsp.DspCreativeListReq;
import jp.acepro.haishinsan.dto.dsp.DspCreativeListRes;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.service.BaseService;
import jp.acepro.haishinsan.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DspCreativeServiceImpl extends BaseService implements DspCreativeService {

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	DspTokenCustomDao dspTokenCustomDao;

	@Autowired
	DspCreativeCustomDao dspCreativeCustomDao;

	@Autowired
	CreativeManageDao creativeManageDao;

	@Autowired
	DspApiService dspApiService;

	@Override
	@Transactional
	public List<DspCreativeDto> creativeList() {

		// ShopIdでCreative情報取得
		List<CreativeManage> creativeManageList = dspCreativeCustomDao.selectByShopId(ContextUtil.getCurrentShop().getShopId());
		// DBでクリエイティブ情報を取得できなかったの場合、NULLを返却する
		if (creativeManageList == null || creativeManageList.size() == 0) {
			return null;
		}
		
		// Token取得
		DspToken dspToken = dspApiService.getToken();

		// Req URL作成
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder = builder.scheme(applicationProperties.getDspScheme());
		builder = builder.host(applicationProperties.getDspHost());
		builder = builder.path(applicationProperties.getCreativeList());
		builder = builder.queryParam("token", dspToken.getToken());
		String resource = builder.build().toUri().toString();

		// Body作成
		DspCreativeListReq dspCreativeListReq = new DspCreativeListReq();
		dspCreativeListReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		List<Integer> ids = new ArrayList<Integer>();
		for (CreativeManage creativeManage : creativeManageList) {
			Integer id = Integer.valueOf(creativeManage.getCreativeId());
			ids.add(id);
		}
		dspCreativeListReq.setIds(ids);

		// Creativeリスト 取得
		DspCreativeListRes dspCreativeListResList = null;
		try {
			dspCreativeListResList = call(resource, HttpMethod.POST, dspCreativeListReq, null, DspCreativeListRes.class);
		} catch (Exception e) {
			log.debug("DSP:クリエイティブリスト取得エラー、リクエストボディー:{}", dspCreativeListReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
		// APIでクリエイティブ情報を取得できなかったの場合、NULLを返却する
		if (dspCreativeListResList.getResult() == null) {
			return null;
		}

		// DspCreativeDtoをリストとして 返す
		List<DspCreativeDto> newDspCreativeDtoList = new ArrayList<DspCreativeDto>();
		for (DspCreativeListDto dspCreativeListDto : dspCreativeListResList.getResult()) {
			DspCreativeDto newDspCreativeDto = new DspCreativeDto();
			newDspCreativeDto.setCreativeId(dspCreativeListDto.getId());
			newDspCreativeDto.setCreativeName(dspCreativeListDto.getName());
			newDspCreativeDto.setSrc(dspCreativeListDto.getSrc());
			newDspCreativeDto.setScreening(dspCreativeListDto.getScreening());
			newDspCreativeDtoList.add(newDspCreativeDto);
		}

		return newDspCreativeDtoList;
	}

	@Override
	@Transactional
	public DspCreativeDto createCreative(DspCreativeDto dspCreativeDto) {

		// Token取得
		DspToken dspToken = dspApiService.getToken();

		// Req URL組み立てる
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder = builder.scheme(applicationProperties.getDspScheme());
		builder = builder.host(applicationProperties.getDspHost());
		builder = builder.path(applicationProperties.getCreateCreative());
		builder = builder.queryParam("token", dspToken.getToken());
		String resource = builder.build().toUri().toString();

		// Res Body作成
		DspCreateCreativeReq dspCreateCreativeReq = new DspCreateCreativeReq();
		dspCreateCreativeReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		dspCreateCreativeReq.setName(dspCreativeDto.getCreativeName());
		dspCreateCreativeReq.setBody(dspCreativeDto.getBase64Str());

		DspCreateCreativeRes dspCreateCreativeRes = null;
		try {
			dspCreateCreativeRes = call(resource, HttpMethod.POST, dspCreateCreativeReq, null, DspCreateCreativeRes.class);
		} catch (Exception e) {
			log.debug("DSP:クリエイティブを作成エラー、リクエストボディー:{}", dspCreateCreativeReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		// DBに保存する
		CreativeManage creativeManage = new CreativeManage();
		creativeManage.setCreativeId(dspCreateCreativeRes.getId());
		creativeManage.setShopId(ContextUtil.getCurrentShop().getShopId());
		creativeManage.setCreativeName(dspCreativeDto.getCreativeName());
		creativeManage.setUrl(dspCreateCreativeRes.getSrc());
		creativeManageDao.insert(creativeManage);

		DspCreativeDto newDspCreativeDto = new DspCreativeDto();
		newDspCreativeDto.setCreativeId(dspCreateCreativeRes.getId());
		newDspCreativeDto.setCreativeName(dspCreateCreativeRes.getName());
		newDspCreativeDto.setSrc(dspCreateCreativeRes.getSrc());
		
		return dspCreativeDto;
	}

	@Override
	public DspCreativeDto creativeDetail(Integer creativeId) {

		// Token取得
		DspToken dspToken = dspApiService.getToken();

		// Req AdGroup URL組み立てる
		UriComponentsBuilder creativeBuilder = UriComponentsBuilder.newInstance();
		creativeBuilder = creativeBuilder.scheme(applicationProperties.getDspScheme());
		creativeBuilder = creativeBuilder.host(applicationProperties.getDspHost());
		creativeBuilder = creativeBuilder.path(applicationProperties.getCreativeList());
		creativeBuilder = creativeBuilder.queryParam("token", dspToken.getToken());
		String creativeResource = creativeBuilder.build().toUri().toString();

		// Body作成
		DspCreativeListReq dspCreativeListReq = new DspCreativeListReq();
		dspCreativeListReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		dspCreativeListReq.setId(creativeId);

		// CreativeIdで クリエイティブ情報取得
		DspCreativeListRes dspCreativeListRes = null;
		try {
			dspCreativeListRes = call(creativeResource, HttpMethod.POST, dspCreativeListReq, null, DspCreativeListRes.class);
		} catch (Exception e) {
			log.debug("DSP:クリエイティブ詳細を取得エラー、リクエストボディー:{}", dspCreativeListReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		DspCreativeDto dspCreativeDto = new DspCreativeDto();
		dspCreativeDto.setCreativeId(dspCreativeListRes.getResult().get(0).getId());
		dspCreativeDto.setCreativeName(dspCreativeListRes.getResult().get(0).getName());
		dspCreativeDto.setSrc(dspCreativeListRes.getResult().get(0).getSrc());
		
		return dspCreativeDto;
	}

	@Override
	@Transactional
	public void creativeDelete(Integer creativeId) {

		CreativeManage creativeManage = dspCreativeCustomDao.selectByCreativeId(creativeId);
		creativeManage.setIsActived(Flag.OFF.getValue());
		creativeManageDao.update(creativeManage);
	}

	@Override
	@Transactional
	public List<DspCreativeDto> creativeListFromDb() {

		// ShopIdでDBからCreative情報取得
		List<CreativeManage> creativeManageList = dspCreativeCustomDao.selectByShopId(ContextUtil.getCurrentShop().getShopId());
		List<DspCreativeDto> dspCreativeDtoList = new ArrayList<DspCreativeDto>();
		for (CreativeManage dreativeManage : creativeManageList) {
			DspCreativeDto dspCreativeDto = new DspCreativeDto();
			dspCreativeDto.setCreativeId(Integer.valueOf(dreativeManage.getCreativeId()));
			dspCreativeDto.setCreativeName(dreativeManage.getCreativeName());
			dspCreativeDtoList.add(dspCreativeDto);
		}
		
		return dspCreativeDtoList;
	}
}
