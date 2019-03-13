package jp.acepro.haishinsan.dto.facebook;

import java.util.List;

import lombok.Data;

@Data
public class FbTemplateDto {

	Long templateId;
	String templateName;
	Long shopId;
	Integer templatePriority;
	String unitPriceType;
	Long dailyBudget;
	String geolocation;

	// 地域
	List<Long> locationList;
	
//	public boolean getGeolocationLabel() {
//		StringBuilder stringBuilder = new StringBuilder();
//		
//		for (Long location : locationList) {
//			CodeMasterServiceImpl.facebookAreaNameList.
//			stringBuilder.append(location.toString());
//			stringBuilder.append(",");
//		}
//		if (stringBuilder.length() > 0) {
//			stringBuilder.substring(0, stringBuilder.length() - 1);
//		}
//		
//		//CodeMasterServiceImpl.facebookAreaNameList.
//		
//		//<option th:each="object : ${T(jp.acepro.haishinsan.service.CodeMasterServiceImpl).facebookAreaNameList}" th:selected="${#lists.contains(fbTemplateDto.locationList, object.getFirst())}" th:value="${object.getFirst()}" th:text="${object.getSecond()}">日本</option>
//		
//	}
	

}
