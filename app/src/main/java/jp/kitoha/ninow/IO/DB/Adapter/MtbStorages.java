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
 * @brief       MtbStoragesの操作
 * @note
 * @copyright   (c)copyright 2016 KITOHA All Rights Reserved.
 */
public class MtbStorages implements ITableAdapter {
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
    public MtbStorages(Context context) {
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
        sql     = "SELECT `_id`, `company_id`, `base_id`, `shipper_id`, `shipper_base_id`, `name`, `code`, "
                + "`seq_no`, `is_check`, `status`, `created`, `modified`, `registrant_id` "
                + "FROM " + Constants.TBL_MTB_STORAGES;

        Cursor      cursor   = db_mgr.raw_query( sql );
        if( cursor.getCount() <= 0 ) {
            return "";
        }

        //取得したデータをJSON形式に整形する
        JSONObject	json = new JSONObject();
        JSONArray   list = new JSONArray();
        int			idx		= 0;
        int         row     = 0;
        try {
            if( cursor.getCount() > 0 ){
                //データを全件数処理する
                while( cursor.moveToNext() ){
                    idx	= 0;
                    JSONObject data = new JSONObject();
                    data.put( "_id",					cursor.getInt( idx++ ) );			//ID
                    data.put( "company_id",				cursor.getInt( idx++ ) );			//企業ID
                    data.put( "base_id", 				cursor.getInt( idx++ ) );			//拠点ID
                    data.put( "shipper_id", 			cursor.getInt( idx++ ) );		    //パートナーID
                    data.put( "shipper_base_id",		cursor.getString( idx++ ) );		//名前
                    data.put( "name",					cursor.getString( idx++ ) );		//車両番号
                    data.put( "code",					cursor.getString( idx++ ) );		//ナンバープレート(地域)
                    data.put( "seq_no",					cursor.getString( idx++ ) );		//ナンバープレート(分類)
                    data.put( "is_check",				cursor.getString( idx++ ) );		//ナンバープレート(ひらがな)
                    data.put( "status",					cursor.getInt( idx++ ) );			//ステータス
                    data.put( "created",				cursor.getString( idx++ ) );		//作成日
                    data.put( "modified",				cursor.getString( idx++ ) );		//更新日
                    data.put( "registrant_id",			cursor.getInt( idx++ ) );			//作成者
                    list.put( data );
                }
                json.put( String.valueOf( cursor.getInt( row ) ), list );
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

        query = "SELECT `_id`, `name` "
                + "FROM " + Constants.TBL_MTB_STORAGES
                + " WHERE `status` = 1 "
                + "ORDER BY `seq_no`";

        return query;
    }
}
