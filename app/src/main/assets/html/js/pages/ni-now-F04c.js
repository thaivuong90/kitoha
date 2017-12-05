/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 2211;

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

		var info	= RunInfo.getDetailOrderInfo();			//配送情報
		var json	= JSON.parse( info );

		//値の更新
		var tag_order_id				= document.getElementById( "order_id" );
		var tag_order_no				= document.getElementById( "order_no" );
		var tag_flat_no					= document.getElementById( "flat_no" );
		var tag_product_name			= document.getElementById( "product_name" );
		var tag_status					= document.getElementById( "status" );
		var tag_transmission_order_no	= document.getElementById( "transmission_order_no" );
		var tag_name					= document.getElementById( "name" );
		var tag_phone					= document.getElementById( "phone" );
		var tag_post_code				= document.getElementById( "post_code" );
		var tag_address					= document.getElementById( "address" );
		var tag_time_order				= document.getElementById( "time_order" );
		var tag_weight					= document.getElementById( "weight" );
		var tag_memo					= document.getElementById( "memo" );

		//表示する値の設定
		var flat_no						= "";
		var name						= "";
		var phone						= "";
		var post_code					= "";
		var address						= "";
		var time_order					= "";
		var memo						= "";
		var loading_check				= "";

		if( +json['line_no'] > 1 ){
			flat_no			= json['line_tracking_no'];
		}
		switch( +json['transport_type'] ){
			case 1:									//引取
				name		= json['shipper_name'] + " " + json['shipper_section_name'] + " " + json['shipper_contact'];
				phone		= json['shipper_phone'];
				post_code	= json['shipper_post_code'];
				address		= json['shipper_address'];
				switch( +json['pickup_time_order'] ){
					case 2:			//時間指定
						time_order	= json['pickup_time_from'] + "～" + json['pickup_time_to'];
					case 3:			//範囲指定
						if( +json['pickup_time_range'] == 1 ){
							time_order	= "AM";
						}else{
							time_order	= "PM";
						}
					case 1:			//指定しない
					default:		//指定しないに設定
						time_order	= "―";
						break;
				}
				memo		= json['pickup_memo'];
				break;
			case 2:									//納品
				name		= json['consignee_name'] + " " + json['consignee_section_name'] + " " + json['consignee_contact'];
				phone		= json['consignee_phone'];
				post_code	= json['consignee_post_code'];
				address		= json['consignee_address'];
				switch( +json['delivery_time_order'] ){
					case 2:			//時間指定
						time_order	= json['delivery_time_from'] + "～" + json['delivery_time_to'];
						break;
					case 3:			//範囲指定
						if( +json['delivery_time_range'] == 1 ){
							time_order	= "AM";
						}else{
							time_order	= "PM";
						}
						break;
					case 1:			//指定しない
					default:		//指定しないに設定
						time_order	= "―";
						break;
				}
				memo		= json['delivery_memo'];
				break;
			default:
				//基本的に引取／納品以外はおかしい
				break;
		}
		if( (+json['status'] >= 20 ) && ( +json['status'] < 90 ) ){
			loading_check	= '<i class="fa fa-check" aria-hidden="true"></i>';
		}else if( +json['status'] >= 90 ){
			loading_check	= '<i class="fa fa-close" aria-hidden="true"></i>';
		}
		tag_order_id.value					= json['_id'];
		tag_order_no.innerHTML				= json['order_no'];
		tag_flat_no.innerHTML				= flat_no;
		tag_product_name.innerHTML			= json['line_product_name1'];
		tag_status.innerHTML				= loading_check;
		tag_transmission_order_no.innerHTML	= json['transmission_order_no'];
		tag_name.innerHTML					= name;
		tag_phone.innerHTML					= phone;
		tag_post_code.innerHTML				= post_code;
		tag_address.innerHTML				= address;
		tag_time_order.innerHTML			= time_order;
		tag_weight.innerHTML				= json['req_weight'];
		tag_memo.innerHTML					= memo;
		$( "#input_reason" ).val( json['cancel_loading_reason'] );
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
	//ページ番号の保存
	LocalStorage.setItem( "prev_screen", PAGE_NO );
}

/*******************************************************************************
 * ページ内で発生するイベント
 *******************************************************************************/
 /**
 * @brief	除外理由設定ボタン押下イベント
 * @note	
 **/
function onExcludeResonButtonClick(){
	var order_id		= document.getElementById( "order_id" ).value;
	var	cancel_reason	= $( "#input_reason" ).val();					//除外理由の取得
	var	reason_text		= $( "#input_reason option:selected" ).text();

	var delivery_list	= RunInfo.getDeliveryList();					//配送リスト
	var json			= JSON.parse( delivery_list );
	var max_num			= Object.keys( json ).length;
	var idx				= 0;
	//積込リストの表示
	for( idx = 0; idx < max_num; idx++ ){
		if( order_id == json[idx]['_id'] ){
			break;
		}
	}

	//取得した情報の当該
	if( cancel_reason == "" ){
		if( json[idx]['loaded_time'] ){
			json[idx]['status']						= 20;					//積込済
			json[idx]['cancel_loading_reason']		= null;
			json[idx]['cancel_loading_reason_text']	= "";
		}else{
			json[idx]['status']						= 11;					//配車確認済
			json[idx]['cancel_loading_reason']		= null;
			json[idx]['cancel_loading_reason_text']	= "";
		}
	}else{
		json[idx]['status']							= 90;					//事前キャンセル
		json[idx]['cancel_loading_reason']			= cancel_reason;
		json[idx]['cancel_loading_reason_text']		= reason_text;
	}
	var after_list	= JSON.stringify( json );
	var after_info	= JSON.stringify( json[idx] );

	//入力された伝票番号から該当する情報を検索する
	RunInfo.setLoadingList( after_list );
	RunInfo.setDetailOrderInfo( after_info );
	RunInfo.save();
	
	//OSによってデータの保存先を変える
	var agent		= navigator.userAgent;
	if( ( agent.search( /iPhone/ ) != -1 ) ||
		( agent.search( /iPad/ ) != -1 ) ||
		( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
	}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
		//DBの更新
		jv_mainapi.update_dtb_orders( order_id );
	}else{
		location.href	= Transitions.getScreen( PAGE_NO );
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
