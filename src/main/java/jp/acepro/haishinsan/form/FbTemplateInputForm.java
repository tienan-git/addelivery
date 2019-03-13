package jp.acepro.haishinsan.form;

import java.util.List;

import lombok.Data;

@Data
public class FbTemplateInputForm {

	Long templateId;
	String templateName;
	Integer templatePriority;
	String unitPriceType;
	Long dailyBudget;
	
	// 地域
	List<Long> locationList;
	
	

}
