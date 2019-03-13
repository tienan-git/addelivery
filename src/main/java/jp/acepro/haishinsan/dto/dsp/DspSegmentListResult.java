package jp.acepro.haishinsan.dto.dsp;

import java.util.List;

import jp.acepro.haishinsan.dto.UrlRules;
import lombok.Data;

@Data
public class DspSegmentListResult {

	Integer share_type;
	List<Integer> required_ids;
	List<Integer> forbidden_ids;
	Integer frequency;
	Integer im_segment_id;
	Integer priority;
	Integer is_enabled;
	Integer is_ip_targeting;
	Integer id;
	Integer is_ref;
	List<UrlRules> url_rules;
	String name;
	String type;
}
