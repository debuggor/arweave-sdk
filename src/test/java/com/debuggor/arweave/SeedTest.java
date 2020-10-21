package com.debuggor.arweave;

import org.spongycastle.util.encoders.Hex;

import java.security.SecureRandom;

/**
 * @Author: yong.huang
 * @DATE: 2020/10/21 19:07
 */
public class SeedTest {

    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        System.out.println("seed: " + Hex.toHexString(bytes));
    }

}
