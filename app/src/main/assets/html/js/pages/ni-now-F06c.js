/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 4101;

var name		= "";
var address		= "";

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
		setDeliveryInfo();				//配送情報設定
		setItemList();					//荷物一覧
	}catch( e ){
		throw e;
	}
}

/**
 * @brief	配送情報設定
 */
function setDeliveryInfo(){
	var info	= RunInfo.getDetailOrderInfo();			//配送情報
	var json	= JSON.parse( info );

	//表示する値の設定
	var transmission_order_no		= "";
	var phone						= "";
	var post_code					= "";
	var time_order					= "";
	var weight						= "";
	var memo						= "";
	var loading_check				= "";
	var status						= 0;
	var footer						= "";

	switch( +json['transport_type'] ){
		case 1:									//引取
			this.name		= json['shipper_name'] + " "
							+ json['shipper_section_name'] + " "
							+ json['shipper_contact'];
			phone			= json['shipper_phone'];
			post_code		= json['shipper_post_code'];
			this.address	= json['shipper_address'];
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
			status		= 31;
			break;
		case 2:									//納品
			this.name		= json['consignee_name'] + " "
							+ json['consignee_section_name'] + " "
							+ json['consignee_contact'];
			phone			= json['consignee_phone'];
			post_code		= json['consignee_post_code'];
			this.address	= json['consignee_address'];
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
			status		= 41;
			break;
		default:
			//基本的に引取／納品以外はおかしい
			break;
	}
	if( json['req_weight'] ){
		weight	= json['req_weight'] + json['req_weight_unit'];
	}
	//作業開始、処理用ボタン表示の切り替え
	footer	= getFooter( +json['status'], status );

	//タグの取得
	var tag_transmission_order_no	= document.getElementById( "transmission_order_no" );
	var tag_name					= document.getElementById( "name" );
	var tag_phone					= document.getElementById( "phone" );
	var tag_post_code				= document.getElementById( "post_code" );
	var tag_address					= document.getElementById( "address" );
	var tag_time_order				= document.getElementById( "time_order" );
	var tag_weight					= document.getElementById( "weight" );
	var tag_memo					= document.getElementById( "memo" );
	var tag_status					= document.getElementById( "status" );
	var tag_footer					= document.getElementById( "footer" );
	
	tag_transmission_order_no.innerHTML	= json['transmission_order_no'];
	tag_name.innerHTML					= name;
	tag_phone.innerHTML					= phone;
	tag_post_code.innerHTML				= post_code;
	tag_address.innerHTML				= address;
	tag_time_order.innerHTML			= time_order;
	tag_weight.innerHTML				= weight;
	tag_memo.innerHTML					= memo;
	tag_status.innerHTML				= getStatusString( +json['status'] );
	//作業開始済の場合のみフッターを上書き
	if( footer ){
		tag_footer.innerHTML			= footer;		
	}
}

/**
 * @brief	配送情報設定
 * @param	json(in)	配送リスト
 */
