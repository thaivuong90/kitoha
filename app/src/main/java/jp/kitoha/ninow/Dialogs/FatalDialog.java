package jp.kitoha.ninow.Dialogs;

import android.app.Activity;
import android.webkit.WebView;

import jp.kitoha.ninow.Dialogs.Core.DialogListener;

/******************************************************************************
 * @brief		警告ダイアログ
 * @note		JavaScriptからJavaのダイアログを表示する場合にも利用
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class FatalDialog extends BaseDialog implements DialogListener {
    /****************************************************************
     * メソッド
     *****************************************************************/
    /**
     * @biref コンストラクタ
     * @param activity  ダイアログを表示するアクティビティ
     * @param view      WebView
     */
    public FatalDialog( Activity activity, WebView view ){
        super( activity, view );
    }

    /****************************************************************
     * ボタンイベント(これ以降をOverrideする)
     *****************************************************************/
    /**
     * @brief	肯定ボタンの押下
     **/
    @Override
    public void doPositiveClick() {
        view.reload();                  //ビューの再表示
    }

    /**
     * @brief	否定ボタンの押下
     **/
    @Override
    public void doNegativeClick() {
        view.reload();                  //ビューの再表示
    }
}
