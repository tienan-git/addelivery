package jp.acepro.haishinsan.dto.dsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.acepro.haishinsan.dto.UrlRules;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class DspCreateSegmentReq {

	private Integer user_id;

	private String name;

	private String type;

	private Integer frequency;

	private Integer is_enabled;

	private List<UrlRules> url_rules = new ArrayList<UrlRules>(Arrays.asList(new UrlRules()));

	private Integer share_type;

}
