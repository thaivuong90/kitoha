package jp.kitoha.ninow.IO.Image;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import jp.kitoha.ninow.Common.Constants;
import jp.kitoha.ninow.Data.Config.RunInfo;
import jp.kitoha.ninow.IO.DB.Core.DBManager;

/******************************************************************************
 * @brief		画像処理用クラス
 * @note		エラーコード以外の定数定義
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class ImageTool{
	/****************************************************************
	 * メソッド
	 ****************************************************************/
	/**
	 * パスの補正
	 * @param	path	ファイルパス
	 * @return	補正後のファイルパス
	 */
	public static String sanitize(String path){
		String file_path;
		String uri = "file://";
		//"file://"から始まる場合は、パスを補正する
		if( path.startsWith( uri ) ){
			file_path = path.substring( uri.length() );
		}else{
			file_path = path;
		}

		return file_path;
	}

	/**
	 * オリジナル写真かどうか判定
	 * @param	path	ファイルパス
	 * @return	true=オリジナル写真, false=編集写真
	 */
	public static boolean is_photo(String path){
		String uri = "file://";
		//"file://"から始まる場合は、パスを補正する
		if( path.startsWith( uri ) ){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 画像リサイズ
	 * @param bitmap 変換対象ビットマップ
	 * @param newWidth 変換サイズ横
	 * @param newHeight 変換サイズ縦
	 * @return 変換後Bitmap
	 */
	public static Bitmap resize(Bitmap bitmap, int newWidth, int newHeight) {
		float scaleWidth;
		float scaleHeight;

		//ビットマップが存在しない場合は処理しない
		if (bitmap == null) {
			return null;
		}

		//ビットマップのオリジナルサイズを記憶
		int originWidth		= bitmap.getWidth();
		int originHeight	= bitmap.getHeight();

		Matrix scale = new Matrix();
		//縦横比を確認し、横幅が大きいようであれば、画像を回転させる
		if( originWidth > originHeight ) {
			scale.postRotate( 90 );
			//拡大縮小比率の計算
			scaleWidth = ( ( float ) newWidth ) / originHeight;
			scaleHeight = ( ( float ) newHeight ) / originWidth;
		}else{
			//拡大縮小比率の計算
			scaleWidth = ( ( float ) newWidth ) / originWidth;
			scaleHeight = ( ( float ) newHeight ) / originHeight;
		}

		//拡大縮尺比率を小さいほうに合わせる()
		float scaleFactor = Math.min( scaleWidth, scaleHeight );
		Log.d( "Event(mem)", "scaleWidth " + newWidth + " / " + originWidth );
		Log.d( "Event(mem)", "scaleHeight " + newHeight + " / " + originHeight );
		Log.d( "Event(mem)", "scaleFactor " + scaleFactor );

		//縮尺比率を使って、画像の拡大、縮小を行う
		scale.postScale( scaleFactor, scaleFactor );
		Bitmap resizeBitmap = Bitmap.createBitmap( bitmap, 0, 0, originWidth, originHeight, scale, false );

		return resizeBitmap;
	}

	/**
	 * ファイルをDBに保存する
	 * @param filePath ファイルパス
	 * @return
	 */
	public static void setEditPhoto( String filePath, Context context ){
		RunInfo 	run_info			= RunInfo.getInstance();			//
		String		order				= run_info.getDetailOrderInfo();	//オーダー情報
		try {
			JSONObject	json			= new JSONObject( order );
			int			transmission_no = json.getInt( "transmission_order_no" );		//路順
			String		order_no		= json.getString( "order_no" );					//配送指示番号
//			String		org_file_path	= run_info.getPhotoPath();						//元ファイルのパス
//			int			org_fileFlag	= run_info.getIsEditPhoto();					//元ファイルが修正済みかどうか
			String		tmbPath			= filePath.replace(".jpg", "_tmb.jpg");			//サムネイル用のファイル名
			DBManager	dbmgr 			= DBManager.getInstance();						//DB
			dbmgr.set_context( context );
			boolean	is_success			= true;
			String 	clause;
/*
			//WEHARE文:配送先番号と配送指示番号から条件指定
			if( org_fileFlag == 0 ){
				//元ファイルが編集済みだった場合は検索対象カラムが変わる
				clause		= "filepath = '" + org_file_path + "'";
			}else{
				clause		= "edit_path = '" + org_file_path + "'";
			}

			//データベースに接続する
			dbmgr.open();
			dbmgr.begin_transaction();
			ContentValues values = new ContentValues();
			long update_rep = 0;									//追加用
			values.put( "edit_path",	filePath );						//保存先アドレス
			values.put( "thumbnail",	tmbPath );						//サムネ保存先アドレス
			//values.put( "modified", date);						//作成時刻
			update_rep = dbmgr.update( Constants.TBL_DTB_IMAGES, values, clause, null);

			Log.d("Event(saveDB)", "thumbnail " + tmbPath );
			Log.d( "Event(saveDB)", "update_rep " + update_rep );

			if( update_rep == -1 ){
				is_success	= false;
				Log.d( "[INSERT]", "error!");
			}
			dbmgr.end_transaction( is_success );
			dbmgr.close();
*/
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
