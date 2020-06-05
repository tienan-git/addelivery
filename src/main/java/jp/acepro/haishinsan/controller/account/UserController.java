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
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.dto.account.ShopDto;
import jp.acepro.haishinsan.dto.account.UserDto;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.UserInputForm;
import jp.acepro.haishinsan.mapper.UserMapper;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.account.ShopService;
import jp.acepro.haishinsan.service.account.UserService;

@Controller
@RequestMapping("/account/user")
public class UserController {

	@Autowired
	HttpSession session;

	@Autowired
	UserService userService;

	@Autowired
	ShopService shopService;

	@Autowired
	OperationService operationService;

	@GetMapping("/list")
	public ModelAndView list(ModelAndView mv, BindingResult result) {

		List<UserDto> userDtoList = userService.search();

		mv.addObject("userDtoList", userDtoList);
		mv.setViewName("account/user/list");
		return mv;
	}

	@GetMapping("/create")
	public ModelAndView create(@Validated UserInputForm userInputForm, Long shopId, ModelAndView mv) {

		ShopDto shopDto = shopService.getById(shopId);
		userInputForm.setShopId(shopDto.getShopId());
		userInputForm.setShopName(shopDto.getShopName());
		mv.addObject("userInputForm", userInputForm);
		mv.setViewName("account/user/create");
		return mv;
	}

	@PostMapping("/createComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.USER_CREATE + "')")
	public ModelAndView createComplete(@Validated UserInputForm userInputForm, BindingResult result, ModelAndView mv) {

		UserDto userDto = UserMapper.INSTANCE.map(userInputForm);
		try {
			userDto = userService.create(userDto);
		} catch (BusinessException be) {
			result.reject(be.getMessage());
			mv.setViewName("account/user/create");
			mv.addObject("userInputForm", userInputForm);
			return mv;
		}
		userInputForm = UserMapper.INSTANCE.mapToForm(userDto);
		mv.setViewName("account/user/createComplete");
		mv.addObject("userInputForm", userInputForm);

		// オペレーションログ記録
		operationService.create(Operation.USER_CREATE.getValue(), String.valueOf(userInputForm.getUserId()));
		return mv;
	}

	@GetMapping("/detail")
	public ModelAndView detail(Long userId, ModelAndView mv) {

		UserDto userDto = userService.getById(userId);

		mv.addObject("userDto", userDto);
		mv.setViewName("account/user/detail");
		return mv;
	}

	@PostMapping("/update")
	public ModelAndView update(@Validated UserInputForm userInputForm, ModelAndView mv, BindingResult result) {

		mv.addObject("userInputForm", userInputForm);
		mv.setViewName("account/user/update");
		return mv;
	}

	@PostMapping("/updateComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.USER_UPDATE + "')")
	public ModelAndView updateComplete(@Validated UserInputForm userInputForm, BindingResult result, ModelAndView mv) {

		UserDto userDto = UserMapper.INSTANCE.map(userInputForm);
		try {
			userService.update(userDto);
		} catch (BusinessException be) {
			result.reject(be.getMessage());

			mv.setViewName("account/user/update");
			mv.addObject("userInputForm", userInputForm);
			return mv;
		}

		mv.addObject("userInputForm", userInputForm);
		mv.setViewName("account/user/updateComplete");
		return mv;
	}

	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.USER_DELETE + "')")
	public ModelAndView delete(@Validated UserInputForm userInputForm, ModelAndView mv, BindingResult result) {

		userService.delete(userInputForm.getUserId());

		mv.addObject("userInputForm", userInputForm);
		mv.setViewName("account/user/deleteComplete");
		return mv;

	}

}
