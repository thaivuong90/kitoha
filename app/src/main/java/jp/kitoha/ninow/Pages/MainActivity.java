package jp.kitoha.ninow.Pages;

/*******************************************************************************
 * インポート
 *******************************************************************************/

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.kitoha.ninow.Common.Constants;
import jp.kitoha.ninow.Common.ScreenCode;
import jp.kitoha.ninow.Common.Utility;
import jp.kitoha.ninow.Data.Config.AppInfo;
import jp.kitoha.ninow.Data.Config.ModeInfo;
import jp.kitoha.ninow.Data.Config.RunInfo;
import jp.kitoha.ninow.Data.Config.StatusInfo;
import jp.kitoha.ninow.Data.CourseInfo;
import jp.kitoha.ninow.Dialogs.Core.DialogResult;
import jp.kitoha.ninow.Dialogs.InformationDialog;
import jp.kitoha.ninow.IO.DB.Adapter.DtbOrders;
import jp.kitoha.ninow.IO.DB.Adapter.DtbTransportRecords;
import jp.kitoha.ninow.IO.DB.Adapter.MtbDrivers;
import jp.kitoha.ninow.IO.DB.Adapter.MtbStorages;
import jp.kitoha.ninow.IO.DB.Adapter.MtbTrucks;
import jp.kitoha.ninow.IO.DB.Core.DBManager;
import jp.kitoha.ninow.IO.DB.Core.DBOpenHelper;
import jp.kitoha.ninow.IO.File.FileManager;
import jp.kitoha.ninow.Network.Core.ExWebViewClient;
import jp.kitoha.ninow.Network.HttpCommand;
import jp.kitoha.ninow.Pages.Api.ApiMainActivity;
import jp.kitoha.ninow.R;

/*******************************************************************************
 * メインアクティビティ
 * @author KITOHA N.Endo
 * @note メイン画面
 * @copyright copyright(c) 2015 KITOHA.co.,ltd All Right Reserved.
 *******************************************************************************/
public class MainActivity extends Activity implements LocationListener {
    /**************************************************************************
     * インスタンス変数
     **************************************************************************/
    //region インスタンス変数
    private AppInfo         app_info;                           //アプリ設定
    private RunInfo         run_info;                           //実行情報
    private StatusInfo      status_info;                        //進捗情報
    private ModeInfo        mode_info;                          //モード情報
    private DialogResult    dialog_result;                      //ダイアログの結果情報
    //WebView関連
    private WebView         main_view;                          //WebView
    private WebSettings     settings;                           //WebViewのオプション設定
    //DB関連
    private SQLiteDatabase  db;                                 //接続対象データベース
    File                    app_dir;                            //アプリケーションディレクトリ
    //キーイベント用
    private boolean         is_backkey_down = false;            //戻るキー押下
    //BCR関連
    private String          input_buffer = "";                  //バーコード入力文字列
    private boolean         is_shift_key;						//shiftキー入力判定

    //ファイル関連
    SharedPreferences    app_pref;
    SharedPreferences    run_pref;
    SharedPreferences    status_pref;
    SharedPreferences    mode_pref;

    //JavaScriptから実行するAPIクラス
    private ApiMainActivity mainapi;                            //メインアクティビティ用APIクラス
    private FileManager     file_api;                           //ファイルAPI
    private CourseInfo      course_info;                        //配送情報
    private LocationManager locationManager;                    //GPS
    //endregion

    /**************************************************************************
     * イベント
     **************************************************************************/
    //region イベント
    /**
     * アクティビティの作成
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初期化
        init();
    }

    /**
     * @brief	Activityが非表示になった(別のActivityを表示した)タイミングの動作
     */
    @SuppressLint("MissingSuperCall")
    public void onPause() {
        super.onPause();
    }

    /**
     * @brief	Activityが表示されたタイミング(Pauseからの復帰)
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("MissingSuperCall")
    public void onResume() {
        super.onResume();
        init();
    }

    /**
     * @brief	Activityが停止されたタイミングの動作(システムによる停止)
     */
    //@SuppressLint( "MissingSuperCall" )
    public void onStop() {
        super.onStop();
        destroy();
    }

