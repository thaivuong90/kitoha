/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 4102;

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
		//ページ番号の保存
		LocalStorage.setItem( "current_screen", PAGE_NO );
		//必要なデータの読込み
		AppInfo.read();
		RunInfo.read();

		//表示設定
		setPictureList();
	}catch( e ){
		throw e;
	}
}

/**
 * @brief	配送情報設定
 */
function setPictureList(){
	var info			= RunInfo.getDetailOrderInfo();		//配送情報
	var info_json		= JSON.parse( info );
	var pictures		= RunInfo.getPictureList();			//配送先の写真リスト
	var picture_json	= "";
	var max_num			= 0;
	
	if( pictures.length > 0 ){
		picture_json	= JSON.parse( pictures );		
		max_num			= Object.keys( picture_json ).length;
	}
	
	//表示する値の設定
	var html			= "";

	for( idx = 0; idx < max_num; idx++ ){
		var path		= "";
		var timestamp	= "";
		path	= picture_json['base_path'];
		if( picture_json['edit_path'] ){
			path	= picture_json['edit_path'];
		}

		html	= '<div class="text-center">'
				+ '<figure>'
				+ '<img src="images/' + path + '" alt="">';
				+ '</figure>'
				+ '<button type="" name="" value=""></button>'
				+ '</div>'
	}

	//タグの取得
	var tag_picture_list		= document.getElementById( "picture_list" );
	tag_picture_list.innerHTML	= html;
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
	//ページ番号の保存
	LocalStorage.setItem( "prev_screen", PAGE_NO );
}

/*******************************************************************************
 * ページ内で発生するイベント
 *******************************************************************************/
/**
 * @brief	削除ボタン押下イベント
 * @param	id(in)		画像ID
 */
function onDeleteButtonClick( id ){
	//画像データの削除
	location.href	= Transitions.getScreen( PAGE_NO );
}

/**
 * @brief	撮影ボタン押下イベント
 */
function onTakePictureButtonClick(){
	//ユーザーエージェントを解析する(OS別に表示情報の取得方法を変更)
	var agent		= navigator.userAgent;
	if( ( agent.search( /iPhone/ ) != -1 ) ||
		( agent.search( /iPad/ ) != -1 ) ||
		( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
		var next_id	= Transitions.getNextScreenId( PAGE_NO );
		location.href = Transitions.getScreen( next_id );
	}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
		//Androidの写真撮影を呼び出す(処理完了後、自動で次のページをロードする)
		jv_mainapi.takePicture();
	}else{													//Windowsの場合
		//画面遷移のみ
		location.href = Transitions.getScreen( PAGE_NO );
	}
}

/**
 * @brief	戻るボタン押下イベント
 * @note	
 **/
function onBackButtonClick(){
	var prev_id		= Transitions.getPrevScreenId( PAGE_NO );
	location.href	= Transitions.getScreen( prev_id );
}

/*******************************************************************************
 * デモデータの設定
 *******************************************************************************/

