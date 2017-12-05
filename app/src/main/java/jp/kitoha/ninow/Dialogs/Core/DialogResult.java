package jp.kitoha.ninow.Dialogs.Core;

import android.content.Context;
import android.webkit.JavascriptInterface;

/******************************************************************************
 * @author KITOHA    N.Kawaguchi
 * @brief アプリ内情報の保存･読込
 * @note 入力値を保持する
 * @copyright (c)KITOHA.co., ltd All right reserved.
 * @since 2016 -
 ******************************************************************************/
public class DialogResult{
	//region シングルトン用コード
	/**************************************************************************
	 * 変数
	 **************************************************************************/
	public static DialogResult instance = new DialogResult();

	/**************************************************************************
	 * コンストラクタ
	 **************************************************************************/
	/**
	 * @brief コンストラクタ
	 */
	private DialogResult(){
		button_result	= 0;
		composit_num	= "";
	}

	/**
	 * @brief インスタンスの取得
	 * @return インスタンス
	 */
	public static DialogResult getInstance(){
		return instance;
	}
	//endregion

	//region インスタンス変数
	/**************************************************************************
	 * インスタンス変数
	 **************************************************************************/
	private Context context;
	private int		button_result;										//積込情報表示
	/**
	 * 1=作業開始, 2=作業終了, 10=配送除外, 11=不在, 12=返品
	 */
	private String	composit_num;										//引継ぎ処理を行う為の一時保存

	/**************************************************************************
	 * プロパティ用メソッド(JavaScriptから実行するために用意)
	 **************************************************************************/
	/**
	 * 値の設定
	 * @param value
	 */
	@JavascriptInterface
	public void setButtonResult( int value ){
		this.button_result = value;
	}

	/**
	 * 値の取得
	 * @return int
	 */
	@JavascriptInterface
	public int getButtonResult(){
		return this.button_result;
	}


	/**
	 * 処理用変数の設定
	 * @param value
	 */
	@JavascriptInterface
	public void setCompositNum( String value ){
		this.composit_num = value;
	}

	/**
	 * 処理用変数の取得
	 * @return int
	 */
	@JavascriptInterface
	public String getCompositNum(){
		return this.composit_num;
	}

}
