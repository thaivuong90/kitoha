package jp.kitoha.ninow.Network.AsyncTask;

import android.content.Context;

import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import jp.kitoha.ninow.Common.ErrorCode;
import jp.kitoha.ninow.IO.DB.Adapter.DtbOrders;
import jp.kitoha.ninow.Network.Core.AsyncTaskCallbacks;
import jp.kitoha.ninow.Network.HttpCommand;

/******************************************************************************
 * @brief		コースリスト受信通信処理クラス
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class GetCourseListAsyncTask extends BaseAsyncTask{
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
    public GetCourseListAsyncTask(Context context, AsyncTaskCallbacks callback) {
        super(context, callback);
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
        ret = command.get_course_list();
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
            //配送一覧の処理
            ret = setDeliveryList( body );
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
    private int setDeliveryList( String body ){
        int         ret     = ErrorCode.STS_OK;
        String      sql     = "";
        String      list    = "{";

        DtbOrders dtb_orders  = new DtbOrders( this.context );

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
            JSONObject  json_body       = json_response.getJSONObject( 0 );
            if( !json_body.has( "Route" ) ){
                return ErrorCode.STS_NG_NOT_FOUND_DATA;
            }
            JSONObject  json_orders     = json_body.getJSONObject( "Route" );

            //値リストの設定
            for (int idx = 0; idx < json_orders.length(); idx++) {
                //1件ずつ処理する
                JSONObject	rec		= json_orders.getJSONObject( String.valueOf( idx + 1 ) );

                list   += "\"id\":\"" + rec.getInt( "id" ) + "\","
                        + "\"order_id\":\"" + rec.getInt( "order_id" ) + "\","
                        + "\"distribution_id\":\"" + rec.getInt( "distribution_id" ) + "\","
                        + "\"order_no\":\"" + rec.getString( "order_no" ) + "\","
                        + "\"order_date\":\"" + rec.getString( "order_date" ) + "\","
                        + "\"sub_voucher_no\":\"" + rec.getString( "sub_voucher_no" ) + "\","
                        + "\"voucher_no\":\"" + rec.getString( "voucher_no" ) + "\","
                        + "\"conveyance_type\":\"" + rec.getInt( "conveyance_type" ) + "\","
                        + "\"pickup_type\":\"" + ( rec.isNull( "pickup_type" ) ? 0 : rec.getInt( "pickup_type" ) ) + "\","
                        + "\"transport_type\":\"" + ( rec.isNull( "transport_type" ) ? 0 : rec.getInt( "transport_type" ) ) + "\","
                        + "\"charge_type\":\"" + ( rec.isNull( "charge_type" ) ? 0 : rec.getInt( "charge_type" ) ) + "\","
                        + "\"pickup_date\":\"" + rec.getString( "pickup_date" ) + "\","
                        + "\"pickup_time_order\":\"" + ( rec.isNull( "pickup_time_order" ) ? 0 : rec.getInt( "pickup_time_order" ) ) + "\","
                        + "\"pickup_time_from\":\"" + rec.getString( "pickup_time_from" ) + "\","
                        + "\"pickup_time_to\":\"" + rec.getString( "pickup_time_to" ) + "\","
                        + "\"pickup_range\":\"" + rec.getString( "pickup_range" ) + "\","
                        + "\"pickup_memo\":\"" + rec.getString( "pickup_memo" ) + "\","
                        + "\"delivery_date\":\"" + rec.getString( "delivery_date" ) + "\","
                        + "\"delivery_time_order\":\"" + ( rec.isNull( "delivery_time_order" ) ? 0 : rec.getInt( "delivery_time_order" ) ) + "\","
                        + "\"delivery_time_from\":\"" + rec.getString( "delivery_time_from" ) + "\","
                        + "\"delivery_time_to\":\"" + rec.getString( "delivery_time_to" ) + "\","
                        + "\"delivery_range\":\"" + rec.getString( "delivery_range" ) + "\","
                        + "\"delivery_memo\":\"" + rec.getString( "delivery_memo" ) + "\","
                        + "\"handling_memo\":\"" + rec.getString( "handling_memo" ) + "\","
                        + "\"additional_memo\":\"" + rec.getString( "additional_memo" ) + "\","
                        + "\"special_contents\":\"" + rec.getString( "special_contents" ) + "\","
                        + "\"delivery_no\":\"" + rec.getString( "delivery_no" ) + "\","
                        + "\"shipment_no\":\"" + rec.getString( "shipment_no" ) + "\","
                        + "\"req_number\":\"" + ( rec.isNull( "req_number" ) ? 0 : rec.getInt( "req_number" ) ) + "\","
                        + "\"req_num_unit\":\"" + rec.getString( "req_num_unit" ) + "\","
                        + "\"req_weight\":\"" + ( rec.isNull( "req_weight" ) ? 0 : rec.getInt( "req_weight" ) ) + "\","
                        + "\"req_weight_unit\":\"" + rec.getString( "req_weight_unit" ) + "\","
                        + "\"req_volume\":\"" + ( rec.isNull( "req_volume" ) ? 0 : rec.getInt( "req_volume" ) ) + "\","
                        + "\"req_volume_unit\":\"" + rec.getString( "req_volume_unit" ) + "\","
                        + "\"req_unit_load\":\"" + ( rec.isNull( "req_unit_load" ) ? 0 : rec.getInt( "req_unit_load" ) ) + "\","
                        + "\"report_number\":\"" + ( rec.isNull( "report_number" ) ? 0 : rec.getInt( "report_number" ) ) + "\","
                        + "\"report_num_unit\":\"" + rec.getString( "report_num_unit" ) + "\","
                        + "\"report_weight\":\"" + ( rec.isNull( "report_weight" ) ? 0 : rec.getInt( "report_weight" ) ) + "\","
                        + "\"report_weight_unit\":\"" + rec.getString( "report_weight_unit" ) + "\","
                        + "\"report_volume\":\"" + ( rec.isNull( "report_volume" ) ? 0 : rec.getInt( "report_volume" ) ) + "\","
                        + "\"report_volume_unit\":\"" + rec.getString( "report_volume_unit" ) + "\","
                        + "\"line_no\":\"" + ( rec.isNull( "line_no" ) ? 0 : rec.getInt( "line_no" ) ) + "\","
                        + "\"line_order_no\":\"" + rec.getString( "line_order_no" ) + "\","
                        + "\"line_part_number\":\"" + ( rec.isNull( "line_part_number" ) ? 0 : rec.getInt( "line_part_number" ) ) + "\","
                        + "\"line_tracking_no\":\"" + rec.getString( "line_tracking_no" ) + "\","
                        + "\"line_license_plate_no\":\"" + rec.getString( "line_license_plate_no" ) + "\","
                        + "\"line_seq_no\":\"" + ( rec.isNull( "line_seq_no" ) ? 0 : rec.getInt( "line_seq_no" ) ) + "\","
                        + "\"line_branch_no\":\"" + ( rec.isNull( "line_branch_no" ) ? 0 : rec.getInt( "line_branch_no" ) ) + "\","
                        + "\"line_order_control_no\":\"" + ( rec.isNull( "line_order_control_no" ) ? 0 : rec.getInt( "line_order_control_no" ) ) + "\","
                        + "\"line_owner_product_code\":\"" + rec.getString( "line_owner_product_code" ) + "\","
                        + "\"line_contractor_product_code\":\"" + rec.getString( "line_contractor_product_code" ) + "\","
                        + "\"line_product_name1\":\"" + rec.getString( "line_product_name1" ) + "\","
                        + "\"line_product_name2\":\"" + rec.getString( "line_product_name2" ) + "\","
                        + "\"line_product_name\":\"" + rec.getString( "line_product_name" ) + "\","
                        + "\"line_unit_load\":\"" + ( rec.isNull( "line_unit_load" ) ? 0 : rec.getInt( "line_unit_load" ) ) + "\","
                        + "\"line_req_number\":\"" + ( rec.isNull( "line_req_number" ) ? 0 : rec.getInt( "line_req_number" ) ) + "\","
                        + "\"line_num_unit\":\"" + rec.getString( "line_num_unit" ) + "\","
                        + "\"line_weight\":\"" + ( rec.isNull( "line_weight" ) ? 0 : rec.getInt( "line_weight" ) ) + "\","
                        + "\"line_weight_unit\":\"" + rec.getString( "line_weight_unit" ) + "\","
                        + "\"line_volume\":\"" + ( rec.isNull( "line_volume" ) ? 0 : rec.getInt( "line_volume" ) ) + "\","
                        + "\"line_volume_unit\":\"" + rec.getString( "line_volume_unit" ) + "\","
                        + "\"line_quantity\":\"" + ( rec.isNull( "line_quantity" ) ? 0 : rec.getInt( "line_quantity" ) ) + "\","
                        + "\"line_quantity_unit\":\"" + rec.getString( "line_quantity_unit" ) + "\","
                        + "\"dimensions\":\"" + rec.getString( "dimensions" ) + "\","
                        + "\"package_code\":\"" + rec.getString( "package_code" ) + "\","
                        + "\"package_name\":\"" + rec.getString( "package_name" ) + "\","
                        + "\"baggage_handling_memo\":\"" + rec.getString( "baggage_handling_memo" ) + "\","
                        + "\"line_fares\":\"" + ( rec.isNull( "line_fares" ) ? 0 : rec.getInt( "line_fares" ) ) + "\","
                        + "\"shipper_code\":\"" + rec.getString( "shipper_code" ) + "\","
                        + "\"shipper_name\":\"" + rec.getString( "shipper_name" ) + "\","
                        + "\"shipper_section_code\":\"" + rec.getString( "shipper_section_code" ) + "\","
                        + "\"shipper_section_name\":\"" + rec.getString( "shipper_section_name" ) + "\","
                        + "\"shipper_contact\":\"" + rec.getString( "shipper_contact" ) + "\","
                        + "\"shipper_phone\":\"" + rec.getString( "shipper_phone" ) + "\","
                        + "\"shipper_address\":\"" + rec.getString( "shipper_address" ) + "\","
                        + "\"shipper_post_code\":\"" + rec.getString( "shipper_post_code" ) + "\","
                        + "\"client_id\":\"" + ( rec.isNull( "client_id" ) ? 0 : rec.getInt( "client_id" ) ) + "\","
                        + "\"client_code\":\"" + rec.getString( "client_code" ) + "\","
                        + "\"client_name\":\"" + rec.getString( "client_name" ) + "\","
                        + "\"client_section_code\":\"" + rec.getString( "client_section_code" ) + "\","
                        + "\"client_section_name\":\"" + rec.getString( "client_section_name" ) + "\","
                        + "\"client_phone\":\"" + rec.getString( "client_phone" ) + "\","
                        + "\"client_address\":\"" + rec.getString( "client_address" ) + "\","
                        + "\"client_post_code\":\"" + rec.getString( "client_post_code" ) + "\","
                        + "\"consignee_code\":\"" + rec.getString( "consignee_code" ) + "\","
                        + "\"consignee_name\":\"" + rec.getString( "consignee_name" ) + "\","
                        + "\"consignee_section_code\":\"" + rec.getString( "consignee_section_code" ) + "\","
                        + "\"consignee_section_name\":\"" + rec.getString( "consignee_section_name" ) + "\","
                        + "\"consignee_contact\":\"" + rec.getString( "consignee_contact" ) + "\","
                        + "\"consignee_phone\":\"" + rec.getString( "consignee_phone" ) + "\","
                        + "\"consignee_address\":\"" + rec.getString( "consignee_address" ) + "\","
                        + "\"consignee_post_code\":\"" + rec.getString( "consignee_post_code" ) + "\","
                        + "\"carrier_id\":\"" + ( rec.isNull( "carrier_id" ) ? 0 : rec.getInt( "carrier_id" ) ) + "\","
                        + "\"carrier_code\":\"" + rec.getString( "carrier_code" ) + "\","
                        + "\"carrier_name\":\"" + rec.getString( "carrier_name" ) + "\","
                        + "\"carrier_center_code_from\":\"" + rec.getString( "carrier_center_code_from" ) + "\","
                        + "\"carrier_center_name_from\":\"" + rec.getString( "carrier_center_name_from" ) + "\","
                        + "\"carrier_center_phone_from\":\"" + rec.getString( "carrier_center_phone_from" ) + "\","
                        + "\"carrier_center_code_to\":\"" + rec.getString( "carrier_center_code_to" ) + "\","
                        + "\"carrier_center_name_to\":\"" + rec.getString( "carrier_center_name_to" ) + "\","
                        + "\"billing_id\":\"" + ( rec.isNull( "billing_id" ) ? 0 : rec.getInt( "billing_id" ) ) + "\","
                        + "\"billing_code\":\"" + rec.getString( "billing_code" ) + "\","
                        + "\"billing_name\":\"" + rec.getString( "billing_name" ) + "\","
                        + "\"billing_section_code\":\"" + rec.getString( "billing_section_code" ) + "\","
                        + "\"billing_section_name\":\"" + rec.getString( "billing_section_name" ) + "\","
                        + "\"shipping_code\":\"" + rec.getString( "shipping_code" ) + "\","
                        + "\"shipping_name\":\"" + rec.getString( "shipping_name" ) + "\","
                        + "\"shipping_section_code\":\"" + rec.getString( "shipping_section_code" ) + "\","
                        + "\"shipping_section_name\":\"" + rec.getString( "shipping_section_name" ) + "\","
                        + "\"shipping_phone\":\"" + rec.getString( "shipping_phone" ) + "\","
                        + "\"shipping_address_code\":\"" + rec.getString( "shipping_address_code" ) + "\","
                        + "\"shipping_address\":\"" + rec.getString( "shipping_address" ) + "\","
                        + "\"destination_code\":\"" + rec.getString( "destination_code" ) + "\","
                        + "\"destination_name\":\"" + rec.getString( "destination_name" ) + "\","
                        + "\"destination_section_code\":\"" + rec.getString( "destination_section_code" ) + "\","
                        + "\"destination_section_name\":\"" + rec.getString( "destination_section_name" ) + "\","
                        + "\"destination_contact_code\":\"" + rec.getString( "destination_contact_code" ) + "\","
                        + "\"destination_contact\":\"" + rec.getString( "destination_contact" ) + "\","
                        + "\"destination_phone\":\"" + rec.getString( "destination_phone" ) + "\","
                        + "\"destination_city_code\":\"" + rec.getString( "destination_city_code" ) + "\","
                        + "\"destination_address_code\":\"" + rec.getString( "destination_address_code" ) + "\","
                        + "\"destination_address\":\"" + rec.getString( "destination_address" ) + "\","
                        + "\"destination_post_code\":\"" + rec.getString( "destination_post_code" ) + "\","
                        + "\"seal_number\":\"" + ( rec.isNull( "seal_number" ) ? 0 : rec.getInt( "seal_number" ) ) + "\","
                        + "\"seal_num_unit\":\"" + rec.getString( "seal_num_unit" ) + "\","
                        + "\"seal_weight\":\"" + ( rec.isNull( "seal_weight" ) ? 0 : rec.getInt( "seal_weight" ) ) + "\","
                        + "\"seal_weight_unit\":\"" + rec.getString( "seal_weight_unit" ) + "\","
                        + "\"seal_volume\":\"" + ( rec.isNull( "seal_volume" ) ? 0 : rec.getInt( "seal_volume" ) ) + "\","
                        + "\"seal_volume_unit\":\"" + rec.getString( "seal_volume_unit" ) + "\","
                        + "\"instruction_no\":\"" + rec.getString( "instruction_no" ) + "\","
                        + "\"route_id\":\"" + ( rec.isNull( "route_id" ) ? 0 : rec.getInt( "route_id" ) ) + "\","
                        + "\"transmission_order_no\":\"" + rec.getInt( "transmission_order_no" ) + "\","
                        + "\"size\":\"" + rec.getString( "size" ) + "\","
                        + "\"luggage_type\":\"" + ( rec.isNull( "luggage_type" ) ? 0 : rec.getInt( "luggage_type" ) ) + "\","
                        + "\"service_type\":\"" + ( rec.isNull( "service_type" ) ? 0 : rec.getInt( "service_type" ) ) + "\","
                        + "\"is_weight\":\"" + ( rec.isNull( "is_weight" ) ? 0 : rec.getInt( "is_weight" ) ) + "\","
                        + "\"is_contact\":\"" + ( rec.isNull( "is_contact" ) ? 0 : rec.getInt( "is_contact" ) ) + "\","
                        + "\"is_before_vote\":\"" + ( rec.isNull( "is_before_vote" ) ? 0 : rec.getBoolean( "is_before_vote" ) ) + "\","
                        + "\"previous_check_time\":\"" + rec.getString( "previous_check_time" ) + "\","
                        + "\"previous_check_result\":\"" + rec.getString( "previous_check_result" ) + "\","
                        + "\"base_fare\":\"" + ( rec.isNull( "base_fare" ) ? 0 : rec.getInt( "base_fare" ) ) + "\","
                        + "\"incidental_charge\":\"" + ( rec.isNull( "incidental_charge" ) ? 0 : rec.getInt( "incidental_charge" ) ) + "\","
                        + "\"insurance_code\":\"" + ( rec.isNull( "insurance_code" ) ? 0 : rec.getInt( "insurance_code" ) ) + "\","
                        + "\"insurance_clime\":\"" + ( rec.isNull( "insurance_clime" ) ? 0 : rec.getInt( "insurance_clime" ) ) + "\","
                        + "\"insurance_fee\":\"" + ( rec.isNull( "insurance_fee" ) ? 0 : rec.getInt( "insurance_fee" ) ) + "\","
                        + "\"tax_code\":\"" + ( rec.isNull( "tax_code" ) ? 0 : rec.getInt( "tax_code" ) ) + "\","
                        + "\"item_price\":\"" + ( rec.isNull( "item_price" ) ? 0 : rec.getInt( "item_price" ) ) + "\","
                        + "\"item_tax\":\"" + ( rec.isNull( "item_tax" ) ? 0 : rec.getInt( "item_tax" ) ) + "\","
                        + "\"total_fare\":\"" + ( rec.isNull( "total_fare" ) ? 0 : rec.getInt( "total_fare" ) ) + "\","
                        + "\"total_tax_free\":\"" + ( rec.isNull( "total_tax_free" ) ? 0 : rec.getInt( "total_tax_free" ) ) + "\","
                        + "\"regist_time\":\"" + rec.getString( "regist_time" ) + "\","
//						+ "\"regist_check_time\":\"" + rec.getString( "regist_check_time" ) + "\","
                        + "\"loaded_time\":\"" + rec.getString( "loaded_time" ) + "\","
                        + "\"start_time\":\"" + rec.getString( "start_time" ) + "\","
//						+ "\"destination_start_time\":\"" + rec.getString( "destination_start_time" ) + "\","
                        + "\"pickup_time\":\"" + rec.getString( "pickup_time" ) + "\","
                        + "\"delivery_time\":\"" + rec.getString( "delivery_time" ) + "\","
                        + "\"return_time\":\"" + rec.getString( "return_time" ) + "\","
                        + "\"unloaded_time\":\"" + rec.getString( "unloaded_time" ) + "\","
                        + "\"receipt_time\":\"" + rec.getString( "receipt_time" ) + "\","
//						+ "\"storage_id\":\"" + ( rec.isNull( "storage_id" ) ? 0 : rec.getInt( "storage_id" ) ) + "\","
                        + "\"next_status\":\"" + ( rec.isNull( "next_status" ) ? 0 : rec.getInt( "next_status" ) ) + "\","
                        + "\"status\":\"" + ( rec.isNull( "status" ) ? 0 : rec.getInt( "status" ) ) + "\","
                        + "\"cancel_loading_reason\":\"" + ( rec.isNull( "cancel_loading_reason" ) ? 0 : rec.getInt( "cancel_loading_reason" ) ) + "\","
                        + "\"cancel_loading_reason_text\":\"" + rec.getString( "cancel_loading_reason_text" ) + "\","
                        + "\"cancel_reason\":\"" + ( rec.isNull( "cancel_reason" ) ? 0 : rec.getInt( "cancel_reason" ) ) + "\","
                        + "\"cancel_reason_text\":\"" + rec.getString( "cancel_reason_text" ) + "\","
                        + "\"cancel_after_reason\":\"" + ( rec.isNull( "cancel_after_reason" ) ? 0 : rec.getInt( "cancel_after_reason" ) ) + "\","
                        + "\"cancel_after_reason_text\":\"" + rec.getString( "cancel_after_reason_text" ) + "\","
                        + "\"cancel_process\":\"" + ( rec.isNull( "cancel_process" ) ? 0 : rec.getInt( "cancel_process" ) ) + "\","
                        + "\"registrant_type\":\"" + ( rec.isNull( "registrant_type" ) ? 0 : rec.getInt( "registrant_type" ) ) + "\","
                        + "\"registrant_id\":\"" + ( rec.isNull( "registrant_id" ) ? 0 : rec.getInt( "registrant_id" ) ) + "\","
                        + "\"created\":\"" + rec.getString( "created" ) + "\","
                        + "\"modified\":\"" + rec.getString( "modified" ) + "\","
                        + "\"driver\":\"" + ( rec.isNull( "driver" ) ? 0 : rec.getInt( "driver" ) ) + "\""
                        + "}";

                sql     = dtb_orders.getInsertStatement( rec );
                long    ins_ret     = dtb_orders.insert( sql );
            }
            //末尾がカンマの場合は、カンマを削除
            if( list.endsWith( "," ) ) {
                list = list.substring( 0, list.length() - 1);
            }
            list    += "}";

            //実行時設定に車両一覧を設定
            run_info.setDeliveryList( list );

        } catch (JSONException e) {
            e.printStackTrace();
            ret = ErrorCode.STS_NG;
        }

        return ret;
    }
}