    /**
     * @brief	Activityの再起動(Stopからの復帰)
     * @note	onRestart -> onStartの流れ
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("MissingSuperCall")
    public void onRestart() {
        super.onRestart();
        init();
    }

    /**
     * @brief	アプリケーション終了時の動作
     */
    //@SuppressLint("MissingSuperCall")
    public void onDestroy() {
        super.onDestroy();
        destroy();
    }

    /**
     * 権限設定
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult( int requestCode, String[] permissions, int[] grantResults ){
        if( requestCode == 1000 ){
            // 使用が許可された
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                // 位置測定を始めるコードへ跳ぶ
                return;
            }
        }
    }
    //endregion

    /**************************************************************************
     * メソッド(初期化処理)
     **************************************************************************/
    //region 初期化
    /**
     * @brief 初期化処理
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint( { "SetJavaScriptEnabled", "AddJavascriptInterface" } )
    private void init() {
        //アプリ情報の取得
        mode_info       = ModeInfo.getInstance();
        app_info        = AppInfo.getInstance();
        run_info        = RunInfo.getInstance();
        status_info     = StatusInfo.getInstance();

        //設定ファイルを取得する
        SharedPreferences app_pref      = getSharedPreferences( Constants.FILE_APPINFO, Context.MODE_PRIVATE );
        SharedPreferences run_pref      = getSharedPreferences( Constants.FILE_RUNINFO, Context.MODE_PRIVATE );
        SharedPreferences status_pref   = getSharedPreferences( Constants.FILE_STATUSINFO, Context.MODE_PRIVATE );
        SharedPreferences mode_pref     = getSharedPreferences( Constants.FILE_MODEINFO, Context.MODE_PRIVATE );

        app_info.setPreferences( app_pref );
        run_info.setPreferences( run_pref );
        status_info.setPreferences( status_pref );
        mode_info.setPreferences( mode_pref );

        //設定情報などを復元する
        mode_info.read();
        app_info.read();
        run_info.read();
        status_info.read();

        //DBの読込み
        read_database();

        //ImageLoaderの初期化(Universal-Image-Loader)
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority( Thread.NORM_PRIORITY - 2 )          //スレッドの優先度
                .denyCacheImageMultipleSizesInMemory()               //キャッシュ
                .tasksProcessingOrder( QueueProcessingType.LIFO )    //プロセスのデータ構造(LIFO)
                .writeDebugLogs()                                    //デバッグのログ出力
                .build();
        ImageLoader.getInstance().init(config);

        //WebViewの設定
        main_view = (WebView)findViewById( R.id.main_view );
        set_webview( main_view );

        //リカバリファイルの復元
        setUrl( ScreenCode.HTML_PATH, status_info.getCurrentScreenId() );       //URL設定と読み込み処理

        //GPS設定の確認
        check_gps_service( false );
    }

    /**
     * @brief ViewPortの設定
     * @param view(in)		ビュー
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void set_webview(WebView view ){
        //実際の画面サイズを取得する
        Display         display = getWindowManager().getDefaultDisplay();
        DisplayMetrics  metrics = new DisplayMetrics();
        display.getMetrics( metrics );

        //画面の大きさを取得する
        int vp_width	= metrics.widthPixels;
        int vp_height	= metrics.heightPixels;
        vp_height       = Math.min( vp_width * ScreenCode.DEFAULT_VP_HEIGHT / ScreenCode.DEFAULT_VP_WIDTH, vp_height );

        //最近は、アスペクト比が当てにならないので、値をそのまま設定する
        view.setLayoutParams( new RelativeLayout.LayoutParams( vp_width, vp_height ) );

        view.setWebViewClient( new ExWebViewClient(this) );         //リンクタップで標準のブラウザを起動しない
        view.setWebChromeClient( new WebChromeClient() );
        view.setHorizontalFadingEdgeEnabled( false );               //水平スクロールバーを非表示にする
        view.setVerticalScrollbarOverlay( false );                  //垂直スクロールバーを非表示にする
        view.setFocusable( true );                                  //フォーカスを当てる
        view.setWebContentsDebuggingEnabled( true );                //デバッグの有効化(Chrome)

        //WebViewの設定
        settings    = view.getSettings();
        settings.setJavaScriptEnabled( true );                      //JavaScriptを有効にする
        settings.setSaveFormData( false );                          //フォームデータを保存しない
        settings.setSupportZoom( false );                           //拡大縮小を許可しない
        settings.setDomStorageEnabled( true );                     //DOMストレージを利用しない
        settings.setLoadWithOverviewMode( true );                   //ページの横幅を画面幅に合わせる
        settings.setUseWideViewPort( true );                        //ワイドビューポートへの対応

        //JavaScriptから実行するJavaのクラスを登録する
        course_info     = CourseInfo.getInstance();
        file_api        = FileManager.getInstance();
        dialog_result   = DialogResult.getInstance();
        mainapi         = new ApiMainActivity( this, main_view );

        //設定関連
        view.addJavascriptInterface( app_info,          "jv_app_info" );
        view.addJavascriptInterface( run_info,          "jv_run_info" );
        view.addJavascriptInterface( mode_info,         "jv_mode_info" );
        view.addJavascriptInterface( status_info,       "jv_status_info" );
        view.addJavascriptInterface( mainapi,           "jv_mainapi" );          //画面用API
        view.addJavascriptInterface( file_api,          "jv_file_api" );         //ファイルAPI
        view.addJavascriptInterface( dialog_result,     "jv_dialog_result" );    //ダイアログ
    }
    //endregion

    //region 表示URLの決定
    /**
     * @brief URLの設定と読込
     */
    private void setUrl(String html_path, int screen_id) {
        //画面の更新
        switch( screen_id ){
            case ScreenCode.PAGENO_F01A:                        //業務開始
                main_view.loadUrl( html_path + ScreenCode.PAGE_F01A);
                break;
            case ScreenCode.PAGENO_F01B:                        //業務開始(結果)
                main_view.loadUrl( html_path + ScreenCode.PAGE_F02A);
                break;
            case ScreenCode.PAGENO_F02A:                        //センター着
                main_view.loadUrl( html_path + ScreenCode.PAGE_F01A);
                break;
            case ScreenCode.PAGENO_F02B:                        //センター着(結果)
                main_view.loadUrl( html_path + ScreenCode.PAGE_F02A);
                break;
            case ScreenCode.PAGENO_F03A:                        //配送指示受信
                main_view.loadUrl( html_path + ScreenCode.PAGE_F03A);
                break;
            case ScreenCode.PAGENO_F03B:                        //配送指示受信(結果)
                main_view.loadUrl( html_path + ScreenCode.PAGE_F03B);
                break;
            case ScreenCode.PAGENO_F03C:                        //伝票読み取り
                main_view.loadUrl( html_path + ScreenCode.PAGE_F03A);
                break;
            case ScreenCode.PAGENO_F03D:                        //伝票詳細
                main_view.loadUrl( html_path + ScreenCode.PAGE_F03B);
                break;
            case ScreenCode.PAGENO_F04A:                        //積込開始
                main_view.loadUrl( html_path + ScreenCode.PAGE_F04A);
                break;
            case ScreenCode.PAGENO_F04B:                        //積込一覧
                main_view.loadUrl( html_path + ScreenCode.PAGE_F04B);
                break;
            case ScreenCode.PAGENO_F04C:                        //荷物詳細
                main_view.loadUrl( html_path + ScreenCode.PAGE_F04C);
                break;
            case ScreenCode.PAGENO_F05A:                        //出発報告
                main_view.loadUrl( html_path + ScreenCode.PAGE_F05A);
                break;
            case ScreenCode.PAGENO_F05B:                        //出発報告(結果)
                main_view.loadUrl( html_path + ScreenCode.PAGE_F05B);
                break;
            case ScreenCode.PAGENO_F06A:                        //配送開始
                main_view.loadUrl( html_path + ScreenCode.PAGE_F06A);
                break;
            case ScreenCode.PAGENO_F06B:                        //配送一覧
                main_view.loadUrl( html_path + ScreenCode.PAGE_F06B);
                break;
            case ScreenCode.PAGENO_F06C:                        //配送情報詳細
                main_view.loadUrl( html_path + ScreenCode.PAGE_F06C);
                break;
            case ScreenCode.PAGENO_F06D:                        //
                main_view.loadUrl( html_path + ScreenCode.PAGE_F06D);
                break;
            case ScreenCode.PAGENO_F07A:                        //業務終了
                main_view.loadUrl( html_path + ScreenCode.PAGE_F07A);
                break;
            case ScreenCode.PAGENO_F07B:                        //業務終了(確認)
                main_view.loadUrl(html_path + ScreenCode.PAGE_F07B);
                break;
            case ScreenCode.PAGENO_M01A:                        //メインメニュー
                main_view.loadUrl(html_path + ScreenCode.PAGE_M01A);
                break;
            default:                                            //メインメニュー
                main_view.loadUrl(html_path + ScreenCode.PAGE_M01A);
                break;
        }
    }
    //endregion

