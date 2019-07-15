package net.cloudburo.kyber.tutorial.protocol;

import net.cloudburo.kyber.tutorial.methods.response.*;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.utils.Async;

import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

public class JsonRpc2_0Kyber implements Kyber3j {
    public static final int DEFAULT_BLOCK_TIME = 15 * 1000;

    protected final Web3jService web3jService;
    private final long blockTime;
    //private final JsonRpc2_0Rx web3jRx;
    private final ScheduledExecutorService scheduledExecutorService;

    public JsonRpc2_0Kyber(Web3jService web3jService) {
        this(web3jService, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

    public JsonRpc2_0Kyber(
            Web3jService web3jService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        this.web3jService = web3jService;
        // this.web3jRx = new JsonRpc2_0Rx(this, scheduledExecutorService);
        this.blockTime = pollingInterval;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public Request<?, Currencies> currencies() {
        return new Request<>(
                "currencies",
                Collections.<String>emptyList(),
                web3jService,
                Currencies.class);
    }

    @Override
    public void shutdown() {

    }
}