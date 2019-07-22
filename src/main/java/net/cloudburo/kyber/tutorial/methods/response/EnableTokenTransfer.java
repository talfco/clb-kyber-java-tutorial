package net.cloudburo.kyber.tutorial.methods.response;

public class EnableTokenTransfer extends KyberReponse<EnableTokenTransfer>{

    public EtherRecord getData() {
        return data;
    }

    public void setData(EtherRecord data) {
        this.data = data;
    }

    public EtherRecord data;


}
