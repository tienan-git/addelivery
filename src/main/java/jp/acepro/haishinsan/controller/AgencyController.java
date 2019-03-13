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

import jp.acepro.haishinsan.dto.AgencyDto;
import jp.acepro.haishinsan.dto.CorporationDto;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.AgencyInputForm;
import jp.acepro.haishinsan.mapper.AgencyMapper;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.account.AgencyService;
import jp.acepro.haishinsan.service.account.CorporationService;


@Controller
@RequestMapping("/agency")
public class AgencyController {

	@Autowired
	HttpSession session;

	@Autowired
	AgencyService agencyService;
	
	@Autowired
	CorporationService corporationService;

	@Autowired
	OperationService operationService;

	@GetMapping("/list")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.AGENCY_VIEW + "')")
	public ModelAndView list(ModelAndView mv) {

		List<AgencyDto> agencyDtoList = agencyService.search();
		
		mv.addObject("agencyDtoList", agencyDtoList);
		mv.setViewName("agency/list");
		return mv;
	}

	@GetMapping("/create")
	public ModelAndView create(ModelAndView mv) {

		mv.setViewName("agency/create");
		return mv;
	}

	@PostMapping("/createComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.AGENCY_MANAGE + "')")
	public ModelAndView createComplete(@Validated AgencyInputForm agencyInputForm, BindingResult result, ModelAndView mv) {

		AgencyDto agencyDto = AgencyMapper.INSTANCE.map(agencyInputForm);
		agencyDto = agencyService.create(agencyDto);
		
		agencyInputForm = AgencyMapper.INSTANCE.mapToForm(agencyDto);
		mv.addObject("agencyInputForm", agencyInputForm);
		mv.setViewName("agency/createComplete");

		// オペレーションログ記録
		operationService.create(Operation.USER_CREATE.getValue(), String.valueOf(agencyInputForm.getAgencyId()));
		return mv;
	}

	@GetMapping("/detail")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.AGENCY_VIEW + "')")
	public ModelAndView detail(Long agencyId, ModelAndView mv) {

		AgencyDto agencyDto = agencyService.getById(agencyId);
		
		List<CorporationDto> corporationDtoList = agencyService.searchCorpsByAgencyId(agencyId);
		agencyDto.setCorporationDtoList(corporationDtoList);
		
		AgencyInputForm agencyInputForm = AgencyMapper.INSTANCE.mapToForm(agencyDto);
		mv.addObject("agencyInputForm", agencyInputForm);
		mv.setViewName("agency/detail");
		return mv;
	}

	@GetMapping("/update")
	public ModelAndView update(@Validated AgencyInputForm agencyInputForm, ModelAndView mv,
			BindingResult result, Long agencyId) {

		AgencyDto agencyDto = agencyService.getById(agencyId);
		agencyInputForm = AgencyMapper.INSTANCE.mapToForm(agencyDto);
		mv.addObject("agencyInputForm", agencyInputForm);
		mv.setViewName("agency/update");
		return mv;
	}

	@PostMapping("/updateComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.AGENCY_MANAGE + "')")
	public ModelAndView updateComplete(@Validated AgencyInputForm agencyInputForm, BindingResult result, ModelAndView mv) {

		AgencyDto agencyDto = AgencyMapper.INSTANCE.map(agencyInputForm);
		agencyService.update(agencyDto);

		
		mv.addObject("agencyInputForm", agencyInputForm);
		mv.setViewName("agency/updateComplete");
		return mv;
	}

	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.AGENCY_MANAGE + "')")
	public ModelAndView delete(@Validated AgencyInputForm agencyInputForm,
			BindingResult result, ModelAndView mv) {
		
		try {
			agencyService.delete(agencyInputForm.getAgencyId());
		} catch (BusinessException be) {
			result.reject(be.getMessage());
			mv.setViewName("agency/detail");
			List<CorporationDto> corporationDtoList = agencyService.searchCorpsByAgencyId(agencyInputForm.getAgencyId());
			agencyInputForm.setCorporationDtoList(corporationDtoList);
			
			mv.addObject("agencyInputForm", agencyInputForm);
//			session.setAttribute("agencyDto", agencyDto);
			return mv;
		}
		
		mv.addObject("agencyInputForm", agencyInputForm);
		mv.setViewName("agency/deleteComplete");
		return mv;

	}



}
