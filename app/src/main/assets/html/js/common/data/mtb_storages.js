/*******************************************************************************
 * 定数・変数
 *******************************************************************************/

/*******************************************************************************
 * メソッド
 *******************************************************************************/
var MtbStorages = function() {
	//永続データ
	var list			= "";				//ドライバー一覧 
	var current_id		= 0;				//保管場所ID
	
	return {
		/**
		 * 選択したIDの取得
		 */
		getCurrentId: function(){
			return id;
		},
		/**
		 * 選択したIDの設定
		 */
		setCurrentId: function(val){
			id	= val;
		},	
		/**
		 * リストの取得
		 */
		getList: function(){
			return list;
		},
		/**
		 * リストの設定
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
				list		= jv_run_settings.getStorageList();
				current_id	= jv_run_settings.getStorageId();
			}else{
				list		= LocalStorage.getItem( "mtb_storage_list" );
				current_id	= LocalStorage.getItem( "mtb_storage_current_id" );
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
				jv_run_settings.setStorageList( list );
				jv_run_settings.setStorageId( current_id );
			}else{
				LocalStorage.setItem( "mtb_storage_list",		list );
				LocalStorage.setItem( "mtb_storage_current_id",	current_id );
			}
		}
	}
}();
