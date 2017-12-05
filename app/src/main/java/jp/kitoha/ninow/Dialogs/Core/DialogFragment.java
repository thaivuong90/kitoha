package jp.kitoha.ninow.Dialogs.Core;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
//import android.support.v4.app.DialogFragment;では正しく動作しない


/******************************************************************************
 * @brief		ダイアログ
 * @note		画面に表示するダイアログクラス
 * @author 	KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
******************************************************************************/
public class DialogFragment extends android.app.DialogFragment {
	/**************************************************************************
	 * シングルトン用コード
	 **************************************************************************/
	public static DialogFragment instance	= new DialogFragment();
	private			Context			context;
	private			DialogListener	listener	= null;

	/**
	 * @brief	ファクトリーメソッド
	 * @param	type(in) ダイアログタイプ 0:OKボタンのみ 1:OK, NGボタン
	 */
	/**
	 * ファクトリーメソッド
	 * @param title			タイトル
	 * @param message		メッセージ
	 * @param type			ボタン種別(0:OKボタンのみ, 1:OK, NGボタン表示)
     * @return
     */
	public static DialogFragment factory(String title, String message, int type){
		DialogFragment instance = new DialogFragment();

		//ダイアログに渡すパラメータの設定
		Bundle arguments = new Bundle();
		arguments.putString( "title",	title );
		arguments.putString( "message", message );
		arguments.putInt( "type", type );

		instance.setArguments( arguments );

		return instance;
	}

	/**
	 * @brief インスタンスの取得
	 * @return インスタンス
	 */
	public static DialogFragment getInstance(){
		return instance;
	}

	/**
	 * AlertDialogの作成
	 * @param savedInstanceState
	 * @return
     */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		String	title	= getArguments().getString( "title" );
		String	message	= getArguments().getString( "message" );
		int		type	= getArguments().getInt( "type" );

		AlertDialog.Builder alert = new AlertDialog.Builder( getActivity() )
			.setTitle( title )
			.setMessage( message )
			.setPositiveButton( "OK", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// OKボタンが押された時
					listener.doPositiveClick();
					dismiss();
				}
			} );
		if( type == 1 ){ // NGボタンも付ける場合
			alert.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Cancelボタンが押された時
					listener.doNegativeClick();
					dismiss();
				}
			} );
		}

		return alert.create();
	}

	/**
	 * @brief	リスナーを追加
 	 * @param listener
	 */
	public void setDialogListener(DialogListener listener){
		this.listener = listener;
	}

	/**
	 * @biref	リスナー削除
	 */
	public void removeDialogListener(){
		this.listener = null;
	}


	public final void  showDialog  (int id){}
}

