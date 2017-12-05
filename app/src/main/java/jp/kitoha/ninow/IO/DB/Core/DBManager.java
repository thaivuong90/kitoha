package jp.kitoha.ninow.IO.DB.Core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/******************************************************************************
 * @brief		DB接続管理
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class DBManager {
	/***************************************************************************
	 * Singleton
	 ***************************************************************************/
	public static DBManager instance = new DBManager();

	/**
	 * @brief	インスタンスの取得
	 * @return	インスタンス
	 */
	public static DBManager getInstance(){
		return instance;
	}

	/***************************************************************************
	 * インスタンス変数
	 ***************************************************************************/
	SQLiteDatabase	db;
	Context			context;

	/***************************************************************************
	 * メソッド
	 ***************************************************************************/
	/**
	 * コンストラクタ
	 */
	private DBManager(){}

	/**
	 * @brief	コンストラクタ
	 * @param	context(in)		コンテキスト
	 */
	private DBManager(Context context) {
		this.context	= context;
	}

	/***************************************************************************
	 * メソッド(プロパティ)
	 ***************************************************************************/
	/**
	 * @brief	コンテキストの設定
	 * @param	context(in)		コンテキスト
     */
	public void set_context( Context context ){
		this.context	= context;
	}

	/*****************************************************************
	 * DB接続・切断
	 *****************************************************************/
	/**
	 * @brief	DB接続
	 */
	public void open(){
		DBOpenHelper dboh = new DBOpenHelper( context );
		db = dboh.getWritableDatabase();
	}

	/**
	 * @brief	DB切断
	 */
	public void close(){
		db.close();
	}

	/*****************************************************************
	 * トランザクションの実行
	 *****************************************************************/
	/**
	 * @brief	トランザクションの開始
	 */
	public void begin_transaction(){
		db.beginTransaction();
	}

	/**
	 * @brief	トランザクションの終了
	 * @param 	is_success(in)		成否フラグ(true=コミット, false=ロールバック)
	 */
	public void end_transaction(boolean is_success){
		if( is_success ) {
			db.setTransactionSuccessful();
		}
		db.endTransaction();
	}

	/*****************************************************************
	 * SQL文の実行
	 *****************************************************************/
	/**
	 * @brief	SQL文の実行(INSERT/UPDATE/DELETE)
 	 * @param	sql(in)		実行するSQL文
	 * @return
	 */
	public long exec_sql( String sql ){
		long	ret = 0;
		try{
			db.execSQL( sql );
		}catch( Exception e ){
			ret = -1;
		}
		return ret;
	}

	/**
	 * @brief	SQL文の実行(SELECT)
	 * @param	sql(in)		実行するSQL文
	 * @return
	 */
	public Cursor raw_query( String sql ){
		Cursor cursor = db.rawQuery( sql, null );
		return cursor;
	}

	/**
	 * @brief	SQL文(INSERT)の実行
	 * @param	table(in)	テーブル名
	 * @param	value(in)	パラメータ
	 */
	public long insert(String table, ContentValues value){
		return db.insert( table, null, value );
	}

	/**
	 * @brief	SQL文(UPDATE)の実行
	 * @param	table(in)		テーブル名
	 * @param	value(in)		更新パラメータ
	 * @param	clause(in)		WHERE条件
	 * @param	args(in)		パラメータ(WHERE条件の式に使うパラメータ)
	 * WHERE文を直接記述する場合は、パラメータは不要
	 */
	public long update(String table, ContentValues value, String clause, String[] args){
		return db.update( table, value, clause, args );
	}

	/**
	 * @brief	SQL文(DELETE)の実行
	 * @param	table(in)		テーブル名
	 * @param	clause(in)		WHERE条件
	 * @param	args(in)		パラメータ(WHERE条件の式に使うパラメータ)
	 * WHERE文を直接記述する場合は、パラメータは不要
	 */
	public long delete(String table, String clause, String[] args){
		return db.delete( table, clause, args );
	}

	/**
	 * @biref	SQL文(SELECT)の実行
	 * @param	table(in)		テーブル名
	 * @param	cols(in)		取得項目
	 * @param	clause(in)		WHERE条件
	 * @param	args(in)		パラメータ(WHERE条件の式に使うパラメータ)
	 * WHERE文を直接記述する場合は、パラメータは不要
	 * @param	groupby(in)		GROUP BY
	 * @param	having(in)		HAVING
	 * @param	orderby(in)		ORDER BY
	 * @param	limit(in)		取得件数
	 * @return
	 */
	public Cursor select(String table, String[] cols, String clause, String[] args, String groupby, String having, String orderby, String limit){
		Cursor cursor = null;
		try{
			cursor = db.query( table, cols, clause, args, groupby, having, orderby, limit );
		}catch( Exception e ){
			e.printStackTrace();
		}
		//Cursor cursor = db.query( table, cols, null, null, null, null, null );
		return cursor;
	}

	/**
	 * @biref	SQL文(フリー)の実行
	 * @param	sql(in)			SQL文
	 * @param	args(in)		パラメータ(WHERE条件の式に使うパラメータ)
	 * WHERE文を直接記述する場合は、パラメータは不要
	 * @return
	 */
	public Cursor non_exec_query(String sql, String[] args ){
		Cursor cursor = null;
		try{
			cursor = db.rawQuery( sql, args );
		}catch( Exception e ){
			e.printStackTrace();
		}
		return cursor;
	}
}
