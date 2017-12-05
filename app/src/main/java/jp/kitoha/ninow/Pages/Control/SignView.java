package jp.kitoha.ninow.Pages.Control;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.kitoha.ninow.Common.Constants;
import jp.kitoha.ninow.Common.Utility;
import jp.kitoha.ninow.Data.Config.RunInfo;
import jp.kitoha.ninow.IO.DB.Core.DBManager;

/******************************************************************************
 * @brief		電子サインViewクラス
 * @note		電子サイン用
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class SignView extends View {
	/***************************************************************************
	 * インスタンス変数
	 ***************************************************************************/
	private Paint	paint;
	private Path	path;
	private Bitmap	bmp;
	public	Context	context;
	private int		changePath;
	private String	uri;

	/***************************************************************************
	 * メソッド
	 ***************************************************************************/
	//region コンストラクタ
	/**
	 * コンストラクタ
	 * @param context
	 * @param attrs
     */
	public SignView(Context context, AttributeSet attrs) {
		super( context, attrs );
		this.context	= context;
		this.path 		= new Path();
		this.paint		= new Paint();
		this.changePath = 0;															//編集フラグの初期化
		paint.setColor( Color.BLACK );													//書き込み色
		this.paint.setStyle( Paint.Style.STROKE );
		this.paint.setAntiAlias( true );
		this.paint.setStrokeWidth( 10 );

		//ここから初期画像の展開処理
		RunInfo		run_info	= RunInfo.getInstance();						//RunSettingsのインスタンスを宣言
/*
		String		file		= run_settings.getSignPath();						//ファイルパス取得
		this.uri	= file;


		try {
			//Viewサイズの取得
			WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = windowManager.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);

			//ファイルの読込設定
			//サインが既にあり、サイン編集をタップしている場合
			if( !"none".equals( file ) && run_info.getIsEditSign() == 1 ){
				BitmapFactory.Options imageOptions = new BitmapFactory.Options();				//読み込み用のオプションオブジェクトを生成
				imageOptions.inJustDecodeBounds = true;											//画像のサイズ情報だけを取得する
				InputStream istream = new FileInputStream(file);								//inputStreamではなくFileInputStreamを使う
					BitmapFactory.decodeStream(istream, null, imageOptions);					//画像[情報]のみ取得
					String imageType	= imageOptions.outMimeType;								//ファイルタイプ
					bmp		= decodeSampledBitmapFromFile(file, size.y, size.x);
			}
			//Log.d("Event(mem)", "imageHeight " + size.x);
			//Log.d("Event(mem)", "imageWidth " + size.y);
		}catch( Exception e ){
			Log.d( "Event(onDraw)", "NG " + e.getMessage() );
		}
*/
	}
	//endregion

	/***************************************************************************
	 * メソッド(画像処理)
	 ***************************************************************************/
	//region 画像の読込み
	/**
	 * Bitmapをメモリに展開する
	 * @param filepath		ファイルパス
	 * @param req_width		展開する横幅
	 * @param req_height	展開する縦幅
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filepath, int req_width, int req_height) {
		RunInfo	run_info		= RunInfo.getInstance();		//RunSettingsのインスタンスを宣言
/*
		int		org_fileFlag	= run_info.getIsEditSign();		//修正フラグ
		//inJustDecodeBounds = trueで画像のサイズをチェック
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);

		// inSampleSize を計算
		//options.inSampleSize = calculateInSampleSize(options, req_width, req_height);

		// inSampleSize をセットしてデコード
		options.inJustDecodeBounds = false;
		//float scale	= getFitScale( req_height, req_width, options.outWidth, options.outHeight );
		//Log.d("Event(image)", "scale = "+ scale);

		// Exifに従って画像を回転させる
		// 例) 回転マトリックス作成（90度回転）
		//Matrix mat = new Matrix();
		//mat.postRotate(90);
		//mat.postScale( scale, scale );

		Bitmap	bitmap	= BitmapFactory.decodeFile(filepath, options);
		//bitmap		= Bitmap.createBitmap( bitmap, 0, 0, req_width, req_height, mat, true );
		if( org_fileFlag == 1 ) {
			//オリジナルの場合は縮小表示が必要。それ以外はそのまま表示する
			//bitmap = resize( bitmap, req_width, req_height );
		}
		return bitmap;
*/
		return null;
	}
	//endregion

	//region 画像の保存
	/**
	 * @brief 保存確認メッセージの出力
	 * @param
	 */
	public void dialogMessage(){
		AlertDialog.Builder alertDialog	= new AlertDialog.Builder(this.context);
		// ダイアログの設定
		//alertDialog.setIcon(R.drawable.icon);										//アイコン設定(不要)
		alertDialog.setTitle( "確認" );												//タイトル設定
		alertDialog.setMessage( "画像が編集されています。保存しますか？" );			//内容(メッセージ)設定
		// OK(肯定的な)ボタンの設定
		alertDialog.setPositiveButton( "保存", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			// OKボタン押下時の処理
			Log.d( "AlertDialog", "Positive which :" + which );
			}
		} );
		// NG(否定的な)ボタンの設定
		alertDialog.setNegativeButton( "閉じる", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			// NGボタン押下時の処理
			Log.d( "AlertDialog", "Negative which :" + which );
			}
		} );
		// ダイアログの作成と描画
		alertDialog.show();
	}

	/**
	 * @brief	画像の保存
	 * @param
	 * @return	保存先パス
	 * @note	必要があれば画像を回転させて保存する
	 **/
	public String save( String app_name ) {
		//書かれたメモの保存
		RunInfo	run_info	= RunInfo.getInstance();						//RunSettingsのインスタンスを宣言
/*
		int		edit_flag	= run_info.getIsEditSign();						//修正フラグ

		//Bitmapの取得
		Bitmap bitmap	= this.getViewBitmap();
		try {
			File root = Environment.getExternalStorageDirectory();
			Date date = new Date();
			SimpleDateFormat filename = new SimpleDateFormat( "yyyyMMdd_HHss" );

			//既存サインがあるのに再度保存しようとした場合は、既存のファイルを削除する
			if( !"none".equals( this.uri ) || ( edit_flag == 0 ) ){
				File	delete_file = new File( this.uri );
				delete_file.delete();
			}

			//ファイルの保存ディレクトリ
			String dirpath	= root.getAbsoluteFile() + File.separator + "Android" + File.separator
					+ "data" + File.separator + app_name;
			File save_dir	= new File( dirpath );
			//ディレクトリの存在確認
			if ( !save_dir.exists() ){
				//なければディレクトリを生成する
				if( !save_dir.mkdirs() ){
					Log.d( app_name, "failed to create directory" );
					return null;
				}
			}

			//ファイルパスの生成
			String filepath = save_dir.getPath() + File.separator + filename.format( date ) + ".jpg";
			Log.d( "Event(image)", "SAVE " + filepath );
			File saveFile		= new File( filepath );
			FileOutputStream file_out;

			if( edit_flag == 0  ){					//保存
				file_out	= new FileOutputStream( saveFile, false );
			}else{									//上書き保存
				String fileInfo	= run_settings.getSignPath();
				file_out	= new FileOutputStream( saveFile, false );
			}

			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, file_out );
			try {
				file_out.flush();
				file_out.close();
				run_settings.setSignPath( filepath );
			} catch (IOException e) {
				Log.d("Event(image)", "SAVE NG");
				e.printStackTrace();
			}
			invalidate();
			saveDB( filepath );
			return filepath;
		} catch (FileNotFoundException e) {
			Log.d("Event(image)", "SAVE NG1");
			e.printStackTrace();
			return "none";
		}
*/
		return null;
	}

	/**
0	 * 画像保存(DB)
	 * @param filepath
	 */
	public void saveDB(String filepath ){
		String			date					= Utility.getNow();								//処理日の取得
		long			ret						= 0;											//戻り値
		RunInfo			run_info				= RunInfo.getInstance();						//
/*
		String			instruct_no				= run_info.getInstructNo();						//配送指示番号
		String			order_no				= run_info.getCurrentOrderNo();				//管理番号
		String			voucher_no				= run_info.getCurrentVoucherNo();			//伝票番号
		String			transmission_order_no	= run_info.getCurrentTransmissionOrderNo();	//路順
		String			org_filepath			= run_info.getSignPath();					//電子サインのファイルパス
		int				is_edit_sign			= run_info.getIsEditSign();					//電子サインの編集かどうか
		DBManager		dbmgr 					= DBManager.getInstance();						//DB
		dbmgr.set_context( this.context );
		boolean			is_success				= true;
		String			clause;
		String[]		cols					= new String[]{ "order_no", "route_no" };

		//WEHARE文:配送指示番号と路順から条件指定
		clause		= "instruct_no = '" + instruct_no
				+ "' AND transmission_order_no = '" + transmission_order_no
				+ "' AND image_num = 1";

		Log.d( "Event(saveDB)", "filename " + filepath );
		Log.d( "Event(saveDB)", "org_filePath " + org_filepath );
		Log.d( "Event(saveDB)", "org_fileFlag " + is_edit_sign );

		//データベースに接続する
		dbmgr.open();
		dbmgr.begin_transaction();

		ContentValues values = new ContentValues();
		//登録時の共通情報を設定
		values.put( "image_num",		1 );									//画像番号(サインは1固定)
		values.put( "sign_path",		filepath );								//保存先アドレス
		values.put( "sign_time",		date );									//サイン取得時刻
		values.put( "send_flag",		0 );									//送信フラグ(送信後に書き換えられる可能性)

		//DBに既存データが存在するかチェック
		Cursor cursor	= dbmgr.select( Constants.TBL_DTB_IMAGES, cols, clause, null, null, null, null, null );
		if( cursor.getCount() > 0){
			values.put( "modified", date );										//更新時刻
			//既存データを上書き
			ret = dbmgr.update( Constants.TBL_DTB_IMAGES, values, clause, null);
		}else{
			//新規登録
			values.put( "instruct_no",				instruct_no );				//配送指示番号
			values.put( "order_no",					order_no );					//管理番号
			values.put( "transmission_order_no",	transmission_order_no );	//配送順
			ret	= dbmgr.insert( Constants.TBL_DTB_IMAGES, values );
		}

		Log.d( "Event(saveDB)", "update_rep " + ret );
		if( ret == -1 ){
			is_success	= false;
			Log.d( "[INSERT]", "error!");
		}
		dbmgr.end_transaction( is_success );
		dbmgr.close();
*/
	}

	/**
	 * ビューに表示しているBitmapを取得する
	 * @return	Bitmap
     */
	public Bitmap getViewBitmap(){
		this.setDrawingCacheEnabled( true );
		Bitmap	cache = this.getDrawingCache();
		if( cache == null ){
			return null;
		}
		Bitmap	bitmap = Bitmap.createBitmap( cache );
		this.setDrawingCacheEnabled( false );
		return bitmap;
	}
	//endregion

	//region 編集中のパス(線)のクリア
	/**
	 * @brief	保存前のパスを削除する
	 * @param
	 **/
	public void delete() {
		//書かれたメモの消去
		this.path.reset();
		invalidate();
	}
	//endregion

	//region 画像処理(共通)
	/**
	 * 画像リサイズ
	 * @param bitmap 変換対象ビットマップ
	 * @param newWidth 変換サイズ横
	 * @param newHeight 変換サイズ縦
	 * @return 変換後Bitmap
	 */
	public static Bitmap resize(Bitmap bitmap, int newWidth, int newHeight) {

		if (bitmap == null) {
			return null;
		}

		int oldWidth = bitmap.getWidth();
		int oldHeight = bitmap.getHeight();

		//if (oldWidth < 400 && oldHeight < 400) {
		// 縦も横も指定サイズより小さい場合は何もしない
		//	return bitmap;
		//}

		float scaleWidth = ((float) newWidth) / oldWidth;
		float scaleHeight = ((float) newHeight) / oldHeight;
		float scaleFactor = Math.min(scaleWidth, scaleHeight);

		Matrix scale = new Matrix();
		scale.postScale(scaleFactor, scaleFactor);
		if( oldWidth > oldHeight ) {
			scale.postRotate(90);
		}
		Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, oldWidth, oldHeight, scale, false);
		bitmap.recycle();

		return resizeBitmap;
	}
	//endregion

	/***************************************************************************
	 * イベント
	 ***************************************************************************/
	//region イベント
	/**
	 * @brief	描画イベント
	 * @param
	 **/
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw( canvas );
/*
		RunInfo run_info	= RunInfo.getInstance();						//RunSettingsのインスタンスを宣言
		if( !"none".equals( this.uri ) && ( run_info.getIsEditSign() == 1 )) {
			canvas.drawBitmap( bmp, 0, 0, paint );										//既存のサインを表示させる
		}else{
			//編集済み画像にはパスを載せない
			canvas.drawPath( path, paint );												//パスを表示する
		}
*/
	}

	/**
	 * @brief	タッチイベント
	 * @param event	タッチイベント
	 **/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		//パス(線)の追加表示(書き込み処理)
		switch( event.getAction() ){
			case MotionEvent.ACTION_DOWN:
				this.path.moveTo( x, y );
				break;
			case MotionEvent.ACTION_MOVE:
				this.path.lineTo( x, y );
				break;
			case MotionEvent.ACTION_UP:
				this.path.lineTo( x, y );
				break;
		}

		//画像の編集フラグをオンにする
		setisEdit( 1 );

		invalidate();

		return true;
	}
	//endregion

	/***************************************************************************
	 * プロパティ
	 ***************************************************************************/
	//region プロパティ
	/**
	 * @brief 編集フラグの設定
	 * @param    value        設定値
	 */
	public void setisEdit( int value ){
		this.changePath = value;
	}

	/**
	 * @brief 編集フラグの設定
	 * @param
	 */
	public int getisEdit(){
		return this.changePath;
	}
	//endregion
}
