package jp.kitoha.ninow.Common;

/******************************************************************************
 * @brief		画面用定数定義用クラス
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class ScreenCode {
	/**************************************************************************
	 * 画面サイズ
	 **************************************************************************/
	//region 画面サイズ
	/** デフォルト画面サイズ(16:9) **/
	public static final int		DEFAULT_VP_WIDTH				= 720;						//デフォルトサイズ(横幅)
	public static final int		DEFAULT_VP_HEIGHT				= 1280;						//デフォルトサイズ(縦幅)
	//endregion

	/**************************************************************************
	 * 定数(Intent用リクエストコード)
	 **************************************************************************/
	//region リクエストコード
	/* リクエストコード */
	public static final int		REQUEST_CODE_TAKE_PHOTO			= 10;						//写真撮影
	public static final int		REQUEST_CODE_SELECT_PHOTO		= 11;						//画像選択
	public static final int		REQUEST_CODE_SIGN				= 12;						//サイン
	public static final int		REQUEST_CODE_VIDEO				= 20;						//動画撮影
	public static final int		REQUEST_CODE_RECORD_SOUND		= 21;						//録音
	public static final int		REQUEST_CODE_PRINT				= 30;						//印刷
	public static final int		REQUEST_CODE_DEVELOP_SETTING	= 80;						//開発者設定
	public static final int		REQUEST_CODE_OPTION_SETTING		= 90;						//オプション設定
	//endregion

	/**************************************************************************
	 * 定数(Assetファイル用)
	 **************************************************************************/
	//region ファイルパス
	/** ASSETファイルパス **/
	public static final String	ASSET_PATH					= "file:///android_asset/";		//アセット保管パス
	public static final String	TESTDATA_PATH				= ASSET_PATH + "file/";			//テストデータ保管パス

	public static final String	HTML_PATH					= ASSET_PATH + "html/";			//HTML保管パス

	/** 画面ファイル(画面番号と一対にすること) **/
	public static final String	PAGE_F01A					= "ni-now-F01a.html";			//業務開始画面
	public static final String	PAGE_F01B					= "ni-now-F01b.html";			//業務開始(結果)画面
	public static final String	PAGE_F02A					= "ni-now-F02a.html";			//センター着画面
	public static final String	PAGE_F02B					= "ni-now-F02b.html";			//センター着(結果)画面
	public static final String	PAGE_F03A					= "ni-now-F03a.html";			//配送指示受信画面
	public static final String	PAGE_F03B					= "ni-now-F03b.html";			//配送指示受信(結果)画面
	public static final String	PAGE_F03C					= "ni-now-F03c.html";			//伝票読み取り
	public static final String	PAGE_F03D					= "ni-now-F03d.html";			//伝票詳細画面
	public static final String	PAGE_F04A					= "ni-now-F04a.html";			//積付開始画面
	public static final String	PAGE_F04B					= "ni-now-F04b.html";			//積付一覧画面
	public static final String	PAGE_F04C					= "ni-now-F04c.html";			//荷物詳細画面(積付キャンセル用)
	public static final String	PAGE_F05A					= "ni-now-F05a.html";			//出発報告画面
	public static final String	PAGE_F05B					= "ni-now-F05b.html";			//出発報告(結果)画面
	public static final String	PAGE_F06A					= "ni-now-F06a.html";			//配送確認画面
	public static final String	PAGE_F06B					= "ni-now-F06b.html";			//配送一覧画面
	public static final String	PAGE_F06C					= "ni-now-F06c.html";			//配送先詳細画面
	public static final String	PAGE_F06CC					= "ni-now-F06cC.html";			//配送先詳細画面(写真)
	public static final String	PAGE_F06CS					= "ni-now-F06cS.html";			//配送先詳細画面(電子サイン)
	public static final String	PAGE_F06D					= "ni-now-F06d.html";			//荷物詳細画面
	public static final String	PAGE_F07A					= "ni-now-F07a.html";			//帰社準備画面
	public static final String	PAGE_F07B					= "ni-now-F07b.html";			//帰着報告画面
	public static final String	PAGE_F08A					= "ni-now-F08a.html";			//荷卸し一覧画面
	public static final String	PAGE_F08B					= "ni-now-F08b.html";			//荷卸し詳細画面
	public static final String	PAGE_F09A					= "ni-now-F09a.html";			//回旋終了画面
	public static final String	PAGE_F09B					= "ni-now-F09b.html";			//回旋終了(結果)画面
	public static final String	PAGE_F10A					= "ni-now-F10a.html";			//業務終了画面
	public static final String	PAGE_F10B					= "ni-now-F10b.html";			//業務終了(結果)画面
	public static final String	PAGE_M01A					= "ni-now-M01a.html";			//マイページ
	public static final String	PAGE_M01B					= "ni-now-M01b.html";			//設定1(テキスト)
	public static final String	PAGE_M01C					= "ni-now-M01c.html";			//設定2(コンボボックス)
	public static final String	PAGE_M01D					= "ni-now-M01d.html";			//設定3(機能設定)
	public static final String	PAGE_M02A					= "ni-now-M02a.html";			//メニュー
	public static final String	PAGE_L01A					= "ni-now-L01a.html";			//未送信一覧
	//endregion

	//region 画面番号
	/** 画面番号 **/
	public static final int		PAGENO_F01A					= 1001;							//業務開始画面
	public static final int		PAGENO_F01B					= 1002;							//業務開始(結果)画面
	public static final int		PAGENO_F02A					= 2001;							//センター着画面
	public static final int		PAGENO_F02B					= 2002;							//センター着(結果)画面
	public static final int		PAGENO_F03A					= 2101;							//配送指示受信画面
	public static final int		PAGENO_F03B					= 2102;							//配送指示受信(結果)画面
	public static final int		PAGENO_F03C					= 2111;							//伝票読取画面
	public static final int		PAGENO_F03D					= 2112;							//伝票詳細画面
	public static final int		PAGENO_F04A					= 2201;							//積付開始画面
	public static final int		PAGENO_F04B					= 2202;							//積付一覧画面
	public static final int		PAGENO_F04C					= 2211;							//荷物詳細画面(積付キャンセル用)
	public static final int		PAGENO_F05A					= 3001;							//出発報告画面
	public static final int		PAGENO_F05B					= 3002;							//出発報告(結果)画面
	public static final int		PAGENO_F06A					= 4001;							//配送開始画面
	public static final int		PAGENO_F06B					= 4002;							//配送一覧画面
	public static final int		PAGENO_F06C					= 4101;							//配送先詳細画面
	public static final int		PAGENO_F06CC				= 4102;							//配送先詳細画面(写真)
	public static final int		PAGENO_F06CS				= 4103;							//配送先詳細画面(サイン)
	public static final int		PAGENO_F06D					= 4111;							//荷物画面
	public static final int		PAGENO_F07A					= 5001;							//帰社準備画面
	public static final int		PAGENO_F07B					= 5002;							//帰着報告画面
	public static final int		PAGENO_F08A					= 6001;							//荷卸し一覧画面
	public static final int		PAGENO_F08B					= 6002;							//荷卸し詳細画面
	public static final int		PAGENO_F09A					= 7001;							//回旋終了画面
	public static final int		PAGENO_F09B					= 7002;							//回旋終了(結果)画面
	public static final int		PAGENO_F10A					= 8001;							//業務終了画面
	public static final int		PAGENO_F10B					= 8002;							//業務終了(結果)画面
	public static final int		PAGENO_M01A					= 9001;							//マイページ
	public static final int		PAGENO_M01B					= 9011;							//設定1(テキスト)
	public static final int		PAGENO_M01C					= 9012;							//設定2(コンボボックス)
	public static final int		PAGENO_M01D					= 9013;							//設定2(コンボボックス)
	public static final int		PAGENO_M02A					= 9002;							//設定2(コンボボックス)
	public static final int		PAGENO_L01A					= 9101;							//未送信一覧
	//endregion
}
