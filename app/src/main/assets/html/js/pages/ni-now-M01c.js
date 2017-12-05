/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 9012;
var old_value	= "";												//変更前の値

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

/*******************************************************************************
 * 本ページのみで有効なメソッド
 *******************************************************************************/
/**
 * ページ情報の設定
 **/
function setup(){
	//設定情報の取得
	ModeInfo.read();
	AppInfo.read();
	RunInfo.read();
	var field		= RunInfo.getSettingField();			//表示情報の種別
	
	//表示情報の設定
	var tag_title	= document.getElementById( "setting_title" );

	switch( +field ){
		case 1:				//ドライバー
			tag_title.innerHTML		= "ドライバー";
			readDrivers();
			break;
		case 2:				//車両
			tag_title.innerHTML		= "車両";
			readCars();
			break;
	}
}

/**
 * @brief	ドライバー情報の受信
 */
function readDrivers(){
	MtbDrivers.read();
	//既に取得しているドライバー情報がある場合は、リスト表示
	list		= MtbDrivers.getList();
	driver_id	= AppInfo.getDriverId();
	setList( list, driver_id, false );
}

/**
 * @brief	車両情報の受信
 */
function readCars(){
	MtbTrucks.read();
	//既に取得している車両情報がある場合は、リスト表示
	list		= MtbTrucks.getList();
	car_id		= AppInfo.getCarId();
	setList( list, car_id, false );
}

/*******************************************************************************
 * 画面遷移時の動作
 *******************************************************************************/
/**
 * 設定値の保存処理
 **/
function save(){
	//データの保存
	LocalStorage.setItem( "is_set", "1" );

	var	input_value		= +$( "#input_list" ).val();
	var	input_text		= $( "#input_list option:selected" ).text();

	//値が変更されていない場合は、マイページに戻る
	if( this.old_value != input_value ){
		//値が変更されている場合は、ダイアログを表示
		swal( {
			title:	'確認',
			text:	"変更した値を保存してもよろしいですか？",
			type:	'warning',
			showCancelButton:	true,
			confirmButtonColor:	'#3085d6',
			cancelButtonColor:	'#d33',
			confirmButtonText:	'OK'
		} ).then( function(){
			setAppInfo();
			showNextScreen();
		}, function( dismiss ){
			if( dismiss === 'cancel' ){
			  swal( '結果', '変更をキャンセルしました。', 'error' );
			  showNextScreen();
			}
		} );
	}else{
		showNextScreen();
	}
}

/**
 * @brief	アプリ設定の保存
 */
function setAppInfo(){
	var	input_value		= +$( "#input_list" ).val();
	var	input_text		= $( "#input_list option:selected" ).text();
	var field			= RunInfo.getSettingField();			//表示情報の種別

	switch( +field ){
		case 1:			//ドライバー
			AppInfo.setDriverId( input_value );
			AppInfo.setDriverName( input_text );
			break;
		case 2:			//車両
			AppInfo.setCarId( input_value );
			AppInfo.setCarNo( input_text );
			break;
	}
	AppInfo.save();
}

/********************************************************************************
* ページ内で発生するイベント
********************************************************************************
/**
 * @brief	受信ボタン押下イベント
 * @note	
 **/
function onReceiveButtonClick(){
	//ボタンの無効化
	setButtonEnabled( "receive_button", 7, false );
	
	var field		= RunInfo.getSettingField();			//表示情報の種別
	switch( +field ){
		case 1:			//ドライバー
			receiveDrivers();
			break;
		case 2:			//車両
			receiveCars();
			break;
	}
}

/**
 * @brief	ドライバー情報の受信
 */
