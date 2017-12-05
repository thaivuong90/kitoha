/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO	= 9001;

/*******************************************************************************
 * メソッド
 *******************************************************************************/
/* 入力項目の設定 */
//( function($){
//	$( document ).ready( function(){
//	} );
//} );

/*******************************************************************************
 * ページロード時の動作
 *******************************************************************************/
/**
 * ページロード直後の処理
 **/
window.onload = function(){
//	try {
		//画面に表示する情報のセットアップ
		setup();
//	}catch( e ){
//		alert( e.message );
//	}
}

/**
 * ページ情報の設定
 **/
function setup(){
	LocalStorage.check();

	var is_set		= LocalStorage.getItem( "is_set" );

	AppInfo.read();
	RunInfo.read();
	ModeInfo.read();

	//値が設定されていない場合は、デモデータを取得する
	if( ModeInfo.getDemoMode() == 1 ){
		//アプリ設定
		AppInfo.setServerName( get_server_name() );
		AppInfo.setCompanyId( get_company_id() );
		AppInfo.setUserName( get_user_id() );
		AppInfo.setPassword( get_password() );
		AppInfo.setCarId( get_car_id() );
		AppInfo.setCarNo( get_car_no() );
		AppInfo.setDriverId( get_driver_id() );
		AppInfo.setDriverName( get_driver_name() );
		AppInfo.save();
		//実行情報
		RunInfo.setUnsendNum( get_unsend_num() );
		RunInfo.setNews( get_news() );
		RunInfo.save();
	}

	//お知らせ情報
	if( +AppInfo.getIsNews() == 1 ){
		setNews();
	}else{
		setFooter();
	}

	//表示情報の設定
	var tag_server_name		= document.getElementById( "server_name" );
	var tag_company_id		= document.getElementById( "company_id" );
	var tag_user_id			= document.getElementById( "user_name" );
	var tag_password		= document.getElementById( "password" );
	var tag_car_no			= document.getElementById( "car_no" );
	var tag_driver_name		= document.getElementById( "driver_name" );
	var tag_unsend_button	= document.getElementById( "unsend_button" );
	var tag_unsend_num		= document.getElementById( "unsend_num" );
	//パスワードを"*"に変換するため、文字数を取得する
	var password			= AppInfo.getPassword();
	var pass_len			= 0;
	if( password ){
		pass_len			= password.length;
	}
	tag_server_name.innerHTML	= AppInfo.getServerName();
	tag_company_id.innerHTML	= AppInfo.getCompanyId();
	tag_user_id.innerHTML		= AppInfo.getUserName();
	tag_password.innerHTML		= Array( pass_len + 1 ).join( "*" );
	tag_driver_name.innerHTML	= AppInfo.getDriverName();
	tag_car_no.innerHTML		= AppInfo.getCarNo();
	//未送信件数が0の場合は、ボタン押下不可にする
	if( RunInfo.getUnsendNum() == 0 ){
		tag_unsend_num.remove();
		tag_unsend_button.removeAttribute( "onclick" );
		tag_unsend_button.setAttribute( "class", "btn_disable" );
	}else{
		tag_unsend_num.innerHTML	= RunInfo.getUnsendNum();
	}

	set_ls_clear();
}

/**
 * フッターの設定
 */
function setFooter(){
	var tag_footer	= document.getElementById( "footer" );
	var html		= "";

	html	= '<div class="common_btn btn_sent inline">'
			+ '<div id="unsend_button" onclick="onUnsendButtonClick();">'
			+ '未送信一覧'
			+ '<div id="unsend_num"></div>'
			+ '</div>'
			+ '</div>';

	tag_footer.innerHTML	= html;
}

/**
 * お知らせの設定
 */
