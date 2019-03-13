package jp.acepro.haishinsan.form;

import lombok.Data;

@Data
public class DspTemplateInputForm {

	// テンプレートID
	Long templateId;
	// テンプレート優先順
	Integer templatePriority;
	// テンプレート名
	String templateName;
	// 入札CPC単価
	Integer bidCpcPrice;
	//課金方式
	Integer billingType;
}
