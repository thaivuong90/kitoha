/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 3001;

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
 * ページロード時
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
 * ページ情報の設
 **/
function setup(){
	RunInfo.read();
	StatusInfo.read();
	StatusInfo.setProgress( 5 );
	StatusInfo.setCurrentScreenId( PAGE_NO );		//ページ番号の保存
	StatusInfo.save();
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

/*******************************************************************
 * ページ内で発生するイベント
 *******************************************************************/
/**
 * @brief	報告ボタン押下イベント
 * @note	
 **/
function onReportButtonClick(){
	//ボタンの無効化
	setButtonEnabled( "report_button", 2, false );
	
	//送信する値を設定する
	RunInfo.setStartingReportTime( toLocaleString( new Date() ) );
	//配送のステータスを変更する
	changeStatus();
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
		jv_mainapi.postStartingReport();
	}else{													//Windowsの場合
		//画面遷移のみ
		var next_id	= Transitions.getNextScreenId( PAGE_NO );
		location.href = Transitions.getScreen( next_id );
	}
}

function changeStatus(){
	try{
		//必要なデータの読込み
		RunInfo.read();

		var delivery_list	= RunInfo.getDeliveryList();		//配送リスト
		var json			= JSON.parse( delivery_list );
		var max_num			= Object.keys( json ).length;

		//配送リストの取得
		for( idx = 0; idx < max_num; idx++ ){
			if( +json[idx]['status'] < 90 ){
				switch( +json[idx]['transport_type'] ){
					case 1:				//引取
						json[idx]['status']	= 30;
						break;
					case 2:				//納品
					default:			//その他
						json[idx]['status']	= 40;
						break;
				}	
			}
		}

		//配送リストの更新
		RunInfo.setDeliveryList( JSON.stringify( json ) );
		RunInfo.save();
	}catch( e ){
		throw e;
	}
}

/*******************************************************************************
 * デモデータの設定
 *******************************************************************************/
