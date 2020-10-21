package com.debuggor.arweave;

import com.alibaba.fastjson.JSONObject;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.params.RSAKeyParameters;
import org.spongycastle.crypto.params.RSAPrivateCrtKeyParameters;

import java.math.BigInteger;
import java.util.Base64;

/**
 * @Author: yong.huang
 * @DATE: 2020/10/21 18:38
 */
public class AddressTest {

    public static void main(String[] args) throws Exception {
        String seed = "01c4f3d1377cc4db62cc30b03af5f5528f50d2ca4fec75cd54999b1429514331";
        ArEcKey arEcKey = new ArEcKey(seed);
        String address = arEcKey.toAddress();
        System.out.println("address: " + address);

        AsymmetricCipherKeyPair keyPair = arEcKey.getKeyPair();
        RSAKeyParameters aPublic = (RSAKeyParameters) keyPair.getPublic();
        RSAPrivateCrtKeyParameters aPrivate = (RSAPrivateCrtKeyParameters) keyPair.getPrivate();
        BigInteger aPrivateQ = aPrivate.getExponent();
        BigInteger modulus = aPublic.getModulus();

        Base64.Encoder encoder = Base64.getEncoder();
        String pubkey = encoder.encodeToString(modulus.toByteArray());
        String privkey = encoder.encodeToString(aPrivateQ.toByteArray());

        JSONObject object = new JSONObject();
        object.put("d", ArweaveUtils.b64UrlEncode(privkey));
        object.put("n", ArweaveUtils.b64UrlEncode(pubkey));
        object.put("kty", "RSA");
        object.put("e", "AQAB");
        System.out.println(object);
    }
}
