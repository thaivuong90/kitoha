/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 6001;

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
		StatusInfo.read();
		StatusInfo.setProgress( 8 );
		StatusInfo.setCurrentScreenId( PAGE_NO );		//ページ番号の保存
		StatusInfo.save();
	
		//ページ番号の保存
		LocalStorage.setItem( "current_screen", PAGE_NO );

		//必要なデータの読出し
		AppInfo.read();
		RunInfo.read();
		
	}catch( e ){
		alert( e.message );
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
 * @brief	報告ボタン押下イベント
 * @note	
 **/
function onReportButtonClick(){
	//ボタンの無効化
	setButtonEnabled( "report_button", 2, false );

	var	car_value	= $( "#car_list" ).val();
	var	car_text	= $( "#car_list option:selected" ).text();

	//送信する値を設定する
	RunInfo.setCarId( car_value );
	RunInfo.setCarNo( car_text );
	RunInfo.setWorkStartTime( toLocaleString( new Date() ) );
	RunInfo.save();

	//ユーザーエージェントを解析する(OS別に表示情報の取得方法を変更)
	var agent		= navigator.userAgent;
	if( ( agent.search( /iPhone/ ) != -1 ) ||
		( agent.search( /iPad/ ) != -1 ) ||
		( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合

		var next_id	= Transitions.getNextScreenId( PAGE_NO );
		location.href = Transitions.getScreen( next_id );
	}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
		//Androidの報告処理を呼び出す(処理完了後、自動で次のページをロードする)
		jv_mainapi.postUnloadingReport();
	}else{													//Windowsの場合
		//画面遷移のみ
		var next_id	= Transitions.getNextScreenId( PAGE_NO );
		location.href = Transitions.getScreen( next_id );
	}
}

/**
 * @brief	戻るボタン押下イベント
 * @note	
 **/
function onBackButtonClick(){
	var prev_id	= Transitions.getPrevScreenId( PAGE_NO );
	location.href = Transitions.getScreen( prev_id );
}

/*******************************************************************************
 * デモデータの設定
 *******************************************************************************/
