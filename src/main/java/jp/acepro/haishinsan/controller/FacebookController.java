package jp.acepro.haishinsan.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.facebook.ads.sdk.AdsInsights.EnumDatePreset;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.facebook.FbGraphReportDto;
import jp.acepro.haishinsan.dto.facebook.FbReportDisplayDto;
import jp.acepro.haishinsan.dto.facebook.FbTemplateDto;
import jp.acepro.haishinsan.enums.ApprovalFlag;
import jp.acepro.haishinsan.enums.DateFormatter;
import jp.acepro.haishinsan.enums.FacebookArrangePlace;
import jp.acepro.haishinsan.enums.MediaType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.PeriodSet;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.FbCampaignInputForm;
import jp.acepro.haishinsan.form.FbReportInputForm;
import jp.acepro.haishinsan.form.FbTemplateInputForm;
import jp.acepro.haishinsan.mapper.FacebookMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.issue.FacebookReportingService;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.ImageUtil;
import jp.acepro.haishinsan.util.Utf8BomUtil;

@Controller
@RequestMapping("/facebook")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class FacebookController {

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	FacebookService facebookService;

	@Autowired
	FacebookReportingService facebookReportingService;

	@Autowired
	DspSegmentService dspSegmentService;

	@Autowired
	OperationService operationService;

	@Autowired
	ImageUtil imageUtil;

	@GetMapping("/templateList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_VIEW + "')")
	public ModelAndView templateList() {

		// コードマスタをメモリへロード
		getFacebookAreaList();

		// テンプレートリストを取得
		List<FbTemplateDto> fbTemplateDtoList = facebookService.searchList();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/templateList");
		mv.addObject("fbTemplateDtoList", fbTemplateDtoList);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_TEMPLATE_LIST.getValue(), String.valueOf(""));
		return mv;
	}

	@GetMapping("/createTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_MANAGE + "')")
	public ModelAndView createTemplate(@ModelAttribute FbTemplateInputForm fbTempleteInputForm) {

		// コードマスタをメモリへロード
		getFacebookAreaList();

		// -------- 地域初期値設定 --------
		List<Long> locationList = new ArrayList<Long>();
		fbTempleteInputForm.setLocationList(locationList);

		// -------- 地域初期値設定 --------
		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/createTemplate");
		return mv;
	}

	@PostMapping("/completeTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_MANAGE + "')")
	public ModelAndView completeTemplate(@Validated FbTemplateInputForm fbTemplateInputForm, BindingResult result) {

		FbTemplateDto fbTemplateDto = FacebookMapper.INSTANCE.map(fbTemplateInputForm);
		try {
			fbTemplateDto = facebookService.create(fbTemplateDto);
		} catch (BusinessException e) {
			// 異常時レスポンスを作成
			result.reject(e.getMessage());

			ModelAndView mv = new ModelAndView();
			mv.setViewName("facebook/createTemplate");
			mv.addObject("fbTempleteInputForm", fbTemplateInputForm);
			return mv;
		}

		ModelAndView mv = new ModelAndView("facebook/completeTemplate");
		mv.addObject("fbTempleteInputForm", fbTemplateInputForm);
		mv.addObject("fbTemplateDto", fbTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_TEMPLATE_CREATE.getValue(), String.valueOf(fbTemplateDto.getTemplateId()));
		return mv;
	}

	@GetMapping("/templateDetail")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_VIEW + "')")
	public ModelAndView templateDetail(@RequestParam Long templateId) {

		FbTemplateDto fbTemplateDto = facebookService.templateDetail(templateId);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/templateDetail");
		mv.addObject("fbTemplateDto", fbTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_TEMPLATE_VIEW.getValue(), String.valueOf(templateId));
		return mv;

	}

	@PostMapping("/updateTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_MANAGE + "')")
	public ModelAndView updateTemplate(@RequestParam Long templateId) {

		FbTemplateDto fbTemplateDto = facebookService.templateDetail(templateId);
		FbTemplateInputForm fbTemplateInputForm = FacebookMapper.INSTANCE.mapDtoToForm(fbTemplateDto);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/updateTemplate");
		mv.addObject("fbTemplateInputForm", fbTemplateInputForm);
		return mv;

	}

	@PostMapping("/updateTemplateComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_MANAGE + "')")
	public ModelAndView updateTemplateComplete(@Validated FbTemplateInputForm fbTemplateInputForm, BindingResult result) {

		FbTemplateDto fbTemplateDto = FacebookMapper.INSTANCE.mapFormToDto(fbTemplateInputForm);
		try {
			facebookService.templateUpdate(fbTemplateDto);
		} catch (BusinessException e) {
			// 異常時レスポンスを作成
			result.reject(e.getMessage());

			ModelAndView mv = new ModelAndView();
			mv.setViewName("facebook/updateTemplate");
			mv.addObject("fbTempleteInputForm", fbTemplateInputForm);
			return mv;
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/updateTemplateComplete");
		mv.addObject("fbTemplateDto", fbTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_TEMPLATE_UPDATE.getValue(), String.valueOf(fbTemplateInputForm.getTemplateId()));
		return mv;

	}

	@PostMapping("/deleteTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_MANAGE + "')")
	public ModelAndView deleteTemplate(@RequestParam Long templateId) {

		FbTemplateDto fbTemplateDto = facebookService.templateDelete(templateId);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/deleteTemplate");
		mv.addObject("fbTemplateDto", fbTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_TEMPLATE_DELETE.getValue(), String.valueOf(templateId));
		return mv;

	}



	@PostMapping("/completeCampaign")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_CAMPAIGN_MANAGE + "')")
	public ModelAndView completeCampaign(@Validated FbCampaignInputForm fbCampaignInputForm, BindingResult result) throws IOException {

		FbCampaignDto fbCampaignDto = FacebookMapper.INSTANCE.map(fbCampaignInputForm);
		try {
			imageUtil.getImageBytes(fbCampaignInputForm.getImage(), MediaType.FACEBOOK.getValue());
		} catch (BusinessException e) {
			result.reject(e.getMessage(), e.getParams(), null);
			ModelAndView mv = new ModelAndView("facebook/createCampaign");
			// テンプレート一覧を取得
			List<FbTemplateDto> fbTemplateDtoList = facebookService.searchList();
			// コードマスタをメモリへロード
			getFacebookAreaList();
			// ＤＳＰＵＲＬを読込
			List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();
			mv.addObject("fbCampaignInputForm", fbCampaignInputForm);
			mv.addObject("fbTemplateDtoList", fbTemplateDtoList);
			mv.addObject("dspSegmentDtoList", dspSegmentDtoList);
			return mv;
		}
		
		File imageFile = new File(fbCampaignInputForm.getImage().getOriginalFilename());
		FileOutputStream fo = new FileOutputStream(imageFile);
		fo.write(fbCampaignInputForm.getImage().getBytes());
		fo.close();
		fbCampaignDto.setImageFile(imageFile);
		try {
			facebookService.createCampaign(fbCampaignDto, null);
		} catch (BusinessException e) {
			// 異常時レスポンスを作成
			result.reject(e.getMessage());
			ModelAndView mv = new ModelAndView("facebook/createCampaign");
			// テンプレート一覧を取得
			List<FbTemplateDto> fbTemplateDtoList = facebookService.searchList();
			// コードマスタをメモリへロード
			getFacebookAreaList();
			// ＤＳＰＵＲＬを読込
			List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();
			mv.addObject("fbCampaignInputForm", fbCampaignInputForm);
			mv.addObject("fbTemplateDtoList", fbTemplateDtoList);
			mv.addObject("dspSegmentDtoList", dspSegmentDtoList);
			return mv;
		}finally {
			try {
				imageFile.delete();
			}catch(Exception ex) {
				
			}
		}

		ModelAndView mv = new ModelAndView("facebook/completeCampaign");
		mv.addObject("fbCampaignInputForm", fbCampaignInputForm);
		mv.addObject("fbCampaignDto", fbCampaignDto);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_CAMPAIGN_CREATE.getValue(), String.valueOf(fbCampaignDto.getCampaignId()));
		return mv;

	}

	@GetMapping("/campaignDetail")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_CAMPAIGN_VIEW + "')")
	public ModelAndView campaignDetail(@RequestParam String campaignId) {

		// コードマスタをメモリへロード
		getFacebookAreaList();

		FbCampaignDto fbCampaignDto = facebookService.campaignDetail(campaignId);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/campaignDetail");
		mv.addObject("fbCampaignDto", fbCampaignDto);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_CAMPAIGN_VIEW.getValue(), String.valueOf(campaignId));
		return mv;
	}

	@PostMapping("/deleteCampaign")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_CAMPAIGN_MANAGE + "')")
	public ModelAndView deleteCampaign(@RequestParam String campaignId) {

		// コードマスタをメモリへロード
		getFacebookAreaList();

		facebookService.deleteCampaign(campaignId);

		// キャンペーン一覧を取得
		List<FacebookCampaignManage> facebookCampaignManageList = facebookService.searchFacebookCampaignManageList();
		List<FbCampaignDto> fbCampaignDtoList = facebookService.campaignList(facebookCampaignManageList);
		// ログインユーザーのロールIDを取得
		Integer roleId = ContextUtil.getRoleId();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/campaignList");
		mv.addObject("fbCampaignDtoList", fbCampaignDtoList);
		mv.addObject("roleId", roleId);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_CAMPAIGN_DELETE.getValue(), String.valueOf(campaignId));
		return mv;

	}

	@GetMapping("/updateCampaignStatus/{campaignId}/{switchFlag}")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_CAMPAIGN_MANAGE + "')")
	public ModelAndView updateCampaignStatus(@PathVariable String campaignId, @PathVariable String switchFlag) {

		facebookService.updateCampaignStatus(campaignId, switchFlag);

		facebookService.updateCampaignCheckStatus(campaignId, ApprovalFlag.COMPLETED.getValue());

		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/campaignList");

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_CAMPAIGN_STATUS_UPDATE.getValue(), String.valueOf(campaignId));
		return mv;

	}

	@GetMapping("/getReport")
	public ModelAndView getReport() {

		// レポート取得（API経由）
		facebookReportingService.getReportDetails(EnumDatePreset.VALUE_TODAY);
		facebookReportingService.getReportDetails(EnumDatePreset.VALUE_YESTERDAY);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/reporting");
		mv.addObject("reportType", "");
		return mv;
	}

	@GetMapping("/reporting")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_REPORT_VIEW + "')")
	public ModelAndView reporting(@ModelAttribute FbReportInputForm fbReportInputForm) {

		// 該当店舗所有するキャンペーンリストを設定
		fbReportInputForm.setCampaignPairList(getFacebookCampaignManageList());

		FbGraphReportDto fbGraphReportDto = new FbGraphReportDto();
		// 期間設定初期値
		fbReportInputForm.setPeriod(PeriodSet.WHOLE.getValue());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/reporting");
		mv.addObject("fbGraphReportDto", fbGraphReportDto);
		mv.addObject("reportType", "");
		return mv;
	}

	@GetMapping("/deviceReport")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_REPORT_VIEW + "')")
	public ModelAndView getDeviceReport(@ModelAttribute FbReportInputForm fbReportInputForm) {

		// 該当店舗所有するキャンペーンリストを設定
		fbReportInputForm.setCampaignPairList(getFacebookCampaignManageList());
		String startDate = null;
		String endDate = null;
		if (PeriodSet.LIMITED.getValue().equals(fbReportInputForm.getPeriod())) {
			startDate = fbReportInputForm.getStartDate();
			endDate = fbReportInputForm.getEndDate();
		}
		// テーブル表示用のDtoリストを取得
		List<FbReportDisplayDto> fbReportDisplayDtoList = facebookReportingService.getDeviceReport(fbReportInputForm.getCampaignIdList(), startDate, endDate);
		// Graph用
		FbGraphReportDto fbGraphReportDto = facebookReportingService.getFacebookDeviceReportingGraph(fbReportInputForm.getCampaignIdList(), startDate, endDate);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/reporting");
		mv.addObject("fbReportDisplayDtoList", fbReportDisplayDtoList);
		mv.addObject("fbGraphReportDto", fbGraphReportDto);
		mv.addObject("reportType", ReportType.DEVICE.getValue());

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_DEVICE_REPORT_VIEW.getValue(), String.valueOf(""));
		return mv;
	}

	@GetMapping("/regionReport")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_REPORT_VIEW + "')")
	public ModelAndView getRegionReport(@ModelAttribute FbReportInputForm fbReportInputForm) {

		// 該当店舗所有するキャンペーンリストを設定
		fbReportInputForm.setCampaignPairList(getFacebookCampaignManageList());
		String startDate = null;
		String endDate = null;
		if (PeriodSet.LIMITED.getValue().equals(fbReportInputForm.getPeriod())) {
			startDate = fbReportInputForm.getStartDate();
			endDate = fbReportInputForm.getEndDate();
		}
		// テーブル表示用のDtoリストを取得
		List<FbReportDisplayDto> fbReportDisplayDtoList = facebookReportingService.getRegionReport(fbReportInputForm.getCampaignIdList(), startDate, endDate);
		// Graph用
		FbGraphReportDto fbGraphReportDto = facebookReportingService.getFacebookRegionReportingGraph(fbReportInputForm.getCampaignIdList(), startDate, endDate);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/reporting");
		mv.addObject("fbReportDisplayDtoList", fbReportDisplayDtoList);
		mv.addObject("fbGraphReportDto", fbGraphReportDto);
		mv.addObject("reportType", ReportType.REGIONS.getValue());

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_REGION_REPORT_VIEW.getValue(), String.valueOf(""));
		return mv;
	}

	@GetMapping("/dateReport")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_REPORT_VIEW + "')")
	public ModelAndView getDateReport(@ModelAttribute FbReportInputForm fbReportInputForm) {

		// 該当店舗所有するキャンペーンリストを設定
		fbReportInputForm.setCampaignPairList(getFacebookCampaignManageList());
		String startDate = null;
		String endDate = null;
		if (PeriodSet.LIMITED.getValue().equals(fbReportInputForm.getPeriod())) {
			startDate = fbReportInputForm.getStartDate();
			endDate = fbReportInputForm.getEndDate();
		}
		// テーブル表示用のDtoリストを取得
		List<FbReportDisplayDto> fbReportDisplayDtoList = facebookReportingService.getDateReport(fbReportInputForm.getCampaignIdList(), startDate, endDate);
		// Graph用
		FbGraphReportDto fbGraphReportDto = facebookReportingService.getFacebookDateReportingGraph(fbReportInputForm.getCampaignIdList(), startDate, endDate);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook/reporting");
		mv.addObject("fbReportDisplayDtoList", fbReportDisplayDtoList);
		mv.addObject("fbGraphReportDto", fbGraphReportDto);
		mv.addObject("reportType", ReportType.DATE.getValue());

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_DATE_REPORT_VIEW.getValue(), String.valueOf(""));
		return mv;
	}

	@PostMapping("/download")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_REPORT_VIEW + "')")
	public ResponseEntity<byte[]> download(@ModelAttribute FbReportInputForm fbReportInputForm, @RequestParam Integer reportType) throws IOException {

		// 該当店舗所有するキャンペーンリストを設定
		fbReportInputForm.setCampaignPairList(getFacebookCampaignManageList());
		String startDate = null;
		String endDate = null;
		if (PeriodSet.LIMITED.getValue().equals(fbReportInputForm.getPeriod())) {
			startDate = fbReportInputForm.getStartDate();
			endDate = fbReportInputForm.getEndDate();
		}
		// CSVファイル中身を取得し、文字列にする
		String file = facebookReportingService.download(fbReportInputForm.getCampaignIdList(), startDate, endDate, reportType);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", applicationProperties.getContentTypeCsvDownload());
		String fimeName = "FACEBOOK_REPORT" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
		httpHeaders.setContentDispositionFormData("filename", fimeName);

		// オペレーションログ記録
		switch (ReportType.of(reportType)) {
		case DEVICE:
			operationService.create(Operation.FACEBOOK_DEVICE_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
			break;
		case REGIONS:
			operationService.create(Operation.FACEBOOK_REGION_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
			break;
		case DATE:
			operationService.create(Operation.FACEBOOK_DATE_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
			break;
		}
		return new ResponseEntity<>(Utf8BomUtil.utf8ToWithBom(file), httpHeaders, HttpStatus.OK);
	}

	private void getFacebookAreaList() {
		if (CodeMasterServiceImpl.facebookAreaNameList == null) {
			codeMasterService.getFacebookAreaList();
		}
	}

	private List<Pair<String, String>> getFacebookCampaignManageList() {
		// 該当店舗所有するキャンペーンリストを取得
		List<FacebookCampaignManage> facebookCampaignManageList = facebookService.searchFacebookCampaignManageList();
		List<Pair<String, String>> campaignPairList = new ArrayList<Pair<String, String>>();
		for (FacebookCampaignManage facebookCampaignManage : facebookCampaignManageList) {
			campaignPairList.add(Pair.of(facebookCampaignManage.getCampaignId(), facebookCampaignManage.getCampaignName()));
		}
		return campaignPairList;
	}

}
