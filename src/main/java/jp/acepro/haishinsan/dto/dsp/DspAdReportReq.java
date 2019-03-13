package jp.acepro.haishinsan.dto.dsp;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DspAdReportReq {

	Integer user_id;
	List<Integer> campaign_ids = new ArrayList<>();
	String start_date;
	String end_date;
}
