package jp.kitoha.ninow.IO.DB.Core;

import android.database.sqlite.SQLiteDatabase;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteOpenHelper;

import jp.kitoha.ninow.Common.Constants;

/******************************************************************************
 * @brief		DB入出力管理クラス
 * @note		SQLite用
 * @author 	KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class DBOpenHelper extends SQLiteOpenHelper {
	//region SQLiteのデータ型
	//TEXT		: テキスト型
	//INTEGER	: 数字型
	//REAL		: 浮動小数点数型
	//BLOB		: バイナリデータ型
	//DATETIME	: 日付時刻型
	//BOOLEAN	: 論理値型(0:False, 1:True)
	//NUMERIC	: 数字型
	//endregion

	/***************************************************************************
	 * インスタンス変数
	 ***************************************************************************/
	//region インスタンス変数
	//mtb_drivers
	String create_mtb_drivers 	= "";
	String drop_mtb_drivers 	= "";
	//mtb_trucks
	String create_mtb_trucks 	= "";
	String drop_mtb_trucks 		= "";
	//mtb_storages
	String create_mtb_storages 	= "";
	String drop_mtb_storages 	= "";
	//dtb_orders
	String create_dtb_orders 	= "";
	String drop_dtb_orders 		= "";
	//dtb_transport_records
	String create_dtb_transport_records	= "";
	String drop_dtb_transport_records	= "";
	//dtb_images
	String create_dtb_images	= "";
	String drop_dtb_images		= "";
	//dtb_medias
	String create_dtb_medias	= "";
	String drop_dtb_medias		= "";
	//endregion

	/***************************************************************************
	 * メソッド
	 ***************************************************************************/
	//region コンストラクタ
	/**
	 * @brief	コンストラクタ
	 * @param	context
	 */
	public DBOpenHelper(Context context){
		super( context, Constants.DB_NAME, null, Constants.DB_VERSION );

		//テーブルの設定
		set_tables();
	}

	/**
	 * @brief	コンストラクタ
	 * @param	context
	 * @param	name
	 * @param	factory
	 * @param	version
	 */
	public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super( context, name, factory, version );

		//テーブルの設定
		set_tables();
	}

	/**
	 * @brief	コンストラクタ
	 * @param	context
	 * @param	name
	 * @param	factory
	 * @param	version
	 * @param	errorHandler
	 */
	public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
		super( context, name, factory, version, errorHandler );

		//テーブルの設定
		set_tables();
	}
	//endregion

	//region テーブル定義
	/**
	 * テーブルの設定
	 */
	private void set_tables(){
		//SQL文の設定
		set_mtb_drivers();
		set_mtb_trucks();
		set_mtb_storages();
		set_dtb_orders();
		set_dtb_transport_recodes();
		set_dtb_images();
		set_dtb_medias();
	}

	/**
	 * ドライバーテーブルのSQL作成(CREATE)
	 */
	private void set_mtb_drivers(){
		//CREATE文
		create_mtb_drivers 	= "CREATE TABLE " + Constants.TBL_MTB_DRIVERS + " ( " +
				"`_id`						INTEGER PRIMARY KEY AUTOINCREMENT, " +			//ID
				"`company_id`				INTEGER," +										//企業ID
				"`base_id`					INTEGER," +										//拠点ID
				"`partner_id`				INTEGER," +										//パートナーID
				"`name`						TEXT," +										//ドライバー名
				"`phone`					TEXT," +										//電話番号
				"`seq_no`					INTEGER," +										//表示順
				"`status`					INTEGER," +										//有効/無効
				"`created`					DATETIME, " +									//作成日
				"`modified`					DATETIME, " +									//更新日
				"`registrant_id` TEXT " +													//登録者
				")";
		//DROP文
		drop_mtb_drivers 	= "DROP TABLE " + Constants.TBL_MTB_DRIVERS;
	}

	/**
	 * 車両テーブルのSQL作成(CREATE)
	 */
	private void set_mtb_trucks(){
		//CREATE文
		create_mtb_trucks 	= "CREATE TABLE " + Constants.TBL_MTB_TRUCKS + " ( " +
			"`_id`						INTEGER PRIMARY KEY AUTOINCREMENT, " +			//ID
			"`company_id`				INTEGER," +										//企業ID
			"`base_id`					INTEGER," +										//拠点ID
			"`partner_id`				INTEGER," +										//パートナーID
			"`name`						TEXT," +										//車両名
			"`car_no`					TEXT," +										//車体番号
			"`license_plate_area`		TEXT," +										//ナンバープレート(地域)
			"`license_plate_type`		TEXT," +										//ナンバープレート(分類)
			"`license_plate_kana`		TEXT," +										//ナンバープレート(ひらがな)
			"`license_plate_no`			TEXT," +										//ナンバープレート(番号)
			"`license_plate`			TEXT," +										//ナンバープレート
			"`capacity`					REAL," +										//最大積載量
			"`seq_no`					INTEGER," +										//表示順
			"`status`					INTEGER," +										//有効/無効
			"`created`					DATETIME, " +									//作成日
			"`modified`					DATETIME, " +									//更新日
			"`registrant_id` TEXT " +													//登録者
			")";
		//DROP文
		drop_mtb_trucks 	= "DROP TABLE " + Constants.TBL_MTB_TRUCKS;
	}

	/**
	 * 保管場所テーブルのSQL作成(CREATE)
	 */
	private void set_mtb_storages(){
		//CREATE文
		create_mtb_storages 	= "CREATE TABLE " + Constants.TBL_MTB_STORAGES + " ( " +
			"`_id`						INTEGER PRIMARY KEY AUTOINCREMENT, " +			//ID
			"`company_id`				INTEGER," +										//企業ID
			"`base_id`					INTEGER," +										//拠点ID
			"`shipper_id`				INTEGER," +										//荷主ID
			"`shipper_base_id`			INTEGER," +										//荷主拠点ID
			"`name`						TEXT," +										//保管場所名
			"`code`						TEXT," +										//保管場所コード
			"`seq_no`					INTEGER," +										//表示順
			"`is_check`					INTEGER," +										//確認フラグ
			"`status`					INTEGER," +										//有効/無効
			"`created`					DATETIME, " +									//作成日
			"`modified`					DATETIME, " +									//更新日
			"`registrant_id`			INTEGER" +										//登録者
			")";

		//DROP文
		drop_mtb_storages 	= "DROP TABLE " + Constants.TBL_MTB_STORAGES;
	}

	/**
	 * 依頼テーブルのSQL作成(CREATE)
	 */
	private void set_dtb_orders(){
		//CREATE文
		//配送指示番号, 日付, 配送先番号, 商品名, 数量, 受取人, 郵便番号, 住所, 住所2,
		//積み付け確認, 引渡し確認, 作成日, 更新日, 更新者
		//配送情報テーブル
		create_dtb_orders = "CREATE TABLE " + Constants.TBL_DTB_ORDERS + " ( " +
			"`_id` INTEGER PRIMARY KEY AUTOINCREMENT, " +									//ID
			"`order_id`                     INTEGER," +										//サーバーの依頼ID(dtb_ordersのid)
			"`distribution_id`              INTEGER," +										//サーバーの配送ID(dtb_distributonsのid)
			"`order_no`                     TEXT," +										//jp30001_運送依頼番号
			"`order_date`                   TEXT," +										//jp30003_運送依頼年月日
			"`sub_voucher_no`               TEXT," +										//jp30002_運送送り状番号
			"`voucher_no`                   TEXT," +										//jp30009_共用送り状番号
			"`conveyance_type`              TEXT," +										//jp30051_運送手段コード
			"`pickup_type`                  INTEGER," +										//jp30970_持込区分コード
			"`transport_type`               INTEGER," +										//jp30971_引取区分コード
			"`charge_type`                  TEXT," +										//jp30831_元払着払種別コード
			"`pickup_date`                  TEXT," +										//jp30500_集荷希望日
			"`pickup_time_order`            INTEGER," +										//集荷時時間指定
			"`pickup_time_from`             TEXT," +										//jp30501_集荷希望時刻(FROM)
			"`pickup_time_to`               TEXT," +										//集荷希望時刻(TO)
			"`pickup_range`                 INTEGER," +										//jp30502_集荷希望条件(AM/PM)
			"`pickup_memo`                  TEXT," +										//集荷条件メモ
			"`delivery_date`                TEXT," +										//jp30510_着荷指定日
			"`delivery_time_order`          INTEGER," +										//着荷時時間指定
			"`delivery_time_from`           TEXT," +										//jp30509_着荷指定時刻(FROM)
			"`delivery_time_to`             TEXT," +										//jp30511_着荷指定時刻(TO)
			"`delivery_range`               INTEGER," +										//jp30512_着荷指定時刻条件(AM/PM)
			"`delivery_memo`                TEXT," +										//jp30515_荷届(納入)条件メモ
			"`handling_memo`                TEXT," +										//jp30521_着店荷扱い指示
			"`additional_memo`              TEXT," +										//jp30526_配達付帯作業
			"`special_contents`             TEXT," +										//jp30531_特記内容
			"`delivery_no`                  TEXT," +										//jp00159_納品番号
			"`shipment_no`                  TEXT," +										//jp30005_出荷番号
			"`req_number`                   INTEGER," +										//jp30090_運送梱包総個数(依頼)
			"`req_num_unit`                 TEXT," +										//jp30669_個数単位コード
			"`req_weight`                   REAL," +										//jp30091_運送梱包総重量(依頼)
			"`req_weight_unit`              TEXT," +										//jp30668_重量単位コード
			"`req_volume`                   REAL," +										//jp30092_運送梱包総容積(依頼)
			"`req_volume_unit`              TEXT," +										//jp30667_容積単位コード
			"`req_unit_load`                INTEGER," +										//jp30756_ユニットロード総数(依頼)
			"`report_number`                INTEGER," +										//jp30090_運送梱包総個数(報告)
			"`report_num_unit`              TEXT," +										//jp30669_個数単位コード
			"`report_weight`                REAL," +										//jp30091_運送梱包総重量(報告)
			"`report_weight_unit`           TEXT," +										//jp30668_重量単位コード
			"`report_volume`                REAL," +										//jp30092_運送梱包総容積(報告)
			"`report_volume_unit`           TEXT," +										//jp30667_容積単位コード
			"`line_no`                      TEXT," +										//jp30701_明細番号
			"`line_order_no`                TEXT," +										//jp30027_個別注文番号
			"`line_part_number`             TEXT," +										//jp30600_運送品No(荷送人)
			"`line_tracking_no`             TEXT," +										//jp30601_梱包管理番号(荷送人)
			"`line_license_plate_no`        TEXT," +										//jp30728_ライセンスプレートナンバー
			"`line_seq_no`                  TEXT," +										//jp30705_個口連番
			"`line_branch_no`               TEXT," +										//jp30602_運送送り状番号枝番
			"`line_order_control_no`        TEXT," +										//jp00136_受注者管理番号
			"`line_owner_product_code`      TEXT," +										//jp00024_発注者品名コード
			"`line_contractor_product_code` TEXT," +										//jp30587_受注者品名コード
			"`line_product_name1`           TEXT," +										//jp30606_商品名1
			"`line_product_name2`           TEXT," +										//jp30607_商品名2
			"`line_product_name`            TEXT," +										//jp30640_運送品表記用品名
			"`line_unit_load`               INTEGER," +										//jp30751_ユニットロード数(依頼)
			"`line_req_number`              INTEGER," +										//jp30754_個数(依頼)
			"`line_num_unit`                TEXT," +										//jp30624_個別個数単位コード
			"`line_weight`                  REAL," +										//jp30643_運送梱包重量(依頼)
			"`line_weight_unit`             TEXT," +										//jp30622_個別重量単位コード
			"`line_volume`                  REAL," +										//jp30647_運送梱包容積(依頼)
			"`line_volume_unit`             TEXT," +										//jp30620_個別容積単位コード
			"`line_quantity`                REAL," +										//jp30772_数量(依頼)
			"`line_quantity_unit`           TEXT," +										//jp30771_内容数量単位コード
			"`dimensions`                   TEXT," +										//jp30631_記述式運送梱包寸法
			"`package_code`                 TEXT," +										//jp30612_荷姿コード
			"`package_name`                 TEXT," +										//jp30611_荷姿名
			"`baggage_handling_memo`        TEXT," +										//jp30651_荷物取り扱い条件
			"`line_fares`                   INTEGER," +										//jp30803_梱包管理番号単位運賃
			"`shipper_code`                 TEXT," +										//jp30100_荷送人コード
			"`shipper_name`                 TEXT," +										//jp30102_荷送人名
			"`shipper_section_code`         TEXT," +										//jp30105_荷送人部門コード
			"`shipper_section_name`         TEXT," +										//jp30107_荷送人部門名
			"`shipper_contact`              TEXT," +										//荷送人担当者名
			"`shipper_phone`                TEXT," +										//jp30120_荷送人電話番号
			"`shipper_address`              TEXT," +										//jp30112_荷送人住所
			"`shipper_post_code`            TEXT," +										//jp30113_荷送人郵便番号
			"`client_id`                    INTEGER," +										//運送依頼者ID
			"`client_code`                  TEXT," +										//jp30130_運送依頼者コード
			"`client_name`                  TEXT," +										//jp30132_運送依頼者名
			"`client_section_code`          TEXT," +										//jp30135_運送依頼者部門コード
			"`client_section_name`          TEXT," +										//jp30137_運送依頼者部門名
			"`client_phone`                 TEXT," +										//jp30180_運送依頼者電話番号
			"`client_address`               TEXT," +										//jp30142_運送依頼者住所
			"`client_post_code`             TEXT," +										//jp30143_運送依頼者郵便番号
			"`consignee_code`               TEXT," +										//jp30150_荷受人コード
			"`consignee_name`               TEXT," +										//jp30152_荷受人名
			"`consignee_section_code`       TEXT," +										//jp30155_荷受人部門コード
			"`consignee_section_name`       TEXT," +										//jp30157_荷受人部門名
			"`consignee_contact`            TEXT," +										//jp30167_荷受人担当者名
			"`consignee_phone`              TEXT," +										//jp30170_荷受人電話番号
			"`consignee_address`            TEXT," +										//jp30162_荷受人住所
			"`consignee_post_code`          TEXT," +										//jp30163_荷受人郵便番号
			"`carrier_id`                   INTEGER," +										//運送業者ID
			"`carrier_code`                 TEXT," +										//jp30200_運送事業者コード
			"`carrier_name`                 TEXT," +										//jp30202_運送事業者名
			"`carrier_center_code_from`     TEXT," +										//jp30230_運送事業者拠点コード(FROM)
			"`carrier_center_name_from`     TEXT," +										//jp30232_運送事業者拠点名(FROM)
			"`carrier_center_phone_from`    TEXT," +										//jp30220_運送事業者電話番号(FROM)
			"`carrier_center_code_to`       TEXT," +										//jp30231_運送事業者拠点コード(TO)
			"`carrier_center_name_to`       TEXT," +										//jp30233_運送事業者拠点名(TO)
			"`billing_id`                   INTEGER," +										//運賃請求ID
			"`billing_code`                 TEXT," +										//jp30250_運賃請求コード
			"`billing_name`                 TEXT," +										//jp30252_運賃請求先名
			"`billing_section_code`         TEXT," +										//jp30255_運賃請求先部門コード
			"`billing_section_name`         TEXT," +										//jp30257_運賃請求先部門名
			"`shipping_code`                TEXT," +										//jp30350_出荷場所コード
			"`shipping_name`                TEXT," +										//jp30352_出荷場所名
			"`shipping_section_code`        TEXT," +										//jp30355_出荷場所部門コード
			"`shipping_section_name`        TEXT," +										//jp30357_出荷場所部門名
			"`shipping_phone`               TEXT," +										//jp30370_出荷場所電話番号
			"`shipping_address_code`        TEXT," +										//jp30360_出荷場所住所コード
			"`shipping_address`             TEXT," +										//jp30362_出荷場所住所
			"`destination_code`             TEXT," +										//jp30400_荷届先コード
			"`destination_name`             TEXT," +										//jp30402_荷届先名
			"`destination_section_code`     TEXT," +										//jp30405_荷届先部門コード
			"`destination_section_name`     TEXT," +										//jp30407_荷届先部門名
			"`destination_contact_code`     TEXT," +										//jp30415_荷届先担当者コード
			"`destination_contact`          TEXT," +										//jp30417_荷届先担当者名
			"`destination_phone`            TEXT," +										//jp30420_荷届先電話番号
			"`destination_city_code`        TEXT," +										//jp30284_荷届先市区町村コード
			"`destination_address_code`     TEXT," +										//jp30410_荷届先住所コード
			"`destination_address`          TEXT," +										//jp30412_荷届先住所
			"`destination_post_code`        TEXT," +										//jp30413_荷届先郵便番号
			"`seal_number`                  INTEGER," +										//jp30090_運送梱包総個数(報告確定)
			"`seal_num_unit`                TEXT," +										//jp30669_個数単位コード
			"`seal_weight`                  REAL," +										//jp30091_運送梱包総重量(報告確定)
			"`seal_weight_unit`             TEXT," +										//jp30668_重量単位コード
			"`seal_volume`                  REAL," +										//jp30092_運送梱包総容積(報告確定)
			"`seal_volume_unit`             TEXT," +										//jp30667_容積単位コード
			"`instruction_no`               TEXT," +										//配送指示番号
			"`route_id`                     INTEGER," +										//コースID
			"`transmission_order_no`        INTEGER," +										//路順
			"`size`                         INTEGER," +										//荷物サイズ
			"`luggage_type`                 INTEGER," +										//取扱荷物種別
			"`service_type`                 INTEGER," +										//開梱・梱包区分
			"`is_weight`                    INTEGER," +										//重量物(オプション)
			"`is_contact`                   INTEGER," +										//事前連絡(オプション)
			"`is_before_vote`               INTEGER," +										//事前連絡(オプション)
			"`previous_check_time`          DATETIME," +									//前日配送確認時間
			"`previous_check_result`        INTEGER," +										//前日配送確認結果
			"`base_fare`                    INTEGER," +										//jp30801_基本運賃
			"`incidental_charge`            INTEGER," +										//jp30802_運賃附帯料金
			"`insurance_code`               TEXT," +										//保険有無コード
			"`insurance_clime`              INTEGER," +										//保険金額
			"`insurance_fee`                INTEGER," +										//保険料(運送品価格)
			"`tax_code`                     TEXT," +										//消費税種別コード
			"`item_price`                   INTEGER," +										//品代金
			"`item_tax`                     INTEGER," +										//品代金消費税
			"`total_fare`                   INTEGER," +										//運賃総合計
			"`total_tax_free`               INTEGER," +										//運賃総合計(非課税)
			"`regist_time`                  DATETIME," +									//伝票読取時刻
			"`regist_check_time`            DATETIME," +									//伝票確認時刻
			"`loaded_time`                  DATETIME," +									//積込時刻
			"`is_send_loaded`               INT," +											//積込
			"`start_time`                   DATETIME," +									//出発時刻
			"`destination_start_time`       DATETIME," +									//配送作業開始時刻
			"`pickup_time`                  DATETIME," +									//集荷時刻
			"`delivery_time`                DATETIME," +									//納品時刻
			"`return_time`                  DATETIME," +									//帰還時刻
			"`unloaded_time`                DATETIME," +									//荷降し時刻
			"`receipt_time`                 DATETIME," +									//荷受完了(センター作業者確認)時刻
			"`storage_id`                   INTEGER," +										//保管場所ID
			"`next_status`                  INTEGER," +										//本配送が完了した時に更新するステータス
			"`status`                       INTEGER," +										//配送状況
			"`cancel_loading_reason`        TEXT," +										//積込(納品)キャンセル理由
			"`cancel_loading_reason_text`   TEXT," +										//積込(納品)キャンセル理由テキスト
			"`cancel_reason`                TEXT," +										//現場(納品・引取)キャンセル理由
			"`cancel_reason_text`           TEXT," +										//現場(納品・引取)キャンセル理由テキスト
			"`cancel_after_reason`          TEXT," +										//配送後キャンセル理由
			"`cancel_after_reason_text`     TEXT," +										//配送後キャンセル理由テキスト
			"`cancel_process`               INTEGER," +										//キャンセル処理
			"`driver`                       TEXT," +										//登録者
			"`proc_date`                    TEXT," +										//配送日
			"`direction`                    INTEGER," +										//向き(1=お客様, 2=荷主)
			"`dist_pickup_type`             INTEGER," +										//登録者区分
			"`registrant_id`                INTEGER," +										//登録者(運送会社)
			"`created`                      DATETIME, " +									//作成日
			"`modified`                     DATETIME " +									//更新日
			")";

		//DROP文
		drop_dtb_orders = "DROP TALBE " + Constants.TBL_DTB_ORDERS;
	}

	/**
	 * 配送状況報告テーブルのSQL作成(CREATE)
	 */
	private void set_dtb_transport_recodes(){
		//時間報告テーブル
		//report_type	:
		//	11=業務報告, 12=センター着,
		//	20=指示受信, 21=伝票読込開始, 22=伝票読込終了, 29=指示キャンセル,
		//	30=積込開始, 31=出発
		//	41=作業開始, 42=報告, 43=報告(受領書), 44=返品あり,
		//	61=不在, 62=再配達依頼受付, 63=再配達中
		//	90=荷卸し
		//	71=バッチ終了, 81=業務終了
		//dtb_ordersで処理する対象の30, 41～44, 61～63, 90は、ここでは処理しない
		create_dtb_transport_records = "CREATE TABLE " + Constants.TBL_DTB_TRANSPORT_RECORDES + " ( " +
			"`_id`                          INTEGER PRIMARY KEY AUTOINCREMENT, " +			//ID
			"`proc_date`                    DATE NOT NULL, " +								//処理日
			"`instruct_no`                  TEXT NOT NULL, " +								//配送指示No
			"`report_type`                  INTEGER, " +									//種別(報告種別)
			"`report_time`                  DATETIME, " +									//報告時間
			"`report_gps`                   TEXT, " +										//報告位置
			"`send_flag`                    INTEGER, " +									//報告送信フラグ
			"`created`                      DATETIME, " +									//作成日(出発時刻)
			"`modified`                     DATETIME, " +									//更新日
			"`driver`                       TEXT " +										//登録者
			")";

		//DROP文
		drop_dtb_transport_records = "DROP TALBE " + Constants.TBL_DTB_TRANSPORT_RECORDES;
	}

	/**
	 * 画像テーブルのSQL作成(CREATE)
	 */
	private void set_dtb_images(){
		//撮影画像保存テーブル
		create_dtb_images = "CREATE TABLE " + Constants.TBL_DTB_IMAGES + " ( " +
				"`_id`                          INTEGER PRIMARY KEY AUTOINCREMENT, " +			//ID
				"`order_id`                     INTEGER NOT NULL, " +							//依頼番号(dtb_ordersの_id)
				"`distribution_id`              INTEGER NOT NULL, " +							//配送番号(dtb_distributionsの_id)
				"`order_no`                     TEXT NOT NULL, " +								//依頼番号
				"`voucher_no`                   TEXT, " +										//共通送り状番号
				"`sub_voucher_no`               TEXT NOT NULL, " +								//送り状番号
				"`course_id`                    INTEGER, " +									//コースID
				"`transmission_order_no`        INTEGER, " +									//路順
				"`image_path`                   TEXT, " +										//オリジナルファイルパス
				"`orginal_time`                 DATETIME, " +									//撮影時刻
				"`edit_image_path`              TEXT, " +										//編集後ファイルパス
				"`edit_time`                    DATETIME, " +									//編集時刻
				"`thumbnail`                    TEXT, " +										//サムネイルファイルパス
				"`is_send`                      INT DEFAULT 0, " +								//送信フラグ
				"`created`                      DATETIME, " +									//作成日
				"`modified`                     DATETIME, " +									//更新日
				"`driver`                       TEXT " +										//登録者
				")";

		//DROP文
		drop_dtb_images = "DROP TALBE " + Constants.TBL_DTB_IMAGES;
	}

	/**
	 * 撮影動画テーブルのSQL作成(CREATE)
	 */
	private void set_dtb_medias(){
		//動画、音声保存テーブル
		create_dtb_medias = "CREATE TABLE " + Constants.TBL_DTB_MEDIAS + " ( " +
				"`_id`							INTEGER PRIMARY KEY AUTOINCREMENT, " +			//ID
				"`order_id`						INTEGER NOT NULL, " +							//依頼番号(dtb_ordersの_id)
				"`distribution_id`              INTEGER NOT NULL, " +							//配送番号(dtb_distributionsの_id)
				"`order_no`						TEXT NOT NULL, " +								//依頼番号
				"`voucher_no`					TEXT, " +										//共通送り状番号
				"`sub_voucher_no`				TEXT NOT NULL, " +								//送り状番号
				"`course_id`					INTEGER," +										//コースID
				"`transmission_order_no`		INTEGER," +										//路順
				"`type`							INT, " +										//種別(1=動画, 2=音声)
				"`filepath`						TEXT, " +										//ファイルパス
				"`thumbnail`					TEXT, " +										//サムネイル画像ファイルパス
				"`is_send`						INTEGER, " +									//送信フラグ
				"`created`						DATETIME, " +									//作成日(撮影時刻)
				"`driver`						TEXT " +										//登録者
				")";

		//DROP文
		drop_dtb_medias = "DROP TALBE " + Constants.TBL_DTB_MEDIAS;
	}
	//endregion

	/***************************************************************************
	 * 初期インストール
	 ***************************************************************************/
	/**
	 * @brief	DB Open時にテーブルが存在しない場合の動作
	 * @param	db
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//このバージョンで必要なテーブルを作成する
		db.execSQL( create_mtb_drivers );
		db.execSQL( create_mtb_trucks );
		db.execSQL( create_mtb_storages );
		db.execSQL( create_dtb_orders );
		db.execSQL( create_dtb_transport_records );
		db.execSQL( create_dtb_images );
		db.execSQL( create_dtb_medias );

		//データの登録
	}

	/***************************************************************************
	 * アップデートインストール
	 ***************************************************************************/
	/**
	 * @brief	アップグレード時の動作
	 * @param	db
	 * @param	oldVersion
	 * @param	newVersion
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//新規バージョンにアップグレードする場合
		if( oldVersion >= newVersion ) {
			return;
		}
		//どのバージョンからでも変更できるよう最新のテーブル定義で書き換える
		//場合によっては、oldVersion別に処理を切り分ける必要あり
		switch( oldVersion ) {
			case 1:
				upgrade_1_to_latest( db );
				break;
			default:
				//原則、このルートはないはず
				break;
		}
	}

	/**
	 * @brief	Ver 1 -> Latestへのバージョンアップ
	 * @param db
	 */
	public void upgrade_1_to_latest(SQLiteDatabase db){
		//1.データバックアップ(現在のレコードを取得してファイルに書き込む)

		//2.テーブルの削除
		//db.execSQL( drop_table );

		//3.新規テーブルの作成

		//4.データのリストア
	}
}