function setItemList( json ){
	var list			= "";									//荷物一覧
	var section_class	= "";									//sectionタグのclass属性
	var div_status		= "";									//<div class="status">タグ
	var is_disp			= false;

	var tmp_name		= "";									//配送リストの配送先名
	var tmp_address		= "";									//配送リストの配送先住所
	var delivery_list	= RunInfo.getDeliveryList();			//荷物情報
	var json			= JSON.parse( delivery_list );
	var max_num			= Object.keys( json ).length;

	//荷物リストの表示
	for( idx = 0; idx < max_num; idx++ ){
		tmp_name		= "";
		tmp_address		= "";
		section_class	= "";
		div_status		= "";
		switch( +json['transport_type'] ){
			case 1:									//引取
				tmp_name		= json[idx]['shipper_name'] + " "
								+ json[idx]['shipper_section_name'] + " "
								+ json[idx]['shipper_contact'];
				tmp_address		= json[idx]['shipper_address'];
				if( +json[idx]['status'] == 32 ){
					section_class	= 'complete';
					div_status		= '<div class="status">'
									+ '<img src="images/ico_complete.png" alt="">'
									+ '</div>';
				}else if( +json[idx]['status'] >= 90 ){
					section_class	= 'cancel';					
					div_status		= '<div class="status">'
									+ '<img src="images/ico_cancel.png" alt="">'
									+ '</div>';
				}
				break;
			case 2:									//納品
			default:
				tmp_name		= json[idx]['consignee_name'] + " "
								+ json[idx]['consignee_section_name'] + " "
								+ json[idx]['consignee_contact'];
				tmp_address		= json[idx]['consignee_address'];
				if( ( +json[idx]['status'] == 42 ) || ( +json[idx]['status'] == 43 ) ){
					section_class	= 'complete';
					div_status		= '<div class="status">'
									+ '<img src="images/ico_complete.png" alt="">'
									+ '</div>';
				}else if( ( +json[idx]['status'] == 45 ) || ( +json[idx]['status'] == 46 ) ){
					section_class	= 'cancel';					
					div_status		= '<div class="status">'
									+ '<img src="images/ico_cancel.png" alt="">'
									+ '</div>';
				}else if( +json[idx]['status'] >= 90 ){
					section_class	= 'cancel';					
					div_status		= '<div class="status">'
									+ '<img src="images/ico_cancel.png" alt="">'
									+ '</div>';
				}
				break;
		}

		//配送先が一致するデータのみ表示
		if( ( name		== tmp_name )	&&
			( address	== tmp_address ) ){
			list	+= '<section class="' + section_class + '">'
					+ div_status
					+ '<div>'
					+ '<h2>' + json[idx]['order_no'] + '</h2>'
					+ '<p>' + json[idx]['line_product_name1'] + '</p>'
					+ '</div>'
					+ '</section>';
		}
	}

	//タグの取得
	var tag_item_list		= document.getElementById( "item_list" );
	tag_item_list.innerHTML	= list;
}

/**
 * フッターの設定
 * @param {*} current_status(in)	現在のステータス
 * @param {*} status(in)			作業中のステータス(比較対象)
 */
