package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.Agency;
import jp.acepro.haishinsan.db.entity.Corporation;


@Dao
@InjectConfig
public interface AgencyCustomDao {


    @Select
    List<Agency> selectAll();

    @Select
    List<Corporation> selectCorpsByAgencyId(Long agencyId);
}
