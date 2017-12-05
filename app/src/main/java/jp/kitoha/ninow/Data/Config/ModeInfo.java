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
 * @author KITOHA    N.Endo
 * @brief モード情報
 * @note 動作モード
 * @copyright (c)KITOHA.co., ltd All right reserved.
 * @since 2015 -
 ******************************************************************************/
public class ModeInfo {
    //region シングルトン用コード
    /**************************************************************************
     * インスタンス変数
     **************************************************************************/
    public static ModeInfo instance = new ModeInfo();

    /****************************************************************
     * メソッド
     ****************************************************************/
    /**
     * @brief インスタンスの取得
     * @return インスタンス
     */
    public static ModeInfo getInstance() {
        return instance;
    }
    //endregion

    //region データ設定
    /**************************************************************************
     * インスタンス変数
     **************************************************************************/
    //モード情報
    private int demo_mode;                            //デモモード(0:リリース, 1:デモモード)
    //コンテキスト(ファイル操作で利用)
    private SharedPreferences     pref;

    /**
     * @brief コンストラクタ
     */
    private ModeInfo() {
        demo_mode   = 0;
        //コンテキスト(ファイル操作で利用)
        pref        = null;
    }

    /**
     * 設定の読み込み
     *
     * @return
     */
    @JavascriptInterface
    public int read() {
        this.demo_mode           = pref.getInt( Constants.KEY_DEMO, 0 );

        return ErrorCode.STS_OK;
    }

    /**
     * 設定の保存
     *
     * @return
     */
    @JavascriptInterface
    public int save() {
        SharedPreferences.Editor    editor  = pref.edit();

        //iniファイル形式のデータを読取る(sectionは使わない)
        editor.putInt( Constants.KEY_DEMO,       this.demo_mode );

        editor.commit();

        return ErrorCode.STS_OK;
    }
    //endregion

    //region 進捗状況
    @JavascriptInterface
    public int getDemoMode() {
        return demo_mode;
    }

    @JavascriptInterface
    public void setDemoMode(int demo_mode) {
        this.demo_mode = demo_mode;
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
