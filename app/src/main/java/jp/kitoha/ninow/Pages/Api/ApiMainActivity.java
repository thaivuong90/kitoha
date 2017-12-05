package jp.kitoha.ninow.Pages.Api;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

import jp.kitoha.ninow.Common.Constants;
import jp.kitoha.ninow.Common.ErrorCode;
import jp.kitoha.ninow.Common.ScreenCode;
import jp.kitoha.ninow.Common.Utility;
import jp.kitoha.ninow.Data.Config.AppInfo;
import jp.kitoha.ninow.Data.Config.RunInfo;
import jp.kitoha.ninow.Dialogs.InformationDialog;
import jp.kitoha.ninow.Dialogs.WarningDialog;
import jp.kitoha.ninow.IO.DB.Adapter.DtbOrders;
import jp.kitoha.ninow.Network.AsyncTask.ClearDtbOrdersAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.GetCarListAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.GetCourseInfoAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.GetDriverListAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.GetVoucherInfoAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostCourseRegisterVoucherAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostCycleEndAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostCycleStartAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostDeliveryAbsenceAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostDeliveryEndAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostDeliveryRejectAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostDeliveryStartAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostImagesAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostLoadingReportAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostMediasAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostReturnEndReportAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostReturnStartReportAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostStartingReportAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostUnLoadingReportAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostWorkEndAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.PostWorkStartAsyncTask;
import jp.kitoha.ninow.Network.AsyncTask.UpdateCourseInfoAsyncTask;
import jp.kitoha.ninow.Network.Core.AsyncTaskCallbacks;
import jp.kitoha.ninow.Network.RequestCode;

/*******************************************************************************
 * メインアクティビティ用API
 * @author KITOHA N.Endo
 * @note JavaScriptからの呼び出しなどはこちら
 * @copyright copyright(c) 2015 KITOHA.co.,ltd All Right Reserved.
 *******************************************************************************/
public class ApiMainActivity implements AsyncTaskCallbacks {
    /***************************************************************************
     * 公開変数
     ***************************************************************************/
    public Context         context;                         //MainActivity

    /***************************************************************************
     * インスタンス変数
     ***************************************************************************/
    //region インスタンス変数
    AppInfo         app_info;
    RunInfo         run_info;
    WebView         webview;
    //endregion

    //region メソッド
    /***************************************************************************
     * メソッド
     ***************************************************************************/
    /**
     * コンストラクタ
     * @param activity  アクティビティ(MainActivity)
     * @param view      WebView(main_view)
     */
    public ApiMainActivity( Activity activity, WebView view ){
        this.context    = activity;
        this.webview    = view;
        app_info        = AppInfo.getInstance();
        run_info        = RunInfo.getInstance();
    }
    //endregion

    //region AsyncTaskコールバック
    /***************************************************************************
     * イベント(AsyncTaskコールバック)
     ***************************************************************************/
    /**
     * AsyncTask終了時
     */
    @Override
    public void onTaskFinished( int request_code, int result ) {
        //結果が正常終了の場合のみ遷移(異常終了時は、同じ画面を表示)

        if( result != ErrorCode.STS_OK) {
            //エラーのため、ダイアログを表示する
            ProcFailed( request_code, result );
        }else{
            ProcSuccess( request_code );
        }
    }

    /**
     * AsyncTaskキャンセル時
     */
    @Override
    public void onTaskCancelled( int request_code ) {
        //現在表示している画面をリロード
    }

    /**
     * 正常時の動作
     * @param request_code  リクエストコード
     */
    private void ProcSuccess(int request_code ){
        //次の処理に遷移する
        switch( request_code ){
            case RequestCode.REQ_CODE_POST_WORK_START:                  //業務開始
                webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F01B );
                break;
            case RequestCode.REQ_CODE_POST_CYCLE_START:                 //センター着
                webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F02B );
                break;
            case RequestCode.REQ_CODE_GET_COURSE_INFO:                  //コース情報取得(配送指示受信)
                if( run_info.getDriverList() == "" ){
                    webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F03A );
                }else{
                    webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F03B );
                }
                break;
