package jp.kitoha.ninow.Network.AsyncTask;

import android.content.Context;

import jp.kitoha.ninow.Common.Constants;
import jp.kitoha.ninow.Common.ErrorCode;
import jp.kitoha.ninow.Common.Utility;
import jp.kitoha.ninow.IO.DB.Adapter.DtbOrders;
import jp.kitoha.ninow.Network.Core.AsyncTaskCallbacks;
import jp.kitoha.ninow.Network.HttpCommand;
import jp.kitoha.ninow.Network.RequestCode;

/******************************************************************************
 * @brief		積込報告通信処理クラス
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class PostLoadingReportAsyncTask extends BaseAsyncTask {
	/***************************************************************************
	 * インスタンス変数
	 ***************************************************************************/
	private String	current_date;
	private String	report_time;
	private String	delivery_date;										//配送日
	private String	instruct_no;										//配送指示番号
	private String	except_reason_text;									//除外理由(積込キャンセル理由)
	private int		order_id;											//伝票番号
	private int		except_reason;										//除外理由(積込キャンセル理由)ID
	private int		status;												//ステータス

	/***************************************************************************
	 * コンストラクタ
	 ***************************************************************************/
	/**
	 * @param context
	 * @param callback
	 * @brief コンストラクタ
	 */
	public PostLoadingReportAsyncTask( Context context, AsyncTaskCallbacks callback ) {
		super( context, callback );
		this.request_code	= RequestCode.REQ_CODE_POST_LOADING_REPORT;
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
			delivery_date			= params[5];
			instruct_no				= params[6];
			order_id				= Integer.parseInt( params[7] );
			report_time				= params[8];
			status					= Integer.parseInt( params[9] );
			except_reason			= Integer.parseInt( params[10] );
			except_reason_text		= params[11];
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
		int			is_send		= 1;
		long		ins_ret		= 0;
		HttpCommand	command		= new HttpCommand( car_no, driver );
		//Response	response;

		//業務開始の日付が設定されている場合は、上書き
		if( run_info.getCurrentDate() != "" ) {
			current_date	= run_info.getCurrentDate();
		}

		//報告情報を設定
		report_time			= Utility.getNow();
		if( run_info.getLoadingStartTime().isEmpty() ) {
			run_info.setLoadingStartTime( report_time );
		}

		//実行
		ret = command.post_loading_report( delivery_date, instruct_no, order_id, status,
			except_reason, except_reason_text, latitude, longitude, accuracy, current_date, report_time );
		//送信結果の取得
		if( ret != ErrorCode.STS_OK ){
			is_send	= 0;
		}

		//DBの更新
		DtbOrders	dtb_orders  = new DtbOrders( this.context );
		String      sql 		= this.getUpdateStatement( is_send );
		ins_ret     = dtb_orders.update( sql );

		//Httpレスポンスを解析する(業務開始の場合は不要)
		//response	= command.get_http_response();
		//ret		= parse_response( response );

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

	/**
	 * @brief	更新ステートメント取得
	 * @param 	is_send_loaded(in)	送信結果
	 * @return
     */
	private String getUpdateStatement(int is_send_loaded){
		String query		= "";
		String report_name	= "";

		query   = "UPDATE " + Constants.TBL_DTB_ORDERS
				+ " SET status = " + status;

		if( report_name != "" ){
			query += ", loaded_time = '" + report_time + "'";
		}
		query += ", is_send_loaded = " + is_send_loaded;

		if( except_reason_text != "" ){
			query += ", cancel_loading_reason = '" + except_reason + "'";
			query += ", cancel_loading_reason_text = '" + except_reason_text + "'";
		}
		query += " WHERE order_id = " + order_id;
//		query += " WHERE ( voucher_no = '" + voucher_no + "'";
//		query += " OR ( voucher_no = '" + voucher_no + "'";
//		query += " OR ( sub_voucher_no = '" + voucher_no + "'";
//		query += " OR ( line_trucking_no = '" + voucher_no + "'";

		return query;
	}
}
