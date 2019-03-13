package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterReportingReq {
	
	String entity;
	String entity_ids;
	String start_time;
	String end_time;
	String granularity;
	String placement;
	String metric_groups;
	String segmentation_type;
	String country;
	
	
	String job_ids;
	
}
