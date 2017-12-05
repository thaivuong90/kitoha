package jp.kitoha.ninow.Network;

import android.graphics.PorterDuff;
import android.text.TextUtils;

import jp.kitoha.ninow.Common.ErrorCode;
import jp.kitoha.ninow.Common.Utility;
import jp.kitoha.ninow.Data.Config.AppInfo;
import jp.kitoha.ninow.Data.Config.ModeInfo;
import jp.kitoha.ninow.Network.Core.HttpUtility;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/******************************************************************************
 * @brief		HTTPコマンドの送信
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/

public class HttpCommand {
	/***************************************************************************
	 * Web API
	 ***************************************************************************/
	//region Web API
	//POST:api_worktimes(業務報告) : type(10:開始, 20:終了)							※1対
	//POST:api_cycle_reports(倉庫作業報告) : type(10:センター着, 20:1バッチ終了)	※1対
	//GET:api_instractions(配送指示受信)											※単体
	//POST:api_loading_reports(積込報告)											※納品のみ
	//POST:api_starting_reports(出発報告)											※帰社報告と対
	//POST:api_deliver_reports(配送報告) : type(1:開始, 2:終了, 3:不在, 4:返品)		※1対
	//POST:api_upload_images(画像アップロード)										※同時:配送報告
	//POST:api_upload_medias(動画アップロード)										※同時:配送報告
	//POST:api_unloading_reports(荷卸報告)											※引取のみ
	//POST:api_return_reports(帰社報告) : type(1:帰社前, 2:帰社)					※帰社前、帰社
	//endregion

	/***************************************************************************
	 * 定数
	 ***************************************************************************/
	//region 定数
	public static final String TYPE_NO_START	= "1";				//開始
	public static final String TYPE_NO_END		= "2";				//終了
	public static final String TYPE_NO_ABSENCE	= "3";				//不在
	public static final String TYPE_NO_REJECT	= "4";				//返品
	//endregion

	/***************************************************************************
	 * インスタンス変数
	 ***************************************************************************/
	//region インスタンス変数
	public int			demo_mode;									//デモモード
	public String		server_name;								//サーバー情報
	public String		company_id;									//会社ID
	public String		user_id;									//ユーザーID
	public String		password;									//パスワード
	public String		car_no;										//車両番号
	public String		driver;										//ドライバー
	private Response	response;									//HTTPレスポンス
	private int			res_code;									//HTTPレスポンスコード
	//endregion

	/***************************************************************************
	 * メソッド(コンストラクタ)
	 ***************************************************************************/
	//region コンストラクタ
	/**
	 * コンストラクタ
	 */
	public HttpCommand(){
	}

	/**
	 * コンストラクタ
	 * @param car_no    車両NO
	 * @param driver    ドライバー
	 */
	public HttpCommand( String car_no, String driver ){
		AppInfo		app_info	= AppInfo.getInstance();
		ModeInfo	mode_info	= ModeInfo.getInstance();

		this.server_name	= app_info.getServerName();
		this.company_id		= app_info.getCompanyId();
		this.user_id		= app_info.getUserName();
		this.password		= app_info.getPassword();
		this.demo_mode		= mode_info.getDemoMode();
		this.car_no			= car_no;
		this.driver			= driver;
	}
	//endregion

	/***************************************************************************
	 * 共通メソッド
	 ***************************************************************************/
	//region 共通メソッド(パラメータチェック)
	/***************************************************************************
	 * 接続チェック
	 ***************************************************************************/
	/**
	 * 接続設定の確認
	 * @return	成否(true=正常, false=異常)
	 */
	public int check_setting_param(){
		int	ret = ErrorCode.STS_OK;

		//サーバーURL
		if( TextUtils.isEmpty( this.server_name ) ){
			ret		= ErrorCode.STS_NG_NO_SERVER_SETTING;
		}
		//企業ID
		if( TextUtils.isEmpty( this.company_id ) ){
			ret		= ErrorCode.STS_NG_NO_SERVER_SETTING;
		}
		//ユーザーID
		if( TextUtils.isEmpty( this.user_id ) ){
			ret		= ErrorCode.STS_NG_NO_SERVER_SETTING;
		}
		//パスワード
		if( TextUtils.isEmpty( this.password ) ){
			ret		= ErrorCode.STS_NG_NO_SERVER_SETTING;
		}

		//デモモードの設定確認
		if( this.demo_mode == 1 ){
			ret		= ErrorCode.STS_DEMO;
		}

		return ret;
	}

	/**
	 * 接続設定の確認
	 * @return	成否(true=正常, false=異常)
	 */
	public int check_param(){
		int	ret = ErrorCode.STS_OK;

		//サーバーURL
		if( TextUtils.isEmpty( this.server_name ) ){
			ret		= ErrorCode.STS_NG_NO_SERVER_SETTING;
		}
		//企業ID
		if( TextUtils.isEmpty( this.company_id ) ){
			ret		= ErrorCode.STS_NG_NO_SERVER_SETTING;
		}
		//ユーザーID
		if( TextUtils.isEmpty( this.user_id ) ){
			ret		= ErrorCode.STS_NG_NO_SERVER_SETTING;
		}
		//パスワード
		if( TextUtils.isEmpty( this.password ) ){
			ret		= ErrorCode.STS_NG_NO_SERVER_SETTING;
		}
		//車両No
		if( TextUtils.isEmpty( this.car_no ) ){
			ret		= ErrorCode.STS_NG_NO_SERVER_SETTING;
		}
		//ドライバー
		if( TextUtils.isEmpty( this.driver ) ){
			ret		= ErrorCode.STS_NG_NO_SERVER_SETTING;
		}

		//デモモードの設定確認
		if( this.demo_mode == 1 ){
			ret		= ErrorCode.STS_DEMO;
		}

		return ret;
	}
	//endregion

	//region 共通メソッド(HTTPリクエスト共通パラメータ設定)
	/***************************************************************************
	 * パラメータの設定
	 ***************************************************************************/
	/**
	 * 共通パラメータ(GET)の設定
	 * @return	GET文字列
	 */
	public String set_get_common_params(){
		//認証項目の設定
		String	param	= "?company_id="	+ this.company_id
						+ "&user_id="		+ this.user_id
						+ "&password="		+ this.password
						+ "&car_no="		+ this.car_no
						+ "&driver="		+ this.driver;
		return param;
	}

	/**
	 * 共通パラメータ(GET)の設定
	 * @param	latitude(in)			緯度
	 * @param	longitude(in)			経度
	 * @param	accuracy(in)			補正範囲(精度)
	 * @return	GET文字列
	 */
	public String set_get_gps_params( double latitude, double longitude, float accuracy ){
		//緯度経度情報の設定
		String param	= "&latitude="		+ String.valueOf( latitude )
						+ "&longitude="		+ String.valueOf( longitude )
						+ "&accuracy="		+ String.valueOf( accuracy );

		return param;
	}

	/**
	 * 共通パラメータ(POST)の設定
	 * @return	FormEncodingBuilder
	*/
	public FormEncodingBuilder set_post_common_params(){
		//認証項目の設定
		FormEncodingBuilder param   = new FormEncodingBuilder()
			.add( "company_id",		this.company_id )
			.add( "user_id",		this.user_id )
			.add( "password",		this.password )
			.add( "car_no",			this.car_no )
			.add( "driver",			this.driver );

		return param;
	}

	/**
	 * 共通パラメータ(POST)の設定
	 * @param 	param(in)				フォームパラメータ
	 * @param	latitude(in)			緯度
	 * @param	longitude(in)			経度
	 * @param	accuracy(in)			補正範囲(精度)
	 * @return	FormEncodingBuilder
	 */
	public FormEncodingBuilder set_post_gps_params( FormEncodingBuilder param, double latitude, double longitude, float accuracy ){
		//緯度経度情報の設定
		param.add( "latitude",		String.valueOf( latitude ) )
			 .add( "longitude",		String.valueOf( longitude ) )
			 .add( "accuracy",		String.valueOf( accuracy ) );

		return param;
	}
	//endregion

	//region 共通メソッド(HTTPレスポンス取得)
	/***************************************************************************
	 * レスポンスの取得
	 ***************************************************************************/
	/**
	 * HTTPレスポンスの取得
	 * @return	HTTPレスポンス
	 */
	public Response get_http_response(){
		return response;
	}

	/**
	 * HTTPレスポンスコードの設定
	 * @return	HTTPレスポンスコード
	 */
	public int get_http_response_code(){
		return this.res_code;
	}
	//endregion

	/**************************************************************************
	 * メソッド(HTTPリクエストのパラメータ設定)
	 **************************************************************************/
	//region 拠点業務(業務開始／終了)
	/**
     * 業務開始・終了報告(POST)
	 * @param	type_no					種別(1=開始, 2=終了)
 	 * @param	latitude(in)		    緯度
	 * @param	longitude(in)		    経度
	 * @param	accuracy(in)		    補正範囲(精度)
	 * @param	current_date			処理日
	 * @param	report_time				報告日時
     * @return
     */
	public int post_work_report( String type_no, double latitude, double longitude, float accuracy,
		String current_date, String report_time ){

		int			ret				= ErrorCode.STS_OK;
		String		url				= this.server_name + "api_worktimes.json";

		//パラメータのチェック
		ret = check_param();
		if( ret == ErrorCode.STS_DEMO ){
			//デモモードは、正常終了扱いとする
			return ErrorCode.STS_OK;
		}else if( ret != ErrorCode.STS_OK ) {
			return ret;
		}

		//リクエストのパラメータ作成
		FormEncodingBuilder param	= set_post_common_params();
		param	= set_post_gps_params( param, latitude, longitude, accuracy );
		RequestBody body    =
			param.add( "type",				type_no )						//業務開始
				 .add( "date",				current_date )					//日付(以降、業務終了までこの日付が基本となる)
				 .add( "report_time",		report_time )					//報告日時
				 .build();

		//リクエストの送信
		this.response	= HttpUtility.post( url, body );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}
	//endregion

	//region センター着／バッチ終了
	/**
	 * センター着・1バッチ終了報告(POST)
	 * @param	type_no					種別(1=開始, 2=終了)
	 * @param	latitude(in)		    緯度
	 * @param	longitude(in)		    経度
	 * @param	accuracy(in)		    補正範囲(精度)
	 * @param	current_date			処理日
	 * @param	report_time				報告日時
	 * @return
	 */
	public int post_cycle_reports( String type_no, double latitude, double longitude, float accuracy,
		String current_date, String report_time ) {

		int			ret			= ErrorCode.STS_OK;
		String		url			= server_name + "api_cycle_reports.json";

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_param();
		if( ret == ErrorCode.STS_DEMO ){
			//デモモードは、正常終了扱いとする
			return ErrorCode.STS_OK;
		}else if( ret != ErrorCode.STS_OK ) {
			return ret;
		}

		//リクエストのパラメータ作成
		FormEncodingBuilder param	= set_post_common_params();
		param	= set_post_gps_params( param, latitude, longitude, accuracy );
		RequestBody			body	=
			param.add( "type",				type_no )						//業務開始
				 .add( "date",				current_date )					//日付(以降、業務終了までこの日付が基本となる)
				 .add( "report_time",		report_time )					//報告日付
				 .build();

		//リクエストの送信
		this.response	= HttpUtility.post( url, body );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}
	//endregion

	//region 配送指示取得／配送指示登録
	/***************************************************************************
	 * 配送指示取得
	 ***************************************************************************/
	/**
	 * 配送指示取得(再取得)(GET)
	 * @param   delivery_date(in)	    配送日
	 * @param   acquire_flag(in)	    取得フラグ( 0=初期値, 1=前日も取得する, 2=当日分のみ取得する )
	 * @param   latitude(in)		    緯度
	 * @param   longitude(in)		    経度
	 * @param   accuracy(in)		    補正範囲(精度)
	 * @return
	 */
	public int get_course_info( String delivery_date, int acquire_flag, double latitude, double longitude, float accuracy ){
		int			ret				= ErrorCode.STS_OK;
		String		url         	= "";
		String		report_time 	= Utility.getNow();

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_param();
		if( ret != ErrorCode.STS_OK ) {
			//メソッドの呼び出し元で処理分岐するため、デモモードも返す
			return ret;
		}

		//URLの設定
		url	= this.server_name + "api_instructs.json"
			+ set_get_common_params() + set_get_gps_params( latitude, longitude, accuracy )
			+ "&date="			+ delivery_date
			+ "&acquire_flag="	+ acquire_flag;

		//リクエストの送信
		this.response		= HttpUtility.get( url );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}

	/***************************************************************************
	 * 配送指示登録
	 ***************************************************************************/
	/**
	 * 伝票情報の取得
	 * @param delivery_date		配送日
	 * @param voucher_no		伝票番号
     * @return
     */
	public int get_voucher_info( String delivery_date, String voucher_no ){
		int			ret				= ErrorCode.STS_OK;
		String		url         	= "";
		String		report_time 	= Utility.getNow();

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_param();
		if( ret != ErrorCode.STS_OK ) {
			//メソッドの呼び出し元で処理分岐するため、デモモードも返す
			return ret;
		}

		//URLの設定
		url	= this.server_name + "api_vouchers.json"
				+ set_get_common_params()
				+ "&date="			+ delivery_date
				+ "&voucher_no="	+ voucher_no;

		//リクエストの送信
		this.response	= HttpUtility.get( url );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}

	/**
	 * コースに伝票を登録する
	 * @param delivery_date		配送日
	 * @param course_id			コースID
	 * @param voucher_no		伝票番号
     * @return
     */
	public int post_course_register_voucher( String delivery_date, int course_id, String voucher_no ){
		int			ret				= ErrorCode.STS_OK;
		String		url			= server_name + "api_register_courses.json";
		String		report_time 	= Utility.getNow();

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_param();
		if( ret != ErrorCode.STS_OK ) {
			//メソッドの呼び出し元で処理分岐するため、デモモードも返す
			return ret;
		}

		//リクエストのパラメータ作成
		FormEncodingBuilder param	= set_post_common_params();
		RequestBody			body	=
			param.add( "delivery_date",		delivery_date )
				 .add( "course_id",			String.valueOf( course_id ) )
				 .add( "voucher_no",		voucher_no )
				 .build();

		//リクエストの送信
		this.response	= HttpUtility.post( url, body );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}
	//endregion

	//region 積付報告～出発
	/**
     * 積付報告(POST)
	 * @param   delivery_date(in)	    配送日
     * @param	instruct_no(in)			配送指示番号
	 * @param 	order_id(in)			依頼ID
	 * @param 	status(in)				ステータス
	 * @param 	except_reason(in)		除外理由ID
	 * @param 	except_reason_text(in)	除外理由
	 * @param   latitude(in)		    緯度
	 * @param   longitude(in)		    経度
	 * @param   accuracy(in)		    補正範囲(精度)
	 * @param	current_date			処理日
	 * @param	report_time				報告日時
	 * @return
     */
    public int post_loading_report( String delivery_date, String instruct_no, int order_id, int status,
		int except_reason, String except_reason_text, double latitude, double longitude, float accuracy,
		String current_date, String report_time ){

		int			ret			= ErrorCode.STS_OK;
		String		url			= server_name + "api_loading_reports.json";

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_param();
		if( ret == ErrorCode.STS_DEMO ){
			//デモモードは、正常終了扱いとする
			return ErrorCode.STS_OK;
		}else if( ret != ErrorCode.STS_OK ) {
			return ret;
		}

		//リクエストのパラメータ作成
		FormEncodingBuilder param	= set_post_common_params();
		param		= set_post_gps_params( param, latitude, longitude, accuracy );
		RequestBody			body	=
			param.add( "delivery_date",			delivery_date )
				 .add( "instruct_no",			instruct_no )
				 .add( "order_id",				String.valueOf( order_id ) )
				 .add( "date",					current_date )					//日付(以降、業務終了までこの日付が基本となる)
				 .add( "report_time",			report_time )
				 .add( "status",				String.valueOf( status ) )
				 .add( "except_reason",			String.valueOf( except_reason ) )
				 .add( "except_reason_text",	except_reason_text )
				 .build();

		//リクエストの送信
		response		= HttpUtility.post( url, body );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}

	/**
     * 出発報告(POST)
	 * @param   delivery_date(in)	    配送日
	 * @param	instruct_no(in)			配送指示番号
	 * @param   latitude(in)		    緯度
	 * @param   longitude(in)		    経度
	 * @param   accuracy(in)		    補正範囲(精度)
	 * @param	current_date			処理日
	 * @param	report_time				報告日時
     * @return
     */
    public int post_starting_report( String delivery_date, String instruct_no, double latitude, double longitude, float accuracy,
		String current_date, String report_time){

		int			ret			= ErrorCode.STS_OK;
		String		url			= server_name + "api_starting_reports";

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_param();
		if( ret == ErrorCode.STS_DEMO ){
			//デモモードは、正常終了扱いとする
			return ErrorCode.STS_OK;
		}else if( ret != ErrorCode.STS_OK ) {
			return ret;
		}

		//リクエストのパラメータ作成
		FormEncodingBuilder param	= set_post_common_params();
		param		= set_post_gps_params( param, latitude, longitude, accuracy );
		RequestBody			body	=
			param.add( "instruct_no",		instruct_no )
				 .add( "date",				current_date )					//日付(以降、業務終了までこの日付が基本となる)
				 .add( "report_time",		report_time )
				 .build();

		//リクエストの送信
		this.response		= HttpUtility.post( url, body );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
    }
	//endregion

	//region 配送先業務
	/***************************************************************************
	 * 開始報告
	 ***************************************************************************/
	/**
	 * 配送作業開始報告(POST)
	 * @param   delivery_date(in)	  		配送日
	 * @param	instruct_no(in)				配送指示番号
	 * @param	transmission_order_no(in)	路順
	 * @param   latitude(in)			    緯度
	 * @param   longitude(in)			    経度
	 * @param   accuracy(in)		    	補正範囲(精度)
	 * @return
	 */
	public int post_delivery_start( String delivery_date, String instruct_no,
		String transmission_order_no, double latitude, double longitude, float accuracy ) {

		int			ret			= ErrorCode.STS_OK;
		String		url			= server_name + "api_delivery_start.json";
		String		report_time	= Utility.getNow();

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_param();
		if( ret == ErrorCode.STS_DEMO ){
			//デモモードは、正常終了扱いとする
			return ErrorCode.STS_OK;
		}else if( ret != ErrorCode.STS_OK ) {
			return ret;
		}

		//リクエストのパラメータ作成
		FormEncodingBuilder param	= set_post_common_params();
		param		= set_post_gps_params( param, latitude, longitude, accuracy );
		RequestBody			body	=
			param.add( "delivery_date",			delivery_date )
				 .add( "instruct_no",			instruct_no )
				 .add( "transmission_order_no",	transmission_order_no )
				 .add( "report_time",			report_time )
				 .build();

		//リクエストの送信
		this.response	= HttpUtility.post( url, body );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}

	/***************************************************************************
	 * 完了報告
	 ***************************************************************************/
	/**
     * 配送作業完了報告(POST)
	 * @param   delivery_date(in)	  		配送日
	 * @param	instruct_no(in)				配送指示番号
	 * @param	voucher_no(in)				伝票番号
	 * @param	type_no(in)					種別(2:終了, 3:返品, 4:不在)
	 * @param	status(in)					ステータス
	 * @param	reject_reason(in)			返品理由
	 * @param   latitude(in)			    緯度
	 * @param   longitude(in)			    経度
	 * @param   accuracy(in)		    	補正範囲(精度)
     * @return
     */
	public int post_delivery_report( String delivery_date, String instruct_no, String voucher_no,
		String type_no, int status, String reject_reason, double latitude, double longitude, float accuracy ) {

		int			ret			= ErrorCode.STS_OK;
		String		url			= server_name + "api_deliver_reports.json";
		String		report_time	= Utility.getNow();

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_param();
		if( ret == ErrorCode.STS_DEMO ){
			//デモモードは、正常終了扱いとする
			return ErrorCode.STS_OK;
		}else if( ret != ErrorCode.STS_OK ) {
			return ret;
		}

		//リクエストのパラメータ作成
		FormEncodingBuilder param	= set_post_common_params();
		param		= set_post_gps_params( param, latitude, longitude, accuracy );
		RequestBody			body	=
			param.add( "delivery_date",		delivery_date )
				 .add( "type",				type_no )						//報告
				 .add( "instruct_no",		instruct_no )
				 .add( "voucher_no",		voucher_no )
				 .add( "status",			String.valueOf( status ) )
				 .add( "reject_reason",		String.valueOf( reject_reason ) )
				 .add( "report_time",		report_time )
				 .build();

		//リクエストの送信
		this.response	= HttpUtility.post( url, body );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}
	//endregion

	//region 画像／動画アップロード
    /**
     * 画像アップロード(POST)
	 * @param   delivery_date(in)	  		配送日
     * @param	instruct_no(in)				配送指示番号
     * @param	transmission_order_no(in)	配送先番号
	 * @param	type(in)					種別(1=報告画像(編集前), 2=報告画像(編集後), 10=電子サイン, 90=非公開画像)
	 * @param	seq_no(in)					画像番号(連番:1～)
	 * @param	image(in)					画像データ
	 * @param	thumbnail(in)				サムネイルデータ
	 * @param   latitude(in)			    緯度
	 * @param   longitude(in)			    経度
	 * @param   accuracy(in)		    	補正範囲(精度)
     * @return
     */
    public int post_images( String delivery_date, String instruct_no, String transmission_order_no,
		int seq_no, String image, String thumbnail, int type, double latitude,
		double longitude, float accuracy ) {

		int			ret			= ErrorCode.STS_OK;
		String		url			= server_name + "api_upload_images.json";
		String		report_time	= Utility.getNow();

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_param();
		if( ret == ErrorCode.STS_DEMO ){
			//デモモードは、正常終了扱いとする
			return ErrorCode.STS_OK;
		}else if( ret != ErrorCode.STS_OK ) {
			return ret;
		}

		//リクエストのパラメータ作成
		FormEncodingBuilder param	= set_post_common_params();
		param		= set_post_gps_params( param, latitude, longitude, accuracy );
		RequestBody			body	=
			param.add( "delivery_date",				delivery_date )
				 .add( "instruct_no",				instruct_no )
				 .add( "transmission_order_no",		transmission_order_no )
				 .add( "type",						String.valueOf( type ) )
				 .add( "seq_no",					String.valueOf( seq_no ) )
				 .add( "image",						image )
				 .add( "thumbnail",					thumbnail )
				 .build();

		//リクエストの送信
		this.response	= HttpUtility.post( url, body );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}

    /**
     * 動画アップロード(POST)
	 * @param   delivery_date(in)	  		配送日
	 * @param	instruct_no(in)				配送指示番号
	 * @param	transmission_order_no(in)	配送先番号
	 * @param	type(in)					種別(1=動画, 2=音声)
	 * @param	file(in)					メディアデータ
	 * @param   latitude(in)			    緯度
	 * @param   longitude(in)			    経度
	 * @param   accuracy(in)		    	補正範囲(精度)
     * @return
     */
    public int post_medias( String delivery_date, String instruct_no, String transmission_order_no,
		int type, String file, double latitude, double longitude, float accuracy ) {

		int			ret			= ErrorCode.STS_OK;
		String		url			= server_name + "api_upload_medias.json";
		String		report_time	= Utility.getNow();

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_param();
		if( ret == ErrorCode.STS_DEMO ){
			//デモモードは、正常終了扱いとする
			return ErrorCode.STS_OK;
		}else if( ret != ErrorCode.STS_OK ) {
			return ret;
		}

		//リクエストのパラメータ作成
		FormEncodingBuilder param	= set_post_common_params();
		param		= set_post_gps_params( param, latitude, longitude, accuracy );
		RequestBody			body	=
			param.add( "delivery_date",				delivery_date )
				 .add( "instruct_no",				instruct_no )
				 .add( "transmission_order_no",		transmission_order_no )
				 .add( "type",						String.valueOf( type ) )
				 .add( "file",						file )
				 .build();

		//リクエストの送信
		this.response	= HttpUtility.post( url, body );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}
	//endregion

	//region 帰社報告
	/**
	 * 帰社報告(POST)
	 * @param	type_no(in)					種別(1=これから帰社します連絡, 2=帰着報告)
	 * @param	instruct_no(in)				配送指示番号
	 * @param   latitude(in)			    緯度
	 * @param   longitude(in)			    経度
	 * @param   accuracy(in)		    	補正範囲(精度)
	 * @param	current_date				配送日
	 * @param	report_time					報告時間
	 * @return
	 */
	public int post_return_report( String type_no, String instruct_no,
		double latitude, double longitude, float accuracy, String current_date, String report_time ){

			int			ret			= ErrorCode.STS_OK;
			String		url			= server_name + "api_return_reports.json";

			//サーバーの設定が未入力の場合は、エラーとする
			ret	= check_param();
			if( ret == ErrorCode.STS_DEMO ){
				//デモモードは、正常終了扱いとする
				return ErrorCode.STS_OK;
			}else if( ret != ErrorCode.STS_OK ) {
				return ret;
			}

			//リクエストのパラメータ作成
			FormEncodingBuilder param	= set_post_common_params();
			param		= set_post_gps_params( param, latitude, longitude, accuracy );
			RequestBody			body	=
				param.add( "instruct_no",				instruct_no )
					 .add( "type",						type_no )
					 .add( "date",						current_date )
					 .add( "report_time",				report_time )
					 .build();

			//リクエストの送信
			this.response	= HttpUtility.post( url, body );
			if( this.response != null ) {
				this.res_code = this.response.code();
				if( this.response.code() != 200 ) {
					ret = ErrorCode.STS_NG;
				}
			}

			return ret;
	}
	//endregion

	//region 荷卸し報告
	/**
	 * 荷卸し報告(POST)
	 * @param	delivery_date(in)			配送日
	 * @param	instruct_no(in)				配送指示番号
	 * @param	voucher_no(in)				伝票番号
	 * @param   latitude(in)			    緯度
	 * @param   longitude(in)			    経度
	 * @param   accuracy(in)		    	補正範囲(精度)
	 * @return
	 */
	public int post_unloading_report( String delivery_date, String instruct_no, String voucher_no,
		int storage_id, double latitude, double longitude, float accuracy ) {

		int			ret			= ErrorCode.STS_OK;
		String		url			= server_name + "api_unloading_reports.json";
		String		report_time	= Utility.getNow();

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_param();
		if( ret == ErrorCode.STS_DEMO ){
			//デモモードは、正常終了扱いとする
			return ErrorCode.STS_OK;
		}else if( ret != ErrorCode.STS_OK ) {
			return ret;
		}

		//リクエストのパラメータ作成
		FormEncodingBuilder param	= set_post_common_params();
		param		= set_post_gps_params( param, latitude, longitude, accuracy );
		RequestBody			body	=
				param.add( "delivery_date",				delivery_date )
						.add( "instruct_no",			instruct_no )
						.add( "voucher_no",				voucher_no )
						.add( "storage_id",				String.valueOf( storage_id ) )
						.build();

		//リクエストの送信
		this.response	= HttpUtility.post( url, body );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}
	//endregion

	//region 配送指示更新
	/**
	 * 09-10-01	配送指示更新(GET)
	 * @param	delivery_date(in)			配送日
	 * @param	instruct_no(in)				配送指示番号
	 * @memo	基本的に配送指示と同じ。既に配送指示が取得済みでなければ行えない
	 * @return
	 */
	public int update_course_info( String delivery_date, String instruct_no ){
		int			ret				= ErrorCode.STS_OK;
		String		url         	= "";
		String		report_time 	= Utility.getNow();

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_param();
		if( ret != ErrorCode.STS_OK ) {
			//メソッドの呼び出し元で処理分岐するため、デモモードも返す
			return ret;
		}

		if( instruct_no.equals( "未取得" ) ){
			//未取得時は処理しない(この処理は行われないはず)
			ret		= ErrorCode.STS_OK;
			return ret;
		}

		//URLの設定
		url	= this.server_name + "api_instructs.json"
				+ set_get_common_params()
				+ "&date=" + delivery_date
				+ "&acquire_flag=3"
				+ "&instruct_no=" + instruct_no;

		//リクエストの送信
		this.response	= HttpUtility.get( url );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}
	//endregion

	//region 設定画面から取得する情報
	/***************************************************************************
	 * 設定画面から取得する情報
	 ***************************************************************************/
	/**
	 * 車両リストの取得
	 * @return
	 */
	public int get_car_list(){
		int			ret				= ErrorCode.STS_OK;
		String		url         	= "";
		String		report_time 	= Utility.getNow();

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_setting_param();
		if( ret != ErrorCode.STS_OK ) {
			//メソッドの呼び出し元で処理分岐するため、デモモードも返す
			return ret;
		}

		//URLの設定
		url	= this.server_name + "api_car_lists.json"
				+ set_get_common_params();

		//リクエストの送信
		this.response	= HttpUtility.get( url );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}

	/**
	 * ドライバーリストの取得
	 * @return
	 */
	public int get_driver_list(){
		int			ret				= ErrorCode.STS_OK;
		String		url         	= "";
		String		report_time 	= Utility.getNow();

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_setting_param();
		if( ret != ErrorCode.STS_OK ) {
			//メソッドの呼び出し元で処理分岐するため、デモモードも返す
			return ret;
		}

		//URLの設定
		url	= this.server_name + "api_driver_lists.json"
				+ set_get_common_params();

		//リクエストの送信
		this.response	= HttpUtility.get( url );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}

	/**
	 * コースリストの取得
	 * @return
	 */
	public int get_course_list(){
		int			ret				= ErrorCode.STS_OK;
		String		url         	= "";
		String		report_time 	= Utility.getNow();

		//サーバーの設定が未入力の場合は、エラーとする
		ret	= check_setting_param();
		if( ret != ErrorCode.STS_OK ) {
			//メソッドの呼び出し元で処理分岐するため、デモモードも返す
			return ret;
		}

		//URLの設定
		url	= this.server_name + "api_course_lists.json"
				+ set_get_common_params();

		//リクエストの送信
		this.response	= HttpUtility.get( url );
		if( this.response != null ) {
			this.res_code = this.response.code();
			if( this.response.code() != 200 ) {
				ret = ErrorCode.STS_NG;
			}
		}

		return ret;
	}
	//endregion
}
