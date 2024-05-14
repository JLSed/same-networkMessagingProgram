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
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encryption {
    public static String secretKey = "hellohellohellohellohelo"; 

    public static byte[] Encrypt(String message, String Key) throws Exception {
        Key key = new SecretKeySpec(Key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(message.getBytes());
    }

    public static String Decrypt(byte[] encrypted, String Key) throws Exception {
        Key key = new SecretKeySpec(Key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(encrypted));
    }

    // generates 128 bits random key for encrypting and decrypting
    public static String GenerateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

}
