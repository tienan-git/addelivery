package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterBatchCampaign {

    // Create, Delete, Update
    String operation_type;
    TwitterBatchCampaignParams params;

}
