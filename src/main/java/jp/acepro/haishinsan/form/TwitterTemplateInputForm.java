package jp.acepro.haishinsan.form;

import java.util.List;

import lombok.Data;

@Data
public class TwitterTemplateInputForm {
	
	Long templateId;
	String 	templateName;
	Integer	templatePriority;
	String 	templateType;
	String 	campaignName;
	String 	startTime;
	String 	endTime;
	Integer dailyBudget;
	Integer totalBudget;
	Integer objective;
	String 	broadKeyword;
	String 	similarToFollowersOfUser;
	Integer location;
	List<String> regions;

}
