/*******************************************************************************
 * 定数・変数
 *******************************************************************************/

/*******************************************************************************
 * メソッド
 *******************************************************************************/
var StatusInfo = function() {
	var progress			= 1;				//進捗状況
	var current_screen_id	= 1001;				//現在表示画面
	var prev_screen_id		= 1001;				//前回表示画面
	return {
		//進捗状況の取得
		getProgress: function(){
			return progress;
		},
		setProgress: function(val){
			progress	= val;
		},
		//現在表示画面ID
		getCurrentScreenId: function(){
			return current_screen_id;
		},
		setCurrentScreenId: function(val){
			current_screen_id	= val;
		},
		//前回表示画面ID
		getPrevScreenId: function(){
			return prev_screen_id;
		},
		setPrevScreenId: function(val){
			prev_screen_id	= val;
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
				progress			= jv_status_info.getProgress();
				current_screen_id	= jv_status_info.getCurrentScreenId();
				prev_screen_id		= jv_status_info.getPrevScreenId();
			}else{
				//値がある場合のみ上書き(それ以外は初期値を使用)
				if( LocalStorage.getItem( "status_info_progress" ) ){
					progress			= LocalStorage.getItem( "status_info_progress" );
				}
				if( LocalStorage.getItem( "status_info_cur_scr_id" ) ){
					current_screen_id	= LocalStorage.getItem( "status_info_cur_scr_id" );
				}
				if( LocalStorage.getItem( "status_info_prev_scr_id" ) ){
					prev_screen_id		= LocalStorage.getItem( "status_info_prev_scr_id" );
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
				//アプリケーション情報の保存
				jv_status_info.setProgress( progress );
				jv_status_info.setCurrentScreenId( current_screen_id );
				jv_status_info.setPrevScreenId( prev_screen_id );
				jv_status_info.save();
			}else{
				LocalStorage.setItem( "status_info_progress",		progress );
				LocalStorage.setItem( "status_info_cur_scr_id",		current_screen_id );
				LocalStorage.setItem( "status_info_prev_scr_id",	prev_screen_id );
			}
		}
	}
}();
