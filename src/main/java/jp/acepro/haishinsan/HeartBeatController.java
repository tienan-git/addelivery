package jp.acepro.haishinsan;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartBeatController {

	@RequestMapping("/heartBeat")
	public String heartBeat() {
		return "OK";
	}

}
