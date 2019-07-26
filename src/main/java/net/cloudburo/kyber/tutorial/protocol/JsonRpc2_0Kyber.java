package net.cloudburo.kyber.tutorial.protocol;

import net.cloudburo.kyber.tutorial.methods.request.GasPriceRange;
import net.cloudburo.kyber.tutorial.methods.request.SingleRate;
import net.cloudburo.kyber.tutorial.methods.response.*;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.utils.Async;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class JsonRpc2_0Kyber implements Kyber3j {
    public static final int DEFAULT_BLOCK_TIME = 15 * 1000;


    protected final Web3jService web3jService;
    private final long blockTime;
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
        List<Param> params = List.of(new Param("op",Param.OPS_GET));
        return new Request<>(
                "currencies",
                params,
                web3jService,
                Currencies.class);
    }

    public Request<?, BuyRate> buyRate(String id, String qty,boolean onlyOfficialReserve) {
        List<Param> params = List.of(
                new Param("op",Param.OPS_GET),
                new Param("id",id),new Param("qty",qty),
                new Param("only_official_reserve", Boolean.valueOf(onlyOfficialReserve).toString()));
        return new Request<>(
                "buy_rate",
                params,
                web3jService,
                BuyRate.class);
    }

    public Request<?, SellRate> sellRate(String id, String qty, boolean onlyOfficialReserve) {
        List<Param> params = List.of(
                new Param("op",Param.OPS_GET),
                new Param("id",id),new Param("qty",qty),
                new Param("only_official_reserve", Boolean.valueOf(onlyOfficialReserve).toString()));
        return new Request<>(
                "sell_rate",
                params,
                web3jService,
                SellRate.class);
    }

    public  Request<?,TradeData> tradeData(String userAddress, String srcId, String dstId, Float srcQty, Float minDstQty,
                                           GasPriceRange gasPrice, String walletId, boolean onlyOfficialReserve) {
        List<Param> params = List.of(
                new Param("op",Param.OPS_GET),new Param("user_address",userAddress),
                new Param("src_id",srcId), new Param("dst_id",dstId), new Param("src_qty",srcQty.toString()),
                new Param("min_dst_qty",minDstQty.toString()),new Param("gas_price",gasPrice.name()),
                new Param("wallet_id",walletId),
                new Param( "only_official_reserve", Boolean.valueOf(onlyOfficialReserve).toString()));
        return new Request<>(
                "trade_data",
                params,
                web3jService,
                TradeData.class);
    }

    public  Request<?,TradeData> tradeData(String userAddress, SingleRate rate, GasPriceRange gasPrice) {
        List<Param> params = List.of(
                new Param("op",Param.OPS_GET),new Param("user_address",userAddress),
                new Param("src_id", rate.getSrc_id()), new Param("dst_id", rate.getDst_id()),
                new Param("src_qty", rate.getSrc_qty().toString()),  new Param("min_dst_qty", rate.getDst_qty().toString()),
                new Param("gas_price",gasPrice.name()));
        return new Request<>(
                "trade_data",
                params,
                web3jService,
                TradeData.class);
    }

    public  Request<?,TradeData> tradeData(String userAddress, SingleRate rate, GasPriceRange gasPrice, BigInteger nonce) {
        List<Param> params = List.of(
                new Param("op",Param.OPS_GET),new Param("user_address",userAddress),
                new Param("src_id", rate.getSrc_id()), new Param("dst_id", rate.getDst_id()),
                new Param("src_qty", rate.getSrc_qty().toString()),  new Param("min_dst_qty", rate.getDst_qty().toString()),
                new Param("gas_price",gasPrice.name()),
                new Param("nonce",nonce.toString()));
        return new Request<>(
                "trade_data",
                params,
                web3jService,
                TradeData.class);
    }


    public Request<?,EnabledTokensForWallet> enabledTokensForWallet(String userAddress) {
        List<Param> params = List.of(
                new Param("op",Param.OPS_GET));
        return new Request<>(
                "users/"+userAddress+"/currencies",
                params,
                web3jService,
                EnabledTokensForWallet.class);
    }

    public Request<?, EnableTokenTransfer> enableTokenTransfer(String userAddress, String id, GasPriceRange gasPrice) {
        List<Param> params = List.of(
                new Param("op",Param.OPS_GET),
                new Param("gas_price",gasPrice.name()));
        return new Request<>(
                "users/"+userAddress+"/currencies/"+id+"/enable_data",
                params,
                web3jService,
                EnableTokenTransfer.class);
    }

    @Override
    public void shutdown() { }
}
