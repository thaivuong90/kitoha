package jp.kitoha.ninow.Network.Core;

/******************************************************************************
 * @brief		コールバック処理
 * @note		非同期通信処理
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/

public interface AsyncTaskCallbacks {
	public void onTaskFinished( int request_code, int result );		// 終了
	public void onTaskCancelled( int request_code );				// キャンセル
}

