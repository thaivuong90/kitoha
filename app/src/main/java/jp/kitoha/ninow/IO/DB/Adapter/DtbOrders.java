package jp.kitoha.ninow.IO.DB.Adapter;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import jp.kitoha.ninow.Common.Constants;
import jp.kitoha.ninow.IO.DB.Core.DBManager;
import jp.kitoha.ninow.IO.DB.Core.ITableAdapter;

/**
 * @brief       DtbOrdersの操作
 * @note
 * @copyright   (c)copyright 2016 KITOHA All Rights Reserved.
 */
public class DtbOrders implements ITableAdapter {
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
    public DtbOrders(Context context) {
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
        sql     = "SELECT `_id`, `order_id`, `distribution_id`, `order_no`, `order_date`, `sub_voucher_no`, `voucher_no`, "
                + "`conveyance_type`, `pickup_type`, `transport_type`, `charge_type`, `pickup_date`, `pickup_time_order`, "
                + "`pickup_time_from`, `pickup_time_to`, `pickup_range`, `pickup_memo`, `delivery_date`, `delivery_time_order`, "
                + "`delivery_time_from`, `delivery_time_to`, `delivery_range`, `delivery_memo`, `handling_memo`, "
                + "`additional_memo`, `special_contents`, `delivery_no`, `shipment_no`, `req_number`, `req_num_unit`, "
                + "`req_weight`, `req_weight_unit`, `req_volume`, `req_volume_unit`, `req_unit_load`, `report_number`, "
                + "`report_num_unit`, `report_weight`, `report_weight_unit`, `report_volume`, `report_volume_unit`, "
                + "`line_no`, `line_order_no`, `line_part_number`, `line_tracking_no`, `line_license_plate_no`, "
                + "`line_seq_no`, `line_branch_no`, `line_order_control_no`, `line_owner_product_code`, "
                + "`line_contractor_product_code`, `line_product_name1`, `line_product_name2`, `line_product_name`, "
                + "`line_unit_load`, `line_req_number`, `line_num_unit`, `line_weight`, `line_weight_unit`, "
                + "`line_volume`, `line_volume_unit`, `line_quantity`, `line_quantity_unit`, `dimensions`, "
                + "`package_code`, `package_name`, `baggage_handling_memo`, `line_fares`, `shipper_code`, "
                + "`shipper_name`, `shipper_section_code`, `shipper_section_name`, `shipper_contact`, `shipper_phone`, "
                + "`shipper_address`, `shipper_post_code`, `client_id`, `client_code`, `client_name`, `client_section_code`, "
                + "`client_section_name`, `client_phone`, `client_address`, `client_post_code`, `consignee_code`, "
                + "`consignee_name`, `consignee_section_code`, `consignee_section_name`, `consignee_contact`, "
                + "`consignee_phone`, `consignee_address`, `consignee_post_code`, `carrier_id`, `carrier_code`, "
                + "`carrier_name`, `carrier_center_code_from`, `carrier_center_name_from`, `carrier_center_phone_from`, "
                + "`carrier_center_code_to`, `carrier_center_name_to`, `billing_id`, `billing_code`, `billing_name`, "
                + "`billing_section_code`, `billing_section_name`, `shipping_code`, `shipping_name`, `shipping_section_code`, "
                + "`shipping_section_name`, `shipping_phone`, `shipping_address_code`, `shipping_address`, `destination_code`, "
                + "`destination_name`, `destination_section_code`, `destination_section_name`, `destination_contact_code`, "
                + "`destination_contact`, `destination_phone`, `destination_city_code`, `destination_address_code`, "
                + "`destination_address`, `destination_post_code`, `seal_number`, `seal_num_unit`, `seal_weight`, "
                + "`seal_weight_unit`, `seal_volume`, `seal_volume_unit`, `instruction_no`, `route_id`, `transmission_order_no`, "
                + "`size`, `luggage_type`, `service_type`, `is_weight`, `is_contact`, `is_before_vote`, `previous_check_time`, "
                + "`previous_check_result`, `base_fare`, `incidental_charge`, `insurance_code`, `insurance_clime`, `insurance_fee`, "
                + "`tax_code`, `item_price`, `item_tax`, `total_fare`, `total_tax_free`, `regist_time`, `regist_check_time`, "
                + "`loaded_time`, `start_time`, `destination_start_time`, `pickup_time`, `delivery_time`, `return_time`, "
                + "`unloaded_time`, `receipt_time`, `storage_id`, `next_status`, `status`, `cancel_loading_reason`, "
                + "`cancel_loading_reason_text`, `cancel_reason`, `cancel_reason_text`, `cancel_after_reason`, "
                + "`cancel_after_reason_text`, `cancel_process`, `driver`, `proc_date`, `direction`, `dist_pickup_type`, "
                + "`registrant_id`, `created`, `modified` "
                + "FROM " + Constants.TBL_DTB_ORDERS;

        Cursor      cursor   = db_mgr.raw_query( sql );
        if( cursor.getCount() > 0 ) {
            return "";
        }

        //取得したデータをJSON形式に整形する
        JSONObject	json = new JSONObject();
        JSONArray list = new JSONArray();
        int			idx		= 0;
        try {
            if( cursor.getCount() > 0 ){
                //データを全件数処理する
                while( cursor.moveToNext() ){
                    idx	= 0;
                    JSONObject data = new JSONObject();
                    data.put( "_id",							cursor.getInt( idx++ ) );			//ID
                    data.put( "order_id",						cursor.getInt( idx++ ) );			//
                    data.put( "distribution_id",				cursor.getInt( idx++ ) );			//
                    data.put( "order_no",						cursor.getString( idx++ ) );		//
                    data.put( "order_date",						cursor.getString( idx++ ) );		//
                    data.put( "sub_voucher_no",					cursor.getString( idx++ ) );		//
                    data.put( "voucher_no",						cursor.getString( idx++ ) );		//
                    data.put( "conveyance_type",				cursor.getInt( idx++ ) );			//
                    data.put( "pickup_type",					cursor.getInt( idx++ ) );			//
                    data.put( "transport_type",					cursor.getInt( idx++ ) );			//
                    data.put( "charge_type",					cursor.getInt( idx++ ) );			//
                    data.put( "pickup_date",					cursor.getString( idx++ ) );		//
                    data.put( "pickup_time_order",				cursor.getInt( idx++ ) );			//
                    data.put( "pickup_time_from",				cursor.getString( idx++ ) );		//
                    data.put( "pickup_time_to",					cursor.getString( idx++ ) );		//
                    data.put( "pickup_range",					cursor.getString( idx++ ) );		//
                    data.put( "pickup_memo",					cursor.getString( idx++ ) );		//
                    data.put( "delivery_date",					cursor.getString( idx++ ) );		//
                    data.put( "delivery_time_order",			cursor.getInt( idx++ ) );			//
                    data.put( "delivery_time_from",				cursor.getString( idx++ ) );		//
                    data.put( "delivery_time_to",				cursor.getString( idx++ ) );		//
                    data.put( "delivery_range",					cursor.getString( idx++ ) );		//
                    data.put( "delivery_memo",					cursor.getString( idx++ ) );		//
                    data.put( "handling_memo",					cursor.getString( idx++ ) );		//
                    data.put( "additional_memo",				cursor.getString( idx++ ) );		//
                    data.put( "special_contents",				cursor.getString( idx++ ) );		//
                    data.put( "delivery_no",					cursor.getString( idx++ ) );		//
                    data.put( "shipment_no",					cursor.getString( idx++ ) );		//
                    data.put( "req_number",						cursor.getInt( idx++ ) );			//
                    data.put( "req_num_unit",					cursor.getString( idx++ ) );		//
                    data.put( "req_weight",						cursor.getInt( idx++ ) );			//
                    data.put( "req_weight_unit",				cursor.getString( idx++ ) );		//
                    data.put( "req_volume",						cursor.getInt( idx++ ) );			//
                    data.put( "req_volume_unit",				cursor.getString( idx++ ) );		//
                    data.put( "req_unit_load",					cursor.getInt( idx++ ) );			//
                    data.put( "report_number",					cursor.getInt( idx++ ) );			//
                    data.put( "report_num_unit",				cursor.getString( idx++ ) );		//
                    data.put( "report_weight",					cursor.getInt( idx++ ) );			//
                    data.put( "report_weight_unit",				cursor.getString( idx++ ) );		//
                    data.put( "report_volume",					cursor.getInt( idx++ ) );			//
                    data.put( "report_volume_unit",				cursor.getString( idx++ ) );		//
                    data.put( "line_no",						cursor.getString( idx++ ) );		//
                    data.put( "line_order_no",					cursor.getString( idx++ ) );		//
                    data.put( "line_part_number",				cursor.getInt( idx++ ) );			//
                    data.put( "line_tracking_no",				cursor.getString( idx++ ) );		//
                    data.put( "line_license_plate_no",			cursor.getString( idx++ ) );		//
                    data.put( "line_seq_no",					cursor.getInt( idx++ ) );			//
                    data.put( "line_branch_no",					cursor.getString( idx++ ) );		//
                    data.put( "line_order_control_no",			cursor.getString( idx++ ) );		//
                    data.put( "line_owner_product_code",		cursor.getString( idx++ ) );		//
                    data.put( "line_contractor_product_code",	cursor.getString( idx++ ) );		//
                    data.put( "line_product_name1",				cursor.getString( idx++ ) );		//
                    data.put( "line_product_name2",				cursor.getString( idx++ ) );		//
                    data.put( "line_product_name",				cursor.getString( idx++ ) );		//
                    data.put( "line_unit_load",					cursor.getInt( idx++ ) );			//
                    data.put( "line_req_number",				cursor.getInt( idx++ ) );			//
                    data.put( "line_num_unit",					cursor.getString( idx++ ) );		//
                    data.put( "line_weight",					cursor.getInt( idx++ ) );			//
                    data.put( "line_weight_unit",				cursor.getString( idx++ ) );		//
                    data.put( "line_volume",					cursor.getInt( idx++ ) );			//
                    data.put( "line_volume_unit",				cursor.getString( idx++ ) );		//
                    data.put( "line_quantity",					cursor.getInt( idx++ ) );			//
                    data.put( "line_quantity_unit",				cursor.getString( idx++ ) );		//
                    data.put( "dimensions",						cursor.getString( idx++ ) );		//
                    data.put( "package_code",					cursor.getString( idx++ ) );		//
                    data.put( "package_name",					cursor.getString( idx++ ) );		//
                    data.put( "baggage_handling_memo",			cursor.getString( idx++ ) );		//
                    data.put( "line_fares",						cursor.getInt( idx++ ) );			//
                    data.put( "shipper_code",					cursor.getString( idx++ ) );		//
                    data.put( "shipper_name",					cursor.getString( idx++ ) );		//
                    data.put( "shipper_section_code",			cursor.getString( idx++ ) );		//
                    data.put( "shipper_section_name",			cursor.getString( idx++ ) );		//
                    data.put( "shipper_contact",				cursor.getString( idx++ ) );		//
                    data.put( "shipper_phone",					cursor.getString( idx++ ) );		//
                    data.put( "shipper_address",				cursor.getString( idx++ ) );		//
                    data.put( "shipper_post_code",				cursor.getString( idx++ ) );		//
                    data.put( "client_id",						cursor.getInt( idx++ ) );			//
                    data.put( "client_code",					cursor.getString( idx++ ) );		//
                    data.put( "client_name",					cursor.getString( idx++ ) );		//
                    data.put( "client_section_code",			cursor.getString( idx++ ) );		//
                    data.put( "client_section_name",			cursor.getString( idx++ ) );		//
                    data.put( "client_phone",					cursor.getString( idx++ ) );		//
                    data.put( "client_address",					cursor.getString( idx++ ) );		//
                    data.put( "client_post_code",				cursor.getString( idx++ ) );		//
                    data.put( "consignee_code",					cursor.getString( idx++ ) );		//
                    data.put( "consignee_name",					cursor.getString( idx++ ) );		//
                    data.put( "consignee_section_code",			cursor.getString( idx++ ) );		//
                    data.put( "consignee_section_name",			cursor.getString( idx++ ) );		//
                    data.put( "consignee_contact",				cursor.getString( idx++ ) );		//
                    data.put( "consignee_phone",				cursor.getString( idx++ ) );		//
                    data.put( "consignee_address",				cursor.getString( idx++ ) );		//
                    data.put( "consignee_post_code",			cursor.getString( idx++ ) );		//
                    data.put( "carrier_id",						cursor.getInt( idx++ ) );			//
                    data.put( "carrier_code",					cursor.getString( idx++ ) );		//
                    data.put( "carrier_name",					cursor.getString( idx++ ) );		//
                    data.put( "carrier_center_code_from",		cursor.getString( idx++ ) );		//
                    data.put( "carrier_center_name_from",		cursor.getString( idx++ ) );		//
                    data.put( "carrier_center_phone_from",		cursor.getString( idx++ ) );		//
                    data.put( "carrier_center_code_to",			cursor.getString( idx++ ) );		//
                    data.put( "carrier_center_name_to",			cursor.getString( idx++ ) );		//
                    data.put( "billing_id",						cursor.getInt( idx++ ) );			//
                    data.put( "billing_code",					cursor.getString( idx++ ) );		//
                    data.put( "billing_name",					cursor.getString( idx++ ) );		//
                    data.put( "billing_section_code",			cursor.getString( idx++ ) );		//
                    data.put( "billing_section_name",			cursor.getString( idx++ ) );		//
                    data.put( "shipping_code",					cursor.getString( idx++ ) );		//
                    data.put( "shipping_name",					cursor.getString( idx++ ) );		//
                    data.put( "shipping_section_code",			cursor.getString( idx++ ) );		//
                    data.put( "shipping_section_name",			cursor.getString( idx++ ) );		//
                    data.put( "shipping_phone",					cursor.getString( idx++ ) );		//
                    data.put( "shipping_address_code",			cursor.getString( idx++ ) );		//
                    data.put( "shipping_address",				cursor.getString( idx++ ) );		//
                    data.put( "destination_code",				cursor.getString( idx++ ) );		//
                    data.put( "destination_name",				cursor.getString( idx++ ) );		//
                    data.put( "destination_section_code",		cursor.getString( idx++ ) );		//
                    data.put( "destination_section_name",		cursor.getString( idx++ ) );		//
                    data.put( "destination_contact_code",		cursor.getString( idx++ ) );		//
                    data.put( "destination_contact",			cursor.getString( idx++ ) );		//
                    data.put( "destination_phone",				cursor.getString( idx++ ) );		//
                    data.put( "destination_city_code",			cursor.getString( idx++ ) );		//
                    data.put( "destination_address_code",		cursor.getString( idx++ ) );		//
                    data.put( "destination_address",			cursor.getString( idx++ ) );		//
                    data.put( "destination_post_code",			cursor.getString( idx++ ) );		//
                    data.put( "seal_number",					cursor.getInt( idx++ ) );			//
                    data.put( "seal_num_unit",					cursor.getString( idx++ ) );		//
                    data.put( "seal_weight",					cursor.getInt( idx++ ) );			//
                    data.put( "seal_weight_unit",				cursor.getString( idx++ ) );		//
                    data.put( "seal_volume",					cursor.getInt( idx++ ) );			//
                    data.put( "seal_volume_unit",				cursor.getString( idx++ ) );		//
                    data.put( "instruction_no",					cursor.getString( idx++ ) );		//
                    data.put( "route_id",						cursor.getInt( idx++ ) );			//
                    data.put( "transmission_order_no",			cursor.getInt( idx++ ) );			//
                    data.put( "size",							cursor.getInt( idx++ ) );			//
                    data.put( "luggage_type",					cursor.getInt( idx++ ) );			//
                    data.put( "service_type",					cursor.getInt( idx++ ) );			//
                    data.put( "is_weight",						cursor.getInt( idx++ ) );			//
                    data.put( "is_contact",						cursor.getInt( idx++ ) );			//
                    data.put( "is_before_vote",					cursor.getInt( idx++ ) );			//
                    data.put( "previous_check_time",			cursor.getString( idx++ ) );		//
                    data.put( "previous_check_result",			cursor.getInt( idx++ ) );			//
                    data.put( "base_fare",						cursor.getInt( idx++ ) );			//
                    data.put( "incidental_charge",				cursor.getInt( idx++ ) );			//
                    data.put( "insurance_code",					cursor.getInt( idx++ ) );			//
                    data.put( "insurance_clime",				cursor.getInt( idx++ ) );			//
                    data.put( "insurance_fee",					cursor.getInt( idx++ ) );			//
                    data.put( "tax_code",						cursor.getInt( idx++ ) );			//
                    data.put( "item_price",						cursor.getInt( idx++ ) );			//
                    data.put( "item_tax",						cursor.getInt( idx++ ) );			//
                    data.put( "total_fare",						cursor.getInt( idx++ ) );			//
                    data.put( "total_tax_free",					cursor.getInt( idx++ ) );			//
                    data.put( "regist_time",					cursor.getString( idx++ ) );		//
                    data.put( "regist_check_time",				cursor.getString( idx++ ) );		//
                    data.put( "loaded_time",					cursor.getString( idx++ ) );		//
                    data.put( "start_time",						cursor.getString( idx++ ) );		//
                    data.put( "destination_start_time",			cursor.getString( idx++ ) );		//
                    data.put( "pickup_time",					cursor.getString( idx++ ) );		//
                    data.put( "delivery_time",					cursor.getString( idx++ ) );		//
                    data.put( "return_time",					cursor.getString( idx++ ) );		//
                    data.put( "unloaded_time",					cursor.getString( idx++ ) );		//
                    data.put( "receipt_time",					cursor.getString( idx++ ) );		//
                    data.put( "storage_id",						cursor.getInt( idx++ ) );			//
                    data.put( "next_status",					cursor.getInt( idx++ ) );			//
                    data.put( "status",							cursor.getInt( idx++ ) );			//
                    data.put( "cancel_loading_reason",			cursor.getString( idx++ ) );		//
                    data.put( "cancel_loading_reason_text",		cursor.getString( idx++ ) );		//
                    data.put( "cancel_reason",					cursor.getString( idx++ ) );		//
                    data.put( "cancel_reason_text",				cursor.getString( idx++ ) );		//
                    data.put( "cancel_after_reason",			cursor.getString( idx++ ) );		//
                    data.put( "cancel_after_reason_text",		cursor.getString( idx++ ) );		//
                    data.put( "cancel_process",					cursor.getInt( idx++ ) );			//
                    data.put( "driver",							cursor.getString( idx++ ) );		//
                    data.put( "proc_date",						cursor.getString( idx++ ) );		//
                    data.put( "direction",						cursor.getInt( idx++ ) );			//
                    data.put( "dist_pickup_type",				cursor.getInt( idx++ ) );			//
                    data.put( "registrant_id",					cursor.getInt( idx++ ) );			//
                    data.put( "created",						cursor.getString( idx++ ) );		//
                    data.put( "modified",						cursor.getString( idx++ ) );		//
                    list.put( data );
                }
                json.put( "Order", list );
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

        query = "SELECT `_id`, `order_no` "
                + "FROM " + Constants.TBL_DTB_ORDERS
                + " WHERE `status` = 1 "
                + "ORDER BY `seq_no`";

        return query;
    }

    /**
     * INSERTステートメントの取得
     * @param   rec(in)     レコード
     * @return
     */
    public String getInsertStatement(JSONObject rec) {
        String  query   = "";

        //DTB_ORDERINSERT INTOの設定
        query = "INSERT INTO " + Constants.TBL_DTB_ORDERS + " ( "
                + "`order_id`, `distribution_id`, `order_no`, `order_date`"
                + ", `sub_voucher_no`, `voucher_no`, `conveyance_type`, `pickup_type`, `transport_type`"
                + ", `charge_type`, `pickup_date`, `pickup_time_order`, `pickup_time_from`, `pickup_time_to`"
                + ", `pickup_range`, `pickup_memo`, `delivery_date`, `delivery_time_order`, `delivery_time_from`"
                + ", `delivery_time_to`, `delivery_range`, `delivery_memo`, `handling_memo`, `additional_memo`"
                + ", `special_contents`, `delivery_no`, `shipment_no`, `req_number`, `req_num_unit`"
                + ", `req_weight`, `req_weight_unit`, `req_volume`, `req_volume_unit`, `req_unit_load`"
                + ", `report_number`, `report_num_unit`, `report_weight`, `report_weight_unit`, `report_volume`"
                + ", `report_volume_unit`, `line_no`, `line_order_no`, `line_part_number`, `line_tracking_no`"
                + ", `line_license_plate_no`, `line_seq_no`, `line_branch_no`, `line_order_control_no`, `line_owner_product_code`"
                + ", `line_contractor_product_code`, `line_product_name1`, `line_product_name2`, `line_product_name`, `line_unit_load`"
                + ", `line_req_number`, `line_num_unit`, `line_weight`, `line_weight_unit`, `line_volume`"
                + ", `line_volume_unit`, `line_quantity`, `line_quantity_unit`, `dimensions`, `package_code`"
                + ", `package_name`, `baggage_handling_memo`, `line_fares`, `shipper_code`, `shipper_name`"
                + ", `shipper_section_code`, `shipper_section_name`, `shipper_contact`, `shipper_phone`, `shipper_address`"
                + ", `shipper_post_code`, `client_id`, `client_code`, `client_name`, `client_section_code`"
                + ", `client_section_name`, `client_phone`, `client_address`, `client_post_code`, `consignee_code`"
                + ", `consignee_name`, `consignee_section_code`, `consignee_section_name`, `consignee_contact`, `consignee_phone`"
                + ", `consignee_address`, `consignee_post_code`, `carrier_id`, `carrier_code`, `carrier_name`"
                + ", `carrier_center_code_from`, `carrier_center_name_from`, `carrier_center_phone_from`, `carrier_center_code_to`, `carrier_center_name_to`"
                + ", `billing_id`, `billing_code`, `billing_name`, `billing_section_code`, `billing_section_name`"
                + ", `shipping_code`, `shipping_name`, `shipping_section_code`, `shipping_section_name`, `shipping_phone`"
                + ", `shipping_address_code`, `shipping_address`, `destination_code`, `destination_name`, `destination_section_code`"
                + ", `destination_section_name`, `destination_contact_code`, `destination_contact`, `destination_phone`, `destination_city_code`"
                + ", `destination_address_code`, `destination_address`, `destination_post_code`, `seal_number`, `seal_num_unit`"
                + ", `seal_weight`, `seal_weight_unit`, `seal_volume`, `seal_volume_unit`, `instruction_no`"
                + ", `route_id`, `transmission_order_no`, `size`, `luggage_type`, `service_type`"
                + ", `is_weight`, `is_contact`, `is_before_vote`, `previous_check_time`, `previous_check_result`"
                + ", `base_fare`, `incidental_charge`, `insurance_code`, `insurance_clime`, `insurance_fee`"
                + ", `tax_code`, `item_price`, `item_tax`, `total_fare`, `total_tax_free`"
                + ", `regist_time`, `regist_check_time`, `loaded_time`, `start_time`, `destination_start_time`"
                + ", `pickup_time`, `delivery_time`, `return_time`, `unloaded_time`, `receipt_time`"
                + ", `storage_id`, `next_status`, `status`, `cancel_loading_reason`, `cancel_loading_reason_text`"
                + ", `cancel_reason`, `cancel_reason_text`, `cancel_after_reason`, `cancel_after_reason_text`, `cancel_process`"
                + ", `driver`, `proc_date`, `direction`, `dist_pickup_type`, `registrant_id`"
                + ", `created`, `modified`"
                + ") VALUES ( ";

        //INSERTの値リスト末尾の閉じカッコ
        try {
            query   += rec.getInt( "order_id" )
                    + rec.getInt( "distribution_id" )
                    + "'" + rec.getString( "order_no" ) + "'"
                    + rec.getString( "order_date" )
                    + "'" + rec.getString( "sub_voucher_no" ) + "'"
                    + "'" + rec.getString( "voucher_no" ) + "'"
                    + ( rec.isNull( "conveyance_type" ) ? 0 : rec.getInt( "conveyance_type" ) )
                    + ( rec.isNull( "pickup_type" ) ? 0 : rec.getInt( "pickup_type" ) )
                    + ( rec.isNull( "transport_type" ) ? 0 : rec.getInt( "transport_type" ) )
                    + ( rec.isNull( "charge_type" ) ? 0 : rec.getInt( "charge_type" ) )
                    + "'" + rec.getString( "pickup_date" ) + "'"
                    + ( rec.isNull( "pickup_time_order" ) ? 0 : rec.getInt( "pickup_time_order" ) )
                    + "'" + rec.getString( "pickup_time_from" ) + "'"
                    + "'" + rec.getString( "pickup_time_to" ) + "'"
                    + "'" + rec.getString( "pickup_range" ) + "'"
                    + "'" + rec.getString( "pickup_memo" ) + "'"
                    + "'" + rec.getString( "delivery_date" ) + "'"
                    + ( rec.isNull( "delivery_time_order" ) ? 0 : rec.getInt( "delivery_time_order" ) )
                    + "'" + rec.getString( "delivery_time_from" ) + "'"
                    + "'" + rec.getString( "delivery_time_to" ) + "'"
                    + "'" + rec.getString( "delivery_range" ) + "'"
                    + "'" + rec.getString( "delivery_memo" ) + "'"
                    + "'" + rec.getString( "handling_memo" ) + "'"
                    + "'" + rec.getString( "additional_memo" ) + "'"
                    + "'" + rec.getString( "special_contents" ) + "'"
                    + "'" + rec.getString( "delivery_no" ) + "'"
                    + "'" + rec.getString( "shipment_no" ) + "'"
                    + ( rec.isNull( "req_number" ) ? 0 : rec.getInt( "req_number" ) )
                    + "'" + rec.getString( "req_num_unit" ) + "'"
                    + ( rec.isNull( "req_weight" ) ? 0 : rec.getInt( "req_weight" ) )
                    + "'" + rec.getString( "req_weight_unit" ) + "'"
                    + ( rec.isNull( "req_volume" ) ? 0 : rec.getInt( "req_volume" ) )
                    + "'" + rec.getString( "req_volume_unit" ) + "'"
                    + ( rec.isNull( "req_unit_load" ) ? 0 : rec.getInt( "req_unit_load" ) )
                    + ( rec.isNull( "report_number" ) ? 0 : rec.getInt( "report_number" ) )
                    + "'" + rec.getString( "report_num_unit" ) + "'"
                    + ( rec.isNull( "report_weight" ) ? 0 : rec.getInt( "report_weight" ) )
                    + "'" + rec.getString( "report_weight_unit" ) + "'"
                    + ( rec.isNull( "report_volume" ) ? 0 : rec.getInt( "report_volume" ) )
                    + "'" + rec.getString( "report_volume_unit" ) + "'"
                    + ( rec.isNull( "line_no" ) ? 0 : rec.getInt( "line_no" ) )
                    + "'" + rec.getString( "line_order_no" ) + "'"
                    + ( rec.isNull( "line_part_number" ) ? 0 : rec.getInt( "line_part_number" ) )
                    + "'" + rec.getString( "line_tracking_no" ) + "'"
                    + "'" + rec.getString( "line_license_plate_no" ) + "'"
                    + ( rec.isNull( "line_seq_no" ) ? 0 : rec.getInt( "line_seq_no" ) )
                    + ( rec.isNull( "line_branch_no" ) ? 0 : rec.getInt( "line_branch_no" ) )
                    + ( rec.isNull( "line_order_control_no" ) ? 0 : rec.getInt( "line_order_control_no" ) )
                    + "'" + rec.getString( "line_owner_product_code" ) + "'"
                    + "'" + rec.getString( "line_contractor_product_code" ) + "'"
                    + "'" + rec.getString( "line_product_name1" ) + "'"
                    + "'" + rec.getString( "line_product_name2" ) + "'"
                    + "'" + rec.getString( "line_product_name" ) + "'"
                    + ( rec.isNull( "line_unit_load" ) ? 0 : rec.getInt( "line_unit_load" ) )
                    + ( rec.isNull( "line_req_number" ) ? 0 : rec.getInt( "line_req_number" ) )
                    + "'" + rec.getString( "line_num_unit" ) + "'"
                    + ( rec.isNull( "line_weight" ) ? 0 : rec.getInt( "line_weight" ) )
                    + "'" + rec.getString( "line_weight_unit" ) + "'"
                    + ( rec.isNull( "line_volume" ) ? 0 : rec.getInt( "line_volume" ) )
                    + "'" + rec.getString( "line_volume_unit" ) + "'"
                    + ( rec.isNull( "line_quantity" ) ? 0 : rec.getInt( "line_quantity" ) )
                    + "'" + rec.getString( "line_quantity_unit" ) + "'"
                    + "'" + rec.getString( "dimensions" ) + "'"
                    + "'" + rec.getString( "package_code" ) + "'"
                    + "'" + rec.getString( "package_name" ) + "'"
                    + "'" + rec.getString( "baggage_handling_memo" ) + "'"
                    + ( rec.isNull( "line_fares" ) ? 0 : rec.getInt( "line_fares" ) )
                    + "'" + rec.getString( "shipper_code" ) + "'"
                    + "'" + rec.getString( "shipper_name" ) + "'"
                    + "'" + rec.getString( "shipper_section_code" ) + "'"
                    + "'" + rec.getString( "shipper_section_name" ) + "'"
                    + "'" + rec.getString( "shipper_contact" ) + "'"
                    + "'" + rec.getString( "shipper_phone" ) + "'"
                    + "'" + rec.getString( "shipper_address" ) + "'"
                    + "'" + rec.getString( "shipper_post_code" ) + "'"
                    + ( rec.isNull( "client_id" ) ? 0 : rec.getInt( "client_id" ) )
                    + "'" + rec.getString( "client_code" ) + "'"
                    + "'" + rec.getString( "client_name" ) + "'"
                    + "'" + rec.getString( "client_section_code" ) + "'"
                    + "'" + rec.getString( "client_section_name" ) + "'"
                    + "'" + rec.getString( "client_phone" ) + "'"
                    + "'" + rec.getString( "client_address" ) + "'"
                    + "'" + rec.getString( "client_post_code" ) + "'"
                    + "'" + rec.getString( "consignee_code" ) + "'"
                    + "'" + rec.getString( "consignee_name" ) + "'"
                    + "'" + rec.getString( "consignee_section_code" ) + "'"
                    + "'" + rec.getString( "consignee_section_name" ) + "'"
                    + "'" + rec.getString( "consignee_contact" ) + "'"
                    + "'" + rec.getString( "consignee_phone" ) + "'"
                    + "'" + rec.getString( "consignee_address" ) + "'"
                    + "'" + rec.getString( "consignee_post_code" ) + "'"
                    + ( rec.isNull( "carrier_id" ) ? 0 : rec.getInt( "carrier_id" ) )
                    + "'" + rec.getString( "carrier_code" ) + "'"
                    + "'" + rec.getString( "carrier_name" ) + "'"
                    + "'" + rec.getString( "carrier_center_code_from" ) + "'"
                    + "'" + rec.getString( "carrier_center_name_from" ) + "'"
                    + "'" + rec.getString( "carrier_center_phone_from" ) + "'"
                    + "'" + rec.getString( "carrier_center_code_to" ) + "'"
                    + "'" + rec.getString( "carrier_center_name_to" ) + "'"
                    + ( rec.isNull( "billing_id" ) ? 0 : rec.getInt( "billing_id" ) )
                    + "'" + rec.getString( "billing_code" ) + "'"
                    + "'" + rec.getString( "billing_name" ) + "'"
                    + "'" + rec.getString( "billing_section_code" ) + "'"
                    + "'" + rec.getString( "billing_section_name" ) + "'"
                    + "'" + rec.getString( "shipping_code" ) + "'"
                    + "'" + rec.getString( "shipping_name" ) + "'"
                    + "'" + rec.getString( "shipping_section_code" ) + "'"
                    + "'" + rec.getString( "shipping_section_name" ) + "'"
                    + "'" + rec.getString( "shipping_phone" ) + "'"
                    + "'" + rec.getString( "shipping_address_code" ) + "'"
                    + "'" + rec.getString( "shipping_address" ) + "'"
                    + "'" + rec.getString( "destination_code" ) + "'"
                    + "'" + rec.getString( "destination_name" ) + "'"
                    + "'" + rec.getString( "destination_section_code" ) + "'"
                    + "'" + rec.getString( "destination_section_name" ) + "'"
                    + "'" + rec.getString( "destination_contact_code" ) + "'"
                    + "'" + rec.getString( "destination_contact" ) + "'"
                    + "'" + rec.getString( "destination_phone" ) + "'"
                    + "'" + rec.getString( "destination_city_code" ) + "'"
                    + "'" + rec.getString( "destination_address_code" ) + "'"
                    + "'" + rec.getString( "destination_address" ) + "'"
                    + "'" + rec.getString( "destination_post_code" ) + "'"
                    + ( rec.isNull( "seal_number" ) ? 0 : rec.getInt( "seal_number" ) )
                    + "'" + rec.getString( "seal_num_unit" ) + "'"
                    + ( rec.isNull( "seal_weight" ) ? 0 : rec.getInt( "seal_weight" ) )
                    + "'" + rec.getString( "seal_weight_unit" ) + "'"
                    + ( rec.isNull( "seal_volume" ) ? 0 : rec.getInt( "seal_volume" ) )
                    + "'" + rec.getString( "seal_volume_unit" ) + "'"
                    + "'" + rec.getString( "instruction_no" ) + "'"
                    + ( rec.isNull( "route_id" ) ? 0 : rec.getInt( "route_id" ) )
                    + ( rec.isNull( "transmission_order_no" ) ? 0 : rec.getInt( "transmission_order_no" ) )
                    + "'" + rec.getString( "size" ) + "'"
                    + ( rec.isNull( "luggage_type" ) ? 0 : rec.getInt( "luggage_type" ) )
                    + ( rec.isNull( "service_type" ) ? 0 : rec.getInt( "service_type" ) )
                    + ( rec.isNull( "is_weight" ) ? 0 : rec.getInt( "is_weight" ) )
                    + ( rec.isNull( "is_contact" ) ? 0 : rec.getInt( "is_contact" ) )
                    + ( rec.isNull( "is_before_vote" ) ? 0 : rec.getBoolean( "is_before_vote" ) )
                    + "'" + rec.getString( "previous_check_time" ) + "'"
                    + "'" + rec.getString( "previous_check_result" ) + "'"
                    + ( rec.isNull( "base_fare" ) ? 0 : rec.getInt( "base_fare" ) )
                    + ( rec.isNull( "incidental_charge" ) ? 0 : rec.getInt( "incidental_charge" ) )
                    + ( rec.isNull( "insurance_code" ) ? 0 : rec.getInt( "insurance_code" ) )
                    + ( rec.isNull( "insurance_clime" ) ? 0 : rec.getInt( "insurance_clime" ) )
                    + ( rec.isNull( "insurance_fee" ) ? 0 : rec.getInt( "insurance_fee" ) )
                    + ( rec.isNull( "tax_code" ) ? 0 : rec.getInt( "tax_code" ) )
                    + ( rec.isNull( "item_price" ) ? 0 : rec.getInt( "item_price" ) )
                    + ( rec.isNull( "item_tax" ) ? 0 : rec.getInt( "item_tax" ) )
                    + ( rec.isNull( "total_fare" ) ? 0 : rec.getInt( "total_fare" ) )
                    + ( rec.isNull( "total_tax_free" ) ? 0 : rec.getInt( "total_tax_free" ) )
                    + "'" + rec.getString( "regist_time" ) + "'"
//                    + "'" + rec.getString( "regist_check_time" ) + "'"
                    + "'" + rec.getString( "loaded_time" ) + "'"
                    + ( rec.isNull( "is_send_loaded" ) ? 0 : rec.getInt( "is_send_loaded" ) )
                    + "'" + rec.getString( "start_time" ) + "'"
//                    + "'" + rec.getString( "destination_start_time" ) + "'"
                    + "'" + rec.getString( "pickup_time" ) + "'"
                    + "'" + rec.getString( "delivery_time" ) + "'"
                    + ( rec.isNull( "is_send_comp" ) ? 0 : rec.getInt( "is_send_comp" ) )
                    + "'" + rec.getString( "return_time" ) + "'"
                    + "'" + rec.getString( "unloaded_time" ) + "'"
                    + ( rec.isNull( "is_unloaded_time" ) ? 0 : rec.getInt( "is_unloaded_time" ) )
                    + "'" + rec.getString( "receipt_time" ) + "'"
//                    + ( rec.isNull( "storage_id" ) ? 0 : rec.getInt( "storage_id" ) )
                    + ( rec.isNull( "next_status" ) ? 0 : rec.getInt( "next_status" ) )
                    + ( rec.isNull( "status" ) ? 0 : rec.getInt( "status" ) )
                    + ( rec.isNull( "cancel_loading_reason" ) ? 0 : rec.getInt( "cancel_loading_reason" ) )
                    + "'" + rec.getString( "cancel_loading_reason_text" ) + "'"
                    + ( rec.isNull( "cancel_reason" ) ? 0 : rec.getInt( "cancel_reason" ) )
                    + "'" + rec.getString( "cancel_reason_text" ) + "'"
                    + ( rec.isNull( "cancel_after_reason" ) ? 0 : rec.getInt( "cancel_after_reason" ) )
                    + "'" + rec.getString( "cancel_after_reason_text" ) + "'"
                    + ( rec.isNull( "cancel_process" ) ? 0 : rec.getInt( "cancel_process" ) )
                    + ( rec.isNull( "registrant_type" ) ? 0 : rec.getInt( "registrant_type" ) )
                    + ( rec.isNull( "registrant_id" ) ? 0 : rec.getInt( "registrant_id" ) )
                    + "'" + rec.getString( "created" ) + "'"
                    + "'" + rec.getString( "modified" ) + "'"
                    + ( rec.isNull( "driver" ) ? 0 : rec.getInt( "driver" ) );
            query += " )";
        } catch (JSONException e) {
            //エラーなので、SQLを空にする
            e.printStackTrace();
            query = "";
        }

        return query;
    }

    /*
     * DELETEステートメントの取得
     * @return
     */
    public String getDeleteStatment(){
        String  query   = "";
        query = "DELETE FROM " + Constants.TBL_DTB_ORDERS;
        return query;
    }


}
