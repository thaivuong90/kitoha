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
 * @brief       DtbImagesの操作
 * @note
 * @copyright   (c)copyright 2016 KITOHA All Rights Reserved.
 */
public class DtbImages implements ITableAdapter {
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
    public DtbImages(Context context) {
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
                + "`transmission_order_no`, `image_path`, `orginal_time`, `edit_image_path`, `edit_time`, "
                + "`thumbnail`, `is_send`, `created`, `modified`, `driver` "
                + "FROM " + Constants.TBL_DTB_IMAGES;

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
                    data.put( "order_id",						cursor.getInt( idx++ ) );			//
                    data.put( "distribution_id",				cursor.getInt( idx++ ) );			//
                    data.put( "order_no",						cursor.getString( idx++ ) );		//
                    data.put( "order_date",						cursor.getString( idx++ ) );		//
                    data.put( "sub_voucher_no",					cursor.getString( idx++ ) );		//
                    data.put( "voucher_no",						cursor.getString( idx++ ) );		//
                    data.put( "course_id",				        cursor.getInt( idx++ ) );			//
                    data.put( "transmission_order_no",			cursor.getInt( idx++ ) );			//
                    data.put( "image_path",					    cursor.getString( idx++ ) );		//
                    data.put( "orginal_time",	    			cursor.getString( idx++ ) );		//
                    data.put( "edit_image_path",				cursor.getString( idx++ ) );		//
                    data.put( "edit_time",					    cursor.getString( idx++ ) );		//
                    data.put( "thumbnail",		    			cursor.getString( idx++ ) );		//
                    data.put( "is_send",		    			cursor.getString( idx++ ) );		//
                    data.put( "created",						cursor.getString( idx++ ) );		//
                    data.put( "modified",						cursor.getString( idx++ ) );		//
                    data.put( "driver",					        cursor.getString( idx++ ) );		//
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

        query   = "SELECT `_id`, `order_id`, `distribution_id`, `order_no`, `voucher_no`, `sub_voucher_no`, `course_id`, "
                + "`transmission_order_no`, `image_path`, `orginal_time`, `edit_image_path`, `edit_time`, "
                + "`thumbnail`, `is_send`. `created`, `modified`, `driver` "
                + "FROM " + Constants.TBL_DTB_IMAGES
                + " WHERE `status` = 1 "
                + "ORDER BY `seq_no`";

        return query;
    }
}
