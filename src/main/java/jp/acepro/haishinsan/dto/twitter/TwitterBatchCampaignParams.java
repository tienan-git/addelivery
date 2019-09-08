package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterBatchCampaignParams {

    // funding_instrument_id
    String funding_instrument_id;

    // 日予算
    String daily_budget_amount_local_micro;

    //
    String campaign_id;

}