    //region DB関連処理
    /**
     * DBの読み込み
     */
    private void read_database(){
        String          driver_list             = "";
        String          truck_list              = "";
        String          storage_list            = "";
        String          order_list              = "";
        String          transport_record_list   = "";
        String          image_list              = "";
        String          media_list              = "";

        DBOpenHelper    db_helper = new DBOpenHelper( this );
        //データベースへの接続(更新用)
        db	= db_helper.getWritableDatabase();

        //マスターテーブルの読み込み
        //ドライバー
        MtbDrivers mtb_drivers      = new MtbDrivers( this );
        driver_list                 = mtb_drivers.select();
        run_info.setDriverList( driver_list );
        //車両
        MtbTrucks   mtb_trucks      = new MtbTrucks( this );
        truck_list                  = mtb_trucks.select();
        run_info.setCarList( truck_list );
        //保管場所
        MtbStorages mtb_storages    = new MtbStorages( this );
        storage_list                = mtb_storages.select();
        run_info.setStorageList( storage_list );

        //トランザクションデータ
        //依頼
        DtbOrders   dtb_orders      = new DtbOrders( this );
        order_list                  = dtb_orders.select();
        //run_info.setOrderList( order_list );
        //報告
        DtbTransportRecords dtb_transport_records   = new DtbTransportRecords( this );
        transport_record_list       = dtb_transport_records.select();
        //写真
        //DtbImages dtb_images        = new DtbImages( this );
        //image_list                  = dtb_images.select();
        //動画
        //DtbMedias dtb_medias        = new DtbMedias( this );
        //media_list                  = dtb_medias.select();
    }
    //endregion

