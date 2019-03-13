package jp.acepro.haishinsan.service.dsp;

import java.util.List;

import jp.acepro.haishinsan.db.entity.DspToken;
import jp.acepro.haishinsan.dto.dsp.DspAdReportDto;
import jp.acepro.haishinsan.dto.dsp.DspReportingGraphDto;
import jp.acepro.haishinsan.dto.dsp.DspReportingListDto;
import jp.acepro.haishinsan.dto.dsp.DspTemplateDto;

public interface DspApiService {

    DspToken getToken();

    DspTemplateDto createTemplate(DspTemplateDto dspTemplateDto);

    List<DspTemplateDto> templateList();

    DspTemplateDto templateDetail(Long templateId);

    void templateUpdate(DspTemplateDto dspTemplateDto);

    DspTemplateDto templateDelete(Long templateId);

    void getDspCampaignReporting();

    List<DspReportingListDto> getDspReportingList(DspAdReportDto dspAdReportDto);

    DspReportingGraphDto getDspReportingGraph(DspAdReportDto dspAdReportDto);

    DspReportingListDto getDspReportingSummary(DspAdReportDto dspAdReportDto);

    void createDefaultTemplate(long shopId);

    String download(DspAdReportDto dspAdReportDto);

}
