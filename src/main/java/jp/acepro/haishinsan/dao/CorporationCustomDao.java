package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.entity.CorporationWithAgency;


@Dao
@InjectConfig
public interface CorporationCustomDao {


    @Select
//    List<Corporation> selectAll();
    List<CorporationWithAgency> selectAll();
    
    @Select
    List<Shop> selectShopsByCorpId(Long corporationId);
    
    @Select
    CorporationWithAgency selectById(Long corporationId);

}
