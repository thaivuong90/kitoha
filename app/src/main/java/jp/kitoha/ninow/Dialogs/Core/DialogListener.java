package jp.kitoha.ninow.Dialogs.Core;

import java.util.EventListener;

/******************************************************************************
 * @brief		イベントリスナー用インターフェース
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public interface DialogListener extends EventListener{
	/**
	 * OKボタンが押されたイベントを通知
	 */
	public void doPositiveClick();

	/**
	 * Cancelボタンが押されたイベントを通知
	 */
	public void doNegativeClick();
}