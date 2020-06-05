package jp.acepro.haishinsan.bean;

import com.univocity.parsers.annotations.Parsed;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class YahooCsvBean {

	@Parsed(field = "日")
	private String createDate;

	@Parsed(field = "デバイス")
	private String deviceName;

	@Parsed(field = "都道府県")
	private String regionName;

	@Parsed(field = "市区郡")
	private String cityName;

	@Parsed(field = "行政区")
	private String districtName;

	@Parsed(field = "キャンペーン名")
	private String campaignName;

	@Parsed(field = "キャンペーンタイプ")
	private String campaignType;

	@Parsed(field = "広告掲載方式")
	private String advertisingMethod;

	@Parsed(field = "インプレッション数")
	private String impressionCount;

	@Parsed(field = "クリック数")
	private String clickCount;

	@Parsed(field = "コスト")
	private String cost;

	@Parsed(field = "平均掲載順位")
	private String avePublishRank;

	@Parsed(field = "キャンペーンID")
	private String campaignId;

	@Override
	public String toString() {
		return createDate + "," + deviceName + "," + regionName + "," + cityName + "," + districtName + ","
				+ campaignName + "," + campaignType + "," + advertisingMethod + "," + impressionCount + "," + clickCount
				+ "," + cost + "," + avePublishRank + "," + campaignId;
	}

}
