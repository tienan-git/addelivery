package jp.acepro.haishinsan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.acepro.haishinsan.dto.twitter.TwitterSwitchRes;
import jp.acepro.haishinsan.service.twitter.TwitterApiService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/twitter/api")
public class TwitterApiController {
	
	@Autowired
	TwitterApiService twitterApiService;
	
    @GetMapping("/{campaignId}/{switchFlag}")
    public TwitterSwitchRes switchCampaignStatus(@PathVariable String campaignId, @PathVariable String switchFlag) {
    	
    	// キャンペーンステータスを更新
    	twitterApiService.changeAdsStatus(campaignId,switchFlag);
    	
    	// 正常時レスポンスを作成
    	TwitterSwitchRes res = new TwitterSwitchRes();
    	res.setCode("0000");
    	res.setMessage("キャンペーンのステータスが更新されました！");
        return res;
    }
    
    
}
