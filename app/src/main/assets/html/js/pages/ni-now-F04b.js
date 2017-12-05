/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 2202;

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
	var MAX_LENGTH	= 20;
	try{
		//ページ番号の保存
		StatusInfo.read();
		StatusInfo.setCurrentScreenId( PAGE_NO );		//ページ番号の保存
		StatusInfo.save();

		//必要なデータの読込み
		AppInfo.read();
		RunInfo.read();

		var delivery_list	= RunInfo.getDeliveryList();	//積込リスト
		if( delivery_list !== "" ){
			var json			= JSON.parse( delivery_list );
			var max_num			= Object.keys( json ).length;
			var all_list		= "";							//全件表示
			var uncomp_list		= "";							//未処理表示
			var is_disp			= false;
	
			//積込リストの表示
			for( idx = 0; idx < max_num; idx++ ){
				is_disp		= false;							//未処理表示フラグ
				//納品物のみ表示
				if( +json[idx]['transport_type'] == 2 ){
					//ステータスを数字変換して比較
					var section_class	= "";
					var status			= +json[idx]['status'];
					var order_no		= "";
					if( ( status >= 1 ) && ( status < 20 ) ){			//未積込
						is_disp			= true;
					}else if( ( status >= 20 ) && ( status < 90 ) ){	//積込済
						section_class	= "complete";
						is_disp			= false;
					}else{												//キャンセル
						section_class	= "exception";
						is_disp			= false;
					}
					//伝票番号
					if( +json[idx]['line_no'] == 1 ){
						order_no	= json[idx]['order_no'];
					}else{
						order_no	= json[idx]['line_tracking_no'];
					}	
					//宛先を表示文字数に詰める
					var name_buf	= "";
					name_buf		= json[idx]['consignee_name'] + " "
									+ json[idx]['consignee_section_name'] + " "
									+ json[idx]['consignee_contact'];
					var name		= name_buf.substr( 0, MAX_LENGTH );
					//時間指定等の条件アイコンを表示
					var condition	= '<ul class="cargo_info">';
					if( +json[idx]['delivery_time_order'] > 1 ){
						if( status > 90 ){
							condition	+= '<li><img src="images/ico_clock_exception.png"></li>';
						}else{
							condition	+= '<li><img src="images/ico_clock.png"></li>';
						}
					}else{
						condition	+= '<li><img src="images/ico_clock_exception.png"></li>';
					}
					if( +json[idx]['req_weight'] >= 40 ){
						if( status > 90 ){
							condition	+= '<li><img src="images/ico_kg_exception.png"></li>';
						}else{
							condition	+= '<li><img src="images/ico_kg.png"></li>';
						}
					}else{
						condition	+= '<li><img src="images/ico_kg_exception.png"></li>';
					}
					condition	+= '</ul>';
					//全件リスト表示
					all_list	+= '<section class="' + section_class + '">';
					all_list	+= '	<div class="inner_contents" onclick="onListClick( ' + json[idx]['id'] + ' );">';
					all_list	+= '		<h2>' + order_no + '</h2>';
					all_list	+= '		<p>' + name + '</p>';
					all_list	+= '		<p>' + json[idx]['line_product_name1'] + '</p>';
					all_list	+= condition;
					all_list	+= '	</div>';
					all_list	+= '</section>'
	
					//未処理リスト表示
					if( is_disp == true ){
						uncomp_list	+= '<section>';
						uncomp_list	+= '	<div class="inner_contents" onclick="onListClick( ' + json[idx]['id'] + ' );">';
						uncomp_list	+= '		<h2>' + order_no + '</h2>';
						uncomp_list	+= '		<p>' + name + '</p>';
						uncomp_list	+= '		<p>' + json[idx]['line_product_name1'] + '</p>';
						uncomp_list	+= condition;
						uncomp_list	+= '	</div>';
						uncomp_list	+= '</section>'
					}
				}
			}
			//積込予定件数と処理件数が一致している場合は、積付完了ボタン押下可能
			if( uncomp_list == "" ){
				setButtonEnabled( "next_button", 3, true );
			}else{
				setButtonEnabled( "next_button", 3, false );
			}
		}

		//値の更新
		var tag_all_list			= document.getElementById( "all_list" );
		var tag_uncomp_list			= document.getElementById( "uncomp_list" );
		tag_all_list.innerHTML		= all_list;
		tag_uncomp_list.innerHTML	= uncomp_list;
	}catch( e ){
		throw e;
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
 * @brief	伝票番号入力
 */
function onEntryClick(){
	var tag_input_voucher_no	= document.getElementById( "input_voucher_no" );		//入力
	voucher_no					= tag_input_voucher_no.value;

	var delivery_list	= RunInfo.getDeliveryList();				//積込リスト
	var json			= JSON.parse( delivery_list );
	var max_num			= Object.keys( json ).length;
	var match_type		= 0;
	var idx				= 0;

	//積込リストから該当する伝票番号を検索する
	for( idx = 0; idx < max_num; idx++ ){
		if( json[idx]['order_no'] == voucher_no ){				//管理番号
			match_type		= 1;
			break;			
		}else if( json[idx]['voucher_no'] == voucher_no ){		//共用送り状番号
			match_type		= 2;
			break;
		}else if( json[idx]['sub_voucher_no'] == voucher_no ){	//運送送り状番号
			match_type		= 3;			
			break;
		}if( json[idx]['line_tracking_no'] == voucher_no ){		//個口伝票番号
			match_type		= 4;
			break;
		}
	}
	//一致したデータがある場合
	if( match_type != 0 ){
		var order_id	= json[idx]['order_id'];
		json[idx]['status']			= 20;									//積込済
		json[idx]['loaded_time']	= toLocaleString( new Date() );			//積込時刻
		RunInfo.setDeliveryList( JSON.stringify( json ) );
	}

	RunInfo.setLoadingLastTime( toLocaleString( new Date() ) );
	RunInfo.save();

	//OSによって処理が変わる
	var agent		= navigator.userAgent;
	if( ( agent.search( /iPhone/ ) != -1 ) ||
		( agent.search( /iPad/ ) != -1 ) ||
		( agent.search( /iPod/ ) != -1 ) ){						//iOSの場合
	}else if( agent.search( /Android/ ) != -1 ){				//Androidの場合
		jv_mainapi.postLoadingReport( order_id, 20, 0, "" );
	}else{
		location.href	= Transitions.getScreen( PAGE_NO );
	}
}

/**
 * @brief	リスト押下イベント
 * @param	id(in)		配送依頼ID
 * @note	
 **/
 function onListClick( id ){
	var delivery_list	= RunInfo.getDeliveryList();		//積込リスト
	var json			= JSON.parse( delivery_list );
	var max_num			= Object.keys( json ).length
	var info			= "";

	//積込リストの表示
	for( idx = 0; idx < max_num; idx++ ){
		//IDが一致する情報を検索
		if( json[idx]['id'] == id ){
			//一致している場合は、このIDが該当するデータ
			info = json[idx];
			break;
		}
	}
	//表示したい配送依頼情報を設定
	RunInfo.setDetailOrderInfo( JSON.stringify( info ) );
	RunInfo.save();
	location.href	= Transitions.getScreen( 2211 );
}

/**
 * @brief	次へボタン押下イベント
 * @note	
 **/
function onNextButtonClick(){
	var next_id	= Transitions.getNextScreenId( PAGE_NO );
	location.href = Transitions.getScreen( next_id );
}

/**
 * @brief	モーダルOKボタン押下イベント
 * @note	
 **/
function onModalOKButtonClick(){
	//データ削除
	RunInfo.setDeliveryList( "" );
	RunInfo.save();

	//OSによってデータの保存先を変える
	var agent		= navigator.userAgent;
	if( ( agent.search( /iPhone/ ) != -1 ) ||
		( agent.search( /iPad/ ) != -1 ) ||
		( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
	}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
		//DBの更新
		jv_mainapi.clearDtbOrders();
	}else{
	}

	//戻り先の設定(配送指示受信 or 伝票読取り)
	var prev_id	= 2111;
	if( AppInfo.getIsEntry() == "1" ){
		prev_id	= 2101;
	}
	location.href = Transitions.getScreen( prev_id );
}

/**
 * @brief	モーダルOKボタン押下イベント
 * @note	
 **/
function onModalCancelButtonClick(){
	// #modal-overlay 及び #modal-close をフェードアウトする
	$( "#modal-content, #modal-overlay" ).fadeOut( "slow", function() {
		// フェードアウト後、 #modal-overlay をHTML(DOM)上から削除
		$( "#modal-overlay" ).remove();
	} );
}
	
/*******************************************************************************
 * デモデータの設定
 *******************************************************************************/
