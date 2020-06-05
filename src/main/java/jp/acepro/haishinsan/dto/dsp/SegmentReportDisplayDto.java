package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class SegmentReportDisplayDto {

	String date;
	String segmentName;
	Integer segmentId;
	Integer uunum;
	Integer uunumPc;
	Integer uunumSp;

}
