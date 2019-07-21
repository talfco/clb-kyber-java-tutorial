package net.cloudburo.kyber.tutorial.methods.response;

import org.web3j.protocol.core.Response;

public class TradeData extends KyberReponse<TradeData> {

    private TradeDataRecord data;

    public TradeDataRecord getData() {
        return data;
    }

    public void setData(TradeDataRecord data) {
        this.data = data;
    }

    public class TradeDataRecord {
        private String from;
        private String to;
        private String data;
        private String value;
        private String gasPrice;
        private String nonce;
        private String gasLimit;

        private boolean onlyOfficialReserve;

        public TradeData getTradeData() {
            return getResult();
        }

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

        public void setValue(String value) {
            this.value = value;
        }

        public String getGasPrice() {
            return gasPrice;
        }

        public void setGasPrice(String gasPrice) {
            this.gasPrice = gasPrice;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getGasLimit() {
            return gasLimit;
        }

        public void setGasLimit(String gasLimit) {
            this.gasLimit = gasLimit;
        }

        public boolean isOnlyOfficialReserve() {
            return onlyOfficialReserve;
        }

        public void setOnlyOfficialReserve(boolean onlyOfficialReserve) {
            this.onlyOfficialReserve = onlyOfficialReserve;
        }
    }

}
