package jp.kitoha.ninow.Network.AsyncTask;

import android.content.Context;

import jp.kitoha.ninow.Common.ErrorCode;
import jp.kitoha.ninow.Common.Utility;
import jp.kitoha.ninow.Data.Config.RunInfo;
import jp.kitoha.ninow.Network.Core.AsyncTaskCallbacks;
import jp.kitoha.ninow.Network.HttpCommand;
import jp.kitoha.ninow.Network.RequestCode;

/******************************************************************************
 * @brief		帰着報告通信処理クラス
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class PostReturnEndReportAsyncTask extends BaseAsyncTask {
	/***************************************************************************
	 * インスタンス変数
	 ***************************************************************************/
	RunInfo			run_info;
	String			current_date;
	String			report_time;
	private String	delivery_date;										//配送日
	private String	instruct_no;										//配送指示番号

	/***************************************************************************
	 * コンストラクタ
	 ***************************************************************************/
	/**
	 * @param context
	 * @param callback
	 * @brief コンストラクタ
	 */
	public PostReturnEndReportAsyncTask( Context context, AsyncTaskCallbacks callback ) {
		super( context, callback );
		this.request_code	= RequestCode.REQ_CODE_POST_RETURN_END;
		run_info			= RunInfo.getInstance();
		current_date		= Utility.getToday();
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

		//業務開始の日付が設定されている場合は、上書き
		if( run_info.getCurrentDate() != "" ) {
			current_date	= run_info.getCurrentDate();
		}

		//報告情報を設定
		report_time			= Utility.getNow();			//報告日時
		run_info.setReturnTime( report_time );
		instruct_no			= run_info.getInstructNo();

		//実行
		ret = command.post_return_report( HttpCommand.TYPE_NO_END,
			instruct_no, latitude, longitude, accuracy, current_date, report_time );
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
