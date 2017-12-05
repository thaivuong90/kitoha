package jp.kitoha.ninow.Common;

/******************************************************************************
 * @brief		定数定義用クラス
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class Constants {
	/**************************************************************************
	 * 定数(アプリ設定)
	 **************************************************************************/
	//region アプリ情報
	public static final String APP_NAME				= "ninow";									//アプリ名(開発コード)
	public static final String NEW_LINE				= System.getProperty( "line.separator" );	//改行コード
	//endregion

	//region デフォルト値の定義
	public static int		DF_MODEL_LOW					= 1;								//エントリーモデル
	public static int		DF_MODEL_MIDDLE					= 2;								//ミドルモデル
	public static int		DF_MODEL_HIGH					= 3;								//ハイエンドモデル
	public static int		DF_EMODE_RECEIVE				= 1;								//配送情報受信
	public static int		DF_EMODE_ENTRY					= 2;								//伝票読取
	public static int		DF_UNUSE						= 0;								//使用しない
	public static int		DF_USE							= 1;								//使用する
	//endregion

	//region 接続情報(ここの情報は、本来は設定画面から取得する)
	public static String	DF_SERVER_URL					= "https://api.ninow.jp/";			//サーバーURL
	public static String	DF_COMPANY_ID					= "ADMIN";							//企業ID
	public static String	DF_USER_ID						= "admin";							//ユーザーID
	public static String	DF_PASSWORD						= "Kitadm01";						//パスワード
	public static int		DF_CAR_ID						= 1;								//車両ID
	public static String	DF_CAR_NO						= "多摩 100 あ 1234";				//ナンバープレート
	public static int		DF_DRIVER_ID					= 1;								//ドライバーID
	public static String	DF_DRIVER_NAME					= "鈴木 太郎";						//ドライバー名
	public static int		DF_MODEL						= DF_MODEL_LOW;						//モデル
	public static int		DF_ENTRY_MODE					= DF_EMODE_ENTRY;					//入力モード
	//endregion

	//region ファイル情報
	public static final String FILE_APPINFO					= "app.properties";					//アプリ設定ファイル
	public static final String FILE_RUNINFO					= "run.properties";					//実行情報ファイル
	public static final String FILE_STATUSINFO				= "status.properties";				//進捗状況ファイル
	public static final String FILE_MODEINFO				= "mode.properties";				//モード情報ファイル
	//endregion

	//region ファイル情報(OLD)
	public static final String SETTING_FILE					= "setting.inf";					//設定ファイル名
	public static final String RECOVERY_FILE				= "recovery.dat";					//リカバリファイル名
	public static final String UPDATE_FILE					= "update.txt";						//配送指示更新ファイル名
	//endregion

	//region DB情報
	/**************************************************************************
	 * 定数(DB定義)
	 **************************************************************************/
	/** DB情報 **/
	public static final int		DB_VERSION					= 1;								//バージョン(x.x.xの形式ではないことに注意!)
	public static final String	DB_NAME						= "data.db";						//DB名
	/** テーブル名 **/
	public static final String	TBL_MTB_STORAGES	    	= "mtb_storages";					//保管場所テーブル名
	public static final String	TBL_MTB_TRUCKS		    	= "mtb_trucks";						//車両テーブル名
	public static final String	TBL_MTB_DRIVERS		    	= "mtb_drivers";					//ドライバーテーブル名
	public static final String	TBL_DTB_ORDERS				= "dtb_orders";						//配送情報テーブル名
	public static final String	TBL_DTB_TRANSPORT_RECORDES	= "dtb_transport_records";			//出発報告テーブル名
	public static final String	TBL_DTB_IMAGES				= "dtb_images";						//撮影画像保存テーブル名
	public static final String	TBL_DTB_MEDIAS				= "dtb_medias";						//動画・音声保存テーブル名
	public static final String	TBL_DTB_IMAGE_REPORTS		= "dtb_image_reports";				//写真報告テーブル名

	/** 設定値(upload_medias type) **/
	public static final int		MEDIA_TYPE_VIDEO			= 1;								//動画
	public static final int		MEDIA_TYPE_SOUND			= 2;								//音声
	//endregion

	//region 保存画像のサイズ
	/**************************************************************************
	 * 保存画像のサイズ
	 **************************************************************************/
	/** 保存画像のサイズ(高画質) **/
	public static final int		DEFAULT_IMAGE_WIDTH			= 1080;								//保存する画像の最大横幅
	public static final int		DEFAULT_IMAGE_HEIGHT		= 1920;								//保存する画像の最大縦幅

	/** 保存画像のサイズ(16:9) **/
	public static final int		SAVE_IMAGE_WIDTH			= 360;								//保存する画像の最大横幅
	public static final int		SAVE_IMAGE_HEIGHT	 		= 640;								//保存する画像の最大縦幅

	/** サムネイル画像のサイズ(正方形) **/
	public static final int		THUMBNAIL_WIDTH				= 200;								//保存するサムネイル画像の横幅
	public static final int		THUMBNAIL_HEIGHT	 		= 200;								//保存するサムネイル画像の縦幅
	//endregion

	//region 設定ファイル情報
	/**************************************************************************
	 * 定数(設定ファイル)
	 **************************************************************************/
	/** 設定ファイル用 **/
	public static final String KEY_SERVER_NAME						= "server_name";				//サーバー名
	public static final String KEY_COMPANY_ID						= "company_id";					//企業ID
	public static final String KEY_USER_NAME						= "user_name";					//ユーザー名
	public static final String KEY_PASSWORD							= "password";					//パスワード
	public static final String KEY_DRIVER_ID						= "driver_id";					//ドライバーID
	public static final String KEY_DRIVER_NAME						= "driver_name";				//ドライバー名
	public static final String KEY_CAR_ID							= "car_id";						//車両ID
	public static final String KEY_CAR_NO							= "car_no";						//車両番号
	public static final String KEY_IS_NEWS							= "is_news";					//お知らせ利用設定
	public static final String KEY_IS_ENTRY							= "is_entry";					//配送情報受信利用設定
	public static final String KEY_IS_PHOTO							= "is_photo";					//写真利用設定
	public static final String KEY_IS_SIGN							= "is_sign";					//電子サイン利用設定
	public static final String KEY_IS_CERT							= "is_cert";					//判取証明利用設定
	public static final String KEY_IS_RETURN						= "is_return";					//帰社報告利用設定
	public static final String KEY_IS_UNLOAD						= "is_unload";					//荷降ろし利用設定
	public static final String KEY_ACCURACY							= "accuracy";					//GPS補正精度

	/* 作業中情報 */
	public static final String KEY_DELIVERY_LIST					= "delivery_list";				//配送先情報
	public static final String KEY_PICTURE_LIST						= "picture_list";				//写真情報
	public static final String KEY_SIGN								= "sign";						//電子サイン情報

	/* 時間 */
	public static final String KEY_WORK_START_TIME					= "work_start_time";			//業務開始時間
	public static final String KEY_CYCLE_START_TIME					= "cycle_start_time";			//センター到着時間
	public static final String KEY_WAIT_TIME						= "wait_time";					//待機時間
	public static final String KEY_RECEIVE_COURSE_TIME				= "receive_course_time";		//配送情報受信時間
	public static final String KEY_LOADING_START_TIME				= "loading_start_time";			//積込開始時間
	public static final String KEY_LOADING_LAST_TIME				= "loading_last_time";			//最終積込時間
	public static final String KEY_STARTING_REPORT_TIME				= "starting_report_time";		//出発報告時間
	public static final String KEY_DELIVERY_START_TIME				= "delivery_start_time";		//配送開始時間
	public static final String KEY_DELIVERY_LAST_TIME				= "delivery_last_time";			//最終配送時間
	public static final String KEY_RETURN_TIME						= "return_time";				//帰社時間
	public static final String KEY_UNLOADING_START_TIME				= "unloading_start_time";		//荷下ろし開始時間
	public static final String KEY_UNLOADING_END_TIME				= "unloading_end_time";			//荷下ろし終了時間
	public static final String KEY_CYCLE_END_TIME					= "cycle_end_time";				//回旋終了報告時間
	public static final String KEY_WORK_END_TIME					= "work_end_time";				//業務終了時間

	/* 件数 */
	public static final String KEY_DELIVERY_NUM						= "delivery_num";				//配送件数
	public static final String KEY_DELIVERY_COMP_NUM				= "delivery_comp_num";			//配送完了件数
	public static final String KEY_DELIVERY_ABSENCE_NUM				= "delivery_absence_num";		//不在件数
	public static final String KEY_DELIVERY_EXCLUDE_NUM				= "delivery_exclude_num";		//返品件数
	public static final String KEY_VOUCHER_NUM						= "voucher_num";				//配送伝票数
	public static final String KEY_VOUCHER_COMP_NUM					= "voucher_comp_num";			//配送完了伝票数
	public static final String KEY_VOUCHER_ABSENCE_NUM				= "voucher_absence_num";		//不在伝票数
	public static final String KEY_VOUCHER_EXCLUDE_NUM				= "voucher_exclude_num";		//返品伝票数

	/* 実行情報用 */
	public static final String KEY_UNSEND_NUM						= "unsend_num";					//未送信件数
	public static final String KEY_UNSEND_LIST						= "unsend_list";				//未送信リスト
	public static final String KEY_NEWS								= "news";						//ニュースリスト
	public static final String KEY_CURRENT_DATE						= "current_date";				//処理日
	public static final String KEY_DETAIL_ORDER_INFO				= "detail_order_info";			//配送依頼詳細情報
	/* 進捗情報用 */
	public static final String KEY_PROGRESS							= "progress";					//進捗
	public static final String KEY_CURRENT_SCR_ID					= "current_scr_id";				//現在の画面ID
	public static final String KEY_PREV_SCR_ID						= "prev_scr_id";				//直前の画面ID
	/* モード */
	public static final String KEY_DEMO								= "demo_mode";					//デモモード

	//region バーコードの読込値
	/**************************************************************************
	 * 定数(バーコード)
	 **************************************************************************/
	/** 読み取り値 **/
	public static final String	INPUT_CODE_1				= "1";								//1を読み込み
	public static final String	INPUT_CODE_2				= "2";								//2を読み込み
	public static final String	INPUT_CODE_3				= "3";								//3を読み込み
	public static final String	INPUT_CODE_4				= "4";								//4を読み込み
	public static final String	INPUT_CODE_5				= "5";								//5を読み込み
	public static final String	INPUT_CODE_6				= "6";								//6を読み込み
	public static final String	INPUT_CODE_7				= "7";								//7を読み込み
	public static final String	INPUT_CODE_8				= "8";								//8を読み込み
	public static final String	INPUT_CODE_9				= "9";								//9を読み込み
	public static final String	INPUT_CODE_10				= "10";								//10を読み込み
	public static final String	INPUT_CODE_20				= "20";								//20を読み込み
	public static final String	INPUT_CODE_30				= "30";								//30を読み込み
	public static final String	INPUT_CODE_50				= "50";								//50を読み込み
	public static final String	INPUT_CODE_0				= "0";								//0を読み込み

	/** KeyCode判定用 **/
	public static final int		KEY_CODE_INT_MIN			= 7;								//読込値の最低値(0:数字)
	public static final int		KEY_CODE_INT_MAX			= 16;								//読込値の最大値(9:数字)
	public static final int		KEY_CODE_STR_MIN			= 29;								//読込値の最大値(A:アルファベット)
	public static final int		KEY_CODE_STR_MAX			= 54;								//読込値の最大値(Z:アルファベット)
//	public static final int		KEY_CODE_COMMA				= 55;								//読込値(,:カンマ)
	public static final int		KEY_CODE_PERIOD				= 56;								//読込値(.:ピリオド)
	public static final int		KEY_CODE_SPACE				= 62;								//読込値( :空白)
	public static final int		KEY_CODE_SLASH				= 76;								//読込値(/:スラッシュ)
	public static final int		KEY_CODE_AT					= 77;								//読込値( :AT)
	//endregion

	/**************************************************************************
	 * メソッド
	 **************************************************************************/
	/***
	 * @brief	コンストラクタ
	 */
	private Constants(){};
}
