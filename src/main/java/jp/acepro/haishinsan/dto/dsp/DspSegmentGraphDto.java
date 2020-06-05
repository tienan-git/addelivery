package jp.acepro.haishinsan.dto.dsp;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DspSegmentGraphDto {

	List<String> date = new ArrayList<String>();
	List<String> uunum = new ArrayList<String>();
	List<String> uunumPc = new ArrayList<String>();
	List<String> uunumSp = new ArrayList<String>();

}
