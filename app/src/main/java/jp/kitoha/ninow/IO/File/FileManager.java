package jp.kitoha.ninow.IO.File;

import android.util.Log;

import java.io.*;
import android.webkit.JavascriptInterface;

/******************************************************************************
 * @brief		ファイル入出力管理クラス
 * @note		テキストファイル用(バイナリでも使えないことはない)
 * 				複数のファイルを扱える様にするため、引数で入出力ファイルを管理する
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class FileManager{
	/**************************************************************************
	 * インスタンス変数
	 **************************************************************************/
	public static FileManager instance = new FileManager();

	/**************************************************************************
	 * メソッド
	 **************************************************************************/
	/**
	 * @brief インスタンスの取得
	 * @return インスタンス
	 */
	public static FileManager getInstance(){
		return instance;
	}


	/**
	 * @brief	ファイルを開く
	 * @param	path(in)		ファイルパス
	 * @param	check(in)		ファイルの有無チェック(true=チェックする, false=チェックしない)
	 * @return	Fileオブジェクト
	 */
	public File Open(String path, boolean check){
		//ファイルを開く
		File file = new File( path );
		//ファイルの有無をチェック
		if( check == true ){
			if( file.exists() ){
				Log.d( "FILE", "Open Error(" + path + ")" );
				//ファイルが取得できなかったのでNullを設定する
				file = null;
			}
		}

		return file;
	}

	/**
	 * @brief	ファイルを読み込む
	 * @param	file(in)		Openメソッドの戻り値で取得したFileオブジェクト
	 * @return	BufferedReaderオブジェクト
	 */
	public BufferedReader Read(File file){
		FileReader		reader	= null;
		BufferedReader	buffer	= null;
		try {
			//ファイルの読み込み
			reader	= new FileReader( file );
			buffer	= new BufferedReader( reader );
		} catch(FileNotFoundException e) {
			Log.d("FILE", "Read Error(" + file.getPath() + ")");
			e.printStackTrace();
		}

		return buffer;
	}

	/**
	 * @brief	ファイルを書き込む
	 * @param 	file(in)		Openメソッドの戻り値で取得したFileオブジェクト
	 * @param	data(in)		ファイルに出力する内容
	 * @param 	is_newline(in)	改行マークを強制的に書き込む(true=書き込む, false=書き込まない)
	 * @return	成否(0=正常, 0以外=エラー)
	 */
	public int Write(File file, String data, boolean is_newline){
		int			ret		= 0;
		FileWriter	writer	= null;

		try {
			writer = new FileWriter( file );
			//改行コードの付与判定
			if( is_newline == true ){
				//出力データの末尾が改行コードの場合は、改行コードを無視する
				if( !data.endsWith( System.getProperty( "line.separator" ) ) ) {
					//末尾に改行コードを追加する
					data += System.getProperty( "line.separator" );
				}
			}
			//データの出力
			writer.write( data );

			//出力し終わったらオブジェクトを解放する
			writer.close();
		} catch(IOException e) {
			Log.d( "FILE", "Write Error(" + file.getPath() + ")" );
			e.printStackTrace();
			ret	= 1;
		}

		return ret;
	}

	/**
	 * @brief	ファイルから特定の文字列を検索する
	 * @param	reader(in)	Openメソッドの戻り値で取得したFileオブジェクト
	 * @param	search(in)	検索する文字列
	 * @return	最初に該当するものが見つかった行数(0:見つからない, 1以上=行数)
	 */
	public int SearchRow( BufferedReader reader, String search ){
		int		ret		= 0;							//戻り値
		int		idx		= 1;							//行数カウンター
		String	data;									//読み込んだレコードの格納用変数
		try {
			//最初のデータを読み込む
			data	= reader.readLine();
			while( data != null ){
				//該当する文字列があるかチェックする
				if( data.indexOf( search ) > 0 ){
					//見つかった場合は、行数を設定する
					ret	= idx;
					break;
				}

				//次のデータを読み込む
				data	= reader.readLine();
				idx++;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}

		return	ret;
	}

	/**
	 * @brief	ファイルを閉じる
	 * @param	reader(in)		Openメソッドの戻り値で取得したFileオブジェクト
	 */
	public void Close( BufferedReader reader ){
		try {
			reader.close();
		} catch(IOException e) {
			Log.d( "FILE", "Close Error" );
			e.printStackTrace();
		}
	}

	/**
	 * ※番外
	 * @brief	ファイルの存在確認
	 * @param	file_name(in)		ファイルのフルパス
	 */
	@JavascriptInterface
	public boolean fileCheck( String file_name ){
		File file	= new File( file_name );
		Log.d( "FILE", file_name + " = " + file.length() );
		if( file.exists() ){
			return true;
		}
		return false;
	}

}