//[スピン用]ここから ---->
            case RequestCode.REQ_CODE_GET_VOUCHER_INFO:                 //伝票情報取得(伝票読み取り)
                webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F03D );
                break;
            case RequestCode.REQ_CODE_POST_COURSE_REGISTER_VOUCHER:     //伝票情報登録(伝票読み取り)
                //webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F03E );
                break;
//[スピン用]ここまで <----
            case RequestCode.REQ_CODE_CLEAR_COURSE_INFO:
                //配送情報受信 or 伝票登録画面に戻る
                if( app_info.getIsEntry() == 1 ){
                    webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F03A );
                }else{
                    webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F03C );
                }
                break;
            case RequestCode.REQ_CODE_POST_LOADING_REPORT:              //積込報告
                //同じ画面をリロード
                webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F04B );
                break;
            case RequestCode.REQ_CODE_POST_STARTING_REPORT:             //出発報告
                //同じ画面をリロード
                webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F05B );
                break;
            case RequestCode.REQ_CODE_POST_DELIVERY_START:              //配送作業開始
                //詳細を表示
                webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F06C );
                break;
            case RequestCode.REQ_CODE_POST_DELIVERY_END:                //配送報告
                //一覧に戻る
                webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F06B );
                break;
            case RequestCode.REQ_CODE_POST_DELIVERY_REJECT:             //配送報告(返品あり)
                //一覧に戻る
                webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F06B );
                break;
            case RequestCode.REQ_CODE_POST_DELIVERY_ABSENCE:            //配送報告(不在)
                //一覧に戻る
                webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F06B );
                break;
            case RequestCode.REQ_CODE_POST_IMAGES:                      //画像アップロード
                //動画アップロードの実行
                break;
            case RequestCode.REQ_CODE_POST_MEDIAS:                      //動画アップロード
                //配送報告を実行(終了、返品、不在の報告判定が必要)
                break;
//[スピン用]ここから ---->
            case RequestCode.REQ_CODE_POST_RETURN_START:                //帰社準備報告
                //webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F08B );
                break;
            case RequestCode.REQ_CODE_POST_RETURN_END:                  //帰着報告
                if( app_info.getIsUnload() == 1 ) {
                    //荷下ろし
                    webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F08A );
                }else{
                    //帰着報告結果
                    webview.loadUrl(ScreenCode.HTML_PATH + ScreenCode.PAGE_F07B);
                }
                break;
            case RequestCode.REQ_CODE_POST_UNLOADING_REPORT:            //荷下ろし報告
                //リロード
                //webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F09B );
                break;
//[スピン用]ここまで <----
            case RequestCode.REQ_CODE_POST_CYCLE_END:                   //回旋終了報告
                //リロード
                webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F09B );
                break;
            case RequestCode.REQ_CODE_POST_WORK_END:                    //業務終了報告
                webview.loadUrl( ScreenCode.HTML_PATH + ScreenCode.PAGE_F10B );
                break;
            case RequestCode.REQ_CODE_UPDATE_COURSE_INFO:               //コース情報更新
                //現在表示している画面をリロード
                break;
            case RequestCode.REQ_CODE_GET_DRIVER_LIST:                  //ドライバー情報取得
                webview.loadUrl( "javascript:setList( '" + run_info.getDriverList() + "'," + app_info.getDriverId() + ", true );" );
                break;
            case RequestCode.REQ_CODE_GET_CAR_LIST:                     //車両情報取得
                webview.loadUrl( "javascript:setList( '" + run_info.getCarList() + "'," + app_info.getCarId() + ", true );" );
                break;
            default:                                                    //上記依頼
                break;
        }
    }

    /**
     * エラー時の処理
     * @param request_code
     * @param result
     */
    private void ProcFailed(int request_code, int result ){
        WarningDialog dialog = new WarningDialog( (Activity)this.context, this.webview );

        //次の処理に遷移する
        switch( request_code ){
            case RequestCode.REQ_CODE_POST_WORK_START:                  //業務開始
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_WORK_START, 0 );
                break;
            case RequestCode.REQ_CODE_POST_CYCLE_START:                 //センター着
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_CYCLE_START, 0 );
                break;
            case RequestCode.REQ_CODE_GET_COURSE_INFO:                  //コース情報取得(配送指示受信)
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_COURSE_INFO, 0 );
                break;
