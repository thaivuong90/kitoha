package jp.kitoha.ninow.Data.Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.webkit.JavascriptInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.kitoha.ninow.Common.Constants;
import jp.kitoha.ninow.Common.ErrorCode;

/******************************************************************************
 * @author  KITOHA    N.Endo
 * @brief   アプリ設定情報
 * @note    アプリ設定情報
 * @since 2015 -
 * @copyright (c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class AppInfo {
    //region シングルトン用コード
    /**************************************************************************
     * インスタンス変数
     **************************************************************************/
    public static AppInfo instance = new AppInfo();

    /****************************************************************
     * メソッド
     ****************************************************************/
    /**
     * @brief	インスタンスの取得
     * @return	インスタンス
     */
    public static AppInfo getInstance(){
        return instance;
    }
    //endregion

    //region データ設定
    /**************************************************************************
     * インスタンス変数
     **************************************************************************/
    //接続情報
    private String  server_name;                    //サーバーURL
    private String  company_id;                     //企業ID
    private String  user_name;                      //ユーザー名
    private String  password;                       //パスワード
    private int     driver_id;				        //運転手ID
    private int     car_id;				            //車両ID
    private String  driver_name;			    	//運転手名
    private String  car_no;			            	//車両番号(ナンバープレート)
    //機能設定
    private int     is_news;                        //お知らせ使用設定
    private int     is_entry;		    		    //配送情報受信／伝票登録選択
    private int     is_photo;	            		//写真使用設定
    private int     is_sign;	            		//電子サイン使用設定
    private int     is_cert;	            		//判取証明確認使用設定
    private int     is_return;	            		//帰社報告使用設定
    private int     is_unload;	            		//荷降ろし使用設定
    //裏設定(画面上からは設定できない)
    public int      accuracy;                        //GPS補正精度
    //コンテキスト(ファイル操作で利用)
    private SharedPreferences     pref;

    /**
     * @brief コンストラクタ
     */
    private AppInfo(){
        //アプリ設定
        server_name = Constants.DF_SERVER_URL;
        //company_id  = "";
        //user_name   = "";
        //password    = "";
        company_id  = "admin";
        user_name   = "admin";
        password    = "Kitadm01";
        car_id      = 0;
        driver_id   = 0;
        car_no      = "";
        driver_name = "";

        //機能利用設定
        is_news     = 1;
        is_entry    = 1;
        is_photo    = 1;
        is_sign     = 1;
        is_cert     = 0;
        is_return   = 1;
        is_unload   = 0;

        //裏設定
        accuracy    = 100;

        //コンテキスト(ファイル操作で利用)
        pref        = null;
    }

    /**
     * 設定の読み込み
     * @return
     */
    @JavascriptInterface
    public int read(){
        this.server_name        = pref.getString( Constants.KEY_SERVER_NAME, "https://api.ninow.jp/" );
        this.company_id         = pref.getString( Constants.KEY_COMPANY_ID, "" );
        this.user_name          = pref.getString( Constants.KEY_USER_NAME, "" );
        this.password           = pref.getString( Constants.KEY_PASSWORD, "" );
        this.car_id             = pref.getInt( Constants.KEY_CAR_ID, 0 );
        this.car_no             = pref.getString( Constants.KEY_CAR_NO, "" );
        this.driver_id          = pref.getInt( Constants.KEY_DRIVER_ID, 0 );
        this.driver_name        = pref.getString( Constants.KEY_DRIVER_NAME, "" );
        this.is_news            = pref.getInt( Constants.KEY_IS_NEWS, 0 );
        this.is_entry           = pref.getInt( Constants.KEY_IS_ENTRY, 1 );
        this.is_photo           = pref.getInt( Constants.KEY_IS_PHOTO, 1 );
        this.is_sign            = pref.getInt( Constants.KEY_IS_SIGN, 1 );
        this.is_cert            = pref.getInt( Constants.KEY_IS_CERT, 0 );
        this.is_return          = pref.getInt( Constants.KEY_IS_RETURN, 1 );
        this.is_unload          = pref.getInt( Constants.KEY_IS_UNLOAD, 0 );
        this.accuracy           = pref.getInt( Constants.KEY_ACCURACY, 100 );

        return ErrorCode.STS_OK;
    }

    /**
     * 設定の保存
     * @return
     */
    @JavascriptInterface
    public int save(){
        SharedPreferences.Editor    editor  = pref.edit();

        //iniファイル形式のデータを読取る(sectionは使わない)
        editor.putString( Constants.KEY_SERVER_NAME,    this.server_name );
        editor.putString( Constants.KEY_COMPANY_ID,     this.company_id );
        editor.putString( Constants.KEY_USER_NAME,      this.user_name );
        editor.putString( Constants.KEY_PASSWORD,       this.password );
        editor.putInt( Constants.KEY_CAR_ID,            this.car_id );
        editor.putString( Constants.KEY_CAR_NO,         this.car_no );
        editor.putInt( Constants.KEY_DRIVER_ID,         this.driver_id );
        editor.putString( Constants.KEY_DRIVER_NAME,    this.driver_name );
        editor.putInt( Constants.KEY_IS_NEWS,           this.is_news );
        editor.putInt( Constants.KEY_IS_ENTRY,          this.is_entry );
        editor.putInt( Constants.KEY_IS_PHOTO,          this.is_photo );
        editor.putInt( Constants.KEY_IS_SIGN,           this.is_sign );
        editor.putInt( Constants.KEY_IS_CERT,           this.is_cert );
        editor.putInt( Constants.KEY_IS_RETURN,         this.is_return );
        editor.putInt( Constants.KEY_IS_UNLOAD,         this.is_unload );
        editor.putInt( Constants.KEY_ACCURACY,        this.accuracy );

        editor.commit();

        return ErrorCode.STS_OK;
    }
    //endregion

    //region アプリ設定
    @JavascriptInterface
    public String getServerName() {
        return server_name;
    }

    @JavascriptInterface
    public void setServerName(String server_name) {
        this.server_name = server_name;
    }

    @JavascriptInterface
    public String getCompanyId() {
        return company_id;
    }

    @JavascriptInterface
    public void setCompanyId(String company_id) {
        this.company_id = company_id;
    }

    @JavascriptInterface
    public String getUserName() {
        return user_name;
    }

    @JavascriptInterface
    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    @JavascriptInterface
    public String getPassword() {
        return password;
    }

    @JavascriptInterface
    public void setPassword(String password) {
        this.password = password;
    }

    @JavascriptInterface
    public int getDriverId() {
        return driver_id;
    }

    @JavascriptInterface
    public void setDriverId(int driver_id) {
        this.driver_id = driver_id;
    }

    @JavascriptInterface
    public int getCarId() {
        return car_id;
    }

    @JavascriptInterface
    public void setCarId(int car_id) {
        this.car_id = car_id;
    }

    @JavascriptInterface
    public String getDriverName() {
        return driver_name;
    }

    @JavascriptInterface
    public void setDriverName(String driver_name) {
        this.driver_name = driver_name;
    }

    @JavascriptInterface
    public String getCarNo() {
        return car_no;
    }

    @JavascriptInterface
    public void setCarNo(String car_no) {
        this.car_no = car_no;
    }
    //endregion

    //region 機能利用設定
    @JavascriptInterface
    public int getIsNews() {
        return is_news;
    }

    @JavascriptInterface
    public void setIsNews(int is_news) {
        this.is_news = is_news;
    }

    @JavascriptInterface
    public int getIsEntry() {
        return is_entry;
    }

    @JavascriptInterface
    public void setIsEntry(int is_entry) {
        this.is_entry = is_entry;
    }

    @JavascriptInterface
    public int getIsPhoto() {
        return is_photo;
    }

    @JavascriptInterface
    public void setIsPhoto(int is_photo) {
        this.is_photo = is_photo;
    }

    @JavascriptInterface
    public int getIsSign() {
        return is_sign;
    }

    @JavascriptInterface
    public void setIsSign(int is_sign) {
        this.is_sign = is_sign;
    }

    @JavascriptInterface
    public int getIsCert() {
        return is_cert;
    }

    @JavascriptInterface
    public void setIsCert(int is_cert) {
        this.is_cert = is_cert;
    }

    @JavascriptInterface
    public int getIsReturn() {
        return is_return;
    }

    @JavascriptInterface
    public void setIsReturn(int is_return) {
        this.is_return = is_return;
    }

    @JavascriptInterface
    public int getIsUnload() {
        return is_unload;
    }

    @JavascriptInterface
    public void setIsUnload(int is_unload) {
        this.is_unload = is_unload;
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
}
