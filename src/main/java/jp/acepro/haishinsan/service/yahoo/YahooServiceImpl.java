package jp.acepro.haishinsan.service.yahoo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.axis.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.bean.YahooCsvBean;
import jp.acepro.haishinsan.bean.YahooDateReportCsvBean;
import jp.acepro.haishinsan.bean.YahooDeviceReportCsvBean;
import jp.acepro.haishinsan.bean.YahooLocationReportCsvBean;
import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.DspSegmentCustomDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.dao.YahooCampaignManageDao;
import jp.acepro.haishinsan.dao.YahooCustomDao;
import jp.acepro.haishinsan.dao.YahooReportManageDao;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.SegmentManage;
import jp.acepro.haishinsan.db.entity.YahooArea;
import jp.acepro.haishinsan.db.entity.YahooCampaignManage;
import jp.acepro.haishinsan.db.entity.YahooReportManage;
import jp.acepro.haishinsan.dto.EmailCampDetailDto;
import jp.acepro.haishinsan.dto.EmailDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentDto;
import jp.acepro.haishinsan.dto.yahoo.YahooGraphReportDto;
import jp.acepro.haishinsan.dto.yahoo.YahooImageDto;
import jp.acepro.haishinsan.dto.yahoo.YahooIssueDto;
import jp.acepro.haishinsan.dto.yahoo.YahooLocationDto;
import jp.acepro.haishinsan.dto.yahoo.YahooReportDisplayDto;
import jp.acepro.haishinsan.entity.YahooIssueDetail;
import jp.acepro.haishinsan.enums.EmailTemplateType;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.enums.MediaCollection;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.service.EmailService;
import jp.acepro.haishinsan.util.CalculateUtil;
import jp.acepro.haishinsan.util.CharsetDetector;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.DateUtil;
import jp.acepro.haishinsan.util.ImageUtil;
import jp.acepro.haishinsan.util.ReportUtil;

@Service
public class YahooServiceImpl implements YahooService {

	@Autowired
	ApplicationProperties applicationProperties;

	// 入力チェック
	@Override
	public void yahooCampaignCheck(YahooIssueDto yahooIssueDto) {

		// 配信開始、終了日のチェック
		if (DateUtil.toLocalDate(yahooIssueDto.getStartDate())
				.isAfter(DateUtil.toLocalDate(yahooIssueDto.getEndDate()))) {
			throw new BusinessException(ErrorCodeConstant.E60003);
		}

		String adShortTitle = yahooIssueDto.getAdShortTitle();
		String adTitle1 = yahooIssueDto.getAdTitle1();
		String adTitle2 = yahooIssueDto.getAdTitle2();
		String adDescription = yahooIssueDto.getAdDescription();

		// タイトルと説明文のレングスをチェック
		// 短い広告見出し
		if (Objects.nonNull(adShortTitle)
				&& CalculateUtil.strLenCounter(adShortTitle) > applicationProperties.getTitleLength()) {
			throw new BusinessException(ErrorCodeConstant.E60004, "短い広告見出し",
					String.valueOf(applicationProperties.getTitleLength()),
					String.valueOf(applicationProperties.getTitleLength() / 2));
		}
		// 広告見出し１
		if (Objects.nonNull(adTitle1)
				&& CalculateUtil.strLenCounter(adTitle1) > applicationProperties.getTitleLength()) {
			throw new BusinessException(ErrorCodeConstant.E60004, "広告見出し１",
					String.valueOf(applicationProperties.getTitleLength()),
					String.valueOf(applicationProperties.getTitleLength() / 2));
		}
		// 広告見出し２
		if (Objects.nonNull(adTitle2)
				&& CalculateUtil.strLenCounter(adTitle2) > applicationProperties.getTitleLength()) {
			throw new BusinessException(ErrorCodeConstant.E60004, "広告見出し２",
					String.valueOf(applicationProperties.getTitleLength()),
					String.valueOf(applicationProperties.getTitleLength() / 2));
		}
		// 説明文
		if (Objects.nonNull(adDescription)
				&& CalculateUtil.strLenCounter(adDescription) > applicationProperties.getDescriptionLength()) {
			throw new BusinessException(ErrorCodeConstant.E60004, "説明文",
					String.valueOf(applicationProperties.getDescriptionLength()),
					String.valueOf(applicationProperties.getDescriptionLength() / 2));
		}

	}

