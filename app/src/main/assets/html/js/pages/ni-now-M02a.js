/*******************************************************************************
 * 定数・変数
 *******************************************************************************/

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
	} catch( e ){
		alert( e.message );
	}
}

/**
 * ページ情報の設定
 **/
function setup(){
	try{
		//進捗状況の取得
		StatusInfo.read();
		var progress	= +StatusInfo.getProgress();
		if( ( progress == null )	||
			( progress == 0 ) ){
			StatusInfo.setProgress( 1 );
		}
		set_menu( progress );
	}catch( e ){
		throw e;
	}
}

/**
 * メニュー表示
 * @param	progress(in)	進捗状況
 */
function set_menu( progress ){
	var menu	= document.getElementById( "menu_" + progress );
	menu.setAttribute( "class", "active" );
	menu.setAttribute( "onclick", "onMenuButtonClick(" + progress + ")" );
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
}

/*******************************************************************
 * ページ内で発生するイベント
 *******************************************************************/
/**
 * @brief	ボタン押下イベント
 * @param	progress(in)	進捗状況
 **/
function onMenuButtonClick( progress ){
	screen_id		= StatusInfo.getCurrentScreenId();
	location.href	= Transitions.getScreen( screen_id );
}
