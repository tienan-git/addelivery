package jp.acepro.haishinsan.form;

import java.util.List;

import lombok.Data;

@Data
public class DspSegmentSearchForm {

	List<Integer> segmentIdList;
	String startDate;
	String endDate;
}
