package jp.acepro.haishinsan.dto;

import lombok.Data;

@Data
public class UrlRules {

	Integer id;
	Integer url_match_type;
	String url_match_rule;
	String url_param_match_rule;
	Integer referrer_match_type;
	String referrer_match_rule;
	String referrer_param_match_rule;
}
