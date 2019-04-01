$(function() {
	$('#basicTable').DataTable({
		// 件数切替機能 無効
		'iDisplayLength' : 50,
		scrollY: 500,
        scrollX: true,
        scrollCollapse: true,
        paging: false,
        autoFill: true,
		lengthChange : false,
		'language' : {
			"sEmptyTable": "テーブルにデータがありません",
			"sInfo":           " _TOTAL_ 件中 _START_ から _END_ まで表示",
			"sInfoEmpty":      " 0 件中 0 から 0 まで表示",
			"sSearch" : "検索",
			"paginate" : {
				"sNext" : "次",
				"sPrevious" : "前"
			},
			
		},
	});
});

$(function() {
	$('#basicTable1').DataTable({
		// 件数切替機能 無効
		'iDisplayLength' : 50,
		scrollY: 500,
        scrollX: true,
        scrollCollapse: true,
        paging: false,
        autoFill: true,
		lengthChange : false,
		searching:false,
		'language' : {
			"sEmptyTable": "テーブルにデータがありません",
			"sInfo":           " _TOTAL_ 件中 _START_ から _END_ まで表示",
			"sInfoEmpty":      " 0 件中 0 から 0 まで表示",
			"sSearch" : "検索",
			"paginate" : {
				"sNext" : "次",
				"sPrevious" : "前"
			},
			
		},
	});
});