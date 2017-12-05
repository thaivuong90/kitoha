package jp.kitoha.ninow.Data;

import android.content.Context;
import android.database.Cursor;
import android.webkit.JavascriptInterface;

import jp.kitoha.ninow.Common.Constants;
import jp.kitoha.ninow.IO.DB.Adapter.DtbOrders;
import jp.kitoha.ninow.IO.DB.Core.DBManager;

/******************************************************************************
 * @brief		配送情報クラス
 * @note		シングルトン
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class CourseInfo {
	/**************************************************************************
	 * インスタンス変数
	 **************************************************************************/
	public static CourseInfo instance = new CourseInfo();
	private Context context;

	/**************************************************************************
	 * メソッド
	 **************************************************************************/
	/**
	 * @brief	インスタンスの取得
	 * @return	インスタンス
	 */
	public static CourseInfo getInstance(){
		return instance;
	}

	/**
	 * @brief	コンストラクタ
	 */
	public CourseInfo(){

	}

	/**
	 * @brief	コンテキストの設定
	 * @param	context
	 */
	public void setContext(Context context){
		this.context	= context;
	}

	/**************************************************************************
	 * メソッド(JavaScriptから実行するために用意)
	 **************************************************************************/
	/**
	 * 日付別の配送指示の取得
	 * @param 	date(in)		日付
	 * @return	配送指示データ(JSON形式)
	 */
	@JavascriptInterface
	public String get_data( String date ){
		//DBに接続してデータを取得する
		DtbOrders	dtb_orders	= new DtbOrders( this.context );
		String		orders		= dtb_orders.select();
		dtb_orders.close();

		//JSON文字列を返す
		return orders;
	}
}
