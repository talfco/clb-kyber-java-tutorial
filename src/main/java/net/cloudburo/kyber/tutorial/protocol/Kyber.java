package net.cloudburo.kyber.tutorial.protocol;

import net.cloudburo.kyber.tutorial.methods.response.*;

import org.web3j.protocol.core.Request;

public interface Kyber {

    public Request<?, Currencies> currencies();
    public Request<?, BuyRate> buyRate(String id, String qty,boolean onlyOfficialReserve);
}