    //region 終了処理
    private void destroy(){
        //DBの切断
        if( db.isOpen() == true ){
            db.close();
        }
    }
    //endregion

    //region バーコード関連処理
    /**************************************************************************
     * イベント(バーコード読み取り)
     **************************************************************************/
    /**
     * @brief キー入力イベント
     * @param    event    キー入力イベント
     * @return 成否(false=エラー, true=成功)
     * バーコード読込もこの処理に入る
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        //キー入力を許可しているかどうか判定
        if( checkKeyCode( event.getKeyCode() ) == true ) {
            //イベントアクション毎の動作を決定
            procKeyAction( event );
        }

        return super.dispatchKeyEvent(event);
    }

    /**
     * バーコード入力時の入力可能文字の判定
     * @param		keyCode(in)		入力キー
     * @return		成否
     **/
    public boolean checkKeyCode( int keyCode ) {
        int code	= Integer.valueOf( keyCode );
        if( ( code >= Constants.KEY_CODE_INT_MIN && code <= Constants.KEY_CODE_INT_MAX ) ||			    //数値コード
            ( code >= Constants.KEY_CODE_STR_MIN && code <= Constants.KEY_CODE_STR_MAX ) ||		        //文字コード
            ( code == KeyEvent.KEYCODE_ENTER ) ||                                                       //エンターキー
            ( code == KeyEvent.KEYCODE_NUMPAD_SUBTRACT ) ||	                                            //ハイフン
            ( code == KeyEvent.KEYCODE_MINUS ) ||                                                       //マイナス記号
            ( code == KeyEvent.KEYCODE_SLASH ) ||                                                       //スラッシュ
            ( code == KeyEvent.KEYCODE_PERIOD ) ||                                                      //ピリオド
            ( ( code == KeyEvent.KEYCODE_SHIFT_LEFT ) || ( code == KeyEvent.KEYCODE_SHIFT_RIGHT) ) ){	//SHIFT
            return true;
        }
        return false;
    }