//[スピン用]ここから ---->
            case RequestCode.REQ_CODE_GET_VOUCHER_INFO:                 //伝票情報取得(伝票読み取り)
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_CYCLE_START, 0 );
                break;
            case RequestCode.REQ_CODE_POST_COURSE_REGISTER_VOUCHER:     //伝票情報登録(伝票読み取り)
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_CYCLE_START, 0 );
                break;
//[スピン用]ここまで <----
            case RequestCode.REQ_CODE_CLEAR_COURSE_INFO:
                //配送情報受信 or 伝票登録画面に戻る
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_CLEAR_COURSE_INFO, 0 );
                break;
            case RequestCode.REQ_CODE_POST_LOADING_REPORT:              //積込報告
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_LOADING, 0 );
                break;
            case RequestCode.REQ_CODE_POST_STARTING_REPORT:             //出発報告
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_START_REPORT, 0 );
                break;
            case RequestCode.REQ_CODE_POST_DELIVERY_START:              //配送作業開始
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_DELIVERY_START, 0 );
                break;
            case RequestCode.REQ_CODE_POST_DELIVERY_END:                //配送報告
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_DELIVERY_REPORT, 0 );
                break;
            case RequestCode.REQ_CODE_POST_DELIVERY_REJECT:             //配送報告(返品あり)
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_DELIVERY_REJECT, 0 );
                break;
            case RequestCode.REQ_CODE_POST_DELIVERY_ABSENCE:            //配送報告(不在)
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_DELIVERY_ABSENCE, 0 );
                break;
            case RequestCode.REQ_CODE_POST_IMAGES:                      //画像アップロード
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_UPLOAD_IMAGE, 0 );
                break;
            case RequestCode.REQ_CODE_POST_MEDIAS:                      //動画アップロード
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_UPLOAD_MEDIA, 0 );
                break;
//[スピン用]ここから ---->
            case RequestCode.REQ_CODE_POST_RETURN_START:                //帰社準備報告
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_RETURN_READY, 0 );
                break;
            case RequestCode.REQ_CODE_POST_RETURN_END:                  //帰着報告
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_RETURN_REPORT, 0 );
                break;
            case RequestCode.REQ_CODE_POST_UNLOADING_REPORT:            //荷卸し報告
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_UNLOADING, 0 );
                break;
