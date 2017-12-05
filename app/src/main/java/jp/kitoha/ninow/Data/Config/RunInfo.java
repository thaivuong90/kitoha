package jp.kitoha.ninow.Data.Config;

import android.content.SharedPreferences;
import android.os.Environment;
import android.webkit.JavascriptInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.kitoha.ninow.Common.Constants;
import jp.kitoha.ninow.Common.ErrorCode;

/******************************************************************************
 * @author  KITOHA    N.Endo
 * @brief   実行情報
 * @note    アプリ実行中の一時情報
 * @since 2015 -
 * @copyright (c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class RunInfo {
    //region シングルトン用コード
    /**************************************************************************
     * インスタンス変数
     **************************************************************************/
    public static RunInfo instance = new RunInfo();

    /****************************************************************
     * メソッド
     ****************************************************************/
    /**
     * @brief	インスタンスの取得
     * @return	インスタンス
     */
    public static RunInfo getInstance(){
        return instance;
    }
    //endregion

    //region コンストラクタ等
    /**************************************************************************
     * インスタンス変数
     **************************************************************************/
    //業務情報
    private int     car_id;				            //車両ID
    private int     driver_id;				        //運転手ID
    private String  car_no;			            	//車両番号(ナンバープレート)
    private String  driver_name;			    	//運転手名
    private int     route_id;                       //ルートID
    private String  instruct_no;                    //配送指示番号
    private String  current_date;                   //処理日(業務開始時の時間に従う)

    //リスト情報
    private String  delivery_list;                  //配送情報リスト
    private String  picture_list;                   //画像リスト
    private String  sign;                           //電子サイン

    //時間情報
    private String  work_start_time;				//業務開始報告時間
    private String  cycle_start_time;				//センター着時間
    private String  wait_time;						//待機時間
    private String  receive_course_time;			//データ受信時間
    private String  loading_start_time;				//積込開始時間
    private String  loading_last_time;				//最終積込時間
    private String  starting_report_time;			//出発時間
    private String  delivery_start_time;			//配送開始時間
    private String  delivery_last_time;				//最終配送時間
    private String  return_time;					//帰社報告時間
    private String  unloading_start_time;			//荷降ろし開始報告時間
    private String  unloading_end_time;				//荷降ろし終了報告時間
    private String  cycle_end_time;					//回旋終了報告時間
    private String  work_end_time;					//業務終了報告時間

    //件数情報
    private int		delivery_num;					//配送件数
    private int		delivery_comp_num;				//配送完了件数
    private int		delivery_absence_num;			//不在件数
    private int		delivery_exclude_num;			//返品件数
    private int		voucher_num;					//配送伝票枚数
    private int		voucher_comp_num;				//配送完了伝票枚数
    private int		voucher_absence_num;			//不在伝票枚数
    private int		voucher_exclude_num;			//返品伝票枚数
    //詳細情報(ID情報など)
    private String	detail_order_info;				//配送依頼情報(詳細ページ)

    //入力情報
    private String  entry_barcode;                  //バーコード入力
    //マスター情報
    private String  driver_list;                    //ドライバーリスト
    private String  car_list;                       //車両リスト
    private String  storage_list;                   //保管場所リスト

    //マイページ表示情報
    private int     unsend_num;                     //未送信件数
    private String  unsend_list;		    		//未送信リスト
    private String  news;	            			//お知らせ
    private int     setting_field;				    //設定項目

    //GPS(緯度経度)情報
    private double	latitude;						//緯度
    private double	longitude;						//経度
    private float	accuracy;						//精度

    //コンテキスト(ファイル操作で利用)
    private SharedPreferences	pref;

    /**
     * @brief コンストラクタ
     */
    private RunInfo(){
        //業務情報
        car_id					= 1;
        driver_id				= 1;
        car_no					= "";
        driver_name				= "";
        route_id				= 0;			//ルート情報
        instruct_no				= "";			//配送指示番号
        current_date            = "";           //処理日
        //各種リスト
        delivery_list			= "";			//配送先リスト
        picture_list			= "";			//写真リスト
        sign					= "";			//電子サイン
        //時間情報
        work_start_time			= "";			//業務開始報告時間
        cycle_start_time		= "";			//センター着時間
        wait_time               = "";           //待機時間
        receive_course_time		= "";			//データ受信時間
        loading_start_time		= "";			//積込開始時間
        loading_last_time		= "";			//最終積込時間
        starting_report_time	= "";			//出発時間
        delivery_start_time		= "";			//配送開始時間
        delivery_last_time		= "";			//最終配送時間
        return_time				= "";			//帰社報告時間
        unloading_start_time	= "";			//荷降ろし開始報告時間
        unloading_end_time		= "";			//荷降ろし終了報告時間
        cycle_end_time			= "";			//回旋終了報告時間
        work_end_time			= "";			//業務終了報告時間
        //件数情報
        delivery_num			= 0;			//配送件数
        delivery_comp_num		= 0;			//配送完了件数
        delivery_absence_num	= 0;			//不在件数
        delivery_exclude_num	= 0;			//返品件数
        voucher_num				= 0;			//配送伝票枚数
        voucher_comp_num		= 0;			//配送完了伝票枚数
        voucher_absence_num		= 0;			//不在伝票枚数
        voucher_exclude_num		= 0;			//返品伝票枚数
        //詳細情報(ID情報など)
        detail_order_info		= "";			//配送依頼情報(詳細ページ)
        entry_barcode           = "";
        //マスター情報
        driver_list				= "";
        car_list				= "";
        storage_list			= "";

        //マイページ表示情報
        unsend_num				= 0;
        unsend_list				= "";
        news					= "";
        setting_field			= 1;

        //GPS(緯度経度)情報
        latitude                = 0.0;
        longitude               = 0.0;
        accuracy                = 0;

        //コンテキスト(ファイル操作で利用)
        pref				    = null;
    }

    /**
     * 設定の読み込み
     * @return
     */
    @JavascriptInterface
    public int read(){
        this.current_date       = pref.getString( Constants.KEY_CURRENT_DATE, "" );
        this.car_id             = pref.getInt( Constants.KEY_CAR_ID, 0 );
        this.car_no             = pref.getString( Constants.KEY_CAR_NO, "" );
        this.driver_id          = pref.getInt( Constants.KEY_DRIVER_ID, 0 );
        this.driver_name        = pref.getString( Constants.KEY_DRIVER_NAME, "" );
        this.unsend_num         = pref.getInt( Constants.KEY_UNSEND_NUM, 0 );
        this.unsend_list        = pref.getString( Constants.KEY_UNSEND_LIST, "" );
        this.news               = pref.getString( Constants.KEY_NEWS, "" );

        //作業中情報
        this.delivery_list			= pref.getString( Constants.KEY_DELIVERY_LIST, "" );
        this.picture_list			= pref.getString( Constants.KEY_PICTURE_LIST, "" );
        this.sign					= pref.getString( Constants.KEY_SIGN, "" );
        this.detail_order_info		= pref.getString( Constants.KEY_DETAIL_ORDER_INFO, "" );

        //時間
        this.work_start_time		= pref.getString( Constants.KEY_WORK_START_TIME, "" );
        this.cycle_start_time		= pref.getString( Constants.KEY_CYCLE_START_TIME, "" );
        this.wait_time				= pref.getString( Constants.KEY_WAIT_TIME, "" );
        this.receive_course_time	= pref.getString( Constants.KEY_RECEIVE_COURSE_TIME, "" );
        this.loading_start_time		= pref.getString( Constants.KEY_LOADING_START_TIME, "" );
        this.loading_last_time		= pref.getString( Constants.KEY_LOADING_LAST_TIME, "" );
        this.starting_report_time	= pref.getString( Constants.KEY_STARTING_REPORT_TIME, "" );
        this.delivery_start_time	= pref.getString( Constants.KEY_DELIVERY_START_TIME, "" );
        this.delivery_last_time		= pref.getString( Constants.KEY_DELIVERY_LAST_TIME, "" );
        this.return_time			= pref.getString( Constants.KEY_RETURN_TIME, "" );
        this.unloading_start_time	= pref.getString( Constants.KEY_UNLOADING_START_TIME, "" );
        this.unloading_end_time		= pref.getString( Constants.KEY_UNLOADING_END_TIME, "" );
        this.cycle_end_time			= pref.getString( Constants.KEY_CYCLE_END_TIME, "" );
        this.work_end_time			= pref.getString( Constants.KEY_WORK_END_TIME, "" );

        //件数
        this.delivery_num			= pref.getInt( Constants.KEY_DELIVERY_NUM, 0 );
        this.delivery_comp_num		= pref.getInt( Constants.KEY_DELIVERY_COMP_NUM, 0 );
        this.delivery_absence_num	= pref.getInt( Constants.KEY_DELIVERY_ABSENCE_NUM, 0 );
        this.delivery_exclude_num	= pref.getInt( Constants.KEY_DELIVERY_EXCLUDE_NUM, 0 );
        this.voucher_num			= pref.getInt( Constants.KEY_VOUCHER_NUM, 0 );
        this.voucher_comp_num		= pref.getInt( Constants.KEY_VOUCHER_COMP_NUM, 0 );
        this.voucher_absence_num	= pref.getInt( Constants.KEY_VOUCHER_ABSENCE_NUM, 0 );
        this.voucher_exclude_num	= pref.getInt( Constants.KEY_VOUCHER_EXCLUDE_NUM, 0 );

        return ErrorCode.STS_OK;
    }

    /**
     * 設定の保存
     * @return
     */
    @JavascriptInterface
    public int save(){
        SharedPreferences.Editor    editor  = pref.edit();

        editor.putString( Constants.KEY_CURRENT_DATE,   this.current_date );
        editor.putInt( Constants.KEY_CAR_ID,            this.car_id );
        editor.putString( Constants.KEY_CAR_NO,         this.car_no );
        editor.putInt( Constants.KEY_DRIVER_ID,         this.driver_id );
        editor.putString( Constants.KEY_DRIVER_NAME,    this.driver_name );
        editor.putInt( Constants.KEY_UNSEND_NUM,        this.unsend_num );
        editor.putString( Constants.KEY_UNSEND_LIST,    this.unsend_list );
        editor.putString( Constants.KEY_NEWS,           this.news );

        //作業中情報
        editor.putString( Constants.KEY_DELIVERY_LIST,		this.delivery_list );
        editor.putString( Constants.KEY_PICTURE_LIST,		this.picture_list );
        editor.putString( Constants.KEY_SIGN,				this.sign );
        editor.putString( Constants.KEY_DETAIL_ORDER_INFO,	this.detail_order_info );

        //時間
        editor.putString( Constants.KEY_WORK_START_TIME,		this.work_start_time );
        editor.putString( Constants.KEY_CYCLE_START_TIME,		this.cycle_start_time );
        editor.putString( Constants.KEY_WAIT_TIME,				this.wait_time );
        editor.putString( Constants.KEY_RECEIVE_COURSE_TIME,	this.receive_course_time );
        editor.putString( Constants.KEY_LOADING_START_TIME,		this.loading_start_time );
        editor.putString( Constants.KEY_LOADING_LAST_TIME,		this.loading_last_time );
        editor.putString( Constants.KEY_STARTING_REPORT_TIME,	this.starting_report_time );
        editor.putString( Constants.KEY_DELIVERY_START_TIME,	this.delivery_start_time );
        editor.putString( Constants.KEY_DELIVERY_LAST_TIME,		this.delivery_last_time );
        editor.putString( Constants.KEY_RETURN_TIME,			this.return_time );
        editor.putString( Constants.KEY_UNLOADING_START_TIME,	this.unloading_start_time );
        editor.putString( Constants.KEY_UNLOADING_END_TIME,		this.unloading_end_time );
        editor.putString( Constants.KEY_CYCLE_END_TIME,			this.cycle_end_time );
        editor.putString( Constants.KEY_WORK_END_TIME,			this.work_end_time );

        /* 件数 */
        editor.putInt( Constants.KEY_DELIVERY_NUM,			this.delivery_num );
        editor.putInt( Constants.KEY_DELIVERY_COMP_NUM,		this.delivery_comp_num );
        editor.putInt( Constants.KEY_DELIVERY_ABSENCE_NUM,	this.delivery_absence_num );
        editor.putInt( Constants.KEY_DELIVERY_EXCLUDE_NUM,	this.delivery_exclude_num );
        editor.putInt( Constants.KEY_VOUCHER_NUM,			this.voucher_num );
        editor.putInt( Constants.KEY_VOUCHER_COMP_NUM,		this.voucher_comp_num );
        editor.putInt( Constants.KEY_VOUCHER_ABSENCE_NUM,	this.voucher_absence_num );
        editor.putInt( Constants.KEY_VOUCHER_EXCLUDE_NUM,	this.voucher_exclude_num );

        editor.commit();

        return ErrorCode.STS_OK;
    }

    /**
     * @brief   値のクリア
     * @memo    業務終了時のクリア
     */
     @JavascriptInterface
    public void clear(){
        //業務情報
        route_id				= 0;			//ルート情報
        instruct_no				= "";			//配送指示番号
        current_date            = "";           //処理日
        //各種リスト
        delivery_list			= "";			//配送先リスト
        picture_list			= "";			//写真リスト
        sign					= "";			//電子サイン
        //時間情報
        work_start_time			= "";			//業務開始報告時間
        cycle_start_time		= "";			//センター着時間
        wait_time               = "";           //待機時間
        receive_course_time		= "";			//データ受信時間
        loading_start_time		= "";			//積込開始時間
        loading_last_time		= "";			//最終積込時間
        starting_report_time	= "";			//出発時間
        delivery_start_time		= "";			//配送開始時間
        delivery_last_time		= "";			//最終配送時間
        return_time				= "";			//帰社報告時間
        unloading_start_time	= "";			//荷降ろし開始報告時間
        unloading_end_time		= "";			//荷降ろし終了報告時間
        cycle_end_time			= "";			//回旋終了報告時間
        work_end_time			= "";			//業務終了報告時間
        //件数情報
        delivery_num			= 0;			//配送件数
        delivery_comp_num		= 0;			//配送完了件数
        delivery_absence_num	= 0;			//不在件数
        delivery_exclude_num	= 0;			//返品件数
        voucher_num				= 0;			//配送伝票枚数
        voucher_comp_num		= 0;			//配送完了伝票枚数
        voucher_absence_num		= 0;			//不在伝票枚数
        voucher_exclude_num		= 0;			//返品伝票枚数
        //詳細情報(ID情報など)
        detail_order_info		= "";			//配送依頼情報(詳細ページ)
        entry_barcode           = "";

        //マイページ表示情報
        unsend_num				= 0;
        unsend_list				= "";
        setting_field			= 1;

        save();
    }
    //endregion

    //region 業務情報
    //車両ID
    @JavascriptInterface
    public int getCarId() {
        return car_id;
    }

    @JavascriptInterface
    public void setCarId(int car_id) {
        this.car_id = car_id;
    }

    //ドライバーID
    @JavascriptInterface
    public int getDriverId() {
        return driver_id;
    }

    @JavascriptInterface
    public void setDriverId(int driver_id) {
        this.driver_id = driver_id;
    }

    //車両NO
    @JavascriptInterface
    public String getCarNo() {
        return car_no;
    }

    @JavascriptInterface
    public void setCarNo(String car_no) {
        this.car_no = car_no;
    }

    //ドライバー名
    @JavascriptInterface
    public String getDriverName() {
        return driver_name;
    }

    @JavascriptInterface
    public void setDriverName(String driver_name) {
        this.driver_name = driver_name;
    }

    //ルートID
    @JavascriptInterface
    public int getRouteId() {
        return route_id;
    }

    @JavascriptInterface
    public void setRouteId(int route_id) {
        this.route_id = route_id;
    }

    //配送指示番号
    @JavascriptInterface
    public String getInstructNo() {
        return instruct_no;
    }

    @JavascriptInterface
    public void setInstructNo(String instruct_no) {
        this.instruct_no = instruct_no;
    }

    //処理日
    @JavascriptInterface
    public String getCurrentDate() {
        return current_date;
    }

    @JavascriptInterface
    public void setCurrentDate(String current_date) {
        this.current_date = current_date;
    }
    //endregion

    //region 報告情報
    //配送リスト
    @JavascriptInterface
    public String getDeliveryList() {
        return delivery_list;
    }

    @JavascriptInterface
    public void setDeliveryList(String delivery_list) {
        this.delivery_list = delivery_list;
    }

    //画像リスト
    @JavascriptInterface
    public String getPictureList() {
        return picture_list;
    }

    @JavascriptInterface
    public void setPictureList(String picture_list) {
        this.picture_list = picture_list;
    }

    //電子サイン
    @JavascriptInterface
    public String getSign() {
        return sign;
    }

    @JavascriptInterface
    public void setSign(String sign) {
        this.sign = sign;
    }
    //endregion

    //region 報告時間
    //業務開始時間
    @JavascriptInterface
    public String getWorkStartTime() {
        return work_start_time;
    }

    @JavascriptInterface
    public void setWorkStartTime(String work_start_time) {
        this.work_start_time = work_start_time;
    }

    //センター着時間
    @JavascriptInterface
    public String getCycleStartTime() {
        return cycle_start_time;
    }

    @JavascriptInterface
    public void setCycleStartTime(String cycle_start_time) {
        this.cycle_start_time = cycle_start_time;
    }

    //待機時間
    public String getWaitTime() {
        return wait_time;
    }

    public void setWaitTime(String wait_time) {
        this.wait_time = wait_time;
    }

    //配送情報受信時間
    @JavascriptInterface
    public String getReceiveCourseTime() {
        return receive_course_time;
    }

    @JavascriptInterface
    public void setReceiveCourseTime(String receive_course_time) {
        this.receive_course_time = receive_course_time;
    }

    //積込開始時間
    @JavascriptInterface
    public String getLoadingStartTime() {
        return loading_start_time;
    }

    @JavascriptInterface
    public void setLoadingStartTime(String loading_start_time) {
        this.loading_start_time = loading_start_time;
    }

    //積込終了時間
    @JavascriptInterface
    public String getLoadingLastTime() {
        return loading_last_time;
    }

    @JavascriptInterface
    public void setLoadingLastTime(String loading_last_time) {
        this.loading_last_time = loading_last_time;
    }

    //出発時間
    @JavascriptInterface
    public String getStartingReportTime() {
        return starting_report_time;
    }

    @JavascriptInterface
    public void setStartingReportTime(String starting_report_time) {
        this.starting_report_time = starting_report_time;
    }

    //配送開始時間
    @JavascriptInterface
    public String getDeliveryStartTime() {
        return delivery_start_time;
    }

    @JavascriptInterface
    public void setDeliveryStartTime(String delivery_start_time) {
        this.delivery_start_time = delivery_start_time;
    }

    //最終配送時間
    @JavascriptInterface
    public String getDeliveryLastTime() {
        return delivery_last_time;
    }

    @JavascriptInterface
    public void setDeliveryLastTime(String delivery_last_time) {
        this.delivery_last_time = delivery_last_time;
    }

    //帰社報告時間
    @JavascriptInterface
    public String getReturnTime() {
        return return_time;
    }

    @JavascriptInterface
    public void setReturnTime(String return_time) {
        this.return_time = return_time;
    }

    //荷降ろし開始時間
    @JavascriptInterface
    public String getUnloadingStartTime() {
        return unloading_start_time;
    }

    @JavascriptInterface
    public void setUnloadingStartTime(String unloading_start_time) {
        this.unloading_start_time = unloading_start_time;
    }

    //荷降ろし終了時間
    @JavascriptInterface
    public String getUnloadingEndTime() {
        return unloading_end_time;
    }

    @JavascriptInterface
    public void setUnloadingEndTime(String unloading_end_time) {
        this.unloading_end_time = unloading_end_time;
    }

    //回旋終了時間
    @JavascriptInterface
    public String getCycleEndTime() {
        return cycle_end_time;
    }

    @JavascriptInterface
    public void setCycleEndTime(String cycle_end_time) {
        this.cycle_end_time = cycle_end_time;
    }

    //業務終了時間
    @JavascriptInterface
    public String getWorkEndTime() {
        return work_end_time;
    }

    @JavascriptInterface
    public void setWorkEndTime(String work_end_time) {
        this.work_end_time = work_end_time;
    }
    //endregion

    //region 件数情報
    @JavascriptInterface
    public int getDeliveryNum() {
        return delivery_num;
    }

    @JavascriptInterface
    public void setDeliveryNum(int delivery_num) {
        this.delivery_num = delivery_num;
    }

    @JavascriptInterface
    public int getDeliveryCompNum() {
        return delivery_comp_num;
    }

    @JavascriptInterface
    public void setDeliveryCompNum(int delivery_comp_num) {
        this.delivery_comp_num = delivery_comp_num;
    }

    @JavascriptInterface
    public int getDeliveryAbsenceNum() {
        return delivery_absence_num;
    }

    @JavascriptInterface
    public void setDeliveryAbsenceNum(int delivery_absence_num) {
        this.delivery_absence_num = delivery_absence_num;
    }

    @JavascriptInterface
    public int getDeliveryExcludeNum() {
        return delivery_exclude_num;
    }

    @JavascriptInterface
    public void setDeliveryExcludeNum(int delivery_exclude_num) {
        this.delivery_exclude_num = delivery_exclude_num;
    }

    @JavascriptInterface
    public int getVoucherNum() {
        return voucher_num;
    }

    @JavascriptInterface
    public void setVoucherNum(int voucher_num) {
        this.voucher_num = voucher_num;
    }

    @JavascriptInterface
    public int getVoucherCompNum() {
        return voucher_comp_num;
    }

    @JavascriptInterface
    public void setVoucherCompNum(int voucher_comp_num) {
        this.voucher_comp_num = voucher_comp_num;
    }

    @JavascriptInterface
    public int getVoucherAbsenceNum() {
        return voucher_absence_num;
    }

    @JavascriptInterface
    public void setVoucherAbsenceNum(int voucher_absence_num) {
        this.voucher_absence_num = voucher_absence_num;
    }

    @JavascriptInterface
    public int getVoucherExcludeNum() {
        return voucher_exclude_num;
    }

    @JavascriptInterface
    public void setVoucherExcludeNum(int voucher_exclude_num) {
        this.voucher_exclude_num = voucher_exclude_num;
    }

    @JavascriptInterface
    public String getDetailOrderInfo() {
        return detail_order_info;
    }

    @JavascriptInterface
    public void setDetailOrderInfo(String detail_order_info) {
        this.detail_order_info = detail_order_info;
    }
    //endregion

    //region 入力情報
    //読取バーコード情報
    public String getEntryBarcode() {
        return entry_barcode;
    }

    public void setEntryBarcode(String entry_barcode) {
        this.entry_barcode = entry_barcode;
    }
    //endregion

    //region マスター情報
    //ドライバーリスト
    @JavascriptInterface
    public String getDriverList() {
        return driver_list;
    }

    @JavascriptInterface
    public void setDriverList(String driver_list) {
        this.driver_list = driver_list;
    }

    //車両リスト
    @JavascriptInterface
    public String getCarList() {
        return car_list;
    }

    @JavascriptInterface
    public void setCarList(String car_list) {
        this.car_list = car_list;
    }

    //保管場所リスト
    @JavascriptInterface
    public String getStorageList() {
        return storage_list;
    }

    @JavascriptInterface
    public void setStorageList(String storage_list) {
        this.storage_list = storage_list;
    }
    //endregion

    //region マイページ情報
    //未送信件数
    @JavascriptInterface
    public int getUnsendNum() {
        return unsend_num;
    }

    @JavascriptInterface
    public void setUnsendNum(int unsend_num) {
        this.unsend_num = unsend_num;
    }

    //未送信リスト
    @JavascriptInterface
    public String getUnsendList() {
        return unsend_list;
    }

    @JavascriptInterface
    public void setUnsendList(String unsend_list) {
        this.unsend_list = unsend_list;
    }

    //お知らせ
    @JavascriptInterface
    public String getNews() {
        return news;
    }

    @JavascriptInterface
    public void setNews(String news) {
        this.news = news;
    }

    //設定項目(表示情報)
    @JavascriptInterface
    public int getSettingField() {
        return setting_field;
    }

    @JavascriptInterface
    public void setSettingField(int setting_field) {
        this.setting_field = setting_field;
    }
    //endregion

    //region コンテキスト
    public SharedPreferences getPreferences() {
        return pref;
    }

    public void setPreferences(SharedPreferences pref) {
        this.pref = pref;
    }
    //endregion

    //region GPS(緯度経度)情報
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
    //endregion
}
