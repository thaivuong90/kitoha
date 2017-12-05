/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 1001;

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
		StatusInfo.setProgress( 1 );
		StatusInfo.setCurrentScreenId( PAGE_NO );		//ページ番号の保存
		StatusInfo.save();

		//必要なデータの読出し
		AppInfo.read();
		RunInfo.read();
		MtbTrucks.read();
		
		//入力情報を取得する
		var car_list	= "";
		var car_id		= "";
		//マイページから車両情報を受信していない場合は、受信させる
		car_id		= AppInfo.getCarId();
		car_list	= MtbTrucks.getList();

		//設定がない場合は、オプションを設定しない
		if( ( car_list != "" ) && ( car_list != null ) && ( car_list != "null" ) ){
			//JSON文字列をJSONに変換
			var json = JSON.parse( car_list );
			//車両情報選択リストのoptionを追加
			var input_car_list	= document.getElementById( "car_list" );
			for( var item in json ){
				var option	= document.createElement( 'option' );
				option.setAttribute( 'value', item );
				if( item == car_id ){
					option.setAttribute( 'selected', true );
				}
				option.innerHTML	= json[item];
				input_car_list.appendChild( option );
			}
		}
	}catch( e ){
		swal( 'エラー', e.message, 'error' );
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
 * @brief	お知らせOK押下イベント
 * @note	
 **/
function onModalOKButtonClick(){
	// #modal-overlay 及び #modal-close をフェードアウトする
	$( "#modal-content, #modal-overlay" ).fadeOut( "slow", function() {
		// フェードアウト後、 #modal-overlay をHTML(DOM)上から削除
		$( "#modal-overlay" ).remove();
	} );
}

/**
 * @brief	報告ボタン押下イベント
 * @note	
 **/
function onReportButtonClick(){
	//ボタンの無効化
	setButtonEnabled( "report_button", 2, false );

	var	car_value	= +$( "#car_list" ).val();
	var	car_text	= $( "#car_list option:selected" ).text();

	//値が変更されている場合は、ダイアログを表示
	if( car_value == 0 ){
		swal( '警告', '車両リストを受信していません。<br />マイページの車両選択画面から車両リストを受信してください。', 'error' );
		return;
	}

	sendReport();
}

function sendReport(){
	var	car_value	= +$( "#car_list" ).val();
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
		jv_mainapi.postWorkStart();
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
