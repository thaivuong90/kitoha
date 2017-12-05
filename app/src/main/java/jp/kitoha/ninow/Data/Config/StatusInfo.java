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
import jp.kitoha.ninow.Common.ScreenCode;

/******************************************************************************
 * @author  KITOHA    N.Endo
 * @brief   進捗情報
 * @note    進捗ステータス
 * @since 2015 -
 * @copyright (c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class StatusInfo {
    //region シングルトン用コード
    /**************************************************************************
     * インスタンス変数
     **************************************************************************/
    public static StatusInfo instance = new StatusInfo();

    /****************************************************************
     * メソッド
     ****************************************************************/
    /**
     * @brief	インスタンスの取得
     * @return	インスタンス
     */
    public static StatusInfo getInstance(){
        return instance;
    }
    //endregion

    //region データ設定
    /**************************************************************************
     * インスタンス変数
     **************************************************************************/
    //業務情報
    private int     progress;				            //進捗状況
    private int     current_screen_id;		            //現在表示画面
    private int     prev_screen_id;	    	            //直前表示画面
    //コンテキスト(ファイル操作で利用)
    private SharedPreferences     pref;

    /**
     * @brief コンストラクタ
     */
    private StatusInfo(){
        progress            = 1;
        current_screen_id   = 1001;
        prev_screen_id      = 0;
        //コンテキスト(ファイル操作で利用)
        pref                = null;
    }
    /**
     * 設定の読み込み
     * @return
     */
    @JavascriptInterface
    public int read(){
        this.progress           = pref.getInt( Constants.KEY_PROGRESS, 1 );
        this.current_screen_id  = pref.getInt( Constants.KEY_CURRENT_SCR_ID, ScreenCode.PAGENO_F01A );
        this.prev_screen_id     = pref.getInt( Constants.KEY_PREV_SCR_ID, ScreenCode.PAGENO_F01A );

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
        editor.putInt( Constants.KEY_PROGRESS,              this.progress );
        editor.putInt( Constants.KEY_CURRENT_SCR_ID,        this.current_screen_id );
        editor.putInt( Constants.KEY_PREV_SCR_ID,           this.prev_screen_id );

        editor.commit();

        return ErrorCode.STS_OK;
    }
    //endregion

    //region 進捗状況
    @JavascriptInterface
    public int getProgress() {
        return progress;
    }

    @JavascriptInterface
    public void setProgress(int progress) {
        this.progress = progress;
    }
    //endregion

    //region 表示画面情報
    @JavascriptInterface
    public int getCurrentScreenId() {
        return current_screen_id;
    }

    @JavascriptInterface
    public void setCurrentScreenId(int current_screen_id) {
        this.current_screen_id = current_screen_id;
    }

    @JavascriptInterface
    public int getPrevScreenId() {
        return prev_screen_id;
    }

    @JavascriptInterface
    public void setPrevScreenId(int prev_screen_id) {
        this.prev_screen_id = prev_screen_id;
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
