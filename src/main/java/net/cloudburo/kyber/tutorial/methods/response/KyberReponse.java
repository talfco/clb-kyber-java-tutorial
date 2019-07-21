package net.cloudburo.kyber.tutorial.methods.response;

import org.web3j.protocol.core.Response;

public class KyberReponse<T> extends Response<T> {

    private boolean error;
    private String reason;
    private String additional_data;
    private Error web3jError;

    private Error web3jError() {
        if (web3jError == null)  web3jError =  new Error();
        return web3jError;
    }

    public Error getWeb3jError() {
        return web3jError;
    }

    public void setError(boolean error) {
        if (error) {
            this.error = error;
            web3jError();
        }
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
        String msg = web3jError().getMessage();
        msg += "Reason: "+reason+"\n";
        web3jError().setMessage(msg);
    }

    public String getAdditional_data() {
        return additional_data;
    }

    public void setAdditional_data(String additional_data) {
        this.additional_data = additional_data;
        String msg = web3jError().getMessage();
        msg += "Additional Data: "+additional_data;
        web3jError().setMessage(msg);
    }
}
