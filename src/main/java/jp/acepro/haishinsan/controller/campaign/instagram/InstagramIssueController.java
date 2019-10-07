package jp.acepro.haishinsan.controller.campaign.instagram;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.io.ByteSource;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.dsp.DspSegmentDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.facebook.FbCreativeDto;
import jp.acepro.haishinsan.dto.facebook.FbIssueDto;
import jp.acepro.haishinsan.dto.facebook.FbTemplateDto;
import jp.acepro.haishinsan.enums.MediaType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.FbIssueInputForm;
import jp.acepro.haishinsan.mapper.FacebookMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.ImageUtil;

@Controller
@RequestMapping("/campaign/instagram")
public class InstagramIssueController {

	@Autowired
	HttpSession session;

	@Autowired
	ImageUtil imageUtil;

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	FacebookService facebookService;

	@Autowired
	DspSegmentService dspSegmentService;

	@Autowired
	OperationService operationService;

	@Autowired
	MessageSource msg;

	private void getFacebookAreaList() {
		if (CodeMasterServiceImpl.facebookAreaNameList == null) {
			codeMasterService.getFacebookAreaList();
		}
	}

	@GetMapping("/campaignList")
	public ModelAndView campaignList(@ModelAttribute FbIssueInputForm fbIssueInputForm) {

		List<FacebookCampaignManage> facebookCampaignManageList = facebookService.searchFacebookCampaignManageList();

		List<FbCampaignDto> fbCampaignDtoList = facebookService.campaignList(facebookCampaignManageList);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/instagram/campaignList");
		mv.addObject("fbCampaignDtoList", fbCampaignDtoList);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_CAMPAIGN_LIST.getValue(), String.valueOf(""));
		return mv;
	}

	@GetMapping("/createIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView createIssue(@ModelAttribute FbIssueInputForm fbIssueInputForm) {

//		if (fbIssueInputForm.getIdList() == null || fbIssueInputForm.getIdList().isEmpty()) {
//			result.reject("E00020");
//			return campaignList(fbIssueInputForm);
//		}
//		if (fbIssueInputForm.getIdList().size() > 1) {
//			result.reject("E00021");
//			return campaignList(fbIssueInputForm);
//		}

		// テンプレート一覧を取得
		List<FbTemplateDto> fbTemplateDtoList = facebookService.searchList();
		// コードマスタをメモリへロード
		getFacebookAreaList();

		// 日付によるセグメント情報を取得
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.selectUrlByDateTimeWithNoCheck(LocalDateTime.now());

		// -------- 優先度一番高いテンプレートで初期値を設定 --------
		if (fbTemplateDtoList != null && fbTemplateDtoList.size() > 0) {
			if (fbTemplateDtoList.get(0).getLocationList() != null) {
				fbIssueInputForm.setLocationList(fbTemplateDtoList.get(0).getLocationList());
			}
			if (fbTemplateDtoList.get(0).getDailyBudget() != null) {
				fbIssueInputForm.setDailyBudget(fbTemplateDtoList.get(0).getDailyBudget());
			}
			// fbIssueInputForm.setTemplateId(fbTemplateDtoList.get(0).getTemplateId());
			fbIssueInputForm.setUnitPriceType(fbTemplateDtoList.get(0).getUnitPriceType());
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/instagram/createIssue");
		mv.addObject("dspSegmentDtoList", dspSegmentDtoList);

		session.setAttribute("dspSegmentDtoList", dspSegmentDtoList);
		return mv;

	}

	@PostMapping("/confirmIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView confirmIssue(@Validated FbIssueInputForm fbIssueInputForm, BindingResult result)
			throws IOException {

		String imaBase64 = null;
		FbIssueDto fbIssueDto = FacebookMapper.INSTANCE.map(fbIssueInputForm);

		try {
			imaBase64 = imageUtil.getImageBytes(fbIssueInputForm.getImage(), MediaType.FACEBOOK.getValue());
			fbIssueDto.setImageFileName(fbIssueInputForm.getImage().getOriginalFilename());
			fbIssueDto.setImageBytes(getByteArrayFromStream(fbIssueInputForm.getImage().getInputStream()));
			facebookService.dailyCheck(fbIssueDto);
		} catch (BusinessException e) {
			result.reject(e.getMessage(), e.getParams(), null);
			ModelAndView mv = new ModelAndView("campaign/instagram/createIssue");
			List<DspSegmentListDto> dspSegmentDtoList = (ArrayList<DspSegmentListDto>) session.getAttribute("dspSegmentDtoList");
			mv.addObject("fbIssueInputForm", fbIssueInputForm);
			mv.addObject("dspSegmentDtoList", dspSegmentDtoList);
			return mv;
		}

		DspSegmentDto dspSegmentDto = dspSegmentService.selectBySegmentId(fbIssueInputForm.getSegmentId());

		// FormからDtoまで変更
		ModelAndView mv = new ModelAndView();

		fbIssueDto.setLinkUrl(dspSegmentDto.getUrl());

		session.setAttribute("fbIssueDto", fbIssueDto);

		StringBuffer data = new StringBuffer();
		data.append("data:image/jpeg;base64,");
		data.append(imaBase64);

		mv.setViewName("campaign/instagram/confirmIssue");
		mv.addObject("fbIssueDto", fbIssueDto);
		mv.addObject("base64Data", data.toString());

		return mv;
	}

	@GetMapping("/completeIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView completeIssue() {

		FbIssueDto fbIssueDto = (FbIssueDto) session.getAttribute("fbIssueDto");

		Issue issue = facebookService.createIssue(fbIssueDto);

		session.removeAttribute("fbIssueDto");
		session.removeAttribute("dspSegmentDtoList");
		ModelAndView mv = new ModelAndView("campaign/instagram/completeIssue");

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_ISSUE_CREATE.getValue(), String.valueOf(issue.getIssueId()));
		return mv;
	}

	// Utilities
	private byte[] getByteArrayFromStream(final InputStream inputStream) throws IOException {
		return new ByteSource() {
			@Override
			public InputStream openStream() {
				return inputStream;
			}
		}.read();
	}

}
