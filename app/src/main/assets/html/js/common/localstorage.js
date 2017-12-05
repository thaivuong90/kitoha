var LocalStorage = function() {
	return {
		/**
		 * ローカルストレージの使用可否チェック
		 */
		check: function(){
			if( !window.localStorage ){
				alert( "ローカルストレージは利用可能できません。\nデモをご覧いただくことはできません。" );
			}
		},
		/**
		 * ローカルストレージへの保存
		 */
		setItem: function( field, data ){
			window.localStorage.setItem( field, data );
		},
		/**
		 * ローカルストレージからの取得
		 */
		getItem: function( field ){
			return window.localStorage.getItem( field );
		},
		/**
		 * ローカルストレージから項目削除
		 */
		removeItem: function( field ){
			return window.localStorage.removeItem( field );
		},
		/**
		 * ローカルストレージの全削除
		 */
		clear: function(){
			return window.localStorage.clear();
		}
	}
}();
