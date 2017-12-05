package jp.kitoha.ninow.Network.AsyncTask;

import android.content.Context;

import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import jp.kitoha.ninow.Common.ErrorCode;
import jp.kitoha.ninow.IO.DB.Adapter.MtbDrivers;
import jp.kitoha.ninow.Network.Core.AsyncTaskCallbacks;
import jp.kitoha.ninow.Network.HttpCommand;
import jp.kitoha.ninow.Network.RequestCode;

/******************************************************************************
 * @brief		ドライバーリスト受信通信処理クラス
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class GetDriverListAsyncTask extends BaseAsyncTask{
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
    public GetDriverListAsyncTask(Context context, AsyncTaskCallbacks callback) {
        super( context, callback );
        this.request_code	= RequestCode.REQ_CODE_GET_DRIVER_LIST;
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
        HttpCommand command		= new HttpCommand( car_no, driver );
        Response response;

        //実行
        ret = command.get_driver_list();
        if( ret != ErrorCode.STS_OK ){
            return ret;
        }

        //Httpレスポンスを解析する(業務開始の場合は不要)
        response	= command.get_http_response();
        ret			= parse_response( response );

        return ret;
    }

    /***
     * HTTPレスポンスの解析
     * @param response	HTTPレスポンス
     * @return
     */
    //@Override
    protected int parse_response( Response response ){
        int			ret 		= ErrorCode.STS_OK;
        String      body        = "";
        String      sql         = "";

        //レスポンスがない場合はエラー
        if( response == null ){
            return ErrorCode.STS_NG;
        }

        //レスポンスのボディー部(JSON形式)を取得する
        try {
            body	= response.body().string();
            //車両一覧の処理
            ret = set_driver_list( body );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

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
    /**
     * データ登録
     * @param body  HTTPレスポンス(車両リスト:JSON)
     * @return
     */
    private int set_driver_list( String body ){
        int         ret     = ErrorCode.STS_OK;
        long        ins_ret = 0;
        String      sql     = "";
        String      list    = "{";

        MtbDrivers mtb_drivers  = new MtbDrivers( this.context );

        try {
            //HTTPレスポンスのJSONを解析する
            JSONObject json = new JSONObject( body );
            //エラーデータがある場合は、処理しない
            if( json.has( "error" ) ){
                return ErrorCode.STS_NG;
            }
            //正常応答でデータなしの場合は、処理しない
            if( !json.has( "response" ) ){
                return ErrorCode.STS_NG_NOT_FOUND_DATA;
            }

            JSONArray   json_response   = json.getJSONArray( "response" );
            JSONObject  json_driver     = json_response.getJSONObject( 0 );
            if( !json_driver.has( "Driver" ) ){
                return ErrorCode.STS_NG_NOT_FOUND_DATA;
            }
            JSONObject  json_drivers     = json_driver.getJSONObject( "Driver" );

            //値リストの設定
            for (int idx = 0; idx < json_drivers.length(); idx++) {
                //1件ずつ処理する
                JSONObject  rec = json_drivers.getJSONObject( String.valueOf( idx + 1 ) );

                list    += "\"" + rec.getString( "id" ) +  "\":\"" + rec.getString( "name" ) + "\",";

                sql     = mtb_drivers.getInsertStatement( rec );
                ins_ret = mtb_drivers.insert( sql );
            }
            //末尾がカンマの場合は、カンマを削除
            if( list.endsWith( "," ) ) {
                list = list.substring( 0, list.length() - 1);
            }
            list    += "}";

            //実行時設定に車両一覧を設定
            run_info.setDriverList( list );

        } catch (JSONException e) {
            e.printStackTrace();
            ret = ErrorCode.STS_NG;
        }

        return ret;
    }
}
