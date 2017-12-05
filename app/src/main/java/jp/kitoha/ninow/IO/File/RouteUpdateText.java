package jp.kitoha.ninow.IO.File;

import android.os.Environment;
import android.webkit.JavascriptInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import jp.kitoha.ninow.Common.Constants;
import jp.kitoha.ninow.Common.ErrorCode;

/******************************************************************************
 * @brief		更新情報保存クラス
 * @note		配送指示の更新情報を保持する
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class RouteUpdateText {
	/**************************************************************************
	 * シングルトン用コード
	 **************************************************************************/
	public static RouteUpdateText instance = new RouteUpdateText();

	/**************************************************************************
	 * インスタンス変数(プロパティ)
	 **************************************************************************/
	//アプリケーション情報
	public String update_text;									//アプリケーションコード
	public String filePath;										//バージョン

	/****************************************************************
	 * メソッド
	 ****************************************************************/
	/**
	 * @brief	コンストラクタ
	 */
	public RouteUpdateText(){
		//更新テキストの初期化
		update_text = "";
		filePath = "";
	}

	/**
	 * @brief	インスタンスの取得
	 * @return	インスタンス
	 */
	public static RouteUpdateText getInstance(){
		return instance;
	}

	/**
	 * @brief	ファイルの読込
	 * @return	成否
	 */
	@JavascriptInterface
	public String readFileInfo( String filePath){
		File environment		= new File( filePath );
		update_text = "";
		/*
		* DBからファイルパスをもらってくること
		 */
		File file				= new File(String.valueOf(environment));

		//ファイルが存在する場合のみ実施
		if( environment.exists() == true ){
			try {
				FileReader fileReader = new FileReader(file);
				BufferedReader br = new BufferedReader(fileReader);
				String line;
				String temp_string;
				//保存ファイルを読み、復元する
				while( ( line = br.readLine() ) != null ){
					update_text		+= line + Constants.NEW_LINE;
				}
				br.close();
				fileReader.close();
			} catch (FileNotFoundException e) {
				//ファイルの存在チェックを行っているので、ここに入るはずがない
				e.printStackTrace();
				//return ErrorCode.STS_NG;
				return "更新情報ファイルが存在しません。";
			} catch (IOException e) {
				e.printStackTrace();
				//return ErrorCode.STS_NG;
				return "更新情報ファイルの読み込みに失敗しました。";
			}
		}

		return update_text;
	}

	/**
	 * @brief	更新情報の保存
	 * @return	成否
	 */
	public String saveTextFile( String update_text, String date ){
		File environment	= new File( Environment.getExternalStorageDirectory(), Constants.APP_NAME );
		File file			= new File( environment, date + Constants.UPDATE_FILE );
		BufferedWriter bw	= null;

		if( file.exists() == false ){												//ファイルの存在確認
			try{
				if( environment.exists() == false ){								//ディレクトリの存在確認
					environment.mkdir();											//ない場合はディレクトリの作成
				}
				file.createNewFile();												//ファイルの作成
			}catch( IOException e ){
				e.printStackTrace();
			}
		}

		try {
			bw	= new BufferedWriter( new FileWriter( file ) );
			bw.write( update_text );
			bw.newLine();
			if( bw != null ){
				bw.flush();
				bw.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}

		return file.getAbsolutePath();
	}

	/**
	 * @brief	ファイルの削除
	 * @return	成否
	 */
	public static int fileDelete(String filePath){
		File deleteFile	= new File( filePath );											//削除対象パスのファイル読込
		deleteFile.delete();															//ファイルの削除
		deleteFile	= null;
		return ErrorCode.STS_OK;
	}

}
