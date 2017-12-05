/*******************************************************************************
 * 定数・変数
 *******************************************************************************/

/*******************************************************************************
 * メソッド
 *******************************************************************************/
var RunInfo = function() {
	//業務情報
	var car_id					= 0;			//車両ID 
	var car_no					= "";			//車両番号(ナンバープレート) 
	var driver_id				= 0;			//運転手ID
	var driver_name				= "";			//運転手名
	var route_id				= 0;			//ルート情報
	var instruct_no				= "";			//配送指示番号
	var current_date			= "";			//配送日(データ取得基準日)
	//各種リスト
	var delivery_list			= "";			//配送先リスト
	var picture_list			= "";			//写真リスト
	var sign					= "";			//電子サイン
	//時間情報
	var work_start_time			= "";			//業務開始報告時間
	var cycle_start_time		= "";			//センター着時間
	var receive_course_time		= "";			//データ受信時間
	var loading_start_time		= "";			//積込開始時間
	var loading_last_time		= "";			//最終積込時間
	var starting_report_time	= "";			//出発時間
	var delivery_start_time		= "";			//配送開始時間
	var delivery_last_time		= "";			//最終配送時間
	var return_time				= "";			//帰社報告時間
	var unloading_start_time	= "";			//荷降ろし開始報告時間
	var unloading_end_time		= "";			//荷降ろし終了報告時間
	var cycle_end_time			= "";			//回旋終了報告時間
	var work_end_time			= "";			//業務終了報告時間
	//件数情報
	var delivery_num			= 0;			//配送件数
	var delivery_comp_num		= 0;			//配送完了件数
	var delivery_absence_num	= 0;			//不在件数
	var delivery_exclude_num	= 0;			//返品件数
	var voucher_num				= 0;			//配送伝票枚数
	var voucher_comp_num		= 0;			//配送完了伝票枚数
	var voucher_absence_num		= 0;			//不在伝票枚数
	var voucher_exclude_num		= 0;			//返品伝票枚数
	//詳細情報(ID情報など)
	var detail_order_info		= "";			//配送依頼情報(詳細ページ)
	//マイページ情報
	var unsend_num				= 0;			//未送信件数
	var unsend_list				= "";			//未送信リスト
	var news					= "";			//お知らせ
	var setting_field			= 0;			//設定項目(マイページから設定する際のページ連携用)

	return {
		/**********************************************************************
		 * 業務情報
		 **********************************************************************/
		//車両ID
		getCarId: function(){
			return car_id;
		},
		setCarId: function(val){
			car_id		= val;
		},
		//車両番号(ナンバープレート)
		getCarNo: function(){
			return car_no;
		},
		setCarNo: function(val){
			car_no		= val;
		},
		//運転手ID
		getDriverId: function(){
			return driver_id;
		},
		setDriverId: function(val){
			driver_id		= val;
		},
		//運転手名
		getDriverName: function(){
			return driver_name;
		},
		setDriverName: function(val){
			driver_name		= val;
		},
		//配送指示番号
		getInstructNo: function(){
			return instruct_no;
		},
		setInstructNo: function(val){
			instruct_no		= val;
		},
		//運転手ID
		getRouteId: function(){
			return route_id;
		},
		setRouteId: function(val){
			route_id		= val;
		},
		//配送指示情報
		getDeliveryList: function(){
			return delivery_list;
		},
		setDeliveryList: function(val){
			delivery_list	= val;
		},
		//配送指示情報
		getPictureList: function(){
			return picture_list;
		},
		setPictureList: function(val){
			picture_list	= val;
		},
		//配送指示情報
		getSign: function(){
			return sign;
		},
		setSign: function(val){
			sign	= val;
		},
		//配送指示情報
		getCurrentDate: function(){
			return current_date;
		},
		setCurrentDate: function(val){
			current_date	= val;
		},

		/**********************************************************************
		 * アプリ設定表示情報
		 **********************************************************************/
		//未送信件数
		getUnsendNum: function(){
			return unsend_num;
		},
		setUnsendNum: function(val){
			unsend_num	= val;
		},
		//未送信リスト
		getUnsendList: function(){
			return unsend_list;
		},
		setUnsendList: function(val){
			unsend_list		= val;
		},
		//お知らせ
		getNews: function(){
			return news;
		},
		setNews: function(val){
			news		= val;
		},
		/**********************************************************************
		 * 時間報告
		 **********************************************************************/
		/**
		 * 業務開始報告時間
		 **/
		getWorkStartTime(){
			return work_start_time;
		},
		setWorkStartTime( val ){
			work_start_time = val;
		},
		/**
		 * センター着時間
		 **/
		getCycleStartTime(){
			return cycle_start_time;
		},
		setCycleStartTime( val ){
			cycle_start_time = val;
		},
		/**
		 * データ受信時間
		 */
		getReceiveCourseTime(){
			return receive_course_time;
		},
		setReceiveCourseTime(val){
			receive_course_time	= val;
		},
		/**
		 * 積込開始時間
		 **/
		getLoadingStartTime(){
			return loading_start_time;
		},
		setLoadingStartTime( val ){
			loading_start_time = val;
		},
		/**
		 * 最終積込時間
		 **/
		getLoadingLastTime(){
			return loading_last_time;
		},
		setLoadingLastTime( val ){
			loading_last_time = val;
		},
		/**
		 * 出発時間
		 **/
		getStartingReportTime(){
			return starting_report_time;
		},
		setStartingReportTime( val ){
			starting_report_time = val;
		},
		/**
		 * 配送開始時間
		 **/
		setDeliveryStartTime(){
			return delivery_start_time;
		},
		getDeliveryStartTime( val ){
			delivery_start_time = val;
		},
		/**
		 * 最終配送時間
		 **/
		getDeliveryLastTime(){
			return delivery_last_time;
		},
		setDeliveryLastTime( val ){
			delivery_last_time = val;
		},
		/**
		 * 帰社報告時間
		 **/
		getReturnTime(){
			return return_time;
		},
		setReturnTime( val ){
			return_time = val;
		},
		/**
		 * 荷降ろし開始報告時間
		 **/
		getUnloadingStartTime(){
			return unloading_start_time;
		},
		setUnloadingStartTime( val ){
			unloading_start_time = val;
		},
		/**
		 * 荷降ろし終了報告時間
		 **/
		getUnloadingEndTime(){
			return unloading_end_time;
		},
		setUnloadingEndTime( val ){
			unloading_end_time = val;
		},
		/**
		 * 回旋終了報告時間
		 **/
		getCycleEndTime(){
			return cycle_end_time;
		},
		setCycleEndTime( val ){
			cycle_end_time = val;
		},
		/**
		 * 業務終了報告時間
		 **/
		getWorkEndTime(){
			return work_end_time;
		},
		setWorkEndTime( val ){
			work_end_time = val;
		},
		/**********************************************************************
		 * 詳細情報(ID情報など)
		 **********************************************************************/
		/**
		 * 配送依頼ID(詳細ページ)
		 **/
		getDetailOrderInfo(){
			return detail_order_info;
		},
		setDetailOrderInfo( val ){
			detail_order_info = val;
		},
		/**********************************************************************
		 * 件数
		 **********************************************************************/
		/**
		 * 配送件数
		 **/
		getDeliveryNum(){
			return delivery_num;
		},
		setDeliveryNum( val ){
			delivery_num = val;
		},
		/**
		 * 配送完了件数
		 **/
		getDeliveryCompNum(){
			return delivery_comp_num;
		},
		setDeliveryCompNum( val ){
			delivery_comp_num = val;
		},
		/**
		 * 不在件数
		 **/
		getDeliveryAbsenceNum(){
			return delivery_absence_num;
		},
		setDeliveryAbsenceNum( val ){
			delivery_absence_num = val;
		},
		/**
		 * 返品件数
		 **/
		getDeliveryExcludeNum(){
			return delivery_exclude_num;
		},
		setDeliveryExcludeNum( val ){
			delivery_exclude_num = val;
		},
		/**
		 * 配送伝票枚数
		 **/
		getVoucherNum(){
			return voucher_num;
		},
		setVoucherNum( val ){
			voucher_num = val;
		},
		/**
		 * 配送完了伝票枚数
		 **/
		getVoucherCompNum(){
			return voucher_comp_num;
		},
		setVoucherCompNum( val ){
			voucher_comp_num = val;
		},
		/**
		 * 不在伝票枚数
		 **/
		getVoucherAbsenceNum(){
			return voucher_absence_num;
		},
		setVoucherAbsenceNum( val ){
			voucher_absence_num = val;
		},
		/**
		 * 返品伝票枚数
		 **/
		getVoucherExcludeNum(){
			return voucher_exclude_num;
		},
		setVoucherExcludeNum( val ){
			voucher_exclude_num = val;
		},
	
		/**********************************************************************
		 * マイページ設定詳細との連携
		 **********************************************************************/
		//設定項目
		getSettingField: function(){
			//OSによってデータの取得元を変える
			var agent		= navigator.userAgent;
			if( ( agent.search( /iPhone/ ) != -1 ) ||
				( agent.search( /iPad/ ) != -1 ) ||
				( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
			}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
				setting_field	= jv_run_info.getSettingField();
			}else{
				setting_field	= LocalStorage.getItem( "run_info_setting_field" );
			}
			return setting_field;
		},
		setSettingField: function(val){
			setting_field	= val;
			//OSによってデータの取得元を変える
			var agent		= navigator.userAgent;
			if( ( agent.search( /iPhone/ ) != -1 ) ||
				( agent.search( /iPad/ ) != -1 ) ||
				( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
			}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
				jv_run_info.setSettingField( setting_field );
			}else{
				LocalStorage.setItem( "run_info_setting_field", setting_field );
			}
		},
		/**
		 * 設定の読み取り
		 */
		read: function(){
			//OSによってデータの取得元を変える
			var agent		= navigator.userAgent;
			if( ( agent.search( /iPhone/ ) != -1 ) ||
				( agent.search( /iPad/ ) != -1 ) ||
				( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
			}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
				car_id					= jv_run_info.getCarId();
				car_no					= jv_run_info.getCarNo();
				driver_id				= jv_run_info.getDriverId();
				driver_name				= jv_run_info.getDriverName();
				instruct_no				= jv_run_info.getInstructNo();
				route_id				= jv_run_info.getRouteId();
				current_date			= jv_run_info.getCurrentDate();
				//アプリ情報
				unsend_num				= jv_run_info.getUnsendNum();
				unsend_list				= jv_run_info.getUnsendList();
				news					= jv_run_info.getNews();
				//各種リスト
				delivery_list			= jv_run_info.getDeliveryList();			//配送先リスト
				picture_list			= jv_run_info.getPictureList();				//写真リスト
				sign					= jv_run_info.getSign();					//電子サイン
				//時間情報
				work_start_time			= jv_run_info.getWorkStartTime();
				cycle_start_time		= jv_run_info.getCycleStartTime();
				receive_course_time		= jv_run_info.getReceiveCourseTime();
				loading_start_time		= jv_run_info.getLoadingStartTime();
				loading_last_time		= jv_run_info.getLoadingLastTime();
				starting_report_time	= jv_run_info.getStartingReportTime();
				delivery_start_time		= jv_run_info.getDeliveryStartTime();
				delivery_last_time		= jv_run_info.getDeliveryLastTime();
				return_time				= jv_run_info.getReturnTime();
				unloading_start_time	= jv_run_info.getUnloadingStartTime();
				unloading_end_time		= jv_run_info.getUnloadingEndTime();
				cycle_end_time			= jv_run_info.getCycleEndTime();
				work_end_time			= jv_run_info.getWorkEndTime();
				//件数情報
				delivery_num			= jv_run_info.getDeliveryNum();
				delivery_comp_num		= jv_run_info.getDeliveryCompNum();
				delivery_absence_num	= jv_run_info.getDeliveryAbsenceNum();
				delivery_exclude_num	= jv_run_info.getDeliveryExcludeNum();
				voucher_num				= jv_run_info.getVoucherNum();
				voucher_comp_num		= jv_run_info.getVoucherCompNum();
				voucher_absence_num		= jv_run_info.getVoucherAbsenceNum();
				voucher_exclude_num		= jv_run_info.getVoucherExcludeNum();
				//詳細情報(ID情報など)
				detail_order_info		= jv_run_info.getDetailOrderInfo();
			}else{
				//値がある場合のみ上書き(それ以外は初期値を使用)
				if( LocalStorage.getItem( "run_info_cur_car_id" ) ){
					car_id			= LocalStorage.getItem( "run_info_cur_car_id" );
				}
				if( LocalStorage.getItem( "run_info_cur_car_no" ) ){
					car_no			= LocalStorage.getItem( "run_info_cur_car_no" );
				}
				if( LocalStorage.getItem( "run_info_cur_driver_id" ) ){
					driver_id		= LocalStorage.getItem( "run_info_cur_driver_id" );
				}
				if( LocalStorage.getItem( "run_info_cur_driver_name" ) ){
					driver_name		= LocalStorage.getItem( "run_info_cur_driver_name" );
				}
				if( LocalStorage.getItem( "run_info_instruct_no" ) ){
					instruct_no		= LocalStorage.getItem( "run_info_instruct_no" );
				}
				if( LocalStorage.getItem( "run_info_route_id" ) ){
					driver_id		= LocalStorage.getItem( "run_info_route_id" );
				}
				if( LocalStorage.getItem( "run_info_current_date" ) ){
					current_date	= LocalStorage.getItem( "run_info_current_date" );
				}
				//アプリ情報
				if( LocalStorage.getItem( "run_info_unsend_num" ) ){
					unsend_num		= LocalStorage.getItem( "run_info_unsend_num" );
				}
				if( LocalStorage.getItem( "run_info_unsend_list" ) ){
					unsend_list		= LocalStorage.getItem( "run_info_unsend_list" );
				}
				if( LocalStorage.getItem( "run_info_news" ) ){
					news			= LocalStorage.getItem( "run_info_news" );
				}
				//各種リスト
				if( LocalStorage.getItem( "run_info_delivery_list" ) ){
					delivery_list		= LocalStorage.getItem( "run_info_delivery_list" );
				}
				if( LocalStorage.getItem( "run_info_picture_list" ) ){
					picture_list		= LocalStorage.getItem( "run_info_picture_list" );
				}
				if( LocalStorage.getItem( "run_info_sign" ) ){
					sign				= LocalStorage.getItem( "run_info_sign" );
				}
				//時間情報
				if( LocalStorage.getItem( "run_info_work_start_time" ) ){
					work_start_time			= LocalStorage.getItem( "run_info_work_start_time" );
				}
				if( LocalStorage.getItem( "run_info_cycle_start_time" ) ){
					cycle_start_time		= LocalStorage.getItem( "run_info_cycle_start_time" );
				}
				if( LocalStorage.getItem( "run_info_receive_course_time" ) ){
					receive_course_time		= LocalStorage.getItem( "run_info_receive_course_time" );
				}
				if( LocalStorage.getItem( "run_info_loading_start_time" ) ){
					loading_start_time		= LocalStorage.getItem( "run_info_loading_start_time" );
				}
				if( LocalStorage.getItem( "run_info_loading_last_time" ) ){
					loading_last_time		= LocalStorage.getItem( "run_info_loading_last_time" );
				}
				if( LocalStorage.getItem( "run_info_starting_report_time" ) ){
					starting_report_time	= LocalStorage.getItem( "run_info_starting_report_time" );
				}
				if( LocalStorage.getItem( "run_info_delivery_start_time" ) ){
					delivery_start_time		= LocalStorage.getItem( "run_info_delivery_start_time" );
				}
				if( LocalStorage.getItem( "run_info_delivery_last_time" ) ){
					delivery_last_time		= LocalStorage.getItem( "run_info_delivery_last_time" );
				}
				if( LocalStorage.getItem( "run_info_return_time" ) ){
					return_time				= LocalStorage.getItem( "run_info_return_time" );
				}
				if( LocalStorage.getItem( "run_info_unloading_start_time" ) ){
					unloading_start_time	= LocalStorage.getItem( "run_info_unloading_start_time" );
				}
				if( LocalStorage.getItem( "run_info_unloading_end_time" ) ){
					unloading_end_time		= LocalStorage.getItem( "run_info_unloading_end_time" );
				}
				if( LocalStorage.getItem( "run_info_cycle_end_time" ) ){
					cycle_end_time			= LocalStorage.getItem( "run_info_cycle_end_time" );
				}
				if( LocalStorage.getItem( "run_info_work_end_time" ) ){
					work_end_time			= LocalStorage.getItem( "run_info_work_end_time" );
				}
				//件数情報
				if( LocalStorage.getItem( "run_info_delivery_num" ) ){
					delivery_num			= LocalStorage.getItem( "run_info_delivery_num" );
				}
				if( LocalStorage.getItem( "run_info_delivery_comp_num" ) ){
					delivery_comp_num		= LocalStorage.getItem( "run_info_delivery_comp_num" );
				}
				if( LocalStorage.getItem( "run_info_delivery_absence_num" ) ){
					delivery_absence_num	= LocalStorage.getItem( "run_info_delivery_absence_num" );
				}
				if( LocalStorage.getItem( "run_info_delivery_exclude_num" ) ){
					delivery_exclude_num	= LocalStorage.getItem( "run_info_delivery_exclude_num" );
				}
				if( LocalStorage.getItem( "run_info_voucher_num" ) ){
					voucher_num				= LocalStorage.getItem( "run_info_voucher_num" );
				}
				if( LocalStorage.getItem( "run_info_voucher_comp_num" ) ){
					voucher_comp_num		= LocalStorage.getItem( "run_info_voucher_comp_num" );
				}
				if( LocalStorage.getItem( "run_info_voucher_absence_num" ) ){
					voucher_absence_num		= LocalStorage.getItem( "run_info_voucher_absence_num" );
				}
				if( LocalStorage.getItem( "run_info_voucher_exclude_num" ) ){
					voucher_exclude_num		= LocalStorage.getItem( "run_info_voucher_exclude_num" );
				}
				//詳細情報(ID情報など)
				if( LocalStorage.getItem( "run_info_detail_order_info" ) ){
					detail_order_info		= LocalStorage.getItem( "run_info_detail_order_info" );
				}
			}
		},
		/**
		 * 設定保存
		 */
		save: function(){
			//OSによってデータの保存先を変える
			var agent		= navigator.userAgent;
			if( ( agent.search( /iPhone/ ) != -1 ) ||
				( agent.search( /iPad/ ) != -1 ) ||
				( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
			}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
				jv_run_info.setCarId( car_id );
				jv_run_info.setCarNo( car_no );
				jv_run_info.setDriverId( driver_id );
				jv_run_info.setDriverName( driver_name );
				jv_run_info.setInstructNo( instruct_no );
				jv_run_info.setRouteId( route_id );
				jv_run_info.setCurrentDate( current_date );
				//アプリ情報
				jv_run_info.setUnsendNum( unsend_num );
				jv_run_info.setUnsendList( unsend_list );
				jv_run_info.setNews( news );
				//各種リスト
				jv_run_info.setDeliveryList( delivery_list );		//配送先リスト
				jv_run_info.setPictureList( picture_list );			//写真リスト
				jv_run_info.setSign( sign );						//電子サイン
				//時間情報
				jv_run_info.setWorkStartTime( work_start_time );
				jv_run_info.setCycleStartTime( cycle_start_time );
				jv_run_info.setReceiveCourseTime( receive_course_time );
				jv_run_info.setLoadingStartTime( loading_start_time );
				jv_run_info.setLoadingLastTime( loading_last_time );
				jv_run_info.setStartingReportTime( starting_report_time );
				jv_run_info.setDeliveryStartTime( delivery_start_time );
				jv_run_info.setDeliveryLastTime( delivery_last_time );
				jv_run_info.setReturnTime( return_time );
				jv_run_info.setUnloadingStartTime( unloading_start_time );
				jv_run_info.setUnloadingEndTime( unloading_end_time );
				jv_run_info.setCycleEndTime( cycle_end_time );
				jv_run_info.setWorkEndTime( work_end_time );
				//件数情報
				jv_run_info.setDeliveryNum( delivery_num );
				jv_run_info.setDeliveryCompNum( delivery_comp_num );
				jv_run_info.setDeliveryAbsenceNum( delivery_absence_num );
				jv_run_info.setDeliveryExcludeNum( delivery_exclude_num );
				jv_run_info.setVoucherNum( voucher_num );
				jv_run_info.setVoucherCompNum( voucher_comp_num );
				jv_run_info.setVoucherAbsenceNum( voucher_absence_num );
				jv_run_info.setVoucherExcludeNum( voucher_exclude_num );
				//詳細情報(ID情報など)
				jv_run_info.setDetailOrderInfo( detail_order_info );
				jv_run_info.save();
			}else{
				LocalStorage.setItem( "run_info_cur_car_id",			car_id );
				LocalStorage.setItem( "run_info_cur_car_no",			car_no );
				LocalStorage.setItem( "run_info_cur_driver_id",			driver_id );
				LocalStorage.setItem( "run_info_cur_driver_name",		driver_name );
				LocalStorage.setItem( "run_info_route_id",				route_id );
				LocalStorage.setItem( "run_info_instruct_no",			instruct_no );
				LocalStorage.getItem( "run_info_current_date",			current_date );
				//アプリ情報
				LocalStorage.setItem( "run_info_unsend_num",			unsend_num );
				LocalStorage.setItem( "run_info_unsend_list",			unsend_list );
				LocalStorage.setItem( "run_info_news",					news );
				//リスト情報
				LocalStorage.setItem( "run_info_delivery_list",			delivery_list );
				LocalStorage.setItem( "run_info_picture_list",			picture_list );
				LocalStorage.setItem( "run_info_sign",					sign );
				//時間情報
				LocalStorage.setItem( "run_info_work_start_time",		work_start_time );
				LocalStorage.setItem( "run_info_cycle_start_time",		cycle_start_time );
				LocalStorage.setItem( "run_info_receive_course_time",	receive_course_time );
				LocalStorage.setItem( "run_info_loading_start_time",	loading_start_time );
				LocalStorage.setItem( "run_info_loading_last_time",		loading_last_time );
				LocalStorage.setItem( "run_info_starting_report_time",	starting_report_time );
				LocalStorage.setItem( "run_info_delivery_start_time",	delivery_start_time );
				LocalStorage.setItem( "run_info_delivery_last_time",	delivery_last_time );
				LocalStorage.setItem( "run_info_return_time",			return_time );
				LocalStorage.setItem( "run_info_unloading_start_time",	unloading_start_time );
				LocalStorage.setItem( "run_info_unloading_end_time",	unloading_end_time );
				LocalStorage.setItem( "run_info_cycle_end_time",		cycle_end_time );
				LocalStorage.setItem( "run_info_work_end_time",			work_end_time );
				//件数情報
				LocalStorage.setItem( "run_info_delivery_num",			delivery_num );
				LocalStorage.setItem( "run_info_delivery_comp_num",		delivery_comp_num );
				LocalStorage.setItem( "run_info_delivery_absence_num",	delivery_absence_num );
				LocalStorage.setItem( "run_info_delivery_exclude_num",	delivery_exclude_num );
				LocalStorage.setItem( "run_info_voucher_num",			voucher_num );
				LocalStorage.setItem( "run_info_voucher_comp_num",		voucher_comp_num );
				LocalStorage.setItem( "run_info_voucher_absence_num",	voucher_absence_num );
				LocalStorage.setItem( "run_info_voucher_exclude_num",	voucher_exclude_num );
				//詳細情報(ID情報など)
				LocalStorage.setItem( "run_info_detail_order_info",		detail_order_info );
			}
		},
		clear: function(){
			//OSによってデータの保存先を変える
			var agent		= navigator.userAgent;
			if( ( agent.search( /iPhone/ ) != -1 ) ||
				( agent.search( /iPad/ ) != -1 ) ||
				( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
			}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
				//Javaの配送情報をクリアする
				jv_run_info.clear();
				jv_run_info.save();
			}else{
				//ルート情報
				LocalStorage.removeItem( "run_info_route_id" );
				//配送指示、未報告リスト、お知らせ
				LocalStorage.removeItem( "run_info_instruct_no" );
				LocalStorage.removeItem( "run_info_current_date" );
				LocalStorage.removeItem( "run_info_unsend_num" );
				LocalStorage.removeItem( "run_info_unsend_list" );
				LocalStorage.removeItem( "run_info_news" );
				//リスト情報
				LocalStorage.removeItem( "run_info_delivery_list" );
				LocalStorage.removeItem( "run_info_picture_list" );
				LocalStorage.removeItem( "run_info_sign" );
				//時間情報
				LocalStorage.removeItem( "run_info_work_start_time" );
				LocalStorage.removeItem( "run_info_cycle_start_time" );
				LocalStorage.removeItem( "run_info_receive_course_time" );
				LocalStorage.removeItem( "run_info_loading_start_time" );
				LocalStorage.removeItem( "run_info_loading_last_time" );
				LocalStorage.removeItem( "run_info_starting_report_time" );
				LocalStorage.removeItem( "run_info_delivery_start_time" );
				LocalStorage.removeItem( "run_info_delivery_last_time" );
				LocalStorage.removeItem( "run_info_return_time" );
				LocalStorage.removeItem( "run_info_unloading_start_time" );
				LocalStorage.removeItem( "run_info_unloading_end_time" );
				LocalStorage.removeItem( "run_info_cycle_end_time" );
				LocalStorage.removeItem( "run_info_work_end_time" );
				//件数情報
				LocalStorage.removeItem( "run_info_delivery_num" );
				LocalStorage.removeItem( "run_info_delivery_comp_num" );
				LocalStorage.removeItem( "run_info_delivery_absence_num" );
				LocalStorage.removeItem( "run_info_delivery_exclude_num" );
				LocalStorage.removeItem( "run_info_voucher_num" );
				LocalStorage.removeItem( "run_info_voucher_comp_num" );
				LocalStorage.removeItem( "run_info_voucher_absence_num" );
				LocalStorage.removeItem( "run_info_voucher_exclude_num" );
				//詳細情報(ID情報など)
				LocalStorage.removeItem( "run_info_detail_order_info" );
			}
		}
	}
}();
