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
 * @brief       MtbTrucksの操作
 * @note
 * @copyright   (c)copyright 2016 KITOHA All Rights Reserved.
 */
public class MtbTrucks implements ITableAdapter {
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
    public MtbTrucks(Context context) {
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
        sql     = "SELECT `_id`, `company_id`, `base_id`, `partner_id`, `name`, `car_no`, `license_plate_area`, "
                + "`license_plate_type`, `license_plate_kana`, `license_plate_no`, `license_plate`, `capacity`, "
                + "`seq_no`, `status`, `created`, `modified`, `registrant_id` "
                + "FROM " + Constants.TBL_MTB_TRUCKS;

        Cursor      cursor   = db_mgr.raw_query( sql );
        if( cursor.getCount() <= 0 ) {
            return "";
        }

        //取得したデータをJSON形式に整形する
        JSONObject	json = new JSONObject();
//        JSONArray list = new JSONArray();
        int			idx		= 0;
        try {
            if( cursor.getCount() > 0 ){
                //データを全件数処理する
                while( cursor.moveToNext() ){
                    idx	= 0;
//                    JSONObject data = new JSONObject();
//                    data.put( "_id",					cursor.getInt( idx++ ) );			//ID
//                    data.put( "company_id",				cursor.getInt( idx++ ) );			//企業ID
//                    data.put( "base_id", 				cursor.getInt( idx++ ) );			//拠点ID
//                    data.put( "partner_id", 			cursor.getInt( idx++ ) );		    //パートナーID
//                    data.put( "name",					cursor.getString( idx++ ) );		//名前
//                    data.put( "car_no",					cursor.getString( idx++ ) );		//車両番号
//                    data.put( "license_plate_area",		cursor.getString( idx++ ) );		//ナンバープレート(地域)
//                    data.put( "license_plate_type",		cursor.getString( idx++ ) );		//ナンバープレート(分類)
//                    data.put( "license_plate_kana",		cursor.getString( idx++ ) );		//ナンバープレート(ひらがな)
//                    data.put( "license_plate_no",		cursor.getString( idx++ ) );		//ナンバープレート(4桁の番号)
//                    data.put( "license_plate",	    	cursor.getString( idx++ ) );		//ナンバープレート(完全版)
//                    data.put( "capacity",				cursor.getDouble( idx++ ) );		//積載量
//                    data.put( "seq_no",					cursor.getInt( idx++ ) );		    //表示順
//                    data.put( "status",			    	cursor.getInt( idx++ ) );			//ステータス
//                    data.put( "created",				cursor.getString( idx++ ) );		//作成日
//                    data.put( "modified",				cursor.getString( idx++ ) );		//更新日
//                    data.put( "registrant_id",			cursor.getInt( idx++ ) );			//作成者
//                    list.put( data );
                    json.put( String.valueOf( cursor.getInt( 0 ) ), cursor.getString( 10 ) );
                }
//                json.put( "Truck", list );
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

        query = "SELECT `_id`, `license_plate` "
              + "FROM " + Constants.TBL_MTB_TRUCKS
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
        //INSERT INTOの設定
        query = "INSERT INTO " + Constants.TBL_MTB_TRUCKS + " ( "
                + " `_id`, `company_id`, `base_id`, `partner_id`, `name`, `car_no`, `license_plate_area`, "
                + "`license_plate_type`, `license_plate_kana`, `license_plate_no`, `license_plate` ) "
                + "VALUES ( ";

        //INSERTの値リスト末尾の閉じカッコ
        String tmp_partner_id   = null;
        try {
            //INSERTの値リスト末尾の閉じカッコ
            tmp_partner_id      = rec.getString( "partner_id" );
            int    partner_id   = 0;
            if( tmp_partner_id != "null" ){
                partner_id      = Integer.parseInt( tmp_partner_id );
            }

            query   += rec.getInt( "id" ) + ", "
                    + rec.getInt( "company_id" ) + ", "
                    + rec.getInt( "base_id" ) + ", "
                    + partner_id + ", "
                    + "'" + rec.getString( "name" ) + "', "
                    + "'" + rec.getString( "car_no" ) + "', "
                    + "'" + rec.getString( "license_plate_area" ) + "', "
                    + "'" + rec.getString( "license_plate_type" ) + "', "
                    + "'" + rec.getString( "license_plate_kana" ) + "', "
                    + "'" + rec.getString( "license_plate_no" ) + "', "
                    + "'" + rec.getString( "license_plate" ) + "'";

            query += " )";
        } catch (JSONException e) {
            //エラーなので、SQLを空にする
            e.printStackTrace();
            query = "";
        }

        return query;
    }
}
