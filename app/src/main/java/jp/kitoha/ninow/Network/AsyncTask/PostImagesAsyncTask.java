package jp.kitoha.ninow.Network.AsyncTask;

import android.content.Context;

import jp.kitoha.ninow.Common.ErrorCode;
import jp.kitoha.ninow.Network.Core.AsyncTaskCallbacks;
import jp.kitoha.ninow.Network.HttpCommand;
import jp.kitoha.ninow.Network.RequestCode;

/******************************************************************************
 * @brief		画像アップロード通信処理クラス
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class PostImagesAsyncTask extends BaseAsyncTask {
	/***************************************************************************
	 * インスタンス変数
	 ***************************************************************************/
	private String	delivery_date;										//配送日
	private String	instruct_no;										//取得条件
	private String	transmission_order_no;								//路順
	private	String	image;												//画像
	private	String	thumbnail;											//サムネイル
	private int		type;												//種別
	private int		seq_no;												//表示順

	/***************************************************************************
	 * コンストラクタ
	 ***************************************************************************/
	/**
	 * @param context
	 * @param callback
	 * @brief コンストラクタ
	 */
	public PostImagesAsyncTask( Context context, AsyncTaskCallbacks callback ) {
		super( context, callback );
		this.request_code	= RequestCode.REQ_CODE_POST_IMAGES;
	}

	/***************************************************************************
	 * メソッド
	 ***************************************************************************/
	/**
	 * @brief	事前処理
	 * @param	params(in)	パラメータ
	 * @return	成否(STS_OK:正常, STS_OK以外:エラー)
	 */
	@Override
	protected int pre_proc( String... params ){
		int		ret 			= ErrorCode.STS_OK;

		//パラメータの解析
		if( params.length == 0 ) {
			ret			= ErrorCode.STS_NG;
		}else{
			car_no					= params[0];
			driver					= params[1];
			latitude				= Double.parseDouble( params[2] );
			longitude				= Double.parseDouble( params[3] );
			accuracy				= Float.parseFloat( params[4] );
			delivery_date			= params[5];
			instruct_no				= params[6];
			transmission_order_no	= params[7];
			type					= Integer.parseInt( params[8] );
			seq_no					= Integer.parseInt( params[9] );
			image					= params[10];
			thumbnail				= params[11];
		}

		return ret;
	}

	/***
	 * @brief	メイン処理
	 * @return	成否(STS_OK:正常, STS_OK以外:エラー)
	 ***/
	@Override
	protected int main_proc(){
		int			ret 		= ErrorCode.STS_OK;
		HttpCommand	command		= new HttpCommand( car_no, driver );
		//Response	response;

		//実行
		ret = command.post_images( delivery_date, instruct_no, transmission_order_no,
			seq_no, image, thumbnail, type, latitude, longitude, accuracy );
		if( ret != ErrorCode.STS_OK ){
			return ret;
		}

		//Httpレスポンスを解析する(業務開始の場合は不要)
		//response	= command.get_http_response();
		//ret			= parse_response( response );

		return ret;
	}

	/***
	 * HTTPレスポンスの解析
	 * @param response	HTTPレスポンス
	 * @return
	 */
	//@Override
	//protected int parse_response( Response response ){
	//	int			ret 		= ErrorCode.STS_OK;
	//
	//	return ret;
	//}

	/***
	 * @brief	事後処理
	 * @return	成否(STS_OK:正常, STS_OK以外:エラー)
	 ***/
	@Override
	protected int term_proc(){
		int		ret 			= ErrorCode.STS_OK;

		return ret;
	}
}
