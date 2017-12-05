/*******************************************************************************
 * 共通処理(ステータス取得)
 *******************************************************************************/
/**
 * @brief	ステータス文字列の取得
 * @param	status(in)		ステータスID
 **/
function getStatusString( status ){
	var status_list	= new Array();

	status_list[1]	= "依頼受付";
	status_list[10]	= "配車中(伝票読取)";
	status_list[11]	= "配車確認済";
	status_list[20]	= "積込済";
	status_list[30]	= "配送中(引取)";
	status_list[31]	= "作業中(引取)";
	status_list[32]	= "引取完了";
	status_list[40]	= "配送中(納品)";
	status_list[41]	= "作業中(納品)";
	status_list[42]	= "納品完了";
	status_list[43]	= "納品完了(受領書確認済)";
	status_list[45]	= "納品完了(返品あり)";
	status_list[46]	= "納品完了(返品あり：受領書確認済)";
	status_list[50]	= "荷卸し完了";
	status_list[51]	= "籠詰完了";
	status_list[60]	= "不在";
	status_list[61]	= "再配達受付";
	status_list[62]	= "再配達中";
	status_list[90]	= "事前キャンセル";
	status_list[91]	= "現地キャンセル";
	status_list[92]	= "配送後キャンセル";
	
	return status_list[status];
}

/*******************************************************************************
 * 共通処理(ボタン制御)
 *******************************************************************************/
/**
 * @brief	ボタン制御イベント
 * @param	button_name(in)		ボタン名
 * @param	type(in)			タイプ名(1=next,2=report,3=check, 4=camera)
 * @param	is_enable(in)		有効無効(true=有効, false=無効)
 * @note	
 **/
function setButtonEnabled( button_name, type, is_enable ){
	var tag_report_button		= document.getElementById( button_name );	//ボタン名

	var css_types	= new Array();
	css_types[1]	= "common_btn buttom_next_btn inline";
	css_types[2]	= "common_btn bottom_report_btn inline";
	css_types[3]	= "common_btn btn_checked inline";					//積込確認
	css_types[4]	= "common_btn btn_camera inline";					//写真
	css_types[5]	= "common_btn btn_sign inline";						//サイン
	css_types[6]	= "common_btn btn_next inline modal-open";			//作業開始
	css_types[7]	= "common_btn btn_dl inline";						//受信

	var event_names	= new Array();
	event_names[1]	= "onNextButtonClick();";
	event_names[2]	= "onReportButtonClick();";
	event_names[3]	= "onNextButtonClick();";
	event_names[4]	= "onTakePictureButtonClick();";
	event_names[5]	= "onDrawSignButtonClick();";
	event_names[6]	= "onNextButtonClick();";
	event_names[7]	= "onReceiveButtonClick();";
	
	//ボタンの有効化
	if( is_enable == true ){												//有効
		tag_report_button.setAttribute( "onclick", event_names[type] );
		tag_report_button.removeAttribute( "class" );
		tag_report_button.setAttribute( "class", css_types[type] );
	}else{																	//無効
		tag_report_button.removeAttribute( "onclick" );
		tag_report_button.removeAttribute( "class" );
		tag_report_button.setAttribute( "class", css_types[type] + " btn_disable" );
	}
}

/*******************************************************************************
 * 日付のフォーマット変換（区切り文字あり）
 *******************************************************************************/
/**
 * 日付のフォーマット変換
 * @return	日時
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
 * 日付のフォーマット変換
 * @return	日付
 */
function toLocaleDateString( date ){
	return [
		date.getFullYear(),
		date.getMonth() + 1,
		date.getDate()
	].join( '/' );
}

/**
 * 時間のフォーマット変換
 * @return	時間
 */
function toLocaleTimeString( date ){
	return [
		( "0" + date.getHours() ).slice( -2 ),
		( "0" + date.getMinutes() ).slice( -2 ),
		( "0" + date.getSeconds() ).slice( -2 ),
	].join( ':' );
}

/*******************************************************************************
 * 日付のフォーマット変換（区切り文字なし）
 *******************************************************************************/
/**
 * 日付のフォーマット変換
 * @return	日付
 */
function toLocaleString2( date ){
	return [
		date.getFullYear(),
		date.getMonth() + 1,
		date.getDate()
	].join( '' )
	+ [
		( "0" + date.getHours() ).slice( -2 ),
		( "0" + date.getMinutes() ).slice( -2 ),
		( "0" + date.getSeconds() ).slice( -2 ),
	].join( '' );
}

/**
 * 日付のフォーマット変換
 * @return	日付
 */
function toLocaleDateString2( date ){
	return [
		date.getFullYear(),
		date.getMonth() + 1,
		date.getDate()
	].join( '' );
}

/**
 * 時間のフォーマット変換
 * @return	時間
 */
function toLocaleTimeString2( date ){
	return [
		( "0" + date.getHours() ).slice( -2 ),
		( "0" + date.getMinutes() ).slice( -2 ),
		( "0" + date.getSeconds() ).slice( -2 ),
	].join( '' );
}
