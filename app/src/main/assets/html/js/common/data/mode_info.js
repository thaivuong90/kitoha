/*******************************************************************************
 * 定数・変数
 *******************************************************************************/

/*******************************************************************************
 * メソッド
 *******************************************************************************/
var ModeInfo = function() {
	var demo_mode			= 0;									//デモモード
	return {
		//進捗状況の取得
		getDemoMode: function(){
			return demo_mode;
		},
		setDemoMode: function(val){
			demo_mode	= val;
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
				demo_mode		= jv_mode_info.getDemoMode();
			}else{
				//値がある場合のみ上書き(それ以外は初期値を使用)
				if( LocalStorage.getItem( "mode_info_demo_mode" ) ){
					demo_mode	= LocalStorage.getItem( "mode_info_demo_mode" );
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
				jv_mode_info.setDemoMode( demo_mode );
				jv_mode_info.save();
			}else{
				LocalStorage.setItem( "mode_info_demo_mode",		demo_mode );
			}
		}
	}
}();
