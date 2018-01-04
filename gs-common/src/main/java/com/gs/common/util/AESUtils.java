package com.gs.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by linjuntan on 2017/12/3.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class AESUtils {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

    private static final String securityKey = "GAME_SERVER_123456";

    private static Cipher encrypt;

    private static Cipher decrypt;

    static {
        try {
            encrypt = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            encrypt.init(Cipher.ENCRYPT_MODE, getSecretKey(securityKey));

            decrypt = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            decrypt.init(Cipher.DECRYPT_MODE, getSecretKey(securityKey));
        } catch (Exception e) {
            log.error("[error] >>> init aes cipher error", e);
        }
    }

    public static byte[] encrypt(byte[] data) {
        try {
            return encrypt.doFinal(data);
        } catch (Exception ex) {
            log.error("[error] >>> encrypt message error", ex);
        }

        return null;
    }

    public static byte[] decrypt(byte[] data) {

        try {
            return decrypt.doFinal(data);
        } catch (Exception ex) {
            log.error("[error] >>> decrypt message error", ex);
        }

        return null;
    }

    private static SecretKeySpec getSecretKey(final String password) {
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);

            kg.init(128, new SecureRandom(password.getBytes()));

            SecretKey secretKey = kg.generateKey();

            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            log.error("[error] >>> generate secretKey error", ex);
        }

        return null;
    }
}

