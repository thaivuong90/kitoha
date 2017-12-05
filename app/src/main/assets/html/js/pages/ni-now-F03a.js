/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 2101;

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
	RunInfo.read();
	StatusInfo.read();
	StatusInfo.setProgress( 3 );
	StatusInfo.setCurrentScreenId( PAGE_NO );		//ページ番号の保存
	StatusInfo.save();
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
 * @brief	報告ボタン押下イベント
 * @note	
 **/
function onReportButtonClick(){
	//ボタンの無効化
	setButtonEnabled( "report_button", 2, false );
	
	//送信する値を設定する
	RunInfo.setCurrentDate( toLocaleDateString2( new Date() ) );
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
		jv_mainapi.getCourseInfo();
	}else{													//Windowsの場合
		//Androidの通信処理で取得する情報を設定する
		RunInfo.setInstructNo( get_instruct_no() );
		course_info		= get_course_info();
		RunInfo.setDeliveryList( course_info );				//配送リスト(納品／引取含む)
		setDeliveryInfo( course_info );
	}
}

/**
 * 件数情報情報を設定する
 * @param {*} course_info(in)	JSON文字列
 **/
function setDeliveryInfo( course_info ){
	//JSONから件数を取得する
	RunInfo.setDeliveryNum( getDeliveryNum( course_info ) );
	RunInfo.setVoucherNum( getVoucherNum( course_info ) );
	RunInfo.save();

	//画面遷移
	var next_id	= Transitions.getNextScreenId( PAGE_NO );
	location.href = Transitions.getScreen( next_id );
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
	var num		= 0;

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

/**
 * @brief	メッセージ表示
 */
function showMessage(){
	swal( '警告', '配送情報がありませんでした。センターの事務員に割当て状況を確認してください。', 'error' );
}

/*******************************************************************************
 * デモデータの設定
 *******************************************************************************/
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
