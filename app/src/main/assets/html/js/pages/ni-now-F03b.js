/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 2102;

/*******************************************************************************
 * メソッド
 *******************************************************************************/
/* 入力項目の設定 */
//( function($){
//	$( document ).ready( function(){
//	} );
//} );

/*******************************************************************************
 * ページロード時
 *******************************************************************************/
/**
 * ページロード直後の処理
 **/
window.onload = function(){
	try {
		//画面に表示する情報のセットアップ
		setup();
	}catch( e ){
		alert( e.message );
	}
}

/**
 * ページ情報の設定
 **/
function setup(){
	try{
		//ページ番号の保存
		StatusInfo.read();
		StatusInfo.setCurrentScreenId( PAGE_NO );		//ページ番号の保存
		StatusInfo.save();

		//必要なデータの読込み
		AppInfo.read();
		RunInfo.read();

		var instruct_no		= RunInfo.getInstructNo();			//配送指示番号
		var report_time		= RunInfo.getReceiveCourseTime();	//取得時間
		var delivery_num	= RunInfo.getDeliveryNum();			//車両番号
		var voucher_num		= RunInfo.getVoucherNum();			//車両番号

		//値の更新
		var tag_instruct_no		= document.getElementById( "instruct_no" );
		var tag_report_time		= document.getElementById( "report_time" );
		var tag_delivery_num	= document.getElementById( "delivery_num" );
		var tag_voucher_num		= document.getElementById( "voucher_num" );
		tag_instruct_no.innerHTML	= instruct_no;
		tag_report_time.innerHTML	= report_time;
		tag_delivery_num.innerHTML	= delivery_num;
		tag_voucher_num.innerHTML	= voucher_num;
	}catch( e ){
		throw e;
	}
}

/*******************************************************************************
 * 画面遷移時の動作
 *******************************************************************************/
/**
 * ページアンロード時
 **/
window.onunload = function(){
	//リカバリ情報の保存
	save();
}

/**
 * 設定値の保存処理
 **/
function save(){
	StatusInfo.setPrevScreenId( PAGE_NO );		//ページ番号の保存
	StatusInfo.save();
}

/*******************************************************************************
 * ページ内で発生するイベント
 *******************************************************************************/
/**
 * @brief	次へボタン押下イベント
 * @note	
 **/
function onNextButtonClick(){
	var next_id	= Transitions.getNextScreenId( PAGE_NO );
	location.href = Transitions.getScreen( next_id );
}

/*******************************************************************************
 * デモデータの設定
 *******************************************************************************/
