package jp.acepro.haishinsan.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.acepro.haishinsan.service.api.CreativeStatusApiService;

@RestController
@RequestMapping("/api/creativeStatus")
public class CreativeStatusApiController {

	@Autowired
	CreativeStatusApiService creativeStatusApiService;

	@GetMapping("/updateLocalCreativeStatus")
	public void updateLocalCreativeStatus() {
		creativeStatusApiService.executeAsync();
	}

}