	@Override
	public List<YahooCsvBean> readCsv(MultipartFile multipartFile) {
		// ファイルのサイズチェック
		if (multipartFile.isEmpty()) {
			throw new BusinessException(ErrorCodeConstant.E00002);
		}

		InputStream ins;
		try {
			ins = multipartFile.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException("システムエラーが発生しました。");
		}
		File f = new File(multipartFile.getOriginalFilename());
		File csvFile = inputStreamToFile(ins, f);

		String charsetName = CharsetDetector.detectCharset(csvFile, applicationProperties.getCsvUploadCharsets());

		if (charsetName == null) {
			throw new BusinessException(ErrorCodeConstant.E00017,
					String.join(",", applicationProperties.getCsvUploadCharsets()));
		}

		BeanListProcessor<YahooCsvBean> rowProcessor = new BeanListProcessor<>(YahooCsvBean.class);

		CsvParserSettings parserSettings = new CsvParserSettings();
		parserSettings.setProcessor(rowProcessor);
		parserSettings.setHeaderExtractionEnabled(true);

		CsvParser parser = new CsvParser(parserSettings);
		parser.parse(csvFile, Charset.forName(charsetName));

		// BeanListProcessor从输入中提取出一个对象列表
		List<YahooCsvBean> beans = rowProcessor.getBeans();
		if (beans.get(beans.size() - 1).getCreateDate() != null
				&& beans.get(beans.size() - 1).getCreateDate().toLowerCase().equals("total")) {
			beans.remove(beans.size() - 1);
		}

		// delete initiated files
		f.delete();
		csvFile.delete();

		return beans;

	}

	/******************** MultipartFileからFileまで変更する ********************/
	private static File inputStreamToFile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	@Autowired
	YahooReportManageDao yahooReportManageDao;

	@Transactional
	@Override
	public void uploadData(List<YahooCsvBean> yahooCsvBeanList) {
		List<YahooReportManage> yahooReportManageList = new ArrayList<YahooReportManage>();

		for (YahooCsvBean yahooCsvBean : yahooCsvBeanList) {
			YahooReportManage yahooReportManage = new YahooReportManage();
			yahooReportManage.setAdvertisingMethod(yahooCsvBean.getAdvertisingMethod());
			yahooReportManage.setAvePublishRank(Double.parseDouble(yahooCsvBean.getAvePublishRank()));
			yahooReportManage.setCampaignId(Long.parseLong(yahooCsvBean.getCampaignId()));
			yahooReportManage.setCampaignName(yahooCsvBean.getCampaignName());
			yahooReportManage.setCampaignType(yahooCsvBean.getCampaignType());
			yahooReportManage.setCityName(yahooCsvBean.getCityName());
			yahooReportManage.setClickCount(Long.parseLong(yahooCsvBean.getClickCount()));
			yahooReportManage.setCost(Double.parseDouble(yahooCsvBean.getCost()));
			yahooReportManage.setCreateDate(
					LocalDate.parse(yahooCsvBean.getCreateDate(), DateTimeFormatter.ofPattern("yyyy-M-d")));
			yahooReportManage.setDeviceName(yahooCsvBean.getDeviceName());
			yahooReportManage.setDistrictName(yahooCsvBean.getDistrictName());
			yahooReportManage.setImpressionCount(Long.parseLong(yahooCsvBean.getImpressionCount()));
			yahooReportManage.setRegionName(yahooCsvBean.getRegionName());
			yahooReportManageList.add(yahooReportManage);
		}
		yahooReportManageDao.insert(yahooReportManageList);

	}

	@Autowired
	YahooCustomDao yahooCustomDao;

	@Override
	public List<YahooCampaignManage> searchYahooCampaignManageList() {

		List<YahooCampaignManage> yahooReportManageList = yahooCustomDao
				.selectByShopId(ContextUtil.getCurrentShop().getShopId());

		return yahooReportManageList;
	}

	@Override
	public List<YahooReportDisplayDto> getDeviceReport(List<String> campaignIdList, String startDate, String endDate) {
		List<YahooReportDisplayDto> yahooReportDisplayDtoList = new ArrayList<YahooReportDisplayDto>();

		LocalDate start = StringUtils.isEmpty(startDate) ? null : DateUtil.toLocalDate(startDate);
		LocalDate end = StringUtils.isEmpty(startDate) ? null : DateUtil.toLocalDate(endDate);

		List<YahooReportManage> yahooReportManageList = yahooCustomDao.selectDeviceReport(campaignIdList, start, end);
		for (YahooReportManage yahooReportManage : yahooReportManageList) {
			Long longPrice = CalculateUtil.getRoundedPrice(Double.valueOf(yahooReportManage.getCost()));
			YahooReportDisplayDto yahooReportDisplayDto = new YahooReportDisplayDto();
			yahooReportDisplayDto.setCampaignId(yahooReportManage.getCampaignId().toString());
			yahooReportDisplayDto.setCampaignName(yahooReportManage.getCampaignName());
			yahooReportDisplayDto.setDevice(yahooReportManage.getDeviceName());
			yahooReportDisplayDto
					.setImpressions(BigDecimal.valueOf(yahooReportManage.getImpressionCount()).toPlainString());
			yahooReportDisplayDto.setClicks(BigDecimal.valueOf(yahooReportManage.getClickCount()).toPlainString());
			yahooReportDisplayDto.setSpend(longPrice.toString());

			String ctr = ReportUtil.calCtr(yahooReportManage.getClickCount(), yahooReportManage.getImpressionCount());
			yahooReportDisplayDto.setCtr(ctr);

			Integer cpc = ReportUtil.calCpc(yahooReportManage.getClickCount(), longPrice);
			yahooReportDisplayDto.setCpc(BigDecimal.valueOf(cpc).toPlainString());

			Integer cpm = ReportUtil.calCpm(yahooReportManage.getImpressionCount(), longPrice);
			yahooReportDisplayDto.setCpm(BigDecimal.valueOf(cpm).toPlainString());

			yahooReportDisplayDtoList.add(yahooReportDisplayDto);
		}

		return getSummary(yahooReportDisplayDtoList);
	}

