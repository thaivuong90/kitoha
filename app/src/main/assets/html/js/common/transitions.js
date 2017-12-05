/*******************************************************************************
 * 定数・変数
 *******************************************************************************/

/*******************************************************************************
 * メソッド
 *******************************************************************************/
var Transitions = function() {
	//スクリーンの設定
	var screens = new Array(30);
	screens[1001]	= "ni-now-F01a.html";							//業務開始画面
	screens[1002]	= "ni-now-F01b.html";							//業務開始(結果)画面
	screens[2001]	= "ni-now-F02a.html";							//センター着画面
	screens[2002]	= "ni-now-F02b.html";							//センター着(結果)画面
	screens[2101]	= "ni-now-F03a.html";							//配送指示受信画面
	screens[2102]	= "ni-now-F03b.html";							//配送指示受信(結果)画面
	screens[2111]	= "ni-now-F03c.html";							//伝票読み取り画面
	screens[2112]	= "ni-now-F03d.html";							//伝票詳細画面
	screens[2201]	= "ni-now-F04a.html";							//積付開始画面
	screens[2202]	= "ni-now-F04b.html";							//積付一覧画面
	screens[2211]	= "ni-now-F04c.html";							//荷物詳細画面(積付キャンセル用)
	screens[3001]	= "ni-now-F05a.html";							//出発報告画面
	screens[3002]	= "ni-now-F05b.html";							//出発報告(結果)画面
	screens[4001]	= "ni-now-F06a.html";							//配送開始画面
	screens[4002]	= "ni-now-F06b.html";							//配送一覧画面
	screens[4101]	= "ni-now-F06c.html";							//配送詳細画面
	screens[4102]	= "ni-now-F06cC.html";							//配送詳細画面(写真)
	screens[4103]	= "ni-now-F06cS.html";							//配送詳細画面(電子サイン)
	screens[4111]	= "ni-now-F06d.html";							//荷物詳細画面(返品)
	screens[4201]	= "ni-now-F06e.html";							//判取証明画面
	screens[5001]	= "ni-now-F07a.html";							//帰社準備
	screens[5002]	= "ni-now-F07b.html";							//帰着報告
	screens[6001]	= "ni-now-F08a.html";							//荷卸し一覧画面
	screens[6002]	= "ni-now-F08b.html";							//荷卸し詳細画面
	screens[7001]	= "ni-now-F09a.html";							//回旋終了画面
	screens[7002]	= "ni-now-F09b.html";							//回旋終了(結果)画面
	screens[8001]	= "ni-now-F10a.html";							//業務終了画面
	screens[8002]	= "ni-now-F10b.html";							//業務終了(結果)画面
	screens[9001]	= "ni-now-M01a.html";							//マイページ(設定)
	screens[9011]	= "ni-now-M01b.html";							//設定1(テキスト)
	screens[9012]	= "ni-now-M01c.html";							//設定2(コンボボックス)
	screens[9013]	= "ni-now-M01d.html";							//設定2(機能設定)
	screens[9101]	= "ni-now-L01a.html";							//未送信一覧
	screens[9002]	= "ni-now-M02a.html";							//メニュー
	
	//戻るボタン押下時の画面遷移(戻るがないものは未記載)
	var prev_screens = new Array(30);
	prev_screens[1001]	= 1001;						//業務開始画面
	prev_screens[1002]	= 1001;						//業務開始(結果)画面
	prev_screens[2001]	= 1002;						//センター着画面
	prev_screens[2002]	= 2001;						//センター着(結果)画面
	prev_screens[2101]	= 2002;						//配送指示受信画面
	prev_screens[2102]	= 2101;						//配送指示受信(結果)画面
	prev_screens[2111]	= 1002;						//配送指示受信画面
	prev_screens[2112]	= 2111;						//配送指示受信(結果)画面
	prev_screens[2201]	= 2102;						//積付開始画面
	prev_screens[2202]	= 2201;						//積付一覧画面
	prev_screens[2211]	= 2202;						//荷物詳細画面(積付キャンセル用)
	prev_screens[3001]	= 2202;						//出発報告画面
	prev_screens[3002]	= 3001;						//出発報告(結果)画面
	prev_screens[4001]	= 3002;						//配送開始画面
	prev_screens[4002]	= 4001;						//配送一覧画面
	prev_screens[4101]	= 4002;						//配送詳細画面
	prev_screens[4102]	= 4101;						//配送詳細画面(写真)
	prev_screens[4103]	= 4101;						//配送詳細画面(電子サイン)
	prev_screens[4111]	= 4101;						//荷物詳細画面(返品)
	prev_screens[5001]	= 4002;						//帰社準備
	prev_screens[5002]	= 5001;						//帰着報告
	prev_screens[6001]	= 5002;						//荷卸し一覧画面
	prev_screens[6002]	= 6001;						//荷卸し詳細画面
	prev_screens[7001]	= 6001;						//回旋終了画面
	prev_screens[7002]	= 7001;						//回旋終了(結果)画面
	prev_screens[8001]	= 7002;						//業務終了(結果)画面
	prev_screens[8002]	= 8001;						//業務終了(結果)画面
	prev_screens[9001]	= 9002;						//マイページ(設定)
	prev_screens[9011]	= 9001;						//設定1(テキスト)
	prev_screens[9012]	= 9001;						//設定2(コンボボックス)
	prev_screens[9013]	= 9001;						//設定2(機能設定)
	prev_screens[9101]	= 9001;						//未送信一覧

	//次へボタン押下時の画面遷移(次へがないものは未記載)
	var next_screens = new Array(30);
	next_screens[1001]	= 1002;						//業務開始画面
	next_screens[1002]	= 2001;						//業務開始(結果)画面
	next_screens[2001]	= 2002;						//センター着画面
	next_screens[2002]	= 2101;						//センター着(結果)画面
	next_screens[2101]	= 2102;						//配送指示受信画面
	next_screens[2102]	= 2201;						//配送指示受信(結果)画面
	next_screens[2111]	= 2201;						//伝票読取画面
	next_screens[2112]	= 2201;						//伝票詳細画面
	next_screens[2201]	= 2202;						//積付開始画面
	next_screens[2202]	= 3001;						//積付一覧画面
	next_screens[3001]	= 3002;						//出発報告画面
	next_screens[3002]	= 4001;						//出発報告(結果)画面
	next_screens[4001]	= 4002;						//配送開始画面
	next_screens[4002]	= 5001;						//配送一覧画面
	next_screens[5001]	= 5002;						//帰社準備
	next_screens[5002]	= 6001;						//帰着報告
	next_screens[6001]	= 7001;						//荷卸し一覧画面
	next_screens[7001]	= 7002;						//回旋終了画面
	next_screens[7002]	= 8001;						//回旋終了(結果)画面
	next_screens[8001]	= 8002;						//業務終了画面
	next_screens[8002]	= 1001;						//業務終了(結果)画面
	
	return {
		/**
		 * 画面URLの取得
		 */
		getScreen: function( id ){
			//この値は、Common/ScreenCode.javaを参照
			return screens[id];
		},
		/**
		 * 1つ前の画面IDの取得
		 */
		getPrevScreenId: function( id ){
			//この値は、Common/ScreenCode.javaを参照
			return prev_screens[id];
		},
		/**
		 * 1つ後の画面IDの取得
		 */
		getNextScreenId: function( id ){
			//この値は、Common/ScreenCode.javaを参照
			return next_screens[id];
		}
	}
}();
