package jp.acepro.haishinsan.controller.campaign.facebook;

import java.io.IOException;
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
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.facebook.FbIssueDto;
import jp.acepro.haishinsan.dto.facebook.FbTemplateDto;
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
@RequestMapping("/campaign/facebook")
public class FacebookIssueController {

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
		mv.setViewName("campaign/facebook/campaignList");
		mv.addObject("fbCampaignDtoList", fbCampaignDtoList);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_CAMPAIGN_LIST.getValue(), String.valueOf(""));
		return mv;
	}

	@PostMapping("/createIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView createIssue(@Validated FbIssueInputForm fbIssueInputForm, BindingResult result) {

		if (fbIssueInputForm.getIdList() == null || fbIssueInputForm.getIdList().isEmpty()) {
			result.reject("E00020");
			return campaignList(fbIssueInputForm);
		}
		if (fbIssueInputForm.getIdList().size() > 1) {
			result.reject("E00021");
			return campaignList(fbIssueInputForm);
		}

		// テンプレート一覧を取得
		List<FbTemplateDto> fbTemplateDtoList = facebookService.searchList();
		// コードマスタをメモリへロード
		getFacebookAreaList();

		// -------- 優先度一番高いテンプレートで初期値を設定 --------
		if (fbTemplateDtoList != null && fbTemplateDtoList.size() > 0) {
			if (fbTemplateDtoList.get(0).getLocationList() != null) {
				fbIssueInputForm.setLocationList(fbTemplateDtoList.get(0).getLocationList());
			}
			if (fbTemplateDtoList.get(0).getDailyBudget() != null) {
				fbIssueInputForm.setDailyBudget(fbTemplateDtoList.get(0).getDailyBudget());
			}
			// fbIssueInputForm.setTemplateId(fbTemplateDtoList.get(0).getTemplateId());
			// fbIssueInputForm.setUnitPriceType(fbTemplateDtoList.get(0).getUnitPriceType());
		}

		session.setAttribute("campaignId", fbIssueInputForm.getIdList().get(0));
		ModelAndView mv = new ModelAndView();
		mv.setViewName("campaign/facebook/createIssue");
		return mv;

	}

	@PostMapping("/confirmIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView confirmIssue(@Validated FbIssueInputForm fbIssueInputForm, BindingResult result)
			throws IOException {

		ModelAndView mv = new ModelAndView();
		FbIssueDto fbIssueDto = FacebookMapper.INSTANCE.map(fbIssueInputForm);
		String campaignId = (String) session.getAttribute("campaignId");
		fbIssueDto.setCampaignId(campaignId);

        try {
        	facebookService.dailyCheck(fbIssueDto);
        } catch (BusinessException e) {
            // 異常時レスポンス
            result.reject(e.getMessage());
            mv.setViewName("campaign/facebook/createIssue");
            return mv;
        }
		
		session.setAttribute("fbIssueDto", fbIssueDto);

		mv.setViewName("campaign/facebook/confirmIssue");
		mv.addObject("fbIssueDto", fbIssueDto);

		return mv;
	}

	@GetMapping("/completeIssue")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CAMPAIGN_CREATE_NEW + "')")
	public ModelAndView completeIssue() {

		FbIssueDto fbIssueDto = (FbIssueDto) session.getAttribute("fbIssueDto");

		Issue issue = facebookService.createIssue(fbIssueDto);

		session.removeAttribute("fbIssueDto");
		session.removeAttribute("campaignId");
		ModelAndView mv = new ModelAndView("campaign/facebook/completeIssue");
		// mv.addObject("fbIssueDto", fbIssueDto);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_ISSUE_CREATE.getValue(), String.valueOf(issue.getIssueId()));
		return mv;
	}

}
