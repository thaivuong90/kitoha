( function($){
	$( document ).ready( function (){
		//ヘッダーの高さ調整
		hsize = $( 'header' ).outerHeight();
		if( $( ".contents" ).length ){
			$( ".contents" ).css( "padding-top", hsize + "px" );
		}
		if( $( ".menu_contents" ).length ){
			$( ".menu_contents" ).css( "padding-top", hsize + "px" );
		}
		//フッターの高さ調整
		hsize = $( 'footer' ).outerHeight();
		$( ".contents" ).css( "padding-bottom", hsize + "px" );
	} );
} )
(jQuery);
//Modal
$(function() {
    $(".modal-open").on("click", function() {
//------------------------------------------------------------------
// キーボード操作などにより、オーバーレイが多重起動するのを防止する
//------------------------------------------------------------------
    // ボタンからフォーカスを外す
        $(this).blur();
    // 現在のモーダルウィンドウを削除して新しく起動する
        if($("#modal-overlay")[0]) $("#modal-overlay").remove();
    // オーバーレイ用のHTMLコードを、[body]内の最後に生成する
        $("body").append('<div id="modal-overlay"></div>');
    // #modal-overlay 及び #modalcontent をフェードインさせる
        $("#modal-overlay, #modal-content").fadeIn("slow");
        centeringModalSyncer();

//------------------------------------------------------------------
// #modal-overlay または #modal-close のクリック時に実行する処理
//------------------------------------------------------------------
        $("#modal-overlay, #modal-close").off().on("click", function() {
        // #modal-overlay 及び #modal-close をフェードアウトする
            $("#modal-content, #modal-overlay").fadeOut("slow", function() {
            // フェードアウト後、 #modal-overlay をHTML(DOM)上から削除
                $("#modal-overlay").remove();
            });
        });

//------------------------------------------------------------------
// リサイズ操作をした際に、モーダルウィンドウを中央寄せにする
//------------------------------------------------------------------
    // Case.1 リサイズ操作の度に実行する場合
        // $(window).resize(centeringModalSyncer);
    // Case.2 リサイズ操作が終了したときのみ実行する場合
        var timer = false;
        $(window).resize(function() {
            if (timer !== false) clearTimeout(timer);
            timer = setTimeout(function() {
                centeringModalSyncer();
            }, 200);
        });

    //------------------------------------------------------------------
    // モーダルウィンドウを中央寄せする関数
    //------------------------------------------------------------------
        function centeringModalSyncer() {
        // 画面(ウィンドウ)の幅、高さを取得
            var w = $(window).width();
            var h = window.innerHeight;

        // コンテンツ(#modal-content)の幅、高さを取得
        // Case.1 margin 含める場合
            var cw = $("#modal-content").outerWidth(true);
            var ch = $("#modal-content").outerHeight(true);
        // Case.2 margin 含めない場合
           //  var cw = $("#modal-content").outerWidth();
            // var ch = $("#modal-content").outerHeight();

        // #modal-content を真ん中に配置するのに、左端と上部から何ピクセル離せばいいか？を計算してCSSのポジションを設定する
        // Case.1 left と top で変数を分ける
        /*
            var pxleft = ((w - cw) / 2);
            var pxtop  = ((h - ch) / 2);
            $("#modal-content").css({
                "left":pxleft + "px",
                "top":pxtop + "px"
            });
        */
        // Case.2 プロパティを持たせて一つの変数に纏める
            var p_prop = {
                left:((w - cw) / 2),
                top:((h - ch) / 2)
            };
            $("#modal-content").css(p_prop);
        }
    });
});