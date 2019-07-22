package net.cloudburo.kyber.tutorial.methods.response;

import java.util.List;

public class EnabledTokensForWallet extends KyberReponse<EnabledTokensForWallet>{

    private List<EnabledTokenStatus> data;

    public List<EnabledTokenStatus> getData() {
        return data;
    }

    public boolean isEnabled(String tokenId) {
        return data.stream()
                .filter(tokenStatus -> tokenId.equals(tokenStatus.getId()))
                .findAny()
                .orElse(null)
                !=null;
    }

    public EnabledTokenStatus getEnabledTokenStatus(String tokenId) {
        return data.stream()
         .filter(tokenStatus -> tokenId.equals(tokenStatus.getId()))
                .findAny()
                .orElse(null);
    }

    public void setData(List<EnabledTokenStatus> data) {
        this.data = data;
    }

    public static class EnabledTokenStatus {
        private String id;
        private boolean enabled;
        private int txs_required;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getTxs_required() {
            return txs_required;
        }

        public void setTxs_required(int txs_required) {
            this.txs_required = txs_required;
        }
    }
}
