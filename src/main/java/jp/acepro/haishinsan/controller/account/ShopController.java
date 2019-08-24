package jp.acepro.haishinsan.controller.account;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.dto.account.CorporationDto;
import jp.acepro.haishinsan.dto.account.ShopDto;
import jp.acepro.haishinsan.dto.account.UserDto;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.ShopInputForm;
import jp.acepro.haishinsan.mapper.ShopMapper;
import jp.acepro.haishinsan.service.account.CorporationService;
import jp.acepro.haishinsan.service.account.ShopService;

@Controller
@RequestMapping("/account/shop")
public class ShopController {

	@Autowired
	HttpSession session;

	@Autowired
	ShopService shopService;

	@Autowired
	CorporationService corporationService;

	@GetMapping("/list")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SHOP_VIEW + "')")
	public ModelAndView list(ModelAndView mv, BindingResult result) {

		List<ShopDto> shopDtoList = shopService.search();

		mv.addObject("shopDtoList", shopDtoList);

		mv.setViewName("shop/list");

		return mv;
	}

	@GetMapping("/create")
	public ModelAndView toCreate(ModelAndView mv, @RequestParam Long corporationId) {

		CorporationDto corporationDto = corporationService.getById(corporationId);

		ShopInputForm shopInputForm = new ShopInputForm();
		shopInputForm.setCorporationId(corporationId);
		shopInputForm.setCorporationName(corporationDto.getCorporationName());
		mv.addObject("shopInputForm", shopInputForm);
		mv.setViewName("shop/create");
		return mv;
	}

	@PostMapping("/createComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SHOP_MANAGE + "')")
	public ModelAndView create(ModelAndView mv, @Validated ShopInputForm shopInputForm, BindingResult result) {

		ShopDto shopDto = ShopMapper.INSTANCE.map(shopInputForm);

		try {
			shopDto = shopService.create(shopDto);
		} catch (BusinessException be) {
			result.reject(be.getMessage(), be.getParams(), null);

			mv.addObject("shopInputForm", shopInputForm);
			mv.setViewName("shop/create");
			return mv;
		}

		mv.setViewName("shop/createComplete");
		mv.addObject("shopDto", shopDto);
		return mv;
	}

	@GetMapping("/detail")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SHOP_VIEW + "')")
	public ModelAndView detail(@Validated ShopInputForm shopInputForm, Long shopId, ModelAndView mv) {

		ShopDto shopDto = shopService.getById(shopId);

		shopInputForm = ShopMapper.INSTANCE.mapToForm(shopDto);

		List<UserDto> userDtoList = shopService.searchUsersByShopId(shopId);
		shopInputForm.setUserDtoList(userDtoList);

		mv.addObject("shopInputForm", shopInputForm);
		mv.setViewName("shop/detail");
		return mv;
	}

	@GetMapping("/update")
	public ModelAndView update(@Validated ShopInputForm shopInputForm, ModelAndView mv, BindingResult result,
			Long shopId) {

		ShopDto shopDto = shopService.getById(shopId);

		shopInputForm = ShopMapper.INSTANCE.mapToForm(shopDto);
		mv.addObject("shopInputForm", shopInputForm);
		mv.setViewName("shop/update");
		return mv;
	}

	@PostMapping("/updateComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SHOP_MANAGE + "')")
	public ModelAndView updateComplete(@Validated ShopInputForm shopInputForm, BindingResult result, ModelAndView mv) {

		ShopDto shopDto = ShopMapper.INSTANCE.map(shopInputForm);

		try {
			shopService.update(shopDto);
		} catch (BusinessException be) {
			result.reject(be.getMessage(), be.getParams(), null);

			mv.addObject("shopInputForm", shopInputForm);
			mv.setViewName("shop/update");
			return mv;
		}

		mv.addObject("shopDto", shopDto);
		mv.setViewName("shop/updateComplete");
		return mv;
	}

	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.SHOP_MANAGE + "')")
	public ModelAndView delete(@Validated ShopInputForm shopInputForm, BindingResult result, ModelAndView mv) {

		try {
			shopService.delete(shopInputForm.getShopId());
		} catch (BusinessException be) {
			result.reject(be.getMessage());
			List<UserDto> userDtoList = shopService.searchUsersByShopId(shopInputForm.getShopId());
			shopInputForm.setUserDtoList(userDtoList);

			mv.addObject("shopInputForm", shopInputForm);
			mv.setViewName("shop/detail");
			return mv;
		}

		mv.addObject("shopInputForm", shopInputForm);
		mv.setViewName("shop/deleteComplete");
		return mv;

	}

}
