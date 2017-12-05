/*******************************************************************************
 * 定数・変数
 *******************************************************************************/
var PAGE_NO		= 4002;

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

		var delivery_list	= RunInfo.getDeliveryList();	//配送先リスト
		var json			= JSON.parse( delivery_list );
		var max_num			= Object.keys( json ).length;
		var all_list		= "";							//全件表示
		var uncomp_list		= "";							//未処理表示
		var is_disp			= false;
		//1回前に処理した配送先情報
		var pre_address		= "";
		var pre_name		= "";
		
		//配送リストの表示
		for( idx = 0; idx < max_num; idx++ ){
			is_disp				= false;						//未処理表示フラグ
			var section_class	= "";
			var h2_class		= "";
			var status			= +json[idx]['status'];
			var order_no		= "";
			var name			= "";
			var address			= "";
			switch( +json[idx]['transport_type'] ){
				case 1:				//引取
					//ステータスの反映
					if( ( status == 30 ) || ( status == 31 ) ){					//配送中
						is_disp			= true;
					}else if( ( status == 32 ) || ( status == 33 ) ){			//完了
						section_class	= "complete";
						status_section	= '<div class="status"><img src="images/ico_complete.png" alt=""></div>';
					}else if( ( status == 60 ) || ( status >= 90 ) ){			//不在、キャンセル
						section_class	= "exception";
						status_section	= '<div class="status"><img src="images/ico_exception.png" alt=""></div>';
					}
					//宛先（表示文字数に詰める）
					name		= json[idx]['shipper_name'] + " "
								+ json[idx]['shipper_section_name'] + " "
								+ json[idx]['shipper_contact'];
					//住所（表示文字数に詰める）
					address		= json[idx]['shipper_address'];
					break;
				case 2:				//配送
					//ステータスの反映
					if( ( status == 40 ) || ( status == 41 ) ){					//配送中
						is_disp	= true;
					}else if( ( status == 42 ) || ( status == 43 ) ){			//完了
						section_class	= "complete";
						status_section	= '<div class="status"><img src="images/ico_complete.png" alt=""></div>';
					}else if( ( status == 44 ) || ( status == 45 ) ){			//返品あり
						section_class	= "absence";
						h2_class		= "fo-bold";
						status_section	= '<div class="status"><img src="images/ico_absence.png" alt=""></div>';
					}else if( ( status == 60 ) || ( status >= 90 ) ){			//不在、キャンセル
						section_class	= "exception";
						status_section	= '<div class="status"><img src="images/ico_exception.png" alt=""></div>';
					}
					//宛先（表示文字数に詰める）
					name		= json[idx]['consignee_name'] + " "
								+ json[idx]['consignee_section_name'] + " "
								+ json[idx]['consignee_contact'];
					//住所（表示文字数に詰める）
					address		= json[idx]['consignee_address'];
					break;
				default:
					break;
			}
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

			//1件目の処理では出力見送り、2件目以降で処理する
//			if( ( ( address	!= pre_address )	||
//				( name		!= pre_name ) )		&&
//				( idx		> 0 ) ){
			if( ( address	!= pre_address )	||
				( name		!= pre_name ) ){
				//全件リスト表示
				all_list	+= '<section class="' + section_class + '">';
				all_list	+= '	<div class="inner_contents" onclick="onListClick( ' + ( +json[idx]['id'] ) + ' );">';
				all_list	+= '		<h2 class="' + h2_class + '">' + name.substr( 0, MAX_LENGTH ) + '</h2>';
				all_list	+= '		<p>' + address.substr( 0, MAX_LENGTH ) + '</p>';
				all_list	+= condition;
				all_list	+= '	</div>';
				all_list	+= '</section>';

				//未処理リスト表示
				if( is_disp == true ){
					uncomp_list	+= '<section>';
					uncomp_list	+= '	<div class="inner_contents" onclick="onListClick( ' + ( +json[idx]['id'] ) + ' );">';
					uncomp_list	+= '		<h2 class="' + h2_class + '">' + name.substr( 0, MAX_LENGTH ) + '</h2>';
					uncomp_list	+= '		<p>' + address.substr( 0, MAX_LENGTH ) + '</p>';
					uncomp_list	+= condition;
					uncomp_list	+= '	</div>';
					uncomp_list	+= '</section>';
				}

				//新しい配送先を記録する
				pre_address	= address;
				pre_name	= name;
			}
		}

		//積込予定件数と処理件数が一致している場合は、積付完了ボタン押下可能
		if( uncomp_list == "" ){
			setButtonEnabled( "report_button", 3, true );
		}else{
			setButtonEnabled( "report_button", 3, false );
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
 * @brief	リスト押下イベント
 * @param	id(in)		配送依頼ID
 * @note	
 **/
 function onListClick( id ){
	var delivery_list	= RunInfo.getDeliveryList();			//配送先リスト
	var json			= JSON.parse( delivery_list );
	var max_num			= Object.keys( json ).length
	var info			= "";

	//配送情報リストの取得
	for( idx = 0; idx < max_num; idx++ ){
		//IDが一致する情報を検索
		if( json[idx]['id'] == id ){
			//一致している場合は、このIDが該当するデータ
			info		= json[idx];
			break;
		}	
	}
	//
	//表示したい配送依頼情報を設定
	RunInfo.setDetailOrderInfo( JSON.stringify( info ) );
	RunInfo.save();
	location.href	= Transitions.getScreen( 4101 );
}

 /**
 * @brief	次へボタン押下イベント
 * @note	
 **/
function onNextButtonClick(){
	var next_id		= Transitions.getNextScreenId( PAGE_NO );
	location.href	= Transitions.getScreen( next_id );
}

/*******************************************************************************
 * デモデータの設定
 *******************************************************************************/
