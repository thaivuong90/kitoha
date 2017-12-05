/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 7002;

/*******************************************************************************
 * メソッド
 *******************************************************************************/
/**
 * jQueryのreadyで処理する内容を記載
 */
//( function($){
//	$( document ).ready( function(){
//	} );
//} );

/*******************************************************************************
 * イベント
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

		var delivery_num				= RunInfo.getDeliveryNum();				//配送先件数
		var delivery_comp_num			= RunInfo.getDeliveryCompNum();			//配送先件数(完了)
		var delivery_absence_num		= RunInfo.getDeliveryAbsenceNum();		//配送先件数(不在)
		var delivery_exclude_num		= RunInfo.getDeliveryExcludeNum();		//配送先件数(返品)
		var voucher_num					= RunInfo.getVoucherNum();				//伝票枚数
		var voucher_comp_num			= RunInfo.getVoucherCompNum();			//伝票枚数(完了)
		var voucher_absence_num			= RunInfo.getVoucherAbsenceNum();		//伝票枚数(不在)
		var voucher_exclude_num			= RunInfo.getVoucherExcludeNum();		//伝票枚数(返品)

		//値の更新
		var tag_delivery_num			= document.getElementById( "delivery_num" );
		var tag_delivery_comp_num		= document.getElementById( "delivery_comp_num" );
		var tag_delivery_absence_num	= document.getElementById( "delivery_absence_num" );
		var tag_delivery_exclude_num	= document.getElementById( "delivery_exclude_num" );
		var tag_voucher_num				= document.getElementById( "voucher_num" );
		var tag_voucher_comp_num		= document.getElementById( "voucher_comp_num" );
		var tag_voucher_absence_num		= document.getElementById( "voucher_absence_num" );
		var tag_voucher_exclude_num		= document.getElementById( "voucher_exclude_num" );

		tag_delivery_num.innerHTML			= delivery_num + "件";
		tag_delivery_comp_num.innerHTML		= delivery_comp_num + "件";
		tag_delivery_absence_num.innerHTML	= delivery_absence_num + "件";
		tag_delivery_exclude_num.innerHTML	= delivery_exclude_num + "件";
		tag_voucher_num.innerHTML			= voucher_num + "枚";
		tag_voucher_comp_num.innerHTML		= voucher_comp_num + "枚";
		tag_voucher_absence_num.innerHTML	= voucher_absence_num + "枚";
		tag_voucher_exclude_num.innerHTML	= voucher_exclude_num + "枚";
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
 * 共通処理
 *******************************************************************************/
/**
 * 日付のフォーマット変換
 * @return	日時
 */
function toLocaleString( date ){
	return [
		date.getFullYear(),
		date.getMonth() + 1,
		date.getDate()
	].join( '/' ) + ' '
	+ [
		( "0" + date.getHours() ).slice( -2 ),
		( "0" + date.getMinutes() ).slice( -2 ),
		( "0" + date.getSeconds() ).slice( -2 ),
	].join( ':' );
}

/**
 * 時間のフォーマット変換
 * @return	時間
 */
function toLocaleTimeString( date ){
	return [
		( "0" + date.getHours() ).slice( -2 ),
		( "0" + date.getMinutes() ).slice( -2 ),
		( "0" + date.getSeconds() ).slice( -2 ),
	].join( ':' );
}
