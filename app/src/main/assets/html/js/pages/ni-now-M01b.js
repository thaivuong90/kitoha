/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 9011;
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
	AppInfo.read();
	var field		= RunInfo.getSettingField();			//表示情報の種別
	
	//表示情報の設定
	var tag_title	= document.getElementById( "setting_title" );
	var tag_field	= document.getElementById( "setting_value" );

	switch( +field ){
		case 1:			//サーバー名
			tag_title.innerHTML		= "サーバー名";
			tag_field.value			= AppInfo.getServerName();
			break;
		case 2:			//企業ID
			tag_title.innerHTML		= "企業ID";
			tag_field.value			= AppInfo.getCompanyId();
			break;
		case 3:			//ユーザー名
			tag_title.innerHTML		= "ユーザー名";
			tag_field.value			= AppInfo.getUserName();
			break;
		case 4:			//パスワード
			tag_title.innerHTML		= "パスワード";
			tag_field.value			= AppInfo.getPassword();
			break;
	}
	this.old_value	= tag_field.value;
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

	var tag_field	= document.getElementById( "setting_value" );
	
	//値が変更されていない場合は、マイページに戻る
	if( this.old_value != tag_field.value ){
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
	var tag_field	= document.getElementById( "setting_value" );
	var field		= RunInfo.getSettingField();			//表示情報の種別
	switch( +field ){
		case 1:			//サーバー名
			AppInfo.setServerName( tag_field.value );
			break;
		case 2:			//企業ID
			AppInfo.setCompanyId( tag_field.value );
			break;
		case 3:			//ユーザー名
			AppInfo.setUserName( tag_field.value );
			break;
		case 4:			//パスワード
			AppInfo.setPassword( tag_field.value );
			break;
	}
	AppInfo.save();
}

/********************************************************************************
* ページ内で発生するイベント
********************************************************************************
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