    /**
     * イベント
     * @param event
     */
    private void procKeyAction( KeyEvent event ){
        String	action		= "";
        String	tmp_input	= "";

        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:          //Key押下時(キーボード押下時)
                action = "Action:KeyDown";
                if( event.getKeyCode() == KeyEvent.KEYCODE_ENTER ) {                //エンターキー以外の場合
                    //メニュー毎の処理
                    procMenuKeyAction( status_info.getCurrentScreenId(), input_buffer );

                    //デバッグログ
                    Log.d( "Event(Key)", "input_buffer = " + input_buffer );

                    //次の入力に備えてバッファーを初期化する
                    input_buffer	= "";
                    is_shift_key	= false;
                }else{
                    tmp_input	= String.valueOf( event.getDisplayLabel() );

                    //入力されたキーがShiftキーかどうかチェック
                    if( ( event.getKeyCode() == KeyEvent.KEYCODE_SHIFT_LEFT )   ||
                        ( event.getKeyCode() == KeyEvent.KEYCODE_SHIFT_RIGHT ) ) {
                        //Shiftキーを押している状態なのでON
                        is_shift_key = !is_shift_key;                               //booleanの切り替え
                    }else {
                        if( is_shift_key == false ){
                            //Shiftキーが離されていたら小文字にする
                            tmp_input = tmp_input.toLowerCase();
                        }
                        //入力文字列に入力された文字を追加する
                        input_buffer = input_buffer + tmp_input;
                        //この方法で取得した場合は、全て大文字で取得してしまう
                        //input_buffer = input_buffer + event.getDisplayLabel();
                        //input_buffer	= input_buffer + (char)event.getUnicodeChar();
                    }
                }
                break;
            case KeyEvent.ACTION_UP:                    //Key離し時(キーボードを離しキートップが上がった時)
                action = "Action:KeyUp";
                if( ( event.getKeyCode() == KeyEvent.KEYCODE_SHIFT_LEFT )   ||
                    ( event.getKeyCode() == KeyEvent.KEYCODE_SHIFT_RIGHT ) ) {
                    //Shiftキーを離したのでOFF
                    is_shift_key = !is_shift_key;                                   //booleanの切り替え
                }
                break;
            default:
                action = "Action:Other(" + event.getAction() + ")";
                break;
        }

