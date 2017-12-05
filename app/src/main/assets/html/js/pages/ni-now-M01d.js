/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 9013;
var old_news	= false;
var old_entry	= false;
var old_photo	= false;
var old_sign	= false;
var old_cert	= false;
var old_return	= false;
var old_unload	= false;

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
	
	//表示情報の設定
	var toggle_news		= document.getElementById( "toggle_news" );
	var toggle_entry	= document.getElementById( "toggle_entry" );
	var toggle_photo	= document.getElementById( "toggle_photo" );
	var toggle_sign		= document.getElementById( "toggle_sign" );
	var toggle_cert		= document.getElementById( "toggle_cert" );
	var toggle_return	= document.getElementById( "toggle_return" );
	var toggle_unload	= document.getElementById( "toggle_unload" );
	
	if( +AppInfo.getIsNews() == 1 ){
		toggle_news.checked		= AppInfo.getIsNews();
		this.old_news			= AppInfo.getIsNews();
	}
	if( +AppInfo.getIsEntry() == 1 ){
		toggle_entry.checked	= AppInfo.getIsEntry();
		this.old_entry			= AppInfo.getIsEntry();
	}
	if( +AppInfo.getIsPhoto() == 1 ){
		toggle_photo.checked	= AppInfo.getIsPhoto();
		this.old_photo			= AppInfo.getIsPhoto();
	}
	if( +AppInfo.getIsSign() == 1 ){
		toggle_sign.checked		= AppInfo.getIsSign();
		this.old_sign			= AppInfo.getIsSign();
	}
	if( +AppInfo.getIsCert() == 1 ){
		toggle_cert.checked		= AppInfo.getIsCert();
		this.old_cert			= AppInfo.getIsCert();
	}
	if( +AppInfo.getIsReturn() == 1 ){
		toggle_return.checked	= AppInfo.getIsReturn();
		this.old_return			= AppInfo.getIsReturn();
	}
	if( +AppInfo.getIsUnload() == 1 ){
		toggle_unload.checked	= AppInfo.getIsUnload();
		this.old_unload			= AppInfo.getIsUnload();
	}
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

	//値が変更されていない場合は、マイページに戻る
	var toggle_news		= document.getElementById( "toggle_news" );
	var toggle_entry	= document.getElementById( "toggle_entry" );
	var toggle_photo	= document.getElementById( "toggle_photo" );
	var toggle_sign		= document.getElementById( "toggle_sign" );
	var toggle_cert		= document.getElementById( "toggle_cert" );
	var toggle_return	= document.getElementById( "toggle_return" );
	var toggle_unload	= document.getElementById( "toggle_unload" );

	if( ( this.old_news		!= toggle_news.checked ) ||
		( this.old_entry	!= toggle_entry.checked ) ||
		( this.old_photo	!= toggle_photo.checked ) ||
		( this.old_sign		!= toggle_sign.checked ) ||
		( this.old_cert		!= toggle_cert.checked ) ||
		( this.old_return	!= toggle_return.checked ) ||
		( this.old_unload	!= toggle_unload.checked ) ){

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
 * 
 */
function setAppInfo(){
	var toggle_news		= document.getElementById( "toggle_news" );
	var toggle_entry	= document.getElementById( "toggle_entry" );
	var toggle_photo	= document.getElementById( "toggle_photo" );
	var toggle_sign		= document.getElementById( "toggle_sign" );
	var toggle_cert		= document.getElementById( "toggle_cert" );
	var toggle_return	= document.getElementById( "toggle_return" );
	var toggle_unload	= document.getElementById( "toggle_unload" );

	var is_news_val	= 0;
	if( toggle_news.checked ){
		is_news_val	= 1;
	}
	AppInfo.setIsNews( is_news_val );
	var is_entry_val	= 0;
	if( toggle_entry.checked ){
		is_entry_val	= 1;
	}
	AppInfo.setIsEntry( is_entry_val );
	var is_photo_val	= 0;
	if( toggle_photo.checked ){
		is_photo_val	= 1;
	}
	AppInfo.setIsPhoto( is_photo_val );
	var is_sign_val	= 0;
	if( toggle_sign.checked ){
		is_sign_val	= 1;
	}
	AppInfo.setIsSign( is_sign_val );
	var is_cert_val	= 0;
	if( toggle_cert.checked ){
		is_cert_val	= 1;
	}
	AppInfo.setIsCert( is_cert_val );
	var is_return_val	= 0;
	if( toggle_return.checked ){
		is_return_val	= 1;
	}
	AppInfo.setIsReturn( is_return_val );
	var is_unload_val	= 0;
	if( toggle_unload.checked ){
		is_unload_val	= 1;
	}
	AppInfo.setIsUnload( is_unload_val );
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
