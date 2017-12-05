package jp.kitoha.ninow.IO.DB.Core;

import android.database.Cursor;

/******************************************************************************
 * @brief		アダプタークラス用I/F
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public interface ITableAdapter {
    public String select();                                         // 全件取得
    public Cursor select(String sql );                              // 指定したSELECT文を実行する

    public long insert( String sql );                               //指定したINSERT文を実行する
    public long update( String sql );                               //指定したUPDATE文を実行する
    public long delete( String sql );                               //指定したDELETE文を実行する

    public void close();
}
