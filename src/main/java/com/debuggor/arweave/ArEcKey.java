package com.debuggor.arweave;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.pkcs.PrivateKeyInfo;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.generators.RSAKeyPairGenerator;
import org.spongycastle.crypto.params.AsymmetricKeyParameter;
import org.spongycastle.crypto.params.RSAKeyGenerationParameters;
import org.spongycastle.crypto.params.RSAKeyParameters;
import org.spongycastle.crypto.util.PrivateKeyInfoFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * @Author: yong.huang
 * @DATE: 2020/10/21 18:34
 */
public class ArEcKey {

    // e 通常选择一个固定的值 2^16+1
    public int publicExponent = 65537;

    // RSA-4096
    public int modulusLength = 4096;

    AsymmetricCipherKeyPair keyPair;

    /**
     * 根据seed，得到RSA的公私钥对
     *
     * @param seed
     */
    public ArEcKey(String seed) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(Hex.decode(seed));
        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        RSAKeyGenerationParameters rsaKeyGenerationParameters = new RSAKeyGenerationParameters(BigInteger.valueOf(publicExponent), secureRandom, modulusLength, 0);
        rsaKeyPairGenerator.init(rsaKeyGenerationParameters);
        this.keyPair = rsaKeyPairGenerator.generateKeyPair();
    }

    public AsymmetricCipherKeyPair getKeyPair() {
        return keyPair;
    }

    public String toAddress() {
        RSAKeyParameters aPublic = (RSAKeyParameters) keyPair.getPublic();
        BigInteger modulus = aPublic.getModulus();

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] hash = ArweaveUtils.sha256(Hex.decode(modulus.toString(16)));
        String address = ArweaveUtils.b64UrlEncode(encoder.encodeToString(hash));
        return address;
    }

    public String getBase64PrivateKey() throws IOException {
        AsymmetricKeyParameter privateKey = keyPair.getPrivate();
        PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo(privateKey);
        ASN1Object asn1ObjectPrivate = privateKeyInfo.toASN1Primitive();
        byte[] privateInfoByte = asn1ObjectPrivate.getEncoded();
        final Base64.Encoder encoder64 = Base64.getEncoder();
        return encoder64.encodeToString(privateInfoByte);
    }

    public String getBase64PublicKey() {
        RSAKeyParameters aPublic = (RSAKeyParameters) keyPair.getPublic();
        BigInteger modulus = aPublic.getModulus();
        Base64.Encoder encoder = Base64.getEncoder();
        String pubkey = encoder.encodeToString(Hex.decode(modulus.toString(16)));
        return ArweaveUtils.b64UrlEncode(pubkey);
    }

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SHA256withRSA签名
     *
     * @param privateKeyStr 私钥
     * @param plainBytes    明文 byte[]
     * @return
     */
    public static byte[] signSha256withRSA(byte[] plainBytes, String privateKeyStr) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA/PSS", "BC");
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        sign.initSign(privateKey);
        sign.update(plainBytes);
        byte[] signed = sign.sign();
        return signed;
    }

    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

}
