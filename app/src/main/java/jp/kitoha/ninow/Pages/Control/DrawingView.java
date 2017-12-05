package jp.kitoha.ninow.Pages.Control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/******************************************************************************
 * @brief		描画Viewクラス
 * @note		画像上に絵を描くためのビュー
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class DrawingView extends ImageView{
	/***************************************************************************
	 * インスタンス変数
	 ***************************************************************************/
	private Paint		paint;											//描画
	private Path		path;											//パス(線)
	public	Context		context;										//コンテキスト(Activityなど)
	private int			change_path;									//パス
	private int			image_width;									//画像(横幅)
	private int			canvas_width;									//キャンパス(横幅)

	/***************************************************************************
	 * メソッド
	 ***************************************************************************/
	/**
	 * コンストラクタ
	 * @param context
	 * @param attrs
	 */
	public DrawingView(Context context, AttributeSet attrs) {
		super( context, attrs );
		this.context		= context;
		this.path 			= new Path();
		this.paint			= new Paint();
		this.change_path = 0;													//編集フラグの初期化
		this.image_width = 0;
		this.canvas_width = 0;
		paint.setColor( Color.RED );												//書き込み色
		this.paint.setStyle( Paint.Style.STROKE );
		this.paint.setAntiAlias( true );
		this.paint.setStrokeWidth( 20 );
	}

	/****************************************************************
	 * イベント
	 ****************************************************************/
	/**
	 * @brief	背景画像とパスの表示
	 * @param
	 **/
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawPath( path, paint );												//その上にパスを表示させる
	}

	/**
	 * @brief	パスの追加表示(書き込み処理)
	 * @param event	タッチイベント
	 **/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				this.path.moveTo( x, y );
				break;
			case MotionEvent.ACTION_MOVE:
				this.path.lineTo( x, y );
				break;
			case MotionEvent.ACTION_UP:
				this.path.lineTo (x, y );
				break;
		}
		setChangePath( 1 );																//編集フラグをオンにする
		invalidate();
		return true;
	}

	/***************************************************************************
	 * メソッド(画像処理)
	 ***************************************************************************/
	/**
	 * @brief	保存前のパスを削除する
	 * @param
	 **/
	public void delete() {
		//書かれた線を消去する
		this.path.reset();
		invalidate();
	}

	/**
	 * ビューに描画されているBitmapの取得
	 * @return	Bitmap
     */
	public Bitmap getViewBitmap(){
		this.setDrawingCacheEnabled( true );		//キャッシュを有効にする

		//キャッシュ情報を取得し、Bitmapに変換する
		Bitmap	cache = this.getDrawingCache();
		if( cache == null ){
			return null;
		}
		Bitmap	bitmap = Bitmap.createBitmap( cache );

		this.setDrawingCacheEnabled( false );		//キャッシュを無効にする

		return bitmap;
	}

	/***************************************************************************
	 * メソッド(プロパティ)
	 ***************************************************************************/
	//region プロパティ
	/**
	 * @brief 編集フラグの設定
	 * @param    value        設定値
	 */
	public void setChangePath(int value ){
		this.change_path = value;
	}

	/**
	 * @brief 編集フラグの設定
	 * @param
	 */
	public int getChagePath(){
		return this.change_path;
	}
	//endregion
}
