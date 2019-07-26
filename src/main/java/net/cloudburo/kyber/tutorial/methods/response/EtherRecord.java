package net.cloudburo.kyber.tutorial.methods.response;

import org.bouncycastle.util.test.FixedSecureRandom;

import java.math.BigInteger;

public class EtherRecord {
    private String from;
    private String to;
    private String data;
    private String value;
    private String gasPrice;
    private String nonce;
    private String gasLimit;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getValue() {
        return value;
    }

    public BigInteger getValueAsBI() { return new BigInteger( value.substring(2),16);}

    public void setValue(String value) {
        this.value = value;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public BigInteger getGasPriceAsBI() { return new BigInteger( gasPrice.substring(2),16);}

    public void setGasPrice(String gasPrice) { this.gasPrice = gasPrice; }

    public String getNonce() { return nonce; }

    public BigInteger getNonceAsBI() { return new BigInteger( nonce.substring(2),16);}

    public void setNonce(String nonce) { this.nonce = nonce; }

    public void incrementNonceByOne() {
        BigInteger bi = getNonceAsBI();
        bi.add(BigInteger.valueOf(1));
        setNonce("0x"+bi.toString(16));
    }

    public String getGasLimit() { return gasLimit;
    }
    public BigInteger getGasLimitAsBI() { return new BigInteger( gasLimit.substring(2),16);}

    public void setGasLimit(String gasLimit) { this.gasLimit = gasLimit; }
}
