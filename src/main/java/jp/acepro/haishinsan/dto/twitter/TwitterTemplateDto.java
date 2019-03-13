package jp.acepro.haishinsan.dto.twitter;

import java.util.List;

import lombok.Data;

@Data
public class TwitterTemplateDto {

	Long templateId;
	String 	templateName;
	Integer	templatePriority;
	String 	templateType;
	String 	campaignName;
	String 	startTime;
	String 	endTime;
	Integer dailyBudget;
	Integer totalBudget;
	String 	objective;
	String 	broadKeyword;
	String 	similarToFollowersOfUser;
	String 	location;
	List<String>  regions;
}
