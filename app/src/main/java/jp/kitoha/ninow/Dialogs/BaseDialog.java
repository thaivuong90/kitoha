package jp.kitoha.ninow.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import jp.kitoha.ninow.Data.Config.AppInfo;
import jp.kitoha.ninow.Data.Config.RunInfo;
import jp.kitoha.ninow.Dialogs.Core.DialogFragment;
import jp.kitoha.ninow.Dialogs.Core.DialogListener;

/******************************************************************************
 * @brief		ダイアログ表示に使用するAPIクラス
 * @note		JavaScriptからJavaのダイアログを表示する場合にも利用
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class BaseDialog implements DialogListener {
	Context context;
	protected Activity		activity;
	protected WebView		view;
	protected AppInfo		app_info;
	protected RunInfo 		run_info;			//アプリ起動中の設定

	/****************************************************************
	 * メソッド
	 *****************************************************************/
	/**
	 * @biref コンストラクタ
	 * @param    activity(in)    コンテキスト
	 */
	public BaseDialog(Activity activity, WebView view ){
		this.view		= view;
		this.activity	= activity;
		app_info 		= AppInfo.getInstance();
		run_info 		= RunInfo.getInstance();
	}

	/****************************************************************
	 * ダイアログ表示
	 *****************************************************************/
	/**
	 * @brief	ダイアログ表示
	 * @param	title(in)		タイトル
	 * @param	message(in)		メッセージ
	 * @param	type(in) ダイアログタイプ 0:OKボタンのみ 1:OK, NGボタン
	 **/
	@JavascriptInterface
	public void show( String title, String message, int type){
		DialogFragment dialog = DialogFragment.factory( title, message, type );
		dialog.setDialogListener( this );
		dialog.show( activity.getFragmentManager(), "DialogFragment" );
	}

	/****************************************************************
	 * ボタンイベント(これ以降をOverrideする)
	 *****************************************************************/
	/**
	 * @brief	肯定ボタンの押下
	 **/
	@Override
	public void doPositiveClick() {
	}

	/**
	 * @brief	否定ボタンの押下
	 **/
	@Override
	public void doNegativeClick() {
	}
}
