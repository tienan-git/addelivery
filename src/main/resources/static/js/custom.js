$(document).ready(function() {
	$('[name=shopSelecter]').on('change', function() {
		obj = document.shopSelecter;
		var shopId = $(this).val();
		var urlStr = "/topApi/currentShop/" + shopId;
		$.ajax({
			url : urlStr, // 自分のURLに変更
			success : function(result) {
				window.location.href = "/"
			}
		});

	});
});
function check(action) {
	if (action == '') {
		action = '確認してよろしいでしょうか？';
	}
	if (window.confirm(action)) { // 確認ダイアログを表示
		return true; // 「OK」時は送信を実行
	} else { // 「キャンセル」時の処理
		return false;// 送信を中止
	}
}
function issueCheck() {
	var dspSelected = $("#dspSelected").prop('checked');
	var googleSelected = $("#googleSelected").prop('checked');
	var facebookSelected = $("#facebookSelected").prop('checked');
	var twitterSelected = $("#twitterSelected").prop('checked');
	if (dspSelected || googleSelected || facebookSelected || twitterSelected) {
		return check('');
	} else {
		alert("一つ以上の媒体を選択してください。");
		return false;
	}
}