/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 2111;

/*******************************************************************************
 * メソッド
 *******************************************************************************/
/**
 * jQueryのreadyで処理する内容を記載
 */
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
	} catch( e ){
		alert( e.message );
	}
}

/**
 * ページ情報の設
 **/
function setup(){
	//読み込んだ伝票リストを表示する
	var MAX_LENGTH	= 20;
	try{
		//ページ番号の保存
		StatusInfo.read();
		StatusInfo.setProgress( 3 );
		StatusInfo.setCurrentScreenId( PAGE_NO );		//ページ番号の保存
		StatusInfo.save();

		//必要なデータの読込み
		AppInfo.read();
		RunInfo.read();

		var delivery_list	= RunInfo.getDeliveryList();	//配送リスト
		if( delivery_list !== "" ){
			var json			= JSON.parse( delivery_list );
			var max_num			= Object.keys( json ).length;
			var all_list		= "";							//全件表示
			var name_buf		= "";							//配送先名(完全版)
			var name			= "";							//配送先名(先頭から20文字)
			var condition		= "";							//配送条件アイコン
			
			//配送リストの表示
			for( idx = 0; idx < max_num; idx++ ){
				is_disp		= false;							//未処理表示フラグ
	
				//伝票番号
				if( +json[idx]['line_no'] == 1 ){
					order_no	= json[idx]['order_no'];
				}else{
					order_no	= json[idx]['line_tracking_no'];
				}	
	
				//配送条件のULタグ
				condition	= '<ul class="cargo_info">';
	
				//納品物のみ表示
				switch( +json[idx]['transport_type'] ){
					case 1:
						//宛先を表示文字数に詰める
						name_buf		= json[idx]['shipper_name'] + " "
										+ json[idx]['shipper_section_name'] + " "
										+ json[idx]['shipper_contact'];
						//時間指定等の条件アイコンを表示
						if( +json[idx]['delivery_time_order'] > 1 ){
							condition	+= '<li><img src="images/ico_clock.png"></li>';
						}else{
							condition	+= '<li><img src="images/ico_clock_exception.png"></li>';
						}
						if( +json[idx]['req_weight'] >= 40 ){
							condition	+= '<li><img src="images/ico_kg.png"></li>';
						}else{
							condition	+= '<li><img src="images/ico_kg_exception.png"></li>';
						}
						break;
					case 2:
						//宛先を表示文字数に詰める
						name_buf		= json[idx]['consignee_name'] + " "
										+ json[idx]['consignee_section_name'] + " "
										+ json[idx]['consignee_contact'];
						//時間指定等の条件アイコンを表示
						var condition	= '<ul class="cargo_info">';
						if( +json[idx]['delivery_time_order'] > 1 ){
							condition	+= '<li><img src="images/ico_clock.png"></li>';
						}else{
							condition	+= '<li><img src="images/ico_clock_exception.png"></li>';
						}
						if( +json[idx]['req_weight'] >= 40 ){
							condition	+= '<li><img src="images/ico_kg.png"></li>';
						}else{
							condition	+= '<li><img src="images/ico_kg_exception.png"></li>';
						}
						break;
				}
				condition	+= '</ul>';									//配送条件の終了
				name		= name_buf.substr( 0, MAX_LENGTH );			//配送先名を20文字にする
	
				all_list	+= '<section class="inner_contents" onclick="onListClick( ' + json[idx]['_id'] + ' );">';
				all_list	+= '	<h2>' + order_no + '</h2>';
				all_list	+= '	<p>' + json[idx]['line_product_name1'] + '</p>';
				all_list	+= '	<p>' + name + '</p>';
				all_list	+= condition;
				all_list	+= '</section>'
			}
			//値の更新
			var tag_all_list			= document.getElementById( "all_list" );
			tag_all_list.innerHTML		= all_list;
		}
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

/*******************************************************************
 * ページ内で発生するイベント
 *******************************************************************/
/**
 * @brief	伝票番号入力
 */
function onEntryClick(){
	var tag_input_voucher_no	= document.getElementById( "input_voucher_no" );		//入力
	voucher_no					= tag_input_voucher_no.value;

	if( voucher_no == "" ){
		swal( '結果', '伝票番号が入力されていません。', 'error' );
	}else{
		//OSによって処理が変わる
		var agent		= navigator.userAgent;
		if( ( agent.search( /iPhone/ ) != -1 ) ||
			( agent.search( /iPad/ ) != -1 ) ||
			( agent.search( /iPod/ ) != -1 ) ){						//iOSの場合
		}else if( agent.search( /Android/ ) != -1 ){				//Androidの場合
			//Androidの配送情報取得処理を実行する
			jv_mainapi.get_voucher_info( voucher_no );
			//Androidの処理が終わるとリロード
		}else{
			get_order( voucher_no );
			//location.href	= Transitions.getScreen( PAGE_NO );
		}
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
		if( json[idx]['_id'] == id ){
			//一致している場合は、このIDが該当するデータ
			info = json[idx];
			break;
		}
	}
	//表示したい配送依頼情報を設定
	RunInfo.setDetailOrderInfo( JSON.stringify( info ) );
	RunInfo.save();
	location.href	= Transitions.getScreen( 2112 );
}

 /**
 * @brief	報告ボタン押下イベント
 * @note	
 **/
function onReportButtonClick(){
	//ボタンの無効化
	setButtonEnabled( "report_button", 3, false );
	
	//送信する値を設定する
	RunInfo.setReceiveCourseTime( toLocaleString( new Date() ) );
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
		jv_mainapi.post_course_info();
	}else{													//Windowsの場合
		//Androidの通信処理で取得する情報を設定する
		RunInfo.setInstructNo( get_instruct_no() );
		course_info		= get_course_info();
		RunInfo.setDeliveryList( course_info );				//配送リスト(納品／引取含む)
		//JSONから件数を取得する
		RunInfo.setDeliveryNum( getDeliveryNum( course_info ) );
		RunInfo.setVoucherNum( getVoucherNum( course_info ) );
		RunInfo.save();
		//画面遷移
		var next_id	= Transitions.getNextScreenId( PAGE_NO );
		location.href = Transitions.getScreen( next_id );
	}
}

/**
 * 配送先件数を取得する
 * @param {*} list(in)	JSON文字列
 **/
function getDeliveryNum( list ){
	//JSON文字列をJSONに変換
	var json	= JSON.parse( list );
	var max_num	= Object.keys( json ).length;
	var pre_consignee_address	= "";
	var pre_consignee_company	= "";
	var pre_consignee_section	= "";
	var pre_consignee_contact	= "";
	var num						= 0;

	//配送先件数のカウント
	for( idx = 0; idx < max_num; idx++ ){
		if( ( json[idx]['consignee_address']		!= pre_consignee_address ) ||
			( json[idx]['consignee_name']			!= pre_consignee_company ) ||
			( json[idx]['consignee_section_name']	!= pre_consignee_section ) ||
			( json[idx]['consignee_contact']		!= pre_consignee_contact ) ){
			//件数を増やす
			num++;
			//新しい配送先を記録する
			pre_consignee_address	= json[idx]['consignee_address'];
			pre_consignee_company	= json[idx]['consignee_name'];
			pre_consignee_section	= json[idx]['consignee_section_name'];
			pre_consignee_contact	= json[idx]['consignee_contact'];
		}
	}

	return num;
}

/**
 * 伝票枚数を取得する
 * @param {*} list(in)	JSON文字列
 **/
function getVoucherNum( list ){
	//JSON文字列をJSONに変換
	var json = JSON.parse( list );
	return Object.keys( json ).length;
}

/*******************************************************************************
 * デモデータの設定
 *******************************************************************************/
/**
 * @brief	配送依頼の取得
 * @param	voucher_no(in)	管理番号(伝票番号、送り状番号など)
 */
function get_order( voucher_no ){
	//サーバー情報に見立てたデモデータ
	var tmp_list		= get_course_info();
	var tmp_json		= JSON.parse( tmp_list );
	var max_tmp_num		= Object.keys( tmp_json ).length;
	var tmp_info		= new Array();										//一致したデータ

	//サーバー情報から一致するデータの検索(ダミー処理)
	for( idx = 0; idx < max_tmp_num; idx++ ){
		//管理番号が一致するデータを検索する(個口も同じタイミングで取得する)
		if( ( tmp_json[idx]['order_no']			== voucher_no )	||
			( tmp_json[idx]['voucher_no']		== voucher_no )	||
			( tmp_json[idx]['sub_voucher_no']	== voucher_no )	||
			( tmp_json[idx]['line_tracking_no']	== voucher_no ) ){
			//受信情報
			tmp_info[tmp_json[idx]['_id']] = tmp_json[idx];
		}
	}

	//内部情報から一致するデータの検索
	var delivery_list	= RunInfo.getDeliveryList();						//読込リスト
	var json;
	var max_num			= 0;
	if( delivery_list !== "" ){
		json			= JSON.parse( delivery_list );
		max_num			= Object.keys( json ).length;
	}
	var info				= new Array();					//内部データとして一致した情報
	var new_delivery_list	= new Array();					//既存データ(配送情報として上書きする情報)
	for( idx = 0; idx < max_num; idx++ ){
		//管理番号が一致するデータを検索する
		if( ( json[idx]['order_no']			== voucher_no )	||
			( json[idx]['voucher_no']		== voucher_no )	||
			( json[idx]['sub_voucher_no']	== voucher_no )	||
			( json[idx]['line_tracking_no']	== voucher_no ) ){

			//一致するデータが登録されている
			info[json[idx]['_id']] = json[idx];
		}else{
			//一致しないデータは、既存の登録データとして使う
			new_delivery_list[json[idx]['_id']]	= json[idx];
		}
	}

	//サーバーから当該データが取得できない場合
	if( Object.keys( tmp_info ).length == 0 ){
		//メッセージの表示
		swal( {
			title:	'警告',
			text:	"該当する伝票番号(" + voucher_no + ")が存在しませんでした。新規伝票として登録しますが、よろしいですか？",
			type:	'warning',
			showCancelButton:	true,
			confirmButtonColor:	'#3085d6',
			cancelButtonColor:	'#d33',
			confirmButtonText:	'OK'
		} ).then( function(){
			var tmp_new_rec	= get_new_record( Object.keys( new_delivery_list ).length + 1, voucher_no );
			var tmp_rec		= JSON.parse( tmp_new_rec );
			new_delivery_list[Object.keys( new_delivery_list ).length + 1]	= tmp_rec;
			//該当するものがないので、当該データを追加して保存
			RunInfo.setDeliveryList( JSON.stringify( new_delivery_list ) );
			RunInfo.save();
		}, function( dismiss ){
			if( dismiss === 'cancel' ){
				swal( '結果', '新規伝票登録をキャンセルしました。', 'error' );
			}
		} );
	}else{
		//取得した情報を取り込む
		if( Object.keys( info ).length > 0 ){
			swal( {
				title:	'警告',
				text:	"この番号は、既に読み込まれた番号です。更新しますが、よろしいですか？",
				type:	'info',
				showCancelButton:	true,
				confirmButtonColor:	'#3085d6',
				cancelButtonColor:	'#d33',
				confirmButtonText:	'OK'
			} ).then( function(){
				//更新処理
				info.foreach( function(value){
					new_delivery_list[value['_id']]	= value;
				} );
				//該当するものがないので、当該データを追加して保存
				RunInfo.setDeliveryList( JSON.stringify( new_delivery_list ) );
				RunInfo.save();
			}, function( dismiss ){
				if( dismiss === 'cancel' ){
				swal( '結果', '更新をキャンセルしました。', 'error' );
				}
			} );
		}else{
			//該当するものがないので、当該データを追加して保存
			RunInfo.setDeliveryList( JSON.stringify( new_delivery_list ) );
			RunInfo.save();
		}
	}
}

/**
 * 配送情報の取得
 * @return	配送情報
 * @note	配送情報は、配送先住所、配送先名(企業名、部署名、担当者名)順にソートされていること
 */
function get_course_info(){
	var date			= new Date();
	var order_date		= toLocaleDateString( date );	
	date.setDate( date.getDate() + 1 );
	var pickup_date		= toLocaleDateString( date );
	date.setDate( date.getDate() + 1 );
	var delivery_date	= toLocaleDateString( date );
	var created			= toLocaleString( new Date() );
	var instruct_no		= get_instruct_no();
	var driver_name		= RunInfo.getDriverName();
	
	var delivery_list	= "["
		+ '{"_id":1,"order_id":1,"distribution_id":1,"order_no":"EC103401700001","order_date":"' + order_date + '","sub_voucher_no":1701102034190000,"voucher_no":110203419000117,"conveyance_type":"00","pickup_type":null,"transport_type":2,"charge_type":"03","pickup_date":"' + pickup_date + '","pickup_time_order":1,"pickup_time_from":null,"pickup_time_to":null,"pickup_range":null,"pickup_memo":null,"delivery_date":"' + delivery_date + '","delivery_time_order":2,"delivery_time_from":"10:00","delivery_time_to":"12:00","delivery_range":"","delivery_memo":"自転車のかごに入れてください","handling_memo":null,"additional_memo":null,"special_contents":null,"delivery_no":"EC100000000001","shipment_no":"ECS00000000001","req_number":1,"req_num_unit":"PCE","req_weight":null,"req_weight_unit":"KG","req_volume":null,"req_volume_unit":"CM3","req_unit_load":null,"report_number":null,"report_num_unit":"PCE","report_weight":null,"report_weight_unit":"KG","report_volume":null,"report_volume_unit":"CM3","line_no":1,"line_order_no":null,"line_part_number":null,"line_tracking_no":"EC103401700001","line_license_plate_no":null,"line_seq_no":1,"line_branch_no":1,"line_order_control_no":null,"line_owner_product_code":null,"line_contractor_product_code":null,"line_product_name1":"書籍","line_product_name2":null,"line_product_name":null,"line_unit_load":null,"line_req_number":1,"line_num_unit":"PCE","line_weight":null,"line_weight_unit":"KG","line_volume":null,"line_volume_unit":"CM3","line_quantity":null,"line_quantity_unit":null,"dimensions":null,"package_code":null,"package_name":null,"baggage_handling_memo":null,"line_fares":null,"shipper_code":"AMAZON","shipper_name":"アマ○ン・ジャパン","shipper_section_code":"TK","shipper_section_name":"東京センター","shipper_contact":null,"shipper_phone":"03-0000-0000","shipper_address":"東京都江東区木場","shipper_post_code":1350042,"client_id":1,"client_code":"AMAZON","client_name":"アマ○ン・ジャパン","client_section_code":"TK","client_section_name":"東京センター","client_phone":"03-0000-0000","client_address":"東京都江東区木場","client_post_code":1350042,"consignee_code":null,"consignee_name":"株式会社KITOHA","consignee_section_code":null,"consignee_section_name":"営業部","consignee_contact":"中嶋 祐太","consignee_phone":"03-5687-7552","consignee_address":"東京都千代田区神田佐久間町3-37-3 シェルプリーズビルⅡ 4F","consignee_post_code":1010025,"carrier_id":1,"carrier_code":"DEMO","carrier_name":"株式会社デモ運輸","carrier_center_code_from":null,"carrier_center_name_from":null,"carrier_center_phone_from":null,"carrier_center_code_to":null,"carrier_center_name_to":null,"billing_id":null,"billing_code":null,"billing_name":null,"billing_section_code":null,"billing_section_name":null,"shipping_code":null,"shipping_name":null,"shipping_section_code":null,"shipping_section_name":null,"shipping_phone":null,"shipping_address_code":null,"shipping_address":null,"destination_code":null,"destination_name":null,"destination_section_code":null,"destination_section_name":null,"destination_contact_code":null,"destination_contact":null,"destination_phone":null,"destination_city_code":null,"destination_address_code":null,"destination_address":null,"destination_post_code":null,"seal_number":null,"seal_num_unit":null,"seal_weight":null,"seal_weight_unit":null,"seal_volume":null,"seal_volume_unit":null,"instruction_no":"'+ instruct_no +'","route_id":1,"transmission_order_no":1,"size":"S","luggage_type":null,"service_type":null,"is_weight":null,"is_contact":null,"is_before_vote":null,"previous_check_time":null,"previous_check_result":null,"base_fare":null,"incidental_charge":null,"insurance_code":null,"insurance_clime":null,"insurance_fee":null,"tax_code":null,"item_price":null,"item_tax":null,"total_fare":null,"total_tax_free":null,"regist_time":null,"regist_check_time":null,"loaded_time":null,"start_time":null,"destination_start_time":null,"pickup_time":null,"delivery_time":null,"return_time":null,"unloaded_time":null,"receipt_time":null,"storage_id":null,"next_status":null,"status":11,"cancel_loading_reason":null,"cancel_loading_reason_text":null,"cancel_reason":null,"cancel_reason_text":null,"cancel_after_reason":null,"cancel_after_reason_text":null,"cancel_process":null,"driver":"' + driver_name + '","proc_date":"' + delivery_date + '","direction":1,"dist_pickup_type":1,"registrant_id":1,"created":"' + created + '","modified":"' + created + '"},'
		+ '{"_id":3,"order_id":3,"distribution_id":3,"order_no":"BB201101700001","order_date":"' + order_date + '","sub_voucher_no":1701102034190002,"voucher_no":110203419000317,"conveyance_type":"00","pickup_type":null,"transport_type":2,"charge_type":"03","pickup_date":"' + pickup_date + '","pickup_time_order":1,"pickup_time_from":null,"pickup_time_to":null,"pickup_range":null,"pickup_memo":null,"delivery_date":"' + delivery_date + '","delivery_time_order":3,"delivery_time_from":"","delivery_time_to":"","delivery_range":"PM","delivery_memo":"ポスト下においてください","handling_memo":null,"additional_memo":null,"special_contents":null,"delivery_no":"BB000000000001","shipment_no":"BBS00000000001","req_number":1,"req_num_unit":"PCE","req_weight":null,"req_weight_unit":"KG","req_volume":null,"req_volume_unit":"CM3","req_unit_load":null,"report_number":null,"report_num_unit":"PCE","report_weight":null,"report_weight_unit":"KG","report_volume":null,"report_volume_unit":"CM3","line_no":1,"line_order_no":null,"line_part_number":null,"line_tracking_no":"BB201101700001","line_license_plate_no":null,"line_seq_no":1,"line_branch_no":1,"line_order_control_no":null,"line_owner_product_code":null,"line_contractor_product_code":null,"line_product_name1":"筆記用具","line_product_name2":null,"line_product_name":null,"line_unit_load":null,"line_req_number":1,"line_num_unit":"PCE","line_weight":null,"line_weight_unit":"KG","line_volume":null,"line_volume_unit":"CM3","line_quantity":null,"line_quantity_unit":null,"dimensions":null,"package_code":null,"package_name":null,"baggage_handling_memo":null,"line_fares":null,"shipper_code":"ASUKLE","shipper_name":"○スクル","shipper_section_code":"CB","shipper_section_name":"千葉センター","shipper_contact":null,"shipper_phone":"043-000-0000","shipper_address":"千葉県千葉市緑区あすみが丘","shipper_post_code":2670066,"client_id":2,"client_code":"ASUKLE","client_name":"○スクル","client_section_code":"CB","client_section_name":"千葉センター","client_phone":"043-000-0000","client_address":"千葉県千葉市緑区あすみが丘","client_post_code":2670066,"consignee_code":null,"consignee_name":"株式会社KITOHA","consignee_section_code":null,"consignee_section_name":"営業部","consignee_contact":"中嶋 祐太","consignee_phone":"03-5687-7552","consignee_address":"東京都千代田区神田佐久間町3-37-3 シェルプリーズビルⅡ 4F","consignee_post_code":1010025,"carrier_id":1,"carrier_code":"DEMO","carrier_name":"株式会社デモ運輸","carrier_center_code_from":null,"carrier_center_name_from":null,"carrier_center_phone_from":null,"carrier_center_code_to":null,"carrier_center_name_to":null,"billing_id":null,"billing_code":null,"billing_name":null,"billing_section_code":null,"billing_section_name":null,"shipping_code":null,"shipping_name":null,"shipping_section_code":null,"shipping_section_name":null,"shipping_phone":null,"shipping_address_code":null,"shipping_address":null,"destination_code":null,"destination_name":null,"destination_section_code":null,"destination_section_name":null,"destination_contact_code":null,"destination_contact":null,"destination_phone":null,"destination_city_code":null,"destination_address_code":null,"destination_address":null,"destination_post_code":null,"seal_number":null,"seal_num_unit":null,"seal_weight":null,"seal_weight_unit":null,"seal_volume":null,"seal_volume_unit":null,"instruction_no":"'+ instruct_no +'","route_id":1,"transmission_order_no":2,"size":"S","luggage_type":null,"service_type":null,"is_weight":null,"is_contact":null,"is_before_vote":null,"previous_check_time":null,"previous_check_result":null,"base_fare":null,"incidental_charge":null,"insurance_code":null,"insurance_clime":null,"insurance_fee":null,"tax_code":null,"item_price":null,"item_tax":null,"total_fare":null,"total_tax_free":null,"regist_time":null,"regist_check_time":null,"loaded_time":null,"start_time":null,"destination_start_time":null,"pickup_time":null,"delivery_time":null,"return_time":null,"unloaded_time":null,"receipt_time":null,"storage_id":null,"next_status":null,"status":11,"cancel_loading_reason":null,"cancel_loading_reason_text":null,"cancel_reason":null,"cancel_reason_text":null,"cancel_after_reason":null,"cancel_after_reason_text":null,"cancel_process":null,"driver":"' + driver_name + '","proc_date":"' + delivery_date + '","direction":1,"dist_pickup_type":1,"registrant_id":1,"created":"' + created + '","modified":"' + created + '"},'
		+ '{"_id":5,"order_id":5,"distribution_id":5,"order_no":"BC305601700001","order_date":"' + order_date + '","sub_voucher_no":1701102034190004,"voucher_no":110203419000517,"conveyance_type":"00","pickup_type":null,"transport_type":2,"charge_type":"03","pickup_date":"' + pickup_date + '","pickup_time_order":1,"pickup_time_from":null,"pickup_time_to":null,"pickup_range":null,"pickup_memo":null,"delivery_date":"' + delivery_date + '","delivery_time_order":1,"delivery_time_from":"18:00","delivery_time_to":"20:00","delivery_range":"","delivery_memo":"天地無用","handling_memo":null,"additional_memo":null,"special_contents":null,"delivery_no":"BC000000000001","shipment_no":"BCS00000000001","req_number":1,"req_num_unit":"PCE","req_weight":null,"req_weight_unit":"KG","req_volume":null,"req_volume_unit":"CM3","req_unit_load":null,"report_number":null,"report_num_unit":"PCE","report_weight":null,"report_weight_unit":"KG","report_volume":null,"report_volume_unit":"CM3","line_no":1,"line_order_no":null,"line_part_number":null,"line_tracking_no":"BC305601700001","line_license_plate_no":null,"line_seq_no":1,"line_branch_no":1,"line_order_control_no":null,"line_owner_product_code":null,"line_contractor_product_code":null,"line_product_name1":"PC","line_product_name2":null,"line_product_name":null,"line_unit_load":null,"line_req_number":1,"line_num_unit":"PCE","line_weight":null,"line_weight_unit":"KG","line_volume":null,"line_volume_unit":"CM3","line_quantity":null,"line_quantity_unit":null,"dimensions":null,"package_code":null,"package_name":null,"baggage_handling_memo":null,"line_fares":null,"shipper_code":"ASUKLE","shipper_name":"○スクル","shipper_section_code":"CB","shipper_section_name":"千葉センター","shipper_contact":null,"shipper_phone":"043-000-0000","shipper_address":"千葉県千葉市緑区あすみが丘","shipper_post_code":2670066,"client_id":2,"client_code":"ASUKLE","client_name":"○スクル","client_section_code":"CB","client_section_name":"千葉センター","client_phone":"043-000-0000","client_address":"千葉県千葉市緑区あすみが丘","client_post_code":2670066,"consignee_code":null,"consignee_name":"株式会社KITOHA","consignee_section_code":null,"consignee_section_name":"システム部","consignee_contact":"川口 健","consignee_phone":"03-5687-7551","consignee_address":"東京都千代田区神田佐久間町3-37-3 シェルプリーズビルⅡ 4F","consignee_post_code":1010025,"carrier_id":1,"carrier_code":"DEMO","carrier_name":"株式会社デモ運輸","carrier_center_code_from":null,"carrier_center_name_from":null,"carrier_center_phone_from":null,"carrier_center_code_to":null,"carrier_center_name_to":null,"billing_id":null,"billing_code":null,"billing_name":null,"billing_section_code":null,"billing_section_name":null,"shipping_code":null,"shipping_name":null,"shipping_section_code":null,"shipping_section_name":null,"shipping_phone":null,"shipping_address_code":null,"shipping_address":null,"destination_code":null,"destination_name":null,"destination_section_code":null,"destination_section_name":null,"destination_contact_code":null,"destination_contact":null,"destination_phone":null,"destination_city_code":null,"destination_address_code":null,"destination_address":null,"destination_post_code":null,"seal_number":null,"seal_num_unit":null,"seal_weight":null,"seal_weight_unit":null,"seal_volume":null,"seal_volume_unit":null,"instruction_no":"'+ instruct_no +'","route_id":1,"transmission_order_no":3,"size":"M","luggage_type":null,"service_type":null,"is_weight":null,"is_contact":null,"is_before_vote":null,"previous_check_time":null,"previous_check_result":null,"base_fare":null,"incidental_charge":null,"insurance_code":null,"insurance_clime":null,"insurance_fee":null,"tax_code":null,"item_price":null,"item_tax":null,"total_fare":null,"total_tax_free":null,"regist_time":null,"regist_check_time":null,"loaded_time":null,"start_time":null,"destination_start_time":null,"pickup_time":null,"delivery_time":null,"return_time":null,"unloaded_time":null,"receipt_time":null,"storage_id":null,"next_status":null,"status":11,"cancel_loading_reason":null,"cancel_loading_reason_text":null,"cancel_reason":null,"cancel_reason_text":null,"cancel_after_reason":null,"cancel_after_reason_text":null,"cancel_process":null,"driver":"' + driver_name + '","proc_date":"' + delivery_date + '","direction":1,"dist_pickup_type":1,"registrant_id":1,"created":"' + created + '","modified":"' + created + '"},'
		+ '{"_id":2,"order_id":2,"distribution_id":2,"order_no":"EC103401700002","order_date":"' + order_date + '","sub_voucher_no":1701102034190001,"voucher_no":110203419000217,"conveyance_type":"00","pickup_type":null,"transport_type":2,"charge_type":"03","pickup_date":"' + pickup_date + '","pickup_time_order":1,"pickup_time_from":null,"pickup_time_to":null,"pickup_range":null,"pickup_memo":null,"delivery_date":"' + delivery_date + '","delivery_time_order":1,"delivery_time_from":"","delivery_time_to":"","delivery_range":"","delivery_memo":"","handling_memo":null,"additional_memo":null,"special_contents":null,"delivery_no":"EC100000000002","shipment_no":"ECS00000000002","req_number":1,"req_num_unit":"PCE","req_weight":null,"req_weight_unit":"KG","req_volume":null,"req_volume_unit":"CM3","req_unit_load":null,"report_number":null,"report_num_unit":"PCE","report_weight":null,"report_weight_unit":"KG","report_volume":null,"report_volume_unit":"CM3","line_no":1,"line_order_no":null,"line_part_number":null,"line_tracking_no":"EC103401700002","line_license_plate_no":null,"line_seq_no":1,"line_branch_no":1,"line_order_control_no":null,"line_owner_product_code":null,"line_contractor_product_code":null,"line_product_name1":"水","line_product_name2":null,"line_product_name":null,"line_unit_load":null,"line_req_number":1,"line_num_unit":"PCE","line_weight":null,"line_weight_unit":"KG","line_volume":null,"line_volume_unit":"CM3","line_quantity":null,"line_quantity_unit":null,"dimensions":null,"package_code":null,"package_name":null,"baggage_handling_memo":null,"line_fares":null,"shipper_code":"AMAZON","shipper_name":"アマ○ン・ジャパン","shipper_section_code":"TK","shipper_section_name":"東京センター","shipper_contact":null,"shipper_phone":"03-0000-0000","shipper_address":"東京都江東区木場","shipper_post_code":1350042,"client_id":1,"client_code":"AMAZON","client_name":"アマ○ン・ジャパン","client_section_code":"TK","client_section_name":"東京センター","client_phone":"03-0000-0000","client_address":"東京都江東区木場","client_post_code":1350042,"consignee_code":null,"consignee_name":"株式会社プレンティー","consignee_section_code":null,"consignee_section_name":"","consignee_contact":"","consignee_phone":"","consignee_address":"東京都品川区上大崎","consignee_post_code":1410021,"carrier_id":1,"carrier_code":"DEMO","carrier_name":"株式会社デモ運輸","carrier_center_code_from":null,"carrier_center_name_from":null,"carrier_center_phone_from":null,"carrier_center_code_to":null,"carrier_center_name_to":null,"billing_id":null,"billing_code":null,"billing_name":null,"billing_section_code":null,"billing_section_name":null,"shipping_code":null,"shipping_name":null,"shipping_section_code":null,"shipping_section_name":null,"shipping_phone":null,"shipping_address_code":null,"shipping_address":null,"destination_code":null,"destination_name":null,"destination_section_code":null,"destination_section_name":null,"destination_contact_code":null,"destination_contact":null,"destination_phone":null,"destination_city_code":null,"destination_address_code":null,"destination_address":null,"destination_post_code":null,"seal_number":null,"seal_num_unit":null,"seal_weight":null,"seal_weight_unit":null,"seal_volume":null,"seal_volume_unit":null,"instruction_no":"'+ instruct_no +'","route_id":1,"transmission_order_no":4,"size":"M","luggage_type":null,"service_type":null,"is_weight":null,"is_contact":null,"is_before_vote":null,"previous_check_time":null,"previous_check_result":null,"base_fare":null,"incidental_charge":null,"insurance_code":null,"insurance_clime":null,"insurance_fee":null,"tax_code":null,"item_price":null,"item_tax":null,"total_fare":null,"total_tax_free":null,"regist_time":null,"regist_check_time":null,"loaded_time":null,"start_time":null,"destination_start_time":null,"pickup_time":null,"delivery_time":null,"return_time":null,"unloaded_time":null,"receipt_time":null,"storage_id":null,"next_status":null,"status":11,"cancel_loading_reason":null,"cancel_loading_reason_text":null,"cancel_reason":null,"cancel_reason_text":null,"cancel_after_reason":null,"cancel_after_reason_text":null,"cancel_process":null,"driver":"' + driver_name + '","proc_date":"' + delivery_date + '","direction":1,"dist_pickup_type":1,"registrant_id":1,"created":"' + created + '","modified":"' + created + '"},'
		+ '{"_id":4,"order_id":4,"distribution_id":4,"order_no":"BB201101700002","order_date":"' + order_date + '","sub_voucher_no":1701102034190003,"voucher_no":110203419000417,"conveyance_type":"00","pickup_type":null,"transport_type":2,"charge_type":"03","pickup_date":"' + pickup_date + '","pickup_time_order":1,"pickup_time_from":null,"pickup_time_to":null,"pickup_range":null,"pickup_memo":null,"delivery_date":"' + delivery_date + '","delivery_time_order":1,"delivery_time_from":"","delivery_time_to":"","delivery_range":"","delivery_memo":"","handling_memo":null,"additional_memo":null,"special_contents":null,"delivery_no":"BB000000000002","shipment_no":"BBS00000000002","req_number":1,"req_num_unit":"PCE","req_weight":null,"req_weight_unit":"KG","req_volume":null,"req_volume_unit":"CM3","req_unit_load":null,"report_number":null,"report_num_unit":"PCE","report_weight":null,"report_weight_unit":"KG","report_volume":null,"report_volume_unit":"CM3","line_no":1,"line_order_no":null,"line_part_number":null,"line_tracking_no":"BB201101700002","line_license_plate_no":null,"line_seq_no":1,"line_branch_no":1,"line_order_control_no":null,"line_owner_product_code":null,"line_contractor_product_code":null,"line_product_name1":"いす","line_product_name2":null,"line_product_name":null,"line_unit_load":null,"line_req_number":1,"line_num_unit":"PCE","line_weight":null,"line_weight_unit":"KG","line_volume":null,"line_volume_unit":"CM3","line_quantity":null,"line_quantity_unit":null,"dimensions":null,"package_code":null,"package_name":null,"baggage_handling_memo":null,"line_fares":null,"shipper_code":"ASUKLE","shipper_name":"○スクル","shipper_section_code":"CB","shipper_section_name":"千葉センター","shipper_contact":null,"shipper_phone":"043-000-0000","shipper_address":"千葉県千葉市緑区あすみが丘","shipper_post_code":2670066,"client_id":2,"client_code":"ASUKLE","client_name":"○スクル","client_section_code":"CB","client_section_name":"千葉センター","client_phone":"043-000-0000","client_address":"千葉県千葉市緑区あすみが丘","client_post_code":2670066,"consignee_code":null,"consignee_name":"株式会社IHK","consignee_section_code":null,"consignee_section_name":"","consignee_contact":"遠藤 直樹","consignee_phone":"03-5687-7553","consignee_address":"東京都千代田区神田佐久間町3-37-3 シェルプリーズビルⅡ 4F","consignee_post_code":1010025,"carrier_id":1,"carrier_code":"DEMO","carrier_name":"株式会社デモ運輸","carrier_center_code_from":null,"carrier_center_name_from":null,"carrier_center_phone_from":null,"carrier_center_code_to":null,"carrier_center_name_to":null,"billing_id":null,"billing_code":null,"billing_name":null,"billing_section_code":null,"billing_section_name":null,"shipping_code":null,"shipping_name":null,"shipping_section_code":null,"shipping_section_name":null,"shipping_phone":null,"shipping_address_code":null,"shipping_address":null,"destination_code":null,"destination_name":null,"destination_section_code":null,"destination_section_name":null,"destination_contact_code":null,"destination_contact":null,"destination_phone":null,"destination_city_code":null,"destination_address_code":null,"destination_address":null,"destination_post_code":null,"seal_number":null,"seal_num_unit":null,"seal_weight":null,"seal_weight_unit":null,"seal_volume":null,"seal_volume_unit":null,"instruction_no":"'+ instruct_no +'","route_id":1,"transmission_order_no":5,"size":"L","luggage_type":null,"service_type":null,"is_weight":null,"is_contact":null,"is_before_vote":null,"previous_check_time":null,"previous_check_result":null,"base_fare":null,"incidental_charge":null,"insurance_code":null,"insurance_clime":null,"insurance_fee":null,"tax_code":null,"item_price":null,"item_tax":null,"total_fare":null,"total_tax_free":null,"regist_time":null,"regist_check_time":null,"loaded_time":null,"start_time":null,"destination_start_time":null,"pickup_time":null,"delivery_time":null,"return_time":null,"unloaded_time":null,"receipt_time":null,"storage_id":null,"next_status":null,"status":11,"cancel_loading_reason":null,"cancel_loading_reason_text":null,"cancel_reason":null,"cancel_reason_text":null,"cancel_after_reason":null,"cancel_after_reason_text":null,"cancel_process":null,"driver":"' + driver_name + '","proc_date":"' + delivery_date + '","direction":1,"dist_pickup_type":1,"registrant_id":1,"created":"' + created + '","modified":"' + created + '"},'
		+ '{"_id":6,"order_id":6,"distribution_id":6,"order_no":"TC101801700001","order_date":"' + order_date + '","sub_voucher_no":1701102034190005,"voucher_no":110203419001017,"conveyance_type":"00","pickup_type":null,"transport_type":2,"charge_type":"03","pickup_date":"' + pickup_date + '","pickup_time_order":1,"pickup_time_from":null,"pickup_time_to":null,"pickup_range":null,"pickup_memo":null,"delivery_date":"' + delivery_date + '","delivery_time_order":3,"delivery_time_from":"","delivery_time_to":"","delivery_range":"AM","delivery_memo":"","handling_memo":null,"additional_memo":null,"special_contents":null,"delivery_no":"TC000000000001","shipment_no":"TCS00000000001","req_number":2,"req_num_unit":"PCE","req_weight":null,"req_weight_unit":"KG","req_volume":null,"req_volume_unit":"CM3","req_unit_load":null,"report_number":null,"report_num_unit":"PCE","report_weight":null,"report_weight_unit":"KG","report_volume":null,"report_volume_unit":"CM3","line_no":1,"line_order_no":null,"line_part_number":null,"line_tracking_no":"TC101801700001","line_license_plate_no":null,"line_seq_no":1,"line_branch_no":1,"line_order_control_no":null,"line_owner_product_code":null,"line_contractor_product_code":null,"line_product_name1":"コピー用紙","line_product_name2":null,"line_product_name":null,"line_unit_load":null,"line_req_number":1,"line_num_unit":"PCE","line_weight":null,"line_weight_unit":"KG","line_volume":null,"line_volume_unit":"CM3","line_quantity":null,"line_quantity_unit":null,"dimensions":null,"package_code":null,"package_name":null,"baggage_handling_memo":null,"line_fares":null,"shipper_code":"OTSUKA","shipper_name":"大○商会","shipper_section_code":"ST","shipper_section_name":"埼玉センター","shipper_contact":null,"shipper_phone":"048-000-0000","shipper_address":"埼玉県朝霞市青葉台","shipper_post_code":3510016,"client_id":3,"client_code":"OTSUKA","client_name":"大○商会","client_section_code":"ST","client_section_name":"埼玉センター","client_phone":"048-000-0000","client_address":"埼玉県朝霞市青葉台","client_post_code":3510016,"consignee_code":null,"consignee_name":"木葉 茂","consignee_section_code":null,"consignee_section_name":"","consignee_contact":"","consignee_phone":"","consignee_address":"東京都港区西新橋","consignee_post_code":1030005,"carrier_id":1,"carrier_code":"DEMO","carrier_name":"株式会社デモ運輸","carrier_center_code_from":null,"carrier_center_name_from":null,"carrier_center_phone_from":null,"carrier_center_code_to":null,"carrier_center_name_to":null,"billing_id":null,"billing_code":null,"billing_name":null,"billing_section_code":null,"billing_section_name":null,"shipping_code":null,"shipping_name":null,"shipping_section_code":null,"shipping_section_name":null,"shipping_phone":null,"shipping_address_code":null,"shipping_address":null,"destination_code":null,"destination_name":null,"destination_section_code":null,"destination_section_name":null,"destination_contact_code":null,"destination_contact":null,"destination_phone":null,"destination_city_code":null,"destination_address_code":null,"destination_address":null,"destination_post_code":null,"seal_number":null,"seal_num_unit":null,"seal_weight":null,"seal_weight_unit":null,"seal_volume":null,"seal_volume_unit":null,"instruction_no":"'+ instruct_no +'","route_id":1,"transmission_order_no":6,"size":"L","luggage_type":null,"service_type":null,"is_weight":null,"is_contact":null,"is_before_vote":null,"previous_check_time":null,"previous_check_result":null,"base_fare":null,"incidental_charge":null,"insurance_code":null,"insurance_clime":null,"insurance_fee":null,"tax_code":null,"item_price":null,"item_tax":null,"total_fare":null,"total_tax_free":null,"regist_time":null,"regist_check_time":null,"loaded_time":null,"start_time":null,"destination_start_time":null,"pickup_time":null,"delivery_time":null,"return_time":null,"unloaded_time":null,"receipt_time":null,"storage_id":null,"next_status":null,"status":11,"cancel_loading_reason":null,"cancel_loading_reason_text":null,"cancel_reason":null,"cancel_reason_text":null,"cancel_after_reason":null,"cancel_after_reason_text":null,"cancel_process":null,"driver":"' + driver_name + '","proc_date":"' + delivery_date + '","direction":1,"dist_pickup_type":1,"registrant_id":1,"created":"' + created + '","modified":"' + created + '"},'
		+ '{"_id":7,"order_id":7,"distribution_id":7,"order_no":"TC101801700001","order_date":"' + order_date + '","sub_voucher_no":1701102034190005,"voucher_no":110203419001017,"conveyance_type":"00","pickup_type":null,"transport_type":2,"charge_type":"03","pickup_date":"' + pickup_date + '","pickup_time_order":1,"pickup_time_from":null,"pickup_time_to":null,"pickup_range":null,"pickup_memo":null,"delivery_date":"' + delivery_date + '","delivery_time_order":3,"delivery_time_from":"","delivery_time_to":"","delivery_range":"AM","delivery_memo":"","handling_memo":null,"additional_memo":null,"special_contents":null,"delivery_no":"TC000000000001","shipment_no":"TCS00000000001","req_number":null,"req_num_unit":"PCE","req_weight":null,"req_weight_unit":"KG","req_volume":null,"req_volume_unit":"CM3","req_unit_load":null,"report_number":null,"report_num_unit":"PCE","report_weight":null,"report_weight_unit":"KG","report_volume":null,"report_volume_unit":"CM3","line_no":2,"line_order_no":null,"line_part_number":null,"line_tracking_no":"KTC0000000001K","line_license_plate_no":null,"line_seq_no":2,"line_branch_no":2,"line_order_control_no":null,"line_owner_product_code":null,"line_contractor_product_code":null,"line_product_name1":"コピー用紙","line_product_name2":null,"line_product_name":null,"line_unit_load":null,"line_req_number":1,"line_num_unit":"PCE","line_weight":null,"line_weight_unit":"KG","line_volume":null,"line_volume_unit":"CM3","line_quantity":null,"line_quantity_unit":null,"dimensions":null,"package_code":null,"package_name":null,"baggage_handling_memo":null,"line_fares":null,"shipper_code":"OTSUKA","shipper_name":"大○商会","shipper_section_code":"ST","shipper_section_name":"埼玉センター","shipper_contact":null,"shipper_phone":"048-000-0000","shipper_address":"埼玉県朝霞市青葉台","shipper_post_code":3510016,"client_id":3,"client_code":"OTSUKA","client_name":"大○商会","client_section_code":"ST","client_section_name":"埼玉センター","client_phone":"048-000-0000","client_address":"埼玉県朝霞市青葉台","client_post_code":3510016,"consignee_code":null,"consignee_name":"渡辺 太郎","consignee_section_code":null,"consignee_section_name":"","consignee_contact":"","consignee_phone":"","consignee_address":"東京都港区西新橋","consignee_post_code":1030005,"carrier_id":1,"carrier_code":"DEMO","carrier_name":"株式会社デモ運輸","carrier_center_code_from":null,"carrier_center_name_from":null,"carrier_center_phone_from":null,"carrier_center_code_to":null,"carrier_center_name_to":null,"billing_id":null,"billing_code":null,"billing_name":null,"billing_section_code":null,"billing_section_name":null,"shipping_code":null,"shipping_name":null,"shipping_section_code":null,"shipping_section_name":null,"shipping_phone":null,"shipping_address_code":null,"shipping_address":null,"destination_code":null,"destination_name":null,"destination_section_code":null,"destination_section_name":null,"destination_contact_code":null,"destination_contact":null,"destination_phone":null,"destination_city_code":null,"destination_address_code":null,"destination_address":null,"destination_post_code":null,"seal_number":null,"seal_num_unit":null,"seal_weight":null,"seal_weight_unit":null,"seal_volume":null,"seal_volume_unit":null,"instruction_no":"'+ instruct_no +'","route_id":1,"transmission_order_no":7,"size":"S","luggage_type":null,"service_type":null,"is_weight":null,"is_contact":null,"is_before_vote":null,"previous_check_time":null,"previous_check_result":null,"base_fare":null,"incidental_charge":null,"insurance_code":null,"insurance_clime":null,"insurance_fee":null,"tax_code":null,"item_price":null,"item_tax":null,"total_fare":null,"total_tax_free":null,"regist_time":null,"regist_check_time":null,"loaded_time":null,"start_time":null,"destination_start_time":null,"pickup_time":null,"delivery_time":null,"return_time":null,"unloaded_time":null,"receipt_time":null,"storage_id":null,"next_status":null,"status":11,"cancel_loading_reason":null,"cancel_loading_reason_text":null,"cancel_reason":null,"cancel_reason_text":null,"cancel_after_reason":null,"cancel_after_reason_text":null,"cancel_process":null,"driver":"' + driver_name + '","proc_date":"' + delivery_date + '","direction":1,"dist_pickup_type":1,"registrant_id":1,"created":null,"modified":"' + created + '"}'
		+ "]";

	return delivery_list;
}

/**
 * 配送指示番号の取得
 * @return	配送指示番号
 */
function get_instruct_no(){
	var instruct_no;
	instruct_no	= "DM";
	instruct_no	+= toLocaleDateString( new Date() );
	instruct_no += "0001";
	return instruct_no;
}

/**
 * 新規伝票登録
 * @param {*} id(in)			ID(路順)
 * @param {*} voucher_no(in)	伝票番号
 */
function get_new_record( id, voucher_no ){
	var date					= new Date();
	var order_date				= toLocaleDateString( date );	
	date.setDate( date.getDate() + 1 );
	var pickup_date				= toLocaleDateString( date );
	date.setDate( date.getDate() + 1 );
	var delivery_date			= toLocaleDateString( date );
	var created					= toLocaleString( new Date() );
	var instruct_no				= get_instruct_no();
	var driver_name				= RunInfo.getDriverName();
	var transmission_order_no	= id;
	//ルートIDは、基本的に情報取得時に配送指示番号と同じI/F(伝票情報取得処理)で受領する
	var route_id				= RunInfo.getRouteId();

	return '{"_id":' + id + ',"order_id":' + id + ',"distribution_id":' + id + ',"order_no":"' + voucher_no + '","order_date":"' + order_date + '","sub_voucher_no":"","voucher_no":"","conveyance_type":"00","pickup_type":null,"transport_type":2,"charge_type":"03","pickup_date":"' + pickup_date + '","pickup_time_order":1,"pickup_time_from":null,"pickup_time_to":null,"pickup_range":null,"pickup_memo":null,"delivery_date":"' + delivery_date + '","delivery_time_order":1,"delivery_time_from":"","delivery_time_to":"","delivery_range":"","delivery_memo":"","handling_memo":null,"additional_memo":null,"special_contents":null,"delivery_no":"","shipment_no":"","req_number":1,"req_num_unit":"PCE","req_weight":null,"req_weight_unit":"KG","req_volume":null,"req_volume_unit":"CM3","req_unit_load":null,"report_number":null,"report_num_unit":"PCE","report_weight":null,"report_weight_unit":"KG","report_volume":null,"report_volume_unit":"CM3","line_no":1,"line_order_no":null,"line_part_number":null,"line_tracking_no":"","line_license_plate_no":null,"line_seq_no":1,"line_branch_no":1,"line_order_control_no":null,"line_owner_product_code":null,"line_contractor_product_code":null,"line_product_name1":null,"line_product_name2":null,"line_product_name":null,"line_unit_load":null,"line_req_number":1,"line_num_unit":"PCE","line_weight":null,"line_weight_unit":"KG","line_volume":null,"line_volume_unit":"CM3","line_quantity":null,"line_quantity_unit":null,"dimensions":null,"package_code":null,"package_name":null,"baggage_handling_memo":null,"line_fares":null,"shipper_code":"","shipper_name":"","shipper_section_code":"","shipper_section_name":"","shipper_contact":null,"shipper_phone":"","shipper_address":"","shipper_post_code":null,"client_id":null,"client_code":"","client_name":"","client_section_code":"","client_section_name":"","client_phone":"","client_address":"","client_post_code":null,"consignee_code":null,"consignee_name":"","consignee_section_code":null,"consignee_section_name":"","consignee_contact":"","consignee_phone":"","consignee_address":"","consignee_post_code":null,"carrier_id":1,"carrier_code":"DEMO","carrier_name":"株式会社デモ運輸","carrier_center_code_from":null,"carrier_center_name_from":null,"carrier_center_phone_from":null,"carrier_center_code_to":null,"carrier_center_name_to":null,"billing_id":null,"billing_code":null,"billing_name":null,"billing_section_code":null,"billing_section_name":null,"shipping_code":null,"shipping_name":null,"shipping_section_code":null,"shipping_section_name":null,"shipping_phone":null,"shipping_address_code":null,"shipping_address":null,"destination_code":null,"destination_name":null,"destination_section_code":null,"destination_section_name":null,"destination_contact_code":null,"destination_contact":null,"destination_phone":null,"destination_city_code":null,"destination_address_code":null,"destination_address":null,"destination_post_code":null,"seal_number":null,"seal_num_unit":null,"seal_weight":null,"seal_weight_unit":null,"seal_volume":null,"seal_volume_unit":null,"instruction_no":"'+ instruct_no +'","route_id":' + route_id + ',"transmission_order_no":' + transmission_order_no + ',"size":"","luggage_type":null,"service_type":null,"is_weight":null,"is_contact":null,"is_before_vote":null,"previous_check_time":null,"previous_check_result":null,"base_fare":null,"incidental_charge":null,"insurance_code":null,"insurance_clime":null,"insurance_fee":null,"tax_code":null,"item_price":null,"item_tax":null,"total_fare":null,"total_tax_free":null,"regist_time":null,"regist_check_time":null,"loaded_time":null,"start_time":null,"destination_start_time":null,"pickup_time":null,"delivery_time":null,"return_time":null,"unloaded_time":null,"receipt_time":null,"storage_id":null,"next_status":null,"status":11,"cancel_loading_reason":null,"cancel_loading_reason_text":null,"cancel_reason":null,"cancel_reason_text":null,"cancel_after_reason":null,"cancel_after_reason_text":null,"cancel_process":null,"driver":"' + driver_name + '","proc_date":"' + delivery_date + '","direction":1,"dist_pickup_type":1,"registrant_id":1,"created":"' + created + '","modified":"' + created + '"}';
}
