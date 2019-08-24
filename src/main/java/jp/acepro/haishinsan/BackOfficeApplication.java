package jp.acepro.haishinsan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("jp.acepro.haishinsan")
public class BackOfficeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackOfficeApplication.class, args);
	}
}
