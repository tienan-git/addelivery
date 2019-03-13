package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.acepro.haishinsan.bean.YahooCsvBean;
import lombok.Data;

@Data
public class YahooCsvInputForm {

	String fileName;
	MultipartFile csvFile;
	List<YahooCsvBean> yahooCsvBeanList;
}
