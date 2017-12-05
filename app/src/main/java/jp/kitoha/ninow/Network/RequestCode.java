package jp.kitoha.ninow.Network;

/******************************************************************************
 * @brief		通信リクエストコード
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class RequestCode {
	/**************************************************************************
	 * コールバック判定用
	 **************************************************************************/
	//センター業務(始業～積込み)
	public static final int		REQ_CODE_POST_WORK_START				= 101;				//業務開始
	public static final int		REQ_CODE_POST_CYCLE_START				= 102;				//センター着
	public static final int		REQ_CODE_GET_COURSE_INFO				= 201;			   	//配送指示受信
	public static final int		REQ_CODE_GET_VOUCHER_INFO				= 211;			   	//伝票情報取得
	public static final int		REQ_CODE_POST_COURSE_REGISTER_VOUCHER	= 212;	   			//コースに伝票を登録する
	public static final int		REQ_CODE_CLEAR_COURSE_INFO				= 290;				//配送指示取り消し
	public static final int		REQ_CODE_UPDATE_COURSE_INFO				= 291;				//配送指示更新
	public static final int		REQ_CODE_POST_LOADING_REPORT			= 301;				//積込報告
	public static final int		REQ_CODE_POST_STARTING_REPORT			= 311;				//出発報告
	//配送先業務
	public static final int		REQ_CODE_POST_DELIVERY_START			= 401;				//配送作業開始報告
	public static final int		REQ_CODE_POST_DELIVERY_END				= 411;				//配送完了報告
	public static final int		REQ_CODE_POST_DELIVERY_REJECT			= 412;				//返品報告
	public static final int		REQ_CODE_POST_DELIVERY_ABSENCE			= 413;				//不在報告
	public static final int		REQ_CODE_POST_IMAGES					= 421;				//画像アップロード
	public static final int		REQ_CODE_POST_MEDIAS					= 422;				//動画アップロード
	public static final int		REQ_CODE_POST_RETURN_START			    = 501;				//帰社準備報告
	//センター業務(荷卸し～終業)
	public static final int		REQ_CODE_POST_RETURN_END    			= 511;				//帰社報告
	public static final int		REQ_CODE_POST_UNLOADING_REPORT			= 601;				//荷卸報告
	public static final int		REQ_CODE_POST_CYCLE_END					= 701;				//バッチ終了
	public static final int		REQ_CODE_POST_WORK_END	    			= 801;				//業務終了
	//設定
	public static final int		REQ_CODE_GET_DRIVER_LIST				= 901;			   	//ドライバー一覧
	public static final int		REQ_CODE_GET_CAR_LIST					= 902;			   	//車両一覧
}
