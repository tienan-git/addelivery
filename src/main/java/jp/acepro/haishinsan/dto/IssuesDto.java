package jp.acepro.haishinsan.dto;

import lombok.Data;

@Data
public class IssuesDto {

    // 案件ID
    Long issueId;
    // 法人名
    String corporationName;
    // 店舗
    String shopName;
    // キャンペーン名
    String campaignName;
    // 配信媒体（アイコン）
    String mediaIcon;
    // 配信媒体
    String media;
    // ステータス（アイコン）
    String status;
    // 費用
    Long budget;
    // 配信開始日
    String startDate;
    // 配信終了日
    String endDate;

}
