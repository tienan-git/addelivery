package jp.acepro.haishinsan.controller.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.acepro.haishinsan.dto.google.GoogleSwitchRes;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.google.GoogleCampaignService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/google/api")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GoogleApiController {
	
	@Autowired
	GoogleCampaignService googleService;

	@Autowired
	OperationService operationService;
	
    @GetMapping("/{campaignId}/{switchFlag}")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_CAMPAIGN_MANAGE + "')")
    public GoogleSwitchRes switchCampaignStatus(@PathVariable Long campaignId, @PathVariable String switchFlag) {
    	
    	// キャンペーンステータスを更新
    	googleService.updateCampaignStatus(campaignId, switchFlag); 
    	
    	// 正常時レスポンスを作成
    	GoogleSwitchRes res = new GoogleSwitchRes();
    	res.setCode("0000");
    	res.setMessage("キャンペーンのステータスが更新されました！");
		
		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_CAMPAIGN_STATUS_UPDATE.getValue(), String.valueOf(campaignId));
        return res;
    }
}
