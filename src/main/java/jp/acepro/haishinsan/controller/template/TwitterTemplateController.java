package jp.acepro.haishinsan.controller.template;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import jp.acepro.haishinsan.dto.twitter.TwitterTemplateDto;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.TwitterLocationType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.TwitterTemplateInputForm;
import jp.acepro.haishinsan.mapper.TwitterMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.twitter.TwitterTemplateService;

@Controller
@RequestMapping("/template/twitter")
public class TwitterTemplateController {

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	HttpSession session;

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	TwitterTemplateService twitterTemplateService;

	@Autowired
	OperationService operationService;

	// テンプレート作成
	@GetMapping("/createTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TEMPLATE_CREATE + "')")
	public ModelAndView templateaCreate(@ModelAttribute TwitterTemplateInputForm twittertemplateInputForm) {

		// コードマスタをメモリへロード
		searchRegions();

		ModelAndView modelAndView = new ModelAndView();

		// -------- 地域初期値設定 --------
		twittertemplateInputForm.setLocation(TwitterLocationType.ALLCITY.getValue());
		// レスポンス
		modelAndView.setViewName("template/twitter/createTemplate");

		return modelAndView;
	}

	// テンプレート作成完了
	@PostMapping("/completeTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TEMPLATE_CREATE + "')")
	public ModelAndView templateComplete(@Validated TwitterTemplateInputForm twitterTemplateInputForm,
			BindingResult result) {

		TwitterTemplateDto twitterTemplateDto = TwitterMapper.INSTANCE.tempFormToDto(twitterTemplateInputForm);
		ModelAndView modelAndView = new ModelAndView();
		try {
			twitterTemplateService.createTemplate(twitterTemplateDto);
		} catch (BusinessException e) {
			// 異常時レスポンス
			result.reject(e.getMessage());
			modelAndView.setViewName("template/twitter/createTemplate");
			modelAndView.addObject("twitterTemplateDto", twitterTemplateDto);
			modelAndView.addObject("twitterTemplateInputForm", twitterTemplateInputForm);
			return modelAndView;
		}
		// 正常時レスポンス
		modelAndView.setViewName("template/twitter/completeTemplate");
		modelAndView.addObject("twitterTemplateDto", twitterTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.TWITTER_TEMPLATE_CREATE.getValue(),
				String.valueOf(twitterTemplateDto.getTemplateId()));

		return modelAndView;
	}

	// テンプレートリスト
	@GetMapping("/templateList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TEMPLATE_LIST + "')")
	public ModelAndView templateList() {

		// コードマスタをメモリへロード
		searchRegions();

		ModelAndView modelAndView = new ModelAndView();
		List<TwitterTemplateDto> twitterTemplateDtoList = twitterTemplateService.templateList();
		modelAndView.setViewName("template/twitter/templateList");
		modelAndView.addObject("twitterTemplateDtoList", twitterTemplateDtoList);

		// オペレーションログ記録
		operationService.create(Operation.TWITTER_TEMPLATE_LIST.getValue(), String.valueOf(""));
		return modelAndView;
	}

	// テンプレート詳細
	@GetMapping("/templateDetail")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TEMPLATE_LIST + "')")
	public ModelAndView templateDetail(@RequestParam Long templateId) {

		ModelAndView modelAndView = new ModelAndView();
		TwitterTemplateDto twitterTemplateDto = twitterTemplateService.templateDetail(templateId);
		modelAndView.setViewName("template/twitter/templateDetail");
		modelAndView.addObject("twitterTemplateDto", twitterTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.TWITTER_TEMPLATE_VIEW.getValue(), String.valueOf(templateId));
		return modelAndView;
	}

	// テンプレート更新
	@PostMapping("/updateTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TEMPLATE_CREATE + "')")
	public ModelAndView templateUpdate(@RequestParam Long templateId) {

		TwitterTemplateDto twitterTemplateDto = twitterTemplateService.templateDetail(templateId);
		TwitterTemplateInputForm twitterTemplateInputForm = TwitterMapper.INSTANCE.tempDtoToForm(twitterTemplateDto);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("template/twitter/updateTemplate");
		modelAndView.addObject("twitterTemplateInputForm", twitterTemplateInputForm);
		return modelAndView;
	}

	// テンプレート更新完了
	@PostMapping("/updateTemplateComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TEMPLATE_CREATE + "')")
	public ModelAndView templateUpdateComplete(@Validated TwitterTemplateInputForm twitterTemplateInputForm,
			BindingResult result) {

		TwitterTemplateDto twitterTemplateDto = TwitterMapper.INSTANCE.tempFormToDto(twitterTemplateInputForm);
		ModelAndView modelAndView = new ModelAndView();
		try {
			twitterTemplateService.templateUpdate(twitterTemplateDto);
		} catch (BusinessException e) {
			// 異常時レスポンス
			result.reject(e.getMessage());
			modelAndView.setViewName("template/twitter/updateTemplate");
			modelAndView.addObject("twitterTemplateInputForm", twitterTemplateInputForm);
			return modelAndView;
		}
		// 正常時レスポンス
		modelAndView.setViewName("template/twitter/updateTemplateComplete");
		modelAndView.addObject("twitterTemplateDto", twitterTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.TWITTER_TEMPLATE_UPDATE.getValue(),
				String.valueOf(twitterTemplateInputForm.getTemplateId()));
		return modelAndView;
	}

	// テンプレート削除
	@PostMapping("/deleteTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TEMPLATE_CREATE + "')")
	public ModelAndView templateDelete(@RequestParam Long templateId) {

		TwitterTemplateDto twitterTemplateDto = twitterTemplateService.templateDelete(templateId);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("template/twitter/deleteTemplate");
		modelAndView.addObject("twitterTemplateDto", twitterTemplateDto);
		// オペレーションログ記録
		operationService.create(Operation.TWITTER_TEMPLATE_UPDATE.getValue(), String.valueOf(templateId));
		return modelAndView;

	}

	// DBからすべての都道府県を検索
	private void searchRegions() {
		if (CodeMasterServiceImpl.twitterRegionList == null) {
			codeMasterService.searchRegions();
		}
	}

}
