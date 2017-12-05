package jp.kitoha.ninow.Network.Core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import jp.kitoha.ninow.Common.ErrorCode;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import okio.Buffer;

/**
 * @brief       HTTP通信処理用ライブラリ
 * @note
 * @copyright   (c)copyright 2016 KITOHA All Rights Reserved.
 */
public class HttpUtility {
	/***************************************************************************
	 * メソッド
	 ***************************************************************************/
	/**
	 * コンストラクタ
	 */
	public HttpUtility(){
	}

	/***************************************************************************
	 * メソッド(Http要求送信／応答受信)
	 ***************************************************************************/
	/**
	 * 接続パラメータチェック
	 * @return	成否(true=正常, false=異常)
	 */
	public static int required_check(){
		int				ret				= ErrorCode.STS_OK;

		//製品版では、ここで認証情報をチェックする

		return ret;
	}

	/**
	 * Getリクエストの送信
	 * @param	url(in)		Getリクエストを送信するURL(パラメータ含む)
	 * @return	受領したデータ(文字列)
	 */
	public static Response get( String url ){
		String		res    		= "";                                                  	//HTTPレスポンス
		Request		request		= new Request.Builder().url( url ).get().build();		//リクエストの生成
		Response	response	= null;

		//OKHttpにてHttp通信
		OkHttpClient client  = new OkHttpClient();
		try{
			response	= client.newCall( request ).execute();
			//res     	= response.body().string();
		}catch( Exception e ){
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Postリクエストの送信
	 * @param url(in)       URL
	 * @param param(in)     パラメータ
	 * @return	受領したデータ(文字列)
	 */
	public static Response post(String url, RequestBody param){
		String		res			= "";                                                   //HTTPレスポンス
		Buffer		buffer		= new Buffer();
		String		content_length;
		Response	response	= null;



		try{
			param.writeTo( buffer );
			content_length = String.valueOf( buffer.size() );
		}catch(IOException e){
			e.printStackTrace();
			content_length = "-1";
		}finally{
			buffer.close();
		}

		Request request = new Request.Builder()
				.addHeader( "Content-Length", content_length )
				.url( url ).post( param ).build();		                                //リクエストの生成

		//OKHttpにてHttp通信
		OkHttpClient    client  = new OkHttpClient();
		try{
			response	= client.newCall( request ).execute();
			//res     	= response.body().string();
		}catch( IOException e ){
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Putリクエストの送信
	 * @param url(in)       URL
	 * @param param(in)     パラメータ
	 * @return	受領したデータ(文字列)
	 */
	public static Response put(String url, RequestBody param){
		String		res      	= "";                                                           //HTTPレスポンス
		Request		request		= new Request.Builder().url( url ).put( param ).build();		//リクエストの生成
		Response	response	= null;

		//OKHttpにてHttp通信
		OkHttpClient    client  = new OkHttpClient();
		try{
			response	= client.newCall( request ).execute();
			//res			= response.body().string();
		}catch( IOException e ){
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Deleteリクエストの送信
	 * @param url(in)       URL
	 * @return	受領したデータ(文字列)
	 */
	public static Response delete( String url ){
		String		res      	= "";                                                           //HTTPレスポンス
		Request		request		= new Request.Builder().url( url ).delete().build();		    //リクエストの生成
		Response	response	= null;

		//OKHttpにてHttp通信
		OkHttpClient    client  = new OkHttpClient();
		try{
			response	= client.newCall( request ).execute();
			//res     	= response.body().string();
		}catch( IOException e ){
			e.printStackTrace();
		}

		return response;
	}
}