	@Override
	public List<YahooReportDisplayDto> getRegionReport(List<String> campaignIdList, String startDate, String endDate) {
		List<YahooReportDisplayDto> yahooReportDisplayDtoList = new ArrayList<YahooReportDisplayDto>();

		LocalDate start = StringUtils.isEmpty(startDate) ? null : DateUtil.toLocalDate(startDate);
		LocalDate end = StringUtils.isEmpty(startDate) ? null : DateUtil.toLocalDate(endDate);

		List<YahooReportManage> yahooReportManageList = yahooCustomDao.selectRegionReport(campaignIdList, start, end);
		for (YahooReportManage yahooReportManage : yahooReportManageList) {
			Long doublePrice = CalculateUtil.getRoundedPrice(Double.valueOf(yahooReportManage.getCost()));
			YahooReportDisplayDto yahooReportDisplayDto = new YahooReportDisplayDto();
			yahooReportDisplayDto.setCampaignId(yahooReportManage.getCampaignId().toString());
			yahooReportDisplayDto.setCampaignName(yahooReportManage.getCampaignName());
			yahooReportDisplayDto.setRegion(yahooReportManage.getRegionName());
			yahooReportDisplayDto
					.setImpressions(BigDecimal.valueOf(yahooReportManage.getImpressionCount()).toPlainString());
			yahooReportDisplayDto.setClicks(BigDecimal.valueOf(yahooReportManage.getClickCount()).toPlainString());
			yahooReportDisplayDto.setSpend(doublePrice.toString());

			String ctr = ReportUtil.calCtr(yahooReportManage.getClickCount(), yahooReportManage.getImpressionCount());
			if (ctr == null || "".equals(ctr)) {
				yahooReportDisplayDto.setCtr(String.valueOf(0L));
			} else {
				yahooReportDisplayDto.setCtr(ctr);
			}

			Integer cpc = ReportUtil.calCpc(yahooReportManage.getClickCount(), doublePrice);
			if (cpc == null) {
				yahooReportDisplayDto.setCpc(String.valueOf(0L));
			} else {
				yahooReportDisplayDto.setCpc(BigDecimal.valueOf(cpc).toPlainString());
			}

			Integer cpm = ReportUtil.calCpm(yahooReportManage.getImpressionCount(), doublePrice);
			if (cpm == null) {
				yahooReportDisplayDto.setCpm(String.valueOf(0L));
			} else {
				yahooReportDisplayDto.setCpm(BigDecimal.valueOf(cpm).toPlainString());
			}

			yahooReportDisplayDtoList.add(yahooReportDisplayDto);
		}

		return getSummary(yahooReportDisplayDtoList);
	}

	@Override
	public List<YahooReportDisplayDto> getDateReport(List<String> campaignIdList, String startDate, String endDate) {
		List<YahooReportDisplayDto> yahooReportDisplayDtoList = new ArrayList<YahooReportDisplayDto>();

		LocalDate start = StringUtils.isEmpty(startDate) ? null : DateUtil.toLocalDate(startDate);
		LocalDate end = StringUtils.isEmpty(startDate) ? null : DateUtil.toLocalDate(endDate);

		List<YahooReportManage> yahooReportManageList = yahooCustomDao.selectDateReport(campaignIdList, start, end);
		for (YahooReportManage yahooReportManage : yahooReportManageList) {
			Long longPrice = CalculateUtil.getRoundedPrice(Double.valueOf(yahooReportManage.getCost()));
			YahooReportDisplayDto yahooReportDisplayDto = new YahooReportDisplayDto();
			yahooReportDisplayDto.setCampaignId(yahooReportManage.getCampaignId().toString());
			yahooReportDisplayDto.setCampaignName(yahooReportManage.getCampaignName());
			yahooReportDisplayDto.setDate(DateUtil.fromLocalDate(yahooReportManage.getCreateDate()));
			yahooReportDisplayDto
					.setImpressions(BigDecimal.valueOf(yahooReportManage.getImpressionCount()).toPlainString());
			yahooReportDisplayDto.setClicks(BigDecimal.valueOf(yahooReportManage.getClickCount()).toPlainString());
			yahooReportDisplayDto.setSpend(longPrice.toString());

			String ctr = ReportUtil.calCtr(yahooReportManage.getClickCount(), yahooReportManage.getImpressionCount());
			if (ctr == null || "".equals(ctr)) {
				yahooReportDisplayDto.setCtr(String.valueOf(0L));
			} else {
				yahooReportDisplayDto.setCtr(ctr);
			}

			Integer cpc = ReportUtil.calCpc(yahooReportManage.getClickCount(), longPrice);
			if (cpc == null) {
				yahooReportDisplayDto.setCpc(String.valueOf(0L));
			} else {
				yahooReportDisplayDto.setCpc(BigDecimal.valueOf(cpc).toPlainString());
			}

			Integer cpm = ReportUtil.calCpm(yahooReportManage.getImpressionCount(), longPrice);
			if (cpm == null) {
				yahooReportDisplayDto.setCpm(String.valueOf(0L));
			} else {
				yahooReportDisplayDto.setCpm(BigDecimal.valueOf(cpm).toPlainString());
			}

			yahooReportDisplayDtoList.add(yahooReportDisplayDto);
		}

		return getSummary(yahooReportDisplayDtoList);
	}

