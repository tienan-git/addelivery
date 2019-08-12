package jp.acepro.haishinsan.controller.campaign.google;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.io.ByteSource;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import jp.acepro.haishinsan.db.entity.GoogleCampaignManage;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDetailDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignInfoDto;
import jp.acepro.haishinsan.dto.google.GoogleTemplateDto;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.FbIssueInputForm;
import jp.acepro.haishinsan.form.GoogleCampaignForm;
import jp.acepro.haishinsan.form.GoogleIssueInputForm;
import jp.acepro.haishinsan.mapper.GoogleMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.google.GoogleCampaignService;
import jp.acepro.haishinsan.service.google.GoogleTemplateService;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.ImageUtil;

@Controller
@RequestMapping("/google")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GoogleIssueController {

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	HttpSession session;

	@Autowired
	GoogleCampaignService googleCampaignService;

	@Autowired
	GoogleTemplateService googleTemplateService;

	@Autowired
	DspSegmentService dspSegmentService;

	@Autowired
	OperationService operationService;

	@Autowired
	ImageUtil imageUtil;

	@GetMapping("/adTypeSelect")
	public ModelAndView adTypeSelect(ModelAndView mv) {

		mv.setViewName("campaign/google/typeSelect");
		return mv;
	}

	@GetMapping("/bannerCampaignList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_CAMPAIGN_VIEW + "')")
	public ModelAndView bannerCampaignList(@ModelAttribute GoogleIssueInputForm googleIssueInputForm) {

		List<GoogleCampaignManage> googleCampaignManageList = googleCampaignService.searchGoogleCampaignManageList(GoogleAdType.IMAGE.getValue());

		List<GoogleCampaignDto> googleCampaignDtoList = googleCampaignService.campaignList(googleCampaignManageList);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/google/bannerCampaignList");
		mv.addObject("googleCampaignDtoList", googleCampaignDtoList);

		return mv;
	}

//	@GetMapping("/createCampaign")
//	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_CAMPAIGN_MANAGE + "')")
//	public ModelAndView createCampaign() {
//
//		// コードマスタを読込
//		getGoogleAreaList();
//
//		// テンプレートを読込
//		List<GoogleTemplateDto> googleTemplateDtoList = getGoogleTemplateList();
//
//		// ＤＳＰＵＲＬを読込
//		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();
//		// 入力ＦＯＲＭを初期化
//		GoogleCampaignForm googleCampaignForm = new GoogleCampaignForm();
//		// -------- 優先度一番高いテンプレートで初期値を設定 --------
//		googleCampaignForm = GoogleMapper.INSTANCE.map(googleTemplateDtoList.get(0));
//		// -------- 優先度一番高いテンプレートで初期値を設定 --------
//
//		// 正常時レスポンスを作成
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("google/createCampaign");
//		modelAndView.addObject("googleTemplateDtoList", googleTemplateDtoList);
//		modelAndView.addObject("googleCampaignForm", googleCampaignForm);
//		modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
//		return modelAndView;
//	}
//
//	@PostMapping("/createCampaignComplete")
//	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_CAMPAIGN_MANAGE + "')")
//	public ModelAndView createCampaignComplete(@Validated GoogleCampaignForm googleCampaignForm, BindingResult result) throws IOException {
//
//		// コードマスタを読込
//		getKeywordNameList();
//
//		// 正常時レスポンス表示用オブジェクトを初期化
//		List<String> resAdImageList = new ArrayList<String>();
//		List<String> imageAdImageList = new ArrayList<String>();
//		GoogleCampaignDto googleCampaignDto = new GoogleCampaignDto();
//
//		// キャンペーン作成を実施
//		try {
//			// キャンプーン作成用パラメタ設定（画像以外）
//			googleCampaignDto = GoogleMapper.INSTANCE.map(googleCampaignForm);
//			googleCampaignDto.setResAdImageBytesList(new ArrayList<byte[]>());
//			googleCampaignDto.setImageAdImageBytesList(new ArrayList<byte[]>());
//
//			// キャンプーン作成用パラメタ設定（画像）
//			switch (GoogleAdType.of(googleCampaignForm.getAdType())) {
//			case RESPONSIVE:
//				// 画像枚数チェック
//				if (googleCampaignForm.getResAdImageFileList().size() != 2) {
//					// レスポンシブ広告の画像を同時に２枚アップロードしてください。
//					throw new BusinessException(ErrorCodeConstant.E70005);
//				}
//				List<Integer> widthList = new ArrayList<Integer>();
//				List<Integer> heightList = new ArrayList<Integer>();
//				for (MultipartFile imageFile : googleCampaignForm.getResAdImageFileList()) {
//					String base64Str = imageUtil.getImageBytes(imageFile, 2);
//					StringBuffer data = new StringBuffer();
//					data.append("data:image/jpeg;base64,");
//					data.append(base64Str);
//					resAdImageList.add(data.toString());
//					googleCampaignDto.getResAdImageBytesList().add(getByteArrayFromStream(imageFile.getInputStream()));
//					// ファイルの幅、高さチェック取得
//					InputStream is = imageFile.getInputStream();
//					Image image = ImageIO.read(is);
//					widthList.add(image.getWidth(null));
//					heightList.add(image.getHeight(null));
//				}
//				// 画像サイズチェック
//				if (widthList.get(0).equals(widthList.get(1)) && heightList.get(0).equals(heightList.get(1))) {
//					// レスポンシブ広告の画像は、同時に同じサイズの画像をアップロードしないてください。
//					throw new BusinessException(ErrorCodeConstant.E70006);
//				}
//				break;
//			case IMAGE:
//				for (MultipartFile imageFile : googleCampaignForm.getImageAdImageFileList()) {
//					String base64Str = imageUtil.getImageBytes(imageFile, 3);
//					StringBuffer data = new StringBuffer();
//					data.append("data:image/jpeg;base64,");
//					data.append(base64Str);
//					imageAdImageList.add(data.toString());
//					googleCampaignDto.getImageAdImageBytesList().add(getByteArrayFromStream(imageFile.getInputStream()));
//				}
//				break;
//			case TEXT:
//				break;
//			}
//
//			// キャンプーン作成
//			googleCampaignService.createCampaign(googleCampaignDto, null);
//
//		} catch (BusinessException e) {
//			// 異常時レスポンスを作成
//			result.reject(e.getMessage(), e.getParams(), null);
//
//			// テンプレートを読込
//			List<GoogleTemplateDto> googleTemplateDtoList = getGoogleTemplateList();
//			// ＤＳＰＵＲＬを読込
//			List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();
//			ModelAndView modelAndView = new ModelAndView();
//			modelAndView.setViewName("google/createCampaign");
//			modelAndView.addObject("googleTemplateDtoList", googleTemplateDtoList);
//			modelAndView.addObject("googleCampaignForm", googleCampaignForm);
//			modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
//			return modelAndView;
//		}
//
//		// 正常時レスポンスを作成
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("google/createCampaignComplete");
//		modelAndView.addObject("googleCampaignForm", googleCampaignForm);
//		modelAndView.addObject("resAdImageList", resAdImageList);
//		modelAndView.addObject("imageAdImageList", imageAdImageList);
//
//		// オペレーションログ記録
//		operationService.create(Operation.GOOGLE_CAMPAIGN_CREATE.getValue(), String.valueOf(googleCampaignDto.getCampaignId()));
//		return modelAndView;
//	}
//
//	@GetMapping("/listCampaign")
//	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_CAMPAIGN_VIEW + "')")
//	public ModelAndView listCampaign() {
//
//		// コードマスタをメモリへロード
//		getGoogleAreaList();
//
//		List<GoogleCampaignInfoDto> googleCampaignInfoDtoList = new ArrayList<GoogleCampaignInfoDto>();
//		googleCampaignInfoDtoList = googleCampaignService.getCampaignList();
//
//		// 正常時レスポンスを作成
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("google/listCampaign");
//		modelAndView.addObject("googleCampaignInfoDtoList", googleCampaignInfoDtoList);
//
//		// オペレーションログ記録
//		operationService.create(Operation.GOOGLE_CAMPAIGN_LIST.getValue(), String.valueOf(""));
//		return modelAndView;
//	}
//
//	@GetMapping("/detailCampaign")
//	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_CAMPAIGN_VIEW + "')")
//	public ModelAndView detailCampaign(@RequestParam Long campaignId) {
//
//		// コードマスタをメモリへロード
//		getGoogleAreaList();
//
//		// キャンプーン取得
//		GoogleCampaignDetailDto googleCampaignDetailDto = new GoogleCampaignDetailDto();
//		googleCampaignDetailDto = googleCampaignService.getCampaign(campaignId);
//
//		// 正常時レスポンスを作成
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("google/detailCampaign");
//		modelAndView.addObject("googleCampaignDetailDto", googleCampaignDetailDto);
//
//		// オペレーションログ記録
//		operationService.create(Operation.GOOGLE_CAMPAIGN_VIEW.getValue(), String.valueOf(campaignId));
//		return modelAndView;
//	}
//
//	@PostMapping("/deleteCampaign")
//	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_CAMPAIGN_MANAGE + "')")
//	public ModelAndView deleteCampaign(@RequestParam Long campaignId) {
//
//		// コードマスタをメモリへロード
//		getGoogleAreaList();
//
//		// キャンプーン削除
//		googleCampaignService.deleteCampaign(campaignId);
//
//		// キャンプーン一覧取得
//		List<GoogleCampaignInfoDto> googleCampaignInfoDtoList = new ArrayList<GoogleCampaignInfoDto>();
//		googleCampaignInfoDtoList = googleCampaignService.getCampaignList();
//
//		// 正常時レスポンスを作成
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("google/listCampaign");
//		modelAndView.addObject("googleCampaignInfoDtoList", googleCampaignInfoDtoList);
//
//		// オペレーションログ記録
//		operationService.create(Operation.GOOGLE_CAMPAIGN_DELETE.getValue(), String.valueOf(campaignId));
//		return modelAndView;
//	}

	private void getGoogleAreaList() {
		if (CodeMasterServiceImpl.googleAreaNameList == null) {
			codeMasterService.getGoogleAreaList();
		}
	}

	private List<GoogleTemplateDto> getGoogleTemplateList() {
		List<GoogleTemplateDto> googleTemplateDtoList = googleTemplateService.getTemplateList(ContextUtil.getCurrentShop().getShopId());
		return googleTemplateDtoList;
	}

	private byte[] getByteArrayFromStream(final InputStream inputStream) throws IOException {
		return new ByteSource() {
			@Override
			public InputStream openStream() {
				return inputStream;
			}
		}.read();
	}

	private void getKeywordNameList() {
		if (CodeMasterServiceImpl.keywordNameList == null) {
			codeMasterService.getKeywordNameList();
		}
	}
}
