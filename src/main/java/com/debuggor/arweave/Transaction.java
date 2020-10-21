package com.debuggor.arweave;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @Author: yong.huang
 * @DATE: 2020/10/21 18:35
 */
public class Transaction {

    String id;

    String last_tx;
    /**
     * from-n; RSA pubKey
     */
    String owner;
    String[] tags;
    /**
     * to
     */
    String target;
    /**
     * amount
     */
    String quantity;
    String data;
    /**
     * fee
     */
    String reward;

    String signature;

    /**
     * @param fromPubKey
     * @param toAddress
     * @param amountWinston 金额 最小单位
     * @param feeWinston    手续费 最小单位
     * @param lastTx        从链上获取的一个字段
     * @param data          "" 转账交易
     * @param tags          暂不清楚这个字段具体含义，不包含进交易
     */
    public Transaction(String fromPubKey, String toAddress, String amountWinston, String feeWinston, String lastTx, String data, String[] tags) {
        this.owner = fromPubKey;
        this.target = toAddress;
        this.quantity = amountWinston;
        this.reward = feeWinston;
        this.last_tx = lastTx;
        // 转账交易非必须字段
        this.data = data;
        this.tags = tags;
    }

    public void signTX(String seed) throws Exception {
        ArEcKey arEcKey = new ArEcKey(seed);
        String base64PrivateKey = arEcKey.getBase64PrivateKey();
        String base64PublicKey = arEcKey.getBase64PublicKey();
        byte[] unSignedSerialize = unSignedSerialize(base64PublicKey);
        byte[] signature = ArEcKey.signSha256withRSA(unSignedSerialize, base64PrivateKey);
        byte[] txIdBytes = ArweaveUtils.sha256(signature);

        Base64.Encoder encoder = Base64.getEncoder();
        this.id = ArweaveUtils.b64UrlEncode(encoder.encodeToString(txIdBytes));
        this.signature = ArweaveUtils.b64UrlEncode(encoder.encodeToString(signature));
    }

    public String getId() {
        return id;
    }

    public String getSignature() {
        return signature;
    }

    public String getOwner() {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] ownerBytes = decoder.decode(ArweaveUtils.b64UrlDecode(owner));
        ownerBytes = ArweaveUtils.removeZeroHead(ownerBytes);
        String b64UrlEncode = ArweaveUtils.b64UrlEncode(Base64.getEncoder().encodeToString(ownerBytes));
        return b64UrlEncode;
    }

    public byte[] unSignedSerialize(String base64PublicKey) throws IOException {
        if (this.owner == null) {
            this.owner = base64PublicKey;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] lastTxBytes = decoder.decode(ArweaveUtils.b64UrlDecode(this.last_tx));
        byte[] toBytes = decoder.decode(ArweaveUtils.b64UrlDecode(this.target));
        byte[] ownerBytes = decoder.decode(ArweaveUtils.b64UrlDecode(this.owner));
        byte[] quantityBytes = this.quantity.getBytes();
        byte[] rewardBytes = this.reward.getBytes();
        byte[] dataBytes = this.data.getBytes();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(ArweaveUtils.removeZeroHead(ownerBytes));
        out.write(ArweaveUtils.removeZeroHead(toBytes));
        // 转账交易 data为空
        out.write(ArweaveUtils.removeZeroHead(dataBytes));
        out.write(ArweaveUtils.removeZeroHead(quantityBytes));
        out.write(ArweaveUtils.removeZeroHead(rewardBytes));
        out.write(ArweaveUtils.removeZeroHead(lastTxBytes));
        return out.toByteArray();
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.getId());
        jsonObject.put("last_tx", this.last_tx);
        jsonObject.put("owner", this.getOwner());
        jsonObject.put("target", this.target);
        jsonObject.put("quantity", this.quantity);
        jsonObject.put("reward", this.reward);
        jsonObject.put("signature", this.signature);
        jsonObject.put("data", this.data);
        return jsonObject;
    }

}
