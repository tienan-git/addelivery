package jp.acepro.haishinsan.dto.twitter;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class TwitterSearchCampaignRes {

	List<TwitterCampaignData> data;

}
