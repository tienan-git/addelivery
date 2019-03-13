package jp.acepro.haishinsan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.acepro.haishinsan.dao.ShopDao;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.dto.ChangeShopRes;
import jp.acepro.haishinsan.util.ContextUtil;

@RestController
@RequestMapping("/topApi/currentShop")
public class TopApiController {

	@Autowired
	ShopDao shopDao;

	@GetMapping("/{shopId}")
	public ChangeShopRes currentShop(@PathVariable Long shopId) {

		Shop shop = shopDao.selectById(shopId);
		ContextUtil.getLoginUser().changeCurrent(shop);
		ChangeShopRes changeShopRes = new ChangeShopRes();
		changeShopRes.setCode("0000");
		changeShopRes.setMessage("店舗情報が更新されました。");
		
		return changeShopRes;

	}

}
