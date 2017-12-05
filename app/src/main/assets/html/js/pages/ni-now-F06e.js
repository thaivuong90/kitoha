/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 4201;

/*******************************************************************************
 * メソッド
 *******************************************************************************/
/* 入力項目の設定 */
( function($){
	$( document ).ready( function(){
		/*******************************************************************
		 * ページ内で発生するイベント
		 *******************************************************************/
		/**
		 * @brief	報告ボタン押下イベント
		 * @note	
		 **/
		$( '#start_button' ).click( function(){
			//Androidの報告処理を呼び出す(処理完了後、自動で次のページをロードする)
		} );
	} );
} );

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
 * ページアンロード時
 **/
window.onunload = function(){
	//リカバリ情報の保存
	save();
}

/*******************************************************************************
 * 本ページのみで有効なメソッド
 *******************************************************************************/
/**
 * ページ情報の設定
 **/
function setup(){
	try {
		//車両情報選択リストを取得
		var car_list	= document.getElementById( "car_list" );
	}catch( e ){
		alert(e.message);
	}
}

/*******************************************************************************
 * 画面遷移時の動作
 *******************************************************************************/
/**
 * 設定値の保存処理
 **/
function save(){
}