//[スピン用]ここまで <----
            case RequestCode.REQ_CODE_POST_CYCLE_END:                   //バッチ終了報告
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_CYCLE_END, 0 );
                break;
            case RequestCode.REQ_CODE_POST_WORK_END:                    //業務終了報告
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_WORK_END, 0 );
                break;
            case RequestCode.REQ_CODE_UPDATE_COURSE_INFO:               //コース情報更新
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_UPDATE_COURSE_INFO, 0 );
                break;
            case RequestCode.REQ_CODE_GET_DRIVER_LIST:                  //ドライバー情報取得
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_DRIVER_LIST, 0 );
                break;
            case RequestCode.REQ_CODE_GET_CAR_LIST:                     //車両情報取得
                dialog.show( ErrorCode.DLG_TITLE_WARN, ErrorCode.DLG_MSG_CAR_LIST, 0 );
                break;
            default:                                                    //上記依頼
                break;
        }
    }
    //endregion

    //region 設定処理
    @JavascriptInterface
    public void saveAppInfo(){
        app_info.save();
        InformationDialog dialog = new InformationDialog( (Activity)this.context, webview );
        dialog.show( "通知", "設定を保存しました。", 0 );
    }
    //endregion

    //region バーコード読み取り時の処理

    /**
     * 積込画面でのバーコード読取り
     * @param key_code  読取りバーコード
     */
    public void readPageF04A( String key_code ){
        //アプリ内DBから荷物情報の検索
        run_info.setLoadingLastTime( Utility.getNow() );
        run_info.save();
//        postLoadingReport( order_id, 20, 0, "" );
    }

    /**
     * 配送先一覧画面でのバーコード読取り
     * @param key_code  読取りバーコード
     */
    public void readPageF06B( String key_code ){
        //アプリ内DBから荷物情報の検索
        run_info.setLoadingLastTime( Utility.getNow() );
        run_info.save();
//        postLoadingReport( order_id, 20, 0, "" );
    }

    /**
     * 配送先詳細画面でのバーコード読取り
     * @param key_code  読取りバーコード
     */
    public void readPageF06C( String key_code ){

    }

    /**
     * 荷下ろし画面でのバーコード読取り
     * @param key_code  読取りバーコード
     */
    public void readPageF08A( String key_code ){

    }

    /**
     * 配送依頼IDの取得
     * @param   entry_no(in)    入力した管理番号
     */
    private int getOrderId( String entry_no ){
        int ret = 0;
        try {
            //
            String      list = run_info.getDeliveryList();
            JSONArray   json = new JSONArray( list );

            //正常応答でデータなしの場合は、処理しない
            for (int idx = 0; idx < json.length(); idx++) {
                //1件ずつ処理する
                JSONObject	rec		= json.getJSONObject( idx );

            }

            //実行時設定に車両一覧を設定
            run_info.setDeliveryList( json.toString() );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }
    //endregion

    //region 通信処理(JavaScriptからの実行)
    /***
     * @brief 業務開始報告
     ***/
    @JavascriptInterface
    public void postWorkStart(){
        //業務開始報告
        new PostWorkStartAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief センター着報告
     ***/
    @JavascriptInterface
    public void postCycleStart(){
        //センター着報告
        new PostCycleStartAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief 配送指示受信
     ***/
    @JavascriptInterface
    public void getCourseInfo(){
        //配送指示受 信(更新ではないので、更新フラグは0とする)
        new GetCourseInfoAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() ),                   //精度
            String.valueOf( run_info.getCurrentDate() ),                //処理日
            String.valueOf( 0 )                                         //更新フラグ
        );
    }

    /***
     * @brief 伝票情報取得(伝票読み取り)
     ***/
    @JavascriptInterface
    public void getVoucherInfo(){
        //伝票情報取得
        new GetVoucherInfoAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /**
     * @brief   配送情報の取り消し
     */
    @JavascriptInterface
    public void clearDtbOrders(){
        //配送情報の取り消し
        new ClearDtbOrdersAsyncTask( context, this ).execute();
    }

    /***
     * @brief 伝票情報登録(伝票読み取り)
     ***/
    @JavascriptInterface
    public void postCourseRegisterVoucher(){
        //出発報告
        new PostCourseRegisterVoucherAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief 積込開始
     ***/
    @JavascriptInterface
    public void getLoadingStart(){
        Date    arrival_time        = null;                 //センター着時間
        Date    loading_start_time  = null;                 //積付開始時間
        Date    wait_time           = null;                 //待機時間
        try {
            arrival_time        = Utility.convertDateTime( run_info.getCycleStartTime() );
            loading_start_time  = Utility.convertDateTime( run_info.getLoadingStartTime() );
            //待機時間の計算
            wait_time           = Utility.diffDate( arrival_time, loading_start_time );
            wait_time           = Utility.convertTime( wait_time.toString() );
            run_info.setWaitTime( wait_time.toString() );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        run_info.setWaitTime( wait_time.toString() );
    }

    /**
     * @brief   配送情報の更新
     * @param   order_id(in)            依頼ID
     * @param   status(in)              ステータス
     * @param   except_reason(in)       除外理由ID
     * @param   except_reason_text(in)  除外理由(テキスト)
     */
    @JavascriptInterface
    public void postLoadingReport(int order_id, int status, int except_reason, String except_reason_text){
        new PostLoadingReportAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() ),                   //精度
            String.valueOf( run_info.getCurrentDate() ),                //処理日
            String.valueOf( run_info.getInstructNo() ),                 //配送指示番号
            String.valueOf( order_id ),                                 //依頼ID
            String.valueOf( run_info.getLoadingLastTime() ),            //配送指示番号
            String.valueOf( status ),                                   //種別
            String.valueOf( except_reason ),                            //除外理由
            except_reason_text                                          //ステータス
        );
    }

    /***
     * @brief 出発報告
     ***/
    @JavascriptInterface
    public void postStartingReport(){
        //出発報告
        new PostStartingReportAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() ),                   //精度
            run_info.getCurrentDate(),
            run_info.getInstructNo()
        );
    }

    /***
     * @brief 配送作業開始
     ***/
    @JavascriptInterface
    public void postDeliveryStart(){
        //画像アップロード
        new PostDeliveryStartAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief 配送報告
     ***/
    @JavascriptInterface
    public void postDeliveryEnd(){
        //配送報告
        new PostDeliveryEndAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief 配送報告(返品あり)
     ***/
    @JavascriptInterface
    public void postDeliveryReject(){
        //配送報告(返品あり)
        new PostDeliveryRejectAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief 配送報告(不在)
     ***/
    @JavascriptInterface
    public void postDeliveryAbsence(){
        //配送報告(不在)
        new PostDeliveryAbsenceAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief 画像アップロード
     ***/
    @JavascriptInterface
    public void postImages(){
        //画像アップロード
        new PostImagesAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief 動画アップロード
     ***/
    @JavascriptInterface
    public void postMedias(){
        //動画アップロード
        new PostMediasAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief 帰社準備報告
     ***/
    @JavascriptInterface
    public void postReturnStart(){
        //帰社準備報告
        new PostReturnStartReportAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief 帰着報告
     ***/
    @JavascriptInterface
    public void postReturnEnd(){
        //帰着報告
        new PostReturnEndReportAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief 荷卸し報告
     ***/
    @JavascriptInterface
    public void postUnloadingReport(){
        //荷卸し報告
        new PostUnLoadingReportAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief 回旋終了報告
     ***/
    @JavascriptInterface
    public void postCycleEnd(){
        //バッチ終了報告
        new PostCycleEndAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief 業務終了報告
     ***/
    @JavascriptInterface
    public void postWorkEnd(){
        //業務終了報告
        new PostWorkEndAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief コース情報更新
     ***/
    @JavascriptInterface
    public void updateCourseInfo(){
        //コース情報更新(更新なので、更新フラグを1に設定)
        new UpdateCourseInfoAsyncTask( context, this ).execute(
            String.valueOf( run_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() ),                   //精度
            String.valueOf( run_info.getInstructNo() )                  //配送指示番号
        );
    }
    //endregion

    //region 設定画面
    /***
     * @brief 車両一覧取得
     ***/
    @JavascriptInterface
    public void getCarList(){
        //車両一覧取得
        new GetCarListAsyncTask( context, this ).execute(
            String.valueOf( app_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }

    /***
     * @brief ドライバー一覧取得
     ***/
    @JavascriptInterface
    public void getDriverList(){
        //ドライバー一覧取得
        new GetDriverListAsyncTask( context, this ).execute(
            String.valueOf( app_info.getCarId() ),                      //車両ID
            String.valueOf( app_info.getDriverId() ),                   //ドライバーID
            String.valueOf( run_info.getLatitude() ),                   //緯度
            String.valueOf( run_info.getLongitude() ),                  //経度
            String.valueOf( run_info.getAccuracy() )                    //精度
        );
    }
    //endregion

    //region 画面共通
    /***
     * @brief   進捗情報の保存
     * @param   page_no    現在のページ番号
     * @param   next_no    次のページ番号
     */
    public void save_progress(int page_no, int next_no){

    }
    //endregion
}
