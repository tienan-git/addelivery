package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.entity.ShopWithAgency;
import jp.acepro.haishinsan.entity.ShopWithCorporation;
import jp.acepro.haishinsan.entity.UserWithAgency;

@Dao
@InjectConfig
public interface ShopCustomDao {

	@Select
	// List<Shop> selectAll();
	List<ShopWithAgency> selectAll();

	@Select
	List<UserWithAgency> selectUsersByShopId(Long shopId);

	@Select
	ShopWithCorporation selectById(Long shopId);

	@Select
	List<Shop> selectAllShop();

	@Select
	List<Shop> selectByCorporationId(Long corporationId);

	@Select
	List<Shop> selectAgencyShops(Long shopId);

	@Select
	List<Shop> selectCorporationShops(Long shopId);

	@Select
	List<Shop> selectByIssueId(Long issueId);

}
