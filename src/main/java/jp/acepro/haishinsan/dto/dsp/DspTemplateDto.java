package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class DspTemplateDto {

	// テンプレートID
	Long templateId;
	// テンプレート優先順
	Integer templatePriority;
	// テンプレート名
	String templateName;
	// 入札CPC単価
	Integer bidCpcPrice;
	// 課金方式
	Integer billingType;
}
