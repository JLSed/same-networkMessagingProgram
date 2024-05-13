/*
 Made by
 Gambling Addicts Gathering Offcials of STI Nova BS201
 and
 Hygienic Power Rangers Novaliches Proper


 Member and Contributor being:

 John Lawrence Sedillo - Main Programmer

 https://github.com/JLSed/same-networkMessagingProgram
 */

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class Encryption {

    public static byte[] Encrypt(String message, String Key) throws Exception {
        Key key = new SecretKeySpec(Key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS/padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(message.getBytes());
    }

    public static String Decrypt(byte[] encrypted, String Key) throws Exception {
        Key key = new SecretKeySpec(Key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS/padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(encrypted));
    }

}
