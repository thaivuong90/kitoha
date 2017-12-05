package jp.kitoha.ninow.Network.AsyncTask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.os.AsyncTask;

import com.squareup.okhttp.Response;

import jp.kitoha.ninow.Common.ErrorCode;
import jp.kitoha.ninow.Data.Config.AppInfo;
import jp.kitoha.ninow.Data.Config.RunInfo;
import jp.kitoha.ninow.Network.Core.AsyncTaskCallbacks;

/******************************************************************************
 * @brief		基底通信処理クラス
 * @note		非同期通信処理
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class BaseAsyncTask extends AsyncTask<String, Integer, Long> implements  DialogInterface.OnCancelListener{
	/***************************************************************************
	 * インスタンス変数
	 ***************************************************************************/
	protected ProgressDialog		dialog;						//進捗ダイアログ
	protected Context				context;					//コンテキスト(Activity)
	protected AsyncTaskCallbacks	callback	= null;			//コールバッククラス
	protected AppInfo				app_info;					//アプリケーションセッティング
	protected RunInfo 				run_info;					//実行時セッティング
	protected int					request_code;				//リクエストコード
	protected int					result;						//処理結果

	/* 共通パラメータ */
	protected String				car_no;						//車両番号
	protected String				driver;						//ドライバー
	protected double				latitude;					//緯度
	protected double				longitude;					//経度
	protected float					accuracy;					//補正範囲(精度)

	/***************************************************************************
	 * メソッド
	 ***************************************************************************/
	/**
	 * @brief	コンストラクタ
	 */
	public BaseAsyncTask( Context context, AsyncTaskCallbacks callback ){
		this.context		= context;
		this.callback		= callback;
		this.request_code	= 0;
		this.result			= ErrorCode.STS_OK;

		//設定の取得
		app_info = AppInfo.getInstance();
		run_info = RunInfo.getInstance();
	}

	/***************************************************************************
	 * イベント
	 ***************************************************************************/
	/**
	 * @brief	実行前のタイミングで1回動作する
	 */
	@Override
	protected void onPreExecute(){
		//進捗ダイアログ
		dialog	= new ProgressDialog( context );
		dialog.setTitle( "情報" );
		dialog.setMessage( "通信中です。しばらくお待ちください。" );
		dialog.setProgressStyle( ProgressDialog.STYLE_HORIZONTAL );
		dialog.setCancelable( true );
		dialog.setOnCancelListener( this );
		dialog.setMax( 100 );
		dialog.setProgress( 0 );
		dialog.show();
	}

	/**
	 * @brief	バックグラウンド用スレッド
	 * @param	params
	 * @return
	 */
	@Override
	protected Long doInBackground( String... params ){
		long	ret		= ErrorCode.STS_OK;

		//事前処理
		ret				= pre_proc( params );
		publishProgress( 30 );
		if( ret == ErrorCode.STS_OK ){
			//メイン処理
			ret				= main_proc();
			publishProgress( 80 );
			this.result		= (int)ret;
		}
		//事後処理
		ret				= term_proc();

		publishProgress( 100 );

		return ret;
	}

	/**
	 * 進捗バー更新処理
	 * @param values
	 */
	@Override
	protected void onProgressUpdate( Integer... values ){
		dialog.setProgress( values[0] );
	}

	/**
	 * 実行終了
	 * @param result
	 */
	@Override
	protected void onPostExecute( Long result ){
		dialog.dismiss();
		callback.onTaskFinished( this.request_code, this.result );
	}

	/**
	 * キャンセル
	 * @param dialog
	 */
	@Override
	public void onCancel( DialogInterface dialog ){
		this.cancel( true );
		dialog.dismiss();
		callback.onTaskCancelled( this.request_code );
	}

	/***************************************************************************
	 * メソッド
	 ***************************************************************************/
	/**
	 * @brief	事前処理
	 * @param	params(in)	パラメータ
	 * @return	成否(STS_OK:正常, STS_OK以外:エラー)
	 */
	protected int pre_proc( String... params ){
		return ErrorCode.STS_OK;
	}

	/***
	 * @brief	メイン処理
	 * @return	成否(STS_OK:正常, STS_OK以外:エラー)
	 ***/
	protected int main_proc(){
		return ErrorCode.STS_OK;
	}

	/***
	 * HTTPレスポンスの解析
	 * @param response	HTTPレスポンス
	 * @return
     */
	protected int parse_response(Response response){
		return 	ErrorCode.STS_OK;
	}

	/***
	 * @brief	事後処理
	 * @return	成否(STS_OK:正常, STS_OK以外:エラー)
	 ***/
	protected int term_proc(){
		return ErrorCode.STS_OK;
	}
}
