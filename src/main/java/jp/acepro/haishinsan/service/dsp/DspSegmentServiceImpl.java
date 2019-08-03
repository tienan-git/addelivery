package jp.acepro.haishinsan.service.dsp;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import jp.acepro.haishinsan.bean.DspSegmentCsvBean;
import jp.acepro.haishinsan.dao.DspSegmentCustomDao;
import jp.acepro.haishinsan.dao.SegmentManageDao;
import jp.acepro.haishinsan.dao.SegmentReportManageDao;
import jp.acepro.haishinsan.dao.ShopCustomDao;
import jp.acepro.haishinsan.db.entity.DspToken;
import jp.acepro.haishinsan.db.entity.SegmentManage;
import jp.acepro.haishinsan.db.entity.SegmentReportManage;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.dto.DspSegmentRepDto;
import jp.acepro.haishinsan.dto.dsp.DspCreateSegmentReq;
import jp.acepro.haishinsan.dto.dsp.DspCreateSegmentRes;
import jp.acepro.haishinsan.dto.dsp.DspSegmentDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentGraphDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListReq;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListRes;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListResult;
import jp.acepro.haishinsan.dto.dsp.DspSegmentRepReq;
import jp.acepro.haishinsan.dto.dsp.DspSegmentRepRes;
import jp.acepro.haishinsan.dto.dsp.DspSegmentSearchDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentUpdateReq;
import jp.acepro.haishinsan.dto.dsp.DspSegmentUpdateRes;
import jp.acepro.haishinsan.dto.dsp.SegmentReportDisplayDto;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.service.BaseService;
import jp.acepro.haishinsan.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DspSegmentServiceImpl extends BaseService implements DspSegmentService {

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	SegmentManageDao segmentManageDao;

	@Autowired
	DspSegmentCustomDao dspSegmentCustomDao;

	@Autowired
	DspApiService dspApiService;

	@Autowired
	SegmentReportManageDao segmentReportManageDao;

	@Autowired
	DspSegmentService dspSegmentService;

	@Autowired
	ShopCustomDao shopCustomDao;

	@Override
	@Transactional
	public DspSegmentDto createSegment(DspSegmentDto dspSegmentDto) {

		// Token取得
		DspToken dspToken = dspApiService.getToken();

		// Req URL組み立てる
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder = builder.scheme(applicationProperties.getDspScheme());
		builder = builder.host(applicationProperties.getDspHost());
		builder = builder.path(applicationProperties.getCreateSegment());
		builder = builder.queryParam("token", dspToken.getToken());
		String resource = builder.build().toUri().toString();
		// Req Body作成
		DspCreateSegmentReq dspCreateSegmentReq = new DspCreateSegmentReq();
		dspCreateSegmentReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		dspCreateSegmentReq.setName(dspSegmentDto.getSegmentName());
		dspCreateSegmentReq.setType("t");
		dspCreateSegmentReq.setFrequency(1);
		dspCreateSegmentReq.getUrl_rules().get(0).setUrl_match_rule(dspSegmentDto.getUrl());
		dspCreateSegmentReq.getUrl_rules().get(0).setUrl_match_type(1);
		dspCreateSegmentReq.setShare_type(0);
		dspCreateSegmentReq.setIs_enabled(Flag.ON.getValue());

		// Dsp Segment作成して、Req 返す
		DspCreateSegmentRes dspCreateSegmentRes = null;
		try {
			dspCreateSegmentRes = call(resource, HttpMethod.POST, dspCreateSegmentReq, null, DspCreateSegmentRes.class);
		} catch (Exception e) {
			log.debug("DSP:セグメントを作成エラー、リクエストボディー:{}", dspCreateSegmentReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		// DBに保存
		SegmentManage dspSegement = new SegmentManage();
		dspSegement.setSegmentId(dspCreateSegmentRes.getId());
		dspSegement.setShopId(ContextUtil.getCurrentShop().getShopId());
		dspSegement.setSegmentName(dspSegmentDto.getSegmentName());
		dspSegement.setUrl(dspSegmentDto.getUrl());
		segmentManageDao.insert(dspSegement);

		// 画面へ返却
		DspSegmentDto newDspSegmentDto = new DspSegmentDto();
		newDspSegmentDto.setSegmentId(dspSegement.getSegmentId());
		newDspSegmentDto.setSegmentName(dspSegmentDto.getSegmentName());
		newDspSegmentDto.setUrl(dspSegmentDto.getUrl());

		return newDspSegmentDto;
	}

	@Override
	@Transactional
	public List<DspSegmentListDto> segmentList() {

		// ShopIdでDBにセグメント情報を取得して、リストとして返す
		List<SegmentManage> segmentManageList = dspSegmentCustomDao.selectByShopIdWithEmptyUrl(ContextUtil.getCurrentShop().getShopId());
		// もし、リストが空だったら、そのまま返却
		if (segmentManageList == null || segmentManageList.size() == 0) {
			return null;
		}

		// 取得したセグメント情報のIDをリストに編集
		List<Integer> ids = new ArrayList<Integer>();
		segmentManageList.forEach(s -> ids.add(s.getSegmentId()));

		// Token取得
		DspToken dspToken = dspApiService.getToken();

		// Req URL組み立てる
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder = builder.scheme(applicationProperties.getDspScheme());
		builder = builder.host(applicationProperties.getDspHost());
		builder = builder.path(applicationProperties.getSegmentList());
		builder = builder.queryParam("token", dspToken.getToken());
		String resource = builder.build().toUri().toString();

		// Req Body作成
		DspSegmentListReq dspSegmentListReq = new DspSegmentListReq();
		dspSegmentListReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		dspSegmentListReq.setIds(ids);

		// セグメントリストを取得して、Res 返す
		DspSegmentListRes dspSegmentListRes = null;
		try {
			dspSegmentListRes = call(resource, HttpMethod.POST, dspSegmentListReq, null, DspSegmentListRes.class);
		} catch (Exception e) {
			log.debug("DSP:セグメントリスト取得エラー、リクエストボディー:{}", dspSegmentListReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		// セグメントDto編集する
		List<DspSegmentListDto> dspSegmentListDtoList = new ArrayList<DspSegmentListDto>();
		for (DspSegmentListResult dspSegmentListResult : dspSegmentListRes.getResult()) {
			if (dspSegmentListResult.getIs_enabled() == 1) {
				DspSegmentListDto dspSegmentListDto = new DspSegmentListDto();
				dspSegmentListDto.setSegmentId(dspSegmentListResult.getId());
				dspSegmentListDto.setSegmentName(dspSegmentListResult.getName());
				if (Objects.nonNull(dspSegmentListResult.getUrl_rules()) && !dspSegmentListResult.getUrl_rules().isEmpty() && Objects.nonNull(dspSegmentListResult.getUrl_rules().get(0))) {
					dspSegmentListDto.setUrl(dspSegmentListResult.getUrl_rules().get(0).getUrl_match_rule());
				}
				dspSegmentListDto.setIs_enabled(dspSegmentListResult.getIs_enabled());
				dspSegmentListDtoList.add(dspSegmentListDto);
			}
		}

		return dspSegmentListDtoList;
	}

	@Override
	@Transactional
	public DspSegmentDto deleteSegment(Long segmentManageId) {

		// segmentManageIdでDBにセグメント情報を取得
		SegmentManage segmentManage = segmentManageDao.selectById(segmentManageId);

		// Token取得
		DspToken dspToken = dspApiService.getToken();

		// Req 更新URL組み立てる
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder = builder.scheme(applicationProperties.getDspScheme());
		builder = builder.host(applicationProperties.getDspHost());
		builder = builder.path(applicationProperties.getSegmentUpdate());
		builder = builder.queryParam("token", dspToken.getToken());
		String resource = builder.build().toUri().toString();

		// Req 更新Body作成
		DspSegmentUpdateReq dspSegmentUpdateReq = new DspSegmentUpdateReq();
		dspSegmentUpdateReq.setId(segmentManage.getSegmentId());
		dspSegmentUpdateReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		dspSegmentUpdateReq.setIs_enabled(Flag.OFF.getValue());

		// セグメントを更新する
		try {
			call(resource, HttpMethod.POST, dspSegmentUpdateReq, null, DspSegmentUpdateRes.class);
		} catch (Exception e) {
			log.debug("DSP:セグメント削除エラー、リクエストボディー:{}", dspSegmentUpdateReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		// DBに論理削除
		segmentManage.setIsActived(Flag.OFF.getValue());
		segmentManageDao.update(segmentManage);

		DspSegmentDto dspSegmentDto = new DspSegmentDto();
		dspSegmentDto.setSegmentId(segmentManage.getSegmentId());
		dspSegmentDto.setSegmentName(segmentManage.getSegmentName());
		dspSegmentDto.setUrl(segmentManage.getUrl());

		return dspSegmentDto;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void getSegmentReporting() {

		List<Shop> shopList = shopCustomDao.selectAllShop();
		for (Shop shop : shopList) {
			getDspSegmentReporting(shop);
		}

	}

	@Transactional
	private void getDspSegmentReporting(Shop shop) {

		Date dNow = new Date();
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dNow);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		dBefore = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(dBefore);
		String endDate = sdf.format(dNow);

		// システムDBから保存したセグメントIDリストを取得
		List<SegmentManage> segmentManageList = dspSegmentCustomDao.selectByShopId(shop.getShopId());
		List<Integer> segmentIds = new ArrayList<Integer>();
		segmentManageList.forEach(segmentManage -> segmentIds.add(segmentManage.getSegmentId()));

		// Token取得
		DspToken dspToken = dspApiService.getToken();

		// Req SegmentReporting URL組み立てる
		UriComponentsBuilder segmentRepBuilder = UriComponentsBuilder.newInstance();
		segmentRepBuilder = segmentRepBuilder.scheme(applicationProperties.getDspScheme());
		segmentRepBuilder = segmentRepBuilder.host(applicationProperties.getDspHost());
		segmentRepBuilder = segmentRepBuilder.path(applicationProperties.getSegmentReporting());
		segmentRepBuilder = segmentRepBuilder.queryParam("token", dspToken.getToken());
		String segmentRepResource = segmentRepBuilder.build().toUri().toString();

		// Req SegmentReporting Body作成
		DspSegmentRepReq dspSegmentRepReq = new DspSegmentRepReq();
		dspSegmentRepReq.setUser_id(shop.getDspUserId());
		dspSegmentRepReq.setSegment_ids(segmentIds);
		dspSegmentRepReq.setStart_date(startDate);
		dspSegmentRepReq.setEnd_date(endDate);

		// セグメントレポーティング取得
		DspSegmentRepRes dspSegmentRepRes = null;
		try {
			dspSegmentRepRes = call(segmentRepResource, HttpMethod.POST, dspSegmentRepReq, null, DspSegmentRepRes.class);
		} catch (Exception e) {
			log.debug("DSP:セグメントレポーティングを取得エラー、リクエストボディー:{}", dspSegmentRepReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		// システムDBに保存しているセグメントレポーティング情報を検索条件で削除する
		dspSegmentCustomDao.deleteBySegmentIds(segmentIds, startDate, endDate);
		// 取得したセグメントレポート情報を編集して、システムDBに保存する
		List<SegmentReportManage> sgmtListForInsert = new ArrayList<SegmentReportManage>();
		for (DspSegmentRepDto dspSegmentRepDto : dspSegmentRepRes.getResult()) {
			SegmentManage newSegmentManage = dspSegmentCustomDao.selectBySegmentId(dspSegmentRepDto.getSegment_id());
			if (Objects.isNull(newSegmentManage)) {
				continue;
			}
			SegmentReportManage segmentReportManage = new SegmentReportManage();
			segmentReportManage.setSegmentId(dspSegmentRepDto.getSegment_id());
			segmentReportManage.setSegmentName(newSegmentManage.getSegmentName());
			segmentReportManage.setDate(LocalDate.parse(dspSegmentRepDto.getDate()));
			segmentReportManage.setUunum(dspSegmentRepDto.getUunum());
			segmentReportManage.setUunumPc(dspSegmentRepDto.getUunum_pc());
			segmentReportManage.setUunumSp(dspSegmentRepDto.getUunum_sp());
			sgmtListForInsert.add(segmentReportManage);
		}
		segmentReportManageDao.insert(sgmtListForInsert);
	}

	@Override
	@Transactional
	public DspSegmentGraphDto getSegmentReportGraph(DspSegmentSearchDto dspSegmentSearchDto) {

		DspSegmentGraphDto dspSegmentGraphDto = new DspSegmentGraphDto();

		// 検索条件でシステムDBから、セグメントレポーティング情報を取得
		List<SegmentReportManage> segmentReportManageList = dspSegmentCustomDao.selectReportForGraph(dspSegmentSearchDto);
		if (segmentReportManageList == null || segmentReportManageList.size() == 0) {
			return dspSegmentGraphDto;
		}

		List<String> date = new ArrayList<String>();
		List<String> uunum = new ArrayList<String>();
		List<String> uunumPc = new ArrayList<String>();
		List<String> uunumSp = new ArrayList<String>();

		for (SegmentReportManage segmentReportManage : segmentReportManageList) {
			date.add(segmentReportManage.getDate().toString());
			uunum.add(segmentReportManage.getUunum().toString());
			uunumPc.add(segmentReportManage.getUunumPc().toString());
			uunumSp.add(segmentReportManage.getUunumSp().toString());
		}
		date.add(0, "date");
		uunum.add(0, "uunum");
		uunumPc.add(0, "uunumPc");
		uunumSp.add(0, "uunumSp");
		dspSegmentGraphDto.setDate(date);
		dspSegmentGraphDto.setUunum(uunum);
		dspSegmentGraphDto.setUunumPc(uunumPc);
		dspSegmentGraphDto.setUunumSp(uunumSp);

		return dspSegmentGraphDto;
	}

	@Override
	@Transactional
	public List<SegmentReportDisplayDto> getSegmentReportList(DspSegmentSearchDto dspSegmentSearchDto) {

		List<SegmentReportDisplayDto> segmentReportDisplayDtoList = new ArrayList<SegmentReportDisplayDto>();

		// 検索条件でシステムDBから、セグメントレポーティング情報を取得
		List<SegmentReportManage> segmentReportManageList = dspSegmentCustomDao.selectReportForList(dspSegmentSearchDto);
		if (segmentReportManageList == null || segmentReportManageList.size() == 0) {
			return null;
		}

		// 取得したレポーティング情報を編集して、返却する
		for (SegmentReportManage segmentReportManage : segmentReportManageList) {
			SegmentReportDisplayDto segmentReportDisplayDto = new SegmentReportDisplayDto();
			segmentReportDisplayDto.setSegmentId(segmentReportManage.getSegmentId());
			segmentReportDisplayDto.setSegmentName(segmentReportManage.getSegmentName());
			segmentReportDisplayDto.setDate(segmentReportManage.getDate().toString());
			segmentReportDisplayDto.setUunum(segmentReportManage.getUunum());
			segmentReportDisplayDto.setUunumPc(segmentReportManage.getUunumPc());
			segmentReportDisplayDto.setUunumSp(segmentReportManage.getUunumSp());
			segmentReportDisplayDtoList.add(segmentReportDisplayDto);
		}

		return segmentReportDisplayDtoList;
	}

	@Override
	@Transactional
	public SegmentReportDisplayDto getSegmentReportSummary(DspSegmentSearchDto dspSegmentSearchDto) {

		SegmentReportDisplayDto segmentReportDisplayDto = new SegmentReportDisplayDto();

		// 検索条件でシステムDBから、セグメントレポーティング情報を取得
		List<SegmentReportManage> segmentReportManageList = dspSegmentCustomDao.selectReportForGraph(dspSegmentSearchDto);
		if (segmentReportManageList == null || segmentReportManageList.size() == 0) {
			return segmentReportDisplayDto;
		}
		// 取得したレポーティング情報を編集して、返却する
		Integer uunum = 0;
		Integer uunumPc = 0;
		Integer uunumSp = 0;
		for (SegmentReportManage segmentReportManage : segmentReportManageList) {
			uunum += segmentReportManage.getUunum();
			uunumPc += segmentReportManage.getUunumPc();
			uunumSp += segmentReportManage.getUunumSp();
		}
		segmentReportDisplayDto.setSegmentName("合計");
		segmentReportDisplayDto.setDate("-");
		segmentReportDisplayDto.setUunum(uunum);
		segmentReportDisplayDto.setUunumPc(uunumPc);
		segmentReportDisplayDto.setUunumSp(uunumSp);

		return segmentReportDisplayDto;
	}

	@Override
	@Transactional
	public String download(DspSegmentSearchDto dspSegmentSearchDto) {

		// 検索条件でシステムDBから、セグメントレポーティング情報を取得
		List<SegmentReportDisplayDto> segmentReportList = getSegmentReportList(dspSegmentSearchDto);
		if (segmentReportList == null || segmentReportList.size() == 0) {
			return null;
		}
		SegmentReportDisplayDto summaryDto = getSegmentReportSummary(dspSegmentSearchDto);
		segmentReportList.add(summaryDto);

		List<DspSegmentCsvBean> dspSegmentCsvBeanList = new ArrayList<DspSegmentCsvBean>();
		for (SegmentReportDisplayDto segmentReportDisplayDto : segmentReportList) {
			DspSegmentCsvBean dspSegmentCsvBean = new DspSegmentCsvBean();
			dspSegmentCsvBean.setSegmentName(segmentReportDisplayDto.getSegmentName());
			dspSegmentCsvBean.setSegmentId(segmentReportDisplayDto.getSegmentId());
			dspSegmentCsvBean.setDate(segmentReportDisplayDto.getDate());
			dspSegmentCsvBean.setUunum(segmentReportDisplayDto.getUunum());
			dspSegmentCsvBean.setUunumPc(segmentReportDisplayDto.getUunumPc());
			dspSegmentCsvBean.setUunumSp(segmentReportDisplayDto.getUunumSp());
			dspSegmentCsvBeanList.add(dspSegmentCsvBean);
		}

		StringWriter out = new StringWriter();
		CsvWriterSettings settings = new CsvWriterSettings();
		settings.setHeaders(DspSegmentCsvBean.columnName);

		BeanWriterProcessor<DspSegmentCsvBean> writerProcessor = new BeanWriterProcessor<>(DspSegmentCsvBean.class);
		settings.setRowWriterProcessor(writerProcessor);

		CsvWriter writer = new CsvWriter(out, settings);
		writer.writeHeaders();
		writer.processRecordsAndClose(dspSegmentCsvBeanList);

		// close
		writer.close();

		return out.toString();
	}

	@Override
	@Transactional
	public List<DspSegmentListDto> selectUrlByDateTime(LocalDateTime dateTime) {

		// 検索条件：日付、店舗ID
		List<SegmentManage> segmentManageList = dspSegmentCustomDao.selectUrlByDateTime(dateTime, ContextUtil.getCurrentShop().getShopId());
		// 検索結果がnullの場合、nullを返す
		if (segmentManageList == null || segmentManageList.size() == 0) {
			return null;
		}
		// 取得したセグメント情報のIDをリストに編集
		List<Integer> ids = new ArrayList<Integer>();
		segmentManageList.forEach(s -> ids.add(s.getSegmentId()));

		// Token取得
		DspToken dspToken = dspApiService.getToken();

		// Req URL組み立てる
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder = builder.scheme(applicationProperties.getDspScheme());
		builder = builder.host(applicationProperties.getDspHost());
		builder = builder.path(applicationProperties.getSegmentList());
		builder = builder.queryParam("token", dspToken.getToken());
		String resource = builder.build().toUri().toString();

		// Req Body作成
		DspSegmentListReq dspSegmentListReq = new DspSegmentListReq();
		dspSegmentListReq.setUser_id(ContextUtil.getCurrentShop().getDspUserId());
		dspSegmentListReq.setIds(ids);

		// セグメントリストを取得して、Res 返す
		DspSegmentListRes dspSegmentListRes = null;
		try {
			dspSegmentListRes = call(resource, HttpMethod.POST, dspSegmentListReq, null, DspSegmentListRes.class);
		} catch (Exception e) {
			log.debug("DSP:セグメントリスト取得エラー、リクエストボディー:{}", dspSegmentListReq);
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		// セグメントDto編集する
		List<DspSegmentListDto> dspSegmentListDtoList = new ArrayList<DspSegmentListDto>();
		for (DspSegmentListResult dspSegmentListResult : dspSegmentListRes.getResult()) {
			if (dspSegmentListResult.getIs_enabled() == 1) {
				DspSegmentListDto dspSegmentListDto = new DspSegmentListDto();
				dspSegmentListDto.setSegmentId(dspSegmentListResult.getId());
				dspSegmentListDto.setSegmentName(dspSegmentListResult.getName());
				if (Objects.nonNull(dspSegmentListResult.getUrl_rules()) && !dspSegmentListResult.getUrl_rules().isEmpty() && Objects.nonNull(dspSegmentListResult.getUrl_rules().get(0))) {
					dspSegmentListDto.setUrl(dspSegmentListResult.getUrl_rules().get(0).getUrl_match_rule());
				}
				dspSegmentListDto.setIs_enabled(dspSegmentListResult.getIs_enabled());
				dspSegmentListDtoList.add(dspSegmentListDto);
			}
		}

		return dspSegmentListDtoList;
	}

}
