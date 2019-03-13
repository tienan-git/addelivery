package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class Segment {

	Integer segment_id;
	Integer retention_window;
	Integer segment_freq_min;
	Integer wait_interval;
	Integer segment_freq_max;
}