function setNews(){
	var tag_modal_contents	= document.getElementById( "modal_contents" );
	var html				= "";
	var list				= RunInfo.getNews();

	//JSON文字列をJSONに変換
	if( ( list != "" ) && ( list != "null" ) ){
		var json	= JSON.parse( list );
		var max_idx	= 3;			//表示するお知らせ件数
		var idx		= 0;			//処理中のお知らせ件数
		for( var item in json ){
			html += "<p>" + item + "<br />" + json[item] + "</p>";
			idx++;
			if( idx >= max_idx ){
				break;
			}
		}
	}else{
		html = "現在、お知らせはありません。";
	}
	tag_modal_contents.innerHTML	= html;
}

/**
 * LocalStorageのクリアボタンを設定する
 */
function set_ls_clear(){
	var tag_ls_clear	= document.getElementById( "ls_clear" );
	var agent			= navigator.userAgent;
	if( ( agent.search( /iPhone/ ) != -1 ) ||
		( agent.search( /iPad/ ) != -1 ) ||
		( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
	}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
	}else{
		tag_ls_clear.innerHTML	= '<input type="button" value="LSクリア" onclick="LocalStorage.clear();" />';
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

/********************************************************************************
* ページ内で発生するイベント
********************************************************************************
/**
 * @brief	テキスト項目押下イベント
 * @note	
 **/
function onTextFieldClick( field ){
	save();
	RunInfo.setSettingField( field );
	location.href = "./ni-now-M01b.html";
}

/**
 * @brief	コンボボックス項目押下イベント
 * @note	
 **/
function onComboFieldClick( field ){
	save();
	RunInfo.setSettingField( field );
	location.href = "./ni-now-M01c.html";
}

/**
 * @brief	トグル項目押下イベント
 * @note	
 **/
function onToggleFieldClick(){
	save();
	location.href = "./ni-now-M01d.html";
}

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
 * @brief	未送信一覧押下イベント
 * @note	
 **/
function onUnsendButtonClick(){
	location.href = "./ni-now-L01a.html";
}

/**
 * @brief	戻るボタン押下イベント
 * @note	
 **/
function onBackButtonClick(){
	var prev_id	= Transitions.getPrevScreenId( PAGE_NO );
	location.href = Transitions.getScreen( prev_id );
}

/********************************************************************************
* デモデータの設定
********************************************************************************/
/**
 * 接続サーバー名の取得
 * @return	接続サーバー名
 */
function get_server_name(){
	return "http://api.ninow.jp/";
}
	
/**
 * 企業IDの取得
 * @return	企業ID
 */
function get_company_id(){
	return "Admin";
}
	
/**
 * ユーザーIDの取得
 * @return	ユーザーID
 */
function get_user_id(){
	return "admin";
}
	
/**
 * パスワードの取得
 * @return	パスワード
 */
function get_password(){
	return "Kitadm01";
}
	
/**
 * ドライバーIDの取得
 * @return	ドライバーID
 */
function get_driver_id(){
	return 1;
}

/**
 * ドライバー名の取得
 * @return	ドライバー名
 */
function get_driver_name(){
	return "鈴木 太郎";
}

/**
 * 車両IDの取得
 * @return	車両ID
 */
function get_car_id(){
	return 1;
}

/**
 * 車両番号の取得
 * @return	車両番号
 */
function get_car_no(){
	return "多摩 100 あ 1234";
}

/**
 * 未送信件数の取得
 * @return	未送信件数
 */
function get_unsend_num(){
	return 1;
}

/**
 * モデルの取得
 * @return	モデル
 */
function get_model(){
	return 1;
}

/**
 * お知らせの取得
 * @return	お知らせ
 */
function get_news(){
	var news_list	= '{ '
					+ '"2017.10.06":"東京マラソンのため、通行禁止区域があります。<br />時間帯に気を付けて配送してください。", '
					+ '"2017.10.02":"台風が近づいていますので、気を付けて配送してください。", '
					+ '"2017.09.01":"出発前確認、安全運転でおねがいします。",'
					+ '"2017.08.01":"荷役中の事故に気を付けてください。"'
					+ '}';

	return news_list;
}
