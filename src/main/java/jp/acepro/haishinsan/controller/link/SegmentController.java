package jp.acepro.haishinsan.controller.link;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dto.dsp.DspSegmentDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentGraphDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentSearchDto;
import jp.acepro.haishinsan.dto.dsp.SegmentReportDisplayDto;
import jp.acepro.haishinsan.enums.DateFormatter;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.DspSegmentInputForm;
import jp.acepro.haishinsan.form.DspSegmentSearchForm;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.util.Utf8BomUtil;

@Controller
@RequestMapping("/link/segment")
public class SegmentController {

	@Autowired
	DspSegmentService dspSegmentService;

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	OperationService operationService;

	@GetMapping("/createSegment")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_SEGMENT_MANAGE + "')")
	public ModelAndView createSegment(@ModelAttribute DspSegmentInputForm dspSegmentInputForm) {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("link/createSegment");
		return modelAndView;
	}

	@PostMapping("/completeSegment")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_SEGMENT_MANAGE + "')")
	public ModelAndView completeSegment(DspSegmentInputForm dspSegmentInputForm) {

		DspSegmentDto dspSegmentDto = new DspSegmentDto();
		dspSegmentDto.setSegmentName(dspSegmentInputForm.getSegmentName());
		dspSegmentDto.setUrl(dspSegmentInputForm.getUrl());

		dspSegmentDto = dspSegmentService.createSegment(dspSegmentDto);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("link/completeSegment");
		modelAndView.addObject("dspSegmentDto", dspSegmentDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_SEGMENT_CREATE.getValue(), String.valueOf(dspSegmentDto.getSegmentId()));

		return modelAndView;
	}

	@GetMapping("/segmentList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_SEGMENT_VIEW + "')")
	public ModelAndView segmentList() {

		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("link/segmentList");
		modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);

		// オペレーションログ記録
		operationService.create(Operation.DSP_SEGMENT_LIST.getValue(), null);

		return modelAndView;
	}

	@PostMapping("/segmentDelete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_SEGMENT_MANAGE + "')")
	public ModelAndView deleteSegment(@RequestParam Long segmentManageId) {

		DspSegmentDto dspSegmentDto = dspSegmentService.deleteSegment(segmentManageId);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("link/deleteSegment");
		modelAndView.addObject("dspSegmentDto", dspSegmentDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_SEGMENT_DELETE.getValue(), String.valueOf(segmentManageId));

		return modelAndView;
	}

	@GetMapping("/segmentReportingForBatch")
	public ModelAndView reportingForBatch() {

		dspSegmentService.getSegmentReporting();
		return null;
	}

	@GetMapping("/segmentReporting")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_REPORT_VIEW + "')")
	public ModelAndView segmentReporting(@ModelAttribute DspSegmentSearchForm dspSegmentSearchForm) {

		// DBから ShopIdでセグメント情報取得
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();

		SegmentReportDisplayDto segmentReportDisplayDto = new SegmentReportDisplayDto();

		// レポーティングを取得期間の初期値を付ける
		Date dNow = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dNow);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String endDate = sdf.format(calendar.getTime());
		String startDate = endDate.substring(0, 8) + "01";
		dspSegmentSearchForm.setStartDate(startDate);
		dspSegmentSearchForm.setEndDate(endDate);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("link/segmentReporting");
		modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
		modelAndView.addObject("segmentReportDisplayDto", segmentReportDisplayDto);

		return modelAndView;
	}

	@PostMapping("/segmentReporting")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_REPORT_VIEW + "')")
	public ModelAndView searchBySegmentId(@Validated DspSegmentSearchForm dspSegmentSearchForm, BindingResult result)
			throws ParseException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = sdf.parse(dspSegmentSearchForm.getStartDate());
			Date endDate = sdf.parse(dspSegmentSearchForm.getEndDate());

			Calendar maxEndcalendar = Calendar.getInstance();
			maxEndcalendar.setTime(startDate);
			maxEndcalendar.add(Calendar.MONTH, 3);

			Calendar endcalendar = Calendar.getInstance();
			endcalendar.setTime(endDate);

			if (!endcalendar.before(maxEndcalendar)) {
				throw new BusinessException(ErrorCodeConstant.E30006);
			}
		} catch (BusinessException e) {
			// 異常時レスポンスを作成
			result.reject(e.getMessage(), e.getParams(), null);
			List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();
			SegmentReportDisplayDto segmentReportDisplayDto = new SegmentReportDisplayDto();
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("link/segmentReporting");
			modelAndView.addObject("dspSegmentSearchForm", dspSegmentSearchForm);
			modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
			modelAndView.addObject("segmentReportDisplayDto", segmentReportDisplayDto);
			return modelAndView;
		}

		// DBから ShopIdでセグメント情報取得
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();
		List<Integer> ids = new ArrayList<Integer>();
		dspSegmentDtoList.forEach(dspSegmentDto -> ids.add(dspSegmentDto.getSegmentId()));

		DspSegmentSearchDto dspSegmentSearchDto = new DspSegmentSearchDto();
		dspSegmentSearchDto.setSegmentIdList(dspSegmentSearchForm.getSegmentIdList());
		dspSegmentSearchDto.setStartDate(dspSegmentSearchForm.getStartDate());
		dspSegmentSearchDto.setEndDate(dspSegmentSearchForm.getEndDate());
		// 検索したレポーティングをグラフとして、画面に表示する
		DspSegmentGraphDto dspSegmentGraphDto = dspSegmentService.getSegmentReportGraph(dspSegmentSearchDto);
		// 検索したレポーティングをリストとして、画面に表示する
		List<SegmentReportDisplayDto> segmentReportDisplayDtoList = dspSegmentService
				.getSegmentReportList(dspSegmentSearchDto);
		if (segmentReportDisplayDtoList != null) {
			// レポーティング情報の合計を取得、画面に表示する
			SegmentReportDisplayDto segmentReportDisplayDto = dspSegmentService
					.getSegmentReportSummary(dspSegmentSearchDto);
			segmentReportDisplayDtoList.add(segmentReportDisplayDto);
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("link/segmentReporting");
		modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
		modelAndView.addObject("segmentReportDisplayDtoList", segmentReportDisplayDtoList);
		modelAndView.addObject("dspSegmentGraphDto", dspSegmentGraphDto);
		if (dspSegmentSearchForm.getSegmentIdList() == null || dspSegmentSearchForm.getSegmentIdList().size() == 0) {
			dspSegmentSearchForm.setSegmentIdList(ids);
		}
		modelAndView.addObject("dspSegmentSearchForm", dspSegmentSearchForm);

		// オペレーションログ記録
		operationService.create(Operation.DSP_SEGMENT_REPORT_VIEW.getValue(), null);

		return modelAndView;
	}

	@PostMapping("/segment/download")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_REPORT_VIEW + "')")
	public ResponseEntity<byte[]> download(@ModelAttribute DspSegmentSearchForm dspSegmentSearchForm)
			throws IOException {

		DspSegmentSearchDto dspSegmentSearchDto = new DspSegmentSearchDto();
		dspSegmentSearchDto.setSegmentIdList(dspSegmentSearchForm.getSegmentIdList());
		dspSegmentSearchDto.setStartDate(dspSegmentSearchForm.getStartDate());
		dspSegmentSearchDto.setEndDate(dspSegmentSearchForm.getEndDate());

		// 検索したレポーティングをリストとして、画面に表示する
		List<SegmentReportDisplayDto> segmentReportDisplayDtoList = dspSegmentService
				.getSegmentReportList(dspSegmentSearchDto);
		if (segmentReportDisplayDtoList != null) {
			// レポーティング情報の合計を取得、画面に表示する
			SegmentReportDisplayDto segmentReportDisplayDto = dspSegmentService
					.getSegmentReportSummary(dspSegmentSearchDto);
			segmentReportDisplayDtoList.add(segmentReportDisplayDto);
		}

		// CSVファイル中身を取得し、文字列にする
		String file = dspSegmentService.download(dspSegmentSearchDto);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", applicationProperties.getContentTypeCsvDownload());
		String fimeName = "DSP_SEGMENT_REPORT" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
		httpHeaders.setContentDispositionFormData("filename", fimeName);

		// オペレーションログ記録
		operationService.create(Operation.DSP_SEGMENT_REPORT_DOWNLOAD.getValue(), null);

		return new ResponseEntity<>(Utf8BomUtil.utf8ToWithBom(file), httpHeaders, HttpStatus.OK);
	}
}
