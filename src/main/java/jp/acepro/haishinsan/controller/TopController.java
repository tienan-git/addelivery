package jp.acepro.haishinsan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.util.ContextUtil;

@Controller
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TopController {

	@Autowired
	OperationService operationService;

	@GetMapping({ "/", "/index" })
	public String index() {
		return "index";
	}

	@GetMapping("/loginSuccess")
	public String loginSuccess() {

		Long userId = ContextUtil.getUserId();
		operationService.create(Operation.LOGIN.getValue(), userId.toString());
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

}