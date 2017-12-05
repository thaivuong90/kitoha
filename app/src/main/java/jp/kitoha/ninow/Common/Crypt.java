package jp.kitoha.ninow.Common;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static android.R.attr.key;

/******************************************************************************
 * @brief		暗号／復号処理用クラス
 * @author 		KITOHA	N.Endo
 * @since		2015 -
 * @copyright	(c)KITOHA.co., ltd All right reserved.
 ******************************************************************************/
public class Crypt {
    /***************************************************************************
     * 定数
     ***************************************************************************/
    private static final String CRYPT_ALGORITHM = "des";			//暗号アルゴリズム
    private static final String CRYPT_MODE 		= "ecb";			//モード( "ecb", "cbc", "cfb", "ofb", "nofb" ,"stream" )
    private static final String MD_KEY 			= "vPmgErik";		//MD5 Key

    /***************************************************************************
     * インスタンス変数
     ***************************************************************************/
    private String			iv;                                     //IV
    private IvParameterSpec	ivspec;
    private DESKeySpec      keyspec;
//    private SecretKeySpec	keyspec;
    private Cipher			cipher;

    /***************************************************************************
     * メソッド
     ***************************************************************************/
    public String decrypt(String source){
/*
        Cipher  chipper  = null;
        try {
            //keyspec = new SecretKeySpec( MD_KEY.getBytes(), "AES" );
            //chipper = Cipher.getInstance( "AES/CBC/PKCS5Padding" );

            keyspec = new DESKeySpec( MD_KEY.getBytes() );
            cipher = Cipher.getInstance( "DES/ECB/PKCS7Padding", "BC" );

            // IVの読み込み
            byte[] iv = null;
            // Cipherオブジェクトに秘密鍵を設定
            if (chipper != null) {
                chipper.init( Cipher.DECRYPT_MODE, keyspec );
            }

            // 暗号化
            byte[] bytesDecoded = Base64.decode( source, 0 );
            byte[] protectedStr = chipper.doFinal( pStr.getBytes() );

            // 読み込んだバイトを使用できる形に変換
            IvParameterSpec ips = new IvParameterSpec(iv);
            // 秘密鍵を複合化モードで初期化
            chipper.init(Cipher.DECRYPT_MODE, key, ips);
            // 復号化
            byte[] decryptStr = chipper.doFinal(dStr);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
*/
        return "";
    }
}
