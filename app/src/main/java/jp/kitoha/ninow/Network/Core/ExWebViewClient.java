package jp.kitoha.ninow.Network.Core;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import jp.kitoha.ninow.Data.Config.AppInfo;

/******************************************************************************
 * @brief		拡張WebViewClient
 * @note		WebKit用のWebViewClientクラス
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class ExWebViewClient extends WebViewClient{
	private Activity		procActivity;					//アクティビティ
	private AppInfo			settings;						//設定

	/**
	 * コンストラクタ
	 * @param activity
     */
	public ExWebViewClient( Activity activity ){
		this.procActivity	= activity;
		this.settings		= AppInfo.getInstance();
	}

	// 新しいURLが指定されたときの処理を定義
	//@Override
	//public boolean shouldOverrideUrlLoading( WebView view, String url ) {
	//	Log.d( "url", url );
	//	//return true;		// 別のActivityやアプリを起動する場合
	//	return false;		// WebView内に読み込み結果を表示する場合
	//}

	/**
	 * ページ読み込み開始時の処理
	 * @param view			ビュー
	 * @param url			URL
	 * @param favicon		favicon
     */
	@Override
	public void onPageStarted( WebView view, String url, Bitmap favicon ) {
		//if( settings.demo_mode == 1 ){
		//	Toast.makeText( this.procActivity.getBaseContext(), "読み込み開始", Toast.LENGTH_LONG ).show();
		//}
	}

	/**
	 * ページ読み込み完了時の処理
	 * @param view			ビュー
	 * @param url			URL
     */
	@Override
	public void onPageFinished( WebView view, String url ) {
		//if( settings.demo_mode == 1 ){
		//	Toast.makeText( this.procActivity.getBaseContext(), "読み込み完了", Toast.LENGTH_LONG ).show();
		//}
	}

	/**
	 * ページ読み込みエラー時の処理
	 * @param view			ビュー
	 * @param errorCode		エラーコード
	 * @param url			URL
	 */
	//@Override
	//public void onReceivedError( WebView view, int errorCode, String description, String url ) {
	//	Toast.makeText( this.procActivity.getBaseContext(), "通信エラー", Toast.LENGTH_LONG ).show();
	//}
}
