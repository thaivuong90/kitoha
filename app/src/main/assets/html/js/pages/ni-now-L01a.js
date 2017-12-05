/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 9101;

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
	//ページ番号の保存
	LocalStorage.setItem( "current_screen", PAGE_NO );

	//未送信リストの取得
	//ユーザーエージェントを解析する(OS別に表示情報の取得方法を変更)
	var agent		= navigator.userAgent;
	if( ( agent.search( /iPhone/ ) != -1 ) ||
		( agent.search( /iPad/ ) != -1 ) ||
		( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
	}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
		//念のため、Androidの設定から取得する
		AppInfo.setUnsendNum( jv_run_settings.getUnsendNum() );
		RunInfo.setUnsendList( jv_run_settings.getUnsendList() );
	}else{													//Windowsの場合
		AppInfo.read();
		RunInfo.setUnsendList( get_unsend_list() );
	}

	//未送信リストの表示
	set_list();
}

/**
 * @brief	未送信リストの表示
 */
function set_list(){
	var tag_unsend_list		= document.getElementById( "unsend_list" );	
	var html				= "";
	var destination_name	= "";
	var destination_address	= "";
	
	//JSON文字列をJSONに変換
	list		= RunInfo.getUnsendList();
	if( list.length > 0 ){
		var json	= JSON.parse( list );
		var len		= json.length;
		//未送信リストの表示
		for( var idx = 0; idx < len; idx++ ){
			//種別によって表示する情報を変える
			destination_name	= "";
			destination_address	= "";
			switch( json[idx]["transport_type"] ){
				case "1":			//引取
					destination_name	= json[idx]["shipper_name"];
					destination_address	= json[idx]["shipper_address"];
					break;
				case "2":			//納品
					destination_name	= json[idx]["consignee_name"];
					destination_address	= json[idx]["consignee_address"];
					break;
			}
			html += "<section>";
			html += "<h2>" + destination_name + "</h2>";
			html += "<p class=\"number\">" + json[idx]["order_no"] + "</p>";
			html += "<p>" + destination_address + "</p>";
			html += "</section>";
		}
	}
	//リスト化したものを表示
	tag_unsend_list.innerHTML	= html;
}

/*******************************************************************************
 * 画面遷移時の動作
 *******************************************************************************/
/**
 * 設定値の保存処理
 **/
function save(){
	//データの保存
	AppInfo.save();

	//ページ番号の保存
	LocalStorage.setItem( "prev_screen", PAGE_NO );
}

/********************************************************************************
* ページ内で発生するイベント
********************************************************************************
/**
 * @brief	戻るボタン押下イベント
 * @note	
 **/
function onSendButtonClick(){
	//ユーザーエージェントを解析する(OS別に表示情報の取得方法を変更)
	var agent		= navigator.userAgent;
	if( ( agent.search( /iPhone/ ) != -1 ) ||
		( agent.search( /iPad/ ) != -1 ) ||
		( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
	}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
		//Androidの報告処理を呼び出す
		jv_mainapi.post_unsend_list();
		//処理終了後にリロードされる
	}else{													//Windowsの場合
		//全部成功した扱いとする
		RunInfo.setUnsendList( "" );
		AppInfo.setUnsendNum( 0 );
		AppInfo.save();
		//再描画
		set_list();
	}
}

/**
 * @brief	戻るボタン押下イベント
 * @note	
 **/
function onBackButtonClick(){
	save();
	var prev_id	= Transitions.getPrevScreenId( PAGE_NO );
	location.href = Transitions.getScreen( prev_id );
}

/********************************************************************************
 * 共通処理
 ********************************************************************************/
/**
* 日付のフォーマット変換
* @return	業務開始時間
*/
function toLocaleString( date ){
	return [
		date.getFullYear(),
		date.getMonth() + 1,
		date.getDate()
	].join( '/' ) + ' '
	+ [
		( "0" + date.getHours() ).slice( -2 ),
		( "0" + date.getMinutes() ).slice( -2 ),
		( "0" + date.getSeconds() ).slice( -2 ),
	].join( ':' );
}

/**
* 時間のフォーマット変換
* @return	業務開始時間
*/
function toLocaleTimeString( date ){
	return [
		( "0" + date.getHours() ).slice( -2 ),
		( "0" + date.getMinutes() ).slice( -2 ),
		( "0" + date.getSeconds() ).slice( -2 ),
	].join( ':' );
}

/********************************************************************************
* デモデータの設定
********************************************************************************/
/**
 * ドライバーリストの取得
 * @return	積み付けリスト
 */
function get_unsend_list(){
	//依頼ID, 伝票番号, 個口番号, 納品・引取区分, 宛先, 住所
	var unsend_list	= '[ '
					+ '{"id":"1","original_id":"1","order_no":"1000200030004001","transport_type":"1","shipper_name":"株式会社KITOHA","shipper_address":"東京都千代田区神田佐久間町","consignee_name":"株式会社プレンティー","consignee_address":"東京都品川区上大崎"},'
					+ '{"id":"2","original_id":"2","order_no":"1000200030004002","transport_type":"1","shipper_name":"株式会社○×商事","shipper_address":"静岡県富士市今泉","consignee_name":"遠藤 次郎","consignee_address":"愛知県名古屋市"},'
					+ '{"id":"3","original_id":"3","order_no":"1000200030004003","transport_type":"2","shipper_name":"株式会社○×自動車","shipper_address":"愛知県豊田市","consignee_name":"株式会社××商事","consignee_address":"埼玉県大宮市"},'
					+ '{"id":"4","original_id":"4","order_no":"1000200030004004","transport_type":"1","shipper_name":"株式会社○×商店","shipper_address":"大阪府大阪市","consignee_name":"鈴木 太郎","consignee_address":"茨城県常総市"},'
					+ '{"id":"5","original_id":"5","order_no":"1000200030004005","transport_type":"1","shipper_name":"株式会社○×飲料","shipper_address":"福岡県福岡市","consignee_name":"佐藤 花子","consignee_address":"東京都八王子市"}'
					+ ']';

	return unsend_list;
}
