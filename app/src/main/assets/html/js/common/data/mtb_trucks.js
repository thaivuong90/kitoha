/*******************************************************************************
 * 定数・変数
 *******************************************************************************/

/*******************************************************************************
 * メソッド
 *******************************************************************************/
var MtbTrucks = function() {
	//永続データ
	var list			= "";				//ドライバー一覧 
	
	return {
		/**
		 * サーバーURLの取得
		 */
		getList: function(){
			return list;
		},
		/**
		 * サーバーURLの設定
		 */
		setList: function(val){
			list	= val;
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
				list		= jv_run_info.getCarList();
			}else{
				list		= LocalStorage.getItem( "mtb_truck_list" );
			}
		},
		/**
		 * JSONを連想配列に変換
		 * @return	連想配列(JSON)
		 */
		json_parse: function(){
			return JSON.parse( list );			
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
				//アプリケーション情報の保存
				jv_run_info.setCarList( list );
			}else{
				LocalStorage.setItem( "mtb_truck_list",	list );
			}
		}
	}
}();