function receiveDrivers(){
	//OSによってデータの取得元を変える
	var agent		= navigator.userAgent;
	if( ( agent.search( /iPhone/ ) != -1 ) ||
		( agent.search( /iPad/ ) != -1 ) ||
		( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
	}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
		//Androidの取得処理を呼び出す
		jv_mainapi.getDriverList();
	}else{
		//リストの取得
		list		= get_driver_list();
		MtbDrivers.setList( list );
		MtbDrivers.save();
		driver_id	= AppInfo.getDriverId();
		setList( list, driver_id, true );
		swal( {
			title:	'結果',
			text:	"ドライバー情報を受信しました。",
			type:	'info',
			showCancelButton:	false,
			confirmButtonColor:	'#3085d6',
			confirmButtonText:	'OK'
		} )
	}
}

/**
 * @brief	車両情報の受信
 */
function receiveCars(){
	//OSによってデータの取得元を変える
	var agent		= navigator.userAgent;
	if( ( agent.search( /iPhone/ ) != -1 ) ||
		( agent.search( /iPad/ ) != -1 ) ||
		( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
	}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
		//Androidの取得処理を呼び出す
		jv_mainapi.getCarList();
	}else{
		list	= get_car_list();
		car_id	= AppInfo.getCarId();
		MtbTrucks.setList( list );
		MtbTrucks.save();
		setList( list, car_id, true );
	}
}

/**
 * リストの設定
 * @param	list(in)		リスト
 * @param	default_id(in)	初期選択のID
 * @param	is_message(in)	メッセージ表示(true=する, false=しない)
 * @note	コールバック関数(Androidから制御を戻す)
 */
function setList( list, default_id, is_message ){
	//設定がない場合は、オプションを設定しない
	if( ( list != "" ) && ( list != "null" ) ){
		var tag_title	= document.getElementById( "setting_title" );
	
		//JSON文字列をJSONに変換
		var json = JSON.parse( list );
		//リストのoptionを追加
		var input_list	= document.getElementById( "input_list" );
		//リストを一度クリアする
		while( input_list.lastChild ){
			input_list.removeChild( input_list.lastChild );
		}
		//リストの再設定
		var option	= document.createElement( 'option' );
		option.setAttribute( 'value', "" );
		option.innerHTML	= "選択してください。";
		input_list.appendChild( option );
		for( var item in json ){
			var option	= document.createElement( 'option' );
			option.setAttribute( 'value', item );
			if( item == default_id ){
				option.setAttribute( 'selected', true );
			}
			option.innerHTML	= json[item];
			input_list.appendChild( option );
		}
		//処理終了
		if( is_message === true ){
			swal( '結果', 'データ受信が完了しました。', 'info' );
		}
	}
	this.old_value	= default_id;

	//ボタンの有効化
	setButtonEnabled( "receive_button", 7, true );
}

/**
 * @brief	戻るボタン押下イベント
 * @note	
 **/
function onBackButtonClick(){
	save();
	save();
}

/**
 * @brief	次の画面表示
 * @note	
 **/
function showNextScreen(){
	var prev_id	= Transitions.getPrevScreenId( PAGE_NO );
	location.href = Transitions.getScreen( prev_id );	
}

/********************************************************************************
 * デモデータの設定
 ********************************************************************************/
/**
 * ドライバーリストの取得
 * @return	ドライバーリスト
 */
function get_driver_list(){
	var driver_list	= '{ '
					+ '"1":"木島 太郎", '
					+ '"2":"鈴木 花子", '
					+ '"3":"田中 三郎", '
					+ '"4":"安藤 次郎", '
					+ '"5":"渡辺 権蔵" '
					+ '}';

	return driver_list;
}

/**
 * 車両リストの取得
 * @return	車両リスト
 */
function get_car_list(){
	var car_list	= '{ '
					+ '"1":"多摩 100 あ 9001", '
					+ '"2":"多摩 100 あ 9002", '
					+ '"3":"多摩 100 あ 9003", '
					+ '"4":"多摩 100 あ 9004", '
					+ '"5":"多摩 100 あ 9005" '
					+ '}';

	return car_list;
}
