/*******************************************************************************
 * 定数・変数
 *******************************************************************************/

/*******************************************************************************
 * メソッド
 *******************************************************************************/
var MtbDrivers = function() {
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
				list		= jv_run_info.getDriverList();
			}else{
				list		= LocalStorage.getItem( "mtb_driver_list" );
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
				jv_run_info.setDriverList( list );
			}else{
				LocalStorage.setItem( "mtb_driver_list",	list );
			}
		}
	}
}();
