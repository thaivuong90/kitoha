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
 * @brief       MtbDriversの操作
 * @note
 * @copyright   (c)copyright 2016 KITOHA All Rights Reserved.
 */
public class MtbDrivers implements ITableAdapter {
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
    public MtbDrivers(Context context) {
        sql     = "";
        db_mgr  = DBManager.getInstance();
        db_mgr.set_context( context );
        db_mgr.open();
    }

    /***************************************************************************
     * メソッド(操作)
     ***************************************************************************/
    @Override
    public String select() {
        this.sql    = "SELECT `_id`, `company_id`, `base_id`, `partner_id`, `name`, `phone`, `seq_no`, "
                    + "`status`, `created`, `modified`, `registrant_id` "
                    + "FROM " + Constants.TBL_MTB_DRIVERS;

        Cursor      cursor   = db_mgr.raw_query( this.sql );
        if( cursor.getCount() <= 0 ) {
            return "";
        }

        //取得したデータをJSON形式に整形する
        JSONObject	json = new JSONObject();
//        JSONArray   list = new JSONArray();
        int			idx		= 0;
        int         row     = 0;
        try {
            if( cursor.getCount() > 0 ){
                //データを全件数処理する(SELECT形式のデータ取得)
                while( cursor.moveToNext() ){
                    idx	= 0;
//                    JSONObject data = new JSONObject();
//                    data.put( "_id",					cursor.getInt( idx++ ) );			//ID
//                    data.put( "company_id",				cursor.getInt( idx++ ) );		//企業ID
//                    data.put( "base_id", 				cursor.getInt( idx++ ) );			//拠点ID
//                    data.put( "partner_id", 			cursor.getInt( idx++ ) );		    //パートナーID
//                    data.put( "name",					cursor.getString( idx++ ) );		//名前
//                    data.put( "phone",					cursor.getString( idx++ ) );	//電話番号
//                    data.put( "seq_no",					cursor.getInt( idx++ ) );		//表示順
//                    data.put( "status",			    	cursor.getInt( idx++ ) );		//ステータス
//                    data.put( "created",				cursor.getString( idx++ ) );		//作成日
//                    data.put( "modified",				cursor.getString( idx++ ) );		//更新日
//                    data.put( "registrant_id",			cursor.getInt( idx++ ) );		//作成者
//                    list.put( data );
                    json.put( String.valueOf( cursor.getInt( 0 ) ), cursor.getString( 4 ) );
                }
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

    //region SQL文作成
    /***************************************************************************
     * メソッド(SQL文作成)
     ***************************************************************************/
    /**
     * SELECTステートメントの取得
     * @return  SQL文
     */
    public String getSelectStatement(){
        String  query   = "";

        query = "SELECT `_id`, `name` "
                + "FROM " + Constants.TBL_MTB_DRIVERS
                + " WHERE `status` = 1 "
                + "ORDER BY `seq_no`";

        return query;
    }

    /**
     * INSERTステートメントの取得
     * @param   rec   Insert対象データ(1レコード) JSONObject
     * @return  SQL文
     */
    public String getInsertStatement(JSONObject rec ){
        String  query   = "";

        //INSERT INTOの設定
        query = "INSERT INTO " + Constants.TBL_MTB_DRIVERS + " ( "
                + " `_id`, `company_id`, `base_id`, `partner_id`, `name`, `phone`, `seq_no` ) "
                + "VALUES ( ";

        //INSERTの値リスト末尾の閉じカッコ
        String tmp_partner_id   = null;
        try {
            tmp_partner_id = rec.getString( "partner_id" );
            int    partner_id       = 0;
            if( tmp_partner_id != "null" ){
                partner_id      = Integer.parseInt( tmp_partner_id );
            }

            query   += rec.getInt( "id" ) + ", "
                    + rec.getInt( "company_id" ) + ", "
                    + rec.getInt( "base_id" ) + ", "
                    + partner_id + ", "
                    + "'" + rec.getString( "name" ) + "', "
                    + "'" + rec.getString( "phone" ) + "', "
                    + "'" + rec.getString( "seq_no" ) + "'";

            query += " )";
        } catch (JSONException e) {
            //エラーなので、SQLを空にする
            e.printStackTrace();
            query = "";
        }

        return query;
    }
    //endregion
}
