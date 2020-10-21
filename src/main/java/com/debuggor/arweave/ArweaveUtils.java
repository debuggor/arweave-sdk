package com.debuggor.arweave;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: yong.huang
 * @DATE: 2020/10/21 18:35
 */
public class ArweaveUtils {


    public static String b64UrlEncode(String b64UrlString) {
        b64UrlString = b64UrlString.replace("+", "-");
        b64UrlString = b64UrlString.replace("/", "_");
        b64UrlString = b64UrlString.replace("=", "");
        return b64UrlString;
    }

    public static String b64UrlDecode(String b64UrlString) {
        b64UrlString = b64UrlString.replace("-", "+").replace("_", "/");
        int padding = b64UrlString.length() % 4 == 0 ? 0 : (4 - (b64UrlString.length() % 4));
        String pad = "";
        for (int i = 0; i < padding; i++) {
            pad = pad.concat("=");
        }
        return b64UrlString.concat(pad);
    }

    public static byte[] sha256(byte[] bytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(bytes);
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 去掉byte[] 开头的0
     *
     * @param bytes
     * @return
     */
    public static byte[] removeZeroHead(byte[] bytes) {
        if (bytes.length > 0 && bytes[0] == 0) {
            int i = 0;
            for (; i < bytes.length && bytes[i] == 0; i++) {
            }
            byte[] newBytes = new byte[bytes.length - i];
            System.arraycopy(bytes, i, newBytes, 0, newBytes.length);
            bytes = newBytes;
        }
        return bytes;
    }


}
