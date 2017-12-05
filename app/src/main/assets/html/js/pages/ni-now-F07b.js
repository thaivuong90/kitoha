/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 5002;

/*******************************************************************************
 * メソッド
 *******************************************************************************/
/* 入力項目の設定 */
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

		var report_time	= RunInfo.getReturnTime();				//帰社時間
		var car_no		= RunInfo.getCarNo();					//車両番号

		//値の更新
		var tag_report_time	= document.getElementById( "report_time" );
		var tag_car_no		= document.getElementById( "car_no" );
		tag_report_time.innerHTML	= report_time;
		tag_car_no.innerHTML		= car_no;
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
	//荷降ろし機能を使用しない場合は、回旋終了画面を表示する
	if( ( AppInfo.getIsUnload() != 1 ) || ( AppInfo.getIsUnload() != "1" ) ){
		next_id	= 7001;
	}
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

/*******************************************************************************
 * デモデータの設定
 *******************************************************************************/
