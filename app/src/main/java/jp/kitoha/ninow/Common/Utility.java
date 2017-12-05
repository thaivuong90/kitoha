package jp.kitoha.ninow.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/******************************************************************************
 * @brief		ユーティリティクラス
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class Utility {
    /***************************************************************************
     * メソッド(日時の取得)
     ***************************************************************************/
    //region 日時取得
    /**
     * 現在日時の取得
     * @return	現在日時
     */
    public static String getNow(){
        //現在日を取得する
        Date date = new Date();
        @SuppressLint( "SimpleDateFormat" )
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
        return sdf.format( date );
    }

    /**
     * 現在日の取得
     * @return	現在日時
     */
    public static String getToday(){
        //現在日を取得する
        Date date = new Date();
        @SuppressLint( "SimpleDateFormat" )
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd" );
        return sdf.format( date );
    }

    /**
     * 現在時間の取得
     * @return	現在時間
     */
    public static String getTime(){
        //現在日を取得する
        Date date = new Date();
        @SuppressLint( "SimpleDateFormat" )
        SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm:ss" );
        return sdf.format( date );
    }

    /**
     * 日時の変換(文字列⇒Date型)
     * @return	時間
     */
    public static Date convertDateTime( String date ) throws ParseException {
        //現在日を取得する
        @SuppressLint( "SimpleDateFormat" )
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
        return sdf.parse( date );
    }

    /**
     * 日付の変換(文字列⇒Date型)
     * @return	日付
     */
    public static Date convertDate( String date ) throws ParseException {
        //現在日を取得する
        @SuppressLint( "SimpleDateFormat" )
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd" );
        return sdf.parse( date );
    }

    /**
     * 時間の変換(文字列⇒Date型)
     * @return	時間
     */
    public static Date convertTime( String date ) throws ParseException {
        //現在日を取得する
        @SuppressLint( "SimpleDateFormat" )
        SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm:ss" );
        return sdf.parse( date );
    }

    /**
     * 日付計算(2つの日付の差)
     * @param from      日付1
     * @param to        日付2
     * @return
     */
    public static Date diffDate( Date from, Date to ){
        Date    result      = null;
        long    from_time   = from.getTime();
        long    to_time     = to.getTime();

        long    diff        = to_time - from_time;

        result.setTime( diff );

        return result;
    }
    //endregion

    //region Nullチェック
    /**
     * NULLチェック
     * @param value     チェック対象文字
     * @return
     */
    public static boolean isNullOrZeroLength( String value ){
        return android.text.TextUtils.isEmpty( value );
    }
    //endregion

    /***************************************************************************
     * メソッド(ネットワーク接続状況チェック)
     ***************************************************************************/
    //region 通信チェック
    /**
     * ネットワーク接続チェック
     * 外部に繋がらない場合を想定
     * @return	成否(true=正常, false=異常)[
     * Activity(インスタンス)から呼び出すこと
     */
    public static boolean network_check(Context context){
        ConnectivityManager cm		= (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo         info	= cm.getActiveNetworkInfo();

        //ネットワークが接続されているかチェック
        if( info != null ){
            return info.isConnected();
        } else {
            return false;
        }
    }
    //endregion
}