        //デバッグログ
        Log.d( "Event(Key)", action + ", KeyCode = " + event.getKeyCode() );
    }

    /***
     * @brief		画面の判定(読み取ったキーと表示ページから処理を判別する)
     * @param		screen_id(in)		表示中の画面ID
     * @param		key_code(in)		入力キー
     */
    public void procMenuKeyAction( int screen_id, String key_code ) {
        InformationDialog   dialog      = new InformationDialog( this, main_view );
        String			    html_path	= ScreenCode.HTML_PATH;
//        DBManager           dbLib		= new DBLib( main_api.context );
        String 			    date		= Utility.getToday();

        date	= date.replaceAll( "/", "" );			//日付からスラッシュの削除

        //dbLib.setReadCode( key_code );					//BC読取履歴に保存

        run_info.setEntryBarcode( key_code );

        //スクリーン毎の処理
        switch (screen_id) {
            case ScreenCode.PAGENO_F01A:                    //業務開始報告
                break;
            case ScreenCode.PAGENO_F01B:                    //業務開始結果
                break;
            case ScreenCode.PAGENO_F02A:                    //センター着報告
                break;
            case ScreenCode.PAGENO_F02B:                    //センター着結果
                break;
            case ScreenCode.PAGENO_F03A:                    //配送指示受信
                break;
            case ScreenCode.PAGENO_F03B:                    //配送指示受信結果
                break;
            case ScreenCode.PAGENO_F04A:                    //積付開始報告
                break;
            case ScreenCode.PAGENO_F04B:                    //積付一覧
                mainapi.readPageF04A( key_code );            //積込照会
                break;
            case ScreenCode.PAGENO_F04C:                    //荷物詳細
                break;
            case ScreenCode.PAGENO_F05A:                    //出発報告
                break;
            case ScreenCode.PAGENO_F05B:                    //出発結果
                break;
            case ScreenCode.PAGENO_F06A:                    //配送開始報告
                break;
            case ScreenCode.PAGENO_F06B:                    //配送一覧
                mainapi.readPageF06B( key_code );           //配送先照会
                break;
            case ScreenCode.PAGENO_F06C:                    //配送先詳細
                mainapi.readPageF06C( key_code );           //配送荷物照会
                break;
            case ScreenCode.PAGENO_F06CC:                   //写真
                break;
            case ScreenCode.PAGENO_F06CS:                   //電子サイン
                break;
            case ScreenCode.PAGENO_F06D:                    //荷物詳細
                break;
            case ScreenCode.PAGENO_F07A:                    //帰社報告
                break;
            case ScreenCode.PAGENO_F07B:                    //帰社結果
                break;
            case ScreenCode.PAGENO_F08A:                    //荷下ろし報告
                mainapi.readPageF08A( key_code );           //配送荷物照会
                break;
            case ScreenCode.PAGENO_F08B:                    //荷下ろし結果
                break;
            case ScreenCode.PAGENO_F09A:                    //回旋終了報告
                break;
            case ScreenCode.PAGENO_F09B:                    //回旋終了結果
                break;
            case ScreenCode.PAGENO_F10A:                    //業務終了報告
                break;
            case ScreenCode.PAGENO_F10B:                    //業務終了結果
                break;
            case ScreenCode.PAGENO_M01A:                    //マイページ
                break;
            case ScreenCode.PAGENO_M01B:                    //設定(テキスト)
                break;
            case ScreenCode.PAGENO_M01C:                    //設定(コンボボックス)
                break;
            case ScreenCode.PAGENO_M01D:                    //設定(機能設定)
                break;
            case ScreenCode.PAGENO_M02A:                    //メニュー
                break;
            case ScreenCode.PAGENO_L01A:                    //未送信一覧
                break;
        }
    }
    //endregion

    /***************************************************************************
     * GPS関連処理
     ***************************************************************************/
    //region GPSサービスの利用チェック
    /**
     * @brief GPSが有効か確認する
     * @param
     */
    private void check_gps_service(boolean is_enable) {
        // LocationListenerを登録
        if( ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){
            //パーミッションの設定
            ActivityCompat.requestPermissions( this,
                new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 1 );
            is_enable   = false;
        }else{
            is_enable   = true;
        }

        //ロケーション情報の取得
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {        //GPSセンサーが利用不可
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            //GPS設定画面起動用ボタンとイベントの定義
            alertDialogBuilder.setMessage("GPSが有効になっていません。\n有効化しますか？").setCancelable(false)
                .setPositiveButton("GPS設定起動", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       Intent gps_intent = new Intent(
                           android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                           startActivity(gps_intent);
                    }
                }
            );
            //キャンセルボタン処理
            alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            } );
        }

        if (is_enable == true) {
            // Criteriaオブジェクトを生成
            Criteria criteria = new Criteria();
            criteria.setAccuracy( Criteria.ACCURACY_COARSE );         //Accuracyを指定(低精度)
            criteria.setPowerRequirement( Criteria.POWER_LOW );       //PowerRequirementを指定(低消費電力)
            // ロケーションプロバイダの取得
            String provider = locationManager.getBestProvider( criteria, true );
            locationManager.requestLocationUpdates( provider, 60000, 10, this );
        }
    }
    //endregion

    //region GPS関連イベント
    /**
     * 位置情報変更イベント
     * @param location      位置情報
     */
    @Override
    public void onLocationChanged(Location location) {
        //精度が100以下であれば、GPS情報を更新する
        if( ( location.getAccuracy() > 0.0 ) || ( location.getAccuracy() < app_info.accuracy ) ) {
            run_info.setLatitude( location.getLatitude() );
            run_info.setLongitude( location.getLongitude() );
            run_info.setAccuracy( location.getAccuracy() );
        }
    }

    /**
     * 状態変更イベント
     * @param provider      プロバイダ
     * @param status        状態
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * プロバイダ有効検知イベント
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * プロバイダ無効検知イベント
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider) {

    }
    //endregion
}