function getFooter( current_status, status ){
	var footer	= "";

	//不在、再配達受付、再配達中
	if( ( current_status >= 60 ) && ( current_status < 63 ) ){
		var tag_modal_title			= document.getElementById( "modal_title" );
		tag_modal_title.innerHTML	= "再配達開始";
		var tag_modal_body			= document.getElementById( "modal_body" );
		tag_modal_body.innerHTML	= "再配達を開始します。よろしいですか？";
		var tag_next_button_name		= document.getElementById( "next_button_name" );
		tag_next_button_name.innerHTML	= "再配達";
	}else if( ( current_status == 32 )	||
			  ( current_status == 42 )	||
			  ( current_status == 43 )	||
			  ( current_status == 45 )	||
			  ( current_status == 46 ) ){
		footer	= '<div class="common_btn bottom_btn">'
				+ '<div class="btn_disable">不在</div>'
				+ '<div onclick="onTakePictureButtonClick();">写真</div>'
				+ '<div onclick="onDrawSignButtonClick();">サイン</div>'
				+ '<div class="btn_disable">報告</div>'
				+ '</div>';
	}else if( current_status >= status ){
		footer	= '<div class="common_btn bottom_btn">'
				+ '<div onclick="onAbsenceButtonClick();">不在</div>'
				+ '<div onclick="onTakePictureButtonClick();">写真</div>'
				+ '<div onclick="onDrawSignButtonClick();">サイン</div>'
				+ '<div onclick="onReportButtonClick();">報告</div>'
				+ '</div>';
//	}else{
//		この書き方では、何故かmodal-openが効かない。
//		footer	= '<div class="common_btn btn_next modal-open">'
//				+ '<div>作業開始</div>'
//				+ '</div>';
	}
	return footer;
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
 * 作業開始ボタン押下イベント
 */
function onNextButtonClick(){
}
	
 /**
 * @brief	作業開始OKボタン押下イベント
 */
function onModalOKButtunClick(){
	//該当する荷物情報のステータスを作業中に変更する
	var delivery_list	= RunInfo.getDeliveryList();			//荷物情報
	var json			= JSON.parse( delivery_list );
	var max_num			= Object.keys( json ).length;
	var status			= 0;

	var info		= RunInfo.getDetailOrderInfo();				//配送情報
	var info_json	= JSON.parse( info );

	//荷物リストの表示
	for( idx = 0; idx < max_num; idx++ ){
		switch( +json['transport_type'] ){
			case 1:									//引取
				tmp_name		= json[idx]['shipper_name'] + " "
								+ json[idx]['shipper_section_name'] + " "
								+ json[idx]['shipper_contact'];
				tmp_address		= json[idx]['shipper_address'];
				status			= 31;
				break;
			case 2:									//納品
			default:
				tmp_name		= json[idx]['consignee_name'] + " "
								+ json[idx]['consignee_section_name'] + " "
								+ json[idx]['consignee_contact'];
				tmp_address		= json[idx]['consignee_address'];
				status			= 41;
				break;
		}

		//配送先が一致するデータのみ処理
		if( ( name		== tmp_name )	&&
			( address	== tmp_address ) ){
			//ステータスを作業中に更新
			json[idx]['status']	= status;
		}
		if( info_json['_id'] == json[idx]['_id'] ){
			info_json['status']	= status;
		}
	}
	
	RunInfo.setDeliveryList( JSON.stringify( json ) );
	RunInfo.setDetailOrderInfo( JSON.stringify( info_json ) );
	RunInfo.save();

	location.href	= Transitions.getScreen( PAGE_NO );
}

/**
 * 不在報告ボタン押下イベント
 */
function onAbsenceButtonClick(){
	swal( {
		title:	'確認',
		text:	"不在報告を行いますが、よろしいですか？",
		type:	'warning',
		showCancelButton:	true,
		confirmButtonColor:	'#3085d6',
		cancelButtonColor:	'#d33',
		confirmButtonText:	'OK'
	} ).then( function(){
		save_absence();
	}, function( dismiss ){
		if( dismiss === 'cancel' ){
		  swal( '結果', '不在報告をキャンセルしました。', 'error' );
		}
	} );
}

/**
 * 写真ボタン押下イベント
 */
function onTakePictureButtonClick(){
	location.href	= Transitions.getScreen( 4102 );
}

/**
 * @brief	サインボタン押下イベント
 */
function onDrawSignButtonClick(){
	location.href	= Transitions.getScreen( 4103 );
}

/**
 * @brief	報告ボタン押下イベント
 */
function onReportButtonClick(){
	swal( {
		title:	'確認',
		text:	"配送完了報告を行いますが、よろしいですか？",
		type:	'warning',
		showCancelButton:	true,
		confirmButtonColor:	'#3085d6',
		cancelButtonColor:	'#d33',
		confirmButtonText:	'OK'
	} ).then( function(){
		send_report();
	}, function( dismiss ){
		if( dismiss === 'cancel' ){
		  swal( '結果', '配送完了報告をキャンセルしました。', 'error' );
		}
	} );
}

/**
 * @brief	ステータスを不在にする
 **/
function save_absence(){
	//該当する荷物情報のステータスを作業中に変更する
	var delivery_list	= RunInfo.getDeliveryList();			//荷物情報
	var json			= JSON.parse( delivery_list );
	var max_num			= Object.keys( json ).length;
	var status			= 0;

	var info		= RunInfo.getDetailOrderInfo();				//配送情報
	var info_json	= JSON.parse( info );

	//同じ配送先のステータスを全て不在にする
	status			= 60;
	for( idx = 0; idx < max_num; idx++ ){
		switch( +json['transport_type'] ){
			case 1:									//引取
				tmp_name		= json[idx]['shipper_name'] + " "
								+ json[idx]['shipper_section_name'] + " "
								+ json[idx]['shipper_contact'];
				tmp_address		= json[idx]['shipper_address'];
				break;
			case 2:									//納品
			default:
				tmp_name		= json[idx]['consignee_name'] + " "
								+ json[idx]['consignee_section_name'] + " "
								+ json[idx]['consignee_contact'];
				tmp_address		= json[idx]['consignee_address'];
				break;
		}

		//配送先が一致するデータのみ処理
		if( ( name		== tmp_name )	&&
			( address	== tmp_address ) ){
			//ステータスを作業中に更新
			json[idx]['status']	= status;
			info_json['status']	= status;

			//不在報告の送信
			//管理番号がある場合は、管理番号を設定
			var order_no	= json[idx]['order_no'];
			//個口がある場合は、個口のみ処理する
			if( json[idx]['line_tracking_no'] != "" ){
				order_no	= json[idx]['line_tracking_no'];
			}
			//個口がない場合は、送り状番号を取得
			if( order_no != "" ){
				order_no	= json[idx]['sub_voucher_no'];
			}
			if( order_no != "" ){
				order_no	= json[idx]['voucher_no'];
			}

			send_absence( json[idx]['order_no'], json[idx]['voucher_no'], json[idx]['sub_voucher_no'],  );
		}
	}

	RunInfo.setDeliveryList( JSON.stringify( json ) );
	RunInfo.setDetailOrderInfo( JSON.stringify( info_json ) );
	RunInfo.save();

	location.href	= Transitions.getScreen( PAGE_NO );	
}

/**
 * @brief	不在報告の送信
 **/
function send_absence(){
	//OSによってデータの取得元を変える
	var agent		= navigator.userAgent;
	if( ( agent.search( /iPhone/ ) != -1 ) ||
		( agent.search( /iPad/ ) != -1 ) ||
		( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
	}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
		jv_mainapi.postDeliveryAbsence();
	}else{
	}
}

/**
 * 報告の送信
 */
function send_report(){
	//該当する荷物情報のステータスを作業中に変更する
	var delivery_list	= RunInfo.getDeliveryList();			//荷物情報
	var json			= JSON.parse( delivery_list );
	var max_num			= Object.keys( json ).length;
	var status			= 0;

	var info		= RunInfo.getDetailOrderInfo();				//配送情報
	var info_json	= JSON.parse( info );
	var max_status	= 0;
	
	//同じ配送先のステータスを全て不在にする
	for( idx = 0; idx < max_num; idx++ ){
		switch( +json['transport_type'] ){
			case 1:									//引取
				tmp_name		= json[idx]['shipper_name'] + " "
								+ json[idx]['shipper_section_name'] + " "
								+ json[idx]['shipper_contact'];
				tmp_address		= json[idx]['shipper_address'];
				status			= 32;
				break;
			case 2:									//納品
			default:
				tmp_name		= json[idx]['consignee_name'] + " "
								+ json[idx]['consignee_section_name'] + " "
								+ json[idx]['consignee_contact'];
				tmp_address		= json[idx]['consignee_address'];
				status			= 42;
				//キャンセルありの場合は、ステータスを納品完了(返品あり)にする
				if( json[idx]['cancel_reason'] ){
					status		= 45;
				}
				break;
		}

		//配送先が一致するデータのみ処理
		if( ( name		== tmp_name )	&&
			( address	== tmp_address ) ){
			//ステータスを作業中に更新
			json[idx]['status']	= status;
			if( max_status < status ){
				max_status		= status;
			}
		}
	}

	info_json['status']	= max_status;
	
	RunInfo.setDeliveryList( JSON.stringify( json ) );
	RunInfo.setDetailOrderInfo( JSON.stringify( info_json ) );
	RunInfo.save();

	location.href	= Transitions.getScreen( PAGE_NO );
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

