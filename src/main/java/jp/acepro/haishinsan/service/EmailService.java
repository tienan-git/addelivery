package jp.acepro.haishinsan.service;

import jp.acepro.haishinsan.dto.EmailDto;

public interface EmailService {

	void sendEmail(EmailDto emailDto);

}
