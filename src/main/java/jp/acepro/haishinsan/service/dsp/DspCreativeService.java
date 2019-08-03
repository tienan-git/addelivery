package jp.acepro.haishinsan.service.dsp;

import java.util.List;

import jp.acepro.haishinsan.dto.dsp.DspCreativeDto;

public interface DspCreativeService {

	DspCreativeDto createCreative(DspCreativeDto dspCreativeDto);

	List<DspCreativeDto> creativeList();

	List<DspCreativeDto> creativeListFromDb();

	DspCreativeDto creativeDetail(Integer creativeId);

	void creativeDelete(Integer creativeId);

	/**
	 * IDリストによるクリエイティブ情報取得
	 * @param idList
	 * @return
	 */
	List<DspCreativeDto> selectCreativeByIdList(List<Integer> idList);
}
