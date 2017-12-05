package jp.kitoha.ninow.IO.DB.Adapter;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.kitoha.ninow.Common.Constants;
import jp.kitoha.ninow.IO.DB.Core.DBManager;
import jp.kitoha.ninow.IO.DB.Core.ITableAdapter;

/**
 * @brief       DtbMediasの操作
 * @note
 * @copyright   (c)copyright 2016 KITOHA All Rights Reserved.
 */
public class DtbMedias implements ITableAdapter {
    /***************************************************************************
     * インスタンス変数
     ***************************************************************************/
    String      sql;
    DBManager   db_mgr;

    /***************************************************************************
     * メソッド
     ***************************************************************************/
    /**
     * コンストラクタ
     */
    public DtbMedias(Context context) {
        sql     = "";
        db_mgr  = DBManager.getInstance();
        db_mgr.set_context( context );
        this.db_mgr.open();
    }

    /***************************************************************************
     * メソッド(操作)
     ***************************************************************************/
    @Override
    public String select() {
        sql     = "SELECT `_id`, `order_id`, `distribution_id`, `order_no`, `voucher_no`, `sub_voucher_no`, `course_id`, "
                + "`transmission_order_no`, `type`, `filepath`, `thumbnail`, `is_send`, `created`, `driver` "
                + "FROM " + Constants.TBL_DTB_MEDIAS;

        Cursor      cursor   = db_mgr.raw_query( sql );
        if( cursor.getCount() > 0 ) {
            return "";
        }

        //取得したデータをJSON形式に整形する
        JSONObject	json = new JSONObject();
        JSONArray list = new JSONArray();
        int			idx		= 0;
        try {
            if( cursor.getCount() > 0 ){
                //データを全件数処理する
                while( cursor.moveToNext() ){
                    idx	= 0;
                    JSONObject data = new JSONObject();
                    data.put( "_id",							cursor.getInt( idx++ ) );			//ID
                    data.put( "order_id",						cursor.getInt( idx++ ) );			//依頼ID
                    data.put( "distribution_id",				cursor.getInt( idx++ ) );			//配送情報ID
                    data.put( "order_no",						cursor.getString( idx++ ) );		//依頼番号
                    data.put( "order_date",						cursor.getString( idx++ ) );		//依頼日
                    data.put( "voucher_no",						cursor.getString( idx++ ) );		//共通運送送り状番号
                    data.put( "sub_voucher_no",					cursor.getString( idx++ ) );		//運送業者運送送り状番号
                    data.put( "course_id",				        cursor.getInt( idx++ ) );			//コースID
                    data.put( "transmission_order_no",			cursor.getInt( idx++ ) );			//路順
                    data.put( "type",					        cursor.getString( idx++ ) );		//種別
                    data.put( "filepath",	    		    	cursor.getString( idx++ ) );		//ファイルパス
                    data.put( "thumbnail",		        		cursor.getString( idx++ ) );		//サムネイル画像ファイルパス
                    data.put( "is_send",					    cursor.getString( idx++ ) );		//送信フラグ
                    data.put( "created",		    			cursor.getString( idx++ ) );		//作成日
                    data.put( "driver",		    			    cursor.getString( idx++ ) );		//登録者

                    list.put( data );
                }
                json.put( "Media", list );
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
        cursor.close();

        //JSON文字列を返す
        return json.toString();
    }

    @Override
    public Cursor select(String sql) {
        Cursor cursor   = db_mgr.raw_query( sql );
        return cursor;
    }

    @Override
    public long insert(String sql) {
        return db_mgr.exec_sql( sql );
    }

    @Override
    public long update(String sql) {
        return db_mgr.exec_sql( sql );
    }

    @Override
    public long delete(String sql) {
        return db_mgr.exec_sql( sql );
    }

    @Override
    public void close() {
        db_mgr.close();
    }

    /***************************************************************************
     * メソッド(SQL文作成)
     ***************************************************************************/
    /**
     * SELECTステートメントの取得
     * @return
     */
    public String getSelectStatment(){
        String  query   = "";

        query = "SELECT `_id`, `order_id`, `distribution_id`, `order_no`, `order_date`, "
              + "`voucher_no`, `sub_voucher_no`, `course_id`, `transmission_order_no`"
              + "`type`. `filepath`. `thumbnail`, `is_send` "
              + "FROM " + Constants.TBL_DTB_MEDIAS
              + " WHERE `status` = 1 "
              + "ORDER BY `seq_no`";

        return query;
    }
}
