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
 * @brief       DtbTransportRecordsの操作
 * @note
 * @copyright   (c)copyright 2016 KITOHA All Rights Reserved.
 */
public class DtbTransportRecords implements ITableAdapter {
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
    public DtbTransportRecords(Context context) {
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
        sql     = "SELECT `_id`, `proc_date`, `instruct_no`, `report_type`, `report_time`, `report_gps`, `send_flag`, "
                + "`created`, `modified`, `driver` "
                + "FROM " + Constants.TBL_DTB_TRANSPORT_RECORDES;

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
                    data.put( "proc_date",						cursor.getString( idx++ ) );		//
                    data.put( "instruct_no",				    cursor.getString( idx++ ) );		//
                    data.put( "report_type",					cursor.getInt( idx++ ) );		    //
                    data.put( "report_time",					cursor.getString( idx++ ) );		//
                    data.put( "report_gps",					    cursor.getString( idx++ ) );		//
                    data.put( "send_flag",						cursor.getInt( idx++ ) );		    //
                    data.put( "created",						cursor.getString( idx++ ) );		//
                    data.put( "modified",						cursor.getString( idx++ ) );		//
                    data.put( "driver",						    cursor.getString( idx++ ) );		//
                    list.put( data );
                }
                json.put( "TransportRecord", list );
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

        query = "SELECT `_id`, `proc_date`, `report_type`, `report_time`, `report_gps`, `send_flag` "
                + "FROM " + Constants.TBL_DTB_TRANSPORT_RECORDES
                + " WHERE `status` = 1 "
                + "ORDER BY `seq_no`";

        return query;
    }
}
