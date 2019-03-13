package jp.acepro.haishinsan.dto.twitter;

import java.util.ArrayList;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterPromotTweetsRes {
	
	ArrayList<TwitterDataRes> data;

}
