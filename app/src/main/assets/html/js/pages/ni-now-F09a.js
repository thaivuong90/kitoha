/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 7001;

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
	StatusInfo.read();
	StatusInfo.setProgress( 9 );
	StatusInfo.setCurrentScreenId( PAGE_NO );		//ページ番号の保存
	StatusInfo.save();

	RunInfo.read();
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
	RunInfo.setCycleEndTime( toLocaleString( new Date() ) );
	setNum();												//件数情報設定
	RunInfo.save();
	
	//ユーザーエージェントを解析する(OS別に表示情報の取得方法を変更)
	var agent		= navigator.userAgent;
	if( ( agent.search( /iPhone/ ) != -1 ) ||
		( agent.search( /iPad/ ) != -1 ) ||
		( agent.search( /iPod/ ) != -1 ) ){					//iOSの場合
		alert( "回旋終了(iOS)" );
		var next_id	= Transitions.getNextScreenId( PAGE_NO );
		location.href = Transitions.getScreen( next_id );
	}else if( agent.search( /Android/ ) != -1 ){			//Androidの場合
		//Androidの報告処理を呼び出す(処理完了後、自動で次のページをロードする)
		jv_mainapi.postCycleEnd();
	}else{													//Windowsの場合
		//画面遷移のみ
		var next_id	= Transitions.getNextScreenId( PAGE_NO );
		location.href = Transitions.getScreen( next_id );
	}
}

/**
 * @brief	件数情報の設定
 */
function setNum(){
	var delivery_list	= RunInfo.getDeliveryList();	//配送先リスト
	var json			= JSON.parse( delivery_list );
	var max_num			= Object.keys( json ).length;

	//処理で使用する配送先情報
	var name					= "";					//処理中の配送先名
	var status					= 0;					//ステータス
	var pre_address				= "";					//1つ前の配送先住所
	var pre_name				= "";					//1つ前の配送先名
	var is_skip					= false;				//配送先件数の件数加算をスキップする(true=する/false=しない)
	//件数情報
	var delivery_comp_num		= 0;					//完了件数（配送先）
	var delivery_absence_num	= 0;					//不在件数（配送先）
	var delivery_exclude_num	= 0;					//返品件数（配送先）
	var voucher_comp_num		= 0;					//完了件数（伝票＋個口枚数）
	var voucher_absence_num		= 0;					//不在件数（伝票＋個口枚数）
	var voucher_exclude_num		= 0;					//返品件数（伝票＋個口枚数）

	//配送リストの表示
	for( idx = 0; idx < max_num; idx++ ){
		is_skip	= false;
		status	= +json[idx]['status'];
		switch( +json[idx]['transport_type'] ){
			case 1:												//引取
				name = json[idx]['shipper_name'] + " " + json[idx]['shipper_section_name']
					 + json[idx]['shipper_contact'];
				//一致する場合は、カウントしない
				if( ( json[idx]['shipper_address']	!= pre_address ) ||
					( name != pre_name ) ){
					is_skip	= true;
				}

				if( status == 32 ){								//完了
					if( is_skip ){
						delivery_comp_num++;
					}
					voucher_comp_num++;
				}else if( status == 60 ){						//不在
					if( is_skip ){
						delivery_absence_num++;
					}
					voucher_absence_num++;
				}else if( ( status >= 90 ) ){					//返品(キャンセル)
					if( is_skip ){
						delivery_exclude_num++;
					}
					voucher_exclude_num++;
				}

				//新しい配送先を記録する
				pre_address		= json[idx]['shipper_address'];
				pre_name		= name;

				break;
			case 2:												//納品
				name = json[idx]['consignee_name'] + " " + json[idx]['consignee_section_name']
					 + json[idx]['consignee_contact'];
				//一致する場合は、カウントしない
				if( ( json[idx]['consignee_address']	!= pre_address ) ||
					( name != pre_name ) ){
					is_skip	= true;
				}

			   if( ( status == 42 ) || ( status == 43 ) ){		//完了
					if( is_skip ){
						delivery_comp_num++;
					}
					voucher_comp_num++;
				}else if( status == 60 ){						//不在
					if( is_skip ){
						delivery_absence_num++;
					}
					voucher_absence_num++;
				}else if( ( status == 45 ) ||
						  ( status == 46 ) ||
						  ( status >= 90 ) ){					//返品(キャンセル)
					if( is_skip ){
						delivery_exclude_num++;
					}
					voucher_exclude_num++;
				}

				//新しい配送先を記録する
				pre_address		= json[idx]['consignee_address'];
				pre_name		= json[idx]['consignee_name'];

				break;
		}
	}
	RunInfo.setDeliveryCompNum( delivery_comp_num );
	RunInfo.setDeliveryAbsenceNum( delivery_absence_num );
	RunInfo.setDeliveryExcludeNum( delivery_exclude_num );
	RunInfo.setVoucherCompNum( voucher_comp_num );
	RunInfo.setVoucherAbsenceNum( voucher_absence_num );
	RunInfo.setVoucherExcludeNum( voucher_exclude_num );
}


/*******************************************************************************
 * デモデータの設定
 *******************************************************************************/
