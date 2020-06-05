package jp.acepro.haishinsan.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Suppress;
import org.seasar.doma.message.Message;

import jp.acepro.haishinsan.db.annotation.InjectConfig;
import jp.acepro.haishinsan.db.entity.DspReportManage;
import jp.acepro.haishinsan.dto.dsp.DspAdReportDto;

@Dao
@InjectConfig
@Suppress(messages = { Message.DOMA4220 })
public interface DspReportManageCustomDao {

	@Select
	List<DspReportManage> selectByCampaignIds(List<Integer> campaignIds);

	@Select
	List<DspReportManage> selectByDevice(DspAdReportDto dspAdReportDto);

	@Select
	List<DspReportManage> selectByDate(DspAdReportDto dspAdReportDto);

	@Select
	List<DspReportManage> selectByCreative(DspAdReportDto dspAdReportDto);

	@Select
	List<DspReportManage> selectByDeviceForGraph(DspAdReportDto dspAdReportDto);

	@Select
	List<DspReportManage> selectByDateForGraph(DspAdReportDto dspAdReportDto);

	@Select
	List<DspReportManage> selectByCreativeForGraph(DspAdReportDto dspAdReportDto);

	@Select
	List<DspReportManage> selectByCampaignIdsAndDate(List<Integer> campaignIds, String startDate, String endDate);

	@Select
	List<DspReportManage> selectByCampaignId(Integer dspCampaignId, String now);

}
