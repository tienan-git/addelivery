package jp.acepro.haishinsan.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dto.AgencyDto;
import jp.acepro.haishinsan.dto.CorporationDto;
import jp.acepro.haishinsan.dto.ShopDto;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.CorporationInputForm;
import jp.acepro.haishinsan.mapper.CorporationMapper;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.account.AgencyService;
import jp.acepro.haishinsan.service.account.CorporationService;

@Controller
@RequestMapping("/corporation")
public class CorporationController {

	@Autowired
	HttpSession session;

	@Autowired
	CorporationService corporationService;

	@Autowired
	OperationService operationService;
	
	@Autowired
	AgencyService agencyService;

	@GetMapping("/list")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CORPORATION_VIEW + "')")
	public ModelAndView list(ModelAndView mv, BindingResult result) {

		List<CorporationDto> corporationDtoList = corporationService.search();

		mv.addObject("corporationDtoList", corporationDtoList);
		mv.setViewName("corporation/list");
		return mv;
	}

	@GetMapping("/create")
	public ModelAndView create(@Validated CorporationInputForm corporationInputForm, ModelAndView mv, Long agencyId) {

		AgencyDto agencyDto = agencyService.getById(agencyId);
		corporationInputForm.setAgencyId(agencyId);
		corporationInputForm.setAgencyName(agencyDto.getAgencyName());
		mv.addObject("corporationInputForm", corporationInputForm);
		mv.setViewName("corporation/create");
		return mv;
	}

	@PostMapping("/createComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CORPORATION_MANAGE + "')")
	public ModelAndView createComplete(@Validated CorporationInputForm corporationInputForm, BindingResult result,
			ModelAndView mv) {

		CorporationDto corporationDto = CorporationMapper.INSTANCE.map(corporationInputForm);
		try {
			corporationDto = corporationService.create(corporationDto);
		} catch (BusinessException be) {
			result.reject(ErrorCodeConstant.E10002);
			mv.setViewName("corporation/create");
			mv.addObject("corporationInputForm", corporationInputForm);
			return mv;
		}

		mv.setViewName("corporation/createComplete");
		mv.addObject("corporationInputForm", corporationInputForm);

		// オペレーションログ記録
		operationService.create(Operation.USER_CREATE.getValue(), String.valueOf(corporationInputForm.getCorporationId()));
		return mv;
	}

	@GetMapping("/detail")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CORPORATION_VIEW + "')")
	public ModelAndView detail(Long corporationId, ModelAndView mv) {

		CorporationDto corporationDto = corporationService.getById(corporationId);

		List<ShopDto> shopDtoList = corporationService.searchShopsByCorpId(corporationId);
		corporationDto.setShopDtoList(shopDtoList);

		CorporationInputForm corporationInputForm = CorporationMapper.INSTANCE.mapToForm(corporationDto);
		mv.addObject("corporationInputForm", corporationInputForm);
		mv.setViewName("corporation/detail");
		return mv;
	}

	@GetMapping("/update")
	public ModelAndView update(@Validated CorporationInputForm corporationInputForm, Long corporationId,
			ModelAndView mv, BindingResult result) {

		CorporationDto corporationDto = corporationService.getById(corporationId);
		corporationInputForm = CorporationMapper.INSTANCE.mapToForm(corporationDto);
		mv.addObject("corporationInputForm", corporationInputForm);
		mv.setViewName("corporation/update");
		return mv;
	}

	@PostMapping("/updateComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CORPORATION_MANAGE + "')")
	public ModelAndView updateComplete(@Validated CorporationInputForm corporationInputForm, BindingResult result,
			ModelAndView mv) {

		CorporationDto corporationDto = CorporationMapper.INSTANCE.map(corporationInputForm);
		corporationService.update(corporationDto);

		mv.addObject("corporationInputForm", corporationInputForm);
		mv.setViewName("corporation/updateComplete");
		return mv;
	}

	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.CORPORATION_MANAGE + "')")
	public ModelAndView delete(@Validated CorporationInputForm corporationInputForm, BindingResult result,
			ModelAndView mv) {

		try {
			corporationService.delete(corporationInputForm.getCorporationId());
		} catch (BusinessException be) {
			result.reject(be.getMessage());

			List<ShopDto> shopDtoList = corporationService.searchShopsByCorpId(corporationInputForm.getCorporationId());
			corporationInputForm.setShopDtoList(shopDtoList);
			mv.addObject("corporationInputForm", corporationInputForm);
			mv.setViewName("corporation/detail");
			return mv;
		}

		mv.addObject("corporationInputForm", corporationInputForm);
		mv.setViewName("corporation/deleteComplete");
		return mv;

	}

}
