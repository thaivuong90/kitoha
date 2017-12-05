package jp.kitoha.ninow.Common;

/******************************************************************************
 * @brief		エラーコード定義用クラス
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class ErrorCode{
	/**************************************************************************
	 * 定数(エラーコード)
	 **************************************************************************/
	public static final int		STS_DEMO					= -1;							//デモモード
	public static final int		STS_OK						= 0;							//成功
	public static final int		STS_NG						= 100;							//失敗
	//region 環境エラー
	public static final int		STS_NG_NO_RECOVERY_FILE		= 201;							//リカバリーファイルがありません
	//endregion
	//region 初期設定エラー
	public static final int		STS_NG_NO_SERVER_SETTING	= 301;							//サーバーの設定がされていません
	//endregion
	//region 入力エラー
	public static final int		STS_NG_PARAMETER			= 401;							//パラメータエラー
	public static final int		STS_NG_NOT_FOUND_DATA		= 402;							//該当データなし
	//endregion

	/**************************************************************************
	 * 定数(エラーメッセージ)
	 **************************************************************************/
	//region タイトル
	public static final String	DLG_TITLE_INFO				= "通知";						//デモモード
	public static final String	DLG_TITLE_WARN				= "警告";						//成功
	public static final String	DLG_TITLE_FATAL				= "エラー";						//失敗
	//endregion

	//region メッセージ
	public static final String	DLG_MSG_WORK_START			= "業務開始報告に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_CYCLE_START			= "センター着報告に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_COURSE_INFO			= "配送情報の取得に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_CLEAR_COURSE_INFO	= "配送情報の取り消しに失敗しました。\n再度、お試しのください。";
	public static final String	DLG_MSG_LOADING				= "積込報告に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_START_REPORT		= "出発報告に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_DELIVERY_START		= "作業開始報告に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_DELIVERY_REPORT		= "配送報告に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_DELIVERY_REJECT		= "配送報告(返品)に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_DELIVERY_ABSENCE	= "配送報告(不在)に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_UPLOAD_IMAGE		= "画像送信に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_UPLOAD_MEDIA		= "動画送信に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_RETURN_READY		= "帰社準備報告に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_RETURN_REPORT		= "帰社報告に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_UNLOADING			= "荷卸し報告に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_CYCLE_END			= "バッチ終了報告に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_WORK_END			= "業務終了報告に失敗しました。\n通信環境をお確かめの上、再度お試しください。";

	public static final String	DLG_MSG_UPDATE_COURSE_INFO	= "配送情報の更新に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_DRIVER_LIST			= "ドライバー情報の取得に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	public static final String	DLG_MSG_CAR_LIST			= "車両情報の取得に失敗しました。\n通信環境をお確かめの上、再度お試しください。";
	//endregion

	/**************************************************************************
     * メソッド
     **************************************************************************/
	/***
	 * @brief	コンストラクタ
	 */
	private ErrorCode(){};
}
