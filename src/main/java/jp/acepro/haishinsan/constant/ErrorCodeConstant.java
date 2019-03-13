package jp.acepro.haishinsan.constant;

/**
 * エラーコード定数を定義する。
 *
 * @author lmzh17
 *
 */
public class ErrorCodeConstant {

	/************* 共通エラーコード *************/
	// {0}を入力してください。
	public static final String E00001 = "E00001";
	// ファイルが選択されてない、または0バイトファイルです。
	public static final String E00002 = "E00002";
	// ファイルサイズの最大値150KBを増えました。
	public static final String E00003 = "E00003";
	// ファイルはJPEG、PNG、GIFいずれかのタイプにする必要があります。
	public static final String E00004 = "E00004";
	// ファイルはDimensionが間違えました。
	public static final String E00005 = "E00005";
	// GIFの毎秒フレーム数を増えました。
	public static final String E00006 = "E00006";
	// GIFのプレイ時間が長すぎです。
	public static final String E00007 = "E00007";
	// メールの発送失敗しました。
	public static final String E00008 = "E00008";
	// ファイルタイプが.CSVではありません。
	public static final String E00009 = "E00009";
	// 配信期間の開始日と終了日を確認してください。
	public static final String E00010 = "E00010";
	// 配信地域を選択してください。
	public static final String E00011 = "E00011";
	// セグメントのURLが存在しない。
	public static final String E00012 = "E00012";
	// Googleのテンプレートが存在しない。
	public static final String E00013 = "E00013";
	// Facebookのテンプレートが存在しない。
	public static final String E00014 = "E00014";
	// Twitterのテンプレートが存在しない。
	public static final String E00015 = "E00015";
	// DSPのテンプレートが存在しない。
	public static final String E00016 = "E00016";
	// サポートしないエンコードです。
	public static final String E00017 = "E00017";
	// 該当テンプレート名が既に登録されたため、修正してください。
	public static final String E00018 = "E00018";
	// 該当テンプレート優先度が既に登録されたため、修正してください。
	public static final String E00019 = "E00019";

	/************* アカウント関連エラーコード *************/
	// 所属法人が存在するため、代理店削除出来ません。
	public static final String E10001 = "E10001";
	// 所属店舗が存在するため、法人削除出来ません。
	public static final String E10002 = "E10002";
	// 所属ユーザーが存在するため、店舗削除出来ません。
	public static final String E10003 = "E10003";
	// 入力したメールアドレスが不正です。
	public static final String E10004 = "E10004";
	// 店舗更新できないので、該当店舗へ遷移してください。
	public static final String E10005 = "E10005";
	
	/************* Facebookエラーコード *************/
	// テンプレート作成が失敗しました。
	public static final String E40001 = "E40001";
	// 配信地域を選択してください。
	public static final String E40002 = "E40002";
	// 無効なFacebookトークン情報です。
	public static final String E40003 = "E40003";
	// ファイルはDimensionが間違えました。(facebook)
	public static final String E40004 = "E40004";

	/************* ユーザーエラーコード *************/
	// メールアドレスが存在しています。
	public static final String E50011 = "E50011";
	// ユーザーが存在しません。
	public static final String E50012 = "E50012";

	/************* Twitterエラーコード *************/
	// テンプレート名が存在しました。
	public static final String E20001 = "E20001";

	// キャンペーン優先順が存在しました。
	public static final String E20002 = "E20002";

	// 終了時刻は開始時刻より後である必要があります。
	public static final String E20003 = "E20003";

	// 日別予算が総予算を超えることはできません。
	public static final String E20004 = "E20004";

	// ツイートを設定してください。
	public static final String E20005 = "E20005";

	// 配信都道府県を選択してください。
	public static final String E20006 = "E20006";

	// 日別予算は、選択した地域の平均単価（{0}）より、多くなってください。
	public static final String E20007 = "E20007";

	/************* DSPエラーコード *************/
	// テンプレート名が存在しました。
	public static final String E30001 = "E30001";

	// テンプレート優先順が存在しました。
	public static final String E30002 = "E30002";

	// キャンペーンの配信終了日は配信開始日より後の日付を入力してください。
	public static final String E30003 = "E30003";

	// 正確な予算を入力してください。
	public static final String E30004 = "E30004";

	// クリエイティブを選択してください。
	public static final String E30005 = "E30005";

	// レポート取得期間を３ヶ月以内に指定してください。
	public static final String E30006 = "E30006";

	/************* Yahooエラーコード *************/
	// 配信地域を選択してください。。
	public static final String E60001 = "E60001";

	// メール送信失敗しました。
	public static final String E60002 = "E60002";

	// 配信地域を選択してください。
	public static final String E60003 = "E60003";

	// 入力最大値を過ぎました。
	public static final String E60004 = "E60004";

	/************* Googleエラーコード *************/
	// 該当キャンペーン名が既に登録されたため、修正してください。
	public static final String E70001 = "E70001";

	// 配信期間の開始日と終了日を確認してください。
	public static final String E70002 = "E70002";

	// 配信地域を選択してください。
	public static final String E70003 = "E70003";

	// 該当テンプレート名が既に登録されたため、修正してください。
	public static final String E70004 = "E70004";

	// レスポンシブ広告の画像を同時に２枚アップロードしてください。
	public static final String E70005 = "E70005";

	// レスポンシブ広告の画像は、同時に同じサイズの画像をアップロードしないてください。
	public static final String E70006 = "E70006";

	// 該当テンプレート優先度が既に登録されたため、修正してください。
	public static final String E70007 = "E70007";

	// Google広告の画像ポリシーを確認してください。
	public static final String E70008 = "E70008";
}