	@Autowired
	DspSegmentCustomDao dspSegmentCustomDao;

	@Override
	public List<YahooIssueDto> searchYahooIssueList() {

		// DB Access
		List<Issue> issueList = yahooCustomDao.selectIssueByShopId(ContextUtil.getCurrentShop().getShopId());

		// Entity -> Dto
		List<YahooIssueDto> yahooIssueDtoList = new ArrayList<YahooIssueDto>();
		for (Issue issue : issueList) {
			YahooIssueDto yahooIssueDto = new YahooIssueDto();
			yahooIssueDto.setBudget(issue.getBudget());
			yahooIssueDto.setCampaignName(issue.getCampaignName());
			yahooIssueDto.setEndDate(issue.getEndDate());
			yahooIssueDto.setIssueId(issue.getIssueId());
			yahooIssueDto.setStartDate(issue.getStartDate());

			yahooIssueDtoList.add(yahooIssueDto);
		}

		return yahooIssueDtoList;
	}

	@Override
	public List<DspSegmentDto> searchSegmentList() {

		List<SegmentManage> segmentManageList = dspSegmentCustomDao
				.selectByShopId(ContextUtil.getCurrentShop().getShopId());
		List<DspSegmentDto> dspSegmentDtoList = new ArrayList<DspSegmentDto>();
		for (SegmentManage segmentManage : segmentManageList) {
			DspSegmentDto dspSegmentDto = new DspSegmentDto();
			dspSegmentDto.setSegmentId(segmentManage.getSegmentId());
			dspSegmentDto.setSegmentName(segmentManage.getSegmentName());
			dspSegmentDto.setUrl(segmentManage.getUrl());
			dspSegmentDtoList.add(dspSegmentDto);
		}
		return dspSegmentDtoList;
	}

	@Override
	public List<YahooLocationDto> getLocationList(List<Long> locationIdList) {
		List<YahooArea> yahooAreaList = yahooCustomDao.selectAreaByAreaId(locationIdList);
		List<YahooLocationDto> locationList = new ArrayList<YahooLocationDto>();
		for (YahooArea yahooArea : yahooAreaList) {
			YahooLocationDto yahooLocationDto = new YahooLocationDto();
			yahooLocationDto.setLocationId(yahooArea.getAreaId());
			yahooLocationDto.setLocationName(yahooArea.getAreaName());
			locationList.add(yahooLocationDto);
		}
		return locationList;
	}

	@Autowired
	YahooCampaignManageDao yahooCampaignManageDao;
	@Autowired
	IssueDao issueDao;
	@Autowired
	EmailService emailService;
	@Autowired
	ImageUtil imageUtil;

