package jp.kitoha.ninow.Network.AsyncTask;

import android.content.Context;

import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import jp.kitoha.ninow.Common.ErrorCode;
import jp.kitoha.ninow.Common.ScreenCode;
import jp.kitoha.ninow.IO.DB.Adapter.DtbOrders;
import jp.kitoha.ninow.IO.DB.Adapter.MtbTrucks;
import jp.kitoha.ninow.Network.Core.AsyncTaskCallbacks;
import jp.kitoha.ninow.Network.HttpCommand;
import jp.kitoha.ninow.Network.RequestCode;

/******************************************************************************
 * @brief		配送指示取り消し処理クラス
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class ClearDtbOrdersAsyncTask extends BaseAsyncTask{
    /***************************************************************************
     * インスタンス変数
     ***************************************************************************/

    /***************************************************************************
     * コンストラクタ
     ***************************************************************************/
    /**
     * @param context
     * @param callback
     * @brief コンストラクタ
     */
    public ClearDtbOrdersAsyncTask(Context context, AsyncTaskCallbacks callback) {
        super( context, callback );
        this.request_code	= RequestCode.REQ_CODE_CLEAR_COURSE_INFO;
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

        return ret;
    }

    /***
     * @brief	メイン処理
     * @return	成否(STS_OK:正常, STS_OK以外:エラー)
     ***/
    @Override
    protected int main_proc(){
        int			ret 		= ErrorCode.STS_OK;
        long        ins_ret     = 0;

        //DBのクリア
        DtbOrders dtb_orders  = new DtbOrders( this.context );
        String      sql         = dtb_orders.getDeleteStatment();
        ins_ret     = dtb_orders.delete( sql );

        //クリアした情報で実行情報を再更新
        run_info.setDeliveryList( "" );

        return ret;
    }

    /***
     * @brief	事後処理
     * @return	成否(STS_OK:正常, STS_OK以外:エラー)
     ***/
    @Override
    protected int term_proc(){
        int		ret 			= ErrorCode.STS_OK;

        return ret;
    }

    /***************************************************************************
     * メソッド(このクラス固有)
     ***************************************************************************/
}
