package com.debuggor.arweave;

/**
 * @Author: yong.huang
 * @DATE: 2020/10/21 18:38
 */
public class AddressTest {

    public static void main(String[] args) throws Exception {
        String seed = "01c4f3d1377cc4db62cc30b03af5f5528f50d2ca4fec75cd54999b1429514331";

        ArEcKey arEcKey = new ArEcKey(seed);
        String address = arEcKey.toAddress();
        String privateKey = arEcKey.getBase64PrivateKey();

        System.out.println("地址：" + address);
        System.out.println("d: " );
        System.out.println("d: " );

    }
}