	@Transactional
	@Override
	public YahooIssueDto createIssue(YahooIssueDto yahooIssueDto, List<YahooImageDto> imaBase64List) {
		// 配信開始、終了日のチェック
		if (DateUtil.toLocalDate(yahooIssueDto.getStartDate())
				.isAfter(DateUtil.toLocalDate(yahooIssueDto.getEndDate()))) {
			throw new BusinessException(ErrorCodeConstant.E60003);
		}

		String adShortTitle = yahooIssueDto.getAdShortTitle();
		String adTitle1 = yahooIssueDto.getAdTitle1();
		String adTitle2 = yahooIssueDto.getAdTitle2();
		String adDescription = yahooIssueDto.getAdDescription();

		// タイトルと説明文のレングスをチェック
		// 短い広告見出し
		if (Objects.nonNull(adShortTitle)
				&& CalculateUtil.strLenCounter(adShortTitle) > applicationProperties.getTitleLength()) {
			throw new BusinessException(ErrorCodeConstant.E60004, "短い広告見出し",
					String.valueOf(applicationProperties.getTitleLength()),
					String.valueOf(applicationProperties.getTitleLength() / 2));
		}
		// 広告見出し１
		if (Objects.nonNull(adTitle1)
				&& CalculateUtil.strLenCounter(adTitle1) > applicationProperties.getTitleLength()) {
			throw new BusinessException(ErrorCodeConstant.E60004, "広告見出し１",
					String.valueOf(applicationProperties.getTitleLength()),
					String.valueOf(applicationProperties.getTitleLength() / 2));
		}
		// 広告見出し２
		if (Objects.nonNull(adTitle2)
				&& CalculateUtil.strLenCounter(adTitle2) > applicationProperties.getTitleLength()) {
			throw new BusinessException(ErrorCodeConstant.E60004, "広告見出し２",
					String.valueOf(applicationProperties.getTitleLength()),
					String.valueOf(applicationProperties.getTitleLength() / 2));
		}
		// 説明文
		if (Objects.nonNull(adDescription)
				&& CalculateUtil.strLenCounter(adDescription) > applicationProperties.getDescriptionLength()) {
			throw new BusinessException(ErrorCodeConstant.E60004, "説明文",
					String.valueOf(applicationProperties.getDescriptionLength()),
					String.valueOf(applicationProperties.getDescriptionLength() / 2));
		}

		// DTO->Entity
		YahooCampaignManage yahooCampaignManage = new YahooCampaignManage();
		yahooCampaignManage.setCampaignName(yahooIssueDto.getCampaignName());
		yahooCampaignManage.setAdvDestination(yahooIssueDto.getAdvDestination());
		yahooCampaignManage.setDeviceType(yahooIssueDto.getDeviceType());
		yahooCampaignManage.setArea(yahooIssueDto.getLocationIds());
		yahooCampaignManage.setUrl(yahooIssueDto.getUrl());
		yahooCampaignManage.setAdShortTitle(yahooIssueDto.getAdShortTitle());
		yahooCampaignManage.setAdTitle1(yahooIssueDto.getAdTitle1());
		yahooCampaignManage.setAdTitle2(yahooIssueDto.getAdTitle2());
		yahooCampaignManage.setAdDescription(yahooIssueDto.getAdDescription());
		yahooCampaignManage.setImageName(yahooIssueDto.getImageName());

		// DB access
		yahooCampaignManageDao.insert(yahooCampaignManage);

		// DTO->Entity
		Issue issue = new Issue();
		issue.setShopId(ContextUtil.getCurrentShop().getShopId());
		issue.setYahooCampaignManageId(yahooCampaignManage.getYahooCampaignManageId());
		issue.setCampaignName(yahooIssueDto.getCampaignName());
		issue.setBudget(yahooIssueDto.getBudget());
		issue.setEndDate(yahooIssueDto.getEndDate());
		issue.setStartDate(yahooIssueDto.getStartDate());

		// DB access
		issueDao.insert(issue);
		yahooIssueDto.setIssueId(issue.getIssueId());

		EmailDto emailDto = new EmailDto();
		emailDto.setIssueId(yahooIssueDto.getIssueId());
		EmailCampDetailDto emailCampDetailDto = new EmailCampDetailDto();
		emailCampDetailDto.setCampaignName(yahooIssueDto.getCampaignName());
		// 5:yahoo
		emailCampDetailDto.setMediaType(MediaCollection.YAHOO.getValue());
		List<EmailCampDetailDto> emailCampDetailDtoList = new ArrayList<EmailCampDetailDto>();
		emailCampDetailDtoList.add(emailCampDetailDto);
		emailDto.setCampaignList(emailCampDetailDtoList);
		emailDto.setAttachmentList(imaBase64List);

		// Template type - 案件依頼
		emailDto.setTemplateType(EmailTemplateType.ISSUEREQUEST.getValue());
		emailService.sendEmail(emailDto);

		return yahooIssueDto;
	}

	@Transactional
	@Override
	public YahooIssueDto deleteIssue(Long issueId) {

		YahooIssueDto yahooIssueDto = this.getIssueDetail(issueId);
		YahooCampaignManage yahooCampaignManage = yahooCampaignManageDao
				.selectById(yahooIssueDto.getYahooCampaignManageId());

		// 該当キャンペーンを論理削除
		yahooCampaignManage.setIsActived(Flag.OFF.getValue());
		yahooCampaignManageDao.update(yahooCampaignManage);

		Issue issue = issueDao.selectById(issueId);
		issue.setIsActived(Flag.OFF.getValue());
		issueDao.update(issue);

		if (Objects.nonNull(yahooIssueDto.getImageName())) {
			yahooIssueDto.setImageNameList(Arrays.asList(yahooIssueDto.getImageName().split(",")));
		}

		return yahooIssueDto;

	}

	@Override
	public YahooIssueDto getIssueDetail(Long issueId) {

		// DB access
		YahooIssueDetail yahooIssueDetail = yahooCustomDao.selectIssueDetail(issueId);

		// Entity -> Dto
		YahooIssueDto yahooIssueDto = new YahooIssueDto();
		yahooIssueDto.setAdvDestination(yahooIssueDetail.getAdvDestination());
		yahooIssueDto.setBudget(yahooIssueDetail.getBudget());
		yahooIssueDto.setCampaignId(yahooIssueDetail.getCampaignId());
		yahooIssueDto.setCampaignName(yahooIssueDetail.getCampaignName());
		yahooIssueDto.setEndDate(yahooIssueDetail.getEndDate());
		yahooIssueDto.setImageName(yahooIssueDetail.getImageName());
		yahooIssueDto.setIssueId(yahooIssueDetail.getIssueId());
		yahooIssueDto.setLocationIds(yahooIssueDetail.getArea());
		yahooIssueDto.setStartDate(yahooIssueDetail.getStartDate());
		yahooIssueDto.setUrl(yahooIssueDetail.getUrl());
		yahooIssueDto.setAdDescription(yahooIssueDetail.getAdDescription());
		yahooIssueDto.setAdShortTitle(yahooIssueDetail.getAdShortTitle());
		yahooIssueDto.setAdTitle1(yahooIssueDetail.getAdTitle1());
		yahooIssueDto.setAdTitle2(yahooIssueDetail.getAdTitle2());
		yahooIssueDto.setYahooCampaignManageId(yahooIssueDetail.getYahooCampaignManageId());
		yahooIssueDto.setDeviceType(yahooIssueDetail.getDeviceType());

		if (Objects.nonNull(yahooIssueDto.getImageName())) {
			yahooIssueDto.setImageNameList(Arrays.asList(yahooIssueDto.getImageName().split(",")));
		}

		return yahooIssueDto;
	}

