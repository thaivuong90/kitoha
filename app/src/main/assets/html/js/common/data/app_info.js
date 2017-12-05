/*******************************************************************************
 * 定数・変数
 *******************************************************************************/

/*******************************************************************************
 * メソッド
 *******************************************************************************/
var AppInfo = function() {
	//永続データ
	var server_name			= "https://api.ninow.jp/";			//サーバーURL 
	var company_id			= "";				//企業ID
	var user_name			= "";				//ユーザー名 
	var password			= "";				//パスワード
	var driver_id			= 0;				//運転手ID
	var driver_name			= "";				//運転手名
	var car_id				= 0;				//車両ID：デフォルト値
	var car_no				= "";				//車両番号(ナンバープレート)：デフォルト値
	//画面選択オプション
	var is_news				= 1;				//お知らせ
	var is_entry			= 1;				//配送情報受信
	var is_photo			= 1;				//写真
	var is_sign				= 1;				//電子サイン
	var is_cert				= 0;				//判取証明
	var is_return			= 1;				//帰社報告
	var is_unload			= 0;				//荷降ろし
	//画面非表示設定
	//var accuracy			= 100;				//GPS補正精度

	return {
		//サーバーURL
		getServerName: function(){
			return server_name;
		},
		setServerName: function(val){
			server_name	= val;
		},
		//企業ID
		getCompanyId: function(){
			return company_id;
		},
		setCompanyId: function(val){
			company_id	= val;
		},
		//ユーザー名
		getUserName: function(){
			return user_name;
		},
		setUserName: function(val){
			user_name	= val;
		},
		//パスワード
		getPassword: function(){
			return password;
		},
		setPassword: function(val){
			password	= val;
		},

		//車両ID
		getCarId: function(){
			return car_id;
		},
		setCarId: function(val){
			car_id	= val;
		},
		//車両番号(ナンバープレート)
		getCarNo: function(){
			return	car_no;
		},
		setCarNo: function(val){
			car_no	= val;
		},
		//運転手ID
		getDriverId: function(){
			return driver_id;
		},
		setDriverId: function(val){
			driver_id	= val;
		},
		//運転手名
		getDriverName: function(){
			return driver_name;
		},
		setDriverName: function(val){
			driver_name	= val;
		},

		//お知らせ設定
		getIsNews: function(){
			return is_news;
		},
		setIsNews: function(val){
			is_news	= val;
		},
		//配送情報受信設定
		getIsEntry: function(){
			return is_entry;
		},
		setIsEntry: function(val){
			is_entry	= val;
		},
		//写真設定
		getIsPhoto: function(){
			return is_photo;
		},
		setIsPhoto: function(val){
			is_photo	= val;
		},
		//電子サイン設定
		getIsSign: function(){
			return is_sign;
		},
		setIsSign: function(val){
			is_sign	= val;
		},
		//判取証明設定
		getIsCert: function(){
			return is_cert;
		},
		setIsCert: function(val){
			is_cert	= val;
		},
		//帰社報告設定
		getIsReturn: function(){
			return is_return;
		},
		setIsReturn: function(val){
			is_return	= val;
		},
		//荷降ろし設定
		getIsUnload: function(){
			return is_unload;
		},
		setIsUnload: function(val){
			is_unload	= val;
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
				server_name		= jv_app_info.getServerName();
				company_id		= jv_app_info.getCompanyId();
				user_name		= jv_app_info.getUserName();
				password		= jv_app_info.getPassword();
				car_id			= jv_app_info.getCarId();
				car_no			= jv_app_info.getCarNo();
				driver_id		= jv_app_info.getDriverId();
				driver_name		= jv_app_info.getDriverName();

				is_news			= jv_app_info.getIsNews();
				is_entry		= jv_app_info.getIsEntry();
				is_photo		= jv_app_info.getIsPhoto();
				is_sign			= jv_app_info.getIsSign();
				is_cert			= jv_app_info.getIsCert();
				is_return		= jv_app_info.getIsReturn();
				is_unload		= jv_app_info.getIsUnload();
			}else{
				if( LocalStorage.getItem( "app_info_server_name" ) ){
					server_name		= LocalStorage.getItem( "app_info_server_name" );
				}
				if( LocalStorage.getItem( "app_info_company_id" ) ){
					company_id		= LocalStorage.getItem( "app_info_company_id" );
				}
				if( LocalStorage.getItem( "app_info_user_name" ) ){
					user_name		= LocalStorage.getItem( "app_info_user_name" );
				}
				if( LocalStorage.getItem( "app_info_password" ) ){
					password		= LocalStorage.getItem( "app_info_password" );
				}
				if( LocalStorage.getItem( "app_info_car_id" ) ){
					car_id			= LocalStorage.getItem( "app_info_car_id" );
				}
				if( LocalStorage.getItem( "app_info_car_no" ) ){
					car_no			= LocalStorage.getItem( "app_info_car_no" );
				}
				if( LocalStorage.getItem( "app_info_driver_id" ) ){
					driver_id		= LocalStorage.getItem( "app_info_driver_id" );
				}
				if( LocalStorage.getItem( "app_info_driver_name" ) ){
					driver_name		= LocalStorage.getItem( "app_info_driver_name" );
				}

				if( LocalStorage.getItem( "app_info_is_news" ) ){
					is_news			= LocalStorage.getItem( "app_info_is_news" );
				}
				if( LocalStorage.getItem( "app_info_is_entry" ) ){
					is_entry		= LocalStorage.getItem( "app_info_is_entry" );
				}
				if( LocalStorage.getItem( "app_info_is_photo" ) ){
					is_photo		= LocalStorage.getItem( "app_info_is_photo" );
				}
				if( LocalStorage.getItem( "app_info_is_sign" ) ){
					is_sign			= LocalStorage.getItem( "app_info_is_sign" );
				}
				if( LocalStorage.getItem( "app_info_is_cert" ) ){
					is_cert			= LocalStorage.getItem( "app_info_is_cert" );
				}
				if( LocalStorage.getItem( "app_info_is_return" ) ){
					is_return		= LocalStorage.getItem( "app_info_is_return" );
				}
				if( LocalStorage.getItem( "app_info_is_unload" ) ){
					is_unload		= LocalStorage.getItem( "app_info_is_unload" );
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
				jv_app_info.setServerName( server_name );
				jv_app_info.setCompanyId( company_id );
				jv_app_info.setUserName( user_name );
				jv_app_info.setPassword( password );
				jv_app_info.setCarId( car_id );
				jv_app_info.setCarNo( car_no );
				jv_app_info.setDriverId( driver_id );
				jv_app_info.setDriverName( driver_name );

				jv_app_info.setIsNews( is_news );
				jv_app_info.setIsEntry( is_entry );
				jv_app_info.setIsPhoto( is_photo );
				jv_app_info.setIsSign( is_sign );
				jv_app_info.setIsCert( is_cert );
				jv_app_info.setIsReturn( is_return );
				jv_app_info.setIsUnload( is_unload );				
				jv_app_info.save();
			}else{
				LocalStorage.setItem( "app_info_server_name",	server_name );
				LocalStorage.setItem( "app_info_company_id",	company_id );
				LocalStorage.setItem( "app_info_user_name",		user_name );
				LocalStorage.setItem( "app_info_password",		password );
				LocalStorage.setItem( "app_info_car_id",		car_id );
				LocalStorage.setItem( "app_info_car_no",		car_no );
				LocalStorage.setItem( "app_info_driver_id",		driver_id );
				LocalStorage.setItem( "app_info_driver_name",	driver_name );

				LocalStorage.setItem( "app_info_is_news",		is_news );
				LocalStorage.setItem( "app_info_is_entry",		is_entry );
				LocalStorage.setItem( "app_info_is_photo",		is_photo );
				LocalStorage.setItem( "app_info_is_sign",		is_sign );
				LocalStorage.setItem( "app_info_is_cert",		is_cert );
				LocalStorage.setItem( "app_info_is_return",		is_return );
				LocalStorage.setItem( "app_info_is_unload",		is_unload );
			}
		}
	}
}();