	@Transactional
	@Override
	public void updateIssue(Long issueId, String campaignId) {

		YahooIssueDto YahooIssueDto = this.getIssueDetail(issueId);
		YahooCampaignManage yahooCampaignManage = yahooCampaignManageDao
				.selectById(YahooIssueDto.getYahooCampaignManageId());

		// キャンペーンIDを更新する
		yahooCampaignManage.setCampaignId(campaignId);
		yahooCampaignManageDao.update(yahooCampaignManage);

	}

	@Override
	public YahooGraphReportDto getYahooDeviceReportingGraph(List<String> campaignIdList, String startDate,
			String endDate) {
		List<String> reportTypeList = new ArrayList<>(Arrays.asList("x"));
		List<String> impressionList = new ArrayList<>(Arrays.asList("impressions"));
		List<String> clicksList = new ArrayList<>(Arrays.asList("clicks"));
		List<String> spendList = new ArrayList<>(Arrays.asList("spend"));
		List<String> CTRList = new ArrayList<>(Arrays.asList("ctr"));
		List<String> CPCList = new ArrayList<>(Arrays.asList("cpc"));
		List<String> CPMList = new ArrayList<>(Arrays.asList("cpm"));

		List<YahooReportManage> yahooReportManageList = yahooCustomDao.selectDeviceReportGraph(campaignIdList,
				DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));

		for (YahooReportManage yahooReportManage : yahooReportManageList) {
			Long longPrice = CalculateUtil.getRoundedPrice(Double.valueOf(yahooReportManage.getCost()));
			reportTypeList.add(yahooReportManage.getDeviceName());
			impressionList.add(yahooReportManage.getImpressionCount().toString());
			clicksList.add(yahooReportManage.getClickCount().toString());
			spendList.add(longPrice.toString());
			CTRList.add(ReportUtil.calCtr(yahooReportManage.getClickCount(), yahooReportManage.getImpressionCount())
					.toString());
			CPCList.add(ReportUtil.calCpc(yahooReportManage.getClickCount(), longPrice).toString());
			CPMList.add(ReportUtil.calCpm(yahooReportManage.getImpressionCount(), longPrice).toString());
		}

		YahooGraphReportDto yahooGraphReportDto = new YahooGraphReportDto();
		yahooGraphReportDto.setReportTypeList(reportTypeList);
		yahooGraphReportDto.setImpressionList(impressionList);
		yahooGraphReportDto.setClicksList(clicksList);
		yahooGraphReportDto.setSpendList(spendList);
		yahooGraphReportDto.setCTRList(CTRList);
		yahooGraphReportDto.setCPCList(CPCList);
		yahooGraphReportDto.setCPMList(CPMList);

		return yahooGraphReportDto;
	}

	@Override
	public YahooGraphReportDto getYahooDateReportingGraph(List<String> campaignIdList, String startDate,
			String endDate) {
		List<String> reportTypeList = new ArrayList<>(Arrays.asList("x"));
		List<String> impressionList = new ArrayList<>(Arrays.asList("impressions"));
		List<String> clicksList = new ArrayList<>(Arrays.asList("clicks"));
		List<String> spendList = new ArrayList<>(Arrays.asList("spend"));
		List<String> CTRList = new ArrayList<>(Arrays.asList("ctr"));
		List<String> CPCList = new ArrayList<>(Arrays.asList("cpc"));
		List<String> CPMList = new ArrayList<>(Arrays.asList("cpm"));

		List<YahooReportManage> yahooReportManageList = yahooCustomDao.selectDateReportGraph(campaignIdList,
				DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));

		for (YahooReportManage yahooReportManage : yahooReportManageList) {
			Long longPrice = CalculateUtil.getRoundedPrice(Double.valueOf(yahooReportManage.getCost()));
			reportTypeList.add(yahooReportManage.getCreateDate().toString());
			impressionList.add(yahooReportManage.getImpressionCount().toString());
			clicksList.add(yahooReportManage.getClickCount().toString());
			spendList.add(longPrice.toString());
			CTRList.add(ReportUtil.calCtr(yahooReportManage.getClickCount(), yahooReportManage.getImpressionCount())
					.toString());
			CPCList.add(ReportUtil.calCpc(yahooReportManage.getClickCount(), longPrice).toString());
			CPMList.add(ReportUtil.calCpm(yahooReportManage.getImpressionCount(), longPrice).toString());
		}

		YahooGraphReportDto yahooGraphReportDto = new YahooGraphReportDto();
		yahooGraphReportDto.setReportTypeList(reportTypeList);
		yahooGraphReportDto.setImpressionList(impressionList);
		yahooGraphReportDto.setClicksList(clicksList);
		yahooGraphReportDto.setSpendList(spendList);
		yahooGraphReportDto.setCTRList(CTRList);
		yahooGraphReportDto.setCPCList(CPCList);
		yahooGraphReportDto.setCPMList(CPMList);

		return yahooGraphReportDto;
	}

	@Override
	public YahooGraphReportDto getYahooRegionReportingGraph(List<String> campaignIdList, String startDate,
			String endDate) {
		List<String> reportTypeList = new ArrayList<>(Arrays.asList("x"));
		List<String> impressionList = new ArrayList<>(Arrays.asList("impressions"));
		List<String> clicksList = new ArrayList<>(Arrays.asList("clicks"));
		List<String> spendList = new ArrayList<>(Arrays.asList("spend"));
		List<String> CTRList = new ArrayList<>(Arrays.asList("ctr"));
		List<String> CPCList = new ArrayList<>(Arrays.asList("cpc"));
		List<String> CPMList = new ArrayList<>(Arrays.asList("cpm"));

		List<YahooReportManage> yahooReportManageList = yahooCustomDao.selectRegionReportGraph(campaignIdList,
				DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));

		for (YahooReportManage yahooReportManage : yahooReportManageList) {
			Long longPrice = CalculateUtil.getRoundedPrice(Double.valueOf(yahooReportManage.getCost()));
			reportTypeList.add(yahooReportManage.getRegionName());
			impressionList.add(yahooReportManage.getImpressionCount().toString());
			clicksList.add(yahooReportManage.getClickCount().toString());
			spendList.add(longPrice.toString());
			CTRList.add(ReportUtil.calCtr(yahooReportManage.getClickCount(), yahooReportManage.getImpressionCount())
					.toString());
			CPCList.add(ReportUtil.calCpc(yahooReportManage.getClickCount(), longPrice).toString());
			CPMList.add(ReportUtil.calCpm(yahooReportManage.getImpressionCount(), longPrice).toString());
		}

		YahooGraphReportDto yahooGraphReportDto = new YahooGraphReportDto();
		yahooGraphReportDto.setReportTypeList(reportTypeList);
		yahooGraphReportDto.setImpressionList(impressionList);
		yahooGraphReportDto.setClicksList(clicksList);
		yahooGraphReportDto.setSpendList(spendList);
		yahooGraphReportDto.setCTRList(CTRList);
		yahooGraphReportDto.setCPCList(CPCList);
		yahooGraphReportDto.setCPMList(CPMList);

		return yahooGraphReportDto;
	}

	@Override
	public String download(List<String> campaignIdList, String startDate, String endDate, Integer reportType) {

		// 初期処理
		StringWriter out = new StringWriter();
		CsvWriterSettings settings = new CsvWriterSettings();
		CsvWriter writer = null;

		List<YahooReportDisplayDto> yahooReportDisplayDtoList = new ArrayList<YahooReportDisplayDto>();
		switch (ReportType.of(reportType)) {
		case DEVICE:
			yahooReportDisplayDtoList = getDeviceReport(campaignIdList, startDate, endDate);
			List<YahooDeviceReportCsvBean> yahooDeviceReportCsvBeanList = new ArrayList<YahooDeviceReportCsvBean>();
			for (YahooReportDisplayDto yahooReportDisplayDto : yahooReportDisplayDtoList) {
				YahooDeviceReportCsvBean yahooDeviceReportCsvBean = new YahooDeviceReportCsvBean();
				yahooDeviceReportCsvBean.setCampaignId(yahooReportDisplayDto.getCampaignId());
				yahooDeviceReportCsvBean.setCampaignName(yahooReportDisplayDto.getCampaignName());
				yahooDeviceReportCsvBean.setClicks(yahooReportDisplayDto.getClicks());
				yahooDeviceReportCsvBean.setCosts(yahooReportDisplayDto.getSpend());
				yahooDeviceReportCsvBean.setCpc(yahooReportDisplayDto.getCpc());
				yahooDeviceReportCsvBean.setCpm(yahooReportDisplayDto.getCpm());
				yahooDeviceReportCsvBean.setCtr(yahooReportDisplayDto.getCtr());
				yahooDeviceReportCsvBean.setDeviceName(yahooReportDisplayDto.getDevice());
				yahooDeviceReportCsvBean.setImpressions(yahooReportDisplayDto.getImpressions());

				yahooDeviceReportCsvBeanList.add(yahooDeviceReportCsvBean);
			}
			BeanWriterProcessor<YahooDeviceReportCsvBean> deviceWriterProcessor = new BeanWriterProcessor<>(
					YahooDeviceReportCsvBean.class);
			settings.setHeaders(YahooDeviceReportCsvBean.columnName);
			settings.setRowWriterProcessor(deviceWriterProcessor);
			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(yahooDeviceReportCsvBeanList);
			break;
		case REGIONS:
			yahooReportDisplayDtoList = getRegionReport(campaignIdList, startDate, endDate);
			List<YahooLocationReportCsvBean> yahooLocationReportCsvBeanList = new ArrayList<YahooLocationReportCsvBean>();
			for (YahooReportDisplayDto yahooReportDisplayDto : yahooReportDisplayDtoList) {
				YahooLocationReportCsvBean yahooLocationReportCsvBean = new YahooLocationReportCsvBean();
				yahooLocationReportCsvBean.setCampaignId(yahooReportDisplayDto.getCampaignId());
				yahooLocationReportCsvBean.setCampaignName(yahooReportDisplayDto.getCampaignName());
				yahooLocationReportCsvBean.setClicks(yahooReportDisplayDto.getClicks());
				yahooLocationReportCsvBean.setCosts(yahooReportDisplayDto.getSpend());
				yahooLocationReportCsvBean.setCpc(yahooReportDisplayDto.getCpc());
				yahooLocationReportCsvBean.setCpm(yahooReportDisplayDto.getCpm());
				yahooLocationReportCsvBean.setCtr(yahooReportDisplayDto.getCtr());
				yahooLocationReportCsvBean.setLocationName(yahooReportDisplayDto.getRegion());
				yahooLocationReportCsvBean.setImpressions(yahooReportDisplayDto.getImpressions());

				yahooLocationReportCsvBeanList.add(yahooLocationReportCsvBean);
			}
			BeanWriterProcessor<YahooLocationReportCsvBean> locationWriterProcessor = new BeanWriterProcessor<>(
					YahooLocationReportCsvBean.class);
			settings.setHeaders(YahooLocationReportCsvBean.columnName);
			settings.setRowWriterProcessor(locationWriterProcessor);
			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(yahooLocationReportCsvBeanList);
			break;
		case DATE:
			yahooReportDisplayDtoList = getRegionReport(campaignIdList, startDate, endDate);
			List<YahooDateReportCsvBean> yahooDateReportCsvBeanList = new ArrayList<YahooDateReportCsvBean>();
			for (YahooReportDisplayDto yahooReportDisplayDto : yahooReportDisplayDtoList) {
				YahooDateReportCsvBean yahooDateReportCsvBean = new YahooDateReportCsvBean();
				yahooDateReportCsvBean.setCampaignId(yahooReportDisplayDto.getCampaignId());
				yahooDateReportCsvBean.setCampaignName(yahooReportDisplayDto.getCampaignName());
				yahooDateReportCsvBean.setClicks(yahooReportDisplayDto.getClicks());
				yahooDateReportCsvBean.setCosts(yahooReportDisplayDto.getSpend());
				yahooDateReportCsvBean.setCpc(yahooReportDisplayDto.getCpc());
				yahooDateReportCsvBean.setCpm(yahooReportDisplayDto.getCpm());
				yahooDateReportCsvBean.setCtr(yahooReportDisplayDto.getCtr());
				yahooDateReportCsvBean.setDate(yahooReportDisplayDto.getDate());
				yahooDateReportCsvBean.setImpressions(yahooReportDisplayDto.getImpressions());

				yahooDateReportCsvBeanList.add(yahooDateReportCsvBean);
			}
			BeanWriterProcessor<YahooDateReportCsvBean> dateWriterProcessor = new BeanWriterProcessor<>(
					YahooDateReportCsvBean.class);
			settings.setHeaders(YahooDateReportCsvBean.columnName);
			settings.setRowWriterProcessor(dateWriterProcessor);
			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(yahooDateReportCsvBeanList);
			break;

		default:
			break;
		}
		// 終了処理
		writer.close();
		return out.toString();
	}

	// 合計データ
	private List<YahooReportDisplayDto> getSummary(List<YahooReportDisplayDto> yahooReportDisplayDtoList) {

		YahooReportDisplayDto sumReportDto = new YahooReportDisplayDto();
		sumReportDto.setCampaignId("合計");
		sumReportDto.setCampaignName("-");
		sumReportDto.setDevice("-");
		sumReportDto.setRegion("-");
		sumReportDto.setDate("-");
		sumReportDto.setImpressions(String
				.valueOf(yahooReportDisplayDtoList.stream().mapToLong(d -> Long.valueOf(d.getImpressions())).sum()));
		sumReportDto.setClicks(
				String.valueOf(yahooReportDisplayDtoList.stream().mapToLong(d -> Long.valueOf(d.getClicks())).sum()));
		sumReportDto.setSpend(
				String.valueOf(yahooReportDisplayDtoList.stream().mapToLong(d -> Long.valueOf(d.getSpend())).sum()));
		sumReportDto.setCtr(
				ReportUtil.calCtr(Long.valueOf(sumReportDto.getClicks()), Long.valueOf(sumReportDto.getImpressions()))
						.toString());
		sumReportDto.setCpc(ReportUtil
				.calCpc(Long.valueOf(sumReportDto.getClicks()), Long.valueOf(sumReportDto.getSpend())).toString());
		sumReportDto.setCpm(ReportUtil
				.calCpm(Long.valueOf(sumReportDto.getImpressions()), Long.valueOf(sumReportDto.getSpend())).toString());

		yahooReportDisplayDtoList.add(sumReportDto);

		return yahooReportDisplayDtoList;
	}

}
